package at.happylab.fablabtool.web.maintenance.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.inject.Inject;

import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.beans.PackageManagement;
import at.happylab.fablabtool.beans.SubscriptionManagement;
import at.happylab.fablabtool.model.Address;
import at.happylab.fablabtool.model.DebitInfo;
import at.happylab.fablabtool.model.Gender;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.MembershipType;
import at.happylab.fablabtool.model.PaymentMethod;
import at.happylab.fablabtool.model.Subscription;
import at.happylab.fablabtool.model.User;
import at.happylab.fablabtool.web.authentication.AdminBasePage;

public class CreateTestDataPage extends AdminBasePage {

	@Inject
	private MembershipManagement membershipMgmt;
	
	@Inject
	SubscriptionManagement subscriptionMgmt;
	
	@Inject
	PackageManagement packageMgmt;
	
	
	public CreateTestDataPage() {
		System.out.println("Creating Test Data");
		
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
		
		
		try {
			BufferedReader in = new BufferedReader(new FileReader("/Users/karim/Dropbox/University/ASE/dev/workspace/qse-ase-ws11-07/data.csv"));
			String line;
			String[] items;
			
			while ((line = in.readLine()) != null) {
				items = line.split(";");
				
				Membership m = new Membership();
				User u = new User();
				
				m.setMemberId(Long.parseLong(items[0]));
				u.setFirstname(items[1]);
				u.setLastname(items[2]);
				if (!items[7].equals("")) {
					u.setBirthday(dateFormat.parse(items[7]));
				}
				u.setEmail(items[9]);
				if (items[3].equals("m")) {
					u.setGender(Gender.MALE);
				}
				else {
					u.setGender(Gender.FEMALE);
				}
				u.setPhone(items[10]);
				
				Address a = new Address();
				a.setStreet(items[11]);
				a.setZipCode(items[12]);
				a.setCity(items[13]);
				
				if (items[4].equals("P")) {
					m.setMembershipType(MembershipType.PRIVATE);
					u.setAddress(a);
				}
				else {
					m.setMembershipType(MembershipType.BUSINESS);
					m.setCompanyName(items[8]);
					m.setCompanyAddress(a);
				}
				
				m.addUser(u);
				
				try {
					m.setEntryDate(dateFormat.parse(items[14]));
				}
				catch (Exception e2) {
					
				}
				
				try {
					m.setLeavingDate(dateFormat.parse(items[15]));
				}
				catch (Exception e3) {
					
				}
				
				
				try {
					if (!items[18].equals("")) {
						m.setPaymentMethod(PaymentMethod.DEBIT);
						DebitInfo b = new DebitInfo();
						b.setName(u.getFirstname() + " " + u.getLastname());
						b.setIban(items[18]);
						b.setBic(items[19]);
						m.setBankDetails(b);
					}
					else {
						m.setPaymentMethod(PaymentMethod.CASH_IN_ADVANCE);
					}
				}
				catch (Exception e1) {
					m.setPaymentMethod(PaymentMethod.CASH_IN_ADVANCE);
				}
				
				membershipMgmt.storeMembership(m);
			}
			
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
		try {
			BufferedReader in = new BufferedReader(new FileReader("/Users/karim/Dropbox/University/ASE/dev/workspace/qse-ase-ws11-07/pakete.csv"));
			String line;
			String[] items;
			
			while ((line = in.readLine()) != null) {
				items = line.split(";");
				
				Membership member = membershipMgmt.loadMembershipFromMemberId(Integer.parseInt(items[0]));
				System.out.println(member);
				System.out.println(line);
				
				// hardcoded subscriptions
				// 1 = Ad Infinitum
				at.happylab.fablabtool.model.Package ai = packageMgmt.loadPackage(1);
				
				// 2 = Carpe Diem
				at.happylab.fablabtool.model.Package cd = packageMgmt.loadPackage(2);
				
				// 3 = Storage
				at.happylab.fablabtool.model.Package st = packageMgmt.loadPackage(3);
				
				
				try {
					if (!items[4].equals("")) {
						Subscription s = new Subscription();
						s.setBookedBy(member);
						
						s.setBooksPackage(ai);
						s.setValidFrom(dateFormat.parse("01.01.12"));
						s.setPriceOverruled(ai.getPrice());
						
						subscriptionMgmt.storeSubscription(s);
					}
				}
				catch (Exception e1) {
					
				}
				
				try {
					if (!items[5].equals("")) {
						Subscription s = new Subscription();
						s.setBookedBy(member);
						
						s.setBooksPackage(cd);
						s.setValidFrom(dateFormat.parse("01.01.12"));
						s.setPriceOverruled(cd.getPrice());
						s.setDescription(items[9]);
						
						subscriptionMgmt.storeSubscription(s);
					}
				}
				catch (Exception e1) {
					
				}
				
				try {
					if (!items[6].equals("")) {
						Subscription s = new Subscription();
						s.setBookedBy(member);
						
						s.setBooksPackage(st);
						s.setValidFrom(dateFormat.parse("01.01.12"));
						s.setPriceOverruled(st.getPrice());
						s.setDescription(items[8]);
						
						subscriptionMgmt.storeSubscription(s);
					}
				}
				catch (Exception e1) {
						
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
