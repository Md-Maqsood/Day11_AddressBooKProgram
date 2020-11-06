package com.bridgeLabz.addressBookProgram;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddressBookDBIoservice {
	private Connection getConnection() throws AddressBookDBIoException {
		String JDBC_URL = "jdbc:mysql://localhost:3306/addressbookservicedb?useSSL=false";
		String USERNAME = "root";
		String PASSWORD = "abcd1234";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
			return connection;
		} catch (SQLException e) {
			throw new AddressBookDBIoException("Unable to establish connection to database");
		}
	}

	public List<Contact> readContactDetails() throws AddressBookDBIoException {
		String sql = "select * from person join address_details on person.contact_id=address_details.contact_id join contact_details on person.contact_id=contact_details.contact_id;";
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			return this.getContactListFromResultSet(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<Contact> getContactListFromResultSet(ResultSet resultSet) throws AddressBookDBIoException {
		List<Contact> contactList = new ArrayList<Contact>();
		try {
			while (resultSet.next()) {
				String firstName = resultSet.getString("firstname");
				String lastName = resultSet.getString("lastname");
				String address = resultSet.getString("address");
				String city = resultSet.getString("city");
				String state = resultSet.getString("state");
				int zip = resultSet.getInt("zip");
				long phoneNumber = resultSet.getLong("phonenumber");
				String email = resultSet.getString("email");
				contactList.add(new Contact(firstName, lastName, address, city, state, zip, phoneNumber, email));
			}
			return contactList;
		} catch (SQLException e) {
			throw new AddressBookDBIoException("Unable to use the result set");
		}
	}

	public void updateAddressDetails(String firstName, String lastName, String address, String city, String state,
			int zip, long phoneNumber, String email) throws AddressBookDBIoException {
		String sql = String.format("select contact_id from person where firstname='%s' and lastname='%s'", firstName,
				lastName);
		Connection connection = null;
		try {
			connection = this.getConnection();
			connection.setAutoCommit(false);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			int contactId = -1;
			if (resultSet.next()) {
				contactId = resultSet.getInt("contact_id");
			} else {
				throw new AddressBookDBIoException("Person not found");
			}
			sql = String.format(
					"update address_details set address='%s', city='%s', state='%s', zip=%s where contact_id=%s;",
					address, city, state, zip, contactId);
			int rowsAffected = statement.executeUpdate(sql);
			if (rowsAffected != 1) {
				throw new AddressBookDBIoException("Unable to update address_details in database");
			}
			sql = String.format("update contact_details set phonenumber=%s, email='%s' where contact_id=%s;",
					phoneNumber, email, contactId);
			rowsAffected = statement.executeUpdate(sql);
			if (rowsAffected != 1) {
				throw new AddressBookDBIoException("Unable to update contact_details in database");
			}
			connection.commit();
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new AddressBookDBIoException("Unable to update entry in data base");
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public List<Contact> getContactFromDataBase(String firstName, String lastName) throws AddressBookDBIoException {
		String sql = String.format(
				"select * from person join address_details on person.contact_id=address_details.contact_id join contact_details on person.contact_id=contact_details.contact_id where firstname='%s' and lastname='%s';",
				firstName, lastName);
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			return this.getContactListFromResultSet(resultSet);
		} catch (SQLException e) {
			throw new AddressBookDBIoException("Unable to retrieve contact from database");
		}
	}

	public List<Contact> getContactsAddedInDateRange(LocalDate startDate, LocalDate endDate) throws AddressBookDBIoException {
		String sql=String.format("select * from person join address_details on person.contact_id=address_details.contact_id join contact_details on person.contact_id=contact_details.contact_id where date_added between cast('%s' as date) and cast('%s' as date);",startDate, endDate);
		try(Connection connection=this.getConnection()){
			Statement statement=connection.createStatement();
			ResultSet resultSet=statement.executeQuery(sql);
			return this.getContactListFromResultSet(resultSet);
		}catch(SQLException e) {
			throw new AddressBookDBIoException("Unable to retrieve contacts added in the given date range");
		}
	}
}
