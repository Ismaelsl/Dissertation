<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<%-- The item within the {} must be the same name that the string use on the modal on the controller method --%>
<div class="form_css">	
<h1>Event: ${eventname}</h1>
<div class="w3-ul w3-card-4">
<b>Date: </b> ${date}
<br>
<b>Place: </b> ${place}
<br>
<b>Description: </b> ${description}
<br>
<b>Starting Time: </b> ${hour}
<br>
<b>Ending Time: </b> ${endhour}
</div>
<br>
<form:form method="get" action="checklistlist">
	<button class="btn btn-success" value=" ">Go back to Schedule</button>
</form:form>
</div>