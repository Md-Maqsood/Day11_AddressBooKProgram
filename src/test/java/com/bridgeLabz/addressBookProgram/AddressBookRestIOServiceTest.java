package com.bridgeLabz.addressBookProgram;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AddressBookRestIOServiceTest {
	private static final Logger logger=LogManager.getLogger(AddressBookRestIOServiceTest.class);
	@Before
	public void setUp() {
		RestAssured.baseURI= "http://localhost";
		RestAssured.port = 3000;
	}
	
	@Ignore
	public void givenContactDetailsOnJsonServer_WhenReadShouldMatchCount() {
		Contact[] arrayOfContacts=this.getContactDetails();
		AddressBook addressBook=new AddressBook("Book1", Arrays.asList(arrayOfContacts));
		int numOfContacts=addressBook.countEntries();
		Assert.assertEquals(3, numOfContacts);
	}

	@Ignore
	public void givenContactDetails_WhenWritten_ShouldMatch201ResponseAndCount() {
		Contact[] arrayOfContacts=this.getContactDetails();
		AddressBook addressBook=new AddressBook("Book1", Arrays.asList(arrayOfContacts));
		Contact contactToBeWritten=new Contact(4,"Bill", "Gates", "ghk", "Dallas", "Texas", 123457, 7654321789l, "bill@gmail.com");
		Response response=this.writeContactToAddressBook(contactToBeWritten);
		int statusCode=response.getStatusCode();
		Assert.assertEquals(201,statusCode);
		Contact contactToBeAddedToAddressBook=new Gson().fromJson(response.asString(),Contact.class);
		addressBook.addContactToContactList(contactToBeAddedToAddressBook);
		int numOfContacts=addressBook.countEntries();
		Assert.assertEquals(4,numOfContacts);
	}
	
	@Ignore
	public void given3ContactDetails_WhenAddedToRestIO_ShouldSyncWithAddressBookAndMatchCount() {
		AddressBook addressBook=new AddressBook("Book1", Arrays.asList(this.getContactDetails()));
		Contact contactBill=new Contact(4,"Bill", "Gates", "ghk", "Dallas", "Texas", 123457, 7654321789l, "bill@gmail.com");
		Contact contactMike=new Contact(5,"Mike", "Tyson", "def", "Los Angeles", "California", 123232, 8796543210l, "mike@gmail.com");
		Contact contactAman=new Contact(6,"Aman", "Chaudhry", "mno", "New Delhi", "Delhi", 804567, 7867878998l, "arvind@gmail.com");
		Contact[] contactsToBeAdded= {contactBill, contactMike, contactAman};
		this.addMultipleContacts(Arrays.asList(contactsToBeAdded),addressBook);
		Assert.assertTrue(addressBook.checkIfAddressBookInSyncWithResIO("Bill", "Gates",contactBill));
		Assert.assertTrue(addressBook.checkIfAddressBookInSyncWithResIO("Mike", "Tyson",contactMike));
		Assert.assertTrue(addressBook.checkIfAddressBookInSyncWithResIO("Aman", "Chaudhry",contactAman));
		int numOfContacts=addressBook.countEntries();
		Assert.assertEquals(6, numOfContacts);
	}
	
	@Test
	public void givenContactDetails_whenUpdatedOnJsonServer_ShouldSyncWithAddressBook() {
		AddressBook addressBook=new AddressBook("Book1", Arrays.asList(this.getContactDetails()));
		Contact contactToBeUpdated=new Contact(3,"Satya", "Nadela", "ghk", "Dallas", "Texas", 123457, 7654321789l, "satya@gmail.com");
		this.updateContactDetails(contactToBeUpdated,addressBook);
		Assert.assertTrue(addressBook.checkIfAddressBookInSyncWithResIO("Satya","Nadela",contactToBeUpdated));		
	}
	
	@Ignore
	public void givenContactDetails_WhenDeletedShouldSyncWithAddressBookAndMatchTheCount() {
		AddressBook addressBook=new AddressBook("Book1", Arrays.asList(this.getContactDetails()));
		int contactIdToBedeleted=2;
		String contactFirstName="Mark";
		String contactLastName="Zukerberg";
		try {
			this.deleteContact(contactIdToBedeleted, contactFirstName, contactLastName, addressBook);
		} catch (AddressBookDBIoException e) {
			e.printStackTrace();
		}
		Assert.assertTrue(addressBook.checkIfAddressBookInSyncWithResIO(contactFirstName, contactLastName, null));
		int numOfContacts=addressBook.countEntries();
		Assert.assertEquals(2, numOfContacts);
	}
	
	private void deleteContact(int contactIdToBedeleted, String contactFirstName, String contactLastName,AddressBook addressBook) throws AddressBookDBIoException {
		RequestSpecification request=RestAssured.given();
		request.header("Content-Type","apllication/json");
		Response response=request.delete("/contacts/"+contactIdToBedeleted);
		if(response.getStatusCode()==200){
			addressBook.deleteContactFromList(contactFirstName, contactLastName);
		}
	}

	private void updateContactDetails(Contact contactToBeUpdated, AddressBook addressBook) {
		String contactJson=new Gson().toJson(contactToBeUpdated,Contact.class);
		RequestSpecification request=RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(contactJson);
		Response response=request.put("/contacts/"+contactToBeUpdated.getId());
		if(response.getStatusCode()==200) {
			addressBook.updateContactInContactsList(contactToBeUpdated);
		}
	}

	private void addMultipleContacts(List<Contact> contactsToBeAdded, AddressBook addressBook) {
		contactsToBeAdded.forEach(contact->{
			Response response=this.writeContactToAddressBook(contact);
			if(response.getStatusCode()==201) {
				addressBook.addContactToContactList(contact);
			}
		});
	}

	private Response writeContactToAddressBook(Contact contactToBeWritten) {
		String contactJson=new Gson().toJson(contactToBeWritten, Contact.class);
		RequestSpecification request=RestAssured.given();
		request.header("Content-Type","application/json");
		request.body(contactJson);
		return request.post("/contacts");
	}

	private Contact[] getContactDetails() {
		Response response=RestAssured.get("/contacts");
		logger.info("Contacts in json server: "+response.asString());
		Contact[] arrayOfContacts=new Gson().fromJson(response.asString(), Contact[].class);
		return arrayOfContacts;
	}
}
