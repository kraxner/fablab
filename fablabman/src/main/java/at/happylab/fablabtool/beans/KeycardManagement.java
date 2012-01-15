package at.happylab.fablabtool.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.seam.solder.beanManager.BeanManagerLocator;
import org.jboss.seam.wicket.util.NonContextual;

import at.happylab.fablabtool.model.AccessGrant;
import at.happylab.fablabtool.model.KeyCard;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.PackageType;
import at.happylab.fablabtool.model.Subscription;
import at.happylab.fablabtool.model.User;
import at.happylab.fablabtool.session.SessionScopeProducer;

public class KeycardManagement implements Serializable {

	private static final long serialVersionUID = -3130705490590748129L;

	private NonContextual<SessionScopeProducer> sessionScopeProducerRef;
	private SessionScopeProducer sessionScopeProducer;

	@Inject
	private EntityManager em;

	public KeycardManagement() {

	}
	
	public KeycardManagement(EntityManager em) {
		this.em = em;
	}

	public void storeKeyCard(KeyCard k) {
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		em.persist(k);
		em.getTransaction().commit();
		Logger.getLogger(" KeycardManagement").info("number of  Keycards: " + String.valueOf(em.createQuery("select count(k) from KeyCard k").getSingleResult()));
	}

	/**
	 * Removes a KeyCard
	 * 
	 * @param KeyCard
	 */
	public void removeKeycard(KeyCard k) {
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		em.remove(k);
		em.getTransaction().commit();
	}

	public KeyCard loadKeyCard(long id) {
		return em.find(KeyCard.class, id);
	}

	public List<KeyCard> getAllKeyCards() {
		return em.createQuery("from KeyCard", KeyCard.class).getResultList();

	}

	public boolean hasAccess(String rfid) {

		boolean result = false;
		Membership member;
		User user;
		Query qry;
		ArrayList<Subscription> subscriptions;

		if (em == null) {
			BeanManager manager = new BeanManagerLocator().getBeanManager();
			sessionScopeProducerRef = NonContextual.of(SessionScopeProducer.class, manager);

			sessionScopeProducer = sessionScopeProducerRef.newInstance().produce().inject().get();
			em = sessionScopeProducer.getEm();
		}

		KeyCard k = null;

		try {
			qry = em.createQuery("from KeyCard WHERE rfid=:rfid", KeyCard.class);
			qry.setParameter("rfid", rfid);
			k = (KeyCard) qry.getSingleResult();
		} catch (NonUniqueResultException e) {
			return false;
		} catch (NoResultException e) {
			return false;
		}

		/**
		 * 1. known Keycard?
		 */
		if (k == null)
			return false;

		/**
		 * 2. Keycard active?
		 */
		if (!k.isActive())
			return false;

		System.err.println("2. Keycard Active");
		
		/**
		 * 3. Check the Access Times
		 */
		result = false;

		List<AccessGrant> agList = k.getAccessgrants();

		Calendar a = new GregorianCalendar();
		Calendar b = new GregorianCalendar();
		Calendar n = new GregorianCalendar();

		for (AccessGrant ag : agList) {
			if (ag.getDayOfWeek().ordinal() == n.get(Calendar.DAY_OF_WEEK) - 1) {

				a.setTime(ag.getTimeFrom());
				b.setTime(ag.getTimeUntil());

				a.set(Calendar.DAY_OF_MONTH, n.get(Calendar.DAY_OF_MONTH));
				b.set(Calendar.DAY_OF_MONTH, n.get(Calendar.DAY_OF_MONTH));

				a.set(Calendar.MONTH, n.get(Calendar.MONTH));
				b.set(Calendar.MONTH, n.get(Calendar.MONTH));

				a.set(Calendar.YEAR, n.get(Calendar.YEAR));
				b.set(Calendar.YEAR, n.get(Calendar.YEAR));

//				System.err.println("COMPARE: " + a.getTime().toString() + " " + b.getTime().toString() + " " + n.getTime().toString());

				if (n.after(a) && n.before(b)){
					result = true;
					break;
				}
			}

		}
		
		if (!result)
			return false;


		/**
		 * 4a. Membership subscribed to a valid Access Package?
		 */
		try {
			qry = em.createQuery("from User where Keycard_id=:keycard_id", User.class);
			qry.setParameter("keycard_id", k.getId());

			user = (User) qry.getSingleResult();

		} catch (NoResultException e) {
			return false;	
		} catch (IllegalArgumentException e) {
			return false;
		}

		member = user.getMembership();
		
		if(member==null)
			return true;	// Keycards die keinem User zugeteilt sind (z.B. für Putzfrau)

//		System.err.println("3a. Membership found");

		/**
		 * 4b. Check the Subscriptions for a valid ACCESS Package
		 */

		try {
			qry = em.createQuery("FROM Subscription WHERE Bookedby_ID = :memberid", Subscription.class);
			qry.setParameter("memberid", member.getId());

			subscriptions = new ArrayList<Subscription>(qry.getResultList());
		} catch (NoResultException e) {
			return false;
		} catch (IllegalArgumentException e) {
			return false;
		}

		result = false;

		for (Subscription s : subscriptions) {
			if (s.getBooksPackage().getPackageType() == PackageType.ACCESS) {
				if (s.getValidTo() == null)
					result = true;
				else
					result = s.getValidTo().before(new Date()); // Falls Subscription schon gekündigt wurde, noch aktiv?

				if (result)
					return true;
			} else {
				result = false;
			}
		}

		// System.err.println("3c. valid subscription found");

		return false;
	}

}
