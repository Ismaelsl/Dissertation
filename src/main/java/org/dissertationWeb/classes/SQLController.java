package org.dissertationWeb.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SQLController {
	private Connection newConnection;

	public SQLController() {
		DBConnection connect = new DBConnection();
		newConnection = connect.connect();
	}

	/**
	 * Method to check within the DB the login information
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
	 * Method to control the SQL when the DC approve a project that a lecturer send before
	 * @param projectID
	 * @return
	 */
	public int approveProject(int projectID) {
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT * FROM project WHERE projectID = ? FOR UPDATE");
			ps.setInt(1,projectID);
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
		} catch (SQLException e) {
			return 0;
		}
		return 0;
	}

	public boolean saveEdit(Project project) {
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
				ps2.setString(3,project.getTopics());
				ps2.setString(4,project.getDescription());
				ps2.setString(5,project.getCompulsoryReading());
				ps2.setInt(6,rs.getInt("projectID"));
				ps2.executeUpdate();
				ps2.close();
				//newConnection.commit();
			}
			rs.close();
			ps.close();
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	public boolean save(Project project, int userID) {
		String query = " insert into project (year, title, topic, compulsoryreading, description, lecturerID,"
				+ "visible, documentID, waitingtobeapproved, checklistID, visitcounter)"
				+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement preparedStmt = newConnection.prepareStatement(query);
			preparedStmt.setInt (1, project.getYear());
			preparedStmt.setString (2, project.getTitle());
			preparedStmt.setString (3, project.getTopics().toString());
			preparedStmt.setString (4, project.getCompulsoryReading().toString());
			preparedStmt.setString (5, project.getDescription());
			preparedStmt.setInt (6, userID);
			preparedStmt.setBoolean (7, true);
			preparedStmt.setInt (8, 1);
			preparedStmt.setBoolean (9, project.isWaitingToBeApproved());
			preparedStmt.setInt (10, 1);
			preparedStmt.setInt (11, 0);
			preparedStmt.execute();
			preparedStmt.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<Project> search(String lecturer, String technology, String title, String searchValue) {
		PreparedStatement ps;
		List<Project> projectList = new ArrayList<Project>();
		try {
			ps = newConnection.prepareStatement(
					"SELECT DISTINCT project.* FROM project WHERE NOT EXISTS "
							+ "(SELECT * FROM interestproject WHERE project.projectID = interestproject.projectID) "
							+ "AND visible = ? AND waitingtobeapproved = ?");
			ps.setBoolean(1,true);
			ps.setBoolean(2,true);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			searchValue = searchValue.toLowerCase();
			//TODO need to refactor the code for adding project, is repeating too much
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

	public boolean saveCheckList(CheckList checklist) {
		String query = " INSERT INTO checklist (date, eventname, place, description, visible)"
				+ " VALUES (?, ?, ?, ?, ?)";
		try {
			PreparedStatement preparedStmt = newConnection.prepareStatement(query);
			preparedStmt.setString (1, checklist.getDate());
			preparedStmt.setString (2, checklist.getEventName());
			preparedStmt.setString (3, checklist.getPlace());
			preparedStmt.setString (4, checklist.getDescription());
			preparedStmt.setBoolean (5, true);
			preparedStmt.execute();
			preparedStmt.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return false;
	}

	public boolean saveEditCheckList(CheckList checklist) {
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT * FROM checklist WHERE checklistID = ? FOR UPDATE");
			ps.setInt(1,checklist.getCheckListID());
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				PreparedStatement ps2 = newConnection.prepareStatement(
						"UPDATE checklist SET date = ?, eventname = ?, place = ?, description = ?, visible = ? "
								+ "WHERE checklistID = ?");
				ps2.setString (1, checklist.getDate());
				ps2.setString (2, checklist.getEventName());
				ps2.setString (3, checklist.getPlace());
				ps2.setString (4, checklist.getDescription());
				ps2.setBoolean (5, true);
				ps2.setInt(6,rs.getInt("checklistID"));
				ps2.executeUpdate();
				ps2.close();
				ps.close();
				rs.close();
				return true;
				//newConnection.commit();
			}
		} catch (SQLException e) {
			return false;
		}
		return false;

	}

	public int registerInterest(int userID, int projectID) {
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT COUNT(*) AS total FROM interestproject WHERE userID = ? AND visible = ?");
			ps.setInt(1,userID);
			ps.setBoolean(2, true);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				if(rs.getInt("total") == 5) {
					return 5;
				}
				else {
					//Need to do a second query since the first one I am only getting the count and no the actual table data
					System.out.println("This student have " + rs.getInt("total") + " projects already");
					String queryProject ="SELECT * FROM interestproject";
					Statement stProject = newConnection.createStatement();
					ResultSet rsProject = stProject.executeQuery(queryProject);
					while(rsProject.next()) {
						//This is taking care that if the student is already register for that project, cannot register again 
						if(rsProject.getInt("projectID") == projectID && rsProject.getInt("userID") == 
								userID) return 2;
					}
					rsProject.close();
					stProject.close();
					//If the project is not already on the table and the student has not more than 5 projects already then we will add
					//the new project to the table
					String queryInsert = " INSERT INTO interestproject (userID, projectID, visible) VALUES (?, ?, ?)";
					PreparedStatement preparedStmt = newConnection.prepareStatement(queryInsert);
					preparedStmt.setInt (1, userID);
					preparedStmt.setInt (2, projectID);
					preparedStmt.setBoolean (3, true);//make the interest visible
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

	public int getTotalInterestProject(int userID) {
		try {
			PreparedStatement ps = newConnection.prepareStatement(
					"SELECT COUNT(*) AS total FROM interestproject WHERE userID = ? AND visible = ?");
			ps.setInt(1,userID);
			ps.setBoolean(2, true);
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

	public boolean approveInteret(int projectID, User user) {
		Project project = new Project();
		project = project.getProject(projectID);
		//getting user based on the userID, so I can have access to all his data
		user = getUser(user.getUserID());
		String queryInsert = " INSERT INTO approvedproject (userID, projectID, visible) VALUES (?, ?, ?)";
		PreparedStatement preparedStmt;
		try {
			preparedStmt = newConnection.prepareStatement(queryInsert);
			preparedStmt.setInt (1, user.getUserID());
			preparedStmt.setInt (2, projectID);
			preparedStmt.setBoolean(3, true);
			preparedStmt.execute();
			preparedStmt.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

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
				rs.close();
				ps.close();
				return user;
			}
		} catch (SQLException e) {
			return null;
		}		
		return null;
	}
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
		String query = "SELECT * FROM project";
		Statement st;
		List<Project> projectList = new ArrayList<Project>();
		try {
			st = newConnection.createStatement();
			ResultSet rs = st.executeQuery(query);

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
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return projectList;

	}
	public List<Project> getProjectListVisible(boolean status) {
		String query = "SELECT * FROM project";
		Statement st;
		List<Project> projectList = new ArrayList<Project>();
		try {
			st = newConnection.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next())
			{
				//if the project is not approved, then you wont show the project
				if(rs.getBoolean("visible") == status) {
					Project project = new Project();
					//if(rs.getInt("projectID") == 33) System.out.println("Here I am!");
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
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return projectList;

	}

	public List<Project> getProjectListVisibleAnDApprove(boolean statusApproved, boolean statusVisible) {
		//This query it is only showing projects that had not been already choose by another student, in other words
		//get projects which projectID are not in the interestproject table and that are visible
		//I am taking care of the visibility since I need to worry if a student remove the interest of a project then that project
		//should be back to the list
		PreparedStatement ps;
		List<Project> projectList = new ArrayList<Project>();

		try {
			ps = newConnection.prepareStatement(
					"SELECT DISTINCT project.* FROM project WHERE NOT EXISTS "
							+ "(SELECT * FROM interestproject WHERE project.projectID = interestproject.projectID AND visible = ?)"
							+ "AND NOT EXISTS (SELECT * FROM approvedproject WHERE project.projectID = approvedproject.projectID AND visible = ?)");
			ps.setBoolean(1,true);
			ps.setBoolean(2,true);
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
	public List<CheckList> getCheckListList(boolean status) {
		String query = "SELECT * FROM checklist";
		Statement st;
		List<CheckList> checklistList = new ArrayList<CheckList>();
		try {
			st = newConnection.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next())
			{
				CheckList checklist = new CheckList();
				if(rs.getBoolean("visible") == status) {//only if the checklist is visible will be show
					checklist.setCheckListID(rs.getInt("checklistID"));
					checklist.setDate(rs.getString("date"));
					checklist.setEventName(rs.getString("eventname"));
					checklist.setPlace(rs.getString("place"));
					checklist.setDescription(rs.getString("description"));
					checklistList.add(checklist);
				}
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return checklistList;
	}

	public List<Project> getProjectInterestedListByStudent(boolean status, int userLoginID) {
		List<Project> projectList = new ArrayList<Project>();
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT * FROM interestproject WHERE userID = ?");
			ps.setInt(1,userLoginID);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				if(rs.getBoolean("visible") == status) {
					Project project = new Project();
					project = project.getProject(rs.getInt("projectID"));
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
	public List<Project> getProjectInterestedList() {
		String query = "SELECT * FROM interestproject";
		Statement st;
		List<Project> projectList = new ArrayList<Project>();
		try {
			st = newConnection.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next())
			{
				Project project = new Project();
				project = project.getProject(rs.getInt("projectID"));
				User student = new User();
				student = getUser(rs.getInt("userID"));
				project.setStudent(student);
				projectList.add(project);
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return projectList;
	}

	public List<Project> getAllProjectByLecturer(int userLoginID) {
		List<Project> projectList = new ArrayList<Project>();
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT * FROM project WHERE lecturerID =  ?");
			ps.setInt(1,userLoginID);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				Project project = new Project();
				project = project.getProject(rs.getInt("projectID"));
				projectList.add(project);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			return projectList;
		}		
		return projectList;
	}

	public List<User> getAllUserswithInterest() {
		String query = "SELECT * FROM interestproject";
		Statement st;
		List<User> userList = new ArrayList<User>();
		try {
			st = newConnection.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next())
			{
				User user = new User();
				user = getUser(rs.getInt("userID"));
				userList.add(user);
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
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
					"SELECT * from project,interestproject WHERE interestproject.projectID = project.projectID "
							+ " AND lecturerID = ? AND interestproject.visible = ?;");
			ps.setInt(1,userLoginID);
			ps.setBoolean(2, true);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				Project project = new Project();
				project = project.getProject(rs.getInt("projectID"));
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

	public Project getFinalProjectStudent(int userID) {
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT * FROM approvedproject WHERE userID = ? AND visible = ?");
			ps.setInt(1,userID);
			ps.setBoolean(2, true);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Project project = new Project();
				project = project.getProject(rs.getInt("projectID"));
				User user = getUser(userID);
				project.setVisible(rs.getBoolean("visible"));//setting visible 
				project.setUser(user);
				return project;
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			return null;
		}			
		return null;
	}

	public List<User> getAllStudentList() {
		List<User> userList = new ArrayList<User>();
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT * from user WHERE user.userType = ?");
			ps.setInt(1,2);
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
	public void updateProject(int projectID, boolean status) {
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT * FROM project WHERE projectID =  ? FOR UPDATE");
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
	public void updateInterestProject(int projectID, int userID, boolean status) {
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT * FROM interestproject WHERE projectID =  ? AND userID = ? FOR UPDATE");
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

	public void updateInterestFinalProject(int projectID, int userID, boolean status) {
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT * FROM approvedproject WHERE projectID =  ? AND userID = ? FOR UPDATE");
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

	public void updateListOfProjectAfterApproveInterest(int userID) {
		PreparedStatement ps;
		try {
			ps = newConnection.prepareStatement(
					"SELECT * FROM interestproject WHERE userID = ? FOR UPDATE");
			ps.setInt(1,userID);
			ps.getResultSet();
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				//if(rs.getInt("projectID") == projectID)continue;//if is our project, then keep it visible
				PreparedStatement ps2 = newConnection.prepareStatement(
						"UPDATE interestproject SET visible = ? WHERE userID = ?");
				ps2.setBoolean(1, false);
				ps2.setInt(2, userID);
				ps2.execute();
				ps2.close();
				//newConnection.commit();
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
		}
	}

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
				project = project.getProject(rs.getInt("projectID"));
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

	public List<Integer> getAllYearOfProjects() {
		String query ="SELECT Distinct year FROM project";
		Statement st;
		List<Integer> yearList = new ArrayList<Integer>();
		try {
			st = newConnection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				yearList.add(rs.getInt("year"));
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return yearList;
	}
}