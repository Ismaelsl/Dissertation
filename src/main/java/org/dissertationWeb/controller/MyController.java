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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MyController {
	private Connection newConnection;
	private int actualLecturerID;
	
	private void startDBConnection() {
		//Create a connection to the DB as soon as we need it
		DBConnection connect = new DBConnection();
		newConnection = connect.connect();
	}

	@RequestMapping(value = { "/", "/home" }, method = RequestMethod.GET)
	public String homePage(Model model) {
	if(newConnection == null) startDBConnection();
		return "homePage";
	}
	
	@RequestMapping(value = { "/login" }, method = RequestMethod.GET)
	public String loginPage(@RequestParam(value="username, userPassword")String username, String userPassword) {
	if(newConnection == null) startDBConnection();
	System.out.println("username " + username + " password " + userPassword);
	actualLecturerID = 15;
		return "loginPage";
	}

	@RequestMapping(value = { "/contactus" }, method = RequestMethod.GET)
	public String contactusPage(Model model) throws SQLException {
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
		return "contactusPage";
	}
	@RequestMapping(value = { "/projectlist" }, method = RequestMethod.GET)
	public ModelAndView projectListPage(Model model) throws SQLException {
		if(newConnection == null) startDBConnection();
		String query = "SELECT * FROM project";
		Statement st = newConnection.createStatement();
		ResultSet rs = st.executeQuery(query);
		List<Project> projectList = new ArrayList<Project>();
		while (rs.next())
		{
			Project project = new Project();
			project.setProjectID(rs.getInt("projectID"));
			project.setTitle(rs.getString("title"));
			project.setDescription(rs.getString("description"));
			project.setCompulsoryReading(rs.getString("compulsoryReading").replaceAll("[\\[\\]\\(\\)]", ""));
			project.setTopics(rs.getString("topic").replaceAll("[\\[\\]\\(\\)]", ""));
			projectList.add(project);
		}
		System.out.println("List size " + projectList.size());
		return new ModelAndView("projectListPage","projectList",projectList);  
	}
	
	@RequestMapping( "/newproject")
	public ModelAndView newprojectPage(Model model) throws SQLException {  
		if(newConnection == null) startDBConnection();
		return new ModelAndView("newprojectPage","command",new Project());  
	}
	
	@RequestMapping( value="/edit",method = RequestMethod.POST)
	public ModelAndView editprojectPage(@RequestParam(value="projectID") int projectID) throws SQLException { 
		Project project = new Project();
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
		System.out.println(project.getProjectID() +" inside edit project page!!");
		if(newConnection == null) startDBConnection();
		model.addAttribute("year",project.getYear());
		model.addAttribute("title",project.getTitle());
		model.addAttribute("topics",project.getTopics());
		model.addAttribute("compulsoryReading",project.getCompulsoryReading());
		model.addAttribute("description",project.getDescription());
		/*model.addAttribute("lecturerID",actualLecturerID);
		model.addAttribute("visible",false);
		model.addAttribute("documentID",1);
		model.addAttribute("waitingToBeApproved",false);
		model.addAttribute("checklistID",1);*/
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
		model.addAttribute("lecturerID",actualLecturerID);
		model.addAttribute("visible",false);
		model.addAttribute("documentID",1);
		model.addAttribute("waitingToBeApproved",false);
		model.addAttribute("checklistID",1);
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
			preparedStmt.setInt (6, actualLecturerID);
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

}