<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
<%-- The item within the {} must be the same name that the variable pass 
to the view from the controller or the variable names from the class --%>
<h4>Click in any circle to see further details of the projects</h4>
<h1>Project list for the year ${year}</h1>
<c:forEach items="${projectList}" var="project">
<%-- Area where I am setting the values to into var to remove the special characters --%>
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
<div class="divjumperbuttons"><%--This div is here to force a new line between the first and second list--%></div>
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
					<div class="row">
						<b>Student</b>
						<div class="col-md-12" id="modal-studentName"></div>
					</div>
				</div>
			</div>
			<div class="modal-footer" id="modal-footer-admin">
				<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
			</div>

		</div>
	</div>
</div>
<br>
<form:form method="get" action="${previousPage}">
	<button class="btn btn-success" value=" ">Go back</button>
</form:form>
