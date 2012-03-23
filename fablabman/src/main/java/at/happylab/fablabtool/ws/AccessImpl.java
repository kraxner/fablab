package at.happylab.fablabtool.ws;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import at.happylab.fablabtool.beans.KeycardManagement;

@ApplicationScoped
public class AccessImpl implements Access {
	
	@Inject KeycardManagement keycardManagement;
	
	public String mayEnter(String rfid) {

		System.err.println("GOT REQUEST: " + rfid);
		
		if (keycardManagement.hasAccess(rfid)) {
			return "30000;10000;2000\r\n";
		} else {
			return "0\r\n";
		}
	}

}
