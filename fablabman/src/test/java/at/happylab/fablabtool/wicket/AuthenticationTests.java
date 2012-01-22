package at.happylab.fablabtool.wicket;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class AuthenticationTests {
	
	private static WebClient webClient;
	private static HtmlPage page;
	
	@BeforeClass
    public static void setUpClass() throws Exception{
		webClient = new WebClient();
		page = webClient.getPage("http://localhost:8080/fablabman");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    	 webClient.closeAllWindows();
    }
	
	
	@Test
	public void TestHomePage() {
	    
	    assertEquals("Fab Lab - Anmelden", page.getTitleText());

	    assertTrue(page.asXml().contains("<span wicket:id=\"signInPanel\" id=\"signInPanel\">"));

	   
	}
	
	/**
	 * Logging in with incorrect login data
	 * @throws Exception
	 */
	@Test
	public void Test_NoAccess1() throws Exception {
		
		final HtmlForm form =  (HtmlForm) page.getElementById("signInForm1");
		
		form.getInputByName("username").setValueAttribute("");
		form.getInputByName("password").setValueAttribute("");	// No Inputs
		
		form.getInputByName("submit").click();
		
		assertEquals("Fab Lab - Anmelden", page.getTitleText());
		assertTrue(page.asXml().contains("<span wicket:id=\"signInPanel\" id=\"signInPanel\">"));
		
		form.getInputByName("username").setValueAttribute("jb");
		form.getInputByName("password").setValueAttribute("xxxxx");	// Wrong password!!!
		
		form.getInputByName("submit").click();
		
		assertEquals("Fab Lab - Anmelden", page.getTitleText());
		assertTrue(page.asXml().contains("<span wicket:id=\"signInPanel\" id=\"signInPanel\">"));
		
	}
	
	/**
	 * Logging in with incorrect login data
	 * @throws Exception
	 */
	@Test
	public void Test_NoAccess2() throws Exception {
		
		final HtmlForm form =  (HtmlForm) page.getElementById("signInForm1");
		
		form.getInputByName("username").setValueAttribute("jb");
		form.getInputByName("password").setValueAttribute("");	// No password!!!
		
		form.getInputByName("submit").click();
		
		assertEquals("Fab Lab - Anmelden", page.getTitleText());
		assertTrue(page.asXml().contains("<span wicket:id=\"signInPanel\" id=\"signInPanel\">"));
		
	}

	/**
	 * Logging in with incorrect login data
	 * @throws Exception
	 */
	@Test
	public void Test_NoAccess3() throws Exception {
		
		final HtmlForm form =  (HtmlForm) page.getElementById("signInForm1");
		
		form.getInputByName("username").setValueAttribute("jb");
		form.getInputByName("password").setValueAttribute("xxxx");	// wrong password!!!
		
		form.getInputByName("submit").click();
		
		assertEquals("Fab Lab - Anmelden", page.getTitleText());
		assertTrue(page.asXml().contains("<span wicket:id=\"signInPanel\" id=\"signInPanel\">"));
		
	}
	
	/**
	 * Logging in with incorrect login data
	 * @throws Exception
	 */
	@Test
	public void Test_Access1() throws Exception {
		
		final HtmlForm form =  (HtmlForm) page.getElementById("signInForm1");
		
		form.getInputByName("username").setValueAttribute("jb");	// Correct Login Data
		form.getInputByName("password").setValueAttribute("jb");
		
		page = form.getInputByName("submit").click();
		
		assertNotSame("Fab Lab - Anmelden", page.getTitleText());	// Not the login page anymore
		assertEquals("Fab Lab - BasePage", page.getTitleText());
	}

}
