package com.bridgeLabz.addressBookProgram;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.bridgeLabz.addressBookProgram.AddressBook.IOServiceType;

public class AddressBookMultiThreadingTest {

	@Test
	public void given3Contacts_WhenAddedToDB_ShouldMatchContactEntries() {
		List<Contact> contactListToBeAdded= Arrays.asList(new Contact[] {
				new Contact("Jaff", "Bazoss", "xyz", "San Francisco", "California", 120012, 1234567890l,
						"jeff@gmail.com"),			
				new Contact("Merk", "Zukarberg", "abc", "New York City", "New York", 123456, 9874563210l,
								"mark@gmail.com"),
				new Contact("Setya", "Nodela", "pqr", "Los Angeles", "California", 120546, 5463217890l,"satya@gmail.com")		
		});
		AddressBook addressBook=new AddressBook("Book1");
		try {
			addressBook.getContactsIntoListFromDataBase();
			int countBefore=addressBook.readContactListFromIO(IOServiceType.DB_IO).size();
			addressBook.addMultipleContactsWithThreads(contactListToBeAdded);
			int countAfter=addressBook.readContactListFromIO(IOServiceType.DB_IO).size();
			Assert.assertEquals(countBefore+3,countAfter);
		} catch (AddressBookDBIoException e) {
			e.printStackTrace();
		}
	}
}
