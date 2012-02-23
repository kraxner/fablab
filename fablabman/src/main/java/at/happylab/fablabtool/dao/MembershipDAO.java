package at.happylab.fablabtool.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import net.micalo.persistence.dao.BaseDAO;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.MembershipType;


public class MembershipDAO extends BaseDAO<Membership> {
	private static final long serialVersionUID = 1L;

	public MembershipDAO(){
		super(Membership.class); 
	}
	
	public MembershipDAO(EntityManager em){
		super(Membership.class, em); 
	}
	
	
	@Override
	protected void onBeforeStore(Membership membership) {
		// clean up superfluous users
		while (membership.getUsers().size() > membership.getMaxUser()) {
			// remove the last one
			membership.removeUser(membership.getUsers().get(membership.getUsers().size()));
		}
		// FIXME: we need a sequencer, this is not save (if there is more than one user ...)
		if (membership.getMemberId() == 0) {
			Object result = em.createQuery("select MAX(memberId) from Membership ").getSingleResult();
			long lastMemberId = 0;
			if (result != null) {
				lastMemberId = (Long)result;
			}
					
			membership.setMemberId(lastMemberId+1);
		}
		
		// adjustments for non profit memberships
		if (membership.getMembershipType() == MembershipType.PRIVATE) {
			
		}		
	}
	
	public Membership loadMembershipFromKeycard(String rfid) {
		try {
			Query qry = em.createQuery("SELECT m from membership m  inner join user u on m.id = u.membership_id inner join Keycard k on u.keycard_id=k.id where k.rfid=:rfid");
			qry.setParameter("rfid", rfid);
			return (Membership) qry.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
	
	public Membership loadMembershipFromMemberId(long id) {
		try {
			Query qry = em.createQuery("select m from Membership m where memberId=:id");
			qry.setParameter("id", id);
			return (Membership) qry.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}	

}
