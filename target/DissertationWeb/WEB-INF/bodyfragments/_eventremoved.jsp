<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div class="form_css">	
<h2>${message} had been removed successfully from schedule</h2>
<h4>You as course coordinator can still see the event in the schedule, right in the bottom</h4>
<form:form method="get" action="${previousPage}">
	<button id="gobackhome" class="btn btn-success" value=" ">Go
		back</button>
</form:form>
</div>