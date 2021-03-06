package org.dissertationWeb.controller;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.dissertationWeb.classes.CheckList;
import org.dissertationWeb.classes.DBConnection;
import org.dissertationWeb.classes.FileBucket;
import org.dissertationWeb.classes.MailMail;
import org.dissertationWeb.classes.Project;
import org.dissertationWeb.classes.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.opencsv.CSVReader;

/**
 * Main controller class, here I only have method that contains the @RequestMapping or in other words a link between the browser and java
 * those methods are the one that are populating the front end with the data necessary 
 * or methods directly related with the method mentioned before
 * @author Ismael
 *
 */
@Controller
@EnableScheduling
public class MyController {
	@Autowired 
	private HttpSession httpSession;

	private Connection newConnection;

	private SQLController sqlController;

	//Constant to control user type
	public static final int LECTURER = 1;
	public static final int STUDENT = 2;
	public static final int COORDINATOR = 3;

	//Constant for the contact us section to specify the department
	public static final String DEPARTMENT = "Computer Science";

	public int actualYear;//Actual year it is use all around the application to tell SQLController which year for projects, event, user load 

	/**
	 * Main method that start the connection with the DB and start the SQLController class which contains all the SQL related methods
	 */
	@EventListener(ContextRefreshedEvent.class) //with this I am saying that as soon as I start the web application this method MUST be called first
	private void startDBConnection() {
		//Create a connection to the DB as soon as we need it
		DBConnection connect = new DBConnection();
		newConnection = connect.connect();
		sqlController = new SQLController();//object to control all the SQL activities
		actualYear = sqlController.getActualYear();//I am updating the year from SQLController
	}

	/**
	 * Method that checks if the DB connection is still ON, if is not then will catch the exception and restart the connection to the DB
	 */
	public void checkDBConnection() {
		try {
			//if is close will throw one of this two exception, in this case I will catch it and restart the connection
			newConnection.isClosed();
		}catch(com.mysql.jdbc.exceptions.jdbc4.CommunicationsException e) {
			startDBConnection();
		} catch (SQLException e) {
			startDBConnection();
		}
	}

	/**
	 * Method that is removing the element that share from one event list with another
	 * @param checklistList
	 * @param newListList
	 */
	public void removeDuplicateFromEventList(List<CheckList> list1, List<CheckList> list2) {
		for(int i = 0; i < list1.size(); i++) {
			for(int j = 0; j < list2.size(); j++) {
				if(list1.get(i).getCheckListID() == list2.get(j).getCheckListID()) {                 
					list1.remove(list1.get(i));	
					i--;
					if(i < 0) i = 0;
				}
			}
		}
	}

	/**
	 * Method that is removing the element that share from one project list with another
	 * @param checklistList
	 * @param newListList
	 */
	public void removeDuplicateFromProjectList(List<Project> list1, List<Project> list2) {
		for(int i = 0; i < list1.size(); i++) {
			for(int j = 0; j < list2.size(); j++) {
				if(list1.get(i).getProjectID() == list2.get(j).getProjectID()) {                 
					list1.remove(list1.get(i));	
					i--;
					if(i < 0) i = 0;
				}
			}
		}
	}

	/**
	 * Method to keep connection to the DB alive
	 * It would be better if the inactivity time will be higher in the configuration of the DB
	 * but since I am not allowed to modify this file this was the only suitable solution that I
	 * found.
	 * @throws SQLException
	 */
	@Scheduled(fixedRate = 1800000 )//check every 30 minutes
	public void keepConnection() throws SQLException {
		sqlController.keepConnectionAlive();
	}
	
	/**
	 * Automatic method that checks every Friday at 17:59:59 if we have new events in the schedule coming within one week
	 * If we have any events coming, an email will be send to all the students if not, nothing will happens
	 */
	@Scheduled(cron = "59 59 17 * * FRI")
	public void checkSchedule() {
		checkDBConnection();
		//I am getting all the events coming within a week
		List<CheckList> checklistList = sqlController.checkScheduleTime();
		if(!checklistList.isEmpty()) {
			@SuppressWarnings("resource")
			ApplicationContext context =
			new ClassPathXmlApplicationContext("Spring-Mail.xml");
			MailMail mm = (MailMail) context.getBean("mailMail");
			String title = "";
			String info = "";
			String place = "";
			String startHour = "";
			String endHour = "";
			String divider = "";
			String finalTable = "";

			for (CheckList c : checklistList) {
				if(c != null) {
					title = " Event title: " + title + c.getEventName() + " \n";
					info = " Event description: " + info + c.getDescription() + " \n";
					place = " Event place: " + place + c.getPlace() + " \n";
					startHour = " Start time: " + startHour + c.getHour() + " \n";
					endHour = " End time: " + endHour + c.getEndHour() + " \n";
					divider = "*******************************************";
					finalTable = finalTable + title + info + place + startHour + endHour + divider + " \n";
				}
			}
			mm.sendAutomaticEmailSchedule("These are the events coming this week \n" + finalTable, "ismael.sanchez.leon@gmail.com",
					"iss00009uos@gmail.com", "New events coming this week");	
		}
	}

	/**
	 * This method it is the one that I use to load the homePage view
	 * to call this method you need to write / or /home 
	 * @param request
	 * @return
	 * @throws SQLException 
	 */
	@RequestMapping(value = { "/", "/home" }, method = RequestMethod.GET)
	public ModelAndView homePage(HttpServletRequest request) throws SQLException {
		checkDBConnection(); //check if connection is still ON
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		return new ModelAndView("homePage");
	}

	/**
	 * This is the method that is load when /login it is wrote, this method it is loading the loginPage view
	 * @param request
	 * @return
	 * @throws SQLException 
	 */
	@RequestMapping(value = "/login" , method = RequestMethod.GET)
	public ModelAndView login(HttpServletRequest request) throws SQLException {
		checkDBConnection(); //check if connection is still ON
		//redirect to home page if you are login and try to login again
		if(getSession(request) == null) return homePage(request);
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
	 * @throws SQLException 
	 */
	@RequestMapping(value="/logincheck",method = RequestMethod.POST)  
	public ModelAndView checkLogin(@ModelAttribute("user")User user, Model model, HttpServletRequest request) throws SQLException{ 
		checkDBConnection(); //check if connection is still ON
		if(getSession(request) == null) return homePage(request);
		//user it is send to the loginCheck method in SQLController to confirm if the data entered is right, it is returning the userID if success
		int userID = sqlController.loginCheck(user);
		if(userID != 0) {//return userID 0 if fails that is why I check if userID is not 0
			User userLogged = sqlController.getUser(userID);
			createSession(request,userLogged.getUserID(), userLogged.getUserType(), userLogged.getYear());//createSession method
			//I am generating the welcome message to be shown after be login here
			model.addAttribute("welcomeMessage", "Welcome back: ");
			model.addAttribute("username", userLogged.getUsername());
			//The welcome message it is passed to the homePage here
			return new ModelAndView("homePage");
		}else {
			model.addAttribute("message", "You are not logged in, please go to login page and enter your credentials");
			return new ModelAndView("errorPage");
		}
	}
	/**
	 * I have a second logincheck since I can have two possible visits to logincheck one is with POST and that is the one when you
	 * post the data for the formulary to the DB and the other is the GET, this second one happens when user refresh logincheck page or
	 * try to access to logincheck page writing the address in the browser.
	 * With this method I am avoiding an error that stop the running of the application and is only redirecting you to the login
	 * if you still did not login or to homepage if you are already login in the application.
	 * @param user
	 * @param model
	 * @param request
	 * @return
	 * @throws SQLException 
	 */
	@RequestMapping(value="/logincheck",method = RequestMethod.GET)  
	public ModelAndView checkLoginGET(@ModelAttribute("user")User user, Model model, HttpServletRequest request) throws SQLException{ 
		checkDBConnection(); //check if connection is still ON
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		model.addAttribute("message", "You have been redirected as the address entered was not correct");
		return new ModelAndView("homePage");
	}

	/**
	 * Method that create the session and set as attribute in the session the userID and the maximum inactive time to 60 seconds
	 * @param request
	 * @param userID
	 * @throws SQLException 
	 */
	public void createSession(HttpServletRequest request, int userID, int userType, int year) throws SQLException {
		checkDBConnection(); //check if connection is still ON
		HttpSession session = request.getSession() ;
		int numberOfProject = sqlController.numberOfProjectsInDB();
		int numberOfEvents = sqlController.numberOfEventsInDB();
		//I could have a class that keep the value of the count, old and new and save the object of that class in the session
		//But I think that this call are not that complex to the DB and will not take too many resources
		int oldNumberOfProject = sqlController.getProjectCountDB(userID);
		int oldNumberOfEvent = sqlController.getEventCountDB(userID);
		session.setAttribute("userID", userID);
		session.setAttribute("userType", userType);
		session.setAttribute("yearUser", year);
		session.setAttribute("projectNum", numberOfProject);
		session.setAttribute("eventNum", numberOfEvents);
		session.setAttribute("oldProjectNum", oldNumberOfProject);
		session.setAttribute("oldEventNum", oldNumberOfEvent);
		session.setMaxInactiveInterval(1800);//session will expired if user is inactive for 30 minutes
	}

	/**
	 * Method to get the session
	 * @param request
	 * @return
	 * @throws SQLException 
	 */
	public HttpSession getSession(HttpServletRequest request) throws SQLException {
		checkDBConnection(); //check if connection is still ON
		HttpSession session = request.getSession() ;
		if(session!= null) {//if session exist, then return the session if not return null
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
		checkDBConnection(); //check if connection is still ON
		int userID = sqlController.contacPage();
		if(userID != 0) {
			User admin = sqlController.getUser(userID);
			//Passing the data to the model
			model.addAttribute("name", admin.getUsername());
			model.addAttribute("department", DEPARTMENT);//This is hardcode as a constant since for now this web application is only for CS
			model.addAttribute("email", admin.getEmail());
			return new ModelAndView("contactusPage");
		}else { //if retrieving the data fail, then an error page it is load
			//I am passing a message to the error page
			model.addAttribute("message", "Error retrieving information from the database, please try again later");
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
		checkDBConnection(); //check if connection is still ON
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		int projectNum = (Integer)session.getAttribute("projectNum");
		int oldProjectNum = (Integer)session.getAttribute("oldProjectNum");
		List<Project> projectList = sqlController.getProjectListVisibleAnDApprove(true,true);
		List<Project> newProjectList = sqlController.getProjectListVisibleAnDApprove(true,true,oldProjectNum,projectNum);
		//I am only interested to do this if I have elements, if not then ignore the sorting, no point
		if(!newProjectList.isEmpty()) {
			removeDuplicateFromProjectList(projectList, newProjectList);
		}
		User user = sqlController.getUser((Integer)session.getAttribute("userID"));//getting userID from the session
		model.addAttribute("userType", user.getUserType());
		model.addAttribute("studentYear", user.getYear());
		model.addAttribute("actualYear", actualYear);
		model.addAttribute("newProjectList", newProjectList);
		request.getSession().setAttribute("previousURL", request.getRequestURL());
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
		checkDBConnection(); //check if connection is still ON
		if((Integer)session.getAttribute("userType") != COORDINATOR) {
			model.addAttribute("message", "You have been redirected to the Home Page as you have tried to access a restricted area");
			return new ModelAndView("homePage");
		}
		//I am getting projects that are not approved but are visible (false and true)
		List<Project> projectList = sqlController.getProjectListVisibleAnDApprove(false,true);
		//Here I am getting the user
		User user = sqlController.getUser((Integer)session.getAttribute("userID"));
		request.getSession().setAttribute("previousURL", request.getRequestURL());
		//And passing the userType to the view, in this way I can decide which part of the view I want to show to the user
		model.addAttribute("userType", user.getUserType());
		if(projectList.isEmpty()) {//if no projects, then do not bother to load the view
			model.addAttribute("message", "You do not have any projects to approve");
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
	 * @throws SQLException 
	 */
	@RequestMapping( value="/approveproject",method = RequestMethod.POST)
	public ModelAndView approveprojectPage(@RequestParam(value="projectID") int projectID, 
			Model model,HttpServletRequest request) throws SQLException { 
		checkDBConnection(); //check if connection is still ON
		if(projectID == 0) {
			model.addAttribute("message", "Error loading the page");
			return new ModelAndView("errorPage");
		}
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if((Integer)session.getAttribute("userType") != COORDINATOR) {
			model.addAttribute("message", "You have been redirected to the Home Page as you have tried to access a restricted area");
			return new ModelAndView("homePage");
		}
		if(sqlController.approveProject(projectID)!= 0) {//if I am getting 0, that means error
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
			@SuppressWarnings("resource")
			ApplicationContext context =
			new ClassPathXmlApplicationContext("Spring-Mail.xml");
			MailMail mm = (MailMail) context.getBean("mailMail");
			if(mm.sendAutomaticEmail(actualUser, "Your project has been approved", "ismael.sanchez.leon@gmail.com",
					"iss00009uos@gmail.com", "Approved project")){
				//Final version will be sending an email to the lecturer letting him now that the project had been approved
				//(mm.sendAutomaticEmail(actualUser, "Your project has been approved", actualUser.getEmail(),
				//"iss00009uos@gmail.com", "You approved the project " + project.getTitle())
				model.addAttribute("previousPage", session.getAttribute("previousURL"));
				return new ModelAndView("projectPage","project",model);//will display object data 
			}else {
				model.addAttribute("message", "An error happened while trying to send the automatic email");
				//I am using the errorPage since I only want to show the message on the screen without create a new view
				return new ModelAndView("errorPage");
			}			
		}else {
			model.addAttribute("message", "An error happened while trying to approve this project");
			return new ModelAndView("errorPage");//I am using the errorPage since I only want to show the message on the screen without create a new view
		}
	}

	/**
	 * This method is a copy of the previous method since I need to take care of users who write the address of the method on the browser
	 * Since previous method is a POST if I tried to access as a GET (writing the address on the browser) I will get an error
	 * In this way I do not get the error, but instead I am redirected to homepage
	 * @param project
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/approveproject",method = RequestMethod.GET)  
	public ModelAndView approveProjectGet(Model model, HttpServletRequest request) throws SQLException {  
		model.addAttribute("message", "You have been redirected as the address entered was not correct");
		return new ModelAndView("homePage");//I am using the errorPage since I only want to show the message on the screen without create a new view 
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
		checkDBConnection(); //check if connection is still ON
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if((Integer)session.getAttribute("userType") == STUDENT) {
			model.addAttribute("message", "You have been redirected to the Home Page as you have tried to access a restricted area");
			return new ModelAndView("homePage");
		}
		//I am sending the actualYear to the front end since I am not allowing lectures to change the year. 
		//So year will be predefined and readonly.
		model.addAttribute("year",actualYear);
		//I am redirecting to homePage since I do not have a proper previous page to redirect if cancel happens
		model.addAttribute("previousPage", "home");
		return new ModelAndView("newprojectPage","command",new Project());  
	}

	/**
	 * Method that it is use to load the view to create new projects for the next year, 
	 * it is called when /newprojectnextyear it is wrote in the browser
	 * It is loading the newprojectPage view which contains a formulary to enter the data for a new project
	 * @param model
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping( "/newprojectnextyear")
	public ModelAndView newprojectNextYearPage(Model model, HttpServletRequest request) throws SQLException {  
		checkDBConnection(); //check if connection is still ON
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if((Integer)session.getAttribute("userType") == STUDENT) {
			model.addAttribute("message", "You have been redirected to the Home Page as you have tried to access a restricted area");
			return new ModelAndView("homePage");
		}
		//I am sending the actualYear to the front end since I am not allowing lectures to change the year. 
		//So year will be predefined and readonly.
		model.addAttribute("year",actualYear + 1);//I am increasing to next year
		//I am redirecting to homePage since I do not have a proper previous page to redirect if cancel happens
		model.addAttribute("previousPage", "home");
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
		checkDBConnection(); //check if connection is still ON
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if((Integer)session.getAttribute("userType") != STUDENT) {
			model.addAttribute("message", "You have been redirected to the Home Page as you have tried to access a restricted area");
			return new ModelAndView("homePage");
		}
		//I am sending the actualYear to the front end since I am not allowing lectures to change the year. 
		//So year will be predefined and readonly.
		model.addAttribute("year",session.getAttribute("yearUser"));
		return new ModelAndView("projectproposalpage","command",new Project());  
	}

	/**
	 * Method load after the projectproposalpage send the information of a new project
	 * This method it is creating an email with the project information and send it to the module coordinator
	 * @param project
	 * @param model
	 * @param request
	 * @return
	 * @throws SQLException 
	 */
	@RequestMapping(value="/sendProposal",method = RequestMethod.POST)  
	public ModelAndView saveProposal(@ModelAttribute("project") Project project, 
			Model model, HttpServletRequest request) throws SQLException{  
		if(project == null) {
			model.addAttribute("message", "Error loading the page");
			return new ModelAndView("errorPage");
		}
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		checkDBConnection(); //check if connection is still ON
		if((Integer)session.getAttribute("userType") != STUDENT) {
			model.addAttribute("message", "You have been redirected to the Home Page as you have tried to access a restricted area");
			return new ModelAndView("homePage");
		}
		//here, we are displaying project object to prove project has data  
		User user = sqlController.getUser((Integer)session.getAttribute("userID"));
		//Automatic email system
		ApplicationContext context =
				new ClassPathXmlApplicationContext("Spring-Mail.xml");
		MailMail mm = (MailMail) context.getBean("mailMail");
		String message = mm.createMessageProjectProposal(user.getUsername(), project.getDescription(),
				project.getCompulsoryReading(),project.getTitle(),project.getTopics());
		String emailFrom = user.getEmail(); //In the final version I should be getting the email from the user and send it to the coordinator
		mm.sendMail("ismael.sanchez.leon@gmail.com","iss00009uos@gmail.com","New project proposal from " + user.getUsername(),message);
		model.addAttribute("message", "Your proposal has been succesfully sent to the dissertation coordinator");
		return new ModelAndView("homePage"); 
	} 

	/**
	 * This method is a copy of the previous method since I need to take care of users who write the address of the method on the browser
	 * Since previous method is a POST if I tried to access as a GET (writing the address on the browser) I will get an error
	 * In this way I do not get the error, but instead I am redirected to homepage
	 * @param project
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/sendProposal",method = RequestMethod.GET)  
	public ModelAndView saveProposalGet( Model model, HttpServletRequest request){  
		model.addAttribute("message", "You have been redirected as the address entered was not correct");
		return new ModelAndView("homePage");//I am using the errorPage since I only want to show the message on the screen without create a new view 
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
			Model model, HttpServletRequest request) throws SQLException { 
		if(projectID == 0) {
			model.addAttribute("message", "Error loading the page");
			return new ModelAndView("errorPage");
		}

		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		Project project = new Project();
		project = sqlController.getProject(projectID);
		checkDBConnection(); //check if connection is still ON
		model.addAttribute("previousPage", session.getAttribute("previousURL"));
		//I am populating here the view so the user can modify the project, if I need to add more data to the form I should
		//update the constructor on project class
		return new ModelAndView("editprojectPage","command",new Project(project.getProjectID(),project.getYear(),
				project.getTitle(),project.getTopics(),project.getCompulsoryReading(),project.getDescription()));  
	}

	/**
	 * This method is a copy of the previous method since I need to take care of users who write the address of the method on the browser
	 * Since previous method is a POST if I tried to access as a GET (writing the address on the browser) I will get an error
	 * In this way I do not get the error, but instead I am redirected to homepage
	 * @param project
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/edit",method = RequestMethod.GET)  
	public ModelAndView editGet(Model model, HttpServletRequest request) throws SQLException {  
		return new ModelAndView("homePage");//I am using the errorPage since I only want to show the message on the screen without create a new view 
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
		if(projectID == 0) {
			model.addAttribute("message", "Error loading the page");
			return new ModelAndView("errorPage");
		}
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		checkDBConnection(); //check if connection is still ON
		Project project = new Project();
		project = sqlController.getProject(projectID);
		sqlController.updateProject(projectID, false);
		//I am populating here the view so the user can modify the project, if I need to add more data to the form I should
		//update the constructor on project class
		model.addAttribute("message", project.getTitle());
		model.addAttribute("previousPage", session.getAttribute("previousURL"));
		ApplicationContext context =
				new ClassPathXmlApplicationContext("Spring-Mail.xml");

		MailMail mm = (MailMail) context.getBean("mailMail");
		String message = "The project is now not visible";
		User user = sqlController.getUser((Integer)session.getAttribute("userID"));
		String lecturerEmail = user.getEmail();
		mm.sendMail("ismael.sanchez.leon@gmail.com","iss00009uos@gmail.com","Project removed from " + user.getUsername(),message);
		model.addAttribute("mainmessage", "You have made the project " + project.getTitle() + 
				" not visible to students ");
		model.addAttribute("secondmessage", " If you want to make the project visible again "
				+ "or edit it, go to your not visible project list");
		return new ModelAndView("projectRemovedPage");
	}

	/**
	 * This method is a copy of the previous method since I need to take care of users who write the address of the method on the browser
	 * Since previous method is a POST if I tried to access as a GET (writing the address on the browser) I will get an error
	 * In this way I do not get the error, but instead I am redirected to homepage
	 * @param project
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/remove",method = RequestMethod.GET)  
	public ModelAndView removeGet(Model model, HttpServletRequest request) throws SQLException {
		model.addAttribute("message", "You have been redirected as the address entered was not correct");
		return new ModelAndView("homePage");//I am using the errorPage since I only want to show the message on the screen without create a new view 
	} 

	/**
	 * This is the method called by edit, here I am populating a summary view with the information of the project and 
	 * saving the updated project within the DB
	 * @param project
	 * @param model
	 * @param request
	 * @return
	 * @throws SQLException 
	 */
	@RequestMapping(value="/saveEdit",method = RequestMethod.POST)  
	public ModelAndView saveEditProject(@ModelAttribute("project") Project project, 
			Model model, HttpServletRequest request) throws SQLException{ 
		if(project == null) {
			model.addAttribute("message", "Error loading the page");
			return new ModelAndView("errorPage");
		}
		//Since Year it is editable here, is better to take care than you did not change year to something less than actual year
		if(project.getYear() < actualYear) {
			model.addAttribute("message", "Year cannot be before the current year");
			return new ModelAndView("editprojectPage","command",new Project(project.getProjectID(),project.getYear(),
					project.getTitle(),project.getTopics(),project.getCompulsoryReading(),project.getDescription()));
		}
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		checkDBConnection(); //check if connection is still ON
		//I been having some issues with the booleans visible and waiting, so I rather prefer to check on the DB
		//And get the actual values than show data that is not right
		Project projectUpdated = sqlController.getProject(project.getProjectID());
		model.addAttribute("year",project.getYear());
		model.addAttribute("title",project.getTitle());
		model.addAttribute("topics",project.getTopics());
		model.addAttribute("compulsoryReading",project.getCompulsoryReading());
		model.addAttribute("description",project.getDescription());
		model.addAttribute("waitingapprove",projectUpdated.isWaitingToBeApproved());
		model.addAttribute("visible",projectUpdated.isVisible());
		User actualUser = sqlController.getUser((Integer)session.getAttribute("userID"));
		model.addAttribute("lecturerID",actualUser.getUserID());
		model.addAttribute("lecturername", actualUser.getUsername());
		model.addAttribute("lectureremail", actualUser.getEmail());
		switch(sqlController.saveEdit(project)) {
		case 0 :
			model.addAttribute("previousPage", session.getAttribute("previousURL"));
			ApplicationContext context =
					new ClassPathXmlApplicationContext("Spring-Mail.xml");

			MailMail mm = (MailMail) context.getBean("mailMail");
			String message = "Your project had been modified";
			User user = sqlController.getUser((Integer)session.getAttribute("userID"));
			String lecturerEmail = user.getEmail();
			mm.sendMail("ismael.sanchez.leon@gmail.com","iss00009uos@gmail.com","Modified project from " + user.getUsername(),message);
			return new ModelAndView("projectPage","project",model);//will display object data  
		case 1 : 
			model.addAttribute("message", "This project already exist in the database");
			return new ModelAndView("errorPage");

		case 2 : 
			model.addAttribute("message", "An error happened while updating this project, if this error continues please contact the system administrator");
			return new ModelAndView("errorPage");
		}
		model.addAttribute("message", "An error happened while saving your project, please try again later");
		return new ModelAndView("errorPage");
	}

	/**
	 * This method is a copy of the previous method since I need to take care of users who write the address of the method on the browser
	 * Since previous method is a POST if I tried to access as a GET (writing the address on the browser) I will get an error
	 * In this way I do not get the error, but instead I am redirected to homepage
	 * @param project
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/saveEdit",method = RequestMethod.GET)  
	public ModelAndView saveEditGet(Model model, HttpServletRequest request) throws SQLException {  
		model.addAttribute("message", "You have been redirected as the address entered was not correct");
		return new ModelAndView("homePage");//I am using the errorPage since I only want to show the message on the screen without create a new view 
	} 

	/**
	 * This method it is called from the view of newproject, here I am saving the new project in the DB
	 * @param project
	 * @param model
	 * @param request
	 * @return
	 * @throws SQLException 
	 */
	@RequestMapping(value="/save",method = RequestMethod.POST)  
	public ModelAndView save(@ModelAttribute("project") Project project, 
			Model model, HttpServletRequest request) throws SQLException{  
		if(project == null) {
			model.addAttribute("message", "Error loading the page");
			return new ModelAndView("errorPage");
		}
		//I am allowing the compulsory reading to be optional, if the lecturer do not add any reading then I will update the reading to 
		//no compulsory readings
		if(project.getCompulsoryReading().isEmpty() || project.getCompulsoryReading() == null) project.setCompulsoryReading("No compulsory readings");
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		checkDBConnection(); //check if connection is still ON   
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
		User actualUser = sqlController.getUser((Integer)session.getAttribute("userID"));
		model.addAttribute("lecturerID",actualUser.getUserID());
		model.addAttribute("lecturername", actualUser.getUsername());
		model.addAttribute("lectureremail", actualUser.getEmail());
		//model.addAttribute("projectID",project.getTitle());
		switch(sqlController.save(project, (Integer)session.getAttribute("userID"))) {
		case 0 :
			ApplicationContext context =
			new ClassPathXmlApplicationContext("Spring-Mail.xml");

			MailMail mm = (MailMail) context.getBean("mailMail");
			String message = mm.newOrApproveProjectMessage(actualUser.getUsername(),project.getTitle(), "created");
			String emailFrom = actualUser.getEmail(); //In the final version I should be getting the email from the user and send it to the coordinator
			mm.sendMail("ismael.sanchez.leon@gmail.com","iss00009uos@gmail.com","New project from " + actualUser.getUsername(),message);
			model.addAttribute("message", "Your project has been sent to the dissertation coordinator");
			return new ModelAndView("projectPage","project",model);//will display object data  
		case 1 : 
			model.addAttribute("message", "This project already exist in the database");
			return new ModelAndView("errorPage");

		case 2 : 
			try {
				if(sqlController.getLastProjectID() == 0) return new ModelAndView("errorPage","project",null);
				model.addAttribute("projectID",sqlController.getLastProjectID());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		model.addAttribute("message", "An error happened while saving your project, please try again later");
		return new ModelAndView("errorPage");
	}  

	/**
	 * This method is a copy of the previous method since I need to take care of users who write the address of the method on the browser
	 * Since previous method is a POST if I tried to access as a GET (writing the address on the browser) I will get an error
	 * In this way I do not get the error, but instead I am redirected to homepage
	 * @param project
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/save",method = RequestMethod.GET)  
	public ModelAndView saveGet(Model model, HttpServletRequest request) throws SQLException {  
		model.addAttribute("message", "You have been redirected as the address entered was not correct");
		return new ModelAndView("homePage");//I am using the errorPage since I only want to show the message on the screen without create a new view 
	} 

	/**
	 * If any of the three String variables are not null, that means that that is the value that the user request to look for
	 * @param searchValue
	 * @param lecturer
	 * @param technology
	 * @param title
	 * @param model
	 * @return
	 * @throws SQLException 
	 */
	@RequestMapping(value="/search",method = RequestMethod.POST)  
	public ModelAndView search(@RequestParam String searchValue, @RequestParam(required = false)
	String lecturer, @RequestParam(required = false) String technology, @RequestParam(required = false) String title,
	Model model, HttpServletRequest request) throws SQLException{ 
		checkDBConnection(); //check if connection is still ON
		//required false indicate that it is not compulsory to have it, so if I do not have, for example, a title, java will not complaint
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		//If user does not enter any search criteria
		if(searchValue == null) {
			model.addAttribute("message", "Please enter your search criteria in the search box");
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
			model.addAttribute("message", "Your search criteria does not return any results, please try something else");
			return new ModelAndView("errorPage");
		}
	}

	/**
	 * This method is a copy of the previous method since I need to take care of users who write the address of the method on the browser
	 * Since previous method is a POST if I tried to access as a GET (writing the address on the browser) I will get an error
	 * In this way I do not get the error, but instead I am redirected to homepage
	 * @param project
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/search",method = RequestMethod.GET)  
	public ModelAndView searchGet(Model model, HttpServletRequest request) throws SQLException {  
		model.addAttribute("message", "You have been redirected as the address entered was not correct");
		return new ModelAndView("homePage");//I am using the errorPage since I only want to show the message on the screen without create a new view 
	}

	/**
	 * Method to search student based on their name or email (both of them are unique) so this query will always
	 * return a single student
	 * @RequestParam(required = false) it is saying that the parameter is not compulsory so if it is not passed
	 * the method will not complaint
	 * @param searchValue
	 * @param name
	 * @param email
	 * @param model
	 * @param request
	 * @return
	 * @throws SQLException 
	 */
	@RequestMapping(value="/searchStudent",method = RequestMethod.POST)  
	public ModelAndView searchStudent(@RequestParam String searchValue, @RequestParam(required = false)
	String name, @RequestParam(required = false) String email,Model model, HttpServletRequest request) throws SQLException{ 
		checkDBConnection(); //check if connection is still ON
		//required false indicate that it is not compulsory to have it, so if I do not have, for example, a title, java will not complaint
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		//If user does not enter any search criteria
		if(searchValue == null) {
			model.addAttribute("message", "Please enter your search criteria in the search box");
			return new ModelAndView("errorPage");
		}
		searchValue.toLowerCase();//better if I put everything on lower case
		//If any of the checkboxes had been marked
		if(name == null && email == null) {
			model.addAttribute("message", "Please choose any of the two search criteria, name or email");
			return new ModelAndView("errorPage");
		}

		User student = new User();
		//I am using a list since I am reusing a view and this view need a list, the idea is the same
		//the list have a for loop that will only iterate once
		List<User> studentList = new ArrayList<User>();
		//If name is not null, search by name
		if(name != null) {
			student = sqlController.getUserByName(searchValue);
			if(student != null) studentList.add(student);
		}
		//If email is not empty search by email
		if(email != null) {
			student = sqlController.getUserByEmail(searchValue);
			if(student != null) studentList.add(student);
		}
		if(!studentList.isEmpty()) {
			return new ModelAndView("studentListPage","studentList",studentList);
		}else {
			model.addAttribute("message", "Your search criteria does not return any result, please try something else");
			return new ModelAndView("errorPage");
		}
	}

	/**
	 * This method is a copy of the previous method since I need to take care of users who write the address of the method on the browser
	 * Since previous method is a POST if I tried to access as a GET (writing the address on the browser) I will get an error
	 * In this way I do not get the error, but instead I am redirected to homepage
	 * @param project
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/searchStudent",method = RequestMethod.GET)  
	public ModelAndView searchStudentGet(Model model, HttpServletRequest request) throws SQLException {  
		model.addAttribute("message", "You have been redirected as the address entered was not correct");
		return new ModelAndView("homePage");//I am using the errorPage since I only want to show the message on the screen without create a new view 
	}

	/**
	 * Method to create a new entry for the schedule
	 * @param model
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping( "/newchecklist")
	public ModelAndView newchecklistPage(Model model, HttpServletRequest request) throws SQLException {  
		checkDBConnection(); //check if connection is still ON
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if((Integer)session.getAttribute("userType") != COORDINATOR) {
			model.addAttribute("message", "You have been redirected to the Home Page as you have tried to access a restricted area");
			return new ModelAndView("homePage");
		}
		request.getSession().setAttribute("previousURL", request.getRequestURL());
		model.addAttribute("previousPage", session.getAttribute("previousURL"));
		return new ModelAndView("checklistPage","command",new CheckList());  
	}

	/**
	 * Method to save the new entry into the DB
	 * @param checklist
	 * @param model
	 * @param request
	 * @return
	 * @throws SQLException 
	 */
	@RequestMapping(value="/savechecklist",method = RequestMethod.POST)  
	public ModelAndView saveChecklist(@ModelAttribute("checklist") CheckList checklist, 
			Model model, HttpServletRequest request) throws SQLException{  
		checkDBConnection(); //check if connection is still ON
		if(checklist == null) {
			model.addAttribute("message", "Error loading the page");
			return new ModelAndView("errorPage");
		}
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if((Integer)session.getAttribute("userType") != COORDINATOR) {
			model.addAttribute("message", "You have been redirected to the Home Page as you have tried to access a restricted area");
			return new ModelAndView("homePage");
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		//I am checking if the date entered is less than today, if it is, then I am redirecting back to the form with the data that the user entered before
		if(checklist.getDate().compareTo(dateFormat.format(date)) < 0) {
			model.addAttribute("message", "Date cannot be before today");
			return new ModelAndView("checklistPage","command",checklist);
		}
		/**
		 * If ending time is less than starting time, then an error will be show on screen
		 */
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		try {
			Date d1 = sdf.parse(checklist.getHour());
			Date d2 = sdf.parse(checklist.getEndHour());
			long elapsed = d2.getTime() - d1.getTime(); 
			if(elapsed < 0) {//If elapsed is negative that mean that the end time is before starting time
				model.addAttribute("message", "The end hour cannot be less than the start hour");
				return new ModelAndView("checklistPage","command",checklist);
			}
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		switch(sqlController.saveCheckList(checklist)) {
		case 0 :
			model.addAttribute("date", checklist.getDate());
			model.addAttribute("eventname", checklist.getEventName());
			model.addAttribute("place", checklist.getPlace());
			model.addAttribute("description", checklist.getDescription());
			model.addAttribute("hour", checklist.getHour());
			model.addAttribute("endhour", checklist.getEndHour());
			model.addAttribute("previousPage", session.getAttribute("previousURL"));
			try {
				int checkListID = sqlController.getLastChecklistID();
				if(checkListID != 0)
					model.addAttribute("checklistID",checkListID);
				else {
					model.addAttribute("message", "An error happened while saving the new event, please try again later");
					return new ModelAndView("errorPage");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			ApplicationContext context =
					new ClassPathXmlApplicationContext("Spring-Mail.xml");

			MailMail mm = (MailMail) context.getBean("mailMail");
			String message = "A new event has been added to the schedule";
			User user = sqlController.getUser((Integer)session.getAttribute("userID"));
			mm.sendMail("ismael.sanchez.leon@gmail.com","iss00009uos@gmail.com",
					"New event in the scheduled by " + user.getUsername(),message);
			return new ModelAndView("checklistViewPage","checklist",model);//will display object data 
		case 1 : 
			model.addAttribute("message", "This event already exist in the database");
			return new ModelAndView("errorPage");

		case 2 : 
			model.addAttribute("message", "An error happened while saving the event, if this error continues please contact the system administrator");
			return new ModelAndView("errorPage");
		}
		model.addAttribute("message", "An error happened while saving your event, please try again later");
		return new ModelAndView("errorPage");
	}
	/**
	 * This method is a copy of the previous method since I need to take care of users who write the address of the method on the browser
	 * Since previous method is a POST if I tried to access as a GET (writing the address on the browser) I will get an error
	 * In this way I do not get the error, but instead I am redirected to homepage
	 * @param project
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/savechecklist",method = RequestMethod.GET)  
	public ModelAndView saveChecklistGet(Model model, HttpServletRequest request) throws SQLException {  
		model.addAttribute("message", "You have been redirected as the address entered was not correct");
		return new ModelAndView("homePage");//I am using the errorPage since I only want to show the message on the screen without create a new view 
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
		checkDBConnection(); //check if connection is still ON
		int eventNum = (Integer) session.getAttribute("eventNum");
		int oldEventNum = (Integer) session.getAttribute("oldEventNum");
		List<CheckList> checklistList = sqlController.getCheckListList(true);
		List<CheckList> newEventList = sqlController.getCheckListList(true, oldEventNum, eventNum);
		//I am only interested to do this if I have elements, if not then ignore the sorting, no point
		if(!newEventList.isEmpty()) {
			removeDuplicateFromEventList(checklistList, newEventList);
			bubblesrt(newEventList);//sort the list by date
			model.addAttribute("newEventList", newEventList);
		}
		/**
		 * This second list will not be see by students or lecturers, 
		 * but in order to save space I am using the same view, but based on the userType
		 * you will be able to see or not this list
		 */
		List<CheckList> checklistListNotApproved = sqlController.getCheckListList(false);
		//I am only checking the first list since is the one that should never being empty
		if(checklistList.isEmpty()) {
			model.addAttribute("message", "An error happened while loading the schedule, please try again later");
			return new ModelAndView("errorPage");
		}
		//I am sorting both list to be show in a date order
		bubblesrt(checklistList);
		if(!checklistListNotApproved.isEmpty()) bubblesrt(checklistListNotApproved); //only sort if the list is not empty
		User user = sqlController.getUser((Integer)session.getAttribute("userID"));
		request.getSession().setAttribute("previousURL", request.getRequestURL());
		model.addAttribute("userType", (Integer)session.getAttribute("userType"));
		model.addAttribute("checklistListNotApproved", checklistListNotApproved);

		//I want to pass the size since based on the size the view will be different (if size is 0 do not load the list for not approved)
		model.addAttribute("notapprovedsize", checklistListNotApproved.size());
		return new ModelAndView("checklistListPage","checklistList",checklistList);  
	}

	/**
	 * Method to sort the event list before be displayed on the front end
	 * This method it is used in /checklistlist
	 * @param list
	 */
	public void bubblesrt(List<CheckList> list)
	{
		CheckList temp;
		if (list.size()>1) // check if the number of orders is larger than 1
		{
			for (int x=0; x<list.size(); x++) // bubble sort outer loop
			{
				for (int i=0; i < list.size()-x -1; i++) {
					if (list.get(i).getDate().compareTo(list.get(i+1).getDate()) > 0)
					{
						temp = list.get(i);
						list.set(i,list.get(i+1) );
						list.set(i+1, temp);
					}
				}
			}
		}
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
		checkDBConnection(); //check if connection is still ON
		if(checklistID == 0) {
			model.addAttribute("message", "Error loading the page");
			return new ModelAndView("errorPage");
		}
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		if((Integer)session.getAttribute("userType") != COORDINATOR) {
			model.addAttribute("message", "You have been redirected to the Home Page as you have tried to access a restricted area");
			return new ModelAndView("homePage");
		}
		CheckList checkList = new CheckList();
		checkList = sqlController.getchecklist(checklistID);
		//I am populating here the view so the user can modify the checklist, if I need to add more data to the form I should
		//update the constructor on checklist class
		model.addAttribute("checklistID", checklistID); //passing checklistID to the frontend
		model.addAttribute("previousPage", session.getAttribute("previousURL"));
		return new ModelAndView("editchecklistPage","command",new CheckList(checkList.getCheckListID(), checkList.getDate(),
				checkList.getEventName(), checkList.getPlace(), checkList.getDescription(), 
				checkList.getHour(), checkList.getEndHour()));  
	}

	/**
	 * This method is a copy of the previous method since I need to take care of users who write the address of the method on the browser
	 * Since previous method is a POST if I tried to access as a GET (writing the address on the browser) I will get an error
	 * In this way I do not get the error, but instead I am redirected to homepage
	 * @param project
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/editChecklist",method = RequestMethod.GET)  
	public ModelAndView editChecklistGet(Model model, HttpServletRequest request) throws SQLException {  
		model.addAttribute("message", "You have been redirected as the address entered was not correct");
		return new ModelAndView("homePage");//I am using the errorPage since I only want to show the message on the screen without create a new view 
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
			Model model, HttpServletRequest request) throws SQLException { 
		if(checklistID == 0) {
			model.addAttribute("message", "Error loading the page");
			return new ModelAndView("errorPage");
		}
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		checkDBConnection(); //check if connection is still ON
		if((Integer)session.getAttribute("userType") != COORDINATOR) {
			model.addAttribute("message", "You have been redirected to the Home Page as you have tried to access a restricted area");
			return new ModelAndView("homePage");
		}
		//false here means "make whatever it is linked with this ID invisible"
		sqlController.updateChecklist(checklistID,false);
		ApplicationContext context =
				new ClassPathXmlApplicationContext("Spring-Mail.xml");
		MailMail mm = (MailMail) context.getBean("mailMail");
		CheckList checkList = new CheckList();
		checkList = sqlController.getchecklist(checklistID);
		String message = "The event in the schedule " + checkList.getEventName() + " has been removed";
		User user = sqlController.getUser((Integer)session.getAttribute("userID"));
		mm.sendMail("ismael.sanchez.leon@gmail.com","iss00009uos@gmail.com",
				"An event in the schedule has been removed for " + user.getUsername(),message);
		model.addAttribute("previousPage", session.getAttribute("previousURL"));
		model.addAttribute("mainmessage", "You have made the event " + checkList.getEventName() + 
				" not visible to students ");
		model.addAttribute("secondmessage", " If you want to make the event visible again "
				+ "or edit it, go to the schedule and check at the bottom of the list");
		return new ModelAndView("projectRemovedPage");
	}

	/**
	 * This method is a copy of the previous method since I need to take care of users who write the address of the method on the browser
	 * Since previous method is a POST if I tried to access as a GET (writing the address on the browser) I will get an error
	 * In this way I do not get the error, but instead I am redirected to homepage
	 * @param project
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/removeChecklist",method = RequestMethod.GET)  
	public ModelAndView removeChecklistGet(Model model, HttpServletRequest request) throws SQLException {  
		model.addAttribute("message", "You have been redirected as the address entered was not correct");
		return new ModelAndView("homePage");//I am using the errorPage since I only want to show the message on the screen without create a new view 
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
		if(checklistID == 0) {
			model.addAttribute("message", "Error loading the page");
			return new ModelAndView("errorPage");
		}
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		checkDBConnection(); //check if connection is still ON
		if((Integer)session.getAttribute("userType") != COORDINATOR) {
			model.addAttribute("message", "You have been redirected to the Home Page as you have tried to access a restricted area");
			return new ModelAndView("homePage");
		}
		sqlController.updateChecklist(checklistID,true);
		List<CheckList> checklistList = sqlController.getCheckListList(true);
		List<CheckList> checklistListNotApproved = sqlController.getCheckListList(false);
		//This should never happens since this method is redirecting from a populated list, but better to prevent that regret
		if(checklistList.isEmpty()) {
			model.addAttribute("message", "An error happened while loading the schedule, please try again later");
			return new ModelAndView("errorPage");
		}
		bubblesrt(checklistList);
		if(!checklistListNotApproved.isEmpty()) bubblesrt(checklistListNotApproved); //only sort if the list is not empty
		model.addAttribute("checklistList", checklistList);
		model.addAttribute("checklistListNotApproved", checklistListNotApproved);
		model.addAttribute("notapprovedsize", checklistListNotApproved.size());
		//I am using same page since the final message for project or checklist is the same
		return new ModelAndView("checklistListPage");
	}

	/**
	 * This method is a copy of the previous method since I need to take care of users who write the address of the method on the browser
	 * Since previous method is a POST if I tried to access as a GET (writing the address on the browser) I will get an error
	 * In this way I do not get the error, but instead I am redirected to homepage
	 * @param project
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/makeVisibleChecklist",method = RequestMethod.GET)  
	public ModelAndView makeVisibleChecklistGet(Model model, HttpServletRequest request) throws SQLException {  
		return new ModelAndView("homePage");//I am using the errorPage since I only want to show the message on the screen without create a new view 
	}

	/**
	 * Method that it is call from the front end view editchecklistPage, it is getting a checklist object that it is save in the DB
	 * using the method saveEditChecklist from SQLController
	 * After this I am populating the front end view checklistViewPage with the data of the checklist edited
	 * @param checklist
	 * @param model
	 * @param request
	 * @return
	 * @throws SQLException 
	 */
	@RequestMapping(value="/saveEditChecklist",method = RequestMethod.POST)  
	public ModelAndView saveEditChecklist(@ModelAttribute("checklist") CheckList checklist, 
			Model model, HttpServletRequest request) throws SQLException{ 
		if(checklist == null) {
			model.addAttribute("message", "Error loading the page");
			return new ModelAndView("errorPage");
		}
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		checkDBConnection(); //check if connection is still ON
		if((Integer)session.getAttribute("userType") != COORDINATOR) {
			model.addAttribute("message", "You have been redirected to the Home Page as you have tried to access a restricted area");
			return new ModelAndView("homePage");
		}
		model.addAttribute("date", checklist.getDate());
		model.addAttribute("eventname", checklist.getEventName());
		model.addAttribute("description", checklist.getDescription());
		model.addAttribute("place", checklist.getPlace());
		model.addAttribute("hour", checklist.getHour());
		model.addAttribute("endhour", checklist.getEndHour());
		switch(sqlController.saveEditCheckList(checklist)) {
		case 0 :
			ApplicationContext context =
			new ClassPathXmlApplicationContext("Spring-Mail.xml");

			MailMail mm = (MailMail) context.getBean("mailMail");
			String message = "The event in the schedule " + checklist.getEventName() + " has been modified";
			User user = sqlController.getUser((Integer)session.getAttribute("userID"));
			mm.sendMail("ismael.sanchez.leon@gmail.com","iss00009uos@gmail.com",
					"An event in the schedule has been modified by " + user.getUsername(),message);
			model.addAttribute("previousPage", session.getAttribute("previousURL"));
			return new ModelAndView("checklistViewPage","checklist",model);//will display object data 
		case 1 : 
			model.addAttribute("message", "This event already exist in the database");
			return new ModelAndView("errorPage");

		case 2 : 
			model.addAttribute("message", "An error happened while saving your edits of the event, "
					+ "if this error continues please contact the system administrator");
			return new ModelAndView("errorPage");
		}
		model.addAttribute("message", "An error happened while saving your edits of the event, please try again later");
		return new ModelAndView("errorPage");
	}

	/**
	 * This method is a copy of the previous method since I need to take care of users who write the address of the method on the browser
	 * Since previous method is a POST if I tried to access as a GET (writing the address on the browser) I will get an error
	 * In this way I do not get the error, but instead I am redirected to homepage
	 * @param project
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/saveEditChecklist",method = RequestMethod.GET)  
	public ModelAndView saveEditChecklistGet(Model model, HttpServletRequest request) throws SQLException { 
		model.addAttribute("message", "You have been redirected as the address entered was not correct");
		return new ModelAndView("homePage");//I am using the errorPage since I only want to show the message on the screen without create a new view 
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
		if(projectID == 0) {
			model.addAttribute("message", "Error loading the page");
			return new ModelAndView("errorPage");
		}
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		checkDBConnection(); //check if connection is still ON
		if((Integer)session.getAttribute("userType") != STUDENT) {
			model.addAttribute("message", "You have been redirected to the Home Page as you have tried to access a restricted area");
			return new ModelAndView("homePage");
		}
		/**
		 * I have different returns based on the output of the method, 
		 * 0 general error saving. 
		 * 1 everything is OK.
		 * 2 duplicate entry (student trying to register twice in the same project).
		 * 3 if student already have a final project approved
		 * 5 limit of 5 projects achieved.
		 */
		int result = sqlController.registerInterest((Integer)session.getAttribute("userID"), projectID);
		switch(result) {
		case 0 :
			model.addAttribute("message", "An error happened while trying to register your interest, please try again later"); 
			return new ModelAndView("errorPage");

		case 1:
			//I am using same page since the final message for project or checklist is the same
			List<Project> projectList = sqlController.getProjectInterestedListByStudent(true,(Integer)session.getAttribute("userID"));
			List<Project> projectListNotVisible = sqlController.getProjectInterestedListByStudent(false, (Integer)session.getAttribute("userID"));
			//I am taking under consideration the situation where a student have only not visible projects in his/her list
			if(projectList.isEmpty() && projectListNotVisible.isEmpty()) {
				model.addAttribute("message", "You have not shown any interest in any projects, you can do so in the project list");
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

			String messageLecturer = "Student " + user.getUsername() + " has shown interest in your project " + project.getTitle();
			String messageStudent = "Your interest in the project " + project.getTitle() + 
					" has been sent to the lecturer " + lecturer.getUsername();
			//One message is for the lecturer
			mm.sendMail("ismael.sanchez.leon@gmail.com","iss00009uos@gmail.com","Someone has registered interest in one of your projects",messageLecturer);
			//Another message it is send to the student
			mm.sendMail("ismael.sanchez.leon@gmail.com","iss00009uos@gmail.com","Someone has registered interest in one of your projects",messageStudent);
			return new ModelAndView("interestProjectListPage","projectList",projectList); 
		case 2:
			model.addAttribute("message", "You have already registered interest in this project");
			return new ModelAndView("errorPage");
		case 3 :
			model.addAttribute("message", "You already have a final project approved, "
					+ "if you want to change your final project, contact the dissertation coordinator"); 
			return new ModelAndView("errorPage");
		case 5:
			model.addAttribute("message", "You have already shown interest in 5 projects, "
					+ "please remove your interest before you register new ones"); 
			return new ModelAndView("errorPage");
		}		
		model.addAttribute("message", "An error happened while trying to register your interest, please try again later");
		return new ModelAndView("errorPage");	
	}

	/**
	 * This method is a copy of the previous method since I need to take care of users who write the address of the method on the browser
	 * Since previous method is a POST if I tried to access as a GET (writing the address on the browser) I will get an error
	 * In this way I do not get the error, but instead I am redirected to homepage
	 * @param project
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/registerinterest",method = RequestMethod.GET)  
	public ModelAndView registerinterestGet(Model model, HttpServletRequest request) throws SQLException {  
		model.addAttribute("message", "You have been redirected as the address entered was not correct");
		return new ModelAndView("homePage");//I am using the errorPage since I only want to show the message on the screen without create a new view 
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
		if(projectID == 0) {
			model.addAttribute("message", "Error loading the page");
			return new ModelAndView("errorPage");
		}
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		checkDBConnection(); //check if connection is still ON
		if((Integer)session.getAttribute("userType") != STUDENT) {
			model.addAttribute("message", "You have been redirected to the Home Page as you have tried to access a restricted area");
			return new ModelAndView("homePage");
		}
		//I am checking if the project that I am trying to make visible is already been choose or being approved by someone else
		if(sqlController.checkIfProjectIsAlreadyChoose(projectID)) {
			//if it is the case then return error and do not make it visible
			model.addAttribute("message", "Sorry that project has been selected by someone else, contact the dissertation coordinator for more information");
			return new ModelAndView("errorPage");
		}
		int count = sqlController.getTotalInterestProject((Integer)session.getAttribute("userID"), true);
		//I am using this second counter since I can have the situation of not having any project with interest that are visible
		//And only using one counter it will show me the you had not register interest message when I actually have projects
		//but they are not visible
		int countNotVisible = sqlController.getTotalInterestProject((Integer)session.getAttribute("userID"), false);
		if(count == 5) {
			model.addAttribute("message", "You have already shown interest in 5 projects, "
					+ "please remove your interest before you register new ones"); 
			return new ModelAndView("errorPage");
		}
		if(count == 0 && countNotVisible == 0){
			model.addAttribute("message", "You have not registered interest in any projects"); 
			return new ModelAndView("errorPage");
		}else {
			sqlController.updateInterestProject(projectID,(Integer)session.getAttribute("userID"),true);
			List<Project> projectList = sqlController.getProjectInterestedListByStudent(true, (Integer)session.getAttribute("userID"));
			List<Project> projectListNotVisible = sqlController.getProjectInterestedListByStudent(false, (Integer)session.getAttribute("userID"));
			if(projectList.isEmpty()) {
				model.addAttribute("message", "You have not shown any interest in a project, choose one from project list");
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

			String messageLecturer = "Student " + user.getUsername() + " has shown interest in your project " + project.getTitle();
			String messageStudent = "Your interest in the project " + project.getTitle() + " has been send to the lecturer " + lecturer.getUsername();
			//One message is for the lecturer
			mm.sendMail("ismael.sanchez.leon@gmail.com","iss00009uos@gmail.com","Someone has registered interest in one of your projects",messageLecturer);
			//Another message it is send to the student
			mm.sendMail("ismael.sanchez.leon@gmail.com","iss00009uos@gmail.com","Someone has registered interest in one of your projects",messageStudent);
			return new ModelAndView("interestProjectListPage","projectList",projectList);
		}
	}

	/**
	 * This method is a copy of the previous method since I need to take care of users who write the address of the method on the browser
	 * Since previous method is a POST if I tried to access as a GET (writing the address on the browser) I will get an error
	 * In this way I do not get the error, but instead I am redirected to homepage
	 * @param project
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/makeInterestVisible",method = RequestMethod.GET)  
	public ModelAndView makeInterestVisibleGet(Model model, HttpServletRequest request) throws SQLException {  
		return new ModelAndView("homePage");//I am using the errorPage since I only want to show the message on the screen without create a new view 
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
			Model model, User user, HttpServletRequest request) throws SQLException { 
		checkDBConnection(); //check if connection is still ON
		if(projectID == 0) {
			model.addAttribute("message", "Error loading the page");
			return new ModelAndView("errorPage");
		}
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		sqlController.updateInterestProject(projectID,user.getUserID(),false);
		//I am using same page since the final message for project or checklist is the same
		ApplicationContext context =
				new ClassPathXmlApplicationContext("Spring-Mail.xml");
		MailMail mm = (MailMail) context.getBean("mailMail");
		Project project = new Project();
		project = sqlController.getProject(projectID);
		User lecturer = sqlController.getUser(project.getlecturerID());
		String messageStudent = "Lecturer " + lecturer.getUsername() + " has removed your interest in the project " + project.getTitle();
		String messageLecturer = "You have removed interest in the project " + project.getTitle();
		//One message is for the lecturer
		mm.sendMail("ismael.sanchez.leon@gmail.com","iss00009uos@gmail.com","You have removed interest from a project",messageLecturer);
		//Another message it is send to the student
		mm.sendMail("ismael.sanchez.leon@gmail.com","iss00009uos@gmail.com","One of your requests for a project has been removed",messageStudent);
		model.addAttribute("mainmessage", "The interest in " + project.getTitle() + 
				" had been removed succesfully");
		model.addAttribute("secondmessage", " An automatic email has been sent to the student and yourself");
		return new ModelAndView("projectRemovedPage");
	}

	/**
	 * This method is a copy of the previous method since I need to take care of users who write the address of the method on the browser
	 * Since previous method is a POST if I tried to access as a GET (writing the address on the browser) I will get an error
	 * In this way I do not get the error, but instead I am redirected to homepage
	 * @param project
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/removeinterest",method = RequestMethod.GET)  
	public ModelAndView removeInterestGet(Model model, HttpServletRequest request) throws SQLException {
		model.addAttribute("message", "You have been redirected as the address entered was not correct");
		return new ModelAndView("homePage");//I am using the errorPage since I only want to show the message on the screen without create a new view 
	}

	/**
	 * Method that it is call when the dissertation coordinator remove interest from a final project
	 * @param projectID
	 * @param user
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping( value="/removeinterestfinal",method = RequestMethod.POST)
	public ModelAndView removeInterestFinal(@RequestParam(value="projectID") int projectID, 
			Model model, User user, HttpServletRequest request) throws SQLException { 
		if(projectID == 0) {
			model.addAttribute("message", "Error loading the page");
			return new ModelAndView("errorPage");
		}
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		checkDBConnection(); //check if connection is still ON
		if((Integer)session.getAttribute("userType") != COORDINATOR) {
			model.addAttribute("message", "You have been redirected to the Home Page as you have tried to access a restricted area");
			return new ModelAndView("homePage");
		}
		sqlController.updateInterestFinalProject(projectID,user.getUserID(),false);
		//I am using same page since the final message for project or checklist is the same
		ApplicationContext context =
				new ClassPathXmlApplicationContext("Spring-Mail.xml");

		MailMail mm = (MailMail) context.getBean("mailMail");
		Project project = new Project();
		project = sqlController.getProject(projectID);
		//User user = getUser((Integer)session.getAttribute("userID"));
		User lecturer = sqlController.getUser(project.getlecturerID());

		String messageStudent = "Lecturer " + lecturer.getUsername() + " has removed your interest in the project " + project.getTitle();
		String messageLecturer = "You have removed interest in the project " + project.getTitle();
		//One message is for the lecturer
		mm.sendMail("ismael.sanchez.leon@gmail.com","iss00009uos@gmail.com","You have removed the final interest of a project",messageLecturer);
		//Another message it is send to the student
		mm.sendMail("ismael.sanchez.leon@gmail.com","iss00009uos@gmail.com","Your final interest in the project has been removed",messageStudent);
		model.addAttribute("previousPage", session.getAttribute("previousURL"));
		model.addAttribute("mainmessage", "The final project in " + project.getTitle() + 
				" has been removed succesfully ");
		model.addAttribute("secondmessage", " An automatic email has been sent to the student and yourself");
		return new ModelAndView("projectRemovedPage");
	}

	/**
	 * This method is a copy of the previous method since I need to take care of users who write the address of the method on the browser
	 * Since previous method is a POST if I tried to access as a GET (writing the address on the browser) I will get an error
	 * In this way I do not get the error, but instead I am redirected to homepage
	 * @param project
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/removeinterestfinal",method = RequestMethod.GET)  
	public ModelAndView removeInterestFinalGet(Model model, HttpServletRequest request) throws SQLException {  
		model.addAttribute("message", "You have been redirected as the address entered was not correct");
		return new ModelAndView("homePage");//I am using the errorPage since I only want to show the message on the screen without create a new view 
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
			Model model, HttpServletRequest request, User user) throws SQLException { 
		if(projectID == 0) {
			model.addAttribute("message", "Error loading the page");
			return new ModelAndView("errorPage");
		}
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		checkDBConnection(); //check if connection is still ON
		if((Integer)session.getAttribute("userType") != STUDENT) {
			model.addAttribute("message", "You have been redirected to the Home Page as you have tried to access a restricted area");
			return new ModelAndView("homePage");
		}
		User student = sqlController.getUser((Integer)session.getAttribute("userID"));
		sqlController.updateInterestProject(projectID,student.getUserID(),false);
		ApplicationContext context =
				new ClassPathXmlApplicationContext("Spring-Mail.xml");

		MailMail mm = (MailMail) context.getBean("mailMail");
		Project project = new Project();
		project = sqlController.getProject(projectID);

		String messageLecturer = "Student " + student.getUsername() + " has removed the interest in your project " + project.getTitle();
		String messageStudent = "You have removed the interest in the project " + project.getTitle();
		//One message is for the lecturer
		mm.sendMail("ismael.sanchez.leon@gmail.com","iss00009uos@gmail.com","A student has removed interest in one of your projects",messageLecturer);
		//Another message it is send to the student
		mm.sendMail("ismael.sanchez.leon@gmail.com","iss00009uos@gmail.com","You have removed interest in a project",messageStudent);
		//I am using same page since the final message for project or checklist is the same
		model.addAttribute("mainmessage", "The interest in " + project.getTitle() + 
				" has been removed succesfully ");
		model.addAttribute("secondmessage", " An automatic email has been sent to the lecturer and yourself");
		return new ModelAndView("projectRemovedPage");
	}

	/**
	 * This method is a copy of the previous method since I need to take care of users who write the address of the method on the browser
	 * Since previous method is a POST if I tried to access as a GET (writing the address on the browser) I will get an error
	 * In this way I do not get the error, but instead I am redirected to homepage
	 * @param project
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/removeinterestStudent",method = RequestMethod.GET)  
	public ModelAndView removeInterestStudentGet(Model model, HttpServletRequest request) throws SQLException {  
		return new ModelAndView("homePage");//I am using the errorPage since I only want to show the message on the screen without create a new view 
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
		checkDBConnection(); //check if connection is still ON
		//only student can access this menu
		if((Integer)session.getAttribute("userType") != STUDENT) {
			model.addAttribute("message", "You have been redirected to the Home Page as you have tried to access a restricted area");
			return new ModelAndView("homePage");
		}
		//If the student already have a final project approved, then we will only show that project to him
		Project project = sqlController.getFinalProjectStudent((Integer)session.getAttribute("userID"));
		if(project != null) {
			//only if the project if visible this will be show, I am taking consideration of a project 
			//that had been approved and then remove interest by coordinator
			if(project.isVisible()) {
				List<Project> projectList = new ArrayList<Project>();
				projectList.add(project);
				return new ModelAndView("finalProjectPage","projectList",projectList);
			} 
		}
		List<Project> projectList = sqlController.getProjectInterestedListByStudent(true, (Integer)session.getAttribute("userID"));
		List<Project> projectListNotVisible = sqlController.getProjectInterestedListByStudent(false, (Integer)session.getAttribute("userID"));
		//If project is empty then I will redirect to error page with a message explaining what to do
		if(projectList.isEmpty() && projectListNotVisible.isEmpty()) {
			model.addAttribute("message", "You have not projects with interest yet, choose one from the project list");
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
		checkDBConnection(); //check if connection is still ON
		if((Integer)session.getAttribute("userType") == STUDENT) {
			model.addAttribute("message", "You have been redirected to the Home Page as you have tried to access a restricted area");
			return new ModelAndView("homePage");
		}
		List<Project> projectList = sqlController.getProjectListVisible(true, (Integer)session.getAttribute("userID"));	
		List<Project> projectListNextYear = sqlController.getProjectListNextYear(
				(Integer)session.getAttribute("userID"), true);
		//If project is empty then I will redirect to error page with a message explaining what to do
		if(projectList.isEmpty()) {
			model.addAttribute("message", "You have not projects registered yet, choose one in project list");
			return new ModelAndView("errorPage");
		}	
		request.getSession().setAttribute("previousURL", request.getRequestURL());
		if(projectListNextYear.isEmpty()) model.addAttribute("listMessage", "You do not have any projects for next year");
		model.addAttribute("projectList", projectList);
		model.addAttribute("projectListNextYear", projectListNextYear);
		model.addAttribute("actualYear", actualYear);
		model.addAttribute("nextYear", actualYear + 1);//next year it is obtained increasing the actual year plus one
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
		checkDBConnection(); //check if connection is still ON
		if((Integer)session.getAttribute("userType") == STUDENT) {
			model.addAttribute("message", "You have been redirected to the Home Page as you have tried to access a restricted area");
			return new ModelAndView("homePage");
		}
		List<Project> projectWithInterest = sqlController.getLecturerProjectList(
				(Integer)session.getAttribute("userID"));	
		request.getSession().setAttribute("previousURL", request.getRequestURL());
		model.addAttribute("projectWithInterest", projectWithInterest);
		//I been forced to send the size separately to the front end because javaScript length function
		//does not work when the list is with objects
		model.addAttribute("interestListSize",projectWithInterest.size());
		model.addAttribute("user", new User());//passing the user allows to return any user value from the frontend
		return new ModelAndView("yourPersonalListPageWithInterest");
	}

	/**
	 * This method it is showing the list of projects that a student show interest based on the lecturer, or in other words this method
	 * it is creating a list of projects per lecturer that have interest show by students
	 * @param model
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping( value="/projectlistwithinterestapproved",method = RequestMethod.GET)
	public ModelAndView lecturerProjectListWithApprovedInterest(Model model, HttpServletRequest request) throws SQLException { 
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		checkDBConnection(); //check if connection is still ON
		if((Integer)session.getAttribute("userType") == STUDENT) {
			model.addAttribute("message", "You have been redirected to the Home Page as you have tried to access a restricted area");
			return new ModelAndView("homePage");
		}
		List<Project> projectWithInterestApproved = sqlController.getLecturerProjectListApprovedInterest(
				(Integer)session.getAttribute("userID"));	
		request.getSession().setAttribute("previousURL", request.getRequestURL());
		model.addAttribute("projectWithInterest", projectWithInterestApproved);
		//I been forced to send the size separately to the front end because javaScript length function
		//does not work when the list is with objects
		model.addAttribute("interestListSize",projectWithInterestApproved.size());
		model.addAttribute("user", new User());//passing the user allows to return any user value from the frontend
		return new ModelAndView("projectlistapprovedinterest");
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
		checkDBConnection(); //check if connection is still ON
		if((Integer)session.getAttribute("userType") == STUDENT) {
			model.addAttribute("message", "You have been redirected to the Home Page as you have tried to access a restricted area");
			return new ModelAndView("homePage");
		}
		List<Project> projectNotVisibles = sqlController.getProjectListVisible(false, 
				(Integer)session.getAttribute("userID"));
		List<Project> projectListNextYear = sqlController.getProjectListNextYear(
				(Integer)session.getAttribute("userID"), false);
		request.getSession().setAttribute("previousURL", request.getRequestURL());
		//If project is empty then I will redirect to error page with a message explaining what to do
		if(projectNotVisibles.isEmpty()) {
			model.addAttribute("message", "You do not have any projects for this year that are not visible");
		}	
		if(projectListNextYear.isEmpty()) {
			model.addAttribute("nextyearmessage", "You do not have any projects for next year that are not visible");
		}
		model.addAttribute("projectNotVisibles", projectNotVisibles);
		model.addAttribute("projectListNextYear", projectListNextYear);
		model.addAttribute("actualYear", actualYear);
		model.addAttribute("nextYear", actualYear + 1);
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
		if(projectID == 0) {
			model.addAttribute("message", "Error loading the page");
			return new ModelAndView("errorPage");
		}
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		checkDBConnection(); //check if connection is still ON
		if((Integer)session.getAttribute("userType") == STUDENT) {
			model.addAttribute("message", "You have been redirected to the Home Page as you have tried to access a restricted area");
			return new ModelAndView("homePage");
		}
		sqlController.updateProject(projectID, true);//update the project to visible
		//getting the project to obtain the title
		Project project = new Project();
		project = sqlController.getProject(projectID);
		//getting all the project not visible to populate the view again
		List<Project> projectNotVisibles = sqlController.getProjectListVisible(false, 
				(Integer)session.getAttribute("userID"));
		List<Project> projectListNextYear = sqlController.getProjectListNextYear(
				(Integer)session.getAttribute("userID"), false);
		//If project is empty then I will redirect to error page with a message explaining what to do
		if(projectNotVisibles.isEmpty()) {
			model.addAttribute("message", "You do not have any projects for this year that are not visible");
		}	
		if(projectListNextYear.isEmpty()) {
			model.addAttribute("nextyearmessage", "You do not have any projects for next year that are not visible");
		}	
		request.getSession().setAttribute("previousURL", request.getRequestURL());
		ApplicationContext context =
				new ClassPathXmlApplicationContext("Spring-Mail.xml");
		MailMail mm = (MailMail) context.getBean("mailMail");
		String messageStudent = "You made visible the project " + project.getTitle();
		//Another message it is send to the student
		mm.sendMail("ismael.sanchez.leon@gmail.com","iss00009uos@gmail.com","Someone has registered interest in one of your projects",messageStudent);
		//extra message to show that the project had been made visible
		model.addAttribute("message", "Project " + project.getTitle() + " is now visible to students");
		if(projectListNextYear.isEmpty()) model.addAttribute("listMessage", "You do not have any project for next year");
		model.addAttribute("projectNotVisibles", projectNotVisibles);
		model.addAttribute("projectListNextYear", projectListNextYear);
		model.addAttribute("actualYear", actualYear);
		model.addAttribute("nextYear", actualYear + 1);
		return new ModelAndView("notVisibleProjectPage");
	}

	/**
	 * This method is a copy of the previous method since I need to take care of users who write the address of the method on the browser
	 * Since previous method is a POST if I tried to access as a GET (writing the address on the browser) I will get an error
	 * In this way I do not get the error, but instead I am redirected to homepage
	 * @param project
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/makeItVisible",method = RequestMethod.GET)  
	public ModelAndView makeItVisibleGet(Model model, HttpServletRequest request) throws SQLException {  
		model.addAttribute("message", "You have been redirected as the address entered was not correct");
		return new ModelAndView("homePage");//I am using the errorPage since I only want to show the message on the screen without create a new view 
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
		if(projectID == 0) {
			model.addAttribute("message", "Error loading the page");
			return new ModelAndView("errorPage");
		}
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		checkDBConnection(); //check if connection is still ON
		if((Integer)session.getAttribute("userType") == STUDENT) {
			model.addAttribute("message", "You have been redirected to the Home Page as you have tried to access a restricted area");
			return new ModelAndView("homePage");
		}
		if(sqlController.approveInterest(projectID, user)) {//approveInterest return a boolean true if success false if fails
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
			model.addAttribute("message", "You have approved the student " + user.getUsername() + " for your project "
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

			String messageStudent = "Congratulations " + student.getUsername() + " your request for the project " + 
					project.getTitle() + " had been approved";
			String messageLecturer = "You approved the interest in the project " + project.getTitle() + "for student " + student.getUsername();
			//One message is for the lecturer
			mm.sendMail("ismael.sanchez.leon@gmail.com","iss00009uos@gmail.com","Student approved on project",messageLecturer);
			//Another message it is send to the student
			mm.sendMail("ismael.sanchez.leon@gmail.com","iss00009uos@gmail.com","Your interest has been approved",messageStudent);
			return new ModelAndView("yourPersonalListPageWithInterest");
		}else {
			model.addAttribute("message", "An error happened while approving the project, please try again later");
			return new ModelAndView("errorPage");
		}
	}

	/**
	 * This method is a copy of the previous method since I need to take care of users who write the address of the method on the browser
	 * Since previous method is a POST if I tried to access as a GET (writing the address on the browser) I will get an error
	 * In this way I do not get the error, but instead I am redirected to homepage
	 * @param project
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/approveinterest",method = RequestMethod.GET)  
	public ModelAndView approveInterestGet(Model model, HttpServletRequest request) throws SQLException {  
		model.addAttribute("message", "You have been redirected as the address entered was not correct");
		return new ModelAndView("homePage");//I am using the errorPage since I only want to show the message on the screen without create a new view 
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
		checkDBConnection(); //check if connection is still ON
		if((Integer)session.getAttribute("userType") != COORDINATOR) {
			model.addAttribute("message", "You have been redirected to the Home Page as you have tried to access a restricted area");
			return new ModelAndView("homePage");
		}
		List<Integer> yearList = sqlController.getAllYearOfStudents();
		model.addAttribute("yearList", yearList);
		return new ModelAndView("listtStudentsByYearPage"); 
	}

	@RequestMapping( value="/seestudentsbyyear",method = RequestMethod.POST)
	public ModelAndView allStudentsByYearPage(Model model, HttpServletRequest request, int year) throws SQLException { 
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		checkDBConnection(); //check if connection is still ON
		if((Integer)session.getAttribute("userType") != COORDINATOR) {
			model.addAttribute("message", "You have been redirected to the Home Page as you have tried to access a restricted area");
			return new ModelAndView("homePage");
		}

		List<User> studentList = sqlController.getAllStudentList(year);
		if(studentList.isEmpty()) {
			model.addAttribute("message", "No students for this year");
			return new ModelAndView("errorPage");
		}
		model.addAttribute("year", year);
		request.getSession().setAttribute("previousURL", request.getRequestURL());
		return new ModelAndView("studentListPage","studentList",studentList);  
	}

	/**
	 * This method is a copy of the previous method since I need to take care of users who write the address of the method on the browser
	 * Since previous method is a POST if I tried to access as a GET (writing the address on the browser) I will get an error
	 * In this way I do not get the error, but instead I am redirected to homepage
	 * @param project
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/seestudentsbyyear",method = RequestMethod.GET)  
	public ModelAndView allStudentsByYearPageGet(Model model, HttpServletRequest request) throws SQLException {
		model.addAttribute("message", "You have been redirected as the address entered was not correct");
		return new ModelAndView("homePage");//I am using the errorPage since I only want to show the message on the screen without create a new view 
	}


	/**
	 * This method it is returning the final project (if any) that the student have, the idea of this is allow to the
	 * module coordinator to change the interest of a project after been approved by a lecturer 
	 * @param studentID I am using studentID since userID in this case is different, since userID is the ID of the lecturer or admin
	 * @param model
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping( value="/getstudentprojects",method = RequestMethod.POST)
	public ModelAndView getAllProjectStudentList(@RequestParam(value="studentID") int studentID, 
			Model model, HttpServletRequest request) throws SQLException { 
		if(studentID == 0) {
			model.addAttribute("message", "Error loading the page");
			return new ModelAndView("errorPage");
		}
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		checkDBConnection(); //check if connection is still ON
		if((Integer)session.getAttribute("userType") != COORDINATOR){
			model.addAttribute("message", "You have been redirected to the Home Page as you have tried to access a restricted area");
			return new ModelAndView("homePage");
		}
		//If the student already have a final project approved, then we will only show that project to him
		Project finalProject = sqlController.getFinalProjectStudent(studentID);
		User user = sqlController.getUser(studentID);
		//If I do not have a final project then I set the view variable noFinalProject to false it not will be true
		if(finalProject == null) {
			model.addAttribute("noFinalProject", false);
		}else {
			model.addAttribute("noFinalProject", true);
		}
		model.addAttribute("previousPage", session.getAttribute("previousURL"));
		model.addAttribute("user", new User());//passing the user allows to return any user value from the frontend
		model.addAttribute("finalProject", finalProject);
		return new ModelAndView("allStudentProjectPage");
	}

	/**
	 * This method is a copy of the previous method since I need to take care of users who write the address of the method on the browser
	 * Since previous method is a POST if I tried to access as a GET (writing the address on the browser) I will get an error
	 * In this way I do not get the error, but instead I am redirected to homepage
	 * @param project
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getstudentprojects",method = RequestMethod.GET)  
	public ModelAndView getStudentProjectsGet(Model model, HttpServletRequest request) throws SQLException {
		model.addAttribute("message", "You have been redirected as the address entered was not correct");
		return new ModelAndView("homePage");//I am using the errorPage since I only want to show the message on the screen without create a new view 
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
		checkDBConnection(); //check if connection is still ON
		if((Integer)session.getAttribute("userType") != COORDINATOR){
			model.addAttribute("message", "You have been redirected to the Home Page as you have tried to access a restricted area");
			return new ModelAndView("homePage");
		}
		List<Integer> yearList = sqlController.getAllYearOfProjects();
		request.getSession().setAttribute("previousURL", request.getRequestURL());
		if(yearList.isEmpty()) {
			model.addAttribute("message", "Year list is empty");
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
	public ModelAndView seePreviousYearProject(Model model, HttpServletRequest request, int year) throws SQLException { 
		if(year == 0) {
			model.addAttribute("message", "Error loading the page");
			return new ModelAndView("errorPage");
		}
		//redirect to login page if you are not login
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		checkDBConnection(); //check if connection is still ON
		if((Integer)session.getAttribute("userType") != COORDINATOR) {
			model.addAttribute("message", "You have been redirected to the Home Page as you have tried to access a restricted area");
			return new ModelAndView("homePage");
		}
		List<Project> projectList = sqlController.getProjectsByYear(year);
		if(projectList.isEmpty()) {
			model.addAttribute("message", "No projects have been found for the year " + year + " please contact the dissertation coordinator");
			return new ModelAndView("errorPage"); 
		}
		model.addAttribute("previousPage", session.getAttribute("previousURL"));
		model.addAttribute("projectList", projectList);
		model.addAttribute("year", year);
		return new ModelAndView("previousyearprojectlist");
	}

	/**
	 * This method is a copy of the previous method since I need to take care of users who write the address of the method on the browser
	 * Since previous method is a POST if I tried to access as a GET (writing the address on the browser) I will get an error
	 * In this way I do not get the error, but instead I am redirected to homepage
	 * @param project
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/seeprojectbyyear",method = RequestMethod.GET)  
	public ModelAndView seeProjectsByYearGet(Model model, HttpServletRequest request) throws SQLException { 
		model.addAttribute("message", "You have been redirected as the address entered was not correct");
		//I am using the errorPage since I only want to show the message on the screen without create a new view
		return new ModelAndView("homePage"); 
	}

	/**
	 * Method that is showing a list with all the projects of the actual year
	 * As admin you have the option to edit a project but never remove
	 * I am not letting to remove since I do not want admin to remove projects that already
	 * have students
	 * @param model
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping( value="/allprojectsactualyear",method = RequestMethod.GET)
	public ModelAndView seeAllProjectsActualYear(Model model, HttpServletRequest request) throws SQLException { 
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		checkDBConnection(); //check if connection is still ON
		if((Integer)session.getAttribute("userType") != COORDINATOR) {
			model.addAttribute("message", "You have been redirected to the Home Page as you have tried to access a restricted area");
			return new ModelAndView("homePage");
		}
		List<Project> projectList = sqlController.getAllProjectActualYear();
		if(projectList.isEmpty()) {
			model.addAttribute("message", "No projects have been found for the year  " + actualYear + " please contact the dissertation coordinator");
			return new ModelAndView("errorPage"); 
		}
		request.getSession().setAttribute("previousURL", request.getRequestURL());
		model.addAttribute("projectList", projectList);
		//I am passing the actual year to show it on the title of the page
		model.addAttribute("actualYear", actualYear);
		return new ModelAndView("projectListByYearPage");
	}

	/**
	 * Method to show projects for the next year
	 * @param model
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping( value="/nextyearprojects",method = RequestMethod.GET)
	public ModelAndView seeAllNextYearProjects(Model model, HttpServletRequest request) throws SQLException { 
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		checkDBConnection(); //check if connection is still ON
		if((Integer)session.getAttribute("userType") != COORDINATOR) {
			model.addAttribute("message", "You have been redirected to the Home Page as you have tried to access a restricted area");
			return new ModelAndView("homePage");
		}
		List<Project> projectList = sqlController.getAllProjectNextYear();
		if(projectList.isEmpty()) {
			model.addAttribute("message", "No projects have been found for the year  " + actualYear+1 + " please contact the dissertation coordinator");
			return new ModelAndView("errorPage"); 
		}
		request.getSession().setAttribute("previousURL", request.getRequestURL());
		model.addAttribute("projectList", projectList);
		//I am passing the actual year + 1 to show it on the title of the page
		model.addAttribute("actualYear", actualYear+1);
		return new ModelAndView("projectListByYearPage");
	}

	/**
	 * Main method where the process to upload one student starts
	 * It is only checking that you are an admin and returning a view to the page for upload files with a new
	 * fileBucket object that will be populated when a file is uploaded
	 * @param model
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping( value="/uploadonestudents",method = RequestMethod.GET)
	public ModelAndView uploadOneNewStudent(Model model, HttpServletRequest request) throws SQLException { 
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		checkDBConnection(); //check if connection is still ON
		if((Integer)session.getAttribute("userType") != COORDINATOR) {
			model.addAttribute("message", "You have been redirected to the Home Page as you have tried to access a restricted area");
			return new ModelAndView("homePage");
		}	
		return new ModelAndView("addNewstudentsPage", "command", new User());
	}

	@RequestMapping( value="/savestudent",method = RequestMethod.POST)
	public ModelAndView saveStudent(Model model, HttpServletRequest request, User student) throws SQLException { 
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		checkDBConnection(); //check if connection is still ON
		if((Integer)session.getAttribute("userType") != COORDINATOR) {
			model.addAttribute("message", "You have been redirected to the Home Page as you have tried to access a restricted area");
			return new ModelAndView("homePage");
		}	
		if(student.getYear() < actualYear) {
			model.addAttribute("message", "The year cannot be less than current year " + actualYear);
			return new ModelAndView("addNewstudentsPage", "command", new User());
		}
		Boolean newStudentAdded = false;
		student.setUserType(STUDENT);//since is a student this value is giving here
		//A best approach would be to have a password generator that choose a unique password that only works for one login and then user is force
		//to change the password
		student.setPassword("111111");
		if(!sqlController.checkIfUserExistInDB(student)) {
			//add new user to the DB, if 0 means that an error happened during the inclusion of the user to the DB
			if(sqlController.addStudentToDB(student) == 0) {
				model.addAttribute("message", "User could not be added to the database, please check the database or the excel file");
				return new ModelAndView("errorPage");
			}
			newStudentAdded = true;
		}
		if(newStudentAdded) {
			//I am only setting this message if I actually added an user to the DB
			model.addAttribute("message", "New user added to the system successfully for the year " + student.getYear());
		}else {
			//If not new students found in the file I am showing this message
			model.addAttribute("message", "User already exists in the database");
		}
		List<Integer> yearList = sqlController.getAllYearOfStudents();
		model.addAttribute("yearList", yearList);
		return new ModelAndView("listtStudentsByYearPage"); 
	}


	@RequestMapping( value="/savestudent",method = RequestMethod.GET)
	public ModelAndView saveStudentGet(Model model, HttpServletRequest request, User student) throws SQLException { 
		model.addAttribute("message", "You have been redirected as the address entered was not correct");
		return new ModelAndView("homePage");  
	}

	/**
	 * Main method where the process to upload student starts
	 * It is only checking that you are an admin and returning a view to the page for upload files with a new
	 * fileBucket object that will be populated when a file is uploaded
	 * @param model
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping( value="/uploadstudents",method = RequestMethod.GET)
	public ModelAndView uploadFileForNewStudents(Model model, HttpServletRequest request) throws SQLException { 
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		checkDBConnection(); //check if connection is still ON
		if((Integer)session.getAttribute("userType") != COORDINATOR) {
			model.addAttribute("message", "You have been redirected to the Home Page as you have tried to access a restricted area");
			return new ModelAndView("homePage");
		}	
		return new ModelAndView("newstudentsuploadPage", "command", new FileBucket());
	}

	/**
	 * Method that receive a csv or xlsx file with student information and populate the DB with those student information
	 * @param file
	 * @param model
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public ModelAndView addNewStudentToDBFromCSV(@RequestParam MultipartFile file, Model model, 
			HttpServletRequest request) throws IOException {
		checkDBConnection(); //check if connection is still ON
		try {
			if(file.getOriginalFilename().isEmpty()) {//if file name is empty that means that no file was uploaded so I will show an error
				model.addAttribute("message", "Please upload a file using the choose file button on the left");
				return new ModelAndView("newstudentsuploadPage", "command", new FileBucket());
			}
			//If file does not have an .csv or .xlsx extension, then do not let the file go further and return to the previous page with an error message
			else if(!file.getContentType().equalsIgnoreCase("application/vnd.ms-excel") && 
					!file.getContentType().equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")){
				model.addAttribute("message", "The file must have a csv or xlsx extension");
				return new ModelAndView("newstudentsuploadPage", "command", new FileBucket());
			}else {
				/**
				 * this boolean is taking care if new students had been added to the DB or not
				 * I am using this option is better than check if the number of student in the DB 
				 * change between the start and the end of the method
				 */
				int numberStudentAdded = 0;
				int numberStudentIgnored = 0;
				boolean newStudentAdded = false;
				@SuppressWarnings("resource")
				CSVReader reader = new CSVReader(new FileReader(file.getOriginalFilename()));
				String [] nextLine;
				while ((nextLine = reader.readNext()) != null) {
					User newStudent = new User();
					newStudent.setUsername(nextLine[0]);
					newStudent.setPassword(nextLine[1]);
					newStudent.setEmail(nextLine[2]);
					newStudent.setUserType(STUDENT);//since I am adding students, they will always have the same type
					newStudent.setYear(Integer.parseInt(nextLine[4]));
					//Only add student if the student does not exist already in the DB
					if(!sqlController.checkIfUserExistInDB(newStudent)) {
						//add new user to the DB, if 0 means that an error happened during the inclusion of the user to the DB
						if(sqlController.addStudentToDB(newStudent) == 0) {
							model.addAttribute("message", "User could not be added to the database, please check the database or the excel file");
							return new ModelAndView("errorPage");
						}
						newStudentAdded = true;
						numberStudentAdded +=1;
					}else {
						numberStudentIgnored+=1;
					}
				}
				reader.close();
				if(newStudentAdded) {
					//I am only setting this message if I actually added an user to the DB
					model.addAttribute("message",  numberStudentAdded + " new users have been added to the system successfully and " 
							+ numberStudentIgnored + " students have been ignored as they already exist in the database");
				}else {
					//If not new students found in the file I am showing this message
					model.addAttribute("message", numberStudentAdded + " new users have been added to the system successfully and " 
							+ numberStudentIgnored + " students have been ignored as they already exist in the database");
				}
				//At the end I am showing you the list of all students
				List<Integer> yearList = sqlController.getAllYearOfStudents();
				model.addAttribute("yearList", yearList);
				return new ModelAndView("listtStudentsByYearPage"); 
			}	
			//If the file does not have the correct format then catch the error and go back to the upload file page
		}catch(java.lang.ArrayIndexOutOfBoundsException e) {
			model.addAttribute("message", "Incorrect file structure, the correct structure is "
					+ "Username, Password, Email, UserType and Year");
			return new ModelAndView("newstudentsuploadPage", "command", new FileBucket());
		}
	}

	/**
	 * Get method for the upload file, this method is only here to avoid an error if someone try to enter the address of this method
	 * on the browser without passing any file
	 * @param file
	 * @param model
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/uploadFile", method = RequestMethod.GET)
	public ModelAndView addNewStudentToDBFromCSVGet(Model model, 
			HttpServletRequest request) throws IOException {
		model.addAttribute("message", "You have been redirected as the address entered was not correct");
		return new ModelAndView("homePage");  
	}

	/**
	 * Method to edit students, it is receiving the student ID and will redirect to the form
	 * where to edit the student information
	 * @param projectID
	 * @param model
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping( value="/editstudent",method = RequestMethod.POST)
	public ModelAndView editStudent(@RequestParam(value="studentID") int studentID, 
			Model model, HttpServletRequest request) throws SQLException { 
		checkDBConnection(); //check if connection is still ON
		//I create student so it can send all the information to the front end
		User student = sqlController.getUser(studentID);
		return new ModelAndView("editstudentPage", "command", new User(student.getUserID(),student.getUsername(), 
				student.getPassword(), student.getEmail(), student.getUserType(), student.getYear()));
	}
	/**
	 * Copy of the previous method that take care if the user try to access to this address without passing any value
	 * @param projectID
	 * @param model
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping( value="/editstudent",method = RequestMethod.GET)
	public ModelAndView editStudentGet(Model model, HttpServletRequest request) throws SQLException { 
		model.addAttribute("message", "You have been redirected as the address entered was not correct");
		return new ModelAndView("homePage");  
	}

	/**
	 * Method that save the edit student in the DB, I am only allowing to edit from the user
	 * email, username and year, the rest of the data would never been edited
	 * @param student
	 * @param model
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping(value="/saveEditStudent",method = RequestMethod.POST)  
	public ModelAndView saveEditStudent(@ModelAttribute("student") User student, 
			Model model, HttpServletRequest request) throws SQLException{ 
		checkDBConnection(); //check if connection is still ON
		int resultSavingStudent = sqlController.saveEditStudent(student);
		//Based on the result the message show in screen will be different, but all of them will take you back to the
		//student list page but showing the correct message that you got from the save edit method
		switch(resultSavingStudent) {
		case 0:
			model.addAttribute("message", "Student " + student.getUsername() + " has been modified succesfully");
			break;
		case 1:
			model.addAttribute("message", "Student " + student.getUsername() + " could not be modified, please try again later");
			break;
		case 2:
			model.addAttribute("message", "A SQL error happened, please try again later");
			break;
		}
		//At the end I am showing you the list of all students
		List<Integer> yearList = sqlController.getAllYearOfStudents();
		model.addAttribute("yearList", yearList);
		return new ModelAndView("listtStudentsByYearPage"); 
	}
	/**
	 * Copy of the previous method that take care if the user try to access to this address without passing any value
	 * @param projectID
	 * @param model
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping( value="/saveEditStudent",method = RequestMethod.GET)
	public ModelAndView saveEditStudentGet(Model model, HttpServletRequest request) throws SQLException{ 
		model.addAttribute("message", "You have been redirected as the address entered was not correct");
		return new ModelAndView("homePage");  
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
		checkDBConnection(); //check if connection is still ON
		HttpSession session = getSession(request);
		if(session.getAttribute("userID") == null) return login(request);
		//I am saving the old count of the DB when user logout, so if the old had been updated that means that the user saw the new project
		//or event, if it has not, then the user has not see the new project or event and the message of new will be show again the next
		//time that the user login
		if(sqlController != null) {
			sqlController.updateDBCount((Integer)session.getAttribute("userID"),
					(Integer)session.getAttribute("oldProjectNum")
					,(Integer)session.getAttribute("oldEventNum"));
			//I am making myself sure that the userID is null so it will fail if I try to access something else after logout
			session.setAttribute("userID", null);
			session.invalidate();
		}
		return login(request);
	}
}