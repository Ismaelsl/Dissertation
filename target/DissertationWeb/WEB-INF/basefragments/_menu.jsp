<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link href="bootstrap/css/mycss.css" rel="stylesheet" type="text/css" />

<nav class="navbar navbar-default">
	<div class="container-fluid" id="menuContainer">
		<div class="navbar-header">	
			<a href="${pageContext.request.contextPath}/home">
				<img src="bootstrap/images/logomenu.png" alt="logomenuicon" class="logomenustyle">
			</a>
		</div>
		<ul class="nav navbar-nav" id="menu"> 
			<%
				int user = 0;
				try {
					user = (Integer) session.getAttribute("userType");
				} catch (java.lang.NullPointerException e) {

				}
				if (user == 3) {
					//admin can see everything
			%>
			<li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#">
			<img src="bootstrap/images/dissertationcoordinator.png" alt="dissertationcoordinatoricon" class="imagestyle">
			Coordinator<span class="caret"></span></a>
				<ul class="dropdown-menu">
					<li><a
						href="${pageContext.request.contextPath}/projectlisttoapprove">Project
							List to approve</a></li>
					<li><a href="${pageContext.request.contextPath}/newchecklist">New
							enter to the schedule</a></li>
					<li><a href="${pageContext.request.contextPath}/studentlist">Student
							list</a></li>
					<li><a
						href="${pageContext.request.contextPath}/previousyearprojects">Previous
							year projects</a></li>
					<li><a
						href="${pageContext.request.contextPath}/allprojectsactualyear">See all projects
							current year</a></li>
					<li><a
						href="${pageContext.request.contextPath}/nextyearprojects">See all projects
							next year</a></li>
				</ul></li>
			<li class="dropdown" id="lecturer"><a class="dropdown-toggle" data-toggle="dropdown" href="#">
			
			Lecturer<span class="caret"></span></a>
				<ul class="dropdown-menu">
					<li><a
						href="${pageContext.request.contextPath}/projectlecturerlist">Your
							project list(Lecturer)</a></li>
					<li><a
						href="${pageContext.request.contextPath}/projectlistwithinterest">Your
							project with interest</a></li>
					<li><a
						href="${pageContext.request.contextPath}/projectlecturerlistnotvisible">Your
							not visible project list</a></li>
					<li><a href="${pageContext.request.contextPath}/newproject">New
							Project</a></li>
					<li><a href="${pageContext.request.contextPath}/newprojectnextyear">New
							Project next year</a></li> 
				</ul></li>
			<li>
				<a href="${pageContext.request.contextPath}/projectlist">
				<img src="bootstrap/images/projectlist.png" alt="projectlisticon" class="imagestyle">
				Project List</a>
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/checklistlist">
				<img src="bootstrap/images/schedule.png" alt="scheduleicon" class="imagestyle">
				Schedule</a>
			</li>
			<%
				} else if (user == 1) {// lecturer menu
			%>

			<li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#">
			<img src="bootstrap/images/lecturer.png" alt="lecturermenuicon" class="imagestyle">
			Lecturer<span class="caret"></span></a>
				<ul class="dropdown-menu">
					<li><a
						href="${pageContext.request.contextPath}/projectlecturerlist">Your
							project list(Lecturer)</a></li>
					<li><a
						href="${pageContext.request.contextPath}/projectlistwithinterest">Your
							project with interest</a></li>
					<li><a
						href="${pageContext.request.contextPath}/projectlecturerlistnotvisible">Your
							not visible project list</a></li>
					<li><a href="${pageContext.request.contextPath}/newproject">New
							Project</a></li>
					<li><a href="${pageContext.request.contextPath}/newprojectnextyear">New
							Project next year</a></li> 
				</ul></li>
			<li>
				<a href="${pageContext.request.contextPath}/projectlist">
				<img src="bootstrap/images/projectlist.png" alt="projectlisticon" class="imagestyle">
				Project List</a>
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/checklistlist">
				<img src="bootstrap/images/schedule.png" alt="scheduleicon" class="imagestyle">
				Schedule</a>
			</li>
			<%
				} else if (user == 2) { //student menu
			%>
			<li>
				<a href="${pageContext.request.contextPath}/projectlist">
				<img src="bootstrap/images/projectlist.png" alt="projectlisticon" class="imagestyle">
				Project List</a>
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/checklistlist">
				<img src="bootstrap/images/schedule.png" alt="scheduleicon" class="imagestyle">
				Schedule</a>
			</li>
			<li><a
				href="${pageContext.request.contextPath}/projectinterestedlist">Your
					project list(Student)</a></li>
			<li><a href="${pageContext.request.contextPath}/projectproposal">Propose a project</a></li>
			<%
				}
				//general menu
			%>
			<li>
				<a href="${pageContext.request.contextPath}/contactus">
				<img src="bootstrap/images/envelope.png" alt="envelopeicon" class="imagestyle">
				ContactUs</a>
			</li>
			<br>
			<%
				if (user == 0) {//if user is 0 means that I still did not login so login menu will be show
			%>
			<li>
				<a href="${pageContext.request.contextPath}/login">
				<img src="bootstrap/images/login.png" alt="loginicon" class="imagestyle">
				Login</a>
			</li>
			<%
				} else { //if I am already login within the system, then I will show the logout option
			%>
			<li>
				<a href="${pageContext.request.contextPath}/logout">
				<img src="bootstrap/images/logout.png" alt="logouticon" class="imagestyle">
				Logout</a>
			</li>
			<% } %>
		</ul>
	</div>
</nav>
