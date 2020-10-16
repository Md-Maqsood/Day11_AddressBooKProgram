package com.BridgeLabs.addressBookProgram;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AddressBook implements ManageAddressBook {
	
	public enum SearchBy {
		CITY, STATE
	}
	
	private static final Logger logger = LogManager.getLogger(AddressBook.class);
	static Scanner sc = new Scanner(System.in);
	static Map<String, AddressBook> nameToAddressBookMap = new HashMap<String, AddressBook>();
	public String name;
	public List<Contact> contacts;
	public Map<String, Contact> nameToContactMap;
	public Map<String, List<Contact>> cityToContactsMap;
	public Map<String, List<Contact>> stateToContactsMap;

	public AddressBook(String name) {
		super();
		this.name = name;
		this.contacts = new LinkedList<Contact>();
		this.nameToContactMap = new LinkedHashMap<String, Contact>();
		this.cityToContactsMap = new TreeMap<String, List<Contact>>();
		this.stateToContactsMap = new TreeMap<String, List<Contact>>();
	}

	/**
	 *uc7
	 */
	public void addContacts() {
		do {
			logger.info("Enter the contact details in order: \nfirst_name\nlastname\naddress\ncity\nstate\nzip\nphone no.\nemail");
			Contact newContact = new Contact(sc.nextLine(), sc.nextLine(), sc.nextLine(), sc.nextLine(), sc.nextLine(),
					Integer.parseInt(sc.nextLine()), Long.parseLong(sc.nextLine()), sc.nextLine());
			if (contacts.stream().anyMatch(contact -> contact.equals(newContact))) {
				logger.info("Same entry already present. Cannot allow duplicate entries in an address book.");
			} else {
				this.contacts.add(newContact);
				this.nameToContactMap.put(newContact.getFirstName() + " " + newContact.getLastName(), newContact);
			}
			logger.info("Enter 1 to add another contact, else enter 0: ");
		} while (Integer.parseInt(sc.nextLine()) == 1);

	}
	
	public List<Contact> sortContactsByName() {
		return contacts.stream().sorted((contact1,contact2)->contact1.getFirstName().compareTo(contact2.getFirstName())).collect(Collectors.toList());
	}
	
	public List<Contact> sortByCityStateOrZip(){
		logger.info("Enter 1 to sort by city\n2 to sort by state\n3 to sort by zip");
		switch(Integer.parseInt(sc.nextLine())) {
		case 1:
			return contacts.stream().sorted((contact1,contact2)->contact1.getCity().compareTo(contact2.getCity())).collect(Collectors.toList());
		case 2:
			return contacts.stream().sorted((contact1,contact2)->contact1.getState().compareTo(contact2.getState())).collect(Collectors.toList());
		case 3:
			return contacts.stream().sorted((contact1,contact2)->((Integer)contact1.getZip()).compareTo((Integer)contact2.getZip())).collect(Collectors.toList());
		default:
			logger.info("Invalid Input.");
			return contacts;
		}
	}
	
	/**
	 * uc8 Method to search all contacts in a given city/state in all address books
	 */
	public static void getPersonsByCityOrState() {
		logger.info("Choose \n1 To search by city\n2 To search by state\nEnter your choice: ");
		SearchBy searchByParameter = (Integer.parseInt(sc.nextLine()) == 1) ? SearchBy.CITY : SearchBy.STATE;
		logger.info("Enter the name of " + searchByParameter.name() + ": ");
		String cityOrStateName = sc.nextLine();
		nameToAddressBookMap.keySet().stream().forEach(addressBookName -> {
			AddressBook addressBook = nameToAddressBookMap.get(addressBookName);
			logger.info("Persons in the " + searchByParameter.name() + " " + cityOrStateName + " in the address book "
					+ addressBookName + " are: ");
			addressBook.contacts.stream()
					.filter(contact -> ((searchByParameter == SearchBy.CITY ? contact.getCity() : contact.getState())
							.equals(cityOrStateName)))
					.forEach(contact -> logger.info(contact));
			logger.info("");
		});
	}
	
	/**
	 * uc9 Method to map list of contacts to cities and states in this address book
	 */
	public void generateContactsListByCityAndState() {
		Set<String> cityNames=contacts.stream().map(contact->contact.getCity()).collect(Collectors.toSet());
		Set<String> stateNames=contacts.stream().map(contact->contact.getState()).collect(Collectors.toSet());
		this.cityToContactsMap = cityNames.stream()
				.collect(Collectors.toMap(cityName -> cityName,
						cityName -> {
							return contacts.stream().filter(contact -> contact.getCity().equals(cityName)).sorted((c1, c2) -> {
								return c1.getFirstName().compareTo(c2.getFirstName());
							}).collect(Collectors.toList());
						}));
		this.stateToContactsMap = stateNames.stream()
				.collect(Collectors.toMap(stateName -> stateName,
						stateName -> {
							return contacts.stream().filter(contact -> contact.getState().equals(stateName)).sorted((c1, c2) -> {
								return c1.getFirstName().compareTo(c2.getFirstName());
							}).collect(Collectors.toList());
						}));
		}
	
	/**
	 * uc9 Method to display all contacts in all cities/states in all address books
	 */
	public static void viewPersonsByCityOrState() {
		logger.info("Choose \n1 To view by city\n2 To view by state\nEnter your choice: ");
		SearchBy viewByParameter = (Integer.parseInt(sc.nextLine()) == 1) ? SearchBy.CITY : SearchBy.STATE;
		nameToAddressBookMap.keySet().stream().forEach(addressBookName -> {
			AddressBook addressBook = nameToAddressBookMap.get(addressBookName);
			addressBook.generateContactsListByCityAndState();
			logger.info("In the address book " + addressBookName);
			logger.info("");
			(viewByParameter == SearchBy.CITY ? addressBook.cityToContactsMap.keySet()
					: addressBook.stateToContactsMap.keySet()).stream().forEach(cityOrStateName -> {
						logger.info(viewByParameter.name() + ": " + cityOrStateName);
						(viewByParameter == SearchBy.CITY ? addressBook.cityToContactsMap.get(cityOrStateName)
								: addressBook.stateToContactsMap.get(cityOrStateName)).stream()
										.forEach(contact -> logger.info(contact));
						logger.info("");
					});
			logger.info("");
		});
	}
	
	/**
	 * uc10 Method to display no. of contacts by city and state in all address books
	 */
	public static void displayCountByCityAndState() {
		nameToAddressBookMap.keySet().stream().forEach(addressBookName -> {
			AddressBook addressBook = nameToAddressBookMap.get(addressBookName);
			logger.info("In the address book " + addressBookName);
			logger.info("");
			logger.info("Contact counts by city");
			addressBook.cityToContactsMap.keySet().stream().forEach(
					cityName -> logger.info(cityName + ": " + addressBook.cityToContactsMap.get(cityName).size()));
			logger.info("\nContact counts by state");
			addressBook.stateToContactsMap.keySet().stream().forEach(
					stateName -> logger.info(stateName + ": " + addressBook.stateToContactsMap.get(stateName).size()));
			logger.info("");
		});
	}

	public void editContact() {
		do {
			logger.info("Enter name of person whose contact details are to be edited: ");
			String name = sc.nextLine();
			logger.info("Enter the new fields in order: \naddress\ncity\nstate\nzip\nphone no.\nemail");
			try {
				Contact toBeEditedContact=nameToContactMap.get(name);
				toBeEditedContact.setAddress(sc.nextLine());
				toBeEditedContact.setCity(sc.nextLine());
				toBeEditedContact.setState(sc.nextLine());
				toBeEditedContact.setZip(Integer.parseInt(sc.nextLine()));
				toBeEditedContact.setPhoneNumber(Long.parseLong(sc.nextLine()));
				toBeEditedContact.setEmail(sc.nextLine());
				logger.info("Contact after editing:\n"+toBeEditedContact);
			} catch (NullPointerException e) {
				logger.info("No contact found with that name.");
			}
			logger.info("Enter 1 to edit another contact, else enter 0: ");
		} while (Integer.parseInt(sc.nextLine()) == 1);
	}

	public void deleteContact() {
		do {
			logger.info("Enter the name of Contact person to be deleted: ");
			String name = sc.nextLine();
			Contact toBeDeletedContact=nameToContactMap.get(name);
			contacts.remove(toBeDeletedContact);
			nameToContactMap.remove(name);
			logger.info("Address Book after deletion of contact: \n" + this);
			logger.info("Enter 1 to delete another contact, else enter 0: ");
		} while (Integer.parseInt(sc.nextLine()) == 1);
	}

	public static void addAddressBooks() {
		while (true) {
			logger.info("1.Add an address book\n2.Exit\nEnter your choice: ");
			int choice = Integer.parseInt(sc.nextLine());
			if (choice == 1) {
				logger.info("Enter name of the address book");
				String name = sc.nextLine();
				nameToAddressBookMap.put(name, new AddressBook(name));
			} else if (choice == 2) {
				break;
			} else {
				logger.info("Invalid choice. Try again.");
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
			logger.info("Enter the name of the address book to continue: ");
			AddressBook addressBook = nameToAddressBookMap.get(sc.nextLine());
			if (addressBook == null) {
				logger.info("No address book found with that name.");
			} else {
				addressBook.addContacts();
				logger.info(addressBook);
				List<Contact> sortedContacts =addressBook.sortContactsByName();
				logger.info("Contacts sorted by name are: ");
				sortedContacts.forEach(logger::info);
				sortedContacts= addressBook.sortByCityStateOrZip();
				logger.info("Contact sorted according to input are: ");
				sortedContacts.forEach(logger::info);
				addressBook.editContact();
				addressBook.deleteContact();
				addressBook.generateContactsListByCityAndState();
			}
			logger.info("Enter 1 to continue with another address book, else enter 0: ");
		} while (Integer.parseInt(sc.nextLine()) == 1);
		getPersonsByCityOrState();
		viewPersonsByCityOrState();
		displayCountByCityAndState();
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
