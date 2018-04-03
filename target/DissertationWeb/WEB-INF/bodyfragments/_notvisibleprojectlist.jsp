<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script type="text/javascript">
<%-- Global variable to keep the actual ID, this variable will be update in the modalPopulator function
in this way I always will have the actual ID of the project open in the modal --%>
var actualID;

function modalPopulator(title,description,projectID,visible, approved,topics,compulsoryReading, lecturerName, lecturerEmail) {
    $(".modal-title").html( title );
    $("#modal-description").html(description );
    $("#modal-topics").html(topics );
    $("#modal-compulsoryReading").html(compulsoryReading );
    $("#modal-lecturerName").html(lecturerName );
    $("#modal-lecturerEmail").html(lecturerEmail );
    $("#modal-visible").html(visible );
    $("#modal-approved").html(approved );
    actualID = projectID;
}
<%-- Method that pass as value to the edit method on the backend the ID of the actual projet that the modal have open right now --%>
function getProjectID() { 
    var projectID = document.getElementById("modal-edit-id");
    var projectIDRemove = document.getElementById("modal-remove-id");
    var projectIDVisible = document.getElementById("modal-visible-id");

    projectID.value = actualID;
    projectIDRemove.value = actualID;
    projectIDVisible.value = actualID;

}
<%-- Method that pass as value the search criteria to the back end --%>
function getSearchValue() {
    var search = document.getElementById("search-id");
    var searchValue = document.getElementById("search-value-id").value;
    search.value = searchValue;
}

 function checkone(d){
 	if (!d.checked) return; //if it's unchecked, then do nothing
 	var os=document.getElementsByTagName('input');
 	for (var i=0;i<os.length;i++){
    	if (os[i].checked&&os[i]!=d) os[i].checked=false;
  	} 
 }
 </script>
<h4>Click on the box to see project details</h4>
<h1>Projects that are not visible for students for ${actualYear}</h1>
<h2 class ="importantmessage">${message}</h2>
<%-- The item within the {} must be the same name that the variable pass 
to the view from the controller or the variable names from the class --%>
<input type="hidden" id="userType" name="userType" value="${userType}">
<div class="divjumper2">
<c:forEach items="${projectNotVisibles}" var="project"> 
<%-- Area where I am setting the values to into var to remove the special characters --%>
<c:set var = "title" value="${fn:replace(project.title, '\"', '\\'')}" />
<c:set var = "description" value="${fn:replace(project.description, '\"', '\\'')}" />
<c:set var = "topics" value="${fn:replace(project.topics, '\"', '\\'')}" />
<c:set var = "readings" value="${fn:replace(project.compulsoryReading, '\"', '\\'')}" />
	<div class="projectList"><b><a
		onclick='modalPopulator("${fn:escapeXml(title)}","${fn:escapeXml(description)}","${project.projectID}",
  		"${project.visible}","${project.waitingToBeApproved}","${fn:escapeXml(topics)}","${fn:escapeXml(readings)}",
  		"${project.user.username}","${project.user.email}")'
		href="#" class="test" id="userLoginButton" data-toggle="modal"
		data-target="#userModal"><div id="box1">Title: ${project.title}<br /> 
		<br />Technologies:  ${fn:escapeXml(topics)}<br />
		<br />Lecturer: ${project.user.username}</div></a></b>
	</div>
</c:forEach>
</div>
<div class="divjumper2"><%--This div is here to force a new line between the first and second list--%>
<h1>Your list of projects that are not visible (by students) for ${nextYear}</h1>
<h2 class ="importantmessage">${nextyearmessage}</h2>
<c:forEach items="${projectListNextYear}" var="project">
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
		data-target="#userModal"><div id="box1">Title: ${project.title}<br /> 
		<br />Technologies:  ${fn:escapeXml(topics)}<br />
		<br />Lecturer: ${project.user.username}</div></a></b>
	</div>
</c:forEach>
</div>
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
			<div class="modal-footer" id="modal-footer-admin">
				<form:form method="post" action="makeItVisible">
					<button onclick="getProjectID();" id="modal-visible-id"
						name="projectID" class="btn btn-success" value=" ">Make
						 visible</button>
				</form:form>
				<form:form method="post" action="edit">
					<button onclick="getProjectID();" id="modal-edit-id"
						name="projectID" class="btn btn-success" value=" ">Edit</button>
				</form:form>
				<form:form method="post" action="remove">
					<button onclick="getProjectID();" id="modal-remove-id"
						name="projectID" class="btn btn-danger" value=" ">Remove</button>
					<button type="button" class="btn btn-secondary"
						data-dismiss="modal">Close</button>
				</form:form>
			</div>
		</div>
	</div>
</div>
