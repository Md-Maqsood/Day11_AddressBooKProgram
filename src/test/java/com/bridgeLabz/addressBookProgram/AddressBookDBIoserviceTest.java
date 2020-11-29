package com.bridgeLabz.addressBookProgram;

import java.time.LocalDate;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.bridgeLabz.addressBookProgram.AddressBook.IOServiceType;
import com.bridgeLabz.addressBookProgram.AddressBook.SearchBy;

public class AddressBookDBIoserviceTest {
	private AddressBook addressBook;

	@Before
	public void setUp() {
		this.addressBook = new AddressBook("Book1");
	}

	@Ignore
	public void givenAddressBookWhenContactsReadShouldMatchCount() {
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
			Assert.assertTrue(this.addressBook.checkIfAddressBookInSyncWithDataBase(firstName, lastName));
		}catch(AddressBookDBIoException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void givenContactDetailsWhenSearchedOnDateAddedShouldMatchCount() {
		try {
			this.addressBook.getContactsIntoListFromDataBase();
			LocalDate startDate=LocalDate.parse("2019-01-01");
			LocalDate endDate=LocalDate.parse("2020-01-01");
			int numContacts=this.addressBook.getContactsAddedInDateRange(startDate, endDate).size();
			Assert.assertEquals(2, numContacts);
		}catch(AddressBookDBIoException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void givenCityOrStateNameWhenSerchedContactsShouldReturnProperCount(){
		try {
			this.addressBook.getContactsIntoListFromDataBase();
			String city="Los Angeles";
			String state="New York";
			int countOnCity=this.addressBook.getCountOnCityOrState(city, SearchBy.CITY);
			int countOnState=this.addressBook.getCountOnCityOrState(state, SearchBy.STATE);
			Assert.assertEquals(1, countOnCity);
			Assert.assertEquals(1, countOnState);
		}catch(AddressBookDBIoException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void givenContactWhenAddedToDataBaseShouldIncrementCountAndSyncWithDataBase() {
		try {
			this.addressBook.getContactsIntoListFromDataBase();
			int numContactsBeforeAdding=this.addressBook.readContactListFromIO(IOServiceType.DB_IO).size();
			String firstName="John",lastName="Yahya",address="lmn",city="Dallas",state="Texas";
			int zip=123453;
			long phoneNumber=6543217896l;
			String email="john@gmail.com";
			this.addressBook.addContactToDataBase(firstName, lastName, address, city, state, zip, phoneNumber, email);
			int numContactsAfterAdding=this.addressBook.readContactListFromIO(IOServiceType.DB_IO).size();
			Assert.assertTrue(this.addressBook.checkIfAddressBookInSyncWithDataBase(firstName, lastName));
			Assert.assertEquals(numContactsBeforeAdding+1,numContactsAfterAdding);
		}catch(AddressBookDBIoException e) {
			e.printStackTrace();
		}
	}
}
