package at.happylab.fablabtool.wicket;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class NavigationTests {

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
	public void TestNavigationMitglieder() throws Exception {

		page = page.getAnchorByText("Mitglieder").click();
		
		assertEquals("Fab Lab - BasePage", page.getTitleText());
		
		assertTrue(page.asXml().contains("<h1 wicket:id=\"mitgliederLabel\">"));
		assertTrue(page.asXml().contains("<table wicket:id=\"mitgliederTabelle\">"));
		assertTrue(page.asXml().contains("<span wicket:id=\"mitgliederAnzahl\">"));

	}
	
	@Test
	public void TestNavigationSubscriptions() throws Exception {

		page = page.getAnchorByText("Subscriptions").click();
		
		assertEquals("Fab Lab - BasePage", page.getTitleText());
		
		assertTrue(page.asXml().contains("<h1 wicket:id=\"subscriptionLabel\">"));
		assertTrue(page.asXml().contains("<table wicket:id=\"subscriptionTabelle\">"));
		assertTrue(page.asXml().contains("<span wicket:id=\"subscriptionAnzahl\">"));

	}

}
