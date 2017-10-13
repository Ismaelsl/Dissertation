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

import javax.servlet.http.HttpServletRequest;
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
	//private int userLoginID;

	private void startDBConnection() {
		//Create a connection to the DB as soon as we need it
		DBConnection connect = new DBConnection();
		newConnection = connect.connect();
	}

	@RequestMapping(value = { "/", "/home" }, method = RequestMethod.GET)
	public ModelAndView homePage(HttpServletRequest request) {
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if(newConnection == null) startDBConnection();
		return new ModelAndView("homePage");
	}

	@RequestMapping(value = "/login" , method = RequestMethod.GET)
	public ModelAndView login(HttpServletRequest request) {
		//redirect to home page if you are login and try to login again
		if(getSession(request) == null) return homePage(request);
		if(newConnection == null) startDBConnection();
		return new ModelAndView("loginPage","command",new User());
	}

	@RequestMapping(value="/logincheck",method = RequestMethod.POST)  
	public ModelAndView checkLogin(@ModelAttribute("user")User user, ModelMap model, HttpServletRequest request){ 
		//redirect to home page if you are login and try to login again
		if(getSession(request) == null) return homePage(request);
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

					//userLoginID = rs.getInt("userID");
					createSession(request,rs.getInt("userID"));
					//System.out.println("Yeah! " + userLoginID);
					return new ModelAndView("homePage");
				}
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		model.addAttribute("message", "You are not logged in, please go to login page and enter your credentials");
		return new ModelAndView("errorPage");
		//return "loginPage";
	}

	public void createSession(HttpServletRequest request, int userID) {
		HttpSession session = request.getSession() ;
		session.setAttribute("userID", userID);

	}
	public HttpSession getSession(HttpServletRequest request) {
		HttpSession session = request.getSession() ;
		if(session!= null) {
			return request.getSession();
		}
		return null;
	}


	@RequestMapping(value = { "/contactus" }, method = RequestMethod.GET)
	public ModelAndView contactusPage(Model model, HttpServletRequest request) throws SQLException {
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
		rs.close();
		st.close();
		return new ModelAndView("contactusPage");
		//return "contactusPage";
	}
	@RequestMapping(value = { "/projectlist" }, method = RequestMethod.GET)
	public ModelAndView projectListPage(Model model, HttpServletRequest request) throws SQLException {
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if(newConnection == null) startDBConnection();
		List<Project> projectList = getProjectListVisibleAnDApprove(true,true);
		
		System.out.println("testing sessions " + session.getAttribute("userID"));
		System.out.println("testing HttpServletRequest sessions " + request.getAttribute("userID"));
		System.out.println("List size " + projectList.size());
		User user = getUser((Integer)session.getAttribute("userID"));
		model.addAttribute("userType", user.getUserType());
		System.out.println("user type " + user.getUserType());
		if(projectList.isEmpty()) {
			model.addAttribute("message", "You project list is empty");
			return new ModelAndView("errorPage");
		}
		return new ModelAndView("projectListPage","projectList",projectList);  
	}

	@RequestMapping(value = { "/projectlisttoapprove" }, method = RequestMethod.GET)
	public ModelAndView projectListToApprovePage(Model model, HttpServletRequest request) throws SQLException {
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if(newConnection == null) startDBConnection();
		//List<Project> projectList = getProjectList(true);
		List<Project> projectList = getProjectListVisibleAnDApprove(false,true);
		System.out.println("List size " + projectList.size());

		User user = getUser((Integer)session.getAttribute("userID"));
		model.addAttribute("userType", user.getUserType());
		if(projectList.isEmpty()) {
			model.addAttribute("message", "You do not have any project to approve");
			return new ModelAndView("errorPage");
		}
		return new ModelAndView("projectListtoapprovePage","projectList",projectList);  
	}

	@RequestMapping( value="/approveproject",method = RequestMethod.POST)
	public ModelAndView approveprojectPage(@RequestParam(value="projectID") int projectID, 
			Model model,HttpServletRequest request) throws SQLException { 
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);

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
			//HttpSession session = getSession(request);
			User actualUser = getUser((Integer)session.getAttribute("userID"));
			model.addAttribute("lecturerID",actualUser.getUserID());
			model.addAttribute("lecturername", actualUser.getUsername());
			model.addAttribute("lectureremail", actualUser.getEmail());
		}
		rs.close();
		st.close();
		return new ModelAndView("projectPage","project",model);//will display object data 
	}

	@RequestMapping( "/newproject")
	public ModelAndView newprojectPage(Model model, HttpServletRequest request) throws SQLException {  
		//redirect to login page if you are not login
		HttpSession session = getSession(request);if(session.getAttribute("userID") == null) return login(request);
		return new ModelAndView("newprojectPage","command",new Project());  
	}

	@RequestMapping( value="/edit",method = RequestMethod.POST)
	public ModelAndView editprojectPage(@RequestParam(value="projectID") int projectID, 
			HttpServletRequest request) throws SQLException { 
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
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
	public ModelAndView removeprojectPage(@RequestParam(value="projectID") int projectID,
			Model model, HttpServletRequest request) throws SQLException { 
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if(newConnection == null) startDBConnection();
		System.out.println("test projectID " + projectID);
		Project project = new Project();
		project = project.getProject(projectID);
		updateProject(projectID, false);
		//I am populating here the view so the user can modify the project, if I need to add more data to the form I should
		//update the constructor on project class
		model.addAttribute("message", project.getTitle());
		return new ModelAndView("projectRemovedPage");
	}

	@RequestMapping(value="/saveEdit",method = RequestMethod.POST)  
	public ModelAndView saveEditProject(@ModelAttribute("project") Project project, 
			Model model, HttpServletRequest request){ 
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		System.out.println(project.getProjectID() +" inside edit project page!!");
		if(newConnection == null) startDBConnection();
		model.addAttribute("year",project.getYear());
		model.addAttribute("title",project.getTitle());
		model.addAttribute("topics",project.getTopics());
		model.addAttribute("compulsoryReading",project.getCompulsoryReading());
		model.addAttribute("description",project.getDescription());
		//HttpSession session = getSession(request);
		User actualUser = getUser((Integer)session.getAttribute("userID"));
		model.addAttribute("lecturerID",actualUser.getUserID());
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
						"UPDATE project SET year = ?, title = ?, topic = ?, description = ?, compulsoryreading = ? WHERE projectID = ?");
				ps.setInt(1,project.getYear());
				ps.setString(2,project.getTitle());
				ps.setString(3,project.getTopics());
				ps.setString(4,project.getDescription());
				ps.setString(5,project.getCompulsoryReading());
				ps.setInt(6,rs.getInt("projectID"));
				ps.executeUpdate();
				ps.close();
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}


		return new ModelAndView("projectPage","project",model);//will display object data  
	}

	@RequestMapping(value="/save",method = RequestMethod.POST)  
	public ModelAndView save(@ModelAttribute("project") Project project, 
			Model model, HttpServletRequest request){  
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
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
		model.addAttribute("visible",true);
		model.addAttribute("documentID",1);
		model.addAttribute("waitingToBeApproved",false);
		model.addAttribute("checklistID",1);
		//HttpSession session = getSession(request);
		User actualUser = getUser((Integer)session.getAttribute("userID"));
		model.addAttribute("lecturerID",actualUser.getUserID());
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
			preparedStmt.setInt (6, (Integer)session.getAttribute("userID"));
			preparedStmt.setBoolean (7, true);
			preparedStmt.setInt (8, 1);
			preparedStmt.setBoolean (9, project.isWaitingToBeApproved());
			preparedStmt.setInt (10, 1);
			preparedStmt.setInt (11, 0);
			preparedStmt.execute();
			preparedStmt.close();
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
	Model model, HttpServletRequest request){ 
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
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
			rs.close();
			st.close();
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
	public ModelAndView newchecklistPage(Model model, HttpServletRequest request) throws SQLException {  
		//redirect to login page if you are not login
		HttpSession session = getSession(request);if(session.getAttribute("userID") == null) return login(request);
		return new ModelAndView("checklistPage","command",new CheckList());  
	}

	@RequestMapping(value="/savechecklist",method = RequestMethod.POST)  
	public ModelAndView saveChecklist(@ModelAttribute("checklist") CheckList checklist, 
			Model model, HttpServletRequest request){  
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
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
			preparedStmt.close();
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
	public ModelAndView checklistListPage(Model model, HttpServletRequest request) throws SQLException {
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if(newConnection == null) startDBConnection();
		List<CheckList> checklistList = getCheckListList(true);
		List<CheckList> checklistListNotApproved = getCheckListList(false);

		System.out.println("List size " + checklistList.size());
		//Keep this since I need to check if user is admin or not for now
		//HttpSession session = getSession(request);
		User user = getUser((Integer)session.getAttribute("userID"));
		model.addAttribute("userType", user.getUserType());
		model.addAttribute("checklistListNotApproved", checklistListNotApproved);
		model.addAttribute("notapprovedsize", checklistListNotApproved.size());
		return new ModelAndView("checklistListPage","checklistList",checklistList);  
	}

	@RequestMapping( value="/editChecklist",method = RequestMethod.POST)
	public ModelAndView editChecklistPage(@RequestParam(value="checklistID") int checklistID, 
			Model model, HttpServletRequest request) throws SQLException { 
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
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
	public ModelAndView removeChecklistPage(@RequestParam(value="checklistID") int checklistID, 
			HttpServletRequest request) throws SQLException { 
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if(newConnection == null) startDBConnection();
		System.out.println("test projectID " + checklistID);
		updateChecklist(checklistID,false);
		//I am using same page since the final message for project or checklist is the same
		return new ModelAndView("projectRemovedPage");
	}
	@RequestMapping( value="/makeVisibleChecklist",method = RequestMethod.POST)
	public ModelAndView makeVisibleChecklistPage(@RequestParam(value="checklistID") int checklistID, 
			Model model, HttpServletRequest request) 
			throws SQLException { 
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
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
	public ModelAndView saveEditChecklist(@ModelAttribute("checklist") CheckList checklist, 
			Model model, HttpServletRequest request){ 
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
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
			rs.close();
			st.close();
		} catch (SQLException e) {
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
	public ModelAndView registerInterestPage(@RequestParam(value="projectID") int projectID, 
			Model model, HttpServletRequest request) throws SQLException { 
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if(newConnection == null) startDBConnection();
		//HttpSession session = getSession(request);
		String query ="SELECT COUNT(*) as total FROM interestproject WHERE userID = '"+   (Integer)session.getAttribute("userID") + "' "
				+ "AND visible = true; ";
		Statement stCount = newConnection.createStatement();
		ResultSet rs = stCount.executeQuery(query);
		if(rs.next()) {
			if(rs.getInt("total") == 5) {
				model.addAttribute("message", "You already have 5 projects registered on your name, "
						+ "please remove one before you add new ones"); 
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
					model.addAttribute("message", "You are already register in this project");
					if(rsProject.getInt("projectID") == projectID && rsProject.getInt("userID") == (Integer)session.getAttribute("userID")) return new ModelAndView("errorPage");
				}
				rsProject.close();
				stProject.close();
				//If the project is not already on the table and the student has not more than 5 projects already then we will add
				//the new project to the table
				String queryInsert = " insert into interestproject (userID, projectID, visible) values (?, ?, ?)";
				PreparedStatement preparedStmt = newConnection.prepareStatement(queryInsert);
				preparedStmt.setInt (1, (Integer)session.getAttribute("userID"));
				preparedStmt.setInt (2, projectID);
				preparedStmt.setBoolean (3, true);//make the interest visible
				preparedStmt.execute();
				preparedStmt.close();
			}	
			rs.close();
		}

		//I am using same page since the final message for project or checklist is the same
		List<Project> projectList = getProjectInterestedListByStudent(true,(Integer)session.getAttribute("userID"));
		List<Project> projectListNotVisible = getProjectInterestedListByStudent(false, (Integer)session.getAttribute("userID"));
		if(projectList.isEmpty()) {
			model.addAttribute("message", "You do not have any project register yet, go to projec list and choose one!");
			return new ModelAndView("errorPage");
		}
		model.addAttribute("projectListNotVisible",projectListNotVisible);
		model.addAttribute("notInterestListSize", projectListNotVisible.size());
		return new ModelAndView("interestProjectListPage","projectList",projectList);
	}
	/**
	 * Need to have this method since I am having errors when the user have 5 projects already and 
	 * try to update the visibility of a project, since recognize a 6th project and stop you
	 * @param projectID
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping( value="/makeInterestVisible",method = RequestMethod.POST)
	public ModelAndView makeVisibleInterestPage(@RequestParam(value="projectID") int projectID, 
			Model model, HttpServletRequest request) throws SQLException { 
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if(newConnection == null) startDBConnection();
		//HttpSession session = getSession(request);
		String query ="SELECT COUNT(*) as total FROM interestproject WHERE userID = '"+   (Integer)session.getAttribute("userID") + "' "
				+ "AND visible = true; ";
		Statement stCount = newConnection.createStatement();
		ResultSet rs = stCount.executeQuery(query);
		if(rs.next()) {
			if(rs.getInt("total") == 5) {
				model.addAttribute("message", "You already have 5 projects registered on your name, "
						+ "please remove one before you add new ones"); 
				return new ModelAndView("errorPage");
			}else {
				updateInterestProject(projectID,(Integer)session.getAttribute("userID"),true);
				List<Project> projectList = getProjectInterestedListByStudent(true, (Integer)session.getAttribute("userID"));
				List<Project> projectListNotVisible = getProjectInterestedListByStudent(false, (Integer)session.getAttribute("userID"));
				if(projectList.isEmpty()) {
					model.addAttribute("message", "You do not have any project register yet, go to projec list and choose one!");
					return new ModelAndView("errorPage");
				}
				model.addAttribute("projectListNotVisible",projectListNotVisible);
				model.addAttribute("notInterestListSize", projectListNotVisible.size());
				return new ModelAndView("interestProjectListPage","projectList",projectList);
			}
		}
		model.addAttribute("message", "You had not register interest for any project yet"); 
		return new ModelAndView("errorPage");
	}

	@RequestMapping( value="/removeinterest",method = RequestMethod.POST)
	public ModelAndView removeInterest(@RequestParam(value="projectID") int projectID, 
			User user, HttpServletRequest request) throws SQLException { 
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if(newConnection == null) startDBConnection();
		System.out.println("user id is " + user.getUserID() + " and projectID is " + projectID);
		updateInterestProject(projectID,user.getUserID(),false);
		//I am using same page since the final message for project or checklist is the same
		return new ModelAndView("projectRemovedPage");
	}
	
	@RequestMapping( value="/removeinterestStudent",method = RequestMethod.POST)
	public ModelAndView removeInterestStudent(@RequestParam(value="projectID") int projectID, 
			HttpServletRequest request) throws SQLException { 
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if(newConnection == null) startDBConnection();
		//System.out.println("user id is " + user.getUserID() + " and projectID is " + projectID);
		updateInterestProject(projectID,(Integer)session.getAttribute("userID"),false);
		//I am using same page since the final message for project or checklist is the same
		return new ModelAndView("projectRemovedPage");
	}

	@RequestMapping( value="/projectinterestedlist",method = RequestMethod.GET)
	public ModelAndView getListProjectInterested(Model model, HttpServletRequest request) throws SQLException { 
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if(newConnection == null) startDBConnection();
		//If the student already have a final project approved, then we will only show that project to him
		Project project = getFinalProjectStudent((Integer)session.getAttribute("userID"));
		if(project != null) {
			List<Project> projectList = new ArrayList<Project>();
			projectList.add(project);
			return new ModelAndView("finalProjectPage","projectList",projectList);
		}
		//HttpSession session = getSession(request);
		List<Project> projectList = getProjectInterestedListByStudent(true, (Integer)session.getAttribute("userID"));
		List<Project> projectListNotVisible = getProjectInterestedListByStudent(false, (Integer)session.getAttribute("userID"));
		//If project is empty then I will redirect to error page with a message explaining what to do
		if(projectList.isEmpty()) {
			model.addAttribute("message", "You do not have any project register yet, go to projec list and choose one!");
			return new ModelAndView("errorPage");
		}	
		model.addAttribute("projectListNotVisible",projectListNotVisible);
		model.addAttribute("notInterestListSize", projectListNotVisible.size());
		return new ModelAndView("interestProjectListPage","projectList",projectList);
	}

	@RequestMapping( value="/projectlecturerlist",method = RequestMethod.GET)
	public ModelAndView getListOfProjectsLecturer(Model model, HttpServletRequest request) throws SQLException { 
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if(newConnection == null) startDBConnection();

		List<Project> projectList = getProjectListVisible(true);	
		//If project is empty then I will redirect to error page with a message explaining what to do
		if(projectList.isEmpty()) {
			model.addAttribute("message", "You do not have any project register yet, go to project list and choose one!");
			return new ModelAndView("errorPage");
		}	

		model.addAttribute("projectList", projectList);
		return new ModelAndView("yourPersonalListPage");
	}
	@RequestMapping( value="/projectlistwithinterest",method = RequestMethod.GET)
	public ModelAndView lecturerProjectListWithInterest(Model model, HttpServletRequest request) throws SQLException { 
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if(newConnection == null) startDBConnection();
		//HttpSession session = getSession(request);

		List<Project> projectWithInterest = getLecturerProjectList((Integer)session.getAttribute("userID"));	

		model.addAttribute("projectWithInterest", projectWithInterest);
		//I been forced to send the size separately to the front end because javaScript length function
		//does not work when the list is with objects
		model.addAttribute("interestListSize",projectWithInterest.size());
		model.addAttribute("user", new User());//passing the user allows to return any user value from the frontend
		return new ModelAndView("yourPersonalListPageWithInterest");
	}

	@RequestMapping( value="/projectlecturerlistnotvisible",method = RequestMethod.GET)
	public ModelAndView getListOfProjectsNoVisibleLecturer(Model model, HttpServletRequest request) throws SQLException { 
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if(newConnection == null) startDBConnection();

		List<Project> projectNotVisibles = getProjectListVisible(false);
		//If project is empty then I will redirect to error page with a message explaining what to do
		if(projectNotVisibles.isEmpty()) {
			model.addAttribute("message", "You do not have any project not visible");
			return new ModelAndView("errorPage");
		}	

		model.addAttribute("projectNotVisibles", projectNotVisibles);
		return new ModelAndView("notVisibleProjectPage");
	}

	@RequestMapping( value="/makeItVisible",method = RequestMethod.POST)
	public ModelAndView makeAProjectVisible(@RequestParam(value="projectID") int projectID, 
			Model model, HttpServletRequest request) throws SQLException { 
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if(newConnection == null) startDBConnection();
		updateProject(projectID, true);//update the project to visible
		//getting the project to obtain the title
		Project project = new Project();
		project = project.getProject(projectID);
		//getting all the project not visible to populate the view again
		List<Project> projectNotVisibles = getProjectListVisible(false);
		//If project is empty then I will redirect to error page with a message explaining what to do
		if(projectNotVisibles.isEmpty()) {
			model.addAttribute("message", "You do not have any project not visible");
			return new ModelAndView("errorPage");
		}	
		//extra message to show that the project had been made visible
		model.addAttribute("message", "Project " + project.getTitle() + " is visible now to students");
		model.addAttribute("projectNotVisibles", projectNotVisibles);
		return new ModelAndView("notVisibleProjectPage");
	}

	//This user is the one passed from the previous method (projectwithinterestlist)
	//This user should be populate with the userID from the project that we choose to approve
	@RequestMapping( value="/approveinterest",method = RequestMethod.POST)
	public ModelAndView approveInterestPage(@RequestParam(value="projectID") int projectID, 
			User user, Model model, HttpServletRequest request) throws SQLException { 
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if(newConnection == null) startDBConnection();
		//HttpSession session = getSession(request);
		
		Project project = new Project();
		project = project.getProject(projectID);
		//getting user based on the userID, so I can have access to all his data
		user = getUser(user.getUserID());
		String queryInsert = " insert into approvedproject (userID, projectID) values (?, ?)";
		PreparedStatement preparedStmt = newConnection.prepareStatement(queryInsert);
		preparedStmt.setInt (1, user.getUserID());
		preparedStmt.setInt (2, projectID);
		preparedStmt.execute();
		preparedStmt.close();
		updateListOfProjectAfterApproveInterest(user.getUserID());
		List<Project> projectWithInterest = getLecturerProjectList((Integer)session.getAttribute("userID"));	

		model.addAttribute("projectWithInterest", projectWithInterest);
		//I been forced to send the size separately to the front end because javaScript length function
		//does not work when the list is with objects
		model.addAttribute("interestListSize",projectWithInterest.size());
		model.addAttribute("user", new User());//passing the user allows to return any user value from the frontend
		model.addAttribute("message", "Student " + user.getUsername() + " had been registered with your project "
				+ project.getTitle());
		return new ModelAndView("yourPersonalListPageWithInterest");
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
		rs.close();
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
		String query = "SELECT * FROM project";
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
				PreparedStatement ps = newConnection.prepareStatement(
						"UPDATE project SET visible = ? WHERE projectID = ?");
				ps.setBoolean(1, status);
				ps.setInt(2, projectID);
				ps.execute();
				ps.close();
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
			}
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
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Project getFinalProjectStudent(int userID) {
		String query ="SELECT * FROM approvedproject WHERE userID =  '" + userID + "';";
		Statement st;
		try {
			st = newConnection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				Project project = new Project();
				project = project.getProject(rs.getInt("projectID"));
				return project;
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}