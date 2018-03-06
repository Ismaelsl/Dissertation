<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="org.dissertationWeb.classes.Project;" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script type="text/javascript">
<%-- Global variable to keep the actual ID, this variable will be update in the modalPopulator function
in this way I always will have the actual ID of the project open in the modal --%>
var actualID;
<%
	//Area where I am getting the values from the session from the session
	int projectNum = 0;
	int oldProjectNum = 0;
	//Since menu will be load the first thing is normal that session still not exist 
	//so I am catching the error and keep the webapp running
	try {
		projectNum = (Integer) session.getAttribute("projectNum");
		oldProjectNum = (Integer) session.getAttribute("oldProjectNum");
	} catch (java.lang.NullPointerException e) {
	}
%>

function modalPopulator(title,description,projectID,topics,compulsoryReading, lecturerName, lecturerEmail) {

    $(".modal-title").html(title);
    $("#modal-description").html(description);
    $("#modal-topics").html(topics);
    $("#modal-compulsoryReading").html(compulsoryReading);
    $("#modal-lecturerName").html(lecturerName);
    $("#modal-lecturerEmail").html(lecturerEmail);
    actualID = projectID;
}
<%-- Method that pass as value to the edit method on the backend the ID of the actual projet that the modal have open right now --%>
function getProjectID() { 
    var projectID = document.getElementById("modal-edit-id");
    var projectIDRemove = document.getElementById("modal-remove-id");
    var projectRegisterInterest = document.getElementById("modal-registerinterest-id");
    var removeRegisterInterest = document.getElementById("modal-removeinterest-id");
    projectID.value = actualID;
    projectIDRemove.value = actualID;
    projectRegisterInterest.value = actualID;
    removeRegisterInterest.value = actualID;
}
<%-- Method that pass as value the search criteria to the back end --%>
function getSearchValue() {
    var search = document.getElementById("search-id");
    var searchValue = document.getElementById("search-value-id").value;
    search.value = searchValue;
}
<%-- Function that only allow to choose one of the checkboxes at the same time --%>
function checkone(d){
 	if (!d.checked) return; //if it's unchecked, then do nothing
 	var os=document.getElementsByTagName('input');
 	for (var i=0;i<os.length;i++){
    	if (os[i].checked&&os[i]!=d) os[i].checked=false;
  	} 
}
 <%-- Method that show options based on which kind of user you are --%>
window.onload = function() {
	var user = document.getElementById("userType").value;
	var studentyear = document.getElementById("studentYear").value;
	var actualyear = document.getElementById("actualYear").value;
   	var userdiv = document.getElementById("modal-footer-user");
   	var lecturerdiv = document.getElementById("modal-footer-admin");
   	var dcdiv = document.getElementById("modal-footer-dc");
   	var nextYearStudent = document.getElementById("modal-footer-nextyear");

    if(user == 1){<%--Lecturer--%>
   		lecturerdiv.style.display = 'visible';
        userdiv.style.display = 'none';
        dcdiv.style.display = 'none';
        nextYearStudent.style.display = 'none';
    }
    if(user == 2){<%--Student--%>
    	 if(studentyear != actualyear){<%--This cover if user is before of after the actual year--%>
     		dcdiv.style.display = 'none';
        	lecturerdiv.style.display = 'none';
        	nextYearStudent.style.display = 'visible';
        	userdiv.style.display = 'none';
     	}else{
     		dcdiv.style.display = 'none';
        	lecturerdiv.style.display = 'none';
        	nextYearStudent.style.display = 'none';
        	userdiv.style.display = 'visible';
     	}     
    }
    if(user == 3){<%--Dissetation coordinator--%>
        lecturerdiv.style.display = 'none';
        userdiv.style.display = 'none';
        nextYearStudent.style.display = 'none';
        dcdiv.style.display = 'visible';
    } 
}
<%--Function that is removing all the special characters from the modal--%>
function removeXml(unsafe) {
alert(unsafe);
    return unsafe.replace(/[<>&'"]/g, function (c) {
        switch (c) {
            case '<': return '&lt;';
            case '>': return '&gt;';
            case '&': return '&amp;';
            case '\'': return '&apos;';
            case '"': return '&quot;';
        }
    });
}
<%--Function to remove the text from the textarea when you click over--%>
function clearContents(element) {
  element.value = '';
}
 </script>
<form:form method="post" action="search">	
			<div><b>Filter by (checkbox)</b><br />
			<input onclick="checkone(this);" type="checkbox"
				name="lecturer" value="lecturer" id="lecturer" checked>
				Lecturer <input onclick="checkone(this);" type="checkbox"
				name="technology" value="technology" id="technology">
				Technology <input onclick="checkone(this);" type="checkbox"
				name="title" value="title" id="title"> Title 
			</div>
			<b>Filter term:</b>
			<div><textarea class ="textareaSearch" id="search-value-id" 
			rows="1" cols="50" onfocus="clearContents(this);">Type here...</textarea>
			<button onclick="getSearchValue();" id="search-id"
					name="searchValue" class="btn btn-success" value=" ">Filter</button></div>
</form:form>
<h4>Click in any circle to see further details of the projects</h4>
<h1>Project List</h1>
<%-- The item within the {} must be the same name that the variable pass 
to the view from the controller or the variable names from the class --%>
<input type="hidden" id="userType" name="userType" value="${userType}">
<input type="hidden" id="studentYear" name="userType" value="${studentYear}">
<input type="hidden" id="actualYear" name="userType" value="${actualYear}">
<c:forEach items="${projectList}" var="project" varStatus="status">
<%-- Area where I am setting the values to into var to remove the special characters --%>
	<c:set var = "title" value="${fn:replace(project.title, '\"', '\\'')}" />
	<c:set var = "description" value="${fn:replace(project.description, '\"', '\\'')}" />
	<c:set var = "topics" value="${fn:replace(project.topics, '\"', '\\'')}" />
	<c:set var = "readings" value="${fn:replace(project.compulsoryReading, '\"', '\\'')}" />  
		<div class="projectList"><b><a
			onclick='modalPopulator("${fn:escapeXml(title)}","${fn:escapeXml(description)}","${project.projectID}",
  			"${fn:escapeXml(topics)}","${fn:escapeXml(readings)}","${project.user.username}","${project.user.email}")'
			href="#" class="test" id="userLoginButton" data-toggle="modal"
			data-target="#userModal"><div id="box1">Title: ${project.title}<br /> 
			<br /> Technologies:  ${fn:escapeXml(topics)}<br />
			<br />Lecturer: ${project.user.username}</div></a></b>
		</div>	
		<% if(projectNum > oldProjectNum){ 
				//if I have more projects that the last time I logged in I will update the value of old project num to project num
				//so in this way the special icon and message in the menu will dissapear
				session.setAttribute("oldProjectNum", projectNum); 
		   	} %>
</c:forEach>
<c:if test="${not empty newProjectList}">
<div class="divjumper2"><%--This div is here to force a new line between the first and second list--%>
<h2>New Projects!</h2>
<c:forEach items="${newProjectList}" var="project" varStatus="status">
<%-- Area where I am setting the values to into var to remove the special characters --%>
	<c:set var = "title" value="${fn:replace(project.title, '\"', '\\'')}" />
	<c:set var = "description" value="${fn:replace(project.description, '\"', '\\'')}" />
	<c:set var = "topics" value="${fn:replace(project.topics, '\"', '\\'')}" />
	<c:set var = "readings" value="${fn:replace(project.compulsoryReading, '\"', '\\'')}" />  
		<div class="projectList"><b><a
			onclick='modalPopulator("${fn:escapeXml(title)}","${fn:escapeXml(description)}","${project.projectID}",
  			"${fn:escapeXml(topics)}","${fn:escapeXml(readings)}","${project.user.username}","${project.user.email}")'
			href="#" class="test" id="userLoginButton" data-toggle="modal"
			data-target="#userModal"><div id="box1">Title: ${project.title}<br /> 
			<br /> Technologies:  ${fn:escapeXml(topics)}<br />
			<br />Lecturer: ${project.user.username}</div></a></b>
		</div>	
		<% if(projectNum > oldProjectNum){ 
				//if I have more projects that the last time I logged in I will update the value of old project num to project num
				//so in this way the special icon and message in the menu will dissapear
				session.setAttribute("oldProjectNum", projectNum); 
		   	} %>
</c:forEach>
</div>
</c:if>
<!-- User login Modal -->
<div class="modal fade" id="userModal" tabindex="-1" role="dialog"
	aria-labelledby="profileModal" aria-hidden="true">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h3 class="modal-title" id="exampleModalLongTitle">
					<span id="profileTitle"></span>
				</h3>
				
			</div>
			<div class="modal-body">
				<div class="container-fluid">
					<div class="row">
						<b>Description:</b>
						<div class="col-md-12" id="modal-description"></div>
					</div>
					<div class="row">
						<b>Technologies:</b>
						<div class="col-md-12" id="modal-topics"></div>
					</div>
					<div class="row">
						<b>Compulsory readings:</b>
						<div class="col-md-12" id="modal-compulsoryReading"></div>
					</div>
					<div class="row">
						<b>Lecturer</b><br /> <b>Name:</b>
						<div class="col-md-12" id="modal-lecturerName"></div>
						<b>Email:</b>
						<div class="col-md-12" id="modal-lecturerEmail"></div>
					</div>
				</div>
			</div>
			<%--In this list lecturer cannot do nothing, if they want to edit projects they need to go to their personal project list
			in the lecturer menu--%>
			<div class="modal-footer" id="modal-footer-admin">
				<form:form method="post" action="remove">
					<button type="button" class="btn btn-secondary"
						data-dismiss="modal">Close</button>
				</form:form>
			</div>
			<div class="modal-footer" id="modal-footer-nextyear">
				<form:form method="post" action="">
					<b>You are not allowed to register yet, wait a little longer</b>
				</form:form>
			</div>
			<%--User can only register interest--%>
			<div class="modal-footer" id="modal-footer-user">
				<form:form method="post" action="registerinterest">
					<button onclick="getProjectID();" id="modal-registerinterest-id"
						name="projectID" class="btn btn-success" value=" ">Register
						Interest</button>
				</form:form>
			</div>
			<%--Module coordinator can edit or remove any project--%>
			<div class="modal-footer" id="modal-footer-dc">
				<form:form method="post" action="edit">
					<button onclick="getProjectID();" id="modal-edit-id"
						name="projectID" class="btn btn-success" value=" ">Edit Project</button>
				</form:form>
				<form:form method="post" action="remove">
					<button onclick="getProjectID();" id="modal-remove-id"
						name="projectID" class="btn btn-danger" value=" ">Remove
						Project</button>
					<button type="button" class="btn btn-secondary"
						data-dismiss="modal">Close</button>
				</form:form>
			</div>
		</div>
	</div>
</div>