/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/7.0.47
 * Generated at: 2018-03-28 20:54:45 UTC
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
      out.write("\t\t<ul class=\"nav navbar-nav\" id=\"menu\">\r\n");
      out.write("\t\t\t");

				//Session values are obtained here
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
      out.write("\t\t\t<li class=\"dropdown\"><a class=\"dropdown-toggle\"\r\n");
      out.write("\t\t\t\tdata-toggle=\"dropdown\" href=\"#\"> <img class=\"imagestyle\"\r\n");
      out.write("\t\t\t\t\talt=\"dissertationcoordinatoricon\"\r\n");
      out.write("\t\t\t\t\tsrc='bootstrap/images/dissertationcoordinator.png'\r\n");
      out.write("\t\t\t\t\tonmouseover=\"this.src='bootstrap/images/dissertationcoordinatorblack.png';\"\r\n");
      out.write("\t\t\t\t\tonmouseout=\"this.src='bootstrap/images/dissertationcoordinator.png';\" />\r\n");
      out.write("\t\t\t\t\tCoordinator<span class=\"caret\"></span></a>\r\n");
      out.write("\t\t\t\t<ul class=\"dropdown-menu\" id=\"ulmenustyle\">\r\n");
      out.write("\t\t\t\t\t<li><b><a\r\n");
      out.write("\t\t\t\t\t\t\thref=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/newchecklist\">New\r\n");
      out.write("\t\t\t\t\t\t\t\tschedule entry</a></b></li>\r\n");
      out.write("\t\t\t\t\t<li><b><a\r\n");
      out.write("\t\t\t\t\t\t\thref=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/projectlisttoapprove\">Project\r\n");
      out.write("\t\t\t\t\t\t\t\tList for approval</a></b></li>\r\n");
      out.write("\t\t\t\t\t<li><b><a\r\n");
      out.write("\t\t\t\t\t\t\thref=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/previousyearprojects\">Previous\r\n");
      out.write("\t\t\t\t\t\t\t\tyear projects</a></b></li>\r\n");
      out.write("\t\t\t\t\t<li><b><a\r\n");
      out.write("\t\t\t\t\t\t\thref=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/allprojectsactualyear\">Current \r\n");
      out.write("\t\t\t\t\t\t\tyear projects</a></b></li>\r\n");
      out.write("\t\t\t\t\t<li><b><a\r\n");
      out.write("\t\t\t\t\t\t\thref=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/nextyearprojects\">Forthcoming\r\n");
      out.write("\t\t\t\t\t\t\t\tyear projects</a></b></li>\r\n");
      out.write("\t\t\t\t\t<li><b><a\r\n");
      out.write("\t\t\t\t\t\t\thref=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/uploadonestudents\">Add\r\n");
      out.write("\t\t\t\t\t\t\t\tsingle student</a></b></li>\r\n");
      out.write("\t\t\t\t\t<li><b><a\r\n");
      out.write("\t\t\t\t\t\t\thref=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/uploadstudents\">Add\r\n");
      out.write("\t\t\t\t\t\t\t\tnew students</a></b></li>\r\n");
      out.write("\t\t\t\t\t<li><b><a\r\n");
      out.write("\t\t\t\t\t\t\thref=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/studentlist\">Manage \r\n");
      out.write("\t\t\t\t\t\t\tstudent list</a></b></li>\r\n");
      out.write("\t\t\t\t</ul></li>\r\n");
      out.write("\t\t\t<li class=\"dropdown\" id=\"lecturer\"><a class=\"dropdown-toggle\"\r\n");
      out.write("\t\t\t\tdata-toggle=\"dropdown\" href=\"#\"> <img class=\"imagestyle\"\r\n");
      out.write("\t\t\t\t\talt=\"lecturericon\" src='bootstrap/images/lecturer.png'\r\n");
      out.write("\t\t\t\t\tonmouseover=\"this.src='bootstrap/images/lecturerblack.png';\"\r\n");
      out.write("\t\t\t\t\tonmouseout=\"this.src='bootstrap/images/lecturer.png';\" /> Lecturer<span\r\n");
      out.write("\t\t\t\t\tclass=\"caret\"></span></a>\r\n");
      out.write("\t\t\t\t<ul class=\"dropdown-menu\" id=\"ulmenustyle\">\r\n");
      out.write("\t\t\t\t\t<li><b><a\r\n");
      out.write("\t\t\t\t\t\t\thref=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/projectlecturerlist\">My\r\n");
      out.write("\t\t\t\t\t\t\t\tproject list</a></b></li>\r\n");
      out.write("\t\t\t\t\t<li><b><a\r\n");
      out.write("\t\t\t\t\t\t\thref=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/projectlistwithinterest\">My\r\n");
      out.write("\t\t\t\t\t\t\t\tprojects with interest</a></b></li>\r\n");
      out.write("\t\t\t\t\t<li><b><a\r\n");
      out.write("\t\t\t\t\t\t\thref=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/projectlistwithinterestapproved\">My\r\n");
      out.write("\t\t\t\t\t\t\t\tapproved projects</a></b></li>\r\n");
      out.write("\t\t\t\t\t<li><b><a\r\n");
      out.write("\t\t\t\t\t\t\thref=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/projectlecturerlistnotvisible\">My\r\n");
      out.write("\t\t\t\t\t\t\t\tnot visible projects</a></b></li>\r\n");
      out.write("\t\t\t\t\t<li><b><a\r\n");
      out.write("\t\t\t\t\t\t\thref=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/newproject\">Create new\r\n");
      out.write("\t\t\t\t\t\t\t\tProject for current year</a></b></li>\r\n");
      out.write("\t\t\t\t\t<li><b><a\r\n");
      out.write("\t\t\t\t\t\t\thref=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/newprojectnextyear\">Create new\r\n");
      out.write("\t\t\t\t\t\t\t\tProject for forthcoming year</a></b></li>\r\n");
      out.write("\t\t\t\t</ul></li>\r\n");
      out.write("\t\t\t<li>\r\n");
      out.write("\t\t\t\t<div class=\"container\">\r\n");
      out.write("\t\t\t\t\t<a href=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/projectlist\"> <img\r\n");
      out.write("\t\t\t\t\t\tclass=\"imagestyle\" alt=\"projectlisticon\"\r\n");
      out.write("\t\t\t\t\t\tsrc='bootstrap/images/projectlist.png'\r\n");
      out.write("\t\t\t\t\t\tonmouseover=\"this.src='bootstrap/images/projectlistblack.png';\"\r\n");
      out.write("\t\t\t\t\t\tonmouseout=\"this.src='bootstrap/images/projectlist.png';\" />\r\n");
      out.write("\t\t\t\t\t\tProject List\r\n");
      out.write("\t\t\t\t\t</a>\r\n");
      out.write("\t\t\t\t\t");

						if (projectNum > oldProjectNum) { //if I have more projects that the last time I loged in load plus green icon
					
      out.write("\r\n");
      out.write("\t\t\t\t\t<img class=\"imagestylesmall\" id=\"image\" alt=\"newicon\"\r\n");
      out.write("\t\t\t\t\t\tsrc='bootstrap/images/new.png' />\r\n");
      out.write("\t\t\t\t\t<div class=\"middle\">\r\n");
      out.write("\t\t\t\t\t\t<span class=\"text\">New Projects!</span>\r\n");
      out.write("\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t");

						}
					
      out.write("\r\n");
      out.write("\t\t\t\t</div>\r\n");
      out.write("\t\t\t</li>\r\n");
      out.write("\t\t\t<li>\r\n");
      out.write("\t\t\t\t<div class=\"container\">\r\n");
      out.write("\t\t\t\t\t<a href=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/checklistlist\"> <img\r\n");
      out.write("\t\t\t\t\t\tclass=\"imagestyle\" alt=\"scheduleicon\"\r\n");
      out.write("\t\t\t\t\t\tsrc='bootstrap/images/schedule.png'\r\n");
      out.write("\t\t\t\t\t\tonmouseover=\"this.src='bootstrap/images/scheduleblack.png';\"\r\n");
      out.write("\t\t\t\t\t\tonmouseout=\"this.src='bootstrap/images/schedule.png';\" />\r\n");
      out.write("\t\t\t\t\t\tSchedule\r\n");
      out.write("\t\t\t\t\t</a>\r\n");
      out.write("\t\t\t\t\t");

						if (eventNum > oldEventNum) { //if I have more projects that the last time I loged in load plus green icon
					
      out.write("\r\n");
      out.write("\t\t\t\t\t<img class=\"imagestylesmall\" id=\"image\" alt=\"newicon\"\r\n");
      out.write("\t\t\t\t\t\tsrc='bootstrap/images/new.png' />\r\n");
      out.write("\t\t\t\t\t<div class=\"middle\">\r\n");
      out.write("\t\t\t\t\t\t<span class=\"text\">New Events!</span>\r\n");
      out.write("\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t");

						}
					
      out.write("\r\n");
      out.write("\t\t\t\t</div>\r\n");
      out.write("\t\t\t</li>\r\n");
      out.write("\t\t\t");

				} else if (user == 1) {// lecturer menu
			
      out.write("\r\n");
      out.write("\r\n");
      out.write("\t\t\t<li class=\"dropdown\"><a class=\"dropdown-toggle\"\r\n");
      out.write("\t\t\t\tdata-toggle=\"dropdown\" href=\"#\"> <img class=\"imagestyle\"\r\n");
      out.write("\t\t\t\t\talt=\"lecturericon\" src='bootstrap/images/lecturer.png'\r\n");
      out.write("\t\t\t\t\tonmouseover=\"this.src='bootstrap/images/lecturerblack.png';\"\r\n");
      out.write("\t\t\t\t\tonmouseout=\"this.src='bootstrap/images/lecturer.png';\" /> Lecturer<span\r\n");
      out.write("\t\t\t\t\tclass=\"caret\"></span></a>\r\n");
      out.write("\t\t\t\t<ul class=\"dropdown-menu\" id=\"ulmenustyle\">\r\n");
      out.write("\t\t\t\t\t<li><b><a\r\n");
      out.write("\t\t\t\t\t\t\thref=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/projectlecturerlist\">My\r\n");
      out.write("\t\t\t\t\t\t\t\tproject list</a></b></li>\r\n");
      out.write("\t\t\t\t\t<li><b><a\r\n");
      out.write("\t\t\t\t\t\t\thref=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/projectlistwithinterest\">My\r\n");
      out.write("\t\t\t\t\t\t\t\tprojects with interest</a></b></li>\r\n");
      out.write("\t\t\t\t\t<li><b><a\r\n");
      out.write("\t\t\t\t\t\t\thref=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/projectlistwithinterestapproved\">My\r\n");
      out.write("\t\t\t\t\t\t\t\tapproved projects</a></b></li>\r\n");
      out.write("\t\t\t\t\t<li><b><a\r\n");
      out.write("\t\t\t\t\t\t\thref=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/projectlecturerlistnotvisible\">My\r\n");
      out.write("\t\t\t\t\t\t\t\tnot visible projects</a></b></li>\r\n");
      out.write("\t\t\t\t\t<li><b><a\r\n");
      out.write("\t\t\t\t\t\t\thref=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/newproject\">Create new\r\n");
      out.write("\t\t\t\t\t\t\t\tProject for current year</a></b></li>\r\n");
      out.write("\t\t\t\t\t<li><b><a\r\n");
      out.write("\t\t\t\t\t\t\thref=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/newprojectnextyear\">Create new\r\n");
      out.write("\t\t\t\t\t\t\t\tProject for forthcoming year</a></b></li>\r\n");
      out.write("\t\t\t\t</ul></li>\r\n");
      out.write("\t\t\t<li>\r\n");
      out.write("\t\t\t\t<div class=\"container\">\r\n");
      out.write("\t\t\t\t\t<a href=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/projectlist\"> <img\r\n");
      out.write("\t\t\t\t\t\tclass=\"imagestyle\" alt=\"projectlisticon\"\r\n");
      out.write("\t\t\t\t\t\tsrc='bootstrap/images/projectlist.png'\r\n");
      out.write("\t\t\t\t\t\tonmouseover=\"this.src='bootstrap/images/projectlistblack.png';\"\r\n");
      out.write("\t\t\t\t\t\tonmouseout=\"this.src='bootstrap/images/projectlist.png';\" />\r\n");
      out.write("\t\t\t\t\t\tProject List\r\n");
      out.write("\t\t\t\t\t</a>\r\n");
      out.write("\t\t\t\t\t");

						if (projectNum > oldProjectNum) { //if I have more projects that the last time I loged in load plus green icon
					
      out.write("\r\n");
      out.write("\t\t\t\t\t<img class=\"imagestylesmall\" id=\"image\" alt=\"newicon\"\r\n");
      out.write("\t\t\t\t\t\tsrc='bootstrap/images/new.png' />\r\n");
      out.write("\t\t\t\t\t<div class=\"middle\">\r\n");
      out.write("\t\t\t\t\t\t<span class=\"text\">New Projects!</span>\r\n");
      out.write("\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t");

						}
					
      out.write("\r\n");
      out.write("\t\t\t\t</div>\r\n");
      out.write("\t\t\t</li>\r\n");
      out.write("\t\t\t<li>\r\n");
      out.write("\t\t\t\t<div class=\"container\">\r\n");
      out.write("\t\t\t\t\t<a href=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/checklistlist\"> <img\r\n");
      out.write("\t\t\t\t\t\tclass=\"imagestyle\" alt=\"scheduleicon\"\r\n");
      out.write("\t\t\t\t\t\tsrc='bootstrap/images/schedule.png'\r\n");
      out.write("\t\t\t\t\t\tonmouseover=\"this.src='bootstrap/images/scheduleblack.png';\"\r\n");
      out.write("\t\t\t\t\t\tonmouseout=\"this.src='bootstrap/images/schedule.png';\" />\r\n");
      out.write("\t\t\t\t\t\tSchedule\r\n");
      out.write("\t\t\t\t\t</a>\r\n");
      out.write("\t\t\t\t\t");

						if (eventNum > oldEventNum) { //if I have more projects that the last time I loged in load plus green icon
					
      out.write("\r\n");
      out.write("\t\t\t\t\t<img class=\"imagestylesmall\" id=\"image\" alt=\"newicon\"\r\n");
      out.write("\t\t\t\t\t\tsrc='bootstrap/images/new.png' />\r\n");
      out.write("\t\t\t\t\t<div class=\"middle\">\r\n");
      out.write("\t\t\t\t\t\t<span class=\"text\">New Events!</span>\r\n");
      out.write("\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t");

						}
					
      out.write("\r\n");
      out.write("\t\t\t\t</div>\r\n");
      out.write("\t\t\t</li>\r\n");
      out.write("\t\t\t");

				} else if (user == 2) { //student menu
			
      out.write("\r\n");
      out.write("\t\t\t<li>\r\n");
      out.write("\t\t\t\t<div class=\"container\">\r\n");
      out.write("\t\t\t\t\t<a href=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/projectlist\"> <img\r\n");
      out.write("\t\t\t\t\t\tclass=\"imagestyle\" alt=\"projectlisticon\"\r\n");
      out.write("\t\t\t\t\t\tsrc='bootstrap/images/projectlist.png'\r\n");
      out.write("\t\t\t\t\t\tonmouseover=\"this.src='bootstrap/images/projectlistblack.png';\"\r\n");
      out.write("\t\t\t\t\t\tonmouseout=\"this.src='bootstrap/images/projectlist.png';\" />\r\n");
      out.write("\t\t\t\t\t\tProject list\r\n");
      out.write("\t\t\t\t\t</a>\r\n");
      out.write("\t\t\t\t\t");

						if (projectNum > oldProjectNum) { //if I have more projects that the last time I loged in load plus green icon
					
      out.write("\r\n");
      out.write("\t\t\t\t\t<img class=\"imagestylesmall\" id=\"image\" alt=\"newicon\"\r\n");
      out.write("\t\t\t\t\t\tsrc='bootstrap/images/new.png' />\r\n");
      out.write("\t\t\t\t\t<div class=\"middle\">\r\n");
      out.write("\t\t\t\t\t\t<span class=\"text\">New Projects!</span>\r\n");
      out.write("\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t");

						}
					
      out.write("\r\n");
      out.write("\t\t\t\t</div>\r\n");
      out.write("\t\t\t</li>\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\t<li><a\r\n");
      out.write("\t\t\t\thref=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/projectinterestedlist\">\r\n");
      out.write("\t\t\t\t\t<img class=\"imagestyle\" alt=\"studentlisticon\"\r\n");
      out.write("\t\t\t\t\tsrc='bootstrap/images/studentlist.png'\r\n");
      out.write("\t\t\t\t\tonmouseover=\"this.src='bootstrap/images/studentlistblack.png';\"\r\n");
      out.write("\t\t\t\t\tonmouseout=\"this.src='bootstrap/images/studentlist.png';\" /> My\r\n");
      out.write("\t\t\t\t\tinterest list\r\n");
      out.write("\t\t\t</a></li>\r\n");
      out.write("\t\t\t<li><a href=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/projectproposal\">\r\n");
      out.write("\t\t\t\t\t<img class=\"imagestyle\" alt=\"projectproposalicon\"\r\n");
      out.write("\t\t\t\t\tsrc='bootstrap/images/projectproposal.png'\r\n");
      out.write("\t\t\t\t\tonmouseover=\"this.src='bootstrap/images/projectproposalblack.png';\"\r\n");
      out.write("\t\t\t\t\tonmouseout=\"this.src='bootstrap/images/projectproposal.png';\" />\r\n");
      out.write("\t\t\t\t\tPropose a project\r\n");
      out.write("\t\t\t</a></li>\r\n");
      out.write("\t\t\t<li>\r\n");
      out.write("\t\t\t\t<div class=\"container\">\r\n");
      out.write("\t\t\t\t\t<a href=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/checklistlist\"> <img\r\n");
      out.write("\t\t\t\t\t\tclass=\"imagestyle\" alt=\"scheduleicon\"\r\n");
      out.write("\t\t\t\t\t\tsrc='bootstrap/images/schedule.png'\r\n");
      out.write("\t\t\t\t\t\tonmouseover=\"this.src='bootstrap/images/scheduleblack.png';\"\r\n");
      out.write("\t\t\t\t\t\tonmouseout=\"this.src='bootstrap/images/schedule.png';\" />\r\n");
      out.write("\t\t\t\t\t\tSchedule\r\n");
      out.write("\t\t\t\t\t</a>\r\n");
      out.write("\t\t\t\t\t");

						if (eventNum > oldEventNum) { //if I have more projects that the last time I loged in load plus green icon
					
      out.write("\r\n");
      out.write("\t\t\t\t\t<img class=\"imagestylesmall\" id=\"image\" alt=\"newicon\"\r\n");
      out.write("\t\t\t\t\t\tsrc='bootstrap/images/new.png' />\r\n");
      out.write("\t\t\t\t\t<div class=\"middle\">\r\n");
      out.write("\t\t\t\t\t\t<span class=\"text\">New Events!</span>\r\n");
      out.write("\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t");

						}
					
      out.write("\r\n");
      out.write("\t\t\t\t</div>\r\n");
      out.write("\t\t\t</li>\r\n");
      out.write("\t\t\t");

				}
				if (user != 3) {
			
      out.write("\r\n");
      out.write("\t\t\t<li><a href=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/contactus\">\r\n");
      out.write("\t\t\t\t\t<img class=\"imagestyle\" alt=\"contactusicon\"\r\n");
      out.write("\t\t\t\t\tsrc='bootstrap/images/envelope.png'\r\n");
      out.write("\t\t\t\t\tonmouseover=\"this.src='bootstrap/images/envelopeblack.png';\"\r\n");
      out.write("\t\t\t\t\tonmouseout=\"this.src='bootstrap/images/envelope.png';\" />\r\n");
      out.write("\t\t\t\t\tContact us\r\n");
      out.write("\t\t\t</a></li>\r\n");
      out.write("\t\t\t");

				}
				if (user == 0) {//if user is 0 means that I still did not login so login menu will be show
			
      out.write("\r\n");
      out.write("\t\t\t<li><a href=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/login\"> <img\r\n");
      out.write("\t\t\t\t\tclass=\"imagestyle\" alt=\"loginicon\" src='bootstrap/images/login.png'\r\n");
      out.write("\t\t\t\t\tonmouseover=\"this.src='bootstrap/images/loginblack.png';\"\r\n");
      out.write("\t\t\t\t\tonmouseout=\"this.src='bootstrap/images/login.png';\" /> Login\r\n");
      out.write("\t\t\t</a></li>\r\n");
      out.write("\t\t\t");

				} else { //if I am already login within the system, then I will show the logout option
			
      out.write("\r\n");
      out.write("\t\t\t<li><a href=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("/logout\"> <img\r\n");
      out.write("\t\t\t\t\tclass=\"imagestyle\" alt=\"logouticon\"\r\n");
      out.write("\t\t\t\t\tsrc='bootstrap/images/logout.png'\r\n");
      out.write("\t\t\t\t\tonmouseover=\"this.src='bootstrap/images/logoutblack.png';\"\r\n");
      out.write("\t\t\t\t\tonmouseout=\"this.src='bootstrap/images/logout.png';\" /> Logout\r\n");
      out.write("\t\t\t</a></li>\r\n");
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
