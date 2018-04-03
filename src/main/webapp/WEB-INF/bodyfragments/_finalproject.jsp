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
    var withoutInterestdiv = document.getElementById("modal-footer-registerInterest");
   	var withInterestdiv = document.getElementById("modal-footer-removeInterest");
    withInterestdiv.style.visibility  = 'visible';
    withoutInterestdiv.style.visibility  = 'hidden';
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
}
<%-- Method that pass as value to the edit method on the backend the ID of the actual projet that the modal have open right now --%>
function getProjectID() { 
    var removeRegisterInterest = document.getElementById("modal-removeinterest-id");
    var projectRegisterInterest = document.getElementById("modal-makeItVisible-id");
    removeRegisterInterest.value = actualID;
    projectRegisterInterest.value = actualID;
}

 </script>
<h4>Click on the box to see project details</h4>
 <h1>Congratulations on your project selection</h1>
<%-- The item within the {} must be the same name that the variable pass 
to the view from the controller or the variable names from the class --%>
<input type="hidden" id="userType" name="userType" value="${userType}">
<c:forEach items="${projectList}" var="project">
<%-- Area where I am setting the values to into var to remove the special characters --%>
<c:set var = "title" value="${fn:replace(project.title, '\"', '\\'')}" />
<c:set var = "description" value="${fn:replace(project.description, '\"', '\\'')}" />
<c:set var = "topics" value="${fn:replace(project.topics, '\"', '\\'')}" />
<c:set var = "readings" value="${fn:replace(project.compulsoryReading, '\"', '\\'')}" />
	<div class="projectList"><b><a
		onclick='modalPopulator("${fn:escapeXml(title)}","${fn:escapeXml(description)}","${project.projectID}",
  "${fn:escapeXml(topics)}","${fn:escapeXml(readings)}","${project.user.username}","${project.user.email}")'
		href="#" class="test" id="userLoginButton" data-toggle="modal"
		data-target="#userModal"><div id="box1">Title: ${fn:escapeXml(title)}<br /> 
		<br />Technologies:  ${fn:escapeXml(topics)}<br />
		<br />Lecturer: ${project.user.username}</div></a></b></div>
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
						<b>Compulsory reading:</b>
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
			<div class="modal-footer" id="modal-footer-registerInterest">
				<form:form method="post" action="makeInterestVisible">
					<button type="button" class="btn btn-secondary"
						data-dismiss="modal">Close</button>
				</form:form>
			</div>
		</div>
	</div>
</div>
