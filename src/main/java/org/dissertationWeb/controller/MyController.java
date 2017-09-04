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
import org.springframework.web.servlet.ModelAndView;



@Controller
public class MyController {
	private Connection newConnection;

	@RequestMapping(value = { "/", "/home" }, method = RequestMethod.GET)
	public String homePage(Model model) {
		DBConnection connect = new DBConnection();
		newConnection = connect.connect();

		return "homePage";
	}


	@RequestMapping(value = { "/contactus" }, method = RequestMethod.GET)
	public String contactusPage(Model model) throws SQLException {
		String query = "SELECT * FROM Lecturer";
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
		String query = "SELECT * FROM project";
		Statement st = newConnection.createStatement();
		ResultSet rs = st.executeQuery(query);
		List<Project> projectList = new ArrayList<Project>();
		while (rs.next())
		{
			Project project = new Project();
			project.setProjectID(rs.getInt("projectID"));
			project.setTitle( rs.getString("title"));
			project.setDescription(rs.getString("description"));
			projectList.add(project);
		}
		System.out.println("List size" + projectList.size());
		return new ModelAndView("projectListPage","projectList",projectList);  
	}
	@RequestMapping( "/newproject")
	public ModelAndView newprojectPage(Model model) throws SQLException {      
		return new ModelAndView("newprojectPage","command",new Project());  
	}

	@RequestMapping(value="/save",method = RequestMethod.POST)  
	public ModelAndView save(@ModelAttribute("project") Project project, Model model){  
		//write code to save project object  
		//here, we are displaying project object to prove project has data  
		System.out.println(project.getTitle()+" "+project.getDescription()); 

		//I need to use model to populate the projectPage, using the project object itself, does not works
		model.addAttribute("title",project.getTitle());
		model.addAttribute("description",project.getDescription());
		String query = " insert into project (title, description)"
				+ " values (?, ?)";

		try {
			PreparedStatement preparedStmt = newConnection.prepareStatement(query);
			preparedStmt.setString (1, project.getTitle());
			preparedStmt.setString (2, project.getDescription());
			preparedStmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ModelAndView("projectPage","project",model);//will display object data  
	}  

}