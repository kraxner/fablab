package at.happylab.fablabtool.wicket;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class MainNavigationTests {

	private static WebClient webClient;
	private static HtmlPage page;

	@BeforeClass
	public static void setUpClass() throws Exception {
		webClient = new WebClient();
		page = webClient.getPage("http://localhost:8080/fablabman");

		final HtmlForm form = (HtmlForm) page.getElementById("signInForm1");

		form.getInputByName("username").setValueAttribute("jb"); // Correct Login Data
		form.getInputByName("password").setValueAttribute("jb");

		page = form.getInputByName("submit").click(); // Reuse page 

	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		webClient.closeAllWindows();
	}

	@Test
	public void TestAllNavigationLinks() {

		assertNotNull(page.getElementById("menubar"));
		assertNotNull(page.getElementById("mainmenu"));

		assertNotNull(page.getAnchorByText("Mitglieder"));
		assertNotNull(page.getAnchorByText("Subscriptions"));
		assertNotNull(page.getAnchorByText("Rechnungen"));
		assertNotNull(page.getAnchorByText("Stammdaten"));
		assertNotNull(page.getAnchorByText("Aufgaben"));

	}

	@Test
	public void TestNavigationToMitglieder() throws Exception {

		page = page.getAnchorByText("Mitglieder").click();

		assertEquals("Fab Lab - BasePage", page.getTitleText());

		assertTrue(page.asXml().contains("<h1 wicket:id=\"mitgliederLabel\">"));

		final HtmlForm form = (HtmlForm) (page.getForms().get(0));
		assertNotNull(form);
		
		assertNotNull(page.getElementByName("showPreRegistrationsCheckbox"));
		assertNotNull(page.getElementByName("showInactiveMembershipsCheckbox"));
		assertNotNull(page.getElementByName("filterInput"));
		
	}

	@Test
	public void TestNavigationToSubscriptions() throws Exception {

		page = page.getAnchorByText("Subscriptions").click();

		assertEquals("Fab Lab - BasePage", page.getTitleText());

		assertTrue(page.asXml().contains("<wicket:container wicket:id=\"topToolbars\">"));
		assertTrue(page.asXml().contains("<span wicket:id=\"subscriptionCount\">"));

	}
	
	@Test
	public void TestNavigationToRechnungen() throws Exception {

		page = page.getAnchorByText("Rechnungen").click();

		assertEquals("Fab Lab - BasePage", page.getTitleText());
		assertTrue(page.asText().contains("Rechnungen"));

		assertTrue(page.asXml().contains("<wicket:container wicket:id=\"topToolbars\">"));
		assertTrue(page.asXml().contains("<wicket:container wicket:id=\"bottomToolbars\">"));

	}
	
	@Test
	public void TestNavigationToStammdaten() throws Exception {

		page = page.getAnchorByText("Stammdaten").click();

		assertEquals("Fab Lab - BasePage", page.getTitleText());
		
		assertTrue(page.asText().contains("Stammdaten"));

		assertNotNull(page.getAnchorByText("Pakete"));
		assertNotNull(page.getAnchorByText("Keycards"));
		assertNotNull(page.getAnchorByText("Zugangszeiten"));
		assertNotNull(page.getAnchorByText("Consumables"));
		assertNotNull(page.getAnchorByText("Subscriptions"));
		assertNotNull(page.getAnchorByText("Geräte"));
		assertNotNull(page.getAnchorByText("Mitglieder"));
		assertNotNull(page.getAnchorByText("Administratoren"));
		assertNotNull(page.getAnchorByText("Access Test"));
		assertNotNull(page.getAnchorByText("Create Test Data"));

	}


}
