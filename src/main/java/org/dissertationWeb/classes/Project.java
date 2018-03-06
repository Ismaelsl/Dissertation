package org.dissertationWeb.classes;

/**
 * Main class for project, it contains all the attribute and method need it to manage projects
 * @author ismael
 *
 */
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
	private User student;
	private int lecturerID;

	public Project() {

	}
	public Project(int projectID, int year, String title, String topics, 
			String compulsoryReading,String description, User user, 
			boolean visible, Document document, boolean waitingToBeApproved,
			CheckList checkList, int lecturerID) {
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
		this.lecturerID = lecturerID;
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

	//Getter and setter area
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
	public User getStudent() {
		return student;
	}
	public void setStudent(User student) {
		this.student = student;
	}
	public int getlecturerID() {
		return lecturerID;
	}
	//End getter and setter area
}
