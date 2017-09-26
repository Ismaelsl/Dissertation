package org.dissertationWeb.classes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Project {
	private int projectID;
	private int year;
	private String title;
	private String topics;
	private String compulsoryReading;
	private String description;
	private User user;
	private boolean visible;
	private Document document;
	private boolean waitingToBeApproved;
	private CheckList checkList;

	public Project() {

	}
	public Project(int projectID, int year, String title, String topics, 
			String compulsoryReading,String description, User user, 
			boolean visible, Document document, boolean waitingToBeApproved,
			CheckList checkList) {
		super();
		this.projectID = projectID;
		this.year = year;
		this.title = title;
		this.topics = topics;
		this.compulsoryReading = compulsoryReading;
		this.description = description;
		this.user = user;
		this.visible = visible;
		this.document = document;
		this.waitingToBeApproved = waitingToBeApproved;
		this.checkList = checkList;
	}
	public Project(int projectID,int year, String title, String topics,
			String compulsoryReading,String description){
		this.projectID = projectID;
		this.year = year;
		this.title = title;
		this.topics = topics;
		this.compulsoryReading = compulsoryReading;
		this.description = description;
	}
	public Project getProject(int id) {
		DBConnection connect = new DBConnection();
		Connection newConnection = connect.connect();
		String query = "SELECT * FROM project WHERE projectID = " + id + ";";
		Statement st;
		try {
			st = newConnection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next())
			{
				/*
				 * instead of creating new Lecturer, Document and CheckList I should make a call based on the project ID
				 * and get the reference to the DB to those items and create new objects populated with the data from the DB in this way
				 * should work better
				 */
				String queryUser = "SELECT * FROM user WHERE userID = " + rs.getInt("lecturerID") + ";";
				Statement stUser;
				User user = new User();
				try {
					stUser = newConnection.createStatement();
					ResultSet rsUser = stUser.executeQuery(queryUser);
					
					while (rsUser.next())
					{
						user.setUserID(rsUser.getInt("userID"));
						user.setEmail(rsUser.getString("email"));
						user.setUsername(rsUser.getString("username"));
						user.setPassword(rsUser.getString("password"));
						user.setUserType(rsUser.getInt("userType"));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				System.out.println("Get project user test " + user.getEmail());
				
				Project project = new Project(rs.getInt("projectID"), rs.getInt("year"), rs.getString("title"), rs.getString("topic"),
						rs.getString("compulsoryReading"), rs.getString("description"), user, 
						rs.getBoolean("visible"),new Document(rs.getInt("documentID")), rs.getBoolean("waitingtobeapproved"), 
						new CheckList(rs.getInt("checklistID")));
				//System.out.println("test " + rs.getString("title"));
				return project;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public int getProjectID() {
		return projectID;
	}
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTopics() {
		return topics;
	}
	public void setTopics(String topics) {
		this.topics = topics;
	}
	public String getCompulsoryReading() {
		return compulsoryReading;
	}
	public void setCompulsoryReading(String compulsoryReading) {
		this.compulsoryReading = compulsoryReading;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
	public boolean isWaitingToBeApproved() {
		return waitingToBeApproved;
	}
	public void setWaitingToBeApproved(boolean waitingToBeApproved) {
		this.waitingToBeApproved = waitingToBeApproved;
	}
	public CheckList getCheckList() {
		return checkList;
	}
	public void setCheckList(CheckList checkList) {
		this.checkList = checkList;
	}

}
