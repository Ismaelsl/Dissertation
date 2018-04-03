<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script type="text/javascript">
<%-- Global variable to keep the actual ID, this variable will be update in the modalPopulator function
in this way I always will have the actual ID of the project open in the modal --%>
var actualID;

function modalPopulator(username,userID,email,year) {
    $("#modal-username").html( username );
    $("#modal-email").html(email );  
    $("#modal-year").html(year ); 
    actualID = userID;
}
<%-- Method that pass as value to the edit method on the backend the ID of the actual projet that the modal have open right now --%>
function getStudentID() {
    var studentID = document.getElementById("modal-studentproject-id");
    var editStudentID = document.getElementById("modal-editstudent-id");
    studentID.value = actualID;
    editStudentID.value = actualID;
}

<%-- Method that pass as value the search criteria to the back end --%>
function getSearchValue() {
    var search = document.getElementById("search-id");
    var searchValue = document.getElementById("search-value-id").value;
    search.value = searchValue;
}
<%-- Function that only allow to choose one of the checkboxes at the same time --%>
function checkone(d){
 	if (!d.checked) return; //if it's unchecked, then do nothing
 	var os=document.getElementsByTagName('input');
 	for (var i=0;i<os.length;i++){
    	if (os[i].checked&&os[i]!=d) os[i].checked=false;
  	} 
}
<%--Function to remove the text from the textarea when you click over--%>
function clearContents(element) {
  element.value = '';
}

 </script>
<form:form method="post" action="searchStudent">
	<div><b>Search student</b><br />
	<input onclick="checkone(this);" type="checkbox"
				name="name" value="name" id="name" checked>
				Name <input onclick="checkone(this);" type="checkbox"
				name="email" value="email" id="email">Email
				</div>
				<b>Student info:</b>
			<div><textarea class ="textareaSearch" id="search-value-id" 
			rows="1" cols="50" onfocus="clearContents(this);">Type here...</textarea>
			<button onclick="getSearchValue();" id="search-id"
					name="searchValue" class="btn btn-success" value=" ">Search student</button></div>
	

</form:form>
<h4>Click on the box to see student details</h4>
<h1>Student list for the year ${year }</h1>
<%-- The item within the {} must be the same name that the variable pass 
to the view from the controller or the variable names from the class --%>
<input type="hidden" id="userType" name="userType" value="${userType}">
<div class="divjumper2">
<c:forEach items="${studentList}" var="student">
<%-- Area where I am setting the values to into var to remove the special characters --%>
<c:set var = "username" value="${fn:replace(student.username, '\"', '\\'')}" />
	<div class="projectList"><b><a
		onclick='modalPopulator("${fn:escapeXml(username)}","${student.userID}","${fn:escapeXml(student.email)}","${student.year}")'
		href="#" class="test" id="userLoginButton" data-toggle="modal"
		data-target="#userModal"><div id="box1">Name: ${fn:escapeXml(username)}<br /> <br /> email: ${fn:escapeXml(student.email)}<br />
		 </div></a></b></div>
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
						<b>Student name:</b>
						<div class="col-md-12" id="modal-username"></div>
					</div>
					<div class="row">
						<b>Email:</b>
						<div class="col-md-12" id="modal-email"></div>
					</div>
					<div class="row">
						<b>Year:</b>
						<div class="col-md-12" id="modal-year"></div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<form:form method="post" action="getstudentprojects">
					<button onclick="getStudentID();" id="modal-studentproject-id"
						name="studentID" class="btn btn-success" value=" ">Student final project</button>
				</form:form>
				<form:form method="post" action="editstudent">
					<button onclick="getStudentID();" id="modal-editstudent-id"
						name="studentID" class="btn btn-primary" value=" ">Edit student</button>
					<button type="button" class="btn btn-secondary"
						data-dismiss="modal">Close</button>
				</form:form>
			</div>
		</div>
	</div>
</div>
<form:form method="get" action="studentlist">
	<button class="btn btn-success" value=" ">Go back</button>
</form:form>