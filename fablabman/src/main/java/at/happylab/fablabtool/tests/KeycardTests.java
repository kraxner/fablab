package at.happylab.fablabtool.tests;

import static org.junit.Assert.*;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import at.happylab.fablabtool.beans.KeycardManagement;
import at.happylab.fablabtool.model.KeyCard;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.User;
import at.happylab.fablabtool.session.SessionScopeProducer;

public class KeycardTests {

	private static KeycardManagement keycardMgmt;
	private static EntityManager em;

	private static long keycardID;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		SessionScopeProducer p = new SessionScopeProducer();
		em = p.getEm();
		
		keycardMgmt = new KeycardManagement(em);

	}

	@Test
	public void testStoreKeyCard() {

		int countBefore=0;
		int countAfter=0;
		
		countBefore = keycardMgmt.getAllKeyCards().size();

		KeyCard k = new KeyCard();
		
		k.setActive(true);
		k.setDescription("TEST Keycard Description");
		k.setRfid("TEST RFID");

		assertTrue(k.getId() == 0);

		keycardMgmt.storeKeyCard(k);
		
		keycardID = k.getId();

		countAfter = keycardMgmt.getAllKeyCards().size();

		assertEquals(countBefore + 1, countAfter);

		assertTrue(k.getId() > 0);

	}

	@Test
	public void testLoadKeyCard() {
		KeyCard k = keycardMgmt.loadKeyCard(keycardID);

		assertEquals(keycardID, k.getId());
		assertEquals("TEST Keycard Description", k.getDescription());
		assertEquals("TEST RFID", k.getRfid());
	}

	@Test
	public void testGetAllKeyCards() {
		
		List<KeyCard> lst= keycardMgmt.getAllKeyCards();
		
		assertTrue(lst.size()>0);
		
	}
	
	@Test
	public void testRemoveKeycard() {
		int countBefore=0;
		int countAfter=0;
		KeyCard k = keycardMgmt.loadKeyCard(keycardID);
		
		countBefore = keycardMgmt.getAllKeyCards().size();

		keycardMgmt.removeKeycard(k);
		
		countAfter = keycardMgmt.getAllKeyCards().size();

		assertEquals(countBefore - 1, countAfter);
		
		k=null;
		k = keycardMgmt.loadKeyCard(keycardID);

		assertEquals(null, k);
		
	}

	
}
