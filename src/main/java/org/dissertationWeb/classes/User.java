package org.dissertationWeb.classes;

public class User {
	private int userID;
	private String username;
	private String password;
	private String email;
	private int userType;
	
	public User() {
		
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

}
