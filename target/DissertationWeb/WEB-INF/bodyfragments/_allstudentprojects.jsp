<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h1>List of projects that you are interested</h1>
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
    var withoutInterestdiv = document.getElementById("modal-footer-registerInterest");
   	var withInterestdiv = document.getElementById("modal-footer-removeInterest");
    withInterestdiv.style.visibility  = 'visible';
    withoutInterestdiv.style.visibility  = 'hidden';
    var userIDToApprove = document.getElementById("userIDToMakeVisible");
      var userIDToRemove = document.getElementById("userIDToRemove");
     userIDToApprove.value = userID;
     userIDToRemove.value = userID;
}

function modalPopulatorNotVisible(title,description,projectID,topics,compulsoryReading, lecturerName, lecturerEmail) {
    $(".modal-title").html( title );
    $("#modal-description").html(description );
    $("#modal-topics").html(topics );
    $("#modal-compulsoryReading").html(compulsoryReading );
    $("#modal-lecturerName").html(lecturerName );
    $("#modal-lecturerEmail").html(lecturerEmail );
    actualID = projectID;
     var withoutInterestdiv = document.getElementById("modal-footer-registerInterest");
   	var withInterestdiv = document.getElementById("modal-footer-removeInterest");
    withInterestdiv.style.visibility  = 'hidden';
    withoutInterestdiv.style.visibility  = 'visible';
        var userIDToApprove = document.getElementById("userIDToMakeVisible");
      var userIDToRemove = document.getElementById("userIDToRemove");
     userIDToApprove.value = userID;
     userIDToRemove.value = userID;
}
<%-- Method that pass as value to the edit method on the backend the ID of the actual projet that the modal have open right now --%>
function getProjectID() { 
    var removeRegisterInterest = document.getElementById("modal-removeinterest-id");
    var projectRegisterInterest = document.getElementById("modal-makeItVisible-id");
    removeRegisterInterest.value = actualID;
    projectRegisterInterest.value = actualID;
}

<%-- This function will decide which message to show based on the size of the list --%>
function chooseMessage(listSize, finalProject){
	if(listSize == 0){
		$("#secondList").html("You do not have any not visible projects");
	}else{
		$("#secondList").html("Those are the project that you make not visible, click on them if you want to make them visibles");
	}
	if(finalProject){
		$("#finalProject").html("The final project is");
	}else{
		$("#finalProject").html("Student does not have a final project yet");
	}
}
 </script>
<%-- The item within the {} must be the same name that the variable pass 
to the view from the controller or the variable names from the class --%>
<input type="hidden" id="userType" name="userType" value="${userType}">
<body onload='chooseMessage("${notInterestListSize}","${noFinalProject}")'>
	<h2>
		<div id="secondList"></div>
	</h2>

<c:forEach items="${projectListNotVisible}" var="project">
	<li><a
		onclick='modalPopulatorNotVisible("${project.title}","${project.description}","${project.projectID}",
  "${project.topics}","${project.compulsoryReading}","${project.user.username}","${project.user.email}")'
		href="#" class="test" id="userLoginButton" data-toggle="modal"
		data-target="#userModal">Project title: ${project.title}</a></li>
</c:forEach>
	<h2>
		<div id="finalProject"></div>
	</h2>
	<li><a
		onclick='modalPopulatorNotVisible("${finalProject.title}","${finalProject.description}","${finalProject.projectID}",
  "${finalProject.topics}","${finalProject.compulsoryReading}","${finalProject.user.username}","${finalProject.user.email}")'
		href="#" class="test" id="userLoginButton" data-toggle="modal"
		data-target="#userModal">Project title: ${finalProject.title}</a></li>
</body>
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
				</div>
			</div>
			<div class="modal-footer" id="modal-footer-removeInterest">
				<form:form method="post" action="removeinterestStudent"
					>
					<button onclick="getProjectID();" id="modal-removeinterest-id"
						name="projectID" class="btn btn-danger" value=" ">Remove
						Interest</button>
					<button type="button" class="btn btn-secondary"
						data-dismiss="modal">Close</button>
					
				</form:form>
			</div>
			<div class="modal-footer" id="modal-footer-registerInterest">
				<form:form method="post" action="makeInterestVisible"
					>
					<button onclick="getProjectID();" id="modal-makeItVisible-id"
						name="projectID" class="btn btn-success" value=" ">Make
						it visible</button>
					<button type="button" class="btn btn-secondary"
						data-dismiss="modal">Close</button>
				</form:form>
			</div>
		</div>
	</div>
</div>