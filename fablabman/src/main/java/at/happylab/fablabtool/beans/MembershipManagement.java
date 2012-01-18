package at.happylab.fablabtool.beans;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.seam.solder.beanManager.BeanManagerLocator;
import org.jboss.seam.wicket.util.NonContextual;

import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.session.SessionScopeProducer;
import at.happylab.fablabtool.model.MembershipType;

public class MembershipManagement implements Serializable {

	private static final long serialVersionUID = -3130705490590748129L;

	@Inject
	private EntityManager em;
	private NonContextual<SessionScopeProducer> sessionScopeProducerRef;
	private SessionScopeProducer sessionScopeProducer;

	public MembershipManagement() {
		
	}
	
	public MembershipManagement(EntityManager em) {
		this.em = em;
	}

	public void storeMembership(Membership member) {
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		// clean up superfluous users
		while (member.getUsers().size() > member.getMaxUser()) {
			// remove the last one
			member.removeUser(member.getUsers().get(member.getUsers().size()));
		}

		// adjustments for non profit memberships
		if (member.getMembershipType() == MembershipType.PRIVATE) {
			
		}
		em.persist(em.merge(member));
		em.getTransaction().commit();
		Logger.getLogger("Membershipmanagement").info("number of Members: " + String.valueOf(em.createQuery("select count(m) from Membership m ").getSingleResult()));
	}

	/**
	 * Removes a membership and all dependent objects
	 * 
	 * @param member
	 */
	public void removeMembership(Membership member) {
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		em.remove(member);
		em.getTransaction().commit();
	}

	public Membership loadMembership(long id) {
		return em.find(Membership.class, id);
	}

	public Membership loadMembershipFromKeycard(String rfid) {
		BeanManager manager = new BeanManagerLocator().getBeanManager();
		sessionScopeProducerRef = NonContextual.of(SessionScopeProducer.class, manager);

		sessionScopeProducer = sessionScopeProducerRef.newInstance().produce().inject().get();
		EntityManager em = sessionScopeProducer.getEm();

		int memberID = 0;

		try {
			Query qry = em.createQuery("select m.id from membership m  inner join user u on m.id = u.membership_id inner join Keycard k on u.keycard_id=k.id where k.rfid=:rfid");
			qry.setParameter("rfid", rfid);
			memberID = Integer.valueOf((String) qry.getSingleResult());
		} catch (NoResultException e) {
			return null;
		} catch (IllegalArgumentException e) {
			return null;
		}

		try {
			return this.loadMembership(memberID);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	
	public List<Membership> getAllMemberships() {
		return em.createQuery("from Membership", Membership.class).getResultList();
	}
}
