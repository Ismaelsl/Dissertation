/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/7.0.47
 * Generated at: 2018-02-21 15:42:07 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp.WEB_002dINF.basefragments;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class _005fmenu_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
        throws java.io.IOException, javax.servlet.ServletException {

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("<link href=\"bootstrap/css/mycss.css\" rel=\"stylesheet\" type=\"text/css\" />\r\n");
      out.write("\r\n");
      out.write("<nav class=\"navbar navbar-default\">\r\n");
      out.write("\t<div class=\"container-fluid\" id=\"menuContainer\">\r\n");
      out.write("\r\n");
      out.write("\t\t<ul class=\"nav navbar-nav\" id=\"menu\">  \r\n");
      out.write("\t\t\t");

			//Area where I am getting the values from the sessionfrom the session
				int user = 0;
				int projectNum = 0;
				int eventNum = 0;
				int oldProjectNum = 0;
				int oldEventNum = 0;
				//Since menu will be load the first thing is normal that session still not exist 
				//so I am catching the error and keep the webapplication running
				try {
					user = (Integer) session.getAttribute("userType");
					projectNum = (Integer) session.getAttribute("projectNum");
					eventNum = (Integer) session.getAttribute("eventNum");
					oldProjectNum = (Integer) session.getAttribute("oldProjectNum");
					oldEventNum = (Integer) session.getAttribute("oldEventNum");
				} catch (java.lang.NullPointerException e) {

				}
				if (user == 3) {
					//admin can see everything
			
      out.write("\r\n");
      out.write("\t\t\t<li class=\"dropdown\"><a class=\"dropdown-toggle\" data-toggle=\"dropdown\" href=\"#\">\r\n");
      out.write("\t\t\t\t<img class=\"imagestyle\" alt=\"dissertationcoordinatoricon\" \r\n");
      out.write("\t\t\t\tsrc='bootstrap/images/dissertationcoordinator.png' \r\n");
      out.write("\t\t\t\tonmouseover=\"this.src='bootstrap/images/dissertationcoordinatorblack.png';\" \r\n");
      out.write("\t\t\t\tonmouseout=\"this.src='bootstrap/images/dissertationcoordinator.png';\" />\r\n");
      out.write("\t\t\tCoordinator<span class=\"caret\"></span></a>\r\n");
      out.write("\t\t\t\t<ul class=\"dropdown-menu\" id=\"ulmenustyle\">\r\n");
      out.write("\t\t\t\t\t<li><b><a href=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/newchecklist\">New\r\n");
      out.write("\t\t\t\t\t\t\tenter to the schedule</a></b></li>\r\n");
      out.write("\t\t\t\t\t<li><b><a\r\n");
      out.write("\t\t\t\t\t\thref=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/projectlisttoapprove\">Project\r\n");
      out.write("\t\t\t\t\t\t\tList to approve</a></b></li>\r\n");
      out.write("\t\t\t\t\t<li><b><a\r\n");
      out.write("\t\t\t\t\t\thref=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/previousyearprojects\">Previous\r\n");
      out.write("\t\t\t\t\t\t\tyear projects</a></b></li>\r\n");
      out.write("\t\t\t\t\t<li><b><a\r\n");
      out.write("\t\t\t\t\t\thref=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/allprojectsactualyear\">See all projects\r\n");
      out.write("\t\t\t\t\t\t\tcurrent year</a></b></li>\r\n");
      out.write("\t\t\t\t\t<li><b><a\r\n");
      out.write("\t\t\t\t\t\thref=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/nextyearprojects\">See all projects\r\n");
      out.write("\t\t\t\t\t\t\tnext year</a></b></li>\r\n");
      out.write("\t\t\t\t\t<li><b><a\r\n");
      out.write("\t\t\t\t\t\thref=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/uploadonestudents\">Add one \r\n");
      out.write("\t\t\t\t\t\tstudent</a></b></li>\r\n");
      out.write("\t\t\t\t\t<li><b><a\r\n");
      out.write("\t\t\t\t\t\thref=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/uploadstudents\">Add new \r\n");
      out.write("\t\t\t\t\t\tstudents</a></b></li>\r\n");
      out.write("\t\t\t\t\t<li><b><a href=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/studentlist\">Student\r\n");
      out.write("\t\t\t\t\t\t\tlist</a></b></li>\r\n");
      out.write("\t\t\t\t</ul></li>\r\n");
      out.write("\t\t\t<li class=\"dropdown\" id=\"lecturer\"><a class=\"dropdown-toggle\" data-toggle=\"dropdown\" href=\"#\">\r\n");
      out.write("\t\t\t\t<img class=\"imagestyle\" alt=\"lecturericon\" src='bootstrap/images/lecturer.png' \r\n");
      out.write("\t\t\t\tonmouseover=\"this.src='bootstrap/images/lecturerblack.png';\" \r\n");
      out.write("\t\t\t\tonmouseout=\"this.src='bootstrap/images/lecturer.png';\" />\r\n");
      out.write("\t\t\t\tLecturer<span class=\"caret\"></span></a>\r\n");
      out.write("\t\t\t\t<ul class=\"dropdown-menu\" id=\"ulmenustyle\">\r\n");
      out.write("\t\t\t\t\t<li><b><a\r\n");
      out.write("\t\t\t\t\t\thref=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/projectlecturerlist\">Your\r\n");
      out.write("\t\t\t\t\t\t\tlist of projects</a></b></li>\r\n");
      out.write("\t\t\t\t\t<li><b><a\r\n");
      out.write("\t\t\t\t\t\thref=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/projectlistwithinterest\">Your\r\n");
      out.write("\t\t\t\t\t\t\tproject with interest</a></b></li>\r\n");
      out.write("\t\t\t\t\t<li><b><a\r\n");
      out.write("\t\t\t\t\t\thref=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/projectlistwithinterestapproved\">Your approved \r\n");
      out.write("\t\t\t\t\t\tprojects</a></b></li>\r\n");
      out.write("\t\t\t\t\t<li><b><a\r\n");
      out.write("\t\t\t\t\t\thref=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/projectlecturerlistnotvisible\">Your\r\n");
      out.write("\t\t\t\t\t\t\tnot visible project list</a></b></li>\r\n");
      out.write("\t\t\t\t\t<li><b><a href=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/newproject\">New\r\n");
      out.write("\t\t\t\t\t\t\tProject</a></b></li>\r\n");
      out.write("\t\t\t\t\t<li><b><a href=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/newprojectnextyear\">New\r\n");
      out.write("\t\t\t\t\t\t\tProject next year</a></b></li> \r\n");
      out.write("\t\t\t\t</ul></li>\r\n");
      out.write("\t\t\t<li>\r\n");
      out.write("\t\t\t\t<div class=\"container\">\r\n");
      out.write("\t\t\t\t\t<a href=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/projectlist\">\r\n");
      out.write("\t\t\t\t\t<img class=\"imagestyle\" alt=\"projectlisticon\" src='bootstrap/images/projectlist.png' \r\n");
      out.write("\t\t\t\t\t\tonmouseover=\"this.src='bootstrap/images/projectlistblack.png';\" \r\n");
      out.write("\t\t\t\t\t\tonmouseout=\"this.src='bootstrap/images/projectlist.png';\" /> Project List</a>\r\n");
      out.write("\t\t\t\t\t");
 if(projectNum > oldProjectNum){ //if I have more projects that the last time I loged in load plus green icon
      out.write("\r\n");
      out.write("\t\t\t\t\t\t<img class=\"imagestylesmall\" id = \"image\" alt=\"newicon\" src='bootstrap/images/new.png'/>\r\n");
      out.write("\t\t\t\t\t\t<div class=\"middle\">\r\n");
      out.write("\t\t\t\t\t\t\t<span class=\"text\">New Projects!</span>\r\n");
      out.write("\t\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t");
} 
      out.write("\t\t\t \t\r\n");
      out.write("\t\t\t\t</div>\r\n");
      out.write("\t\t\t</li>\r\n");
      out.write("\t\t\t<li>\r\n");
      out.write("\t\t\t\t<div class=\"container\">\r\n");
      out.write("\t\t\t\t\t<a href=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/checklistlist\">\r\n");
      out.write("\t\t\t\t\t<img class=\"imagestyle\" alt=\"scheduleicon\" src='bootstrap/images/schedule.png' \r\n");
      out.write("\t\t\t\t\t\tonmouseover=\"this.src='bootstrap/images/scheduleblack.png';\" \r\n");
      out.write("\t\t\t\t\t\tonmouseout=\"this.src='bootstrap/images/schedule.png';\" /> Schedule</a>\r\n");
      out.write("\t\t\t\t\t");
 if(eventNum > oldEventNum){ //if I have more projects that the last time I loged in load plus green icon
      out.write("\r\n");
      out.write("\t\t\t\t\t\t<img class=\"imagestylesmall\" id = \"image\" alt=\"newicon\" src='bootstrap/images/new.png'/>\r\n");
      out.write("\t\t\t\t\t\t\t<div class=\"middle\">\r\n");
      out.write("\t\t\t\t\t\t\t\t<span class=\"text\">New Events!</span>\r\n");
      out.write("\t\t\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t\t");
} 
      out.write("\t\t\t\t\r\n");
      out.write("\t\t\t\t</div>\r\n");
      out.write("\t\t\t</li>\r\n");
      out.write("\t\t\t");

				} else if (user == 1) {// lecturer menu
			
      out.write("\r\n");
      out.write("\r\n");
      out.write("\t\t\t<li class=\"dropdown\"><a class=\"dropdown-toggle\" data-toggle=\"dropdown\" href=\"#\">\t\t\t\t\r\n");
      out.write("\t\t\t\t<img class=\"imagestyle\" alt=\"lecturericon\" src='bootstrap/images/lecturer.png' \r\n");
      out.write("\t\t\t\tonmouseover=\"this.src='bootstrap/images/lecturerblack.png';\" \r\n");
      out.write("\t\t\t\tonmouseout=\"this.src='bootstrap/images/lecturer.png';\" />\r\n");
      out.write("\t\t\tLecturer<span class=\"caret\"></span></a>\r\n");
      out.write("\t\t\t\t<ul class=\"dropdown-menu\" id=\"ulmenustyle\">\r\n");
      out.write("\t\t\t\t\t<li><b><a\r\n");
      out.write("\t\t\t\t\t\thref=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/projectlecturerlist\">Your\r\n");
      out.write("\t\t\t\t\t\t\tlist of projects</a></b></li>\r\n");
      out.write("\t\t\t\t\t<li><b><a\r\n");
      out.write("\t\t\t\t\t\thref=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/projectlistwithinterest\">Your\r\n");
      out.write("\t\t\t\t\t\t\tproject with interest</a></b></li>\r\n");
      out.write("\t\t\t\t\t<li><b><a\r\n");
      out.write("\t\t\t\t\t\thref=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/projectlistwithinterestapproved\">Your approved \r\n");
      out.write("\t\t\t\t\t\tprojects</a></b></li>\r\n");
      out.write("\t\t\t\t\t<li><b><a\r\n");
      out.write("\t\t\t\t\t\thref=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/projectlecturerlistnotvisible\">Your\r\n");
      out.write("\t\t\t\t\t\t\tnot visible project list</a></b></li>\r\n");
      out.write("\t\t\t\t\t<li><b><a href=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/newproject\">New\r\n");
      out.write("\t\t\t\t\t\t\tProject</a></b></li>\r\n");
      out.write("\t\t\t\t\t<li><b><a href=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/newprojectnextyear\">New\r\n");
      out.write("\t\t\t\t\t\t\tProject next year</a></b></li> \r\n");
      out.write("\t\t\t\t</ul></li>\r\n");
      out.write("\t\t\t<li>\r\n");
      out.write("\t\t\t\t<div class=\"container\">\r\n");
      out.write("\t\t\t\t\t<a href=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/projectlist\">\r\n");
      out.write("\t\t\t\t\t<img class=\"imagestyle\" alt=\"projectlisticon\" src='bootstrap/images/projectlist.png' \r\n");
      out.write("\t\t\t\t\t\tonmouseover=\"this.src='bootstrap/images/projectlistblack.png';\" \r\n");
      out.write("\t\t\t\t\t\tonmouseout=\"this.src='bootstrap/images/projectlist.png';\" /> Project List</a>\r\n");
      out.write("\t\t\t\t\t");
 if(projectNum > oldProjectNum){ //if I have more projects that the last time I loged in load plus green icon
      out.write("\r\n");
      out.write("\t\t\t\t\t\t<img class=\"imagestylesmall\" id = \"image\" alt=\"newicon\" src='bootstrap/images/new.png'/>\r\n");
      out.write("\t\t\t\t\t\t<div class=\"middle\">\r\n");
      out.write("\t\t\t\t\t\t\t<span class=\"text\">New Projects!</span>\r\n");
      out.write("\t\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t");
} 
      out.write("\t\t \t\r\n");
      out.write("\t\t\t\t</div>\r\n");
      out.write("\t\t\t</li>\r\n");
      out.write("\t\t\t<li>\r\n");
      out.write("\t\t\t\t<div class=\"container\">\r\n");
      out.write("\t\t\t\t\t<a href=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/checklistlist\">\r\n");
      out.write("\t\t\t\t\t<img class=\"imagestyle\" alt=\"scheduleicon\" src='bootstrap/images/schedule.png' \r\n");
      out.write("\t\t\t\t\t\tonmouseover=\"this.src='bootstrap/images/scheduleblack.png';\" \r\n");
      out.write("\t\t\t\t\t\tonmouseout=\"this.src='bootstrap/images/schedule.png';\" /> Schedule</a>\r\n");
      out.write("\t\t\t\t\t");
 if(eventNum > oldEventNum){ //if I have more projects that the last time I loged in load plus green icon
      out.write("\r\n");
      out.write("\t\t\t\t\t\t<img class=\"imagestylesmall\" id = \"image\" alt=\"newicon\" src='bootstrap/images/new.png'/>\r\n");
      out.write("\t\t\t\t\t\t\t<div class=\"middle\">\r\n");
      out.write("\t\t\t\t\t\t\t\t<span class=\"text\">New Events!</span>\r\n");
      out.write("\t\t\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t\t");
} 
      out.write("\t\t\t\t\t\r\n");
      out.write("\t\t\t\t</div>\r\n");
      out.write("\t\t\t</li>\r\n");
      out.write("\t\t\t");

				} else if (user == 2) { //student menu
			
      out.write("\r\n");
      out.write("\t\t\t<li>\r\n");
      out.write("\t\t\t\t<div class=\"container\">\r\n");
      out.write("\t\t\t\t\t<a href=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/projectlist\">\r\n");
      out.write("\t\t\t\t\t<img class=\"imagestyle\" alt=\"projectlisticon\" src='bootstrap/images/projectlist.png' \r\n");
      out.write("\t\t\t\t\t\tonmouseover=\"this.src='bootstrap/images/projectlistblack.png';\" \r\n");
      out.write("\t\t\t\t\t\tonmouseout=\"this.src='bootstrap/images/projectlist.png';\" /> Project List</a>\r\n");
      out.write("\t\t\t\t\t");
 if(projectNum > oldProjectNum){ //if I have more projects that the last time I loged in load plus green icon
      out.write("\r\n");
      out.write("\t\t\t\t\t\t<img class=\"imagestylesmall\" id = \"image\" alt=\"newicon\" src='bootstrap/images/new.png'/>\r\n");
      out.write("\t\t\t\t\t\t<div class=\"middle\">\r\n");
      out.write("\t\t\t\t\t\t\t<span class=\"text\">New Projects!</span>\r\n");
      out.write("\t\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t");
} 
      out.write("\t\t\t \t\r\n");
      out.write("\t\t\t\t</div>\r\n");
      out.write("\t\t\t</li>\r\n");
      out.write("\t\t\t<li>\r\n");
      out.write("\t\t\t\t<div class=\"container\">\r\n");
      out.write("\t\t\t\t\t<a href=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/checklistlist\">\r\n");
      out.write("\t\t\t\t\t<img class=\"imagestyle\" alt=\"scheduleicon\" src='bootstrap/images/schedule.png' \r\n");
      out.write("\t\t\t\t\t\tonmouseover=\"this.src='bootstrap/images/scheduleblack.png';\" \r\n");
      out.write("\t\t\t\t\t\tonmouseout=\"this.src='bootstrap/images/schedule.png';\" /> Schedule</a>\r\n");
      out.write("\t\t\t\t\t");
 if(eventNum > oldEventNum){ //if I have more projects that the last time I loged in load plus green icon
      out.write("\r\n");
      out.write("\t\t\t\t\t\t<img class=\"imagestylesmall\" id = \"image\" alt=\"newicon\" src='bootstrap/images/new.png'/>\r\n");
      out.write("\t\t\t\t\t\t\t<div class=\"middle\">\r\n");
      out.write("\t\t\t\t\t\t\t\t<span class=\"text\">New Events!</span>\r\n");
      out.write("\t\t\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t\t");
} 
      out.write("\t\t\t\t\t\r\n");
      out.write("\t\t\t\t</div>\r\n");
      out.write("\t\t\t</li>\r\n");
      out.write("\t\t\t<li><a\r\n");
      out.write("\t\t\t\thref=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/projectinterestedlist\">\r\n");
      out.write("\t\t\t\t<img class=\"imagestyle\" alt=\"studentlisticon\" src='bootstrap/images/studentlist.png' \r\n");
      out.write("\t\t\t\tonmouseover=\"this.src='bootstrap/images/studentlistblack.png';\" \r\n");
      out.write("\t\t\t\tonmouseout=\"this.src='bootstrap/images/studentlist.png';\" />\r\n");
      out.write("\t\t\t\tYour interest list</a></li>\r\n");
      out.write("\t\t\t<li><a \r\n");
      out.write("\t\t\t\thref=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/projectproposal\">\r\n");
      out.write("\t\t\t\t<img class=\"imagestyle\" alt=\"projectproposalicon\" src='bootstrap/images/projectproposal.png' \r\n");
      out.write("\t\t\t\tonmouseover=\"this.src='bootstrap/images/projectproposalblack.png';\" \r\n");
      out.write("\t\t\t\tonmouseout=\"this.src='bootstrap/images/projectproposal.png';\" />\r\n");
      out.write("\t\t\t\tPropose a project</a>\r\n");
      out.write("\t\t\t</li>\r\n");
      out.write("\t\t\t");

				}		
				if (user != 3) {
			
      out.write("\r\n");
      out.write("\t\t\t<li>\r\n");
      out.write("\t\t\t\t<a href=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/contactus\">\r\n");
      out.write("\t\t\t\t<img class=\"imagestyle\" alt=\"contactusicon\" src='bootstrap/images/envelope.png' \r\n");
      out.write("\t\t\t\tonmouseover=\"this.src='bootstrap/images/envelopeblack.png';\" \r\n");
      out.write("\t\t\t\tonmouseout=\"this.src='bootstrap/images/envelope.png';\" />\r\n");
      out.write("\t\t\t\tContactUs</a>\r\n");
      out.write("\t\t\t</li>\r\n");
      out.write("\t\t\t");

				}
				if (user == 0) {//if user is 0 means that I still did not login so login menu will be show
			
      out.write("\r\n");
      out.write("\t\t\t<li>\r\n");
      out.write("\t\t\t\t<a href=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/login\">\r\n");
      out.write("\t\t\t\t<img class=\"imagestyle\" alt=\"loginicon\" src='bootstrap/images/login.png' \r\n");
      out.write("\t\t\t\tonmouseover=\"this.src='bootstrap/images/loginblack.png';\" \r\n");
      out.write("\t\t\t\tonmouseout=\"this.src='bootstrap/images/login.png';\" />\r\n");
      out.write("\t\t\t\tLogin</a>\r\n");
      out.write("\t\t\t</li>\r\n");
      out.write("\t\t\t");

				} else { //if I am already login within the system, then I will show the logout option
			
      out.write("\r\n");
      out.write("\t\t\t<li>\r\n");
      out.write("\t\t\t\t<a href=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/logout\">\r\n");
      out.write("\t\t\t\t<img class=\"imagestyle\" alt=\"logouticon\" src='bootstrap/images/logout.png' \r\n");
      out.write("\t\t\t\tonmouseover=\"this.src='bootstrap/images/logoutblack.png';\" \r\n");
      out.write("\t\t\t\tonmouseout=\"this.src='bootstrap/images/logout.png';\" />\r\n");
      out.write("\t\t\t\tLogout</a>\r\n");
      out.write("\t\t\t</li>\r\n");
      out.write("\t\t\t");
 }
			//general menu
			
      out.write("\r\n");
      out.write("\t\t</ul>\r\n");
      out.write("\t</div>\r\n");
      out.write("</nav>");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
