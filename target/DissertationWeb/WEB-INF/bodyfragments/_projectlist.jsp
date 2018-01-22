<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="org.dissertationWeb.classes.Project;" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<h1>List of projects</h1>
<script type="text/javascript">
<%-- Global variable to keep the actual ID, this variable will be update in the modalPopulator function
in this way I always will have the actual ID of the project open in the modal --%>
var actualID;

function modalPopulator(title,description,projectID,topics,compulsoryReading, lecturerName, lecturerEmail) {

    $(".modal-title").html(title);
    $("#modal-description").html(description);
    $("#modal-topics").html(topics);
    $("#modal-compulsoryReading").html(compulsoryReading);
    $("#modal-lecturerName").html(lecturerName);
    $("#modal-lecturerEmail").html(lecturerEmail);
    actualID = projectID;
}
<%-- Method that pass as value to the edit method on the backend the ID of the actual projet that the modal have open right now --%>
function getProjectID() { 
    var projectID = document.getElementById("modal-edit-id");
    var projectIDRemove = document.getElementById("modal-remove-id");
    var projectRegisterInterest = document.getElementById("modal-registerinterest-id");
    var removeRegisterInterest = document.getElementById("modal-removeinterest-id");
    projectID.value = actualID;
    projectIDRemove.value = actualID;
    projectRegisterInterest.value = actualID;
    removeRegisterInterest.value = actualID;
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
 <%-- Method that show options based on which kind of user you are --%>
window.onload = function() {
	var user = document.getElementById("userType").value;
   	var userdiv = document.getElementById("modal-footer-user");
   	var admindiv = document.getElementById("modal-footer-admin");
   	var dcdiv = document.getElementById("modal-footer-dc");
   	
    if(user == 1){<%--Lecturer--%>
    	admindiv.style.display = 'visible';
        userdiv.style.display = 'none';
        dcdiv.style.display = 'none';
    }
    if(user == 2){<%--Student--%>
        dcdiv.style.display = 'none';
        admindiv.style.display = 'none';
        userdiv.style.display = 'visible';
    }
    if(user == 3){<%--Dissetation coordinator--%>
        admindiv.style.display = 'none';
        userdiv.style.display = 'none';
        dcdiv.style.display = 'visible';
    }
   
}

function removeXml(unsafe) {
alert(unsafe);
    return unsafe.replace(/[<>&'"]/g, function (c) {
        switch (c) {
            case '<': return '&lt;';
            case '>': return '&gt;';
            case '&': return '&amp;';
            case '\'': return '&apos;';
            case '"': return '&quot;';
        }
    });
}
 </script>
<form:form method="post" action="search">
	<table>
		<tr>
			<td>Search:</td>
			<td><textarea id="search-value-id" rows="1" cols="50">Introduce search criteria...</textarea></td>
		</tr>
		<tr>
			<td colspan="2"><input onclick="checkone(this);" type="checkbox"
				name="lecturer" value="lecturer" id="lecturer" checked>
				Lecturer <input onclick="checkone(this);" type="checkbox"
				name="technology" value="technology" id="technology">
				Technology <input onclick="checkone(this);" type="checkbox"
				name="title" value="title" id="title"> Title
				<button onclick="getSearchValue();" id="search-id"
					name="searchValue" class="btn btn-success" value=" ">Search</button>
			</td>
		</tr>
	</table>
</form:form>

<%-- The item within the {} must be the same name that the variable pass 
to the view from the controller or the variable names from the class --%>
<input type="hidden" id="userType" name="userType" value="${userType}">

<c:forEach items="${projectList}" var="project">
<c:set var = "title" value="${fn:replace(project.title, '\"', '\\'')}" />
<c:set var = "description" value="${fn:replace(project.description, '\"', '\\'')}" />
<c:set var = "topics" value="${fn:replace(project.topics, '\"', '\\'')}" />
<c:set var = "readings" value="${fn:replace(project.compulsoryReading, '\"', '\\'')}" />

	<li><a
		onclick='modalPopulator("${fn:escapeXml(title)}","${fn:escapeXml(description)}","${project.projectID}",
  "${fn:escapeXml(topics)}","${fn:escapeXml(readings)}","${project.user.username}","${project.user.email}")'
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
			<%--In this list lecturer cannot do nothing, if they want to edit projects they need to go to their personal project list
			in the lecturer menu--%>
			<div class="modal-footer" id="modal-footer-admin">
				<form:form method="post" action="remove">
					<button type="button" class="btn btn-secondary"
						data-dismiss="modal">Close</button>
				</form:form>
			</div>
			<%--User can only register interest--%>
			<div class="modal-footer" id="modal-footer-user">
				<form:form method="post" action="registerinterest">
					<button onclick="getProjectID();" id="modal-registerinterest-id"
						name="projectID" class="btn btn-success" value=" ">Register
						Interest</button>
				</form:form>
			</div>
			<%--Module coordinator can edit or remove any project--%>
			<div class="modal-footer" id="modal-footer-dc">
				<form:form method="post" action="edit">
					<button onclick="getProjectID();" id="modal-edit-id"
						name="projectID" class="btn btn-success" value=" ">Edit</button>
				</form:form>
				<form:form method="post" action="remove">
					<button onclick="getProjectID();" id="modal-remove-id"
						name="projectID" class="btn btn-danger" value=" ">Remove
						Project</button>
					<button type="button" class="btn btn-secondary"
						data-dismiss="modal">Close</button>
				</form:form>
			</div>
		</div>
	</div>
</div>
