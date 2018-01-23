<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<h1>List of Students</h1>
<script type="text/javascript">
<%-- Global variable to keep the actual ID, this variable will be update in the modalPopulator function
in this way I always will have the actual ID of the project open in the modal --%>
var actualID;

function modalPopulator(username,userID,email) {
    $("#modal-username").html( username );
    $("#modal-email").html(email );  
    actualID = userID;
}
<%-- Method that pass as value to the edit method on the backend the ID of the actual projet that the modal have open right now --%>
function getStudentID() {
    var studentID = document.getElementById("modal-studentproject-id");
    studentID.value = actualID;
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
<form:form method="post" action="searchStudent">
	<table>
		<tr>
			<td>Search:</td>
			<td><textarea id="search-value-id" rows="1" cols="50">Introduce search criteria...</textarea></td>
		</tr>
		<tr>
			<td colspan="2"><input onclick="checkone(this);" type="checkbox"
				name="name" value="name" id="name" checked>
				Name <input onclick="checkone(this);" type="checkbox"
				name="email" value="email" id="email">Email
				<button onclick="getSearchValue();" id="search-id"
					name="searchValue" class="btn btn-success" value=" ">Search</button>
			</td>
		</tr>
	</table>
</form:form>
<%-- The item within the {} must be the same name that the variable pass 
to the view from the controller or the variable names from the class --%>
<input type="hidden" id="userType" name="userType" value="${userType}">
<c:forEach items="${studentList}" var="student">
<c:set var = "username" value="${fn:replace(student.username, '\"', '\\'')}" />
	<li><a
		onclick='modalPopulator("${fn:escapeXml(username)}","${student.userID}","${fn:escapeXml(student.email)}")'
		href="#" class="test" id="userLoginButton" data-toggle="modal"
		data-target="#userModal">Student name: ${student.username}</a></li>
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
						<b>Student name:</b>
						<div class="col-md-12" id="modal-username"></div>
					</div>
					<div class="row">
						<b>Email:</b>
						<div class="col-md-12" id="modal-email"></div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<form:form method="post" action="getstudentprojects">
					<button onclick="getStudentID();" id="modal-studentproject-id"
						name="studentID" class="btn btn-success" value=" ">Student project list</button>
					<button type="button" class="btn btn-secondary"
						data-dismiss="modal">Close</button>
				</form:form>
			</div>
		</div>
	</div>
</div>
<form:form method="get" action="home">
	<button class="btn btn-success" value=" ">Go back Home</button>
</form:form>