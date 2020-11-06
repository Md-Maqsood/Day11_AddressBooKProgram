package com.bridgeLabz.addressBookProgram;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AddressBookDBIoservice {
	private Connection getConnection() throws AddressBookDBIoException {
			String JDBC_URL = "jdbc:mysql://localhost:3306/addressbookservicedb?useSSL=false";
			String USERNAME = "root";
			String PASSWORD = "abcd1234";
			Connection connection= null;
			try {
				connection=DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
			return connection;
		} catch (SQLException e) {
			throw new AddressBookDBIoException("Unable to establish connection to database");
		}
	}
	public List<Contact> readContactDetails() throws AddressBookDBIoException {
		String sql="select * from person join address_details on person.contact_id=address_details.contact_id join contact_details on person.contact_id=contact_details.contact_id;";
		try(Connection connection=this.getConnection()){
			Statement statement=connection.createStatement();
			ResultSet resultSet=statement.executeQuery(sql);
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
				String city=resultSet.getString("city");
				String state=resultSet.getString("state");
				int zip=resultSet.getInt("zip");
				long phoneNumber = resultSet.getLong("phonenumber");
				String email=resultSet.getString("email");
				contactList.add(new Contact(firstName, lastName, address, city, state, zip, phoneNumber, email));
			}
			return contactList;
		} catch (SQLException e) {
			throw new AddressBookDBIoException("Unable to use the result set");
		}
	}
	
}
