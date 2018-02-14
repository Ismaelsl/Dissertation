package org.dissertationWeb.classes;

/**
 * Class used to control all the user of the system (dissertation coordinator(DC), lecturer or student)
 * @author ismael
 *
 */
public class User {
	private int userID;
	private String username;
	private String password;
	private String email;
	private int userType;
	private int year;
	
	public User() {
		
	}
	//Constructor used only when edit student
	public User (int userID, String username, String password, String email, int userType, int year) {
		this.userID = userID;
		this.username = username;
		this.password = password;
		this.email = email;
		this.userType = userType;
		this.year = year;
	}
	public void setUserType(int type){
		this.userType = type;
	}
	public int getUserType() {
		return this.userType;
	}
	
	public void setUserID(int id) {
		this.userID = id;
	}
	public int getUserID() {
		return this.userID;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String userName) {
		this.username = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}

}
