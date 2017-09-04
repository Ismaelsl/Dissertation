package org.dissertationWeb.classes;

import java.util.List;

public class Project {
	private int projectID;
	private int year;
	private String title;
	private List<String> topics;
	private List<String> compulsoryReading;
	private String description;
	private Lecturer lecturer;
	private boolean visible;
	private Document document;
	private boolean waitingToBeApproved;
	private CheckList checkList;

	public Project() {

	}
	public Project(int projectID, int year, String title, List<String> topics, 
			List<String> compulsoryReading,String description, Lecturer lecturer, 
			boolean visible, Document document, boolean waitingToBeApproved,
			CheckList checkList) {
		super();
		this.projectID = projectID;
		this.year = year;
		this.title = title;
		this.topics = topics;
		this.compulsoryReading = compulsoryReading;
		this.description = description;
		this.lecturer = lecturer;
		this.visible = visible;
		this.document = document;
		this.waitingToBeApproved = waitingToBeApproved;
		this.checkList = checkList;
	}
	public Project getProject(int id) {
		//here should be a sql query based on the ID, if project exist, will return the object
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
	public List<String> getTopics() {
		return topics;
	}
	public void setTopics(List<String> topics) {
		this.topics = topics;
	}
	public List<String> getCompulsoryReading() {
		return compulsoryReading;
	}
	public void setCompulsoryReading(List<String> compulsoryReading) {
		this.compulsoryReading = compulsoryReading;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Lecturer getLecturer() {
		return lecturer;
	}
	public void setLecturer(Lecturer lecturer) {
		this.lecturer = lecturer;
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
