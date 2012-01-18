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

public class MembershipProvider extends SortableDataProvider<Membership> implements
		Serializable {

	private static final long serialVersionUID = 8746292597567302072L;
	
	@Inject private EntityManager em;
	
	
	@SuppressWarnings("unchecked")
	public Iterator<Membership> iterator(int first, int count) {
		List<Membership> memberships = em.createQuery("FROM Membership").getResultList();
		Collections.sort(memberships, new Comparator<Membership>() {
			public int compare(Membership m1, Membership m2) {
				int dir = getSort().isAscending() ? 1 : -1;

				if ("firstname".equals(getSort().getProperty())) {
					String m1Val = m1.isPrivateMembership() ? m1.getUsers().get(0).getFirstname() : null;
					String m2Val = m2.isPrivateMembership() ? m2.getUsers().get(0).getFirstname() : null;
					return dir * (compareStrings(m1Val, m2Val));
				} else if ("lastname".equals(getSort().getProperty())) {
					String m1Val = m1.isPrivateMembership() ? m1.getUsers().get(0).getLastname() : null;
					String m2Val = m2.isPrivateMembership() ? m2.getUsers().get(0).getLastname() : null;
					return dir * (compareStrings(m1Val, m2Val));
				} else if ("companyName".equals(getSort().getProperty())) {
					return dir * (compareStrings(m1.getCompanyName(), m2.getCompanyName()));
				} else if ("type".equals(getSort().getProperty())) {
					return dir * (m1.getType().compareTo(m2.getType()));
				} else {
					if (m1.getId() > m2.getId())
						return dir;
					else
						return -dir;
				}
			}
			private int compareStrings(String a, String b) {
				if ((a == null) && (b == null)) {
					return 0;
				}
				if (a != null) {
					return a.compareTo(b);
				} else {
					return b.compareTo(a);
				}
			}
		});
		
		return memberships.subList(first, Math.min(first+count, memberships.size())).iterator();
		
	}
	
	public Iterator<Membership> getMembersWithEntries() {
		List<Membership> memberships = em.createQuery("SELECT distinct m FROM ConsumationEntry as entry join entry.consumedBy as m WHERE invoice_id is NULL").getResultList();
		return memberships.iterator();
	}

	public IModel<Membership> model(final Membership object) {
		return new LoadableDetachableModel<Membership>() {
			private static final long serialVersionUID = 2245677208590656096L;

			protected Membership load() {
				return object;
			}
		};
	}

	public int size() {
		Long count=(Long) em.createQuery("select count(*) from Membership").getSingleResult();
		return count.intValue();
	}

}
