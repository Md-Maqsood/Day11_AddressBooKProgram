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

	public void addContacts() {
		while (true) {
			logger.debug("1.Add next Contact\n2.Exit\nEnter your choice: ");
			int choice1 = Integer.parseInt(sc.nextLine());
			if (choice1 == 1) {
				logger.debug(
						"Enter the fields in order: \nfirst_name\nlastname\naddress\ncity\nstate\nzip\nphone no.\nemail");
				Contact contact = new Contact(sc.nextLine(), sc.nextLine(), sc.nextLine(), sc.nextLine(), sc.nextLine(),
						Integer.parseInt(sc.nextLine()), Long.parseLong(sc.nextLine()), sc.nextLine());
				if (checkDuplicacy(contact)) {
					logger.debug("Same entry already present. Cannot allow duplicate entries in an address book.");
				} else {
					this.contacts.add(contact);
					this.nameToContactMap.put(contact.getFirstName() + " " + contact.getLastName(), contact);
				}
			} else {
				break;
			}
		}

	}

	/**
	 * uc7
	 * @param contact
	 * @return
	 */
	public boolean checkDuplicacy(Contact contact) {
		Contact possibleDuplicate = nameToContactMap.get(contact.getFirstName() + " " + contact.getLastName());
		if (possibleDuplicate != null) {
			if (possibleDuplicate.equals(contact)) {
				return true;
			}
		}
		return false;
	}

	public void editContact() {
		logger.debug("Enter name of person whose contact details are to be edited: ");
		String name = sc.nextLine();
		logger.debug("Enter the new fields in order: \naddress\ncity\nstate\nzip\nphone no.\nemail");
		nameToContactMap.get(name).setAddress(sc.nextLine());
		nameToContactMap.get(name).setCity(sc.nextLine());
		nameToContactMap.get(name).setState(sc.nextLine());
		nameToContactMap.get(name).setZip(Integer.parseInt(sc.nextLine()));
		nameToContactMap.get(name).setPhoneNumber(Long.parseLong(sc.nextLine()));
		nameToContactMap.get(name).setEmail(sc.nextLine());
	}

	public void deleteContact() {
		logger.debug("Enter the name of Contact person to be deleted: ");
		String name = sc.nextLine();
		contacts.remove(nameToContactMap.get(name));
		nameToContactMap.remove(name);
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
		logger.debug("Enter the name of the address book to continue: ");
		AddressBook addressBook = nameToAddressBookMap.get(sc.nextLine());
		if (addressBook == null) {
			logger.debug("No address book found with that name.");
			;
		} else {
			addressBook.addContacts();
			logger.debug(addressBook);
			logger.debug("Before edit:");
			for (Contact contact : addressBook.contacts) {
				logger.debug(contact);
			}
			addressBook.editContact();
			logger.debug("After edit");
			for (Contact contact : addressBook.contacts) {
				logger.debug(contact);
			}
			addressBook.deleteContact();
			logger.debug("After deletion of contact: \n" + addressBook);
		}
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
