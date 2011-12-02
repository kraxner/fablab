package at.happylab.fablabtool;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import at.happylab.fablabtool.model.Membership;
import at.happylab.fablabtool.model.MembershipType;
import at.happylab.fablabtool.model.User;

public class UserProviderX extends SortableDataProvider {

	class SortableDataProviderComparator implements Comparator<Contact>, Serializable {
		public int compare(final Contact o1, final Contact o2) {
			PropertyModel<Comparable> model1 = new PropertyModel<Comparable>(o1, getSort().getProperty());
			PropertyModel<Comparable> model2 = new PropertyModel<Comparable>(o2, getSort().getProperty());

			int result = model1.getObject().compareTo(model2.getObject());

			if (!getSort().isAscending()) {
				result = -result;
			}

			return result;
		}

	}

	private List<Contact> list = new ArrayList<Contact>();
	private SortableDataProviderComparator comparator = new SortableDataProviderComparator();

	public UserProviderX() {
		// The default sorting
		setSort("name.first",true);

		list.add(new Contact(new Name("Abby", "Zerind")));
		list.add(new Contact(new Name("Bernard", "Youst")));
		list.add(new Contact(new Name("Charlie", "Xerg")));
		list.add(new Contact(new Name("Deitri", "West")));
		list.add(new Contact(new Name("Ernie", "Vuntang")));
		list.add(new Contact(new Name("Frank", "Unter")));
		
//		User a = new User();
//		
//		a.setFirstname("Johannes");
//		a.setLastname("Bauer");
//		a.setId(1);
//		
//		Membership m = new Membership() {
//		};
//
//		m.setType(MembershipType.REGULAR);
//		
//		a.setMembership(m);
//		
//		list.add(a);
		
	}
	
	public int getCount()
	{
		return list.size();
	}

	public Iterator<Contact> iterator(final int first, final int count) {
		// In this example the whole list gets copied, sorted and sliced; in real applications typically your database would deliver a sorted and limited list 

		// Get the data
		List<Contact> newList = new ArrayList<Contact>(list);

		// Sort the data
		Collections.sort(newList, comparator);

		// Return the data for the current page - this can be determined only after sorting
		return newList.subList(first, first + count).iterator();
	}

	public IModel<Contact> model(final Object object) {
		return new AbstractReadOnlyModel<Contact>() {
			@Override
			public Contact getObject() {
				return (Contact) object;
			}
		};
	}

	public int size() {
		return list.size();
	}

}

class Contact implements Serializable {

	private final Name name;

	public Contact(final Name name) {
		this.name = name;
	}

	public Name getName() {
		return name;
	}
}

class Name implements Serializable {

	private String firstName;
	private String lastName;

	public Name(final String fName, final String lName) {
		firstName = fName;
		lastName = lName;
	}

	public String getFirst() {
		return firstName;
	}

	public String getLast() {
		return lastName;
	}

	public void setFirst(final String firstName) {
		this.firstName = firstName;
	}

	public void setLast(final String lastName) {
		this.lastName = lastName;
	}
}