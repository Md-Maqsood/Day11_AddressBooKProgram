package com.bridgeLabz.addressBookProgram;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

public class AddressBookCsvIOService {
	private static Logger logger = LogManager.getLogger(AddressBookCsvIOService.class);
	public String addressBookCSVFileName;

	public AddressBookCsvIOService(String addressBookCSVFileName) {
		super();
		this.addressBookCSVFileName = addressBookCSVFileName;
	}

	public void writeContactDetails(List<Contact> contacts) {
		try (Writer writer = Files.newBufferedWriter(Paths.get(this.addressBookCSVFileName));) {
			StatefulBeanToCsv<Contact> beanToCSV = new StatefulBeanToCsvBuilder<Contact>(writer)
					.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).build();
			beanToCSV.write(contacts);
		} catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException | IOException e) {
			logger.info(e.getMessage());
		}
	}

	public List<Contact> readContactDetails() {
		List<Contact> contacts = null;
		try (Reader reader = Files.newBufferedReader(Paths.get(this.addressBookCSVFileName));) {
			CSVReader csvReader = new CSVReader(reader);
			List<String[]> contactStrings = csvReader.readAll();
			contactStrings.remove(0);
			contacts = contactStrings.stream().map(string -> {
				String firstName = string[3], lastName = string[5], address = string[0];
				String city = string[1], state = string[7];
				int zip = Integer.parseInt(string[8]);
				long phoneNumber = Long.parseLong(string[6]);
				String email = string[2];
				return new Contact(firstName, lastName, address, city, state, zip, phoneNumber, email);
			}).collect(Collectors.toList());
			csvReader.close();
		} catch (IOException e) {
			logger.info(e.getMessage());
		}
		return contacts;
	}
}
