package at.happylab.fablabtool.beans;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import at.happylab.fablabtool.model.Device;

public class DeviceManagement implements Serializable {

	private static final long serialVersionUID = -3130705490590748129L;

	@Inject
	private EntityManager em;

	public DeviceManagement(EntityManager em) {
		this.em = em;
	}

	public DeviceManagement() {

	}

	public void storeDevice(Device ag) {
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		em.persist(ag);
		em.getTransaction().commit();
		Logger.getLogger("DeviceManagement").info(
				"number of Devices: "
						+ String.valueOf(em.createQuery(
								"select count(d) from Device d ")
								.getSingleResult()));
	}

	/**
	 * Removes a membership and all dependent objects
	 * 
	 * @param member
	 */
	public void removeDevice(Device ag) {
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		em.remove(ag);
		em.getTransaction().commit();
	}

	public Device loadDevice(long id) {
		return em.find(Device.class, id);
	}


}
