package at.happylab.fablabtool.beans;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import at.happylab.fablabtool.model.ConsumationEntry;
import at.happylab.fablabtool.model.Subscription;

public class Billing {
	
	public static ConsumationEntry createEntryFromSubscription(Subscription sub, Date end) {
		Calendar cal = Calendar.getInstance();
		int months = 0;
		Date newPayedUntilDate = new Date();
		
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		String text = "";
		
		// Sollte des Paket noch nicht aktiv sein, nicht abrechnen
		if (sub.getValidFrom().after(end)) {
			return null;
		}
		
		// Sollte des Paket schon vor dem End-Datum gekündigt worden sein, nur bis zu dem Kündigungsdatum abrechnen
		if (sub.getValidTo() != null && sub.getValidTo().before(end)) {
			end = sub.getValidTo();
		}
		
		
		if (sub.getPayedUntil() == null) {
			// es wird zum ersten mal abgerechnet
			// daher aliquot (erstes Monat zählt voll) bis Ende der Abrechnungsperiode
			
			cal.setTime(sub.getValidFrom());
			int validFromMonth = cal.get(Calendar.MONTH);
			int validFromYear  = cal.get(Calendar.YEAR);
			
			cal.setTime(end);
			int calculateUntilMonth = cal.get(Calendar.MONTH);
			int calculateUntilYear  = cal.get(Calendar.YEAR);
			
			int diffInMonths = 0;
			diffInMonths = (calculateUntilYear - validFromYear)*12;
			diffInMonths += (calculateUntilMonth - validFromMonth);
			
			switch (sub.getBooksPackage().getBillingCycle()) {
				case MONTHLY:
					months = diffInMonths + 1; // erstes Monat wird voll verrechnet
					break;
				
				case QUARTER:
					// auf nächstes Vielfaches von 3 aufrunden
					//months = (3-validFromMonth+1) + (diffInMonths/3)*3;
					break;
				
				case ANNUAL:
					// auf nächstes Vielfaches von 12 aufrunden
					months = (12-validFromMonth) + (calculateUntilYear-validFromYear)*12;
					break;
				
				default:
					break;
			}
			
			// Neues payedUntil Datum (letzer des jeweiligen Monats)
			cal.setTime(sub.getValidFrom());
			cal.add(Calendar.MONTH, months-1);
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			newPayedUntilDate = cal.getTime();
			
			text = dateFormat.format(sub.getValidFrom()) + " - " + dateFormat.format(newPayedUntilDate);
		}
		else {
			// es wurde bereits abgerechnet
			// daher bis Ende des jeweiligen Monats abrechnen
			
			cal.setTime(sub.getPayedUntil());
			int payedUntilMonth = cal.get(Calendar.MONTH);
			int payedUntilYear  = cal.get(Calendar.YEAR);

			cal.setTime(end);
			int calculateUntilMonth = cal.get(Calendar.MONTH);
			int calculateUntilYear  = cal.get(Calendar.YEAR);
			
			int diffInMonths = 0;
			diffInMonths = (calculateUntilYear - payedUntilYear)*12;
			diffInMonths += (calculateUntilMonth - payedUntilMonth);

			switch (sub.getBooksPackage().getBillingCycle()) {
				case MONTHLY:
					months = diffInMonths;
					break;
				
				case QUARTER:
					// auf nächstes Vielfaches von 3 aufrunden
					months = ((diffInMonths+2)/3)*3;
					break;
				
				case ANNUAL:
					// auf nächstes Vielfaches von 12 aufrunden
					//months = ((diffInMonths+11)/12)*12;
					months = (12-payedUntilMonth-1) + (calculateUntilYear-payedUntilYear)*12;
					break;
				
				default:
					break;
			}
			
			// new payed Until Date
			cal.setTime(sub.getPayedUntil());
			cal.add(Calendar.MONTH, months);
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			newPayedUntilDate = cal.getTime();
			
			cal.setTime(sub.getPayedUntil());
			cal.add(Calendar.DAY_OF_MONTH, 1);
			Date payFromDate = cal.getTime();
			
			text = dateFormat.format(payFromDate) + " - " + dateFormat.format(newPayedUntilDate);
		}
		
		if (months <= 0) {
			return null;
		}
		
		ConsumationEntry entry = new ConsumationEntry(sub.getBookedBy());
		entry.setText(sub.getBooksPackage().getName() + " (" + text + ")");
		entry.setDate(new Date());
		entry.setPrice(sub.getPriceOverruled());
		entry.setUnit("Monat");
		entry.setQuantity(months);
		
		sub.setPayedUntil(newPayedUntilDate);
		
		return entry;
	}

}
