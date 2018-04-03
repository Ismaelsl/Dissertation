<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div class="form_css">	
<h2>Registration for ${project.title} successful</h2>
<form:form method="get" action="${previousPage}">
	<button id="gobackhome" class="btn btn-success" value=" ">Go
		back</button>
</form:form>
</div>