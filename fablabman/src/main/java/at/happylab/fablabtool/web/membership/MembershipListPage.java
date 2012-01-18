package at.happylab.fablabtool.web.membership;

import java.util.Date;

import javax.inject.Inject;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.dataprovider.UserProvider;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.MembershipStatus;
import at.happylab.fablabtool.model.MembershipType;
import at.happylab.fablabtool.model.User;
import at.happylab.fablabtool.panels.EnumPropertyColumn;
import at.happylab.fablabtool.panels.LinkPropertyColumn;
import at.happylab.fablabtool.web.authentication.AdminBasePage;

public class MembershipListPage extends AdminBasePage {

	@Inject
	private MembershipManagement membershipMgmt;
	
	@Inject UserProvider userProvider;

	public MembershipListPage() {
		
		add(new Label("mitgliederLabel", "Mitglieder"));

		final Form<String> form = new Form<String>("main");

		IColumn[] columns = new IColumn[11];
		// nr, Vorname, Nachname, Art der Mitgliedschaft, Firmenname, Telefonnummer, Email, Eintrittsdatum, (evtl. Austrittsdatum), Kommentar )
		columns[0] = new LinkPropertyColumn(new Model<String>("Nr"), "memberId", "membership.memberId") {
			@Override
			public void onClick(Item item, String componentId, IModel model) {
				Membership m = ((User)model.getObject()).getMembership();
				setResponsePage(new MembershipDetailPage(m, membershipMgmt));
				
			}
		};
		columns[1] = new PropertyColumn<String>(new Model<String>("Vorname"), "firstname", "firstname");
		columns[2] = new PropertyColumn<String>(new Model<String>("Nachname"), "lastname", "lastname");
		columns[3] = new EnumPropertyColumn<MembershipStatus>(new Model<String>("Art"), "type", "membership.type", MembershipStatus.class, this);
//		columns[3] = new PropertyColumn<MembershipStatus>(new Model<String>("Art"), "type", "membership.type") {
//			@Override
//			public void populateItem(
//					Item<ICellPopulator<MembershipStatus>> item,
//					String componentId, IModel<MembershipStatus> rowModel) {
//				
//				
//				item.add(new Label(componentId, new PropertyModel(rowModel, getPropertyExpression())){
//					private static final long serialVersionUID = 1L;
//
//					@Override
//					public IConverter getConverter(Class<?> type) {
//						return new EnumConverter<MembershipStatus>(MembershipStatus.class, this);
//					}
//				});
//			}
//		};
		columns[4] = new PropertyColumn<String>(new Model<String>("Firmenname"), "companyName", "membership.companyName");
		columns[5] = new PropertyColumn<String>(new Model<String>("Telefonnummer"), "phone", "membership.phone");
		columns[6] = new PropertyColumn<String>(new Model<String>("Email"), "email", "membership.email");
		columns[7] = new PropertyColumn<Date>(new Model<String>("Eintrittsdatum"), "entryDate", "membership.entryDate");
		columns[8] = new PropertyColumn<Date>(new Model<String>("Austrittsdatum"), "leavingDate", "membership.leavingDate");
		columns[9] = new PropertyColumn<String>(new Model<String>("Kommentar"), "comment", "membership.comment");
		columns[10] = new LinkPropertyColumn(new Model<String>("Edit"),new Model<String>("Edit")) {
			@Override
			public void onClick(Item item, String componentId, IModel model) {
				Membership m = ((User)model.getObject()).getMembership();
				setResponsePage(new MembershipDetailPage(m, membershipMgmt));
				
			}
	};
		
		form.add(new DefaultDataTable<User>("mitgliederTabelle", columns, userProvider, 5));
		
		form.add(new Label("mitgliederAnzahl", userProvider.size() + " Datensätze"));

		form.add(new Link("addPrivateMembershipLink") {
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



