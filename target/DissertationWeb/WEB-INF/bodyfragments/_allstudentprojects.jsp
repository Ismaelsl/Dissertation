<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<script type="text/javascript">
<%-- Global variable to keep the actual ID, this variable will be update in the modalPopulator function
in this way I always will have the actual ID of the project open in the modal --%>
var actualID;

function modalPopulator(title,description,projectID,topics,compulsoryReading, lecturerName, lecturerEmail) {
    $(".modal-title").html( title );
    $("#modal-description").html(description );
    $("#modal-topics").html(topics );
    $("#modal-compulsoryReading").html(compulsoryReading );
    $("#modal-lecturerName").html(lecturerName );
    $("#modal-lecturerEmail").html(lecturerEmail );
    actualID = projectID;

}

function modalPopulatorNotVisible(title,description,projectID,topics,compulsoryReading, lecturerName, lecturerEmail, user) {
    $(".modal-title").html( title );
    $("#modal-description").html(description );
    $("#modal-topics").html(topics );
    $("#modal-compulsoryReading").html(compulsoryReading );
    $("#modal-lecturerName").html(lecturerName );
    $("#modal-lecturerEmail").html(lecturerEmail );
    actualID = projectID;
    userID = user;
     var userIDToRemove = document.getElementById("userIDRemove");
     userIDToRemove.value = userID;
}
<%-- Method that pass as value to the edit method on the backend the ID of the actual projet that the modal have open right now --%>
function getProjectID() { 
    var removeRegisterInterest = document.getElementById("modal-removeinterest-id");
    var projectRegisterInterest = document.getElementById("modal-makeItVisible-id");
    removeRegisterInterest.value = actualID;
    projectRegisterInterest.value = actualID;
}
 </script>
<%-- The item within the {} must be the same name that the variable pass 
to the view from the controller or the variable names from the class --%>
<%--I am using and if statement to check if the student have or not a final project, if student have one
then this piece of code will be load--%>
<h1>Final Project</h1><h4>Click in any circle to see further details of the projects</h4>
<c:if test="${noFinalProject}">
	<h2>
		This is the final project chosen by the student ${finalProject.student.username}
	</h2>

	<c:set var = "title" value="${fn:replace(finalProject.title, '\"', '\\'')}" />
	<c:set var = "description" value="${fn:replace(finalProject.description, '\"', '\\'')}" />
	<c:set var = "topics" value="${fn:replace(finalProject.topics, '\"', '\\'')}" />
	<c:set var = "readings" value="${fn:replace(finalProject.compulsoryReading, '\"', '\\'')}" />
	<div class="projectList"><b><a
		onclick='modalPopulatorNotVisible("${fn:escapeXml(title)}","${fn:escapeXml(description)}","${finalProject.projectID}",
  "${fn:escapeXml(topics)}","${fn:escapeXml(readings)}","${finalProject.user.username}","${finalProject.user.email}", 
  "${finalProject.student.userID}")'<%-- I am passing the studentID since I need to keep this ID to know which final project remove --%>
		href="#" class="test" id="userLoginButton" data-toggle="modal"
		data-target="#userModal"><div id="box1">Title: ${finalProject.title}<br /> 
		<br /> Student:  ${finalProject.student.username}<br />
		<br />Lecturer: ${finalProject.user.username}</div></a></b></div>
<%--In here I am using the not in the test, which literally says if student has not final project then show this piece
of code--%>
</c:if>
	<c:if test="${not noFinalProject}">
	<h2>
		<div>This student has not final project</div>
	</h2>
</c:if>
<!-- User login Modal -->
<div class="modal fade" id="userModal" tabindex="-1" role="dialog"
	aria-labelledby="profileModal" aria-hidden="true">
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
						<b>Lecturer</b><br /> <b> Name:</b>
						<div class="col-md-12" id="modal-lecturerName"></div>
						<b>Email:</b>
						<div class="col-md-12" id="modal-lecturerEmail"></div>
					</div>
				</div>
			</div>
			<div class="modal-footer" id="modal-footer-removeInterest">
				<form:form method="post" action="removeinterestfinal"
					modelAttribute="user">
					<button onclick="getProjectID();" id="modal-removeinterest-id"
						name="projectID" class="btn btn-danger" value=" ">Remove
						Interest</button>
					<form:hidden id="userIDRemove" path="userID" value="${studentID }" />
					<button type="button" class="btn btn-secondary"
						data-dismiss="modal">Close</button>
				</form:form>
			</div>
		</div>
	</div>
</div>
<div class="divjumperbuttons"><%--This div is here to force a new line between the first and second list--%></div>
<form:form method="get" action="studentlist">
	<button class="btn btn-success" value=" ">Go back</button>
</form:form>