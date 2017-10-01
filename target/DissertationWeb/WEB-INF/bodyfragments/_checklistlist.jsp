<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h1>List of Checklist</h1>
<script type="text/javascript">
<%-- Global variable to keep the actual ID, this variable will be update in the modalPopulator function
in this way I always will have the actual ID of the project open in the modal --%>
var actualID;
function modalPopulator(date,eventname,place, checklistID) {
    $(".modal-eventname").html( eventname );
    $("#modal-date").html(date );
    $("#modal-place").html(place );
     actualID = checklistID;
}
<%-- Method that pass as value to the edit method on the backend the ID of the actual projet that the modal have open right now --%>
function getChecklistID() { 
    var checklistID = document.getElementById("modal-edit-id");
    var checklistIDRemove = document.getElementById("modal-remove-id");
    checklistID.value = actualID;
    checklistIDRemove.value = actualID;
}
 </script>
<%-- The item within the {} must be the same name that the variable pass 
to the view from the controller or the variable names from the class --%>
<input type="hidden" id="userType" name="userType" value="${userType}">
<c:forEach items="${checklistList}" var="checklist">
	<li><a
		onclick='modalPopulator("${checklist.date}","${checklist.eventName}","${checklist.place}","${checklist.checkListID}")'
		href="#" class="test" id="userLoginButton" data-toggle="modal"
		data-target="#userModal">Checklist title: ${checklist.eventName}</a></li>
</c:forEach>
<!-- User login Modal -->
<div class="modal fade" id="userModal" tabindex="-1" role="dialog"
	aria-labelledby="profileModal" aria-hidden="true">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-eventname" id="exampleModalLongTitle">
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
						<b>Date:</b>
						<div class="col-md-12" id="modal-date"></div>
					</div>
					<div class="row">
						<b>Place:</b>
						<div class="col-md-12" id="modal-place"></div>
					</div>
				</div>
			</div>
			<div class="modal-footer" id="modal-footer-user">
				<form:form method="post" action="editChecklist">
					<button onclick="getChecklistID();" id="modal-edit-id"
						name="checklistID" class="btn btn-success" value=" ">Edit</button>
				</form:form>
				<form:form method="post" action="removeChecklist">
					<button onclick="getChecklistID();" id="modal-remove-id"
						name="checklistID" class="btn btn-danger" value=" ">Remove</button>
					<button type="button" class="btn btn-secondary"
						data-dismiss="modal">Close</button>
				</form:form>
			</div>
		</div>
	</div>
</div>
