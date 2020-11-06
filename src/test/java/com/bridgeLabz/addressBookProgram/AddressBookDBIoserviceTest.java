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
		Contact mark = new Contact("Mark", "Zukerberg", "abc", "New York City", "New York", 123456, 9874563210l,
				"mark@gmail.com");
		List<Contact> contactList;
		try {
			contactList = this.addressBook.readContactListFromIO(IOServiceType.DB_IO);
			Assert.assertEquals(3, contactList.size());
			Assert.assertEquals(mark, contactList.get(1));
		} catch (AddressBookDBIoException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void givenContactDetailsWhenUpdateSHouldSyncWithDataBase() {
		String firstName="Jeff";
		String lastName="Bezoss";
		String address="pqr";
		String city="Dallas";
		String state="Texas";
		int zip=123456;
		long phoneNumber=9876543212l;
		String email="jeff@gmail.com";
		try {
			this.addressBook.getContactsIntoListFromDataBase();
			this.addressBook.updateContactDetailsInDataBase(firstName, lastName, address, city, state, zip, phoneNumber, email);
			this.addressBook.checkIfAddressBookInSyncWithDataBase(firstName, lastName);
		}catch(AddressBookDBIoException e) {
			e.printStackTrace();
		}
	}
}
