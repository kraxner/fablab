package at.happylab.fablabtool.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.hsqldb.lib.KMPSearchAlgorithm;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import at.happylab.fablabtool.beans.AccessGrantManagement;
import at.happylab.fablabtool.beans.KeycardManagement;
import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.beans.PackageManagement;
import at.happylab.fablabtool.beans.SubscriptionManagement;
import at.happylab.fablabtool.beans.UserManagement;
import at.happylab.fablabtool.model.AccessGrant;
import at.happylab.fablabtool.model.DayOfWeek;
import at.happylab.fablabtool.model.KeyCard;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.PackageType;
import at.happylab.fablabtool.model.Subscription;
import at.happylab.fablabtool.model.User;
import at.happylab.fablabtool.model.Package;
import at.happylab.fablabtool.session.SessionScopeProducer;

public class KeycardAccessTests {

	private static KeycardManagement keycardMgmt;
	private static MembershipManagement membershipMgmt;
	private static UserManagement userMgmt;
	private static AccessGrantManagement agMgmt;
	private static PackageManagement pMgmt;
	private static SubscriptionManagement sMgmt;

	private static EntityManager em;

	private static User u;
	private static KeyCard k;
	private static Membership m;
	private static AccessGrant ag;
	private static Package p;
	private static Subscription s;

	private static String rfid;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		SessionScopeProducer ssp = new SessionScopeProducer();
		em = ssp.getEm();

		keycardMgmt = new KeycardManagement(em);
		membershipMgmt = new MembershipManagement(em);
		userMgmt = new UserManagement(em);
		agMgmt = new AccessGrantManagement(em);
		pMgmt = new PackageManagement(em);
		sMgmt = new SubscriptionManagement(em);

		m = new Membership();
		u = new User();
		k = new KeyCard();
		ag = new AccessGrant();
		p = new Package();

		GregorianCalendar n = new GregorianCalendar();
		n.add(Calendar.HOUR, -1);
		ag.setTimeFrom(n.getTime());
		n.add(Calendar.HOUR, 2);
		ag.setTimeUntil(n.getTime());
		
		ag.setDayOfWeek(DayOfWeek.SATURDAY);

		rfid = "TEST RFID";

		u.setFirstname("Johannes");
		u.setLastname("Bauer");

		k.setActive(true);
		k.setRfid(rfid);

		p.setName("Test Access Package");
		p.setPackageType(PackageType.ACCESS);

		keycardMgmt.storeKeyCard(k);
		membershipMgmt.storeMembership(m);
		userMgmt.storeUser(u);
		agMgmt.storeAccessGrant(ag);
		pMgmt.storePackage(p);

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		userMgmt.removeUser(u);
		membershipMgmt.removeMembership(m);
		keycardMgmt.removeKeycard(k);
		agMgmt.removeAccessGrant(ag);
		pMgmt.removePackage(p);
	}

	@Test
	public void testAccess1() {

		// Random RFID gets no access
		assertFalse(keycardMgmt.hasAccess("no rfid"));

	}

	@Test
	public void testAccess2() {

		// Keycard valid but not connected to a user.
		assertFalse(keycardMgmt.hasAccess(rfid));

	}

	/**
	 * Keycard ist gültig aber mit keinem User zugeordnet
	 */
	@Test
	public void testAccess3() {

		u.setKeyCard(k);
		userMgmt.storeUser(u);

		assertFalse(keycardMgmt.hasAccess(rfid));

	}

	/**
	 * Keycard mit gültiger Zeit ohne Verbindung zum User hat Zugang z.B. bei
	 * der Keycard von der Putzfrau
	 */
	@Test
	public void testAccess4() {

		ArrayList<AccessGrant> agLst = new ArrayList<AccessGrant>();

		agLst.add(ag);

		k.setAccessgrants(agLst);

		keycardMgmt.storeKeyCard(k);

		assertTrue(keycardMgmt.hasAccess(rfid));

	}
	
	/**
	 * Keycard mit gültiger Zeit ohne Verbindung zum User hat Zugang z.B. bei
	 * der Keycard von der Putzfrau
	 * Keycard ist aber nicht aktiv
	 */
	@Test
	public void testAccess5() {

		ArrayList<AccessGrant> agLst = new ArrayList<AccessGrant>();

		agLst.add(ag);

		k.setAccessgrants(agLst);
		k.setActive(false);

		keycardMgmt.storeKeyCard(k);

		assertFalse(keycardMgmt.hasAccess(rfid));

	}

}
