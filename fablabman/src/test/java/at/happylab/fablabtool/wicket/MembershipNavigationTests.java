package at.happylab.fablabtool.wicket;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.hibernate.cfg.NotYetImplementedException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;

public class MembershipNavigationTests {

	private static WebClient webClient;
	private static HtmlPage page;

	@BeforeClass
	public static void setUpClass() throws Exception {
		webClient = new WebClient();
		page = webClient.getPage("http://localhost:8080/fablabman");

		HtmlForm form = (HtmlForm) page.getElementById("signInForm1");

		form.getInputByName("username").setValueAttribute("jb"); // Correct Login Data
		form.getInputByName("password").setValueAttribute("jb");

		page = form.getInputByName("submit").click(); // Reuse page

		form = page.getForms().get(0);

		page = form.getButtonsByName("btnNewMembership").get(0).click();

	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		webClient.closeAllWindows();
	}

	@Test
	public void TestNonProfitFields() throws Exception {

		// Eingabe Felder von der Non-Profit Mitgliedschaft
		assertFalse(page.asText().contains("Personendaten"));
		assertTrue(page.asText().contains("Art der Mitgliedschaft"));
		assertTrue(page.asText().contains("Non-Profit"));
		assertTrue(page.asText().contains("Adresse"));
		assertTrue(page.asText().contains("Allgemeine Daten"));
		
	}
	
	@Test
	public void TestBusninessFields() throws Exception {

		// Eingabe Felder von der Non-Profit Mitgliedschaft
		assertFalse(page.asText().contains("Personendaten"));
		assertTrue(page.asText().contains("Art der Mitgliedschaft"));
		assertTrue(page.asText().contains("Non-Profit"));
		assertTrue(page.asText().contains("Adresse"));
		assertTrue(page.asText().contains("Allgemeine Daten"));

		HtmlForm form = page.getForms().get(0);

		for (HtmlRadioButtonInput r : form.getRadioButtonsByName("membershipType"))
			if (r.getValueAttribute().contains("BUSINESS")) {
				page = form.getElementById(r.getId()).click();
			}
		
		throw new NotYetImplementedException("");
		

	}

}
