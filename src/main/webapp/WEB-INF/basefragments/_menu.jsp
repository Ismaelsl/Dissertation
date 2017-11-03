<script type="text/javascript">
<%-- Here I should have an if statement, so based on the user type will show more or less menu--%>
}
 </script>
 <%--OLD MENU
<div class="form-group" style="padding: 5px;"> 
   <ul>
       <li><a href="${pageContext.request.contextPath}/login">Login</a></li>
      <li><a href="${pageContext.request.contextPath}/">Home</a></li>
       <li><a href="${pageContext.request.contextPath}/contactus">Contact Us</a></li>
       <li><a href="${pageContext.request.contextPath}/newproject">New Project</a></li>
       <li><a href="${pageContext.request.contextPath}/projectlist">Project List</a></li>
       <li><a href="${pageContext.request.contextPath}/projectlisttoapprove">Project List to approve</a></li> 
       <li><a href="${pageContext.request.contextPath}/newchecklist">New Checklist</a></li>
       <li><a href="${pageContext.request.contextPath}/checklistlist">List of Checklist</a></li>
       <li><a href="${pageContext.request.contextPath}/projectinterestedlist">Your project list(Student)</a></li>
       <li>Lecturer</li>
       <li><a href="${pageContext.request.contextPath}/projectlecturerlist">Your project list(Lecturer)</a></li>
       <li><a href="${pageContext.request.contextPath}/projectlistwithinterest">Your project with interest</a></li>
       <li><a href="${pageContext.request.contextPath}/projectlecturerlistnotvisible">Your not visible project list</a></li>
   </ul>
</div>--%>
<nav class="navbar navbar-inverse">
  <div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand" href="#">Dissertation application</a>
    </div>
    <ul class="nav navbar-nav">
      <li class="active"><a href="#">Home</a></li>
      <li class="dropdown">
        <a class="dropdown-toggle" data-toggle="dropdown" href="#">Lecturer lists
        <span class="caret"></span></a>
        <ul class="dropdown-menu">
 			<li><a href="${pageContext.request.contextPath}/projectlecturerlist">Your project list(Lecturer)</a></li>
       		<li><a href="${pageContext.request.contextPath}/projectlistwithinterest">Your project with interest</a></li>
      		<li><a href="${pageContext.request.contextPath}/projectlecturerlistnotvisible">Your not visible project list</a></li>
        </ul>
      </li>
        <li class="dropdown">
        <a class="dropdown-toggle" data-toggle="dropdown" href="#">Coordinator menu
        <span class="caret"></span></a>
        <ul class="dropdown-menu">
 			<li><a href="${pageContext.request.contextPath}/projectlisttoapprove">Project List to approve</a></li> 
	       	<li><a href="${pageContext.request.contextPath}/newchecklist">New Checklist</a></li>
	       	<li><a href="${pageContext.request.contextPath}/studentlist">Student list</a></li>
	       	<li><a href="${pageContext.request.contextPath}/previousyearprojects">Previous year projects</a></li>
        </ul>
      </li>
	      	<li><a href="${pageContext.request.contextPath}/login">Login</a></li>
	      	<li class="active"><a href="${pageContext.request.contextPath}/logout">Logout</a></li>
	      	<%-- <li><a href="${pageContext.request.contextPath}/">Home</a></li>--%>
	       	<li><a href="${pageContext.request.contextPath}/contactus">Contact Us</a></li>
	       	<li><a href="${pageContext.request.contextPath}/newproject">New Project</a></li>
	       	<li><a href="${pageContext.request.contextPath}/projectlist">Project List</a></li>
	       	<li><a href="${pageContext.request.contextPath}/checklistlist">List of Checklist</a></li>
	       	<li><a href="${pageContext.request.contextPath}/projectinterestedlist">Your project list(Student)</a></li>
    </ul>
  </div>
</nav>