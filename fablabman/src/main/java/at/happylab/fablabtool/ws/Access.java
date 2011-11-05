package at.happylab.fablabtool.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/keycard")
@Produces(MediaType.TEXT_PLAIN)
public interface Access {

	@GET
	@Path("/{rfId}/access")
	public abstract String mayEnter(@PathParam("rfId") String rfid);

}