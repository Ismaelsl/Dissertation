<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h1>Your personal list of projects</h1>
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
    $("#modal-studentName").html("No student had apply for this project yet" );
    actualID = projectID;
}
function modalInterestPopulator(title,description,projectID,topics,compulsoryReading, lecturerName, lecturerEmail, studentName) {
    $(".modal-title").html( title );
    $("#modal-description").html(description );
    $("#modal-topics").html(topics );
    $("#modal-compulsoryReading").html(compulsoryReading );
    $("#modal-lecturerName").html(lecturerName );
    $("#modal-lecturerEmail").html(lecturerEmail );
    $("#modal-studentName").html(studentName );
    actualID = projectID;
}
<%-- Method that pass as value to the edit method on the backend the ID of the actual projet that the modal have open right now --%>
function getChecklistID() { 
    var removeRegisterInterest = document.getElementById("modal-removeinterest-id");
    removeRegisterInterest.value = actualID;
}
function getUserData(id, userList) { 
alert(id);
    var userID = document.getElementById("userData");
    var user;
    for (i = 0; i < userList.length; i++) { 
    if(userList[i] == id){
    user = userList[i].email;
    }
	}
    userID.value = user;
}
 </script>
<%-- The item within the {} must be the same name that the variable pass 
to the view from the controller or the variable names from the class --%>
<input type="hidden" id="userType" name="userType" value="${userType}">
<c:forEach items="${projectList}" var="project">
	<li><a
		onclick='modalPopulator("${project.title}","${project.description}","${project.projectID}",
  "${project.topics}","${project.compulsoryReading}","${project.user.username}","${project.user.email}")'
		href="#" class="test" id="userLoginButton" data-toggle="modal"
		data-target="#userModal">Project title: ${project.title}</a></li>
</c:forEach>
You have students who had interest on your projects!
<c:forEach items="${projectWithInterest}" var="project">
	<li><a
		onclick='modalInterestPopulator("${project.title}","${project.description}","${project.projectID}",
  "${project.topics}","${project.compulsoryReading}","${project.user.username}","${project.user.email}",
  "${project.student.username}")'
		href="#" class="test" id="userLoginButton" data-toggle="modal"
		data-target="#userModal">Project title: ${project.title}</a></li>
</c:forEach>
<!-- User login Modal -->
<div class="modal fade" id="userModal" tabindex="-1" role="dialog"
	aria-labelledby="profileModal" aria-hidden="true">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="exampleModalLongTitle">
					<span id="profileTitle"></span>
				</h5>
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<div class="container-fluid">
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
			<div class="modal-footer" id="modal-footer-user">
				<form:form method="post" action="removeinterest">
					<button onclick="getProjectID();" id="modal-removeinterest-id"
						name="projectID" class="btn btn-success" value=" ">
						Approve Student</button>
					<button onclick="getProjectID();" id="modal-removeinterest-id"
						name="projectID" class="btn btn-danger" value=" ">Reject
						Student</button>
					<button type="button" class="btn btn-secondary"
						data-dismiss="modal">Close</button>
				</form:form>
			</div>
		</div>
	</div>
</div>
