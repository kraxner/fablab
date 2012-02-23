package at.happylab.fablabtool.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import at.happylab.fablabtool.dao.KeyCardDAO;
import at.happylab.fablabtool.dao.UserDAO;
import at.happylab.fablabtool.model.AccessGrant;
import at.happylab.fablabtool.model.KeyCard;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.PackageType;
import at.happylab.fablabtool.model.Subscription;
import at.happylab.fablabtool.model.User;

public class KeycardManagement implements Serializable {

	private static final long serialVersionUID = -3130705490590748129L;

	@Inject private EntityManager em;
	
	@Inject private KeyCardDAO keyCardDAO;
	@Inject private UserDAO userDAO;

	public KeycardManagement() {
	}

	public KeycardManagement(EntityManager em) {
		this.em = em;
	}

	public boolean hasAccess(String rfid) {

		User user = null;
		Membership membership = null;
		List<Subscription> subscriptions = null;
		KeyCard k = keyCardDAO.loadFromRFID(rfid);

		user = userDAO.loadFromKeycard(rfid);
		membership = user.getMembership();
		if (membership != null) {
			// Keycards of staff members might have no membership, this is ok
			// but if there is a membership, we have to check for existing subscriptions
			subscriptions = membership.getSubscriptions();
		}
		return hasAccess(k, membership, subscriptions);
	}

	public boolean hasAccess(KeyCard k, Membership membership, List<Subscription> subscriptions) {
		boolean result = false;

		/**
		 * 1. known Keycard?
		 */
		if (k == null) {
			System.err.println("unknown keycard");
			return false;
		}

		/**
		 * 2. Keycard active?
		 */
		if (!k.isActive()) {
			System.err.println("keycard not active");
			return false;
		}

		/**
		 * 3. Check the Access Times
		 */
		if (k.getAccessgrants() == null) {
			System.err.println("no accessgrant times");
			return false;
		}

		Calendar a = new GregorianCalendar();
		Calendar b = new GregorianCalendar();
		Calendar n = new GregorianCalendar();

		for (AccessGrant ag : k.getAccessgrants()) {
			if (ag.getDayOfWeek().ordinal() == n.get(Calendar.DAY_OF_WEEK) - 1) {

				a.setTime(ag.getTimeFrom());
				b.setTime(ag.getTimeUntil());

				a.set(Calendar.DAY_OF_MONTH, n.get(Calendar.DAY_OF_MONTH));
				b.set(Calendar.DAY_OF_MONTH, n.get(Calendar.DAY_OF_MONTH));

				a.set(Calendar.MONTH, n.get(Calendar.MONTH));
				b.set(Calendar.MONTH, n.get(Calendar.MONTH));

				a.set(Calendar.YEAR, n.get(Calendar.YEAR));
				b.set(Calendar.YEAR, n.get(Calendar.YEAR));

				if (n.after(a) && n.before(b)) {
					result = true;
					break;
				}
			}

		}

		if (!result) {
			System.err.println("no access granttime valid");
			return false;
		}

		/**
		 * 4. Membership subscribed to a valid Access Package?
		 */

		if (membership != null) {

			/**
			 * 4a. Do we have subscriptions?
			 */
			if (subscriptions != null) {
				if (subscriptions.size() == 0)
					return false;
			} else
				return false; // no subscriptions

			/**
			 * 4b. Check the Subscriptions for a valid ACCESS Package
			 */

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
		} else { // Keycard for i.e. the cleaning lady has no membership
			return true;
		}

		return false;
	}

}
