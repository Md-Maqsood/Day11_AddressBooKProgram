package com.bridgeLabz.addressBookProgram;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.bridgeLabz.addressBookProgram.AddressBook.IOServiceType;

public class AddressBookDBIoserviceTest {
	private AddressBook addressBook;

	@Before
	public void setUp() {
		this.addressBook = new AddressBook("Book1");
	}

	@Test
	public void givenAddressBookWhenContactsREadShouldMatchCount() {
		Contact jeff = new Contact("Jeff", "Bezoss", "xyz", "San Francisco", "California", 120012, 1234567890l,
				"jeff@gmail.com");
		List<Contact> contactList;
		try {
			contactList = this.addressBook.readContactListFromIO(IOServiceType.DB_IO);
			Assert.assertEquals(3, contactList.size());
			Assert.assertEquals(jeff.toString(), contactList.get(0).toString());
		} catch (AddressBookDBIoException e) {
			e.printStackTrace();
		}
	}
}
