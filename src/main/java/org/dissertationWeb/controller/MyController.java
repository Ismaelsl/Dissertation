package org.dissertationWeb.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.dissertationWeb.classes.CheckList;
import org.dissertationWeb.classes.DBConnection;
import org.dissertationWeb.classes.Document;
import org.dissertationWeb.classes.Lecturer;
import org.dissertationWeb.classes.Project;
import org.dissertationWeb.classes.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MyController {
	private Connection newConnection;
	private int userLoginID;

	private void startDBConnection() {
		//Create a connection to the DB as soon as we need it
		DBConnection connect = new DBConnection();
		newConnection = connect.connect();
	}

	@RequestMapping(value = { "/", "/home" }, method = RequestMethod.GET)
	public ModelAndView homePage() {
		//redirect to login page if you are not login
		if(userLoginID == 0) return login();
		if(newConnection == null) startDBConnection();
		return new ModelAndView("homePage");
	}

	@RequestMapping(value = "/login" , method = RequestMethod.GET)
	public ModelAndView login() {
		//redirect to home page if you are login and try to login again
		if(userLoginID != 0) return homePage();
		if(newConnection == null) startDBConnection();
		return new ModelAndView("loginPage","command",new User());
	}

	@RequestMapping(value="/logincheck",method = RequestMethod.POST)  
	public ModelAndView checkLogin(@ModelAttribute("user")User user, ModelMap model){ 
		//redirect to home page if you are login and try to login again
		if(userLoginID != 0) return homePage();
		System.out.println("Username " + user.getUsername() + " password " + user.getPassword());
		String query = "SELECT * FROM user";
		Statement st;
		try {
			st = newConnection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next())
			{
				if(rs.getString("username").equals(user.getUsername()) 
						&& rs.getString("password").equals(user.getPassword())) {

					userLoginID = rs.getInt("userID");
					System.out.println("Yeah! " + userLoginID);
					return new ModelAndView("homePage");
					//return "homePage";
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ModelAndView("loginPage");
		//return "loginPage";
	}

	@RequestMapping(value = { "/contactus" }, method = RequestMethod.GET)
	public ModelAndView contactusPage(Model model) throws SQLException {
		if(newConnection == null) startDBConnection();
		String query = "SELECT * FROM lecturer";
		Statement st = newConnection.createStatement();
		ResultSet rs = st.executeQuery(query);
		while (rs.next())
		{
			model.addAttribute("id", rs.getInt("LecturerID"));
			model.addAttribute("name", rs.getString("Name"));
			model.addAttribute("department", rs.getString("Department"));
			model.addAttribute("email", rs.getString("Email"));
		}
		return new ModelAndView("contactusPage");
		//return "contactusPage";
	}
	@RequestMapping(value = { "/projectlist" }, method = RequestMethod.GET)
	public ModelAndView projectListPage(Model model) throws SQLException {
		//redirect to login page if you are not login
		if(userLoginID == 0) return login();
		if(newConnection == null) startDBConnection();
		String query = "SELECT * FROM project";
		Statement st = newConnection.createStatement();
		ResultSet rs = st.executeQuery(query);
		List<Project> projectList = new ArrayList<Project>();
		while (rs.next())
		{
			if(rs.getBoolean("waitingtobeapproved") == false) continue; //if the project is not approved, then you wont show the project
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

		System.out.println("List size " + projectList.size());
		User user = getUser(userLoginID);
		model.addAttribute("userType", user.getUserType());
		System.out.println("user type " + user.getUserType());
		return new ModelAndView("projectListPage","projectList",projectList);  
	}

	@RequestMapping(value = { "/projectlisttoapprove" }, method = RequestMethod.GET)
	public ModelAndView projectListToApprovePage(Model model) throws SQLException {
		//redirect to login page if you are not login
		if(userLoginID == 0) return login();
		if(newConnection == null) startDBConnection();
		String query = "SELECT * FROM project";
		Statement st = newConnection.createStatement();
		ResultSet rs = st.executeQuery(query);
		List<Project> projectList = new ArrayList<Project>();
		while (rs.next())
		{
			if(rs.getBoolean("waitingtobeapproved") == true) continue; //if the project is not approved, then added to the view
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

		System.out.println("List size " + projectList.size());
		User user = getUser(userLoginID);
		model.addAttribute("userType", user.getUserType());
		return new ModelAndView("projectListtoapprovePage","projectList",projectList);  
	}

	@RequestMapping( value="/approveproject",method = RequestMethod.POST)
	public ModelAndView approveprojectPage(@RequestParam(value="projectID") int projectID, Model model) throws SQLException { 
		//redirect to login page if you are not login
		if(userLoginID == 0) return login();

		//first I update the project
		PreparedStatement ps = newConnection.prepareStatement(
				"UPDATE project SET waitingtobeapproved = ? WHERE projectID = ?");
		ps.setBoolean(1,true);
		ps.setInt(2,projectID);
		ps.executeUpdate();
		ps.close();
		
		//Then I populate the view with the project itself
		Project project = new Project();
		project = project.getProject(projectID);

		model.addAttribute("year",project.getYear());
		model.addAttribute("title",project.getTitle());
		model.addAttribute("topics",project.getTopics());
		model.addAttribute("compulsoryReading",project.getCompulsoryReading());
		model.addAttribute("description",project.getDescription());
		model.addAttribute("visible",false);
		model.addAttribute("documentID",1);
		model.addAttribute("waitingToBeApproved",project.isWaitingToBeApproved());
		model.addAttribute("checklistID",1);
		//Populating user part
		User actualUser = getUser(userLoginID);
		model.addAttribute("lecturerID",userLoginID);
		model.addAttribute("lecturername", actualUser.getUsername());
		model.addAttribute("lectureremail", actualUser.getEmail());
		return new ModelAndView("projectPage","project",model);//will display object data 
	}

	@RequestMapping( "/newproject")
	public ModelAndView newprojectPage(Model model) throws SQLException {  
		//redirect to login page if you are not login
		if(userLoginID == 0) return login();
		return new ModelAndView("newprojectPage","command",new Project());  
	}

	@RequestMapping( value="/edit",method = RequestMethod.POST)
	public ModelAndView editprojectPage(@RequestParam(value="projectID") int projectID) throws SQLException { 
		//redirect to login page if you are not login
		if(userLoginID == 0) return login();
		Project project = new Project();
		System.out.println("test projectID " + projectID);
		project = project.getProject(projectID);
		if(newConnection == null) startDBConnection();
		System.out.println("test description " + project.getDescription());
		//I am populating here the view so the user can modify the project, if I need to add more data to the form I should
		//update the constructor on project class
		return new ModelAndView("editprojectPage","command",new Project(project.getProjectID(),project.getYear(),
				project.getTitle(),project.getTopics(),project.getCompulsoryReading(),project.getDescription()));  
	}

	@RequestMapping( value="/remove",method = RequestMethod.POST)
	public ModelAndView removeprojectPage(@RequestParam(value="projectID") int projectID) throws SQLException { 
		//redirect to login page if you are not login
		if(userLoginID == 0) return login();
		if(newConnection == null) startDBConnection();
		System.out.println("test projectID " + projectID);
		PreparedStatement st = newConnection.prepareStatement("DELETE FROM project WHERE projectID = ?");
		st.setInt(1,projectID);
		st.executeUpdate(); 
		//I am populating here the view so the user can modify the project, if I need to add more data to the form I should
		//update the constructor on project class
		return new ModelAndView("projectRemovedPage");
	}

	@RequestMapping(value="/saveEdit",method = RequestMethod.POST)  
	public ModelAndView saveEditProject(@ModelAttribute("project") Project project, Model model){ 
		//redirect to login page if you are not login
		if(userLoginID == 0) return login();
		System.out.println(project.getProjectID() +" inside edit project page!!");
		if(newConnection == null) startDBConnection();
		model.addAttribute("year",project.getYear());
		model.addAttribute("title",project.getTitle());
		model.addAttribute("topics",project.getTopics());
		model.addAttribute("compulsoryReading",project.getCompulsoryReading());
		model.addAttribute("description",project.getDescription());
		//Populating user part
		User actualUser = getUser(userLoginID);
		model.addAttribute("lecturerID",userLoginID);
		model.addAttribute("lecturername", actualUser.getUsername());
		model.addAttribute("lectureremail", actualUser.getEmail());
		try {
			PreparedStatement ps = newConnection.prepareStatement(
					"UPDATE project SET year = ?, title = ?, topic = ?, compulsoryreading = ? WHERE projectID = ?");
			ps.setInt(1,project.getYear());
			ps.setString(2,project.getTitle());
			ps.setString(3,project.getTopics());
			ps.setString(4,project.getCompulsoryReading());
			ps.setInt(5,project.getProjectID());
			ps.executeUpdate();
			ps.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ModelAndView("projectPage","project",model);//will display object data  
	}

	@RequestMapping(value="/save",method = RequestMethod.POST)  
	public ModelAndView save(@ModelAttribute("project") Project project, Model model){  
		//redirect to login page if you are not login
		if(userLoginID == 0) return login();
		if(newConnection == null) startDBConnection();
		//write code to save project object  
		//here, we are displaying project object to prove project has data  
		System.out.println(project.getTitle()+" "+project.getDescription()); 
		//Lecturer lecturer = new Lecturer("John", "CS", "test@test.com", 0);

		//I need to use model to populate the projectPage, using the project object itself, does not works
		model.addAttribute("year",project.getYear());
		model.addAttribute("title",project.getTitle());
		model.addAttribute("topics",project.getTopics());
		model.addAttribute("compulsoryReading",project.getCompulsoryReading());
		model.addAttribute("description",project.getDescription());
		model.addAttribute("visible",false);
		model.addAttribute("documentID",1);
		model.addAttribute("waitingToBeApproved",false);
		model.addAttribute("checklistID",1);
		//Populating user part
		User actualUser = getUser(userLoginID);
		model.addAttribute("lecturerID",userLoginID);
		model.addAttribute("lecturername", actualUser.getUsername());
		model.addAttribute("lectureremail", actualUser.getEmail());
		//model.addAttribute("projectID",project.getTitle());
		String query = " insert into project (year, title, topic, compulsoryreading, description, lecturerID,"
				+ "visible, documentID, waitingtobeapproved, checklistID)"
				+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement preparedStmt = newConnection.prepareStatement(query);
			preparedStmt.setInt (1, project.getYear());
			preparedStmt.setString (2, project.getTitle());
			preparedStmt.setString (3, project.getTopics().toString());
			preparedStmt.setString (4, project.getCompulsoryReading().toString());
			preparedStmt.setString (5, project.getDescription());
			preparedStmt.setInt (6, userLoginID);
			preparedStmt.setBoolean (7, project.isVisible());
			preparedStmt.setInt (8, 1);
			preparedStmt.setBoolean (9, project.isWaitingToBeApproved());
			preparedStmt.setInt (10, 1);
			preparedStmt.execute();
			if(getLastProjectID() == 0) return new ModelAndView("errorPage","project",null);
			model.addAttribute("projectID",getLastProjectID());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ModelAndView("projectPage","project",model);//will display object data  
	}  

	/**
	 * If any of the three String variables are not null, that means that that is the value that the user request to look for
	 * @param searchValue
	 * @param lecturer
	 * @param technology
	 * @param title
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/search",method = RequestMethod.POST)  
	public ModelAndView search(@RequestParam String searchValue, @RequestParam(required = false)
	String lecturer, @RequestParam(required = false) String technology, @RequestParam(required = false) String title,
	Model model){ 
		//System.out.println("test search value " + searchValue + " lecturer value " + lecturer + " technology value " + technology);
		searchValue.toLowerCase();//better if I put everything on lower case
		String query = "SELECT * FROM project";
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
				if(technology!=null) {
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
				if(lecturer != null) {
					User user = getUserByName(searchValue);
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ModelAndView("projectListPage","projectList",projectList);
	}

	public int getLastProjectID() throws SQLException {
		if(newConnection == null) startDBConnection();
		String query = "SELECT projectID FROM project ORDER BY projectID DESC LIMIT 1";
		Statement st = newConnection.createStatement();
		ResultSet rs = st.executeQuery(query);
		while (rs.next())
		{
			return(rs.getInt("projectID"));
		}
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}