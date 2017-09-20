package org.dissertationWeb.classes;

public class Lecturer {
	private String name;
	private String department;
	private String email;
	private int lecturerID;
	
	public Lecturer(String n, String d, String e, int lecturerID){
		this.name = n;
		this.department = d;
		this.email = e;
		this.lecturerID = lecturerID;
	}
	public Lecturer(int lecturerID){
		this.lecturerID = lecturerID;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
	
	public int getID() {
		return lecturerID;
	}
	
}
