package at.happylab.fablabtool.web.membership;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import at.happylab.fablabtool.beans.MembershipManagement;
import at.happylab.fablabtool.model.BusinessMembership;
import at.happylab.fablabtool.model.Gender;
import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.MembershipType;
import at.happylab.fablabtool.model.PaymentMethod;
import at.happylab.fablabtool.model.User;

public class DataPanel extends Panel{
	private static final long serialVersionUID = -5731106086301274951L;
	
	
	private MembershipManagement membershipMgmt;
	private Membership member;
	

	public DataPanel(String id, Membership member,  MembershipManagement membershipMgmt) {
		super(id);
		
		this.member = member;
		this.membershipMgmt = membershipMgmt;
		
		add(new MemberForm("form", member));
	}

	class MemberForm extends Form<Object> {
		private static final long serialVersionUID = -416444319008642513L;

		public MemberForm(String s, final Membership member) {
			super(s, new CompoundPropertyModel<Object>(member));
			
			final boolean isBusiness = member instanceof BusinessMembership;

			ListView<Object> listView = new ListView<Object>("list", member.getUsers()) {
				private static final long serialVersionUID = 5922287160870873368L;

				protected void populateItem(ListItem<Object> item)
				{
					User user = (User)item.getModelObject();
					user.setId(member.getId());
					item.add(new TextField<Object>("first", new PropertyModel<Object>(user, "firstname")));
					item.add(new TextField<Object>("last", new PropertyModel<Object>(user, "lastname")));
					
					List<Gender> l = new ArrayList<Gender>(2);
                    l.add(Gender.FEMALE);
                    l.add(Gender.MALE);
					DropDownChoice<Gender> gender = new DropDownChoice<Gender>("gender", new PropertyModel<Gender>(user, "gender"), l);
		            item.add(gender);
		            
					item.add(new TextField<Object>("email", new PropertyModel<Object>(user, "email")));
					item.add(new TextField<Object>("mobile", new PropertyModel<Object>(user, "mobile")));
				}
			};
			listView.setReuseItems(true);
			add(listView);
			
			MarkupContainer enclosure = new WebMarkupContainer("businessEnclosure") {
				public boolean isVisible() {
					return isBusiness; 
				}
			};

			enclosure.add(new TextField<Object>("name"));
			enclosure.add(new TextField<Object>("contactPerson"));
			add(enclosure);
			
			
			add(new TextField<Object>("Address.street"));	// in HTML code
			add(new TextField<Object>("Address.city"));
			add(new TextField<Object>("Address.zipCode"));

			DropDownChoice<MembershipType> memType = new DropDownChoice<MembershipType>("type");
			memType.setChoices(new LoadableDetachableModel<List<MembershipType>>() {
				private static final long serialVersionUID = -314703471719830931L;

				public List<MembershipType> load() {
                    List<MembershipType> list = new ArrayList<MembershipType>(3);
                    list.add(MembershipType.REGULAR);
                    list.add(MembershipType.ASSOCIATE);
                    list.add(MembershipType.HONORARY);
                    return list;
                }
            });
            add(memType);
            
            DropDownChoice<PaymentMethod> payMeth = new DropDownChoice<PaymentMethod>("paymentMethod");
            payMeth.setChoices(new LoadableDetachableModel<List<PaymentMethod>>() {
				private static final long serialVersionUID = 4420436576098934666L;

				public List<PaymentMethod> load() {
                    List<PaymentMethod> list = new ArrayList<PaymentMethod>(3);
                    list.add(PaymentMethod.DEBIT);
                    list.add(PaymentMethod.CASH_IN_ADVANCE);
                    list.add(PaymentMethod.ON_ACCOUNT);
                    return list;
                }
            });
            add(payMeth);
            
			add(new TextField<Object>("bankDetails.iban"));
			add(new TextArea<String>("comment"));
			
			
			add(new Button("submit"));
		}

		public void onSubmit() {
			membershipMgmt.storeMembership(member);
			//setResponsePage(new MitgliedDatenPage(member, membershipMgmt));
			//setResponsePage(new MitgliederPage());
			setResponsePage(MembershipListPage.class);
		}
	}
}
