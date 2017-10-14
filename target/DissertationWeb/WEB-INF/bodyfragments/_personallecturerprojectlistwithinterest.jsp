<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h1>Your personal list of projects</h1>
<script type="text/javascript">
<%-- Global variable to keep the actual ID, this variable will be update in the modalPopulator function
in this way I always will have the actual ID of the project open in the modal --%>
var actualID;
var userID;
 
function modalInterestPopulator(title,description,projectID,topics,compulsoryReading, lecturerName, lecturerEmail, studentName,user) {
    $(".modal-title").html( title );
    $("#modal-description").html(description );
    $("#modal-topics").html(topics );
    $("#modal-compulsoryReading").html(compulsoryReading );
    $("#modal-lecturerName").html(lecturerName );
    $("#modal-lecturerEmail").html(lecturerEmail );
    $("#modal-studentName").html(studentName );
    actualID = projectID; 
    userID = user;
     var userIDToApprove = document.getElementById("userID");
      var userIDToRemove = document.getElementById("userIDRemove");
     userIDToApprove.value = userID;
     userIDToRemove.value = userID;
}
<%-- Method that pass as value to the edit method on the backend the ID of the actual projet that the modal have open right now --%>
function getProjectID() { 
    var approveInterest = document.getElementById("modal-approveinterest-id");
    var removeInterest = document.getElementById("modal-removeinterestProject-id");

    approveInterest.value = actualID;
    removeInterest.value = actualID;

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
<%-- The item within the {} must be the same name that the variable pass 
to the view from the controller or the variable names from the class --%>
<h2>${message}</h2>
<input type="hidden" id="userType" name="userType" value="${userType}">

<%-- Need to use body here since onload does not work with all the tags and this was the most appropiate based on the situation --%>
<body onload='chooseMessage("${interestListSize}")'>
	<h2>
		<div id="secondList"></div>
	</h2>
</body>
<c:forEach items="${projectWithInterest}" var="project">
	<li><a
		onclick='modalInterestPopulator("${project.title}","${project.description}","${project.projectID}",
  "${project.topics}","${project.compulsoryReading}","${project.user.username}","${project.user.email}",
  "${project.student.username}","${project.student.userID}")'
		href="#" class="test" id="userLoginButton" data-toggle="modal"
		data-target="#projectModal">Project title: ${project.title}</a></li>
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
			<div class="modal-footer" id="modal-footer-admin">
				<form:form method="post" action="approveinterest" modelAttribute="user">
					<button onclick="getProjectID();" id="modal-approveinterest-id"
						name="projectID" class="btn btn-success" value=" ">Approve
						request</button>
						<form:hidden id = "userID" path="userID" value=""/>
				</form:form>
				<form:form method="post" action="removeinterest" modelAttribute="user">
					<button onclick="getProjectID();"
						id="modal-removeinterestProject-id" name="projectID"
						class="btn btn-danger" value=" ">Remove Request</button>
						<form:hidden id = "userIDRemove" path="userID" value=""/>
					<button type="button" class="btn btn-secondary"
						data-dismiss="modal">Close</button>
				</form:form>
			</div>
		</div>
	</div>
</div>