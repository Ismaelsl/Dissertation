<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%-- The item within the {} must be the same name that the string use on the modal on the controller method --%>
<h4>Event Name: ${eventname}</h4>
Date: ${date}
<br>
Place: ${place}
<br>
Description: ${description}
<br>
<form:form method="get" action="${previousPage}">
	<button class="btn btn-success" value=" ">Go back Schedule</button>
</form:form>
<form:form method="get" action="home">
	<button class="btn btn-success" value=" ">Home</button>
</form:form>