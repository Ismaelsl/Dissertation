package org.dissertationWeb.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.dissertationWeb.classes.CheckList;
import org.dissertationWeb.classes.DBConnection;
import org.dissertationWeb.classes.Document;
import org.dissertationWeb.classes.Lecturer;
import org.dissertationWeb.classes.Project;
import org.dissertationWeb.classes.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.SessionScope;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MyController {
	@Autowired 
	private HttpSession httpSession;

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
					httpSession.setAttribute("userID", rs.getInt("userID"));
					return new ModelAndView("homePage");
					//return "homePage";
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		model.addAttribute("message", "You are not logged in, please go to login page and enter your credentials");
		return new ModelAndView("errorPage");
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
		List<Project> projectList = getProjectList(false);
		System.out.println("testing sessions " + httpSession.getAttribute("userID"));
		System.out.println("List size " + projectList.size());
		User user = getUser(userLoginID);
		model.addAttribute("userType", user.getUserType());
		System.out.println("user type " + user.getUserType());
		if(projectList.isEmpty()) {
			model.addAttribute("message", "You project list is empty");
			return new ModelAndView("errorPage");
		}
		return new ModelAndView("projectListPage","projectList",projectList);  
	}

	@RequestMapping(value = { "/projectlisttoapprove" }, method = RequestMethod.GET)
	public ModelAndView projectListToApprovePage(Model model) throws SQLException {
		//redirect to login page if you are not login
		if(userLoginID == 0) return login();
		if(newConnection == null) startDBConnection();
		List<Project> projectList = getProjectList(true);
		System.out.println("List size " + projectList.size());
		User user = getUser(userLoginID);
		model.addAttribute("userType", user.getUserType());
		if(projectList.isEmpty()) {
			model.addAttribute("message", "You do not have any project to approve");
			return new ModelAndView("errorPage");
		}
		return new ModelAndView("projectListtoapprovePage","projectList",projectList);  
	}

	@RequestMapping( value="/approveproject",method = RequestMethod.POST)
	public ModelAndView approveprojectPage(@RequestParam(value="projectID") int projectID, Model model) throws SQLException { 
		//redirect to login page if you are not login
		if(userLoginID == 0) return login();

		//first I update the project
		String query ="SELECT * FROM project WHERE projectID = '" + projectID + "' FOR UPDATE;";
		Statement st = newConnection.createStatement();
		ResultSet rs = st.executeQuery(query);
		while(rs.next()) {
			PreparedStatement ps = newConnection.prepareStatement(
					"UPDATE project SET waitingtobeapproved = ? WHERE projectID = ?");
			ps.setBoolean(1,true);
			ps.setInt(2,rs.getInt("projectID"));
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
		}
		st.close();
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
		//Using the "For update" method I am locking the project till I close the statement
		String query ="SELECT * FROM project WHERE projectID = '" + project.getProjectID() + "' FOR UPDATE;";
		Statement st;
		try {
			st = newConnection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				PreparedStatement ps = newConnection.prepareStatement(
						"UPDATE project SET year = ?, title = ?, topic = ?, compulsoryreading = ? WHERE projectID = ?");
				ps.setInt(1,project.getYear());
				ps.setString(2,project.getTitle());
				ps.setString(3,project.getTopics());
				ps.setString(4,project.getCompulsoryReading());
				ps.setInt(5,rs.getInt("projectID"));
				ps.executeUpdate();
				ps.close();
			}
			st.close();
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
				+ "visible, documentID, waitingtobeapproved, checklistID, visitcounter)"
				+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
			preparedStmt.setInt (11, 0);
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
					if(user == null) {
						model.addAttribute("message", "You search result for lecturer is empty");
						return new ModelAndView("errorPage");
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(projectList.isEmpty()) {
			model.addAttribute("message", "Your search criteria does not return any result please try something else");
			return new ModelAndView("errorPage");
		}
		return new ModelAndView("projectListPage","projectList",projectList);
	}

	@RequestMapping( "/newchecklist")
	public ModelAndView newchecklistPage(Model model) throws SQLException {  
		//redirect to login page if you are not login
		if(userLoginID == 0) return login();
		return new ModelAndView("checklistPage","command",new CheckList());  
	}

	@RequestMapping(value="/savechecklist",method = RequestMethod.POST)  
	public ModelAndView saveChecklist(@ModelAttribute("checklist") CheckList checklist, Model model){  
		//redirect to login page if you are not login
		if(userLoginID == 0) return login();
		System.out.println("Checklist date: " + checklist.getDate() + " Checklist name: " + checklist.getEventName());

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
			model.addAttribute("date", checklist.getDate());
			model.addAttribute("eventname", checklist.getEventName());
			model.addAttribute("place", checklist.getPlace());
			model.addAttribute("description", checklist.getDescription());
			if(getLastChecklistID() == 0) {
				model.addAttribute("message", "Error happens saving the checklist");
				return new ModelAndView("errorPage");
			}
			//System.out.println("testing ID "  + getLastChecklistID());
			model.addAttribute("checklistID",getLastChecklistID());
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		//Need to change this redirect to a checklistPage to see the checklist that we just added
		return new ModelAndView("checklistViewPage","checklist",model);//will display object data  
	}

	@RequestMapping(value = { "/checklistlist" }, method = RequestMethod.GET)
	public ModelAndView checklistListPage(Model model) throws SQLException {
		//redirect to login page if you are not login
		if(userLoginID == 0) return login();
		if(newConnection == null) startDBConnection();
		List<CheckList> checklistList = getCheckListList(true);
		List<CheckList> checklistListNotApproved = getCheckListList(false);
		
		System.out.println("List size " + checklistList.size());
		//Keep this since I need to check if user is admin or not for now
		User user = getUser(userLoginID);
		model.addAttribute("userType", user.getUserType());
		model.addAttribute("checklistListNotApproved", checklistListNotApproved);
		model.addAttribute("notapprovedsize", checklistListNotApproved.size());
		return new ModelAndView("checklistListPage","checklistList",checklistList);  
	}

	@RequestMapping( value="/editChecklist",method = RequestMethod.POST)
	public ModelAndView editChecklistPage(@RequestParam(value="checklistID") int checklistID, Model model) throws SQLException { 
		//redirect to login page if you are not login
		if(userLoginID == 0) return login();
		CheckList checkList = new CheckList();
		System.out.println("test projectID " + checklistID);
		checkList = checkList.getchecklist(checklistID);
		if(newConnection == null) startDBConnection();
		System.out.println("test checklist ID " + checkList.getCheckListID());
		//I am populating here the view so the user can modify the project, if I need to add more data to the form I should
		//update the constructor on project class
		model.addAttribute("checklistID", checklistID); //passing checklistID to the frontend
		return new ModelAndView("editchecklistPage","command",new CheckList(checkList.getCheckListID(), checkList.getDate(),
				checkList.getEventName(), checkList.getPlace(), checkList.getDescription()));  
	}

	@RequestMapping( value="/removeChecklist",method = RequestMethod.POST)
	public ModelAndView removeChecklistPage(@RequestParam(value="checklistID") int checklistID) throws SQLException { 
		//redirect to login page if you are not login
		if(userLoginID == 0) return login();
		if(newConnection == null) startDBConnection();
		System.out.println("test projectID " + checklistID);
		updateChecklist(checklistID,false);
		//I am using same page since the final message for project or checklist is the same
		return new ModelAndView("projectRemovedPage");
	}
	@RequestMapping( value="/makeVisibleChecklist",method = RequestMethod.POST)
	public ModelAndView makeVisibleChecklistPage(@RequestParam(value="checklistID") int checklistID, Model model) 
			throws SQLException { 
		//redirect to login page if you are not login
		if(userLoginID == 0) return login();
		if(newConnection == null) startDBConnection();
		//System.out.println("test projectID " + checklistID);
		updateChecklist(checklistID,true);
		List<CheckList> checklistList = getCheckListList(true);
		List<CheckList> checklistListNotApproved = getCheckListList(false);
		model.addAttribute("checklistList", checklistList);
		model.addAttribute("checklistListNotApproved", checklistListNotApproved);
		model.addAttribute("notapprovedsize", checklistListNotApproved.size());
		//I am using same page since the final message for project or checklist is the same
		return new ModelAndView("checklistListPage");
	}

	@RequestMapping(value="/saveEditChecklist",method = RequestMethod.POST)  
	public ModelAndView saveEditChecklist(@ModelAttribute("checklist") CheckList checklist, Model model){ 
		//redirect to login page if you are not login
		if(userLoginID == 0) return login();
		System.out.println(checklist.getCheckListID() +" inside edit checklist page!!");
		if(newConnection == null) startDBConnection();
		model.addAttribute("date", checklist.getDate());
		model.addAttribute("eventname", checklist.getEventName());
		model.addAttribute("place", checklist.getPlace());
		model.addAttribute("description", checklist.getDescription());
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
			}
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ModelAndView("checklistViewPage","checklist",model);//will display object data  
	}

	/**
	 * I am passing only the projectID since the userID is already save on the session
	 * That is all the data I need to register the interest of a student with a project
	 * @param projectID
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping( value="/registerinterest",method = RequestMethod.POST)
	public ModelAndView registerInterestPage(@RequestParam(value="projectID") int projectID, Model model) throws SQLException { 
		//redirect to login page if you are not login
		if(userLoginID == 0) return login();
		if(newConnection == null) startDBConnection();
		String query ="SELECT COUNT(*) as total FROM interestproject WHERE userID = '"+   userLoginID + "'; ";
		Statement stCount = newConnection.createStatement();
		ResultSet rs = stCount.executeQuery(query);
		if(rs.next()) {
			if(rs.getInt("total") == 5) {
				model.addAttribute("message", "You already have 5 projects registered on your name, please remove one before you add new ones"); 
				return new ModelAndView("errorPage");
			}
			else {
				//Need to do a second query since the first one I am only getting the count and no the actual table data
				System.out.println("This student have " + rs.getInt("total") + " projects already");
				String queryProject ="SELECT * FROM interestproject";
				Statement stProject = newConnection.createStatement();
				ResultSet rsProject = stProject.executeQuery(queryProject);
				while(rsProject.next()) {
					//For now I am returning to home but the final version will be returning to the error page
					//This is taking care that if the student is already register for that project, cannot register again
					if(rsProject.getInt("projectID") == projectID) return new ModelAndView("homePage");
				}
				//If the project is not already on the table and the student has not more than 5 projects already then we will add
				//the new project to the table
				String queryInsert = " insert into interestproject (userID, projectID) values (?, ?)";
				PreparedStatement preparedStmt = newConnection.prepareStatement(queryInsert);
				preparedStmt.setInt (1, userLoginID);
				preparedStmt.setInt (2, projectID);
				preparedStmt.execute();
				preparedStmt.close();
			}		
		}

		//I am using same page since the final message for project or checklist is the same
		List<Project> projectList = getProjectInterestedListByStudent();
		if(projectList.isEmpty()) {
			model.addAttribute("message", "You do not have any project register yet, go to projec list and choose one!");
			return new ModelAndView("errorPage");
		}
		return new ModelAndView("interestProjectListPage","projectList",projectList);
	}

	@RequestMapping( value="/removeinterest",method = RequestMethod.POST)
	//TODO I need to take care than if the user is not register to that project then he cannot remove interest
	public ModelAndView removeInterest(@RequestParam(value="projectID") int projectID) throws SQLException { 
		//redirect to login page if you are not login
		if(userLoginID == 0) return login();
		if(newConnection == null) startDBConnection();
		PreparedStatement st = newConnection.prepareStatement("DELETE FROM interestproject WHERE projectID = ?");
		st.setInt(1,projectID);
		st.executeUpdate(); 
		//I am using same page since the final message for project or checklist is the same
		return new ModelAndView("projectRemovedPage");
	}

	@RequestMapping( value="/projectinterestedlist",method = RequestMethod.GET)
	//TODO I need to take care than if the user is not register to that project then he cannot remove interest
	public ModelAndView getListProjectInterested(Model model) throws SQLException { 
		//redirect to login page if you are not login
		if(userLoginID == 0) return login();
		if(newConnection == null) startDBConnection();
		List<Project> projectList = getProjectInterestedListByStudent();
		//If project is empty then I will redirect to error page with a message explaining what to do
		if(projectList.isEmpty()) {
			model.addAttribute("message", "You do not have any project register yet, go to projec list and choose one!");
			return new ModelAndView("errorPage");
		}	
		return new ModelAndView("interestProjectListPage","projectList",projectList);
	}

	@RequestMapping( value="/projectlecturerlist",method = RequestMethod.GET)
	public ModelAndView getListOfProjectsLecturer(Model model) throws SQLException { 
		//redirect to login page if you are not login
		if(userLoginID == 0) return login();
		if(newConnection == null) startDBConnection();

		List<Project> projectList = getAllProjectByLecturer();
		List<Project> projectWithInterest = getLecturerProjectList();
		//List<User> userList = getAllUserswithInterest();
		//If project is empty then I will redirect to error page with a message explaining what to do
		if(projectList.isEmpty()) {
			model.addAttribute("message", "You do not have any project register yet, go to projec list and choose one!");
			return new ModelAndView("errorPage");
		}	

		List<Project> finalList = listComparer(projectList,projectWithInterest);
		model.addAttribute("projectList", finalList);
		model.addAttribute("projectWithInterest", projectWithInterest);
		//I been forced to send the size separetely to the front end because javaScript lenght function
		//does not work when the list is with objects
		model.addAttribute("interestListSize",projectWithInterest.size());
		//model.addAttribute("userList", userList);
		return new ModelAndView("yourPersonalListPage");
	}

	@RequestMapping( value="/approveinterest",method = RequestMethod.POST)
	public ModelAndView approveInterestPage(@RequestParam(value="projectID") int projectID, int userID) throws SQLException { 
		//redirect to login page if you are not login
		if(userLoginID == 0) return login();
		if(newConnection == null) startDBConnection();

		Project project = new Project();
		project = project.getProject(projectID);

		String queryInsert = " insert into approvedproject (userID, projectID) values (?, ?)";
		PreparedStatement preparedStmt = newConnection.prepareStatement(queryInsert);
		preparedStmt.setInt (1, userLoginID);
		preparedStmt.setInt (2, projectID);
		preparedStmt.execute();
		preparedStmt.close();
		removeInterest(projectID);
		//I am populating here the view so the user can modify the project, if I need to add more data to the form I should
		//update the constructor on project class
		return new ModelAndView("editprojectPage","command",new Project(project.getProjectID(),project.getYear(),
				project.getTitle(),project.getTopics(),project.getCompulsoryReading(),project.getDescription()));  
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
		st.close();
		return 0;
	}
	public int getLastChecklistID() throws SQLException {
		if(newConnection == null) startDBConnection();
		String query = "SELECT checklistID FROM checklist ORDER BY checklistID DESC LIMIT 1";
		Statement st = newConnection.createStatement();
		ResultSet rs = st.executeQuery(query);
		while (rs.next())
		{
			return(rs.getInt("checklistID"));
		}
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
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return checklistList;
	}

	public List<Project> getProjectInterestedListByStudent() {
		String query = "SELECT * FROM interestproject WHERE userID = '" + userLoginID + "';";
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
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return projectList;
	}

	public List<Project> getAllProjectByLecturer() {
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
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userList;
	}

	public List<Project> getLecturerProjectList() {
		String query = "SELECT * FROM interestproject";
		Statement st;
		List<Project> projectList = new ArrayList<Project>();
		try {
			st = newConnection.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next())
			{
				String queryProject = "SELECT * FROM project WHERE projectID = '" + rs.getString("projectID") + "' "
						+ "AND lecturerID = '" + userLoginID + "';";
				Statement stProject = newConnection.createStatement();
				ResultSet rsProject = stProject.executeQuery(queryProject);
				while (rsProject.next()) {
					Project project = new Project();
					project = project.getProject(rsProject.getInt("projectID"));
					User student = getUser(rs.getInt("userID"));
					project.setStudent(student);
					projectList.add(project);
				}
				stProject.close();
			}
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return projectList;
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
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}