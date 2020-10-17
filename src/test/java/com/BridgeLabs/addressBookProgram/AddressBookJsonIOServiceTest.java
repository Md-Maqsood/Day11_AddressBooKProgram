package com.BridgeLabs.addressBookProgram;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class AddressBookJsonIOServiceTest {
	@Test
	public void given3ContactsWhenWrittenToJsonFileShouldMatchContactEntries() {
		Contact jeff = new Contact("Jeff", "Bezoss", "xyz", "San Francisco", "California", 120012, 1234567890l,
				"jeff@gmail.com");
		Contact mark = new Contact("Mark", "Zukerberg", "abc", "New York City", "New York", 123456, 9874563210l,
				"mark@gmail.com");
		Contact satya = new Contact("Satya", "Nadela", "pqr", "Los Angeles", "California", 120546, 5463217890l,
				"satya@gmail.com");
		List<Contact> contacts = Arrays.asList(new Contact[] { jeff, mark, satya });
		AddressBookJsonIOService addressBookJsonIOService = new AddressBookJsonIOService("addressBook-test-Json.json");
		addressBookJsonIOService.writeContactDetails(contacts);
		List<Contact> readContacts = addressBookJsonIOService.readContactDetails();
		Assert.assertEquals(jeff.toString(), readContacts.get(0).toString());
		Assert.assertEquals(mark.toString(), readContacts.get(1).toString());
		Assert.assertEquals(satya.toString(), readContacts.get(2).toString());
	}
}
