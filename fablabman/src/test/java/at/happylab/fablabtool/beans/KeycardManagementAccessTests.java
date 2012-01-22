package at.happylab.fablabtool.beans;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.BeforeClass;
import org.junit.Test;

import at.happylab.fablabtool.model.AccessGrant;
import at.happylab.fablabtool.model.DayOfWeek;
import at.happylab.fablabtool.model.KeyCard;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.Package;
import at.happylab.fablabtool.model.PackageType;
import at.happylab.fablabtool.model.Subscription;

public class KeycardManagementAccessTests {

	private static KeycardManagement keycardMgmt;

	private static int DayOfWeekToday;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		keycardMgmt = new KeycardManagement();

		DayOfWeekToday = new GregorianCalendar().get(Calendar.DAY_OF_WEEK) - 1;

	}

	/**
	 * Keine Keycard vorhanden -> no access
	 */
	@Test
	public void testAccess_noAG_Times() {
		KeyCard k = new KeyCard();

		k.setActive(false);

		assertFalse(keycardMgmt.hasAccess(k, null, null));

		k.setActive(true);

		assertFalse(keycardMgmt.hasAccess(k, null, null));

	}
	
	/**
	 * Keine Keycard vorhanden -> no access
	 */
	@Test
	public void testAccess_noKeycard() {

		// No Keycard provided -> no access
		assertFalse(keycardMgmt.hasAccess(null, null, null));

	}
	
	/**
	 * Keycard ist gültig mit richtigen Zutrittszeiten
	 * -> Zugang ok
	 */
	@Test
	public void testAccess_KeyCardWithAGs() {

		KeyCard k = new KeyCard();
		AccessGrant ag = new AccessGrant();
		ArrayList<AccessGrant> accessgrants = new ArrayList<AccessGrant>();

		GregorianCalendar n = new GregorianCalendar();
		n.add(Calendar.MINUTE, -1);
		ag.setTimeFrom(n.getTime());
		n.add(Calendar.MINUTE, 5);
		ag.setTimeUntil(n.getTime());

		ag.setDayOfWeek(DayOfWeek.values()[DayOfWeekToday]);

		accessgrants.add(ag);

		k.setAccessgrants(accessgrants);
		k.setActive(true);

		assertTrue(keycardMgmt.hasAccess(k, null, null));

	}

	/**
	 * Keycard ist gültig mit Zutrittszeiten die nicht passen -> kein Zugang
	 */
	@Test
	public void testAccess_wrongAGTimes() {

		KeyCard k = new KeyCard();
		AccessGrant ag = new AccessGrant();
		ArrayList<AccessGrant> accessgrants = new ArrayList<AccessGrant>();

		GregorianCalendar n = new GregorianCalendar();
		n.add(Calendar.HOUR, -2);
		ag.setTimeFrom(n.getTime());
		n.add(Calendar.HOUR, -1);
		ag.setTimeUntil(n.getTime());

		ag.setDayOfWeek(DayOfWeek.values()[DayOfWeekToday]);

		accessgrants.add(ag);

		k.setAccessgrants(accessgrants);
		k.setActive(true);

		assertFalse(keycardMgmt.hasAccess(k, null, null));
	}

	/**
	 * Keycard ist gültig mit Zutrittszeiten die passen, aber am falschen
	 * Wochentag -> kein Zugang
	 */
	@Test
	public void testAccess_wrongDay() {

		KeyCard k = new KeyCard();
		AccessGrant ag = new AccessGrant();
		ArrayList<AccessGrant> accessgrants = new ArrayList<AccessGrant>();

		GregorianCalendar n = new GregorianCalendar();
		n.add(Calendar.HOUR, -1);
		ag.setTimeFrom(n.getTime());
		n.add(Calendar.HOUR, 2);
		ag.setTimeUntil(n.getTime());

		// Tag verstellen: Wenn "morgen" nicht geht, dann "gestern".
		try {
			ag.setDayOfWeek(DayOfWeek.values()[DayOfWeekToday + 1]);
		} catch (IndexOutOfBoundsException e) {
			ag.setDayOfWeek(DayOfWeek.values()[DayOfWeekToday - 1]);
		}

		accessgrants.add(ag);

		k.setAccessgrants(accessgrants);
		k.setActive(true);

		assertFalse(keycardMgmt.hasAccess(k, null, null));
	}

	/**
	 * Keycard ok, Zutrittszeiten ok, aber Mitgliedschaft ohne Subscriptions ->
	 * kein Zugang
	 */
	@Test
	public void testAccess1_Membership() {

		KeyCard k = new KeyCard();
		AccessGrant ag = new AccessGrant();
		ArrayList<AccessGrant> accessgrants = new ArrayList<AccessGrant>();

		GregorianCalendar n = new GregorianCalendar();
		n.add(Calendar.HOUR, -1);
		ag.setTimeFrom(n.getTime());
		n.add(Calendar.HOUR, 2);
		ag.setTimeUntil(n.getTime());

		ag.setDayOfWeek(DayOfWeek.values()[DayOfWeekToday]);

		accessgrants.add(ag);

		k.setAccessgrants(accessgrants);
		k.setActive(true);

		assertFalse(keycardMgmt.hasAccess(k, new Membership(), null));

		assertFalse(keycardMgmt.hasAccess(k, new Membership(), new ArrayList<Subscription>()));
	}

	/**
	 * Keycard ok, Zutrittszeiten ok, aber Mitgliedschaft ohne Subscriptions ->
	 * kein Zugang
	 */
	@Test
	public void testAccess2_Membership() {
		Membership membership = new Membership();
		KeyCard k = new KeyCard();
		Subscription s = new Subscription();
		Package p = new Package();
		AccessGrant ag = new AccessGrant();
		ArrayList<AccessGrant> accessgrants = new ArrayList<AccessGrant>();

		GregorianCalendar n = new GregorianCalendar();
		n.add(Calendar.MINUTE, -1);
		ag.setTimeFrom(n.getTime());
		n.add(Calendar.MINUTE, 10);
		ag.setTimeUntil(n.getTime());

		ag.setDayOfWeek(DayOfWeek.values()[DayOfWeekToday]);

		accessgrants.add(ag);

		k.setAccessgrants(accessgrants);
		k.setActive(true);

		ArrayList<Subscription> subscriptions = new ArrayList<Subscription>();

		p.setPackageType(PackageType.ACCESS);
		s.setBooksPackage(p);
		s.setValidTo(null);

		subscriptions.add(s);

		assertTrue(keycardMgmt.hasAccess(k, membership, subscriptions));
		
		/**
		 * Wrong Package Type
		 */
		
		subscriptions = new ArrayList<Subscription>();

		p.setPackageType(PackageType.MEMBERSHIP);
		s.setBooksPackage(p);

		subscriptions.add(s);
		
		assertFalse(keycardMgmt.hasAccess(k, membership, subscriptions));
		
	}

}
