package at.happylab.fablabtool.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import net.micalo.persistence.EntityManagerProducer;

import org.junit.BeforeClass;
import org.junit.Test;

import at.happylab.fablabtool.dao.KeyCardDAO;
import at.happylab.fablabtool.model.KeyCard;

public class KeycardManagementTest {

	private static KeyCardDAO keycardDAO;
	private static EntityManager em;

	private static long keycardID;
	
	private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("fablabman");	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		em = entityManagerFactory.createEntityManager();
		
		keycardDAO = new KeyCardDAO(em);

	}

	@Test
	public void testStoreKeyCard() {

		int countBefore=0;
		int countAfter=0;
		
		countBefore = keycardDAO.getAll().size();

		KeyCard k = new KeyCard();
		
		k.setActive(true);
		k.setDescription("TEST Keycard Description");
		k.setRfid("TEST RFID");

		assertTrue(k.getId() == 0);

		keycardDAO.store(k);
		
		keycardID = k.getId();

		countAfter = keycardDAO.getAll().size();

		assertEquals(countBefore + 1, countAfter);

		assertTrue(k.getId() > 0);

	}

	@Test
	public void testLoadKeyCard() {
		KeyCard k = keycardDAO.load(keycardID);

		assertEquals(keycardID, k.getId());
		assertEquals("TEST Keycard Description", k.getDescription());
		assertEquals("TEST RFID", k.getRfid());
	}

	@Test
	public void testGetAllKeyCards() {
		
		List<KeyCard> lst= keycardDAO.getAll();
		
		assertTrue(lst.size()>0);
		
	}
	
	@Test
	public void testRemoveKeycard() {
		int countBefore=0;
		int countAfter=0;
		KeyCard k = keycardDAO.load(keycardID);
		
		countBefore = keycardDAO.getAll().size();

		keycardDAO.remove(k);
		keycardDAO.commit();
		
		countAfter = keycardDAO.getAll().size();

		assertEquals(countBefore - 1, countAfter);
		
		k=null;
		k = keycardDAO.load(keycardID);

		assertEquals(null, k);
		
	}

	
}
