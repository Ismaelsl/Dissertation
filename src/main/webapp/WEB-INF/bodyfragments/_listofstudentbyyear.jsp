<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
<%-- Method to only allow to choose one item from the checkbox --%>
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
    if(user == 1){
    	admindiv.style.display = 'visible';
        userdiv.style.display = 'none';
    }
    if(user == 2){
        admindiv.style.display = 'none';
        userdiv.style.display = 'visible';
    } 
}
 </script>
 <h4>Click in any circle to see further details of the year projects</h4>
<h1>Student list by year</h1>
<h3 class ="importantmessage">${message}</h3><%--This message it is use when you add new users using the add new user option in menu --%>
<%-- The item within the {} must be the same name that the variable pass 
to the view from the controller or the variable names from the class --%>
<input type="hidden" id="userType" name="userType" value="${userType}">
<c:forEach items="${yearList}" var="year">
	<form:form method="post" action="seestudentsbyyear">
		<button onclick="getProjectID();" id="modal-approveinterest-id"
			name="year" class="buttonyear" value="${year}">
			<div id="box1"><b>See students fot the year ${year}</b></div></button>
	</form:form>
</c:forEach>