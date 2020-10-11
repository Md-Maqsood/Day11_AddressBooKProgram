package com.BridgeLabs.addressBookProgram;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AddressBook implements ManageAddressBook {
	private static final Logger logger = LogManager.getLogger(AddressBook.class);
	static Scanner sc = new Scanner(System.in);
	static Map<String, AddressBook> nameToAddressBookMap = new HashMap<String, AddressBook>();
	public String name;
	public List<Contact> contacts;
	public Map<String, Contact> nameToContactMap;

	public AddressBook(String name) {
		super();
		this.name = name;
		this.contacts = new LinkedList<Contact>();
		this.nameToContactMap = new LinkedHashMap<String, Contact>();
	}

	/**
	 *uc7
	 */
	public void addContacts() {
		do {
			logger.debug("Enter the contact details in order: \nfirst_name\nlastname\naddress\ncity\nstate\nzip\nphone no.\nemail");
			Contact newContact = new Contact(sc.nextLine(), sc.nextLine(), sc.nextLine(), sc.nextLine(), sc.nextLine(),
					Integer.parseInt(sc.nextLine()), Long.parseLong(sc.nextLine()), sc.nextLine());
			if (contacts.stream().anyMatch(contact -> contact.equals(newContact))) {
				logger.debug("Same entry already present. Cannot allow duplicate entries in an address book.");
			} else {
				this.contacts.add(newContact);
				this.nameToContactMap.put(newContact.getFirstName() + " " + newContact.getLastName(), newContact);
			}
			logger.debug("Enter 1 to add another contact, else enter 0: ");
		} while (Integer.parseInt(sc.nextLine()) == 1);

	}

	public void editContact() {
		do {
			logger.debug("Enter name of person whose contact details are to be edited: ");
			String name = sc.nextLine();
			logger.debug("Enter the new fields in order: \naddress\ncity\nstate\nzip\nphone no.\nemail");
			try {
				Contact toBeEditedContact=nameToContactMap.get(name);
				toBeEditedContact.setAddress(sc.nextLine());
				toBeEditedContact.setCity(sc.nextLine());
				toBeEditedContact.setState(sc.nextLine());
				toBeEditedContact.setZip(Integer.parseInt(sc.nextLine()));
				toBeEditedContact.setPhoneNumber(Long.parseLong(sc.nextLine()));
				toBeEditedContact.setEmail(sc.nextLine());
				logger.debug("Contact after editing:\n"+toBeEditedContact);
			} catch (NullPointerException e) {
				logger.debug("No contact found with that name.");
			}
			logger.debug("Enter 1 to edit another contact, else enter 0: ");
		} while (Integer.parseInt(sc.nextLine()) == 1);
	}

	public void deleteContact() {
		do {
			logger.debug("Enter the name of Contact person to be deleted: ");
			String name = sc.nextLine();
			Contact toBeDeletedContact=nameToContactMap.get(name);
			contacts.remove(toBeDeletedContact);
			nameToContactMap.remove(name);
			logger.debug("Address Book after deletion of contact: \n" + this);
			logger.debug("Enter 1 to delete another contact, else enter 0: ");
		} while (Integer.parseInt(sc.nextLine()) == 1);
	}

	public static void addAddressBooks() {
		while (true) {
			logger.debug("1.Add an address book\n2.Exit\nEnter your choice: ");
			int choice = Integer.parseInt(sc.nextLine());
			if (choice == 1) {
				logger.debug("Enter name of the address book");
				String name = sc.nextLine();
				nameToAddressBookMap.put(name, new AddressBook(name));
			} else if (choice == 2) {
				break;
			} else {
				logger.debug("Invalid choice. Try again.");
			}
		}
	}

	@Override
	public String toString() {
		return "Address Book " + name + " with " + contacts.size() + (contacts.size() == 1 ? " contact" : " contacts");
	}

	public static void main(String[] args) {
		addAddressBooks();
		do {
			logger.debug("Enter the name of the address book to continue: ");
			AddressBook addressBook = nameToAddressBookMap.get(sc.nextLine());
			if (addressBook == null) {
				logger.debug("No address book found with that name.");
			} else {
				addressBook.addContacts();
				logger.debug(addressBook);
				addressBook.editContact();
				addressBook.deleteContact();
			}
			logger.debug("Enter 1 to continue with another address book, else enter 0: ");
		} while (Integer.parseInt(sc.nextLine()) == 1);
		sc.close();
	}

}

interface ManageAddressBook {
	public void addContacts();

	public void editContact();

	public void deleteContact();
}

class Contact {
	private String firstName;
	private String lastName;
	private String address;
	private String city;
	private String state;
	private int zip;
	private long phoneNumber;
	private String email;

	public Contact(String firstName, String lastName, String address, String city, String state, int zip,
			long phoneNumber, String email) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.phoneNumber = phoneNumber;
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getZip() {
		return zip;
	}

	public void setZip(int zip) {
		this.zip = zip;
	}

	public long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Contact: " + firstName + " " + lastName + ", " + address + ", " + city + ", " + state + ", " + zip
				+ ", " + phoneNumber + "\n" + email + ".";
	}

	@Override
	public boolean equals(Object obj) {
		Contact checkContact = (Contact) obj;
		return (checkContact.getFirstName().equals(this.firstName))
				&& (checkContact.getLastName().contentEquals(this.lastName));
	}

}
