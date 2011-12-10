package at.happylab.fablabtool.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import at.happylab.fablabtool.SelectOption;
import at.happylab.fablabtool.model.Package;

public class PackageManagement implements Serializable {

	private static final long serialVersionUID = -3130705490590748129L;

	@Inject
	private EntityManager em;

	public PackageManagement(EntityManager em) {
		this.em = em;
	}

	public PackageManagement() {

	}

	public void storePackage(Package pkg) {
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		em.persist(pkg);
		em.getTransaction().commit();
		Logger.getLogger("Packagemanagement").info(
				"number of Package: "
						+ String.valueOf(em.createQuery(
								"select count(m) from Package m ")
								.getSingleResult()));
	}

	/**
	 * Removes a membership and all dependent objects
	 * 
	 * @param member
	 */
	public void removePackage(Package pkg) {
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		em.remove(pkg);
		em.getTransaction().commit();
	}

	public Package loadPackage(long id) {
		return em.find(Package.class, id);
	}

	public List<Package> getAllPackages() {
		return em.createQuery("from Package", Package.class).getResultList();

	}

	public List<SelectOption> getAllPackagesForDropDown() {

		List<Package> results = em.createQuery("from Package", Package.class)
				.getResultList();

		List<SelectOption> selectOptions = new ArrayList<SelectOption>();

		for (Package p : results) {
			selectOptions.add(new SelectOption<Package>(p, p.getName()));
		}

		return selectOptions;

	}

}
