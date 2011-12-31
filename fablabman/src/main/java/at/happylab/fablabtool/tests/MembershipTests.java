package at.happylab.fablabtool.tests;

import javax.persistence.EntityManager;
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.session.SessionScopeProducer;

public class MembershipTests {

	private static MembershipManagement mMgmt;
	private static EntityManager em;

	private static long membershipID;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		SessionScopeProducer p = new SessionScopeProducer();

		em = p.getEm();

		mMgmt = new MembershipManagement(em);

	}

	@Test
	public void testStoreMembership() {

		int countBefore=0;
		int countAfter=0;
		Membership m = new Membership();

		m.setContactPerson("Johannes");
		m.setMemberId(123);
		
		countBefore = mMgmt.getAllMemberships().size();

		mMgmt.storeMembership(m);
		
		countAfter = mMgmt.getAllMemberships().size();

		membershipID = m.getId();
		
		assertEquals(countBefore + 1, countAfter);

	}

	@Test
	public void testLoadMembership() {

		Membership m = mMgmt.loadMembership(membershipID);

		assertEquals(membershipID, m.getId());
		assertEquals("Johannes", m.getContactPerson());
		assertEquals(123, m.getMemberId());

	}

	@Test
	public void testRemoveMembership() {

		int countBefore=0;
		int countAfter=0;
		
		Membership m = mMgmt.loadMembership(membershipID);
		countBefore = mMgmt.getAllMemberships().size();

		mMgmt.removeMembership(m);
		
		countAfter = mMgmt.getAllMemberships().size();

		assertEquals(countBefore - 1, countAfter);
		
		m = mMgmt.loadMembership(membershipID);

		assertEquals(null, m);

	}

}
