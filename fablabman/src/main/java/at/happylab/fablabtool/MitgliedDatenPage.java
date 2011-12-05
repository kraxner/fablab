package at.happylab.fablabtool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.model.Gender;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.MembershipType;
import at.happylab.fablabtool.model.PaymentMethod;
import at.happylab.fablabtool.model.User;

public class MitgliedDatenPage extends MitgliedDetailPage {
	
	//@Inject private MembershipManagement membershipMgmt;
	private MembershipManagement membershipMgmt;
	private Membership member;

	public MitgliedDatenPage(Membership member, MembershipManagement membershipMgmt) {
		this.member = member;
		this.membershipMgmt = membershipMgmt;
		add(new FeedbackPanel("feedback"));
		add(new MemberForm("form", member));
	}

	class MemberForm extends Form {
		public MemberForm(String s, final Membership member) {
			super(s, new CompoundPropertyModel(member));

			ListView listView = new ListView("list", member.getUsers()) {
				protected void populateItem(ListItem item)
				{
					User user = (User)item.getModelObject();
					user.setId(member.getId());
					item.add(new TextField("first", new PropertyModel(user, "firstname")));
					item.add(new TextField("last", new PropertyModel(user, "lastname")));
					
					List<Gender> l = new ArrayList<Gender>(2);
                    l.add(Gender.FEMALE);
                    l.add(Gender.MALE);
					DropDownChoice<Gender> gender = new DropDownChoice<Gender>("gender", new PropertyModel(user, "gender"), l);
		            item.add(gender);
		            
					item.add(new TextField("email", new PropertyModel(user, "email")));
					item.add(new TextField("mobile", new PropertyModel(user, "mobile")));
				}
			};
			listView.setReuseItems(true);
			add(listView);
			
			add(new TextField("Address.street"));
			add(new TextField("Address.city"));
			add(new TextField("Address.zipCode"));

			DropDownChoice<MembershipType> memType = new DropDownChoice<MembershipType>("type");
			memType.setChoices(new LoadableDetachableModel<List<MembershipType>>() {
				public List<MembershipType> load() {
                    List<MembershipType> list = new ArrayList<MembershipType>(2);
                    list.add(MembershipType.REGULAR);
                    list.add(MembershipType.ASSOCIATE);
                    list.add(MembershipType.HONORARY);
                    return list;
                }
            });
            add(memType);
            
            DropDownChoice<PaymentMethod> payMeth = new DropDownChoice<PaymentMethod>("paymentMethod");
            payMeth.setChoices(new LoadableDetachableModel<List<PaymentMethod>>() {
				public List<PaymentMethod> load() {
                    List<PaymentMethod> list = new ArrayList<PaymentMethod>(2);
                    list.add(PaymentMethod.DEBIT);
                    list.add(PaymentMethod.CASH_IN_ADVANCE);
                    list.add(PaymentMethod.ON_ACCOUNT);
                    return list;
                }
            });
            add(payMeth);
            
			add(new TextField("bankDetails.iban"));
			add(new TextArea<String>("comment"));
			add(new Button("submit"));
		}

		public void onSubmit() {
			membershipMgmt.storeMembership(member);
			//setResponsePage(new MitgliedDatenPage(member, membershipMgmt));
			//setResponsePage(new MitgliederPage());
			//setResponsePage(MitgliederPage.class);
		}
	}

}
