package at.happylab.fablabtool.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import at.happylab.fablabtool.SelectOption;
import at.happylab.fablabtool.model.Package;
import at.happylab.fablabtool.model.TimePeriod;

public class PackageManagement implements Serializable {

	private static final long serialVersionUID = -3130705490590748129L;

	@Inject
	private EntityManager em;

	public PackageManagement(EntityManager em) {
		this.em = em;
	}

	public PackageManagement() {

	}

	public void storePackage(Package p) {
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		em.persist(p);
		em.getTransaction().commit();
		Logger.getLogger("Packagemanagement").info("number of Package: " + String.valueOf(em.createQuery("select count(m) from Package m ").getSingleResult()));
	}

	/**
	 * Removes a membership and all dependent objects
	 * 
	 * @param member
	 */
	public void removePackage(Package p) {
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}

		em.remove(p);
		em.getTransaction().commit();
	}

	public Package loadPackage(long id) {
		return em.find(Package.class, id);
	}

	public List<Package> getAllPackages() {
		return em.createQuery("from Package", Package.class).getResultList();

	}

	public List<SelectOption> getAllPackagesForDropDown() {
		List<Package> results = em.createQuery("from Package", Package.class).getResultList();

		List<SelectOption> selectOptions = new ArrayList<SelectOption>();

		for (Package p : results) {
			selectOptions.add(new SelectOption<Package>(p, p.getName()));
		}

		return selectOptions;
	}

	/**
	 * Methode liefert das nächstmögliche Kündigungsdatum eines Paketes zurück.
	 * 
	 * @param pkg
	 *            das Packet
	 * @return das errechnete Kündigungsdatum
	 * 
	 * @author Johannes Bauer
	 */
	public Date getNextCancelationDate(Package pkg) {
		return getNextCancelationDate(pkg, new GregorianCalendar());
	}

	public Date getNextCancelationDate(Package pkg, GregorianCalendar c) {

		/**
		 * Zum aktuellen Datum die Kündigungsfrist in Monaten dazuzählen.
		 */
		c.add(Calendar.MONTH, pkg.getCancelationPeriod());

		/**
		 * Kündigung nur zu bestimmten Zeiten möglich nur am Ende von
		 * Monat/Quartal/Jahr
		 */
		if (pkg.getCancelationPeriodAdvance() == TimePeriod.MONTHLY) {
			//c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DATE));

		} else if (pkg.getCancelationPeriodAdvance() == TimePeriod.QUARTER) {

			if (c.get(Calendar.MONTH) <= Calendar.MARCH)
				c.set(Calendar.MONTH, Calendar.MARCH);
			else if (c.get(Calendar.MONTH) <= Calendar.JUNE)
				c.set(Calendar.MONTH, Calendar.JUNE);
			else if (c.get(Calendar.MONTH) <= Calendar.SEPTEMBER)
				c.set(Calendar.MONTH, Calendar.SEPTEMBER);
			else if (c.get(Calendar.MONTH) <= Calendar.DECEMBER)
				c.set(Calendar.MONTH, Calendar.DECEMBER);

		} else if (pkg.getCancelationPeriodAdvance() == TimePeriod.ANNUAL) {
			c.set(Calendar.MONTH, Calendar.DECEMBER);
		}

		/**
		 * Kündigung immer nur am letzten Tag eines Monats
		 */
		c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));

		return c.getTime();
	}

}
