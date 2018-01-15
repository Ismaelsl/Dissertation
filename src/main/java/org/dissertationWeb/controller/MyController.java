package org.dissertationWeb.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.dissertationWeb.classes.CheckList;
import org.dissertationWeb.classes.DBConnection;
import org.dissertationWeb.classes.MailMail;
import org.dissertationWeb.classes.Project;
import org.dissertationWeb.classes.SQLController;
import org.dissertationWeb.classes.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
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
	@Autowired 
	private HttpSession httpSession;

	private Connection newConnection;

	private SQLController sqlController;
	//private int userLoginID;

	private void startDBConnection() {
		//Create a connection to the DB as soon as we need it
		DBConnection connect = new DBConnection();
		newConnection = connect.connect();

		sqlController = new SQLController();
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
		if(newConnection == null) startDBConnection();
		//System.out.println("Username " + user.getUsername() + " password " + user.getPassword());
		int userID = sqlController.loginCheck(user);
		if(userID != 0) {
			User userLogged = sqlController.getUser(userID);
			createSession(request,userLogged.getUserID(), userLogged.getUserType());
			return new ModelAndView("homePage");
		}else {
			model.addAttribute("message", "You are not logged in, please go to login page and enter your credentials");
			return new ModelAndView("errorPage");
		}
	}

	/**
	 * Method that create the session and set as attribute in the session the userID and the maximum inactive time to 60 seconds
	 * @param request
	 * @param userID
	 */
	public void createSession(HttpServletRequest request, int userID, int userType) {
		HttpSession session = request.getSession() ;
		session.setAttribute("userID", userID);
		session.setAttribute("userType", userType);
		session.setMaxInactiveInterval(600);//right now is 600 seconds since I need to do some testing and need the session to last longer
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
		int userID = sqlController.contacPage();
		if(userID != 0) {
			User admin = sqlController.getUser(userID);

			//Passing the data to the model
			model.addAttribute("name", admin.getUsername());
			model.addAttribute("department", "Computer Science");
			model.addAttribute("email", admin.getEmail());

			return new ModelAndView("contactusPage");

		}else {
			model.addAttribute("message", "Error retrieving information from the DB, please try again later");//I am passing a message to the error page
			return new ModelAndView("errorPage");
		}
	}
	@RequestMapping(value = { "/projectlist" }, method = RequestMethod.GET)
	public ModelAndView projectListPage(Model model, HttpServletRequest request) throws SQLException {
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if(newConnection == null) startDBConnection();
		List<Project> projectList = sqlController.getProjectListVisibleAnDApprove(true,true);

		System.out.println("testing sessions " + session.getAttribute("userID"));
		System.out.println("testing HttpServletRequest sessions " + request.getAttribute("userID"));
		System.out.println("List size " + projectList.size());
		User user = sqlController.getUser((Integer)session.getAttribute("userID"));//getting userID from the session
		model.addAttribute("userType", user.getUserType());
		System.out.println("user type " + user.getUserType());
		if(projectList.isEmpty()) {
			model.addAttribute("message", "You project list is empty");//I am passing a message to the error page
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
		List<Project> projectList = sqlController.getProjectListVisibleAnDApprove(false,true);
		System.out.println("List size " + projectList.size());

		User user = sqlController.getUser((Integer)session.getAttribute("userID"));
		model.addAttribute("userType", user.getUserType());
		if(projectList.isEmpty()) {
			model.addAttribute("message", "You do not have any project to approve");
			return new ModelAndView("errorPage");
		}
		return new ModelAndView("projectListtoapprovePage","projectList",projectList);  
	}

	@RequestMapping( value="/approveproject",method = RequestMethod.POST)
	public ModelAndView approveprojectPage(@RequestParam(value="projectID") int projectID, 
			Model model,HttpServletRequest request) { 
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);

		if(sqlController.approveProject(projectID)!= 0) {
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
			User actualUser = sqlController.getUser((Integer)session.getAttribute("userID"));
			model.addAttribute("lecturerID",actualUser.getUserID());
			model.addAttribute("lecturername", actualUser.getUsername());
			model.addAttribute("lectureremail", actualUser.getEmail());
			ApplicationContext context =
					new ClassPathXmlApplicationContext("Spring-Mail.xml");

			MailMail mm = (MailMail) context.getBean("mailMail");
			String message = "Your project had been approved";
			String lecturerEmail = actualUser.getEmail();
			mm.sendMail("ismael.sanchez.leon@gmail.com","tatowoke@gmail.com","Approved project from " + actualUser.getUsername(),message);
			//Final version will be sending an email to the lecturer letting him now that the project had been approved
			//mm.sendMail("ismael.sanchez.leon@gmail.com",lecturerEmail,"Project approved " + actualUser.getUsername(),message);

			return new ModelAndView("projectPage","project",model);//will display object data 
		}else {
			model.addAttribute("message", "An error happens while trying to approve your project");
			return new ModelAndView("errorPage");//I am using the errorPage since I only want to show the message on the screen without create a new view
		}
	}

	@RequestMapping( "/newproject")
	public ModelAndView newprojectPage(Model model, HttpServletRequest request) throws SQLException {  
		//redirect to login page if you are not login
		HttpSession session = getSession(request);if(session.getAttribute("userID") == null) return login(request);
		return new ModelAndView("newprojectPage","command",new Project());  
	}

	@RequestMapping( "/projectproposal")
	public ModelAndView newprojectProposalPage(Model model, HttpServletRequest request) throws SQLException {  
		//redirect to login page if you are not login
		HttpSession session = getSession(request);if(session.getAttribute("userID") == null) return login(request);
		return new ModelAndView("projectproposalpage","command",new Project());  
	}

	@RequestMapping(value="/sendProposal",method = RequestMethod.POST)  
	public ModelAndView saveProposal(@ModelAttribute("project") Project project, 
			Model model, HttpServletRequest request){  
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if(newConnection == null) startDBConnection();
		//write code to save project object  
		//here, we are displaying project object to prove project has data  
		User user = sqlController.getUser((Integer)session.getAttribute("userID"));
		System.out.println(project.getTitle()+" "+project.getDescription()); 
		//Automatic email system
		ApplicationContext context =
				new ClassPathXmlApplicationContext("Spring-Mail.xml");

		MailMail mm = (MailMail) context.getBean("mailMail");
		String message = mm.createMessageProjectProposal(user.getUsername(), project.getDescription(),
				project.getCompulsoryReading(),project.getTitle(),project.getTopics());
		String emailFrom = user.getEmail(); //In the final version I should be getting the email from the user and send it to the coordinator
		mm.sendMail("ismael.sanchez.leon@gmail.com","tatowoke@gmail.com","New project proposal from " + user.getUsername(),message);
		model.addAttribute("message", "Your proposal had arrived succesfully to the dissertation coordinator");
		return new ModelAndView("errorPage");//I am using the errorPage since I only want to show the message on the screen without create a new view 
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
		sqlController.updateProject(projectID, false);
		//I am populating here the view so the user can modify the project, if I need to add more data to the form I should
		//update the constructor on project class
		model.addAttribute("message", project.getTitle());
		ApplicationContext context =
				new ClassPathXmlApplicationContext("Spring-Mail.xml");

		MailMail mm = (MailMail) context.getBean("mailMail");
		String message = "The visibility of your project had been deactivated";
		User user = sqlController.getUser((Integer)session.getAttribute("userID"));
		String lecturerEmail = user.getEmail();
		mm.sendMail("ismael.sanchez.leon@gmail.com","tatowoke@gmail.com","Project removed from " + user.getUsername(),message);
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
		User actualUser = sqlController.getUser((Integer)session.getAttribute("userID"));
		model.addAttribute("lecturerID",actualUser.getUserID());
		model.addAttribute("lecturername", actualUser.getUsername());
		model.addAttribute("lectureremail", actualUser.getEmail());
		if(sqlController.saveEdit(project)) {
			ApplicationContext context =
					new ClassPathXmlApplicationContext("Spring-Mail.xml");

			MailMail mm = (MailMail) context.getBean("mailMail");
			String message = "Your project had been modified";
			User user = sqlController.getUser((Integer)session.getAttribute("userID"));
			String lecturerEmail = user.getEmail();
			mm.sendMail("ismael.sanchez.leon@gmail.com","tatowoke@gmail.com","Modified project from " + user.getUsername(),message);
			return new ModelAndView("projectPage","project",model);//will display object data  
		}else {
			model.addAttribute("message", "Error happens while updating the project, if this error continues please contact the system administrator");
			return new ModelAndView("errorPage");
		}


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
		User actualUser = sqlController.getUser((Integer)session.getAttribute("userID"));
		model.addAttribute("lecturerID",actualUser.getUserID());
		model.addAttribute("lecturername", actualUser.getUsername());
		model.addAttribute("lectureremail", actualUser.getEmail());
		//model.addAttribute("projectID",project.getTitle());
		if(sqlController.save(project, (Integer)session.getAttribute("userID"))) {
			try {
				if(sqlController.getLastProjectID() == 0) return new ModelAndView("errorPage","project",null);
				model.addAttribute("projectID",sqlController.getLastProjectID());
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		//Automatic email system
		ApplicationContext context =
				new ClassPathXmlApplicationContext("Spring-Mail.xml");

		MailMail mm = (MailMail) context.getBean("mailMail");
		String message = mm.newOrApproveProjectMessage(actualUser.getUsername(),project.getTitle(), "created");
		String emailFrom = actualUser.getEmail(); //In the final version I should be getting the email from the user and send it to the coordinator
		mm.sendMail("ismael.sanchez.leon@gmail.com","tatowoke@gmail.com","New project from " + actualUser.getUsername(),message);
		model.addAttribute("message", "Your proposal had arrived succesfully to the dissertation coordinator");
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
		//required false indicate that it is not compulsory to have it, so if I do not have, for example, a title, java will not complaint
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		//If user does not enter any search criteria
		if(searchValue == null) {
			model.addAttribute("message", "Please write your search criteria in the search box");
			return new ModelAndView("errorPage");
		}
		searchValue.toLowerCase();//better if I put everything on lower case
		//If any of the checkboxes had been marked
		if(lecturer == null && technology == null && title == null) {
			model.addAttribute("message", "Please choose any of the three search criteria, lecturer, technology or topic");
			return new ModelAndView("errorPage");
		}
		//I am only interested to show projects that are visible and approved and has not been already choose by another student
		List<Project> projectList = sqlController.search(lecturer, technology, title, searchValue);
		if(projectList != null && !projectList.isEmpty()) {
			User user = sqlController.getUser((Integer)session.getAttribute("userID"));
			model.addAttribute("userType", user.getUserType());
			return new ModelAndView("projectListPage","projectList",projectList);
		}else {
			model.addAttribute("message", "Your search criteria does not return any result please try something else");
			return new ModelAndView("errorPage");
		}
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
		if(sqlController.saveCheckList(checklist)) {
			model.addAttribute("date", checklist.getDate());
			model.addAttribute("eventname", checklist.getEventName());
			model.addAttribute("place", checklist.getPlace());
			model.addAttribute("description", checklist.getDescription());
			try {
				int checkListID = sqlController.getLastChecklistID();
				if(checkListID != 0)
					model.addAttribute("checklistID",checkListID);
				else {
					model.addAttribute("message", "Error saving the new enter for the schedule, please try again later");
					return new ModelAndView("errorPage");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}else {
			model.addAttribute("message", "Error saving the new enter for the schedule, please try again later");
			return new ModelAndView("errorPage");
		}

		//Need to change this redirect to a checklistPage to see the checklist that we just added
		ApplicationContext context =
				new ClassPathXmlApplicationContext("Spring-Mail.xml");

		MailMail mm = (MailMail) context.getBean("mailMail");
		String message = "A new element in the scheduled had been added";
		User user = sqlController.getUser((Integer)session.getAttribute("userID"));
		mm.sendMail("ismael.sanchez.leon@gmail.com","tatowoke@gmail.com","New element in the scheduled aded for " + user.getUsername(),message);
		return new ModelAndView("checklistViewPage","checklist",model);//will display object data  
	}

	@RequestMapping(value = { "/checklistlist" }, method = RequestMethod.GET)
	public ModelAndView checklistListPage(Model model, HttpServletRequest request) throws SQLException {
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if(newConnection == null) startDBConnection();
		List<CheckList> checklistList = sqlController.getCheckListList(true);
		List<CheckList> checklistListNotApproved = sqlController.getCheckListList(false);

		System.out.println("List size " + checklistList.size());
		//Keep this since I need to check if user is admin or not for now
		//HttpSession session = getSession(request);
		User user = sqlController.getUser((Integer)session.getAttribute("userID"));
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
		//I am populating here the view so the user can modify the checklist, if I need to add more data to the form I should
		//update the constructor on checklist class
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
		sqlController.updateChecklist(checklistID,false);
		//I am using same page since the final message for project or checklist is the same
		ApplicationContext context =
				new ClassPathXmlApplicationContext("Spring-Mail.xml");

		MailMail mm = (MailMail) context.getBean("mailMail");
		CheckList checkList = new CheckList();
		checkList = checkList.getchecklist(checklistID);
		String message = "The element in the scheduled " + checkList.getEventName() + " had been removed";
		User user = sqlController.getUser((Integer)session.getAttribute("userID"));
		mm.sendMail("ismael.sanchez.leon@gmail.com","tatowoke@gmail.com","An element in the scheduled had been removed for " + user.getUsername(),message);
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
		sqlController.updateChecklist(checklistID,true);
		List<CheckList> checklistList = sqlController.getCheckListList(true);
		List<CheckList> checklistListNotApproved = sqlController.getCheckListList(false);
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
		if(sqlController.saveEditCheckList(checklist)) {
			ApplicationContext context =
					new ClassPathXmlApplicationContext("Spring-Mail.xml");

			MailMail mm = (MailMail) context.getBean("mailMail");
			String message = "The element in the scheduled " + checklist.getEventName() + " had been modified";
			User user = sqlController.getUser((Integer)session.getAttribute("userID"));
			mm.sendMail("ismael.sanchez.leon@gmail.com","tatowoke@gmail.com","An element in the scheduled had been modified for " + user.getUsername(),message);
			return new ModelAndView("checklistViewPage","checklist",model);//will display object data 
		}else {
			model.addAttribute("message", "Error saving the updated enter for the schedule, please try again later");
			return new ModelAndView("errorPage");
		}
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

		/**
		 * I have different returns based on the output of the method, 
		 * 0 general error saving. 
		 * 1 everything is OK.
		 * 2 duplicate entry (student trying to register twice in the same project).
		 * 5 limit of 5 projects achieved.
		 */
		int result = sqlController.registerInterest((Integer)session.getAttribute("userID"), projectID);
		switch(result) {
		case 0 :
			model.addAttribute("message", "Error happen while trying to register your interest, please try again later"); 
			return new ModelAndView("errorPage");

		case 1:
			//I am using same page since the final message for project or checklist is the same
			List<Project> projectList = sqlController.getProjectInterestedListByStudent(true,(Integer)session.getAttribute("userID"));
			List<Project> projectListNotVisible = sqlController.getProjectInterestedListByStudent(false, (Integer)session.getAttribute("userID"));
			if(projectList.isEmpty()) {
				model.addAttribute("message", "You do not have any project register yet, go to projec list and choose one!");
				return new ModelAndView("errorPage");
			}
			model.addAttribute("projectListNotVisible",projectListNotVisible);
			model.addAttribute("notInterestListSize", projectListNotVisible.size());
			ApplicationContext context =
					new ClassPathXmlApplicationContext("Spring-Mail.xml");

			MailMail mm = (MailMail) context.getBean("mailMail");
			Project project = new Project();
			project = project.getProject(projectID);
			User user = sqlController.getUser((Integer)session.getAttribute("userID"));
			User lecturer = sqlController.getUser(project.getlecturerID());

			String messageLecturer = "Student " + user.getUsername() + " had show interest in your project " + project.getTitle();
			String messageStudent = "Your interest in the project " + project.getTitle() + " had been send to the lecturer " + lecturer.getUsername();
			//One message is for the lecturer
			mm.sendMail("ismael.sanchez.leon@gmail.com","tatowoke@gmail.com","Someone register in one of your proejcts",messageLecturer);
			//Another message it is send to the student
			mm.sendMail("ismael.sanchez.leon@gmail.com","tatowoke@gmail.com","Someone register in one of your proejcts",messageStudent);
			return new ModelAndView("interestProjectListPage","projectList",projectList); 
		case 2:
			model.addAttribute("message", "You are already register in this project");
			return new ModelAndView("errorPage");
		case 5:
			model.addAttribute("message", "You already have 5 projects registered on your name, "
					+ "please remove one before you add new ones"); 
			return new ModelAndView("errorPage");
		}		
		model.addAttribute("message", "Error happen while trying to register your interest, please try again later");
		return new ModelAndView("errorPage");	
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
		int count = sqlController.getTotalInterestProject((Integer)session.getAttribute("userID"));
		if(count == 5) {
			model.addAttribute("message", "You already have 5 projects registered on your name, "
					+ "please remove one before you add new ones"); 
			return new ModelAndView("errorPage");
		}
		if(count == 0){
			model.addAttribute("message", "You had not register interest for any project yet"); 
			return new ModelAndView("errorPage");
		}else {
			sqlController.updateInterestProject(projectID,(Integer)session.getAttribute("userID"),true);
			List<Project> projectList = sqlController.getProjectInterestedListByStudent(true, (Integer)session.getAttribute("userID"));
			List<Project> projectListNotVisible = sqlController.getProjectInterestedListByStudent(false, (Integer)session.getAttribute("userID"));
			if(projectList.isEmpty()) {
				model.addAttribute("message", "You do not have any project register yet, go to projec list and choose one!");
				return new ModelAndView("errorPage");
			}
			model.addAttribute("projectListNotVisible",projectListNotVisible);
			model.addAttribute("notInterestListSize", projectListNotVisible.size());
			ApplicationContext context =
					new ClassPathXmlApplicationContext("Spring-Mail.xml");

			MailMail mm = (MailMail) context.getBean("mailMail");
			Project project = new Project();
			project = project.getProject(projectID);
			User user = sqlController.getUser((Integer)session.getAttribute("userID"));
			User lecturer = sqlController.getUser(project.getlecturerID());

			String messageLecturer = "Student " + user.getUsername() + " had show interest in your project " + project.getTitle();
			String messageStudent = "Your interest in the project " + project.getTitle() + " had been send to the lecturer " + lecturer.getUsername();
			//One message is for the lecturer
			mm.sendMail("ismael.sanchez.leon@gmail.com","tatowoke@gmail.com","Someone register in one of your proejcts",messageLecturer);
			//Another message it is send to the student
			mm.sendMail("ismael.sanchez.leon@gmail.com","tatowoke@gmail.com","Someone register in one of your proejcts",messageStudent);
			return new ModelAndView("interestProjectListPage","projectList",projectList);
		}

	}

	@RequestMapping( value="/removeinterest",method = RequestMethod.POST)
	public ModelAndView removeInterest(@RequestParam(value="projectID") int projectID, 
			User user, HttpServletRequest request) throws SQLException { 
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if(newConnection == null) startDBConnection();
		System.out.println("user id is " + user.getUserID() + " and projectID is " + projectID);
		//updateInterestProject(projectID,user.getUserID(),false);
		sqlController.updateInterestFinalProject(projectID,user.getUserID(),false);
		//I am using same page since the final message for project or checklist is the same
		ApplicationContext context =
				new ClassPathXmlApplicationContext("Spring-Mail.xml");

		MailMail mm = (MailMail) context.getBean("mailMail");
		Project project = new Project();
		project = project.getProject(projectID);
		//User user = getUser((Integer)session.getAttribute("userID"));
		User lecturer = sqlController.getUser(project.getlecturerID());

		String messageStudent = "Lecturer " + lecturer.getUsername() + " had cancel your interest in the project " + project.getTitle();
		String messageLecturer = "You remove the interest in the project " + project.getTitle();
		//One message is for the lecturer
		mm.sendMail("ismael.sanchez.leon@gmail.com","tatowoke@gmail.com","Someone register in one of your proejcts",messageLecturer);
		//Another message it is send to the student
		mm.sendMail("ismael.sanchez.leon@gmail.com","tatowoke@gmail.com","Someone register in one of your proejcts",messageStudent);
		return new ModelAndView("projectRemovedPage");
	}

	@RequestMapping( value="/removeinterestStudent",method = RequestMethod.POST)
	public ModelAndView removeInterestStudent(@RequestParam(value="projectID") int projectID, 
			HttpServletRequest request, User user) throws SQLException { 
		System.out.println("Coordinator is changing the interest of a final project1 userID is " + user.getUserID());
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		System.out.println("Coordinator is changing the interest of a final project2");
		if(newConnection == null) startDBConnection();
		System.out.println("Coordinator is changing the interest of a final project3");
		//updateInterestFinalProject(projectID,user.getUserID(),false);
		User student = sqlController.getUser((Integer)session.getAttribute("userID"));
		sqlController.updateInterestProject(projectID,student.getUserID(),false);
		ApplicationContext context =
				new ClassPathXmlApplicationContext("Spring-Mail.xml");

		MailMail mm = (MailMail) context.getBean("mailMail");
		Project project = new Project();
		project = project.getProject(projectID);

		String messageLecturer = "Student " + student.getUsername() + " had cancel the interest in your project " + project.getTitle();
		String messageStudent = "You remove the interest in the project " + project.getTitle();
		//One message is for the lecturer
		mm.sendMail("ismael.sanchez.leon@gmail.com","tatowoke@gmail.com","Someone register in one of your proejcts",messageLecturer);
		//Another message it is send to the student
		mm.sendMail("ismael.sanchez.leon@gmail.com","tatowoke@gmail.com","Someone register in one of your proejcts",messageStudent);
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
		Project project = sqlController.getFinalProjectStudent((Integer)session.getAttribute("userID"));
		if(project != null) {
			//only if the project if visible this will be show, I am taking consideration of a project that had been approved and then remove interest by coordinator
			if(project.isVisible()) {
				List<Project> projectList = new ArrayList<Project>();
				projectList.add(project);
				return new ModelAndView("finalProjectPage","projectList",projectList);
			}
		}
		//HttpSession session = getSession(request);
		List<Project> projectList = sqlController.getProjectInterestedListByStudent(true, (Integer)session.getAttribute("userID"));
		List<Project> projectListNotVisible = sqlController.getProjectInterestedListByStudent(false, (Integer)session.getAttribute("userID"));
		//If project is empty then I will redirect to error page with a message explaining what to do
		if(projectList.isEmpty() && projectListNotVisible.isEmpty()) {
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

		List<Project> projectList = sqlController.getProjectListVisible(true);	
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

		List<Project> projectWithInterest = sqlController.getLecturerProjectList((Integer)session.getAttribute("userID"));	

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

		List<Project> projectNotVisibles = sqlController.getProjectListVisible(false);
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
		sqlController.updateProject(projectID, true);//update the project to visible
		//getting the project to obtain the title
		Project project = new Project();
		project = project.getProject(projectID);
		//getting all the project not visible to populate the view again
		List<Project> projectNotVisibles = sqlController.getProjectListVisible(false);
		//If project is empty then I will redirect to error page with a message explaining what to do
		if(projectNotVisibles.isEmpty()) {
			model.addAttribute("message", "You do not have any project not visible");
			return new ModelAndView("errorPage");
		}	
		ApplicationContext context =
				new ClassPathXmlApplicationContext("Spring-Mail.xml");

		MailMail mm = (MailMail) context.getBean("mailMail");
		String messageStudent = "You make visible the project " + project.getTitle();
		//Another message it is send to the student
		mm.sendMail("ismael.sanchez.leon@gmail.com","tatowoke@gmail.com","Someone register in one of your proejcts",messageStudent);
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
		if(sqlController.approveInteret(projectID, user)) {
			Project project = new Project();
			project = project.getProject(projectID);
			//getting user based on the userID, so I can have access to all his data
			user = sqlController.getUser(user.getUserID());
			sqlController.updateListOfProjectAfterApproveInterest(user.getUserID());
			List<Project> projectWithInterest = sqlController.getLecturerProjectList((Integer)session.getAttribute("userID"));	

			model.addAttribute("projectWithInterest", projectWithInterest);
			//I been forced to send the size separately to the front end because javaScript length function
			//does not work when the list is with objects
			model.addAttribute("interestListSize",projectWithInterest.size());
			model.addAttribute("user", new User());//passing the user allows to return any user value from the frontend
			model.addAttribute("message", "Student " + user.getUsername() + " had been registered with your project "
					+ project.getTitle());

			User student = sqlController.getUser((Integer)session.getAttribute("userID"));
			ApplicationContext context =
					new ClassPathXmlApplicationContext("Spring-Mail.xml");

			MailMail mm = (MailMail) context.getBean("mailMail");
			project = project.getProject(projectID);

			//This two lines will be use in the final version to send the email to the correct lecturer, for now I am sending to the same email
			//For testing purposes
			User lecturer = sqlController.getUser(project.getlecturerID());
			String lecturerEmail = lecturer.getEmail();

			String messageStudent = "Congratulations " + student.getUsername() + " your request for the project " + project.getTitle() + " had been approved!";
			String messageLecturer = "You approved the interest in the project " + project.getTitle() + "for student " + student.getUsername();
			//One message is for the lecturer
			mm.sendMail("ismael.sanchez.leon@gmail.com","tatowoke@gmail.com","Someone register in one of your projects",messageLecturer);
			//Another message it is send to the student
			mm.sendMail("ismael.sanchez.leon@gmail.com","tatowoke@gmail.com","Someone register in one of your projects",messageStudent);
			return new ModelAndView("yourPersonalListPageWithInterest");
		}else {
			model.addAttribute("message", "Error approving project, please try again later");
			return new ModelAndView("errorPage");
		}
	}

	@RequestMapping( value="/studentlist",method = RequestMethod.GET)
	public ModelAndView allStudentsPage(Model model, HttpServletRequest request) throws SQLException { 
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if(newConnection == null) startDBConnection();
		List<User> studentList = sqlController.getAllStudentList();
		return new ModelAndView("studentListPage","studentList",studentList);  
	}

	@RequestMapping( value="/getstudentprojects",method = RequestMethod.POST)
	public ModelAndView getAllProjectStudentList(@RequestParam(value="studentID") int studentID, 
			Model model, HttpServletRequest request) throws SQLException { 
		System.out.println("student ID is :" + studentID);
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if(newConnection == null) startDBConnection();
		//If the student already have a final project approved, then we will only show that project to him
		Project finalProject = sqlController.getFinalProjectStudent(studentID);
		if(finalProject == null) {
			model.addAttribute("message", "Student has not final project");
			return new ModelAndView("errorPage");
		}
		else model.addAttribute("noFinalProject", false);
		model.addAttribute("user", new User());//passing the user allows to return any user value from the frontend
		model.addAttribute("finalProject", finalProject);
		return new ModelAndView("allStudentProjectPage");
	}

	@RequestMapping( value="/previousyearprojects",method = RequestMethod.GET)
	public ModelAndView previousYear(Model model, HttpServletRequest request) throws SQLException { 
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if(newConnection == null) startDBConnection();
		List<Integer> yearList = sqlController.getAllYearOfProjects();
		if(yearList.isEmpty()) {
			model.addAttribute("message", "Year list it is empty");
			return new ModelAndView("errorPage"); 
		}
		model.addAttribute("yearList", yearList);
		return new ModelAndView("yearListProjectPage");
	}

	@RequestMapping( value="/seeprojectbyyear",method = RequestMethod.POST)
	public ModelAndView seeProviousYearProject(Model model, HttpServletRequest request, int year) throws SQLException { 
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		System.out.println("The year that you choose is: " + year);
		if(newConnection == null) startDBConnection();
		List<Project> projectList = sqlController.getProjectsByYear(year);
		if(projectList.isEmpty()) {
			model.addAttribute("message", "We could not found any project for the year " + year + " please contact the system administrator");
			return new ModelAndView("errorPage"); 
		}
		model.addAttribute("projectList", projectList);
		model.addAttribute("year", year);
		return new ModelAndView("previousyearprojectlist");
	}

	@RequestMapping( value="/logout",method = RequestMethod.GET)
	public ModelAndView logout(Model model, HttpServletRequest request) throws SQLException { 
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		//if(session.getAttribute("userID") == null) return login(request);
		if(newConnection == null) startDBConnection();
		session.invalidate();
		return login(request);
	}
}