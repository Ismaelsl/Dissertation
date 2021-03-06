package org.dissertationWeb.classes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * Class use to create events for the schedule
 * @author ismael
 *
 */
public class CheckList {
	private int checkListID;
	//special spring notation that is telling to the view (JSP) how to format the date from the DB on the view
	//If you need to change the format, just change the pattern
	@DateTimeFormat(pattern="dd.MM.yyyy")
	private String date;
	private String hour;
	private String endHour;
	private String eventName;
	private String place;
	private String description;
	private boolean visible;

	public CheckList() {

	}

	public CheckList(String date, String eventName, String place) {
		this.date = date;
		this.eventName = eventName;
		this.place = place;
		this.visible = true;
	}

	public CheckList(int id, String date, String eventName, String place, String description, String hour, String endHour) {
		this.checkListID = id;
		this.date = date;
		this.eventName = eventName;
		this.place = place;
		this.description = description;
		this.visible = true;
		this.hour = hour;
		this.endHour = endHour;
	}

	//Getter and setter area
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
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
	public String getHour() {
		return this.hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}
	public String getEndHour() {
		return this.endHour;
	}
	public void setEndHour(String hour) {
		this.endHour = hour;
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
	//End of getter and setter area
}
