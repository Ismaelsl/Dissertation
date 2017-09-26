<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h1>List of projects</h1>
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
<%-- Method that pass as value to the edit method on the backend the ID of the actual projet that the modal have open right now --%>
function getProjectID() {
    var projectID = document.getElementById("modal-edit-id");
    var projectIDRemove = document.getElementById("modal-remove-id");
    projectID.value = actualID;
    projectIDRemove.value = actualID;
}
<%-- Method that pass as value the search criteria to the back end --%>
function getSearchValue() {
    var search = document.getElementById("search-id");
    var searchValue = document.getElementById("search-value-id").value;
    search.value = searchValue;
}

function checkboxes(){

    var lecturer = document.getElementById('lecturer');
    var technology = document.getElementById('technology');
    var title = document.getElementById('title');

    if (lecturer.checked === false && technology.checked === false && title.checked === false)
    {
    lecturer.checked = false;
      technology.checked = false;
      title.checked = false;
    }
    if (lecturer.checked === false && technology.checked === false && title.checked === true)
    {
    lecturer.checked = false;
      technology.checked = false;
      title.checked = true;
    }
    if (lecturer.checked === false && technology.checked === true && title.checked === false)
    {
    lecturer.checked = false;
      technology.checked = true;
      title.checked = false;
    }
    if (lecturer.checked === false && technology.checked === true && title.checked === true)
    {
    lecturer.checked = false;
      technology.checked = false;
      title.checked = true;
    }
    if (lecturer.checked === true && technology.checked === false && title.checked === false)
    {
    lecturer.checked = true;
      technology.checked = false;
      title.checked = false;
    }
    if (lecturer.checked === true && technology.checked === false && title.checked === true)
    {
    lecturer.checked = false;
      technology.checked = false;
      title.checked = true;
    }
    if (lecturer.checked === true && technology.checked === true && title.checked === false)
    {
    lecturer.checked = false;
      technology.checked = true;
      title.checked = false;
    }
    if (lecturer.checked === true && technology.checked === true && title.checked === true)
    {
    lecturer.checked = true;
      technology.checked = false;
      title.checked = false;
    }
 }
 function checkone(d){

 if (!d.checked) return; //if it's unchecked, then do nothing
  
 var group=document.getElementsByName('checkboxGroupName');

 var os=document.getElementsByTagName('input');
  
 for (var i=0;i<os.length;i++){

    if (os[i].checked&&os[i]!=d) os[i].checked=false;

  }
 }
 </script>
<form:form method="post" action="search">
	<table>
		<tr>
			<td>Search:</td>
			<td><textarea id="search-value-id" rows="1" cols="50">Introduce search criteria</textarea></td>
		</tr>
		<tr>
			<td colspan="2"><input onclick="checkone(this);" type="checkbox" name="checkboxGroupName"
				value="lecturer" id="lecturer" checked> Lecturer <input 
				onclick="checkone(this);" type="checkbox" name="checkboxGroupName" value="technology" id="technology">
				Technology <input onclick="checkone(this);" type="checkbox" name="checkboxGroupName" value="title"
				id="title"> Title
				<button onclick="getSearchValue();" id="search-id"
					name="searchValue" class="btn btn-success" value=" ">Search</button>
			</td>
		</tr>
	</table>
</form:form>

<%-- The item within the {} must be the same name that the variable pass 
to the view from the controller or the variable names from the class --%>
<input type="hidden" name="userID" value="${userType}">
<c:forEach items="${projectList}" var="project">
	<li><a
		onclick='modalPopulator("${project.title}","${project.description}","${project.projectID}",
  "${project.topics}","${project.compulsoryReading}","${project.user.username}","${project.user.email}")'
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
				</div>
			</div>
			<div class="modal-footer">
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
