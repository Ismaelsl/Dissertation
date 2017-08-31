package org.dissertationWeb.controller;
 
import java.util.List;

import org.dissertationWeb.classes.CheckList;
import org.dissertationWeb.classes.Document;
import org.dissertationWeb.classes.Lecturer;
import org.dissertationWeb.classes.Project;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


 
@Controller
public class MyController {
 
    @RequestMapping(value = { "/", "/home" }, method = RequestMethod.GET)
    public String homePage(Model model) {
        return "homePage";
    }
 
     
    @RequestMapping(value = { "/contactus" }, method = RequestMethod.GET)
    public String contactusPage(Model model) {
    	Lecturer test = new Lecturer("John", "Computer Science", "test@test.com");
    	
        model.addAttribute("name", test.getName());
        model.addAttribute("department", test.getDepartment());
        model.addAttribute("email", test.getEmail());
        return "contactusPage";
    }
    @RequestMapping(value = { "/projectlist" }, method = RequestMethod.GET)
    public String projectListPage(Model model) {
    	/*
    	 * int projectID, int year, String title, List<String> topics, 
			List<String> compulsoryReading,String description, Lecturer lecturer, 
			boolean visible, Document document, boolean waitingToBeApproved,
			CheckList checkList
    	 */
    	Project testProject = new Project(1,2017,"Test",null,null,"testing", 
				new Lecturer("John", "Computer Science", "test@test.com"),
				true, new Document(),false, new CheckList());
    	
        model.addAttribute("id", testProject.getProjectID());
        model.addAttribute("year", testProject.getYear());
        model.addAttribute("title", testProject.getTitle());
        model.addAttribute("description", testProject.getDescription());
        model.addAttribute("lecturername", testProject.getLecturer().getName());
        model.addAttribute("lecturerdept", testProject.getLecturer().getDepartment());
        model.addAttribute("lectureremail", testProject.getLecturer().getEmail());
        model.addAttribute("visible", testProject.isVisible());
        model.addAttribute("document", testProject.getDocument());
        model.addAttribute("waitingtoapproved", testProject.isWaitingToBeApproved());
        model.addAttribute("checklist", testProject.getCheckList());
        
        return "projectListPage";
    }
     
}