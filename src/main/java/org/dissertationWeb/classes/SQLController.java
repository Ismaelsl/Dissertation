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
	 * @return the userID to probe that the login was succesfull, if userID is 0 means that login fails
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
		String query = "SELECT * FROM user WHERE userType = 3";
		Statement st;
		try {
			st = newConnection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next())
			{
				return rs.getInt("userID");
			}
			rs.close();
			st.close();
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
		String query ="SELECT * FROM project WHERE projectID = '" + projectID + "' FOR UPDATE;";
		Statement st;
		try {
			st = newConnection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				PreparedStatement ps = newConnection.prepareStatement(
						"UPDATE project SET waitingtobeapproved = ? WHERE projectID = ?");
				ps.setBoolean(1,true);
				ps.setInt(2,rs.getInt("projectID"));
				ps.executeUpdate();
				ps.close();
				return rs.getInt("projectID");
				
			}
			rs.close();
			st.close();
			//newConnection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public boolean saveEdit(Project project) {
		//Using the "For update" method I am locking the project till I close the statement
				String query ="SELECT * FROM project WHERE projectID = '" + project.getProjectID() + "' FOR UPDATE;";
				Statement st;
				try {
					st = newConnection.createStatement();
					ResultSet rs = st.executeQuery(query);
					while(rs.next()) {
						PreparedStatement ps = newConnection.prepareStatement(
								"UPDATE project SET year = ?, title = ?, topic = ?, description = ?, compulsoryreading = ? WHERE projectID = ?");
						ps.setInt(1,project.getYear());
						ps.setString(2,project.getTitle());
						ps.setString(3,project.getTopics());
						ps.setString(4,project.getDescription());
						ps.setString(5,project.getCompulsoryReading());
						ps.setInt(6,rs.getInt("projectID"));
						ps.executeUpdate();
						ps.close();
						//newConnection.commit();
					}
					rs.close();
					st.close();
					return true;
				} catch (SQLException e) {
					e.printStackTrace();
				}
		return false;
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
		String query = "SELECT DISTINCT project.* FROM project WHERE NOT EXISTS "
				+ "(SELECT * FROM interestproject WHERE project.projectID = interestproject.projectID) "
				+ "AND visible = true AND waitingtobeapproved = true";
		Statement st;
		List<Project> projectList = new ArrayList<Project>();
		try {
			st = newConnection.createStatement();
			ResultSet rs = st.executeQuery(query);

			//TODO need to refactor the code for adding project, is repeating too much
			while (rs.next())
			{
				//if(rs.getBoolean("waitingtobeapproved") == false) continue; //if the project is not approved, then added to the view
				Project project = new Project();
				if(technology != null) {
					if(rs.getString("topic").contains(searchValue)) {
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
					if(rs.getString("title").contains(searchValue)) {
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
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return projectList;
	}
	
	public boolean saveCheckList(CheckList checklist) {
		String query = " insert into checklist (date, eventname, place, description, visible)"
				+ " values (?, ?, ?, ?, ?)";
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
		String query ="SELECT * FROM checklist WHERE checklistID = '" + checklist.getCheckListID() + "' FOR UPDATE;";
		Statement st;
		try {
			st = newConnection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				PreparedStatement ps = newConnection.prepareStatement(
						"UPDATE checklist SET date = ?, eventname = ?, place = ?, description = ?, visible = ? "
								+ "WHERE checklistID = ?");
				ps.setString (1, checklist.getDate());
				ps.setString (2, checklist.getEventName());
				ps.setString (3, checklist.getPlace());
				ps.setString (4, checklist.getDescription());
				ps.setBoolean (5, true);
				ps.setInt(6,rs.getInt("checklistID"));
				ps.executeUpdate();
				ps.close();
				//newConnection.commit();
			}
			rs.close();
			st.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public int registerInterest(int userID, int projectID) {
		String query ="SELECT COUNT(*) as total FROM interestproject WHERE userID = '"+   userID + "' "
				+ "AND visible = true; ";
		Statement stCount;
		try {
			stCount = newConnection.createStatement();
			ResultSet rs = stCount.executeQuery(query);
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
					String queryInsert = " insert into interestproject (userID, projectID, visible) values (?, ?, ?)";
					PreparedStatement preparedStmt = newConnection.prepareStatement(queryInsert);
					preparedStmt.setInt (1, userID);
					preparedStmt.setInt (2, projectID);
					preparedStmt.setBoolean (3, true);//make the interest visible
					preparedStmt.execute();
					preparedStmt.close();
				}	
				rs.close();
				return 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}
	
	public int getTotalInterestProject(int userID) {
		String query ="SELECT COUNT(*) as total FROM interestproject WHERE userID = '"+   userID + "' "
				+ "AND visible = true; ";
		Statement stCount;
		try {
			stCount = newConnection.createStatement();
			ResultSet rs = stCount.executeQuery(query);
			if(rs.next()) {
				return rs.getInt("total");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public boolean approveInteret(int projectID, User user) {
		Project project = new Project();
		project = project.getProject(projectID);
		//getting user based on the userID, so I can have access to all his data
		user = getUser(user.getUserID());
		String queryInsert = " insert into approvedproject (userID, projectID, visible) values (?, ?, ?)";
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
		String query = "SELECT * FROM user WHERE userID = " + "'"+ userID + "';";
		Statement st;
		try {
			st = newConnection.createStatement();
			ResultSet rs = st.executeQuery(query);
			User user = new User();
			while (rs.next())
			{
				user.setUserID(rs.getInt("userID"));
				user.setEmail(rs.getString("email"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setUserType(rs.getInt("userType"));
				return user;
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public User getUserByName(String username) {
		String query = "SELECT * FROM user WHERE username = " + "'" + username + "';";
		Statement st;
		User user = new User();
		try {
			st = newConnection.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next())
			{
				user.setUserID(rs.getInt("userID"));
				user.setEmail(rs.getString("email"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				return user;
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
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
		String query = "SELECT DISTINCT project.* FROM project WHERE NOT EXISTS "
				+ "(SELECT * FROM interestproject WHERE project.projectID = interestproject.projectID AND visible = true)";
		Statement st;
		List<Project> projectList = new ArrayList<Project>();
		try {
			st = newConnection.createStatement();
			ResultSet rs = st.executeQuery(query);

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
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
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
		String query = "SELECT * FROM interestproject WHERE userID = '" + userLoginID + "';";
		Statement st;
		List<Project> projectList = new ArrayList<Project>();
		try {
			st = newConnection.createStatement();
			ResultSet rs = st.executeQuery(query);

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
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
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
		String query = "SELECT * FROM project WHERE lecturerID = '" + userLoginID + "';";
		Statement st;
		List<Project> projectList = new ArrayList<Project>();
		try {
			st = newConnection.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next())
			{
				Project project = new Project();
				project = project.getProject(rs.getInt("projectID"));
				projectList.add(project);
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
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

	public List<Project> getLecturerProjectList(int userLoginID) {
		//Since I am only accepting project that their visibility is true, I am controlling that if another lecturer approve
		//a student all the student list will stop show the interest of that student
		//The student interest will still be keep on the DB but hidden
		String query = "SELECT * from project,interestproject WHERE interestproject.projectID = project.projectID "
				+ " AND lecturerID = '" + userLoginID + "' AND interestproject.visible = true;";
		Statement st;
		List<Project> projectList = new ArrayList<Project>();
		try {
			st = newConnection.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next())
			{
				Project project = new Project();
				project = project.getProject(rs.getInt("projectID"));
				//Here I am getting the student who applied to this project
				User student = getUser(rs.getInt("userID"));
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

	public Project getFinalProjectStudent(int userID) {
		String query ="SELECT * FROM approvedproject WHERE userID =  '" + userID + "' AND visible = true;";
		Statement st;
		try {
			st = newConnection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				Project project = new Project();
				project = project.getProject(rs.getInt("projectID"));
				User user = getUser(userID);
				project.setVisible(rs.getBoolean("visible"));//setting visible 
				project.setUser(user);
				return project;
			}
			rs.close();
			st.close();
			//newConnection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<User> getAllStudentList() {
		String query = "SELECT * from user WHERE user.userType = 2;";
		Statement st;
		List<User> userList = new ArrayList<User>();
		try {
			st = newConnection.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next())
			{
				User student = getUser(rs.getInt("userID"));
				userList.add(student);
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userList;
	}

	/**
	 * This method is taking care that when a lecturer enter to his personal list you will have in the top 
	 * all the lecturer projects without interest and on the bottom the lecturer project which have interest
	 * @param projectList
	 * @param projectWithInterest
	 * @return
	 */
	public List<Project> listComparer(List<Project>projectList, List<Project>projectWithInterest) {
		List<Project> finalList = new ArrayList<Project>();
		for (Project projectLect : projectList) {
			boolean found=false;
			for (Project projectInte : projectWithInterest) {
				if (projectLect.getProjectID() == projectInte.getProjectID()) {
					found=true;
					break;
				}
			}
			if(!found){
				finalList .add(projectLect);
			}
		}
		return finalList;
	}
	public void updateChecklist(int checklistID, boolean status) {
		String query ="SELECT * FROM checklist WHERE checklistID = '" + checklistID + "' FOR UPDATE;";
		Statement st;
		try {
			st = newConnection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				PreparedStatement ps = newConnection.prepareStatement(
						"UPDATE checklist SET visible = ? WHERE checklistID = ?");
				ps.setBoolean(1, status);
				ps.setInt(2, checklistID);
				ps.execute();
				ps.close();
				//newConnection.commit();
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void updateProject(int projectID, boolean status) {
		String query ="SELECT * FROM project WHERE projectID = '" + projectID + "' FOR UPDATE;";
		Statement st;
		try {
			st = newConnection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				//synchronized (rs) {
				PreparedStatement ps = newConnection.prepareStatement(
						"UPDATE project SET visible = ? WHERE projectID = ?");
				ps.setBoolean(1, status);
				ps.setInt(2, projectID);
				//newConnection.commit();
				ps.execute();
				ps.close();

				//}		
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void updateInterestProject(int projectID, int userID, boolean status) {
		String query ="SELECT * FROM interestproject WHERE projectID = '" + projectID + 
				"' AND userID =  '" + userID + "'FOR UPDATE;";
		Statement st;
		try {
			st = newConnection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				PreparedStatement ps = newConnection.prepareStatement(
						"UPDATE interestproject SET visible = ? WHERE projectID = ? AND userID = ?");
				ps.setBoolean(1, status);
				ps.setInt(2, projectID);
				ps.setInt(3, userID);
				ps.execute();
				ps.close();
				//newConnection.commit();
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void updateInterestFinalProject(int projectID, int userID, boolean status) {
		String query ="SELECT * FROM approvedproject WHERE projectID = '" + projectID + 
				"' AND userID =  '" + userID + "'FOR UPDATE;";

		System.out.println("MEH! and project ID is " + projectID  + " and userID is " + userID);
		Statement st;
		try {
			st = newConnection.createStatement();
			ResultSet rs = st.executeQuery(query);
			System.out.println("MEH2!");
			while(rs.next()) {
				PreparedStatement ps = newConnection.prepareStatement(
						"UPDATE approvedproject SET visible = ? WHERE projectID = ? AND userID = ?");
				ps.setBoolean(1, status);
				ps.setInt(2, projectID);
				ps.setInt(3, userID);
				ps.execute();
				System.out.println("PS had been executed");
				ps.close();
				//newConnection.commit();
			}
			System.out.println("MEH3!");
			rs.close();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void updateListOfProjectAfterApproveInterest(int userID) {
		String query ="SELECT * FROM interestproject WHERE userID =  '" + userID + "'FOR UPDATE;";
		Statement st;
		try {
			st = newConnection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				//if(rs.getInt("projectID") == projectID)continue;//if is our project, then keep it visible
				PreparedStatement ps = newConnection.prepareStatement(
						"UPDATE interestproject SET visible = ? WHERE userID = ?");
				ps.setBoolean(1, false);
				ps.setInt(2, userID);
				ps.execute();
				ps.close();
				//newConnection.commit();
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
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

	public List<Project> getProjectsByYear(int year) {
		String query ="SELECT * FROM project WHERE year =  '" + year + "';";
		Statement st;
		List<Project> projectList = new ArrayList<Project>();
		try {
			st = newConnection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				Project project = new Project();
				project = project.getProject(rs.getInt("projectID"));
				project.setVisible(rs.getBoolean("visible"));//setting visible 
				projectList.add(project);

			}
			rs.close();
			st.close();
			return projectList;
			//newConnection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}


}
