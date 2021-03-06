package org.dissertationWeb.classes;

import java.io.BufferedReader;
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
	private static final String MAX_POOL = "2050";
	private static final String AUTHFILE = "src/main/webapp/auth/auth.txt";

	// init connection object
	private Connection connection;
	// init properties object
	private Properties properties;

	// create properties
	private Properties getProperties() {
		if (properties == null) {
			try {
				//this piece of code it is use to get userName and password from an external file, so in this way
				//I am adding a new layer of security to the application and not hardcoding the username and password into the code
				BufferedReader userPassIn = new BufferedReader(new FileReader(AUTHFILE));
				String userName = userPassIn.readLine();
				String pass = userPassIn.readLine();
				userPassIn.close();
				properties = new Properties();
				properties.setProperty("user", userName);
				properties.setProperty("password", pass);
				properties.setProperty("MaxPooledStatements", MAX_POOL);
			} catch (IOException e) {
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