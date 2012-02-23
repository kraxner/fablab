package at.happylab.fablabtool.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import net.micalo.persistence.EntityManagerProducer;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import at.happylab.fablabtool.dao.MembershipDAO;
import at.happylab.fablabtool.model.Membership;

public class MembershipManagementTest {

	private static MembershipDAO mDAO;
	private static EntityManager em;

	private static long membershipID;
	private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("fablabman");	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		em = entityManagerFactory.createEntityManager();

		mDAO = new MembershipDAO(em);

	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		
	}

//	@Test
//	public void testStoreMembership() {
//
//		int countBefore=0;
//		int countAfter=0;
//		
//		Membership m = new Membership();
//
//		m.setContactPerson("TEST CONTACT PERSON");
//		m.setMemberId(123);
//		
//		countBefore = mDAO.getAllMemberships().size();
//
//		mDAO.storeMembership(m);
//		
//		countAfter = mDAO.getAllMemberships().size();
//
//		membershipID = m.getIdent();
//		
//		assertEquals(countBefore + 1, countAfter);
//		assertNotSame(0, membershipID);
//		assertNotNull(m);
//
//	}

	@Test
	public void testLoadMembership() {

		Membership m = mDAO.load(membershipID);
		
		assertNotNull(m);

		assertEquals(membershipID, m.getId());
		assertEquals("TEST CONTACT PERSON", m.getContactPerson());
		assertEquals(123, m.getMemberId());

	}

//	@Test
//	public void testRemoveMembership() {
//
//		int countBefore=0;
//		int countAfter=0;
//		
//		Membership m = mDAO.load(membershipID);
//		countBefore = mDAO.getAllMemberships().size();
//
//		mDAO.remove(m);
//		
//		countAfter = mDAO.getAllMemberships().size();
//
//		assertEquals(countBefore - 1, countAfter);
//		
//		m = mDAO.load(membershipID);
//
//		assertEquals(null, m);
//
//	}

}
