<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link href="bootstrap/css/mycss.css" rel="stylesheet" type="text/css" />

<nav class="navbar navbar-default">
	<div class="container-fluid" id="menuContainer">

		<ul class="nav navbar-nav" id="menu">  
			<%
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
			%>
			<li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#">
				<img class="imagestyle" alt="dissertationcoordinatoricon" 
				src='bootstrap/images/dissertationcoordinator.png' 
				onmouseover="this.src='bootstrap/images/dissertationcoordinatorblack.png';" 
				onmouseout="this.src='bootstrap/images/dissertationcoordinator.png';" />
			Coordinator<span class="caret"></span></a>
				<ul class="dropdown-menu" id="ulmenustyle">
					<li><b><a href="${pageContext.request.contextPath}/newchecklist">New
							enter to the schedule</a></b></li>
					<li><b><a
						href="${pageContext.request.contextPath}/projectlisttoapprove">Project
							List to approve</a></b></li>
					<li><b><a
						href="${pageContext.request.contextPath}/previousyearprojects">Previous
							year projects</a></b></li>
					<li><b><a
						href="${pageContext.request.contextPath}/allprojectsactualyear">See all projects
							current year</a></b></li>
					<li><b><a
						href="${pageContext.request.contextPath}/nextyearprojects">See all projects
							next year</a></b></li>
					<li><b><a
						href="${pageContext.request.contextPath}/uploadonestudents">Add one 
						student</a></b></li>
					<li><b><a
						href="${pageContext.request.contextPath}/uploadstudents">Add new 
						students</a></b></li>
					<li><b><a href="${pageContext.request.contextPath}/studentlist">Student
							list</a></b></li>
				</ul></li>
			<li class="dropdown" id="lecturer"><a class="dropdown-toggle" data-toggle="dropdown" href="#">
				<img class="imagestyle" alt="lecturericon" src='bootstrap/images/lecturer.png' 
				onmouseover="this.src='bootstrap/images/lecturerblack.png';" 
				onmouseout="this.src='bootstrap/images/lecturer.png';" />
				Lecturer<span class="caret"></span></a>
				<ul class="dropdown-menu" id="ulmenustyle">
					<li><b><a
						href="${pageContext.request.contextPath}/projectlecturerlist">Your
							list of projects</a></b></li>
					<li><b><a
						href="${pageContext.request.contextPath}/projectlistwithinterest">Your
							project with interest</a></b></li>
					<li><b><a
						href="${pageContext.request.contextPath}/projectlistwithinterestapproved">Your approved 
						projects</a></b></li>
					<li><b><a
						href="${pageContext.request.contextPath}/projectlecturerlistnotvisible">Your
							not visible project list</a></b></li>
					<li><b><a href="${pageContext.request.contextPath}/newproject">New
							Project</a></b></li>
					<li><b><a href="${pageContext.request.contextPath}/newprojectnextyear">New
							Project next year</a></b></li> 
				</ul></li>
			<li>
				<div class="container">
					<a href="${pageContext.request.contextPath}/projectlist">
					<img class="imagestyle" alt="projectlisticon" src='bootstrap/images/projectlist.png' 
						onmouseover="this.src='bootstrap/images/projectlistblack.png';" 
						onmouseout="this.src='bootstrap/images/projectlist.png';" /> Project List</a>
					<% if(projectNum > oldProjectNum){ //if I have more projects that the last time I loged in load plus green icon%>
						<img class="imagestylesmall" id = "image" alt="newicon" src='bootstrap/images/new.png'/>
						<div class="middle">
							<span class="text">New Projects!</span>
						</div>
					<%} %>			 	
				</div>
			</li>
			<li>
				<div class="container">
					<a href="${pageContext.request.contextPath}/checklistlist">
					<img class="imagestyle" alt="scheduleicon" src='bootstrap/images/schedule.png' 
						onmouseover="this.src='bootstrap/images/scheduleblack.png';" 
						onmouseout="this.src='bootstrap/images/schedule.png';" /> Schedule</a>
					<% if(eventNum > oldEventNum){ //if I have more projects that the last time I loged in load plus green icon%>
						<img class="imagestylesmall" id = "image" alt="newicon" src='bootstrap/images/new.png'/>
							<div class="middle">
								<span class="text">New Events!</span>
							</div>
						<%} %>				
				</div>
			</li>
			<%
				} else if (user == 1) {// lecturer menu
			%>

			<li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#">				
				<img class="imagestyle" alt="lecturericon" src='bootstrap/images/lecturer.png' 
				onmouseover="this.src='bootstrap/images/lecturerblack.png';" 
				onmouseout="this.src='bootstrap/images/lecturer.png';" />
			Lecturer<span class="caret"></span></a>
				<ul class="dropdown-menu" id="ulmenustyle">
					<li><b><a
						href="${pageContext.request.contextPath}/projectlecturerlist">Your
							list of projects</a></b></li>
					<li><b><a
						href="${pageContext.request.contextPath}/projectlistwithinterest">Your
							project with interest</a></b></li>
					<li><b><a
						href="${pageContext.request.contextPath}/projectlistwithinterestapproved">Your approved 
						projects</a></b></li>
					<li><b><a
						href="${pageContext.request.contextPath}/projectlecturerlistnotvisible">Your
							not visible project list</a></b></li>
					<li><b><a href="${pageContext.request.contextPath}/newproject">New
							Project</a></b></li>
					<li><b><a href="${pageContext.request.contextPath}/newprojectnextyear">New
							Project next year</a></b></li> 
				</ul></li>
			<li>
				<div class="container">
					<a href="${pageContext.request.contextPath}/projectlist">
					<img class="imagestyle" alt="projectlisticon" src='bootstrap/images/projectlist.png' 
						onmouseover="this.src='bootstrap/images/projectlistblack.png';" 
						onmouseout="this.src='bootstrap/images/projectlist.png';" /> Project List</a>
					<% if(projectNum > oldProjectNum){ //if I have more projects that the last time I loged in load plus green icon%>
						<img class="imagestylesmall" id = "image" alt="newicon" src='bootstrap/images/new.png'/>
						<div class="middle">
							<span class="text">New Projects!</span>
						</div>
					<%} %>		 	
				</div>
			</li>
			<li>
				<div class="container">
					<a href="${pageContext.request.contextPath}/checklistlist">
					<img class="imagestyle" alt="scheduleicon" src='bootstrap/images/schedule.png' 
						onmouseover="this.src='bootstrap/images/scheduleblack.png';" 
						onmouseout="this.src='bootstrap/images/schedule.png';" /> Schedule</a>
					<% if(eventNum > oldEventNum){ //if I have more projects that the last time I loged in load plus green icon%>
						<img class="imagestylesmall" id = "image" alt="newicon" src='bootstrap/images/new.png'/>
							<div class="middle">
								<span class="text">New Events!</span>
							</div>
						<%} %>					
				</div>
			</li>
			<%
				} else if (user == 2) { //student menu
			%>
			<li>
				<div class="container">
					<a href="${pageContext.request.contextPath}/projectlist">
					<img class="imagestyle" alt="projectlisticon" src='bootstrap/images/projectlist.png' 
						onmouseover="this.src='bootstrap/images/projectlistblack.png';" 
						onmouseout="this.src='bootstrap/images/projectlist.png';" /> Project List</a>
					<% if(projectNum > oldProjectNum){ //if I have more projects that the last time I loged in load plus green icon%>
						<img class="imagestylesmall" id = "image" alt="newicon" src='bootstrap/images/new.png'/>
						<div class="middle">
							<span class="text">New Projects!</span>
						</div>
					<%} %>			 	
				</div>
			</li>
			<li>
				<div class="container">
					<a href="${pageContext.request.contextPath}/checklistlist">
					<img class="imagestyle" alt="scheduleicon" src='bootstrap/images/schedule.png' 
						onmouseover="this.src='bootstrap/images/scheduleblack.png';" 
						onmouseout="this.src='bootstrap/images/schedule.png';" /> Schedule</a>
					<% if(eventNum > oldEventNum){ //if I have more projects that the last time I loged in load plus green icon%>
						<img class="imagestylesmall" id = "image" alt="newicon" src='bootstrap/images/new.png'/>
							<div class="middle">
								<span class="text">New Events!</span>
							</div>
						<%} %>					
				</div>
			</li>
			<li><a
				href="${pageContext.request.contextPath}/projectinterestedlist">
				<img class="imagestyle" alt="studentlisticon" src='bootstrap/images/studentlist.png' 
				onmouseover="this.src='bootstrap/images/studentlistblack.png';" 
				onmouseout="this.src='bootstrap/images/studentlist.png';" />
				Your interest list</a></li>
			<li><a 
				href="${pageContext.request.contextPath}/projectproposal">
				<img class="imagestyle" alt="projectproposalicon" src='bootstrap/images/projectproposal.png' 
				onmouseover="this.src='bootstrap/images/projectproposalblack.png';" 
				onmouseout="this.src='bootstrap/images/projectproposal.png';" />
				Propose a project</a>
			</li>
			<%
				}				
			%>
			<li>
				<a href="${pageContext.request.contextPath}/contactus">
				<img class="imagestyle" alt="contactusicon" src='bootstrap/images/envelope.png' 
				onmouseover="this.src='bootstrap/images/envelopeblack.png';" 
				onmouseout="this.src='bootstrap/images/envelope.png';" />
				ContactUs</a>
			</li>
			<%
				if (user == 0) {//if user is 0 means that I still did not login so login menu will be show
			%>
			<li>
				<a href="${pageContext.request.contextPath}/login">
				<img class="imagestyle" alt="loginicon" src='bootstrap/images/login.png' 
				onmouseover="this.src='bootstrap/images/loginblack.png';" 
				onmouseout="this.src='bootstrap/images/login.png';" />
				Login</a>
			</li>
			<%
				} else { //if I am already login within the system, then I will show the logout option
			%>
			<li>
				<a href="${pageContext.request.contextPath}/logout">
				<img class="imagestyle" alt="logouticon" src='bootstrap/images/logout.png' 
				onmouseover="this.src='bootstrap/images/logoutblack.png';" 
				onmouseout="this.src='bootstrap/images/logout.png';" />
				Logout</a>
			</li>
			<% }
			//general menu
			%>
		</ul>
	</div>
</nav>