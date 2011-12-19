package at.happylab.fablabtool.dataprovider;

import java.io.Serializable;
import java.util.Iterator;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.Subscription;

public class SubscriptionProvider extends SortableDataProvider<Subscription>
		implements Serializable {

	private static final long serialVersionUID = 8746292597567302072L;

	@Inject
	private EntityManager em;

	private Membership member;
	private boolean showCancelledSubscriptions;

	public SubscriptionProvider() {
		this.member = null;
		this.showCancelledSubscriptions = false;
	}

	public void setMember(Membership member) {
		this.member = member;
	}

	public void setShowCancelledSubscriptions(boolean showCancelledSubscriptions) {
		this.showCancelledSubscriptions = showCancelledSubscriptions;
	}

	@SuppressWarnings("unchecked")
	public Iterator<Subscription> iterator(int first, int count) {

		String sqlString = "FROM Subscription WHERE 1=1";

		if (!this.showCancelledSubscriptions)
			sqlString += " AND ((ValidTo is null)  OR (DATEDIFF('dd', ValidTo, CURRENT_DATE) < 0))";

		if (member != null)
			sqlString += " AND bookedby_id = " + member.getId();

		return em.createQuery(sqlString).setFirstResult(first)
				.setMaxResults(count).getResultList().iterator();

	}

	public IModel<Subscription> model(final Subscription object) {
		return new LoadableDetachableModel<Subscription>() {
			private static final long serialVersionUID = 2245677208590656096L;

			protected Subscription load() {
				return object;
			}
		};
	}

	public int size() {
		Long count = 0L;

		String sqlString = "SELECT count (*) FROM Subscription WHERE 1=1";

		if (!this.showCancelledSubscriptions)
			sqlString += " AND ((ValidTo is null)  OR (DATEDIFF('dd', ValidTo, CURRENT_DATE) < 0))";

		if (member != null)
			sqlString += " AND bookedby_id = " + member.getId();


		count = (Long) em.createQuery(sqlString).getSingleResult();

		return count.intValue();
	}

}
