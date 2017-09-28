package org.dissertationWeb.classes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class CheckList {
	private int checkListID;
	@DateTimeFormat(pattern="dd.MM.yyyy")
	private String date;
	private String eventName;
	private String place;

	public CheckList() {

	}
	public CheckList(String date, String eventName, String place) {
		this.date = date;
		this.eventName = eventName;
		this.place = place;
	}
	public CheckList(int id, String date, String eventName, String place) {
		this.checkListID = id;
		this.date = date;
		this.eventName = eventName;
		this.place = place;
	}
	public int getCheckListID() {
		return checkListID;
	}
	public void setCheckListID(int checkListID) {
		this.checkListID = checkListID;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}

	public CheckList getchecklist(int checklistID) {
		DBConnection connect = new DBConnection();
		Connection newConnection = connect.connect();
		String query = "SELECT * FROM checklist WHERE checklistID = " + "'" + checklistID + "';";
		Statement st;
		CheckList checklist = new CheckList();
		try {
			st = newConnection.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next())
			{
				checklist.setCheckListID(rs.getInt("checklistID"));
				checklist.setDate(rs.getString("date"));
				checklist.setEventName(rs.getString("eventname"));
				checklist.setPlace(rs.getString("place"));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return checklist;
	}

}
