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

public class SubscriptionProvider extends
		SortableDataProvider<Subscription> implements Serializable {

	private static final long serialVersionUID = 8746292597567302072L;

	@Inject
	private EntityManager em;
	
	private Membership member;
	
	
	public SubscriptionProvider()
	{
		this.member=null;
	}
	
	public void setMember(Membership member)
	{
		this.member=member;
	}

	@SuppressWarnings("unchecked")
	public Iterator<Subscription> iterator(int first, int count) {
		if(member == null)
			return em.createQuery("FROM Subscription").setFirstResult(first)
					.setMaxResults(count).getResultList().iterator();
		else
			return em.createQuery("FROM Subscription WHERE bookedby_id = " + member.getId()).setFirstResult(first)
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
		
		if(member == null)
		{
			count = (Long) em.createQuery("select count(*) from Subscription")
				.getSingleResult();
		}
		else
		{
			count = (Long) em.createQuery("select count(*) from Subscription WHERE bookedby_id = " + member.getId())
					.getSingleResult();
		}
		
		return count.intValue();
	}
	
	

}
