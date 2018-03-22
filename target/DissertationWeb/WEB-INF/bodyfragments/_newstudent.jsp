<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link href="bootstrap/css/mycss.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">

<div class="form_css">
<h1 class ="errormessage">${message}</h1>
	<div class="w3-container w3-teal">	
		<h1>New Student</h1>
	</div>
	<form:form method="post" action="savestudent">
		<table class ="inputtable">
			<tr>
				<td style="border:none;"><b>Name :</b></td>
				<td style="border:none;"><form:input class="w3-input" path="username" required='true'/></td>
			</tr>
			<tr>
				<td style="border:none;"><b>Email :</b></td>
				<td style="border:none;"><form:input class="w3-input" path="email" required='true' size="45" /></td>
			</tr>
			<tr>
				<td style="border:none;"><b>Year :</b></td>
				<td style="border:none;"><form:input class="w3-input" path="year" required='true' size="45" /></td>
			</tr>
			<tr>
				<td colspan="2" style="border:none;">
					<input type="submit" class="btn btn-success btn-lg" value="Save" /> 
					<input type="submit" name="cancel" value="Cancel" class="btn btn-danger btn-lg" form="homeForm" />
				</td>
			</tr>
		</table>
	</form:form>
</div>
<%--This form is the one that is telling to cancel input where to go if is pushed --%>
<form:form id="homeForm" method="get" action="home"></form:form>

