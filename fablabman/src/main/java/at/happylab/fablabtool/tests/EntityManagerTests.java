package at.happylab.fablabtool.tests;

import javax.persistence.EntityManager;

import org.junit.BeforeClass;
import org.junit.Test;

import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.User;
import at.happylab.fablabtool.session.SessionScopeProducer;

public class EntityManagerTests {

	
	private static MembershipManagement mMgmt;
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		EntityManager em = null;

		SessionScopeProducer p = new SessionScopeProducer();
		
		em = p.getEm();
		
		mMgmt = new MembershipManagement(em);
		
		
	}

	@Test
	public void testAddMembership() {
		
		User u = new User();
		
		u.setFirstname("Johannes");
		u.setLastname("Bauer");
		u.setMembership(new Membership());
		
		
		Membership m = new Membership();
		
		m.addUser(u);
		m.setId(1);
		
		mMgmt.storeMembership(m);
		
		
		
		
	}

}
