package com.bridgeLabz.addressBookProgram;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AddressBookFileIOService {
	private static final Logger logger=LogManager.getLogger(AddressBookFileIOService.class);
	public String addressBookFileName; 
	
	
	
	public AddressBookFileIOService(String addressBookFileName) {
		super();
		this.addressBookFileName = addressBookFileName;
	}

	public void writeContactDetails(List<Contact> contacts) {
		StringBuffer contactsBuffer=new StringBuffer();
		contacts.forEach(contact-> contactsBuffer.append(contact.toString().concat("\n")));
		try {
			Files.write(Paths.get(addressBookFileName),contactsBuffer.toString().getBytes());
		}catch(IOException e) {
			logger.info(e.getMessage());
		}
	}
	
	public List<Contact> readContactDetails(){
		List<Contact> contacts=null;
		try {
			contacts=Files.lines(Paths.get(addressBookFileName))
						.map(contactDetailsString->{
							String[] contactDetailsArray=contactDetailsString.split(", ");
							return new Contact(contactDetailsArray[0].split(" ")[0],
									contactDetailsArray[0].split(" ")[1],
									contactDetailsArray[1],
									contactDetailsArray[2],
									contactDetailsArray[3],
									Integer.parseInt(contactDetailsArray[4]),
									Long.parseLong(contactDetailsArray[5]),
									contactDetailsArray[6]
									);
						}).collect(Collectors.toList());
		}catch(IOException e) {
			logger.info(e.getMessage());
		}
		return contacts;
	}
	
}
