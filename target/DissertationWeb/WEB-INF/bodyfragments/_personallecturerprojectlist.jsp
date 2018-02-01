<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="org.dissertationWeb.classes.Project;" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script type="text/javascript">
<%-- Global variable to keep the actual ID, this variable will be update in the modalPopulator function
in this way I always will have the actual ID of the project open in the modal --%>
var actualID;
var userID;

function modalPopulator(title,description,projectID,visible, approved, topics,compulsoryReading, lecturerName, lecturerEmail) {
    $(".modal-title").html( title );
    $("#modal-description").html(description );
    $("#modal-topics").html(topics );
    $("#modal-compulsoryReading").html(compulsoryReading );
    $("#modal-lecturerName").html(lecturerName );
    $("#modal-lecturerEmail").html(lecturerEmail );
    $("#modal-visible").html(visible );
    $("#modal-approved").html(approved );
    $("#modal-studentName").html("No student had apply for this project yet" );
    actualID = projectID;

} 

<%-- Method that pass as value to the edit method on the backend the ID of the actual projet that the modal have open right now --%>
function getProjectID() { 
    var removeProject = document.getElementById("modal-removeinterest-id");
    var editProject = document.getElementById("modal-editinterest-id");

    removeProject.value = actualID;
    editProject.value = actualID;


}
<%-- This function will decide which message to show based on the size of the list --%>
function chooseMessage(listSize){
	if(listSize == 0){
		$("#secondList").html("For now none students applied to your projects");
	}else{
		$("#secondList").html("You have students who had interest on your projects!");
	}
}
 </script>
<h1>Your personal list of projects for ${actualYear}</h1>
<%-- The item within the {} must be the same name that the variable pass 
to the view from the controller or the variable names from the class --%>
<input type="hidden" id="userType" name="userType" value="${userType}">
<c:forEach items="${projectList}" var="project">
	<c:set var = "title" value="${fn:replace(project.title, '\"', '\\'')}" />
	<c:set var = "description" value="${fn:replace(project.description, '\"', '\\'')}" />
	<c:set var = "topics" value="${fn:replace(project.topics, '\"', '\\'')}" />
	<c:set var = "readings" value="${fn:replace(project.compulsoryReading, '\"', '\\'')}" />
	<div class="projectList"><b><a
		onclick='modalPopulator("${fn:escapeXml(title)}","${fn:escapeXml(description)}","${project.projectID}", "${project.visible}",
		"${project.waitingToBeApproved}", "${fn:escapeXml(topics)}","${fn:escapeXml(readings)}",
		"${project.user.username}","${project.user.email}")'
		href="#" class="test" id="userLoginButton" data-toggle="modal"
		data-target="#projectModal"><div id="box1">Title: ${project.title}<br /> 
		<br /> Technologies:  ${fn:escapeXml(topics)}<br />
		<br />Lecturer: ${project.user.username}</div></a></b>
	</div>
</c:forEach>

<div class="divjumper2"><%--This div is here to force a new line between the first and second list--%></div>
<h1>Your personal list of projects for ${nextYear}</h1>
<c:forEach items="${projectListNextYear}" var="project">
	<c:set var = "title" value="${fn:replace(project.title, '\"', '\\'')}" />
	<c:set var = "description" value="${fn:replace(project.description, '\"', '\\'')}" />
	<c:set var = "topics" value="${fn:replace(project.topics, '\"', '\\'')}" />
	<c:set var = "readings" value="${fn:replace(project.compulsoryReading, '\"', '\\'')}" />
	<div class="projectList"><b><a
		onclick='modalPopulator("${fn:escapeXml(title)}","${fn:escapeXml(description)}","${project.projectID}", "${project.visible}",
		"${project.waitingToBeApproved}", "${fn:escapeXml(topics)}","${fn:escapeXml(readings)}",
		"${project.user.username}","${project.user.email}")'
		href="#" class="test" id="userLoginButton" data-toggle="modal"
		data-target="#projectModal"><div id="box1">Title: ${project.title}<br /> 
		<br /> Technologies:  ${fn:escapeXml(topics)}<br />
		<br />Lecturer: ${project.user.username}</div></a></b>
	</div>
</c:forEach>

<!-- project Modal -->
<div class="modal fade" id="projectModal" tabindex="-1" role="dialog"
	aria-labelledby="projectModal" aria-hidden="true">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="exampleModalLongTitle">
					<span id="profileTitle"></span>
				</h5>
			</div>
			<div class="modal-body">
				<div class="container-fluid">
					<div class="row">
						<b>Visible:</b>
						<div class="col-md-12" id="modal-visible"></div>
					</div>
					<div class="row">
						<b>Approved:</b>
						<div class="col-md-12" id="modal-approved"></div>
					</div>
					<div class="row">
						<b>Description:</b>
						<div class="col-md-12" id="modal-description"></div>
					</div>
					<div class="row">
						<b>Topics:</b>
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
					<div class="row">
						<b>Student</b>
						<div class="col-md-12" id="modal-studentName"></div>
					</div>
				</div>
			</div>
			<div class="modal-footer" id="modal-footer-admin">
				<form:form method="post" action="edit">
					<button onclick="getProjectID();" id="modal-editinterest-id"
						name="projectID" class="btn btn-success" value=" ">Edit
						project</button>
				</form:form>
				<form:form method="post" action="remove">
					<button onclick="getProjectID();" id="modal-removeinterest-id"
						name="projectID" class="btn btn-danger" value=" ">Remove
						project</button>
					<button type="button" class="btn btn-secondary"
						data-dismiss="modal">Close</button>
				</form:form>
			</div>
		</div>
	</div>
</div>