<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div class="form_css">	
<h2>${message} had been removed successfully from student list</h2>
<h4>You can still see the project in your not visible project list</h4>
<form:form method="get" action="${previousPage}">
	<button id="gobackhome" class="btn btn-success" value=" ">Go
		back</button>
</form:form>
</div>