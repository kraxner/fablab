package at.happylab.fablabtool.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.BeforeClass;
import org.junit.Test;

import at.happylab.fablabtool.beans.PackageManagement;
import at.happylab.fablabtool.model.Package;
import at.happylab.fablabtool.model.TimePeriod;
import at.happylab.fablabtool.session.SessionScopeProducer;

public class PackageTests {

	private static PackageManagement packageMgmt;
	private static EntityManager em;

	private static long packageID;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		SessionScopeProducer p = new SessionScopeProducer();
		em = p.getEm();

		packageMgmt = new PackageManagement(em);

	}

	@Test
	public void testStorePackage() {

		int countBefore = 0;
		int countAfter = 0;

		countBefore = packageMgmt.getAllPackages().size();

		Package p = new Package();

		p.setName("UNIT TEST PACKAGE NAME");
		p.setDescription("UNIT TEST PACKAGE DESCRIPTION");

		p.setBillingCycle(TimePeriod.MONTHLY);
		p.setCancelationPeriodAdvance(TimePeriod.ANNUAL);
		p.setCancelationPeriod(2);

		packageMgmt.storePackage(p);

		packageID = p.getId();

		countAfter = packageMgmt.getAllPackages().size();

		assertEquals(countBefore + 1, countAfter);

		assertTrue(p.getId() > 0);

	}

	@Test
	public void testLoadPackage() {
		Package p = packageMgmt.loadPackage(packageID);

		assertEquals(packageID, p.getId());
		assertEquals("UNIT TEST PACKAGE NAME", p.getName());
		assertEquals("UNIT TEST PACKAGE DESCRIPTION", p.getDescription());

		assertEquals(TimePeriod.MONTHLY, p.getBillingCycle());
		assertEquals(TimePeriod.ANNUAL, p.getCancelationPeriodAdvance());
		assertEquals(2, p.getCancelationPeriod());
	}

	@Test
	public void testGetAllPackages() {

		int size = 0;

		List<Package> lst = packageMgmt.getAllPackages();

		size = lst.size();

		assertTrue(size > 0);

		assertTrue(lst.size() > 0);

	}

	@Test
	public void testRemovePackage() {
		int countBefore = 0;
		int countAfter = 0;
		Package p = packageMgmt.loadPackage(packageID);

		countBefore = packageMgmt.getAllPackages().size();

		packageMgmt.removePackage(p);

		countAfter = packageMgmt.getAllPackages().size();

		assertEquals(countBefore - 1, countAfter);

		p = null;
		p = packageMgmt.loadPackage(packageID);

		assertEquals(null, p);
	}

	@Test
	public void testGetNextCancellationDate_MONTHLY1() {

		Package p = new Package();

		p.setCancelationPeriod(0);
		p.setCancelationPeriodAdvance(TimePeriod.MONTHLY);

		GregorianCalendar c = new GregorianCalendar(2012, Calendar.JANUARY, 1);// Aktuelles Datum
		GregorianCalendar expected = new GregorianCalendar();

		/**
		 * aktuelles Datum 1.1.2012, keine Kündigungsfrist, monatliche Kündigung
		 * => nächster Kündigungstermin am 31.1.2012
		 */

		expected.setTime(packageMgmt.getNextCancelationDate(p, c));

		assertEquals(31, expected.get(Calendar.DATE));
		assertEquals(Calendar.JANUARY, expected.get(Calendar.MONTH));
		assertEquals(2012, expected.get(Calendar.YEAR));

	}

	@Test
	public void testGetNextCancellationDate_MONTHLY2() {

		Package p = new Package();

		p.setCancelationPeriod(0);
		p.setCancelationPeriodAdvance(TimePeriod.MONTHLY);

		GregorianCalendar c = new GregorianCalendar(2012, Calendar.JANUARY, 1);// Aktuelles Datum
		GregorianCalendar expected = new GregorianCalendar();

		/**
		 * aktuelles Datum 1.1.2012, 1 Monat Kündigungsfrist, monatliche
		 * Kündigung => nächster Kündigungstermin am 29.2.2012
		 */

		p.setCancelationPeriod(1);
		expected.setTime(packageMgmt.getNextCancelationDate(p, c));

		assertEquals(29, expected.get(Calendar.DATE));
		assertEquals(Calendar.FEBRUARY, expected.get(Calendar.MONTH));
		assertEquals(2012, expected.get(Calendar.YEAR));
	}

	@Test
	public void testGetNextCancellationDate_MONTHLY3() {

		Package p = new Package();

		p.setCancelationPeriod(0);
		p.setCancelationPeriodAdvance(TimePeriod.MONTHLY);

		GregorianCalendar c = new GregorianCalendar(2012, Calendar.JANUARY, 1);// Aktuelles Datum
		GregorianCalendar expected = new GregorianCalendar();

		/**
		 * aktuelles Datum 1.1.2012, 3 Monat Kündigungsfrist, monatliche
		 * Kündigung => nächster Kündigungstermin am 31.5.2012
		 */

		p.setCancelationPeriod(3);
		expected.setTime(packageMgmt.getNextCancelationDate(p, c));

		assertEquals(31, expected.get(Calendar.DATE));
		assertEquals(Calendar.MAY, expected.get(Calendar.MONTH));
		assertEquals(2012, expected.get(Calendar.YEAR));

	}

	@Test
	public void testGetNextCancellationDate_QUARTER1() {

		Package p = new Package();

		p.setCancelationPeriod(0);
		p.setCancelationPeriodAdvance(TimePeriod.QUARTER);

		final GregorianCalendar c = new GregorianCalendar(2012, Calendar.JANUARY, 1);// Aktuelles Datum
		GregorianCalendar expected = new GregorianCalendar();

		/**
		 * aktuelles Datum 1.1.2012, keine Kündigungsfrist, Kündigung möglich im
		 * Quartal => nächster Kündigungstermin am 31.3.2012 (Ende des 1.
		 * Quartals)
		 */

		expected.setTime(packageMgmt.getNextCancelationDate(p, c));

		assertEquals(31, expected.get(Calendar.DATE));
		assertEquals(Calendar.MARCH, expected.get(Calendar.MONTH));
		assertEquals(2012, expected.get(Calendar.YEAR));
	}

	@Test
	public void testGetNextCancellationDate_QUARTER2() {

		Package p = new Package();

		p.setCancelationPeriod(0);
		p.setCancelationPeriodAdvance(TimePeriod.QUARTER);

		final GregorianCalendar c = new GregorianCalendar(2012, Calendar.JANUARY, 1);// Aktuelles Datum
		GregorianCalendar expected = new GregorianCalendar();
		/**
		 * aktuelles Datum 1.1.2012, 1 Monat Kündigungsfrist, Kündigung möglich
		 * im Quartal => nächster Kündigungstermin am 31.3.2012 (Ende des 1.
		 * Quartals)
		 */

		p.setCancelationPeriod(1);
		expected.setTime(packageMgmt.getNextCancelationDate(p, c));

		assertEquals(31, expected.get(Calendar.DATE));
		assertEquals(Calendar.MARCH, expected.get(Calendar.MONTH));
		assertEquals(2012, expected.get(Calendar.YEAR));

	}

	@Test
	public void testGetNextCancellationDate_QUARTER3() {

		Package p = new Package();

		p.setCancelationPeriod(0);
		p.setCancelationPeriodAdvance(TimePeriod.QUARTER);

		final GregorianCalendar c = new GregorianCalendar(2012, Calendar.JANUARY, 1);// Aktuelles Datum
		GregorianCalendar expected = new GregorianCalendar();
		
		/**
		 * aktuelles Datum 1.1.2012, 3 Monat Kündigungsfrist, Kündigung möglich
		 * im Quartal => nächster Kündigungstermin am 30.6.2012 (Ende des 1.
		 * Quartals)
		 */

		p.setCancelationPeriod(3);
		expected.setTime(packageMgmt.getNextCancelationDate(p, c));

		assertEquals(30, expected.get(Calendar.DATE));
		assertEquals(Calendar.JUNE, expected.get(Calendar.MONTH));
		assertEquals(2012, expected.get(Calendar.YEAR));

	}

	@Test
	public void testGetNextCancellationDate_ANNUAL1() {

		Package p = new Package();

		p.setCancelationPeriod(0);
		p.setCancelationPeriodAdvance(TimePeriod.ANNUAL);
		
		GregorianCalendar c = new GregorianCalendar(2012, Calendar.JANUARY, 1);// Aktuelles Datum
		GregorianCalendar expected = new GregorianCalendar();

		/**
		 * aktuelles Datum 1.1.2012, keine Kündigungsfrist, jährliche Kündigung
		 * => nächster Kündigungstermin am 31.3.2012 (am Ende des Jahres)
		 */

		expected.setTime(packageMgmt.getNextCancelationDate(p, c));

		assertEquals(31, expected.get(Calendar.DATE));
		assertEquals(Calendar.DECEMBER, expected.get(Calendar.MONTH));
		assertEquals(2012, expected.get(Calendar.YEAR));

	}
	
	@Test
	public void testGetNextCancellationDate_ANNUAL2() {

		Package p = new Package();

		p.setCancelationPeriod(2);
		p.setCancelationPeriodAdvance(TimePeriod.ANNUAL);
		
		GregorianCalendar c = new GregorianCalendar(2012, Calendar.JANUARY, 1);// Aktuelles Datum
		GregorianCalendar expected = new GregorianCalendar();

		/**
		 * aktuelles Datum 1.1.2012, 2 Monate Kündigungsfrist, jährliche Kündigung
		 * => nächster Kündigungstermin am 31.3.2012 (am Ende des Jahres)
		 */

		expected.setTime(packageMgmt.getNextCancelationDate(p, c));

		assertEquals(31, expected.get(Calendar.DATE));
		assertEquals(Calendar.DECEMBER, expected.get(Calendar.MONTH));
		assertEquals(2012, expected.get(Calendar.YEAR));

	}
	
	@Test
	public void testGetNextCancellationDate_ANNUAL3() {

		Package p = new Package();

		p.setCancelationPeriod(12);
		p.setCancelationPeriodAdvance(TimePeriod.ANNUAL);
		
		GregorianCalendar c = new GregorianCalendar(2012, Calendar.JANUARY, 1);// Aktuelles Datum
		GregorianCalendar expected = new GregorianCalendar();

		/**
		 * aktuelles Datum 1.1.2012, 12 Monate Kündigungsfrist, jährliche Kündigung
		 * => nächster Kündigungstermin am 31.3.2013 (nächstes Jahr)
		 */

		expected.setTime(packageMgmt.getNextCancelationDate(p, c));

		assertEquals(31, expected.get(Calendar.DATE));
		assertEquals(Calendar.DECEMBER, expected.get(Calendar.MONTH));
		assertEquals(2013, expected.get(Calendar.YEAR));

	}


}
