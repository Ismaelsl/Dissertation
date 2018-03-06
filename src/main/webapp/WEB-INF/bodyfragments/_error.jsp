<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<div class="form_css">	
<%-- This message it will be different based on who call this view, in this way I only need
one error view that can be reuse within the whole web application --%>
${message}
<br />
<form:form method="get" action="home">
	<button class="btn btn-success" value=" ">Go back home</button>
</form:form>
</div>