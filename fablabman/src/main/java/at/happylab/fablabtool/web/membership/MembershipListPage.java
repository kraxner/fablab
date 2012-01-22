package at.happylab.fablabtool.web.membership;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.dataprovider.UserProvider;
import at.happylab.fablabtool.markup.html.repeater.data.table.EnumPropertyColumn;
import at.happylab.fablabtool.markup.html.repeater.data.table.LinkPropertyColumn;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.MembershipType;
import at.happylab.fablabtool.model.User;
import at.happylab.fablabtool.web.authentication.AdminBasePage;

/**
 * 
 * @author Michael Kraxner
 *
 */
public class MembershipListPage extends AdminBasePage {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Inject private MembershipManagement membershipMgmt;
	@Inject private UserProvider userProvider;
	
	public MembershipListPage() {
		navigation.selectMitglieder();

		add(new Label("mitgliederLabel", "Mitglieder"));

		final Form<User> form = new Form<User>("main");

		// nr, Vorname, Nachname, Art der Mitgliedschaft, Firmenname, Telefonnummer, Email, Eintrittsdatum, (evtl. Austrittsdatum), Kommentar )
		List<IColumn> columns = new ArrayList<IColumn>();
		columns.add(new LinkPropertyColumn(new Model<String>("Nr"), "membership.memberId", "membership.memberId") {
			private static final long serialVersionUID = 7710445535887840657L;
			@Override
			public void onClick(Item item, String componentId, IModel model) {
				Membership m = ((User) model.getObject()).getMembership();
				setResponsePage(new MembershipDetailPage(m, membershipMgmt));
			}
		});
		columns.add(new PropertyColumn<String>(new Model<String>("Vorname"), "firstname", "firstname"));
		columns.add(new PropertyColumn<String>(new Model<String>("Nachname"), "lastname", "lastname"));
		columns.add(new EnumPropertyColumn<MembershipType>(new Model<String>("Art"), "membership.membershipType", "membership.membershipType", MembershipType.class, this));
		columns.add(new PropertyColumn<String>(new Model<String>("Firmenname"), "membership.companyName", "membership.companyName"));
		columns.add(new PropertyColumn<String>(new Model<String>("Telefonnummer"), "membership.phone", "membership.phone"));
		columns.add(new PropertyColumn<String>(new Model<String>("Email"), "email", "membership.email"));
		columns.add(new PropertyColumn<Date>(new Model<String>("Eintrittsdatum"), "membership.entryDate", "membership.entryDate"));
		columns.add(new PropertyColumn<Date>(new Model<String>("Austrittsdatum"), "membership.leavingDate", "membership.leavingDate"));
		columns.add(new PropertyColumn<String>(new Model<String>("Kommentar"), "membership.comment", "membership.comment"));
		columns.add(new LinkPropertyColumn<String>(new Model<String>("Edit"), new Model<String>("Edit")) {
			private static final long serialVersionUID = -3742657567973765L;

			@Override
			public void onClick(Item item, String componentId, IModel model) {
				Membership m = ((User) model.getObject()).getMembership();
				setResponsePage(new MembershipDetailPage(m, membershipMgmt));

			}
		});

		final DefaultDataTable<User> membershipTable = new DefaultDataTable("mitgliederTabelle", columns, userProvider, 50); 
		form.add(membershipTable);
		membershipTable.setOutputMarkupId(true); 
		
		final CheckBox showPreRegistrationsCheckbox = new CheckBox("showPreRegistrationsCheckbox", userProvider.getShowPreRegistrations());
		form.add(showPreRegistrationsCheckbox);
		showPreRegistrationsCheckbox.add(new AjaxFormComponentUpdatingBehavior("onchange") {
		       @Override
		       protected void onUpdate(AjaxRequestTarget target) {
		         target.addComponent(membershipTable);
		       }    
		});
		
		final CheckBox showInactiveMembershipsCheckbox = new CheckBox("showInactiveMembershipsCheckbox", userProvider.getShowInactiveMamberships());
		form.add(showInactiveMembershipsCheckbox);
		showPreRegistrationsCheckbox.add(new AjaxFormComponentUpdatingBehavior("onchange") {
		       @Override
		       protected void onUpdate(AjaxRequestTarget target) {
		         target.addComponent(membershipTable);
		       }    
		});
		
		final TextField<String> filterInput = new TextField<String>("filterInput", userProvider.getFilterModel());
		form.add( filterInput );
		filterInput.add(new AjaxFormComponentUpdatingBehavior("onchange"){
		       @Override
		       protected void onUpdate(AjaxRequestTarget target) {
		         target.addComponent(membershipTable);
		       }    
		    });
		
		
		//form.add(new Label("mitgliederAnzahl", userProvider.size() + " Datensätze"));
		form.add(new Link("addPrivateMembershipLink") {
			private static final long serialVersionUID = -146742357652575068L;

			public void onClick() {
				Membership m = new Membership();
				m.addUser(new User());
				m.setMembershipType(MembershipType.PRIVATE);
				setResponsePage(new MembershipDetailPage(m, membershipMgmt));
			}
		});
		
		add(form);
	}

}
