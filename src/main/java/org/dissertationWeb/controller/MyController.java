package org.dissertationWeb.controller;

import java.sql.Connection;
import java.sql.SQLException;
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

/**
 * Main controller class, here I only have method that contains the @RequestMapping or in other words a link between the browser and java
 * those methods are the one that are populating the front end with the data necessary.
 * @author ismael
 *
 */
@Controller
public class MyController {
	@Autowired 
	private HttpSession httpSession;

	private Connection newConnection;

	private SQLController sqlController;
	//private int userLoginID;

	/**
	 * Main method that start the connection with the DB and start the SQLController class which contains all the SQL related methods
	 */
	private void startDBConnection() {
		//Create a connection to the DB as soon as we need it
		DBConnection connect = new DBConnection();
		newConnection = connect.connect();
		sqlController = new SQLController();//object to control all the SQL activities
	}

	/**
	 * This method it is the one that I use to load the homePage view
	 * to call this method you need to write / or /home 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "/", "/home" }, method = RequestMethod.GET)
	public ModelAndView homePage(HttpServletRequest request) {
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if(newConnection == null) startDBConnection();
		return new ModelAndView("homePage");
	}

	/**
	 * This is the method that is load when /login it is wrote, this method it is loading the loginPage view
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/login" , method = RequestMethod.GET)
	public ModelAndView login(HttpServletRequest request) {
		//redirect to home page if you are login and try to login again
		if(getSession(request) == null) return homePage(request);
		if(newConnection == null) startDBConnection();
		return new ModelAndView("loginPage","command",new User());
	}

	/**
	 * LoginCheck it is call from the loginPage view, what it is doing is check if the information entered in the view
	 * username and password are correct, if they are correct a session it is created with the user information
	 * if not, an errorPage view it is loaded
	 * @param user I created an user object with the data obtained from the loginPage view
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/logincheck",method = RequestMethod.POST)  
	public ModelAndView checkLogin(@ModelAttribute("user")User user, ModelMap model, HttpServletRequest request){ 
		//redirect to home page if you are login and try to login again
		if(getSession(request) == null) return homePage(request);
		if(newConnection == null) startDBConnection();
		//user it is send to the loginCheck method in SQLController to confirm if the data entered is right, it is returning the userID if success
		int userID = sqlController.loginCheck(user);
		if(userID != 0) {//return userID 0 if fails that is why I check if userID is not 0
			User userLogged = sqlController.getUser(userID);
			createSession(request,userLogged.getUserID(), userLogged.getUserType());//createSession method
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
	
	/**
	 * Method to get the session
	 * @param request
	 * @return
	 */
	public HttpSession getSession(HttpServletRequest request) {
		HttpSession session = request.getSession() ;
		if(session!= null) {
			return request.getSession();
		}
		return null;
	}

	/**
	 * when user visit /contactus address in the page,this method it is called, this method it is getting the data from the module administrator
	 * and show it in the page
	 * @param model
	 * @param request
	 * @return
	 * @throws SQLException
	 */
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
		}else { //if retrieving the data fail, then an errorPage it is load
			model.addAttribute("message", "Error retrieving information from the DB, please try again later");//I am passing a message to the error page
			return new ModelAndView("errorPage");
		}
	}
	
	/**
	 * This method it is call when someone write /projectlist in the browser
	 * the function of the method it is to load all the project that are visible, not choose by anyone else, are from the actual year
	 * are approved by the module coordinator
	 * All of those projects are save in a list of projects and passed to the view projectListPage which it is displaying them
	 * using modals
	 * @param model
	 * @param request
	 * @return
	 * @throws SQLException
	 */
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

	/**
	 * Similar to the previous method, but in this case this method it is only use by the module coordinator
	 * this method will show a list of the project that need to be approved
	 * To access this method user need to visit the address /projectlisttoapprove
	 * @param model
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping(value = { "/projectlisttoapprove" }, method = RequestMethod.GET)
	public ModelAndView projectListToApprovePage(Model model, HttpServletRequest request) throws SQLException {
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if(newConnection == null) startDBConnection();
		//I am getting projects that are not approved but are visible (false and true)
		List<Project> projectList = sqlController.getProjectListVisibleAnDApprove(false,true);
		//Here I am getting the user
		User user = sqlController.getUser((Integer)session.getAttribute("userID"));
		//And passing the userType to the view, in this way I can decide which part of the view I want to show to the user
		model.addAttribute("userType", user.getUserType());
		if(projectList.isEmpty()) {//if no projects, then do not bother to load the view
			model.addAttribute("message", "You do not have any project to approve");
			return new ModelAndView("errorPage");
		}
		return new ModelAndView("projectListtoapprovePage","projectList",projectList);  
	}

	/**
	 * After the module coordinator approve a project, this is the method that it is call, this method
	 * it is updating the information of the project on the DB, making the waitingtobeapproved boolean false
	 * and then loading all the data of the project into the model to be display to the user, letting the user know
	 * which project he or she approved
	 * @param projectID
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping( value="/approveproject",method = RequestMethod.POST)
	public ModelAndView approveprojectPage(@RequestParam(value="projectID") int projectID, 
			Model model,HttpServletRequest request) { 
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);

		if(sqlController.approveProject(projectID)!= 0) {
			Project project = new Project();
			project = sqlController.getProject(projectID);
			//Area of populating the model view
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
			//End of area of populating mode view
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

	/**
	 * Method that it is use to load the view to create new projects, it is called when /newproject it is wrote in the browser
	 * It is loading the newprojectPage view which contains a formulary to enter the data for a new project
	 * @param model
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping( "/newproject")
	public ModelAndView newprojectPage(Model model, HttpServletRequest request) throws SQLException {  
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		return new ModelAndView("newprojectPage","command",new Project());  
	}

	/**
	 * Method that user will use to propose a project to the module coordinator
	 * It is loading the projectproposalPage view which contains a formulary to write the information of the project
	 * The method it is load when /projectproposal it is load in the browser
	 * @param model
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping( "/projectproposal")
	public ModelAndView newprojectProposalPage(Model model, HttpServletRequest request) throws SQLException {  
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		return new ModelAndView("projectproposalpage","command",new Project());  
	}

	/**
	 * Method load after the projectproposalpage send the information of a new project
	 * This method it is creating an email with the project information and send it to the module coordinator
	 * @param project
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/sendProposal",method = RequestMethod.POST)  
	public ModelAndView saveProposal(@ModelAttribute("project") Project project, 
			Model model, HttpServletRequest request){  
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if(newConnection == null) startDBConnection();
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

	/**
	 * This method load the view editprojectPage which it is getting the projectID and passing the project to the front end
	 * populating the formulary, in the view, when you are done editing the project you click save that will load the /saveEdit method
	 * @param projectID
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping( value="/edit",method = RequestMethod.POST)
	public ModelAndView editprojectPage(@RequestParam(value="projectID") int projectID, 
			HttpServletRequest request) throws SQLException { 
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		Project project = new Project();
		project = sqlController.getProject(projectID);
		if(newConnection == null) startDBConnection();
		//I am populating here the view so the user can modify the project, if I need to add more data to the form I should
		//update the constructor on project class
		return new ModelAndView("editprojectPage","command",new Project(project.getProjectID(),project.getYear(),
				project.getTitle(),project.getTopics(),project.getCompulsoryReading(),project.getDescription()));  
	}

	/**
	 * This method it is getting a projectID and calling the updateProject from SQLController, this will update the visibility
	 * of the project to false, in this way the project will not be seeing by students, but still will be accesible from the project
	 * list of lecturer
	 * @param projectID
	 * @param model
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping( value="/remove",method = RequestMethod.POST)
	public ModelAndView removeprojectPage(@RequestParam(value="projectID") int projectID,
			Model model, HttpServletRequest request) throws SQLException { 
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if(newConnection == null) startDBConnection();
		System.out.println("test projectID " + projectID);
		Project project = new Project();
		project = sqlController.getProject(projectID);
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

	/**
	 * This is the method called by edit, here I am populating a summary view with the information of the project and 
	 * saving the updated project within the DB
	 * @param project
	 * @param model
	 * @param request
	 * @return
	 */
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

	/**
	 * This method it is called from the view of newproject, here I am saving the new project in the DB
	 * @param project
	 * @param model
	 * @param request
	 * @return
	 */
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

	/**
	 * This method it is loading all the events in the schedule list
	 * @param model
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping(value = { "/checklistlist" }, method = RequestMethod.GET)
	public ModelAndView checklistListPage(Model model, HttpServletRequest request) throws SQLException {
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if(newConnection == null) startDBConnection();
		List<CheckList> checklistList = sqlController.getCheckListList(true);
		//This second list will not be see by students, but in order to save space I am using the same view, but based on the userType
		//you will be able to see or not this list
		List<CheckList> checklistListNotApproved = sqlController.getCheckListList(false);

		//Keep this since I need to check if user is admin or not for now
		//HttpSession session = getSession(request);
		User user = sqlController.getUser((Integer)session.getAttribute("userID"));
		model.addAttribute("userType", user.getUserType());
		model.addAttribute("checklistListNotApproved", checklistListNotApproved);
		//I want to pass the size since based on the size the view will be different (if size is 0 do not load the list for not approved)
		model.addAttribute("notapprovedsize", checklistListNotApproved.size());
		return new ModelAndView("checklistListPage","checklistList",checklistList);  
	}

	/**
	 * Method that it is calling the editchecklistPage and passing the data of the checklist to be show in the front end
	 * In the view after you modify the data, you will call to saveEditChecklist
	 * @param checklistID
	 * @param model
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping( value="/editChecklist",method = RequestMethod.POST)
	public ModelAndView editChecklistPage(@RequestParam(value="checklistID") int checklistID, 
			Model model, HttpServletRequest request) throws SQLException { 
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		CheckList checkList = new CheckList();
		checkList = checkList.getchecklist(checklistID);
		if(newConnection == null) startDBConnection();
		//I am populating here the view so the user can modify the checklist, if I need to add more data to the form I should
		//update the constructor on checklist class
		model.addAttribute("checklistID", checklistID); //passing checklistID to the frontend
		return new ModelAndView("editchecklistPage","command",new CheckList(checkList.getCheckListID(), checkList.getDate(),
				checkList.getEventName(), checkList.getPlace(), checkList.getDescription()));  
	}

	/**
	 * Method that it is call when you remove a checklist from the schedule, in this case the front end will send
	 * to this method the checklistID this ID it is passed to the method updateChecklist and a boolean
	 * In this way I am using the same method to make invisible a checklist or to make it visible again
	 * @param checklistID
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping( value="/removeChecklist",method = RequestMethod.POST)
	public ModelAndView removeChecklistPage(@RequestParam(value="checklistID") int checklistID, 
			HttpServletRequest request) throws SQLException { 
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if(newConnection == null) startDBConnection();
		//false here means "make whatever it is linked with this ID invisible"
		sqlController.updateChecklist(checklistID,false);
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
	/**
	 * This method it is loading the whole list (visible or not) of checklist and showing them on the front end view checklistListPage
	 * @param checklistID
	 * @param model
	 * @param request
	 * @return
	 * @throws SQLException
	 */
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

	/**
	 * Method that it is call from the front end view editchecklistPage, it is getting a checklist object that it is save in the DB
	 * using the method saveEditChecklist from SQLController
	 * After this I am populating the front end view checklistViewPage with the data of the checklist edited
	 * @param checklist
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/saveEditChecklist",method = RequestMethod.POST)  
	public ModelAndView saveEditChecklist(@ModelAttribute("checklist") CheckList checklist, 
			Model model, HttpServletRequest request){ 
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
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
			project = sqlController.getProject(projectID);
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
		int count = sqlController.getTotalInterestProject((Integer)session.getAttribute("userID"), true);
		//I am using this second counter since I can have the situation of not having any project with interest that are visible
		int countNotVisible = sqlController.getTotalInterestProject((Integer)session.getAttribute("userID"), false);
		if(count == 5) {
			model.addAttribute("message", "You already have 5 projects registered on your name, "
					+ "please remove one before you add new ones"); 
			return new ModelAndView("errorPage");
		}
		if(count == 0 && countNotVisible == 0){
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
			project = sqlController.getProject(projectID);
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

	/**
	 * Method that it is call when a lecturer remove interest from a project
	 * In this case since I do not want to remove anything from the DB I am changing the visibility of that selection
	 * to false, so a lecturer will not be able to see that interest anymore, but student could still update the interest
	 * without needing to re apply to the project
	 * @param projectID
	 * @param user
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping( value="/removeinterest",method = RequestMethod.POST)
	public ModelAndView removeInterest(@RequestParam(value="projectID") int projectID, 
			User user, HttpServletRequest request) throws SQLException { 
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if(newConnection == null) startDBConnection();
		sqlController.updateInterestProject(projectID,user.getUserID(),false);
		//sqlController.updateInterestFinalProject(projectID,user.getUserID(),false);
		//I am using same page since the final message for project or checklist is the same
		ApplicationContext context =
				new ClassPathXmlApplicationContext("Spring-Mail.xml");

		MailMail mm = (MailMail) context.getBean("mailMail");
		Project project = new Project();
		project = sqlController.getProject(projectID);
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
	/**
	 * Method that it is call when a lecturer remove interest from a project
	 * In this case since I do not want to remove anything from the DB I am changing the visibility of that selection
	 * to false, so a lecturer will not be able to see that interest anymore, but student could still update the interest
	 * without needing to re apply to the project
	 * @param projectID
	 * @param user
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping( value="/removeinterestfinal",method = RequestMethod.POST)
	public ModelAndView removeInterestFinal(@RequestParam(value="projectID") int projectID, 
			User user, HttpServletRequest request) throws SQLException { 
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if(newConnection == null) startDBConnection();
		//updateInterestProject(projectID,user.getUserID(),false);
		sqlController.updateInterestFinalProject(projectID,user.getUserID(),false);
		//I am using same page since the final message for project or checklist is the same
		ApplicationContext context =
				new ClassPathXmlApplicationContext("Spring-Mail.xml");

		MailMail mm = (MailMail) context.getBean("mailMail");
		Project project = new Project();
		project = sqlController.getProject(projectID);
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
	/**
	 * Method that it is call when a student remove interest from a project
	 * In this case since I do not want to remove anything from the DB I am changing the visibility of that selection
	 * to false, so a lecturer will not be able to see that interest anymore, but student could still update the interest
	 * without needing to re apply to the project
	 * @param projectID
	 * @param user
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping( value="/removeinterestStudent",method = RequestMethod.POST)
	public ModelAndView removeInterestStudent(@RequestParam(value="projectID") int projectID, 
			HttpServletRequest request, User user) throws SQLException { 
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if(newConnection == null) startDBConnection();
		//updateInterestFinalProject(projectID,user.getUserID(),false);
		User student = sqlController.getUser((Integer)session.getAttribute("userID"));
		sqlController.updateInterestProject(projectID,student.getUserID(),false);
		ApplicationContext context =
				new ClassPathXmlApplicationContext("Spring-Mail.xml");

		MailMail mm = (MailMail) context.getBean("mailMail");
		Project project = new Project();
		project = sqlController.getProject(projectID);

		String messageLecturer = "Student " + student.getUsername() + " had cancel the interest in your project " + project.getTitle();
		String messageStudent = "You remove the interest in the project " + project.getTitle();
		//One message is for the lecturer
		mm.sendMail("ismael.sanchez.leon@gmail.com","tatowoke@gmail.com","Someone register in one of your proejcts",messageLecturer);
		//Another message it is send to the student
		mm.sendMail("ismael.sanchez.leon@gmail.com","tatowoke@gmail.com","Someone register in one of your proejcts",messageStudent);
		//I am using same page since the final message for project or checklist is the same
		return new ModelAndView("projectRemovedPage");
	}

	/**
	 * This method it is taking care of showing the project that a student show interest visible or invisible ones
	 * And if the student have a final project approved, will show only the final project
	 * @param model
	 * @param request
	 * @return
	 * @throws SQLException
	 */
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

	/**
	 * This method it is showing all the projects that a lecturer had upload to the applications, visible or invisible ones
	 * @param model
	 * @param request
	 * @return
	 * @throws SQLException
	 */
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
	
	/**
	 * This method it is showing the list of projects that a student show interest based on the lecturer, or in other words this method
	 * it is creating a list of projects per lecturer that have interest show by students
	 * @param model
	 * @param request
	 * @return
	 * @throws SQLException
	 */
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

	/**
	 * This method it is creating a list of projects of one lecturer that are not visible.
	 * After this the list it is passed to the front end view notVisibleProjectPage
	 * @param model
	 * @param request
	 * @return
	 * @throws SQLException
	 */
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

	/**
	 * This method it is receiving a projectID and changing its visibility to true, or in other words visible to students and other lecturers
	 * @param projectID
	 * @param model
	 * @param request
	 * @return
	 * @throws SQLException
	 */
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
		project = sqlController.getProject(projectID);
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

	/**
	 * This user is the one passed from the previous method (projectwithinterestlist)
	 * This user should be populate with the userID from the project that we choose to approve
	 * This method it is use to accept a student request of a project, or in other words, giving the final project to that student
	 * @param projectID
	 * @param user
	 * @param model
	 * @param request
	 * @return
	 * @throws SQLException
	 */
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
			project = sqlController.getProject(projectID);
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
			project = sqlController.getProject(projectID);

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

	/**
	 * This method it is creating a list of all the students within the system
	 * @param model
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping( value="/studentlist",method = RequestMethod.GET)
	public ModelAndView allStudentsPage(Model model, HttpServletRequest request) throws SQLException { 
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if(newConnection == null) startDBConnection();
		List<User> studentList = sqlController.getAllStudentList();
		return new ModelAndView("studentListPage","studentList",studentList);  
	}

	/**
	 * This method it is returning the final project (if any) that the student have, the idea of this is allow to the
	 * module coordinator to change the interest of a project after been approved by a lecturer 
	 * @param studentID
	 * @param model
	 * @param request
	 * @return
	 * @throws SQLException
	 */
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

	/**
	 * Method that it is showing in a list all the years that the DB contains within the projects, the idea is to have a menu to choose
	 * a year and after choose that year all the project with that year will be show
	 * @param model
	 * @param request
	 * @return
	 * @throws SQLException
	 */
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

	/**
	 * This method will pass, based on the year, a list of projects within that year to the front end view previousyearprojectlist
	 * This view only is accessible by module coordinator and does not allow to register or modify project, the only thing that you can do
	 * is check information within the projects
	 * @param model
	 * @param request
	 * @param year
	 * @return
	 * @throws SQLException
	 */
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

	/**
	 * This method it is destroying the session that the user was using and then returning the user to the login page
	 * @param model
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping( value="/logout",method = RequestMethod.GET)
	public ModelAndView logout(Model model, HttpServletRequest request) throws SQLException { 
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		//if(session.getAttribute("userID") == null) return login(request);
		if(newConnection == null) startDBConnection();
		session.invalidate();
		return login(request);
	}
	
	/**
	 * This method is taking care that when a lecturer enter to his personal list you will have in the top 
	 * all the lecturer projects without interest and on the bottom the lecturer project which have interest
	 * @param projectList
	 * @param projectWithInterest
	 * @return
	 */
	/*public List<Project> listComparer(List<Project>projectList, List<Project>projectWithInterest) {
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
	}*/
}