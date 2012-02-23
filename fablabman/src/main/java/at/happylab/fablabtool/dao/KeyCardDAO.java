package at.happylab.fablabtool.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import net.micalo.persistence.dao.BaseDAO;
import at.happylab.fablabtool.model.KeyCard;

public class KeyCardDAO extends BaseDAO<KeyCard> {
	private static final long serialVersionUID = 1L;

	public KeyCardDAO() {
		super(KeyCard.class);
	}
	public KeyCardDAO(EntityManager em){
		super(KeyCard.class, em); 
	}	
	
	public KeyCard loadFromRFID(String rfid) {

		KeyCard k = null;
		Query qry = em.createQuery("from KeyCard WHERE rfid=:rfid", KeyCard.class);

		qry.setParameter("rfid", rfid);

		try {
			k = (KeyCard) qry.getSingleResult();
		} catch (NonUniqueResultException e) {
			return null;
		} catch (NoResultException e) {
			return null;
		}

		return k;
	}	
}
