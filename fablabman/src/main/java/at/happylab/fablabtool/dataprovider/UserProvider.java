/**
 * 
 */
package at.happylab.fablabtool.dataprovider;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import at.happylab.fablabtool.dataprovider.utils.FilterExpressionBuilder;
import at.happylab.fablabtool.dataprovider.utils.FilteredDateField;
import at.happylab.fablabtool.dataprovider.utils.SortableDataProviderComparator;
import at.happylab.fablabtool.model.User;

/**
 * @author Michael Kraxner
 *
 */
public class UserProvider extends SortableDataProvider<User> implements
		Serializable {
	private static final long serialVersionUID = 1L;
	
	@Inject private EntityManager em;
	
	private IModel<String> filterModel = new Model<String>("");
	private IModel<Boolean> showPreRegistrations = new Model<Boolean>(Boolean.FALSE);
	private IModel<Boolean> showInactiveMamberships = new Model<Boolean>(Boolean.FALSE);
	
	private String filterExpression;
	
	public UserProvider(){
		setSort("id", true);

		FilterExpressionBuilder filterBuilder = new FilterExpressionBuilder();
		filterBuilder.addFilteredTextField("firstname");
		filterBuilder.addFilteredTextField("lastname");
		filterBuilder.addFilteredTextField("membership.companyName");
		filterBuilder.addFilteredTextField("membership.companyPhone");
		filterBuilder.addFilteredTextField("membership.companyEmail");
		filterBuilder.addFilteredTextField("mobile");
		filterBuilder.addFilteredTextField("phone");
		filterBuilder.addFilteredTextField("email");
		filterBuilder.addFilteredTextField("membership.comment");
		filterBuilder.add(new FilteredDateField("membership.entryDate"));
		filterBuilder.add(new FilteredDateField("membership.leavingDate"));
		filterExpression = filterBuilder.getSQLFilterExpression();
	}
	
	
	@SuppressWarnings("unchecked")
	public Iterator<? extends User>iterator(int first, int count) {
		// would be nice to let the db do the work, but some fields (phone number,...) 
		// we read either from membership or the first user... thats not possible 
//		String orderBy = getSort().getProperty() + " " + (getSort().isAscending()?" ASC " : " DESC ");
//		List<User> results = em.createQuery("FROM User order by " + orderBy).getResultList();
		
		String filter = filterModel.getObject() == null? "" : filterModel.getObject();

		String filterexpr = "";
		if (!filter.isEmpty()) {
			if (!filterExpression.isEmpty()) {
				filterexpr = " and " +  String.format(filterExpression, "%" + filter + "%");
			}
		}
		String condition = " where (membership.confirmed=:confirmed) ";
		if (showInactiveMamberships.getObject()) {
			condition = condition + " and (membership.leavingDate < :today) ";
		} else {
			condition = condition + " and (membership.leavingDate is null or membership.leavingDate >= :today) ";
		}
		condition = condition + filterexpr;
		Query query = em.createQuery("FROM User " + condition);
		query.setParameter("confirmed", !showPreRegistrations.getObject());
		query.setParameter("today", new Date());
		
		List<User> results = query.getResultList();
		Collections.sort(results , new SortableDataProviderComparator<User>(getSort()));
				
		return results.subList(first, Math.min(first+count, results.size())).iterator();
	}

	public int size() {
		Long count=(Long) em.createQuery("select count(*) from User").getSingleResult();
		return count.intValue();
	}

	public IModel<User> model(final User object) {
		return new LoadableDetachableModel<User>() {
			private static final long serialVersionUID = 2245677208590656096L;
			protected User load() {
				return object;
			}
		};
	}

	public IModel<String> getFilterModel() {
		return filterModel;
	}


	public IModel<Boolean> getShowPreRegistrations() {
		return showPreRegistrations;
	}


	public IModel<Boolean> getShowInactiveMamberships() {
		return showInactiveMamberships;
	}
}
