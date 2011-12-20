package at.happylab.fablabtool.web.authentication;

import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;

import at.happylab.fablabtool.BasePage;

/**
 * Base class for all pages which should be only accessible by fablab staff
 * 
 * @author Michael Kraxner
 *
 */
@AuthorizeInstantiation(Roles.ADMIN)
public abstract class AdminBasePage extends BasePage {

}
