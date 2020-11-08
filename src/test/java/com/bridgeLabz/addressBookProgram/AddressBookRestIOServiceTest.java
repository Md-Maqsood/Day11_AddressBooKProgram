package com.bridgeLabz.addressBookProgram;

import java.util.Arrays;

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
	
	@Test
	public void givenContactDetailsOnJsonServer_WhenReadShouldMatchCount() {
		Contact[] arrayOfContacts=this.getContactDetails();
		AddressBook addressBook=new AddressBook("Book1", Arrays.asList(arrayOfContacts));
		int numOfContacts=addressBook.countEntries();
		Assert.assertEquals(3, numOfContacts);
	}

	@Test
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
