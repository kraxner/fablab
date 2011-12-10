package at.happylab.fablabtool.tests;

import javax.persistence.EntityManager;

import org.junit.BeforeClass;
import org.junit.Test;

import at.happylab.fablabtool.beans.EntityManagerProducer;
import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.model.PrivateMembership;
import at.happylab.fablabtool.model.User;

public class EntityManagerTests {

	
	private static MembershipManagement mMgmt;
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		EntityManager em = null;

		EntityManagerProducer p = new EntityManagerProducer();
		
		em = p.getEm();
		
		mMgmt = new MembershipManagement(em);
		
		
	}

	@Test
	public void testAddMembership() {
		
		User u = new User();
		
		u.setFirstname("Johannes");
		u.setLastname("Bauer");
		u.setMembership(new PrivateMembership());
		
		
		PrivateMembership m = new PrivateMembership();
		
		m.addUser(u);
		m.setId(1);
		
		mMgmt.storeMembership(m);
		
		
		
		
	}

}
