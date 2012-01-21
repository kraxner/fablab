package at.happylab.fablabtool.beans;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import at.happylab.fablabtool.model.ConsumationEntry;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.Package;
import at.happylab.fablabtool.model.Subscription;
import at.happylab.fablabtool.model.TimePeriod;

public class BillingTest {

	Package p;
	Subscription sub;
	Membership m;
	
	static SimpleDateFormat dateFormat;
	static BigDecimal price;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		price = BigDecimal.valueOf(1);
	}

	@Before
	public void setUp() throws Exception {
		p = new Package();
		p.setName("Testpackage");
		p.setPrice(price);
		
		m = new Membership();
		
		sub = new Subscription();
		sub.setBooksPackage(p);
		sub.setPriceOverruled(p.getPrice());
		sub.setBookedBy(m);
	}

	@Test
	public void testBillingOneMonth() throws ParseException {
		
		sub.setValidFrom(dateFormat.parse("01.01.2012"));
		//sub.setPayedUntil(dateFormat.parse(""));
		Date accountUntil = dateFormat.parse("31.01.2012");
		
		p.setBillingCycle(TimePeriod.MONTHLY);
		
		ConsumationEntry entry = Billing.createEntryFromSubscription(sub, accountUntil);
		
		assertNotNull(entry);
		assertEquals(price.doubleValue(), entry.getSum().doubleValue(), 0.01);
		assertEquals(dateFormat.parse("31.01.2012"), sub.getPayedUntil());
	}
	
	@Test
	public void testBillingOneMonthMidStart() throws ParseException {
		
		sub.setValidFrom(dateFormat.parse("15.01.2012"));
		//sub.setPayedUntil(dateFormat.parse(""));
		Date accountUntil = dateFormat.parse("31.01.2012");
		
		p.setBillingCycle(TimePeriod.MONTHLY);
		
		ConsumationEntry entry = Billing.createEntryFromSubscription(sub, accountUntil);
		
		assertNotNull(entry);
		assertEquals(price.doubleValue(), entry.getSum().doubleValue(), 0.01);
		assertEquals(dateFormat.parse("31.01.2012"), sub.getPayedUntil());
	}
	
	@Test
	public void testBillingOneMonthAlreadyPayed() throws ParseException {
		
		sub.setValidFrom(dateFormat.parse("01.01.2012"));
		sub.setPayedUntil(dateFormat.parse("31.01.2012"));
		Date accountUntil = dateFormat.parse("31.01.2012");
		
		p.setBillingCycle(TimePeriod.MONTHLY);
		
		ConsumationEntry entry = Billing.createEntryFromSubscription(sub, accountUntil);
		
		assertNull(entry);
	}
	
	@Test
	public void testBillingOneMonthEndOfYear() throws ParseException {
		
		sub.setValidFrom(dateFormat.parse("01.01.2011"));
		sub.setPayedUntil(dateFormat.parse("31.12.2011"));
		Date accountUntil = dateFormat.parse("31.01.2012");
		
		p.setBillingCycle(TimePeriod.MONTHLY);
		
		ConsumationEntry entry = Billing.createEntryFromSubscription(sub, accountUntil);
		
		assertNotNull(entry);
		assertEquals(price.doubleValue(), entry.getSum().doubleValue(), 0.01);
		assertEquals(dateFormat.parse("31.01.2012"), sub.getPayedUntil());
	}
	
	@Test
	public void testBillingTwoMonths() throws ParseException {
		
		sub.setValidFrom(dateFormat.parse("01.01.2012"));
		//sub.setPayedUntil(dateFormat.parse(""));
		Date accountUntil = dateFormat.parse("29.02.2012");
		
		p.setBillingCycle(TimePeriod.MONTHLY);
		
		ConsumationEntry entry = Billing.createEntryFromSubscription(sub, accountUntil);
		
		assertNotNull(entry);
		assertEquals(2, entry.getQuantity(), 0.01);
		assertEquals(price.doubleValue()*2, entry.getSum().doubleValue(), 0.01);
		assertEquals(dateFormat.parse("29.02.2012"), sub.getPayedUntil());
	}
	
	@Test
	public void testBillingOneMonthNotValid() throws ParseException {
		
		sub.setValidFrom(dateFormat.parse("01.01.2012"));
		sub.setPayedUntil(dateFormat.parse("31.03.2012"));
		sub.setValidTo(dateFormat.parse("31.03.2012"));
		
		Date accountUntil = dateFormat.parse("30.04.2012");
		
		p.setBillingCycle(TimePeriod.MONTHLY);
		
		ConsumationEntry entry = Billing.createEntryFromSubscription(sub, accountUntil);
		
		assertNull(entry);
	}
	
	@Test
	public void testBillingTwoMonthsNotValid() throws ParseException {
		
		sub.setValidFrom(dateFormat.parse("01.01.2012"));
		sub.setPayedUntil(dateFormat.parse("28.02.2012"));
		sub.setValidTo(dateFormat.parse("31.03.2012"));
		
		Date accountUntil = dateFormat.parse("30.04.2012");
		
		p.setBillingCycle(TimePeriod.MONTHLY);
		
		ConsumationEntry entry = Billing.createEntryFromSubscription(sub, accountUntil);
		
		assertNotNull(entry);
		assertEquals(1, entry.getQuantity(), 0.01);
		assertEquals(price.doubleValue(), entry.getSum().doubleValue(), 0.01);
		assertEquals(dateFormat.parse("31.03.2012"), sub.getPayedUntil());
	}
	
	@Test
	public void testBillingOneYear() throws ParseException {
		
		sub.setValidFrom(dateFormat.parse("01.01.2012"));
		//sub.setPayedUntil(dateFormat.parse(""));
		Date accountUntil = dateFormat.parse("31.01.2012");
		
		p.setBillingCycle(TimePeriod.ANNUAL);
		
		ConsumationEntry entry = Billing.createEntryFromSubscription(sub, accountUntil);
		
		assertNotNull(entry);
		assertEquals(12, entry.getQuantity(), 0.01);
		assertEquals(price.doubleValue()*12, entry.getSum().doubleValue(), 0.01);
		assertEquals(dateFormat.parse("31.12.2012"), sub.getPayedUntil());
	}
	
	@Test
	public void testBillingOneYearMidStart1() throws ParseException {
		
		sub.setValidFrom(dateFormat.parse("01.03.2012"));
		//sub.setPayedUntil(dateFormat.parse(""));
		Date accountUntil = dateFormat.parse("31.12.2012");
		
		p.setBillingCycle(TimePeriod.ANNUAL);
		
		ConsumationEntry entry = Billing.createEntryFromSubscription(sub, accountUntil);
		
		assertNotNull(entry);
		assertEquals(10, entry.getQuantity(), 0.01);
		assertEquals(price.doubleValue()*10, entry.getSum().doubleValue(), 0.01);
		assertEquals(dateFormat.parse("31.12.2012"), sub.getPayedUntil());
	}
	
	@Test
	public void testBillingOneYearMidStart2() throws ParseException {
		
		sub.setValidFrom(dateFormat.parse("15.03.2012"));
		//sub.setPayedUntil(dateFormat.parse(""));
		Date accountUntil = dateFormat.parse("31.12.2012");
		
		p.setBillingCycle(TimePeriod.ANNUAL);
		
		ConsumationEntry entry = Billing.createEntryFromSubscription(sub, accountUntil);
		
		assertNotNull(entry);
		assertEquals(10, entry.getQuantity(), 0.01);
		assertEquals(price.doubleValue()*10, entry.getSum().doubleValue(), 0.01);
		assertEquals(dateFormat.parse("31.12.2012"), sub.getPayedUntil());
	}

	@Test
	public void testBillingOneYearNotValid() throws ParseException {
		
		sub.setValidFrom(dateFormat.parse("01.01.2011"));
		sub.setPayedUntil(dateFormat.parse("31.12.2011"));
		sub.setValidTo(dateFormat.parse("31.12.2011"));
		Date accountUntil = dateFormat.parse("31.01.2012");
		
		p.setBillingCycle(TimePeriod.ANNUAL);
		
		ConsumationEntry entry = Billing.createEntryFromSubscription(sub, accountUntil);
		
		assertNull(entry);
	}
	
	@Test
	public void testBillingTwoYears() throws ParseException {
		
		sub.setValidFrom(dateFormat.parse("01.01.2011"));
		sub.setPayedUntil(dateFormat.parse("31.12.2011"));
		Date accountUntil = dateFormat.parse("31.12.2013");
		
		p.setBillingCycle(TimePeriod.ANNUAL);
		
		ConsumationEntry entry = Billing.createEntryFromSubscription(sub, accountUntil);
		
		assertNotNull(entry);
		assertEquals(24, entry.getQuantity(), 0.01);
		assertEquals(price.doubleValue()*24, entry.getSum().doubleValue(), 0.01);
		assertEquals(dateFormat.parse("31.12.2013"), sub.getPayedUntil());
	}
	
	@Test
	public void testBillingTwoYearsMidStart2() throws ParseException {
		
		sub.setValidFrom(dateFormat.parse("15.03.2012"));
		//sub.setPayedUntil(dateFormat.parse(""));
		Date accountUntil = dateFormat.parse("31.12.2013");
		
		p.setBillingCycle(TimePeriod.ANNUAL);
		
		ConsumationEntry entry = Billing.createEntryFromSubscription(sub, accountUntil);
		
		assertNotNull(entry);
		assertEquals(22, entry.getQuantity(), 0.01);
		assertEquals(price.doubleValue()*22, entry.getSum().doubleValue(), 0.01);
		assertEquals(dateFormat.parse("31.12.2013"), sub.getPayedUntil());
	}
	
	@Test
	public void testBillingOneYearsHalfPayed() throws ParseException {
		
		sub.setValidFrom(dateFormat.parse("01.01.2012"));
		sub.setPayedUntil(dateFormat.parse("30.06.2012"));
		Date accountUntil = dateFormat.parse("31.12.2012");
		
		p.setBillingCycle(TimePeriod.ANNUAL);
		
		ConsumationEntry entry = Billing.createEntryFromSubscription(sub, accountUntil);
		
		assertNotNull(entry);
		assertEquals(6, entry.getQuantity(), 0.01);
		assertEquals(price.doubleValue()*6, entry.getSum().doubleValue(), 0.01);
		assertEquals(dateFormat.parse("31.12.2012"), sub.getPayedUntil());
	}
	

}
