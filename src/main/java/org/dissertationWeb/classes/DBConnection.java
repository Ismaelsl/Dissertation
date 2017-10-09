package org.dissertationWeb.classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
	private static final String DATABASE_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DATABASE_URL = "jdbc:mysql://mysql0.cs.stir.ac.uk/isa";
	private static final String USERNAME = "isa";
	private static final String PASSWORD = "isa";
	private static final String MAX_POOL = "2050"; // set your own limit

	// init connection object
	private Connection connection;
	// init properties object
	private Properties properties;

	// create properties
	private Properties getProperties() {
		if (properties == null) {
			properties = new Properties();
			properties.setProperty("user", USERNAME);
			properties.setProperty("password", PASSWORD);
			properties.setProperty("MaxPooledStatements", MAX_POOL);
		}
		return properties;
	}

	// connect database
	public Connection connect() {
		
		if (connection == null) {
			try {
				Class.forName(DATABASE_DRIVER);
				System.out.println("breakpoint line");
				connection = DriverManager.getConnection(DATABASE_URL, getProperties());
			} catch (ClassNotFoundException | SQLException e) {
				// Java 7+
				e.printStackTrace();
			}
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
