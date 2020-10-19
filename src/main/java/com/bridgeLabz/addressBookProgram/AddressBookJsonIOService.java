package com.bridgeLabz.addressBookProgram;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

public class AddressBookJsonIOService {
	private static Logger logger = LogManager.getLogger(AddressBookJsonIOService.class);
	public String addressBookJsonFileName;

	public AddressBookJsonIOService(String addressBookJsonFileName) {
		super();
		this.addressBookJsonFileName = addressBookJsonFileName;
	}

	public void writeContactDetails(List<Contact> contacts) {
		try {
			Gson gson = new Gson();
			String json = gson.toJson(contacts);
			FileWriter writer = new FileWriter(this.addressBookJsonFileName);
			writer.write(json);
			writer.close();
		} catch (IOException e) {
			logger.info(e.getMessage());
		}
	}

	public List<Contact> readContactDetails() {
		List<Contact> contacts = null;
		try {
			Gson gson = new Gson();
			BufferedReader bufferedReader = new BufferedReader(new FileReader(this.addressBookJsonFileName));
			Contact[] contactsArray = gson.fromJson(bufferedReader, Contact[].class);
			contacts = new LinkedList<Contact>(Arrays.asList(contactsArray));
			bufferedReader.close();
		} catch (IOException e) {
			logger.info(e.getMessage());
		}
		return contacts;
	}
}
