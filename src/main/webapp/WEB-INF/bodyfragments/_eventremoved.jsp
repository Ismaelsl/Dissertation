<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div class="form_css">	
<h2>${message} has been removed successfully from schedule</h2>
<h4>The course coordinator can still see this event at the bottom of the event schedule page</h4>
<form:form method="get" action="${previousPage}">
	<button id="gobackhome" class="btn btn-success" value=" ">Go
		back</button>
</form:form>
</div>