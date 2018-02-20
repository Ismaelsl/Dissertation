package org.dissertationWeb.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.dissertationWeb.classes.CheckList;
import org.dissertationWeb.classes.DBConnection;
import org.dissertationWeb.classes.Document;
import org.dissertationWeb.classes.Project;
import org.dissertationWeb.classes.User;

/**
 * Class that I create to keep together all the SQL, this class it is call by MyController when any interaction with the DB 
 * @author Ismael
 *
 */
public class SQLController {
	private Connection newConnection;
	private int actualYear = 2017;

	public int getActualYear() {
		return actualYear;
	}

	public SQLController() {
		DBConnection connect = new DBConnection();
		newConnection = connect.connect();
		updateYear();
	}
	
	/**
	 * SQL Method that is counting how many projects in the DB I have, the idea is count when enter and check with the previous count in the DB
	 * if I have more then a green tick will be show on the project list in the menu.
	 * @return
	 */
	public int numberOfProjectsInDB() {
		try {
			//I am using distinct since does not make sense to have the same project twice
			PreparedStatement ps = newConnection.prepareStatement(
					"SELECT COUNT( DISTINCT project.projectID) AS projectCount FROM project WHERE NOT EXISTS "
							+ "(SELECT * FROM interestproject WHERE project.projectID = interestproject.projectID AND visible = ?)"
							+ "AND NOT EXISTS (SELECT * FROM approvedproject WHERE project.projectID = approvedproject.projectID AND visible = ?)"
							+ "AND project.year = ? AND project.visible = ? AND project.waitingtobeapproved = ?");
			ps.setBoolean(1,true);
			ps.setBoolean(2,true);
			ps.setInt(3,actualYear);
			ps.setBoolean(4,true);
			ps.setBoolean(5,true);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				return rs.getInt("projectCount");
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			return 0;
		}
		return 0;
	}
	
	public int numberOfEventsInDB() {
		String query = " SELECT COUNT(*) AS eventCount FROM checklist WHERE visible = ?";
		try {
			PreparedStatement ps = newConnection.prepareStatement(query);
			ps.setBoolean(1, true);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				return rs.getInt("eventCount"); 
			}else {
				return 0;
			}
		} catch (SQLException e) {
		}
		return 0;
	}
	
	public int updateDBCount(int userID, int projectCount, int eventCount) {
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT * FROM dbcount WHERE userID = ? FOR UPDATE");
			ps.setInt(1,userID);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				System.out.println("Inside of update");
				PreparedStatement ps2 = newConnection.prepareStatement(
						"UPDATE dbcount SET projectcount = ?, eventcount = ? WHERE userID = ?");
				ps2.setInt(1,projectCount);
				ps2.setInt(2,eventCount);
				ps2.setInt(3,rs.getInt("userID"));
				ps2.executeUpdate();
				ps2.close();
				return 1;
			}
			rs.close();
			ps.close();
			//If the row does not exist in the DB then I will insert it, but if exist it will be update and then leave the method
			System.out.println("Inside of insert");
			String queryInsert = " INSERT INTO dbcount (userID, projectcount, eventcount, waitapprovecount) VALUES (?, ?, ?, ?)";
			PreparedStatement preparedStmt;
			try {
				preparedStmt = newConnection.prepareStatement(queryInsert);
				preparedStmt.setInt (1, userID);
				preparedStmt.setInt (2, projectCount);
				preparedStmt.setInt(3, eventCount);
				preparedStmt.setInt(4, 0);//for now I am not using this value so I am always putting it as 0
				preparedStmt.execute();
				preparedStmt.close();
				return 1;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			return 0;
		}
		return 0;
	}
	
	public int getProjectCountDB(int userID) {
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
						"SELECT projectcount FROM dbcount WHERE userID = ?");
			ps.setInt(1,userID);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			if(rs.next()) return rs.getInt("projectcount");
		} catch (SQLException e) {
			return 0;
		}		
		return 0;
	}
	
	public int getEventCountDB(int userID) {
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
						"SELECT eventcount FROM dbcount WHERE userID = ?");
			ps.setInt(1,userID);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			if(rs.next()) return rs.getInt("eventcount");
		} catch (SQLException e) {
			return 0;
		}		
		return 0;
	}
	
	/**
	 * This SQL method is only doing a really basic query to keep the connection alive
	 * is not returning the data or doing anything with the data.
	 * Is only using the DB so the DB does not close because of inactivity
	 */
	public void keepConnectionAlive() {
		String query = "SELECT date FROM checklist WHERE true";
		Statement st;
		try {
			st = newConnection.createStatement();
			@SuppressWarnings("unused")
			ResultSet rs = st.executeQuery(query);
		} catch (SQLException e) {
		}			
	}

	/**
	 * Method that change automatically the year every first of June at midnight
	 */
	public void updateYear() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date changeDate = sdf.parse(Calendar.getInstance().get(Calendar.YEAR) + "-03-01 00:00:00");
			Date today = new Date();
			//I am checking both just in case some delay happens and I compare dates a little bit late in this case if today is after changeDate will
			//trigger the change as well
			if(changeDate.compareTo(today) == 0 || changeDate.compareTo(today) < 0) {
				//I update the actualYear to this year
				actualYear = Calendar.getInstance().get(Calendar.YEAR);
			}
		} catch (ParseException e) {
		}
	}

	/**
	 * SQL Method that get all the checklist and check if them are one week away from the actual date
	 * If they are I am returning a list with the checklists
	 * @return
	 */
	public List<CheckList> checkScheduleTime() {
		List<CheckList> checklistList = new ArrayList<CheckList>();
		Date today = new Date();
		String query = "SELECT * FROM checklist";
		Statement st;
		try {
			st = newConnection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next())
			{
				String DATE_FORMAT_NOW = "yyyy-MM-dd";
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
				String stringDate = sdf.format(today );
				String stringToDate = sdf.format(rs.getDate("date") );

				LocalDate from = LocalDate.parse(stringDate);
				LocalDate to = LocalDate.parse(stringToDate);

				long days = ChronoUnit.DAYS.between(from, to);// method to check the distance in long between the dates
				if(days <= 7 && days > 2){//If is between 7 to 2 that means that is from next Monday to Friday
					CheckList checklist = new CheckList();
					if(rs.getBoolean("visible") == true) {//only if the checklist is visible will be show
						checklist.setCheckListID(rs.getInt("checklistID"));
						checklist.setDate(rs.getString("date"));
						checklist.setEventName(rs.getString("eventname"));
						checklist.setPlace(rs.getString("place"));
						checklist.setDescription(rs.getString("description"));
						checklistList.add(checklist);
					}
				}
			}
		} catch (SQLException e) {
			return checklistList;
		}

		return checklistList;
	}

	/**
	 * SQL method to check within the DB the login information
	 * @param user
	 * @return the userID to probe that the login was successful, if userID is 0 means that login fails
	 */
	public int loginCheck(User user) {

		String query = "SELECT * FROM user";
		Statement st;
		int userID = 0;
		try {
			st = newConnection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next())
			{
				if(rs.getString("username").equals(user.getUsername()) 
						&& rs.getString("password").equals(user.getPassword())) {
					return userID = rs.getInt("userID");
				}
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userID;
	}

	/**
	 * SQL Method that it is showing contact information of the dissertation coordinator
	 * @return
	 */
	public int contacPage() {
		try {
			PreparedStatement ps = newConnection.prepareStatement(
					"SELECT * FROM user WHERE userType = ?");
			ps.setInt(1,3);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				return rs.getInt("userID");
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			return 0;
		}
		return 0;
	}

	/**
	 * SQL method to control when the DC approve a project that a lecturer send before
	 * @param projectID
	 * @return
	 */
	public int approveProject(int projectID) {
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT * FROM project WHERE projectID = ? AND year = ? FOR UPDATE");
			ps.setInt(1,projectID);
			ps.setInt(2, actualYear);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				PreparedStatement ps2 = newConnection.prepareStatement(
						"UPDATE project SET waitingtobeapproved = ? WHERE projectID = ?");
				ps2.setBoolean(1,true);
				ps2.setInt(2,rs.getInt("projectID"));
				ps2.executeUpdate();
				int projectIDToReturn = rs.getInt("projectID");//I am keeping the value in a variable since I want to close ps before the return
				ps.close();
				ps2.close();
				return projectIDToReturn;
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			return 0;
		}
		return 0;
	}

	/**
	 * SQL method used to save any modification to a project in the DB
	 * @param project
	 * @return
	 */
	public int saveEdit(Project project) {
		if(checkIfProjectExistInDBSaveEdit(project)) return 1;
		//Using the "For update" method I am locking the project till I close the statement
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT * FROM project WHERE projectID = ? FOR UPDATE");
			ps.setInt(1,project.getProjectID());
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				PreparedStatement ps2 = newConnection.prepareStatement(
						"UPDATE project SET year = ?, title = ?, topic = ?, description = ?, compulsoryreading = ? WHERE projectID = ?");
				ps2.setInt(1,project.getYear());
				ps2.setString(2,project.getTitle());
				ps2.setString(3,project.getTopics().toLowerCase());
				ps2.setString(4,project.getDescription());
				ps2.setString(5,project.getCompulsoryReading().toLowerCase());
				ps2.setInt(6,rs.getInt("projectID"));
				ps2.executeUpdate();
				ps2.close();
				//newConnection.commit();
			}
			rs.close();
			ps.close();
			return 0;
		} catch (SQLException e) {
			return 2;
		}
	}

	/**
	 * SQL method used to insert into the DB new projects
	 * @param project
	 * @param userID
	 * @return
	 */
	public int save(Project project, int userID) {
		if(checkIfProjectExistInDBSave(project)) return 1;
		String query = " INSERT INTO project (year, title, topic, compulsoryreading, description, lecturerID,"
				+ "visible, documentID, waitingtobeapproved, checklistID, visitcounter)"
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement preparedStmt = newConnection.prepareStatement(query);
			preparedStmt.setInt (1, project.getYear());
			preparedStmt.setString (2, project.getTitle());
			preparedStmt.setString (3, project.getTopics().toString().toLowerCase());
			preparedStmt.setString (4, project.getCompulsoryReading().toString().toLowerCase());
			preparedStmt.setString (5, project.getDescription());
			preparedStmt.setInt (6, userID);
			preparedStmt.setBoolean (7, true);
			preparedStmt.setInt (8, 1);
			preparedStmt.setBoolean (9, project.isWaitingToBeApproved());
			preparedStmt.setInt (10, 1);
			preparedStmt.setInt (11, 0);
			preparedStmt.execute();
			preparedStmt.close();
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 2;
	}

	/**
	 * SQL Method that check if a project it is already in the DB, 
	 * this method it is only use when a new project is created
	 * @param project
	 * @return
	 */
	public boolean checkIfProjectExistInDBSave(Project project) {
		String query = " SELECT * FROM project WHERE year = ? AND title = ?";
		try {
			PreparedStatement ps = newConnection.prepareStatement(query);
			ps.setInt (1, project.getYear());
			ps.setString (2, project.getTitle());
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				return true;
			}else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * SQL Method that check if a project it is already in the DB, 
	 * this method is only used when I am editing a project
	 * @param project
	 * @return
	 */
	public boolean checkIfProjectExistInDBSaveEdit(Project project) {
		String query = " SELECT * FROM project WHERE year = ? AND title = ? "
				+ "AND topic = ? AND compulsoryreading = ? AND description = ?";
		try {
			PreparedStatement ps = newConnection.prepareStatement(query);
			ps.setInt (1, project.getYear());
			ps.setString (2, project.getTitle());
			ps.setString (3, project.getTopics());
			ps.setString (4, project.getCompulsoryReading());
			ps.setString (5, project.getDescription());
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				return true;
			}else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * SQL method to search within the DB based on search criteria
	 * @param lecturer
	 * @param technology
	 * @param title
	 * @param searchValue
	 * @return
	 */
	public List<Project> search(String lecturer, String technology, String title, String searchValue) {
		PreparedStatement ps;
		List<Project> projectList = new ArrayList<Project>();
		try {
			ps = newConnection.prepareStatement(
					"SELECT DISTINCT project.* FROM project WHERE NOT EXISTS "
							+ "(SELECT * FROM interestproject WHERE project.projectID = interestproject.projectID) "
							+ "AND visible = ? AND waitingtobeapproved = ? AND year = ?");
			ps.setBoolean(1,true);
			ps.setBoolean(2,true);
			ps.setInt(3, actualYear);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			searchValue = searchValue.toLowerCase();
			while (rs.next())
			{
				//if(rs.getBoolean("waitingtobeapproved") == false) continue; //if the project is not approved, then added to the view
				Project project = new Project();
				if(technology != null) {
					if(rs.getString("topic").toLowerCase().contains(searchValue)) {
						if(rs.getInt("lecturerID")!=0) { //if the ID is 0 then ignore it
							User actualUser = getUser(rs.getInt("lecturerID"));
							if(actualUser == null) continue; //if by any chance the ID has no lecturer then do not add it.
							project.setUser(actualUser);
						}	

						project.setProjectID(rs.getInt("projectID"));
						project.setTitle(rs.getString("title"));
						project.setDescription(rs.getString("description"));
						project.setCompulsoryReading(rs.getString("compulsoryReading").replaceAll("[\\[\\]\\(\\)]", ""));
						project.setTopics(rs.getString("topic").replaceAll("[\\[\\]\\(\\)]", ""));

						projectList.add(project);

					}						
				}
				if(title != null) {
					if(rs.getString("title").toLowerCase().contains(searchValue)) {
						if(rs.getInt("lecturerID")!=0) { //if the ID is 0 then ignore it
							User actualUser = getUser(rs.getInt("lecturerID"));
							if(actualUser == null) continue; //if by any chance the ID has no lecturer then do not add it.
							project.setUser(actualUser);
						}	

						project.setProjectID(rs.getInt("projectID"));
						project.setTitle(rs.getString("title"));
						project.setDescription(rs.getString("description"));
						project.setCompulsoryReading(rs.getString("compulsoryReading").replaceAll("[\\[\\]\\(\\)]", ""));
						project.setTopics(rs.getString("topic").replaceAll("[\\[\\]\\(\\)]", ""));

						projectList.add(project);
					}
				}
				if(lecturer != null) {
					User user = getUserByName(searchValue);
					if(user == null) {
						return null;
					}
					if(user.getUserID() == rs.getInt("lecturerID")){
						if(rs.getInt("lecturerID")!=0) { //if the ID is 0 then ignore it
							User actualUser = getUser(rs.getInt("lecturerID"));
							if(actualUser == null) continue; //if by any chance the ID has no lecturer then do not add it.
							project.setUser(actualUser);
						}	

						project.setProjectID(rs.getInt("projectID"));
						project.setTitle(rs.getString("title"));
						project.setDescription(rs.getString("description"));
						project.setCompulsoryReading(rs.getString("compulsoryReading").replaceAll("[\\[\\]\\(\\)]", ""));
						project.setTopics(rs.getString("topic").replaceAll("[\\[\\]\\(\\)]", ""));

						projectList.add(project);
					}
				}
			}
			rs.close();
		} catch (SQLException e) {
			return null;
		}
		return projectList;
	}

	/**
	 * SQL method to insert into the DB a new checklist for the schedule
	 * @param checklist
	 * @return
	 */
	public int saveCheckList(CheckList checklist) {
		if(checkIfEventExistInDBNewEvent(checklist)) return 1;
		String query = " INSERT INTO checklist (date, eventname, place, description, visible, hour, endhour)"
				+ " VALUES (?, ?, ?, ?, ?, ?,?)";
		try {
			PreparedStatement preparedStmt = newConnection.prepareStatement(query);
			preparedStmt.setString (1, checklist.getDate());
			preparedStmt.setString (2, checklist.getEventName());
			preparedStmt.setString (3, checklist.getPlace());
			preparedStmt.setString (4, checklist.getDescription());
			preparedStmt.setBoolean (5, true);
			preparedStmt.setString (6,checklist.getHour());
			preparedStmt.setString (7,checklist.getEndHour());
			preparedStmt.execute();
			preparedStmt.close();
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return 2;
	}
	/**
	 * SQL Method to check if an event already exist in the DB when trying to save a modification
	 * I am checking everything since I do not want user to be able to submit the same event without modification
	 * @param checklist
	 * @return
	 */
	public boolean checkIfEventExistInDBSaveEdit(CheckList checklist) {
		String query = " SELECT * FROM checklist WHERE date = ? AND eventname = ? AND place = ? AND description = ? AND hour = ?"
				+ " AND endhour = ?";
		try {
			PreparedStatement ps = newConnection.prepareStatement(query);
			//I am parsing to SQLDate since Date and SQLDate are different and does not work with prepared statements
			java.sql.Date sqlDate = java.sql.Date.valueOf( checklist.getDate() );
			ps.setDate(1, sqlDate);
			ps.setString (2, checklist.getEventName());	
			ps.setString(3, checklist.getPlace());
			ps.setString(4, checklist.getDescription());
			ps.setString(5, checklist.getHour());
			ps.setString (6,checklist.getEndHour());
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				return true; 
			}else {
				return false;
			}
		} catch (SQLException e) {
		}

		return false;
	}
	/**
	 * SQL Method to check if an event already exist in the DB when creating a new event
	 * I am only checking if the same event name and date are already in the DB
	 * @param checklist
	 * @return
	 */
	public boolean checkIfEventExistInDBNewEvent(CheckList checklist) {
		String query = " SELECT * FROM checklist WHERE date = ? AND eventname = ?";
		try {
			PreparedStatement ps = newConnection.prepareStatement(query);
			//I am parsing to SQLDate since Date and SQLDate are different and does not work with prepared statements
			java.sql.Date sqlDate = java.sql.Date.valueOf( checklist.getDate() );
			ps.setDate(1, sqlDate);
			ps.setString (2, checklist.getEventName());	
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				return true; 
			}else {
				return false;
			}
		} catch (SQLException e) {
		}

		return false;
	}

	/**
	 * SQL method to save any modification of a checklist into the DB
	 * @param checklist
	 * @return
	 */
	public int saveEditCheckList(CheckList checklist) {
		if(checkIfEventExistInDBSaveEdit(checklist)) return 1;
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT * FROM checklist WHERE checklistID = ? FOR UPDATE");
			ps.setInt(1,checklist.getCheckListID());
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				PreparedStatement ps2 = newConnection.prepareStatement(
						"UPDATE checklist SET date = ?, eventname = ?, place = ?, description = ?, visible = ?, hour = ? , endhour = ?"
								+ " WHERE checklistID = ?");
				ps2.setString (1, checklist.getDate());
				ps2.setString (2, checklist.getEventName());
				ps2.setString (3, checklist.getPlace());
				ps2.setString (4, checklist.getDescription());
				ps2.setBoolean (5, true);
				ps2.setString (6, checklist.getHour());
				ps2.setString (7,checklist.getEndHour());
				ps2.setInt (8,rs.getInt("checklistID"));
				ps2.executeUpdate();
				ps2.close();
				ps.close();
				rs.close();
				return 0;
				//newConnection.commit();
			}
		} catch (SQLException e) {
			return 2;
		}
		return 2;

	}

	/**
	 * SQL method to save the interest of a student with a project
	 * @param userID
	 * @param projectID
	 * @return 5 if limit of interest project is reach, 0 if error, 2 if student already register with that project and 1 if everything is OK
	 */
	public int registerInterest(int userID, int projectID) {
		//This if statement is checking if the student already have a final project approve, if so then cannot register interest
		if(checkIfStudentHaveFinalProject(userID)) return 3;
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT COUNT(*) AS total FROM interestproject WHERE userID = ? AND visible = ? AND year = ?");
			ps.setInt(1,userID);
			ps.setBoolean(2, true);
			ps.setInt(3, actualYear);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				if(rs.getInt("total") == 5) {
					return 5;
				}
				else {
					//Need to do a second query since the first one I am only getting the count and no the actual table data
					//System.out.println("This student have " + rs.getInt("total") + " projects already");
					PreparedStatement ps2 = newConnection.prepareStatement("SELECT * FROM interestproject WHERE year = ?");
					ps2.setInt(1, actualYear);
					ps2.getResultSet();
					ResultSet rsProject = ps2.executeQuery();
					while(rsProject.next()) {
						//This is taking care that if the student is already register for that project, cannot register again 
						if(rsProject.getInt("projectID") == projectID && rsProject.getInt("userID") == 
								userID) return 2;
					}
					rsProject.close();
					ps2.close();
					//If the project is not already on the table and the student has not more than 5 projects already then we will add
					//the new project to the table
					String queryInsert = " INSERT INTO interestproject (userID, projectID, visible, year) VALUES (?, ?, ?, ?)";
					PreparedStatement preparedStmt = newConnection.prepareStatement(queryInsert);
					preparedStmt.setInt (1, userID);
					preparedStmt.setInt (2, projectID);
					preparedStmt.setBoolean (3, true);//make the interest visible
					preparedStmt.setInt(4, actualYear);
					preparedStmt.execute();
					preparedStmt.close();
				}	
				rs.close();
				ps.close();
				return 1;
			}
		} catch (SQLException e) {
			return 0;
		}
		return 0;
	}
	
	/**
	 * SQL Method that is returning true if student already have a final project approved
	 * And false if not
	 * This method is use in register interest to be sure that student that already have a final project
	 * cannot register interest in more projects
	 * @param studentID
	 * @return
	 */
	public boolean checkIfStudentHaveFinalProject(int studentID) {
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT * FROM approvedproject WHERE userID = ? AND visible = ? AND year = ?");
			ps.setInt(1,studentID);
			ps.setBoolean(2, true);
			ps.setInt(3, actualYear);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				return true;
			}
		}catch(Exception e) {
			
			}
		return false;
	}

	/**
	 * SQL method to count the number of projects that a student had registered interest
	 * @param userID
	 * @param status it is use to show if you want to get projects visible or invisible
	 * @return
	 */
	public int getTotalInterestProject(int userID, boolean status) {
		try {
			PreparedStatement ps = newConnection.prepareStatement(
					"SELECT COUNT(*) AS total FROM interestproject WHERE userID = ? AND visible = ? AND year = ?");
			ps.setInt(1,userID);
			ps.setBoolean(2, status);
			ps.setInt(3, actualYear);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				int total = rs.getInt("total");
				rs.close();
				ps.close();
				return total;
			}
		} catch (SQLException e1) {
			return 0;
		}
		return 0;
	}

	/**
	 * SQL method used to approve the interest on a project by a student, after been approved the student will have a final project
	 * @param projectID
	 * @param user
	 * @return
	 */
	public boolean approveInteret(int projectID, User user) {
		@SuppressWarnings("unused")
		Project project = new Project();
		project = getProject(projectID);
		//getting user based on the userID, so I can have access to all his data
		user = getUser(user.getUserID());
		String queryInsert = " INSERT INTO approvedproject (userID, projectID, visible, year) VALUES (?, ?, ?, ?)";
		PreparedStatement preparedStmt;
		try {
			preparedStmt = newConnection.prepareStatement(queryInsert);
			preparedStmt.setInt (1, user.getUserID());
			preparedStmt.setInt (2, projectID);
			preparedStmt.setBoolean(3, true);
			preparedStmt.setInt(4, actualYear);
			preparedStmt.execute();
			preparedStmt.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * SQL method used to obtain the last projectID saved on the DB
	 * @return
	 * @throws SQLException
	 */
	public int getLastProjectID() throws SQLException {
		//if(newConnection == null) startDBConnection();
		String query = "SELECT projectID FROM project ORDER BY projectID DESC LIMIT 1";
		Statement st = newConnection.createStatement();
		ResultSet rs = st.executeQuery(query);
		while (rs.next())
		{
			return(rs.getInt("projectID"));
		}
		rs.close();
		st.close();
		return 0;
	}

	/**
	 * SQL method used to obtain the last checkListID saved on the DB
	 * @return
	 * @throws SQLException
	 */
	public int getLastChecklistID() throws SQLException {
		//if(newConnection == null) startDBConnection();
		String query = "SELECT checklistID FROM checklist ORDER BY checklistID DESC LIMIT 1";
		Statement st = newConnection.createStatement();
		ResultSet rs = st.executeQuery(query);
		while (rs.next())
		{
			return(rs.getInt("checklistID"));
		}
		rs.close();
		st.close();
		return 0;
	}

	/**
	 * SQL method to obtain an user object based on the userID (which is unique)
	 * @param userID
	 * @return
	 */
	public User getUser(int userID) {
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT * FROM user WHERE userID = ?");
			ps.setInt(1,userID);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			User user = new User();
			if(rs.next()) {
				user.setUserID(rs.getInt("userID"));
				user.setEmail(rs.getString("email"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setUserType(rs.getInt("userType"));
				user.setYear(rs.getInt("year"));
				rs.close();
				ps.close();
				return user;
			}
		} catch (SQLException e) {
			return null;
		}		
		return null;
	}

	/**
	 * SQL method to obtain an user object based on the username (which is unique)
	 * @param username
	 * @return
	 */
	public User getUserByName(String username) {
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT * FROM user WHERE username = ?");
			ps.setString(1,username);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			User user = new User();
			if(rs.next()) {
				user.setUserID(rs.getInt("userID"));
				user.setEmail(rs.getString("email"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setUserType(rs.getInt("userType"));
				user.setYear(rs.getInt("year"));
				rs.close();
				ps.close();
				return user;
			}
		} catch (SQLException e) {
			return null;
		}		
		return null;
	}

	/**
	 * SQL method to obtain an user object based on the email
	 * @param username
	 * @return
	 */
	public User getUserByEmail(String email) {
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT * FROM user WHERE email = ?");
			ps.setString(1,email);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			User user = new User();
			if(rs.next()) {
				user.setUserID(rs.getInt("userID"));
				user.setEmail(rs.getString("email"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setUserType(rs.getInt("userType"));
				user.setYear(rs.getInt("year"));
				rs.close();
				ps.close();
				return user;
			}
		} catch (SQLException e) {
			return null;
		}		
		return null;
	}

	/**
	 * Method to get all the projects
	 * @param approved in this way I can reuse the method to get list of project that had been approved or not
	 * @return
	 */
	public List<Project> getProjectList(boolean approved) {
		List<Project> projectList = new ArrayList<Project>();
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT * FROM project WHERE year = ?");
			ps.setInt(1,actualYear);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				//if the project is not approved, then you wont show the project
				if(rs.getBoolean("waitingtobeapproved") == approved) continue; 
				Project project = new Project();
				if(rs.getInt("lecturerID")!=0) { //if the ID is 0 then ignore it
					User actualUser = getUser(rs.getInt("lecturerID"));
					if(actualUser == null) continue; //if by any chance the ID has no lecturer then do not add it.
					project.setUser(actualUser);
				}	

				project.setProjectID(rs.getInt("projectID"));
				project.setTitle(rs.getString("title"));
				project.setDescription(rs.getString("description"));
				project.setCompulsoryReading(rs.getString("compulsoryReading").replaceAll("[\\[\\]\\(\\)]", ""));
				project.setTopics(rs.getString("topic").replaceAll("[\\[\\]\\(\\)]", ""));

				projectList.add(project);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			return projectList;
		}
		return projectList;
	}

	/**
	 * SQL method to get all the projects that are visible
	 * @param status
	 * @return
	 */
	public List<Project> getProjectListVisible(boolean status, int lecturerID) {
		List<Project> projectList = new ArrayList<Project>();
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT * FROM project WHERE year = ? AND lecturerID = ?");
			ps.setInt(1,actualYear);
			ps.setInt(2, lecturerID);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				//if the project is not approved, then you wont show the project
				if(rs.getBoolean("visible") == status) {
					Project project = new Project();
					if(rs.getInt("lecturerID")!=0) { //if the ID is 0 then ignore it
						User actualUser = getUser(rs.getInt("lecturerID"));
						if(actualUser == null) continue; //if by any chance the ID has no lecturer then do not add it.
						project.setUser(actualUser);
					}	
					project.setProjectID(rs.getInt("projectID"));
					project.setTitle(rs.getString("title"));
					project.setDescription(rs.getString("description"));
					project.setCompulsoryReading(rs.getString("compulsoryReading").replaceAll("[\\[\\]\\(\\)]", ""));
					project.setTopics(rs.getString("topic").replaceAll("[\\[\\]\\(\\)]", ""));
					project.setVisible(rs.getBoolean("visible"));
					project.setWaitingToBeApproved(rs.getBoolean("waitingtobeapproved"));
					projectList.add(project);
				}			
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			return projectList;
		}		
		return projectList;
	}

	/**
	 * SQL method to get all the projects for the next year by lecturer
	 * @param status
	 * @return
	 */
	public List<Project> getProjectListNextYear(int lecturerID, boolean status) {
		List<Project> projectList = new ArrayList<Project>();
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT * FROM project WHERE year = ? AND lecturerID = ?");
			ps.setInt(1,actualYear + 1);
			ps.setInt(2, lecturerID);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				//if the project is not approved, then you wont show the project
				if(rs.getBoolean("visible") == status) {
					Project project = new Project();
					if(rs.getInt("lecturerID")!=0) { //if the ID is 0 then ignore it
						User actualUser = getUser(rs.getInt("lecturerID"));
						if(actualUser == null) continue; //if by any chance the ID has no lecturer then do not add it.
						project.setUser(actualUser);
					}	
					project.setProjectID(rs.getInt("projectID"));
					project.setTitle(rs.getString("title"));
					project.setDescription(rs.getString("description"));
					project.setCompulsoryReading(rs.getString("compulsoryReading").replaceAll("[\\[\\]\\(\\)]", ""));
					project.setTopics(rs.getString("topic").replaceAll("[\\[\\]\\(\\)]", ""));
					project.setVisible(rs.getBoolean("visible"));
					project.setWaitingToBeApproved(rs.getBoolean("waitingtobeapproved"));
					projectList.add(project);
				}
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			return projectList;
		}		
		return projectList;
	}

	/**
	 * SQL method that it is only showing projects that had not been already choose by another student, in other words
	 * get projects which projectID are not in the interestproject table and that are visible
	 * I am taking care of the visibility since I need to worry if a student remove the interest of a project then that project
	 * should be back to the list
	 * @param statusApproved
	 * @param statusVisible
	 * @return
	 */
	public List<Project> getProjectListVisibleAnDApprove(boolean statusApproved, boolean statusVisible) {
		PreparedStatement ps;
		List<Project> projectList = new ArrayList<Project>();

		try {
			//I am using distinct since does not make sense to have the same project twice
			ps = newConnection.prepareStatement(
					"SELECT DISTINCT project.* FROM project WHERE NOT EXISTS "
							+ "(SELECT * FROM interestproject WHERE project.projectID = interestproject.projectID AND visible = ?)"
							+ "AND NOT EXISTS (SELECT * FROM approvedproject WHERE project.projectID = approvedproject.projectID AND visible = ?)"
							+ "AND project.year = ? AND project.visible = ?");
			ps.setBoolean(1,true);
			ps.setBoolean(2,true);
			ps.setInt(3, actualYear);
			ps.setBoolean(4,true);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				//if the project is not approved, then you wont show the project
				if(rs.getBoolean("visible") == statusVisible && rs.getBoolean("waitingtobeapproved") == statusApproved) {
					Project project = new Project();
					if(rs.getInt("lecturerID")!=0) { //if the ID is 0 then ignore it since I have some projects with lecturerID 0
						User actualUser = getUser(rs.getInt("lecturerID"));
						if(actualUser == null) continue; //if by any chance the ID has no lecturer then do not add it.
						project.setUser(actualUser);
					}	
					project.setProjectID(rs.getInt("projectID"));
					project.setTitle(rs.getString("title"));
					project.setDescription(rs.getString("description"));
					project.setCompulsoryReading(rs.getString("compulsoryReading").replaceAll("[\\[\\]\\(\\)]", ""));
					project.setTopics(rs.getString("topic").replaceAll("[\\[\\]\\(\\)]", ""));
					project.setVisible(rs.getBoolean("visible"));
					project.setWaitingToBeApproved(rs.getBoolean("waitingtobeapproved"));

					projectList.add(project);
				}			
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			return projectList;
		}
		return projectList;

	}
	
	/**
	 * SQL Method that is taking all the projects that are not already chosen or approved for other students
	 * but only the projects that are between the range of projects that are the difference between the total
	 * of projects from your last vist and the list of project from your actual visit
	 * @param statusApproved
	 * @param statusVisible
	 * @param lowLimit
	 * @param highLimit
	 * @return
	 */
	public List<Project> getProjectListVisibleAnDApprove(boolean statusApproved, boolean statusVisible, int lowLimit, int highLimit) {
		PreparedStatement ps;
		List<Project> projectList = new ArrayList<Project>();

		try {
			//I am using distinct since does not make sense to have the same project twice
			ps = newConnection.prepareStatement(
					"SELECT DISTINCT project.* FROM project WHERE NOT EXISTS "
							+ "(SELECT * FROM interestproject WHERE project.projectID = interestproject.projectID AND visible = ?)"
							+ "AND NOT EXISTS (SELECT * FROM approvedproject WHERE project.projectID = approvedproject.projectID AND visible = ?)"
							+ "AND project.year = ? AND project.visible = ? ORDER BY projectID DESC LIMIT ?");
			int dif = highLimit - lowLimit;
			ps.setBoolean(1,true);
			ps.setBoolean(2,true);
			ps.setInt(3, actualYear);
			ps.setBoolean(4,true);
			ps.setInt(5, dif);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				//if the project is not approved, then you wont show the project
				if(rs.getBoolean("visible") == statusVisible && rs.getBoolean("waitingtobeapproved") == statusApproved) {
					Project project = new Project();
					if(rs.getInt("lecturerID")!=0) { //if the ID is 0 then ignore it since I have some projects with lecturerID 0
						User actualUser = getUser(rs.getInt("lecturerID"));
						if(actualUser == null) continue; //if by any chance the ID has no lecturer then do not add it.
						project.setUser(actualUser);
					}	
					project.setProjectID(rs.getInt("projectID"));
					project.setTitle(rs.getString("title"));
					project.setDescription(rs.getString("description"));
					project.setCompulsoryReading(rs.getString("compulsoryReading").replaceAll("[\\[\\]\\(\\)]", ""));
					project.setTopics(rs.getString("topic").replaceAll("[\\[\\]\\(\\)]", ""));
					project.setVisible(rs.getBoolean("visible"));
					project.setWaitingToBeApproved(rs.getBoolean("waitingtobeapproved"));

					projectList.add(project);
				}			
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			return projectList;
		}
		return projectList;

	}

	/**
	 * SQL method to get a list of all the events for the schedule between actual year and next year
	 * @param status
	 * @return
	 */
	public List<CheckList> getCheckListList(boolean status) {
		List<CheckList> checklistList = new ArrayList<CheckList>();
		PreparedStatement ps;
		try {
			/**
			 * I am converting here the actual year to string and concatenating a date string
			 * because I need to say in my SQL query two dates between to choose the events
			 * I am converting to SQL date since my DB is using this kind of date
			 */
			String actualYearDate = Integer.toString(actualYear) + "-03-01";
			java.sql.Date sqlDateActualYear = java.sql.Date.valueOf( actualYearDate );
			String nextYearDate = Integer.toString(actualYear + 1) + "-05-31";
			java.sql.Date sqlDateNextYear = java.sql.Date.valueOf( nextYearDate );
			ps = newConnection.prepareStatement(
					"SELECT * FROM checklist WHERE date BETWEEN ? AND ?");
			ps.setDate(1,sqlDateActualYear);
			ps.setDate(2,sqlDateNextYear);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				CheckList checklist = new CheckList();
				if(rs.getBoolean("visible") == status) {//only if the checklist is visible will be show
					checklist.setCheckListID(rs.getInt("checklistID"));
					checklist.setDate(rs.getString("date"));
					checklist.setEventName(rs.getString("eventname"));
					checklist.setPlace(rs.getString("place"));
					checklist.setDescription(rs.getString("description"));
					checklist.setHour(rs.getString("hour"));
					checklist.setEndHour(rs.getString("endhour"));
					checklistList.add(checklist);
				}
			}
			rs.close();
			ps.close();
		}catch(SQLException e) {
			return checklistList;
		}
		return checklistList;
	}
	
	/**
	 * SQL Method to obtain the last number of elements from the DB that are the newer
	 * @param status
	 * @param lowLimit
	 * @param highLimit
	 * @return
	 */
	public List<CheckList> getCheckListList(boolean status, int lowLimit, int highLimit) {
		List<CheckList> checklistList = new ArrayList<CheckList>();
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT * FROM checklist ORDER BY checklistID DESC LIMIT ?;");
			//I am only interested in the difference between them
			int dif = highLimit - lowLimit;
			ps.setInt(1,dif);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				CheckList checklist = new CheckList();
				if(rs.getBoolean("visible") == status) {//only if the checklist is visible will be show
					checklist.setCheckListID(rs.getInt("checklistID"));
					checklist.setDate(rs.getString("date"));
					checklist.setEventName(rs.getString("eventname"));
					checklist.setPlace(rs.getString("place"));
					checklist.setDescription(rs.getString("description"));
					checklist.setHour(rs.getString("hour"));
					checklist.setEndHour(rs.getString("endhour"));
					checklistList.add(checklist);
				}
			}
			rs.close();
			ps.close();
		}catch(SQLException e) {
			return checklistList;
		}
		return checklistList;
	}

	/**
	 * SQL method that get a list of the projects that a student had show interest
	 * @param status
	 * @param userLoginID
	 * @return
	 */
	public List<Project> getProjectInterestedListByStudent(boolean status, int userLoginID) {
		List<Project> projectList = new ArrayList<Project>();
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT * FROM interestproject WHERE userID = ? AND year = ?");
			ps.setInt(1,userLoginID);
			ps.setInt(2, actualYear);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				if(rs.getBoolean("visible") == status) {
					Project project = new Project();
					project = getProject(rs.getInt("projectID"));
					User student = new User();
					student = getUser(rs.getInt("userID"));
					project.setStudent(student);
					projectList.add(project);
				}			
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			return projectList;
		}

		return projectList;
	}

	/**
	 * SQL method to get a list of all the project with interest show by students
	 * @return
	 */
	public List<Project> getProjectInterestedList() {
		List<Project> projectList = new ArrayList<Project>();
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT * FROM interestproject WHERE year = ?");
			ps.setInt(1,actualYear);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				Project project = new Project();
				project = getProject(rs.getInt("projectID"));
				User student = new User();
				student = getUser(rs.getInt("userID"));
				project.setStudent(student);
				projectList.add(project);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			return projectList;
		}

		return projectList;
	}

	/**
	 * SQL method to obtain all the projects that a lecturer have in the system
	 * @param userLoginID
	 * @return
	 */
	public List<Project> getAllProjectByLecturer(int userLoginID) {
		List<Project> projectList = new ArrayList<Project>();
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT * FROM project WHERE lecturerID =  ? AND year = ?");
			ps.setInt(1,userLoginID);
			ps.setInt(2, actualYear);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				Project project = new Project();
				project = getProject(rs.getInt("projectID"));
				projectList.add(project);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			return projectList;
		}		
		return projectList;
	}

	/**
	 * SQL method to get all the students that show interest
	 * @return
	 */
	public List<User> getAllUserswithInterest() {
		List<User> userList = new ArrayList<User>();
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT * FROM interestproject WHERE year = ?");
			ps.setInt(1,actualYear);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				User user = new User();
				user = getUser(rs.getInt("userID"));
				userList.add(user);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			return userList;
		}			
		return userList;
	}

	/**
	 * Method that return the list of project that students show interest 
	 * @param userLoginID
	 * @return projectList
	 */
	public List<Project> getLecturerProjectList(int userLoginID) {
		//Since I am only accepting project that their visibility is true, I am controlling that if another lecturer approve
		//a student all the student list will stop show the interest of that student
		//The student interest will still be keep on the DB but hidden
		List<Project> projectList = new ArrayList<Project>();
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT * FROM project,interestproject WHERE interestproject.projectID = project.projectID "
							+ " AND lecturerID = ? AND interestproject.visible = ? AND interestproject.year = ?;");
			ps.setInt(1,userLoginID);
			ps.setBoolean(2, true);
			ps.setInt(3, actualYear);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				Project project = new Project();
				project = getProject(rs.getInt("projectID"));
				//Here I am getting the student who applied to this project
				User student = getUser(rs.getInt("userID"));
				project.setStudent(student);
				projectList.add(project);
			}
		} catch (SQLException e) {
			return projectList;
		}

		return projectList;
	}

	/**
	 * SQL method to get the final project (if any) of a student based on his ID
	 * @param userID
	 * @return
	 */
	public Project getFinalProjectStudent(int userID) {
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT * FROM approvedproject WHERE userID = ? AND visible = ? AND year = ?");
			ps.setInt(1,userID);
			ps.setBoolean(2, true);
			ps.setInt(3, actualYear);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Project project = new Project();
				project = getProject(rs.getInt("projectID"));
				User student = getUser(userID);
				project.setVisible(rs.getBoolean("visible"));//setting visible 
				project.setStudent(student);
				return project;
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			return null;
		}			
		return null;
	}

	/**
	 * SQL method to get a list of all the students that we have in the DB
	 * @return
	 */
	public List<User> getAllStudentList(int year) {
		List<User> userList = new ArrayList<User>();
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT * from user WHERE user.userType = ? AND year = ?");
			ps.setInt(1,2);
			ps.setInt(2, year);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				User student = getUser(rs.getInt("userID"));
				userList.add(student);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			return userList;
		}		
		return userList;
	}

	/**
	 * SQL method to save any modification in the DB of a checkList
	 * @param checklistID
	 * @param status
	 */
	public void updateChecklist(int checklistID, boolean status) {
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT * FROM checklist WHERE checklistID =  ? FOR UPDATE");
			ps.setInt(1,checklistID);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				PreparedStatement ps2 = newConnection.prepareStatement(
						"UPDATE checklist SET visible = ? WHERE checklistID = ?");
				ps2.setBoolean(1, status);
				ps2.setInt(2, checklistID);
				ps2.execute();
				ps2.close();
				//newConnection.commit();
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
		}

	}

	/**
	 * SQL method to save in the DB the visibility of a project
	 * @param projectID
	 * @param status true to make it visible false for invisible
	 */
	public void updateProject(int projectID, boolean status) {
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT * FROM project WHERE projectID = ? FOR UPDATE");
			ps.setInt(1,projectID);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				PreparedStatement ps2 = newConnection.prepareStatement(
						"UPDATE project SET visible = ? WHERE projectID = ?");
				ps2.setBoolean(1, status);
				ps2.setInt(2, projectID);
				ps2.execute();
				ps2.close();
				//newConnection.commit();
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
		}
	}

	/**
	 * SQL method to update the interest on a project, this method it is use when a student or a lecturer remove the interest on a project
	 * Lecturer remove the interest when the lecturer reject the interest of the student
	 * @param projectID
	 * @param userID
	 * @param status
	 */
	public void updateInterestProject(int projectID, int userID, boolean status) {
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT * FROM interestproject WHERE projectID = ? AND userID = ? FOR UPDATE");
			ps.setInt(1,projectID);
			ps.setInt(2,userID);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				PreparedStatement ps2 = newConnection.prepareStatement(
						"UPDATE interestproject SET visible = ? WHERE projectID = ? AND userID = ?");
				ps2.setBoolean(1, status);
				ps2.setInt(2, projectID);
				ps2.setInt(3, userID);
				ps2.execute();
				ps2.close();
				//newConnection.commit();
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
		}
	}

	/**
	 * SQL method use by the dissertation coordinator that it is use to remove the interest on a project after been approved by a lecturer
	 * Only the dissertation coordinator can do this
	 * @param projectID
	 * @param userID
	 * @param status
	 */
	public void updateInterestFinalProject(int projectID, int userID, boolean status) {
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT * FROM approvedproject WHERE projectID = ? AND userID = ? FOR UPDATE");
			ps.setInt(1,projectID);
			ps.setInt(2,userID);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				PreparedStatement ps2 = newConnection.prepareStatement(
						"UPDATE approvedproject SET visible = ? WHERE projectID = ? AND userID = ?");
				ps2.setBoolean(1, status);
				ps2.setInt(2, projectID);
				ps2.setInt(3, userID);
				ps2.execute();
				ps2.close();
				//newConnection.commit();
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
		}
	}

	/**
	 * SQL method that after a student is approve to a project by a lecturer I am making all the other project that the student show interest
	 * invisible, so other student can choose those projects
	 * @param userID
	 */
	public void updateListOfProjectAfterApproveInterest(int userID) {
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT * FROM interestproject WHERE userID = ? AND year = ? FOR UPDATE");
			ps.setInt(1,userID);
			ps.setInt(2, actualYear);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				//if(rs.getInt("projectID") == projectID)continue;//if is our project, then keep it visible
				PreparedStatement ps2 = newConnection.prepareStatement(
						"UPDATE interestproject SET visible = ? WHERE userID = ? AND year = ?");
				ps2.setBoolean(1, false);
				ps2.setInt(2, userID);
				ps2.setInt(3, actualYear);
				ps2.execute();
				ps2.close();
				//newConnection.commit();
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
		}
	}

	/**
	 * SQL method that get a list of the project based on the year passed as parameter
	 * @param year
	 * @return
	 */
	public List<Project> getProjectsByYear(int year) {
		List<Project> projectList = new ArrayList<Project>();
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT * FROM project WHERE year = ?");
			ps.setInt(1,year);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Project project = new Project();
				project = getProject(rs.getInt("projectID"));
				project.setVisible(rs.getBoolean("visible"));//setting visible 
				projectList.add(project);
			}
			rs.close();
			ps.close();
			return projectList;
		} catch (SQLException e) {
			return projectList;
		}
	}

	/**
	 * SQL method to get a list of all the years that I have in the DB
	 * @return
	 */
	public List<Integer> getAllYearOfProjects() {
		List<Integer> yearList = new ArrayList<Integer>();

		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT DISTINCT year FROM project WHERE year < ?");
			ps.setInt(1,actualYear);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				yearList.add(rs.getInt("year"));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			return yearList;
		}
		return yearList;
	}

	/**
	 * SQL method that I am using to get a single project based on its ID and return a project object
	 * @param id
	 * @return
	 */
	public Project getProject(int projectID) {
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT * FROM project WHERE projectID = ?");
			ps.setInt(1,projectID);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				/*
				 * instead of creating new Lecturer, Document and CheckList I should make a call based on the project ID
				 * and get the reference to the DB to those items and create new objects populated with the data from the DB in this way
				 * should work better
				 */
				PreparedStatement ps2 = newConnection.prepareStatement(
						"SELECT * FROM user WHERE userID = ?");
				ps2.setInt(1,rs.getInt("lecturerID"));
				ps2.getResultSet();
				ResultSet rsUser = ps2.executeQuery();
				User user = new User();
				while (rsUser.next())
				{
					user.setUserID(rsUser.getInt("userID"));
					user.setEmail(rsUser.getString("email"));
					user.setUsername(rsUser.getString("username"));
					user.setPassword(rsUser.getString("password"));
					user.setUserType(rsUser.getInt("userType"));
				}
				rsUser.close();
				ps2.close();
				Project project = new Project(rs.getInt("projectID"), rs.getInt("year"), rs.getString("title"), rs.getString("topic"),
						rs.getString("compulsoryReading"), rs.getString("description"), user, 
						rs.getBoolean("visible"),new Document(rs.getInt("documentID")), rs.getBoolean("waitingtobeapproved"), 
						new CheckList(),user.getUserID());
				rs.close();
				ps.close();
				return project;
			}		
		} catch (SQLException e1) {
			return null;
		}

		return null;
	}

	/**
	 * SQL method to check if a project is already choose for another student or approved by a lecturer, before another student make his choice visible
	 * In other words, if I have a project that is not visible and try to make it visible I am checking if someone already choose that project
	 * or a lecturer already approve that project to someone else
	 * @param projectID
	 * @return
	 */
	public boolean checkIfProjectIsAlreadyChoose(int projectID) {
		PreparedStatement ps, ps2;
		try {
			//I tried to mix box queries but I was getting error, if I have more time I will try to improve this query
			ps = newConnection.prepareStatement("SELECT COUNT(*) AS total FROM interestproject WHERE projectID = ? AND visible = ? AND year = ?");
			ps2 = newConnection.prepareStatement("SELECT COUNT(*) AS totalapproved FROM approvedproject WHERE projectID = ? AND visible = ? AND year = ?");
			ps.setInt(1,projectID);
			ps.setBoolean(2, true);
			ps.setInt(3, actualYear);
			ps2.setInt(1,projectID);
			ps2.setBoolean(2, true);
			ps2.setInt(3, actualYear);
			ps.getResultSet();
			ps2.getResultSet();
			ResultSet rs = ps.executeQuery();
			ResultSet rs2 = ps2.executeQuery();
			//I need to check in this way since if I do not do rs.next I am not moving the pointer and I was always getting null instead of real value
			//The idea behind of this is that count is more than 0 means that someone already have that project choose and visible
			if(rs.next()) {
				if(rs.getInt("total") != 0) {
					return true;
				}
			}
			if(rs2.next()) {
				if(rs2.getInt("totalapproved") != 0) {
					return true;
				}
			}
		} catch (SQLException e) {
		}
		return false;
	}

	/**
	 * SQL Method that is calling to another SQL method to obtain a list of the projects of the actual year only
	 * @return
	 */
	public List<Project> getAllProjectActualYear() {
		return getProjectsByYear(actualYear);
	}
	/**
	 * SQL Method that is calling to another SQL method to obtain a list of the projects of the next year only
	 * @return
	 */
	public List<Project> getAllProjectNextYear() {
		return getProjectsByYear(actualYear+1);
	}
	
	/**
	 * Method to add a new student to the DB, the data is coming from the CSV files that had been upload in the method
	 * addNewStudentToDBFromCSV
	 * @param user
	 * @return
	 */
	public int addStudentToDB(User user) {
		String queryInsert = " INSERT INTO user (name, username, password, email, userType, year) VALUES (?, ?, ?, ?, ?, ?)";
		PreparedStatement preparedStmt;
		try {
			preparedStmt = newConnection.prepareStatement(queryInsert);
			preparedStmt.setString(1, user.getUsername());
			preparedStmt.setString(2, user.getUsername());
			preparedStmt.setString(3, user.getPassword());
			preparedStmt.setString(4, user.getEmail());
			preparedStmt.setInt(5, user.getUserType());
			preparedStmt.setInt(6, user.getYear());
			preparedStmt.execute();
			preparedStmt.close();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * SQL Method that check if the user exist in the DB, and return true or false based on that
	 * @param user
	 * @return
	 */
	public boolean checkIfUserExistInDB(User user) {
		String query = " SELECT * FROM user WHERE year = ? AND username = ? "
				+ "AND email = ? AND userType = ?";
		try {
			PreparedStatement ps = newConnection.prepareStatement(query);
			ps.setInt (1, user.getYear());
			ps.setString (2, user.getUsername());
			ps.setString (3, user.getEmail());
			ps.setInt (4, user.getUserType());
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				return true;
			}else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * SQL Method to save the edit data of the user into the DB, will return 0 if OK, 1 if not saved and 2 if SQL error
	 * @param student
	 * @return
	 */
	public int saveEditStudent(User student) {
		try {
			PreparedStatement ps = newConnection.prepareStatement(
					"SELECT * FROM user WHERE userID = ? FOR UPDATE");
			ps.setInt(1,student.getUserID());
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				PreparedStatement ps2 = newConnection.prepareStatement(
						"UPDATE user SET userID = ?, name = ?, username = ?, password = ?, email = ?, userType = ? , year = ?"
								+ " WHERE userID = ?");
				ps2.setInt (1, student.getUserID());
				ps2.setString (2, student.getUsername());
				ps2.setString (3, student.getUsername());
				ps2.setString (4, student.getPassword());
				ps2.setString (5, student.getEmail());
				ps2.setInt (6, student.getUserType());
				ps2.setInt (7, student.getYear());
				ps2.setInt (8, student.getUserID());
				ps2.executeUpdate();
				ps2.close();
				ps.close();
				rs.close();
				return 0;
			}
		} catch (SQLException e) {
			return 2;
		}
		return 1;
	}
	
	/**
	 * SQL method to get a list of all the years that I have in the DB for students
	 * @return
	 */
	public List<Integer> getAllYearOfStudents() {
		List<Integer> yearList = new ArrayList<Integer>();

		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT DISTINCT year FROM user");
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				yearList.add(rs.getInt("year"));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			return yearList;
		}
		return yearList;
	}
	
	public int getTotalNumberByTechnology(String technology) {
		try {
			PreparedStatement ps = newConnection.prepareStatement(
					"SELECT COUNT( DISTINCT topic) AS technologyCount FROM project");
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				return rs.getInt("technologyCount");
			}
		} catch (SQLException e) {
			return 0;
		}
		return 0;
	}
}