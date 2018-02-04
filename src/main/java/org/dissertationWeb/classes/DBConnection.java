package org.dissertationWeb.classes;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Class used to create and manage the connection with the DB
 * @author ismael
 *
 */
public class DBConnection {
	private static final String DATABASE_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DATABASE_URL = "jdbc:mysql://mysql.cs.stir.ac.uk/isa";
	private static final String USERNAME = "";
	private static final String PASSWORD = "";
	private static final String MAX_POOL = "2050"; // set your own limit

	// init connection object
	private Connection connection;
	// init properties object
	private Properties properties;

	// create properties
	private Properties getProperties() {
		if (properties == null) {
			try {
				//this piece of code it is use to get userName and password from an external file, so in this way
				//I am adding a new layer of security to the application
				BufferedReader userPassIn = new BufferedReader(new FileReader("src/main/webapp/auth/auth.txt"));
				String userName = userPassIn.readLine();
				String pass = userPassIn.readLine();
				userPassIn.close();
				properties = new Properties();
				properties.setProperty("user", userName);
				properties.setProperty("password", pass);
				properties.setProperty("MaxPooledStatements", MAX_POOL);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		return properties;
	}

	// connect database
	public Connection connect() {
		
		if (connection == null) {
			try {
				Class.forName(DATABASE_DRIVER);
				connection = DriverManager.getConnection(DATABASE_URL, getProperties());
			} catch (ClassNotFoundException | SQLException e) {
				// Java 7+
				e.printStackTrace();
			}
		}
		try {
			//Autocommit false, force to do a commit after each insert/delete/update
			//The idea is that if the action false, I can do a rollback and not commit nothing
			//connection.setAutoCommit(false);
			//Complete transaction isolation within my application
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	// disconnect database
	public void disconnect() {
		if (connection != null) {
			try {
				connection.close();
				connection = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
