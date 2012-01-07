package at.happylab.fablabtool.dataprovider;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.Subscription;

public class SubscriptionProvider extends SortableDataProvider<Subscription> implements Serializable {

	private static final long serialVersionUID = 8746292597567302072L;

	@Inject
	private EntityManager em;

	private Membership member;
	private boolean showCancelledSubscriptions;

	public SubscriptionProvider() {
		this.member = null;
		this.showCancelledSubscriptions = false;

		setSort("validFrom", true);
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

		List<Subscription> results = em.createQuery(sqlString).getResultList();

		Collections.sort(results, new Comparator<Subscription>() {
			public int compare(Subscription s1, Subscription s2) {
				int dir = getSort().isAscending() ? 1 : -1;

				if ("validFrom".equals(getSort().getProperty())) {
					if (s1.getValidFrom() == null) {
						if (s2.getValidFrom() == null) {
							return 0;
						} else
							return dir;
					}
					else{
						if (s2.getValidFrom() == null) {
							return 0;
						} else
							return dir * (s1.getValidFrom().compareTo(s2.getValidFrom()));
					}
				} else if ("validTo".equals(getSort().getProperty())) {

					if (s1.getValidTo() == null) {
						if (s2.getValidTo() == null) {
							return 0;
						} else
							return dir;
					}
					else{
						if (s2.getValidTo() == null) {
							return 0;
						} else
							return dir * (s1.getValidTo().compareTo(s2.getValidTo()));
					}
				} else if ("payedUntil".equals(getSort().getProperty())) {

					if (s1.getValidTo() == null) {
						if (s2.getPayedUntil() == null) {
							return 0;
						} else
							return dir;
					}
					else{
						if (s2.getPayedUntil() == null) {
							return 0;
						} else
							return dir * (s1.getPayedUntil().compareTo(s2.getPayedUntil()));
					}
				} else if ("priceOverruled".equals(getSort().getProperty())) {
					return dir * (s1.getPriceOverruled().compareTo(s2.getPriceOverruled()));
				} else if ("firstname".equals(getSort().getProperty())) {
					return dir * (s1.getBookedBy().getUsers().get(0).getFirstname().compareTo(s2.getBookedBy().getUsers().get(0).getFirstname()));
				} else if ("lastname".equals(getSort().getProperty())) {
					return dir * (s1.getBookedBy().getUsers().get(0).getLastname().compareTo(s2.getBookedBy().getUsers().get(0).getLastname()));
				} else if ("companyname".equals(getSort().getProperty())) {
					return dir * (s1.getBookedBy().getCompanyName().compareTo(s2.getBookedBy().getCompanyName()));
				} else if ("Package.name".equals(getSort().getProperty())) {
					return dir * (s1.getBooksPackage().getName().compareTo(s2.getBooksPackage().getName()));
				} else {
					if (s1.getId() > s2.getId())
						return dir;
					else
						return -dir;
				}
			}
		});

		return results.subList(first, Math.min(first+count, results.size())).iterator();

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
