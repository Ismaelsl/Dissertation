<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h1>List of projects</h1>
<script type="text/javascript">
<%-- Global variable to keep the actual ID, this variable will be update in the modalPopulator function
in this way I always will have the actual ID of the project open in the modal --%>
var actualID;
function modalPopulator(title,projectID,description,topics,compulsoryReading) {
    $(".modal-title").html( title );
    $("#modal-id").html(projectID );
    $("#modal-description").html(description );
    $("#modal-topics").html(topics );
    $("#modal-compulsoryReading").html(compulsoryReading );
    actualID = projectID;
}
<%-- Method that pass as value to the edit method on the backend the ID of the actual projet that the modal have open right now --%>
function getProjectID() {
    var projectID = document.getElementById("modal-edit-id");
    var projectIDRemove = document.getElementById("modal-remove-id");
    projectID.value = actualID;
    projectIDRemove.value = actualID;
}
 </script>
<%-- The item within the {} must be the same name that the variable pass 
to the view from the controller or the variable names from the class --%>
<c:forEach items="${projectList}" var="project">
	<li><a
		onclick='modalPopulator("${project.title}","${project.projectID}","${project.description}",
  "${project.topics}","${project.compulsoryReading}")'
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
						<b>ID:</b>
						<div class="col-md-4" id="modal-id"></div>
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
				</div>
			</div>
			<div class="modal-footer">
				<form:form method="post" action="edit">
					<button onclick="getProjectID();" id = "modal-edit-id" name="projectID" class="btn btn-success" value =" ">Edit</button>
				</form:form>
				<form:form method="post" action="remove">
					<button onclick="getProjectID();" id = "modal-remove-id" name="projectID" class="btn btn-danger" value =" ">Remove</button>
				    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
				</form:form>
			</div>
		</div>
	</div>
</div>
