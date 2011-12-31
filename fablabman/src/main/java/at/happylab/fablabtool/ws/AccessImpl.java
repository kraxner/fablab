package at.happylab.fablabtool.ws;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import at.happylab.fablabtool.beans.KeycardManagement;

@ApplicationScoped
public class AccessImpl implements Access {
	
	
	public String mayEnter(String rfid) {
		if (new KeycardManagement().hasAccess(rfid)) {
			return "welcome " + rfid;
		} else {
			return "keycard " + rfid +" must not enter";
		}
	}

}
