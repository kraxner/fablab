package at.happylab.fablabtool.ws;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AccessImpl implements Access {
	
	/* (non-Javadoc)
	 * @see at.happylab.fablabtool.ws.Access#mayEnter(java.lang.String)
	 */
	public String mayEnter(String rfid) {
		return "denied";
	}

}
