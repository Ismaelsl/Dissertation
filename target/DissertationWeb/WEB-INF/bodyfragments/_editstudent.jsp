<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<div class="form_css">
	<div class="w3-container w3-blue">	
		<h1>Edit Student</h1>
	</div>
	<form:form method="post" action="saveEditStudent">
		<table class ="inputtable">
			<tr>
				<td style="border:none;"><b>Username :</b></td>
				<td style="border:none;"><form:input class="w3-input" path="username" required='true' /></td>
			</tr>
			<tr>
				<td style="border:none;"><b>Email :</b></td>
				<td style="border:none;"><form:input class="w3-input" path="email" required='true' /></td>
			</tr>
			<tr>
				<td style="border:none;"><b>Year :</b></td>
				<td style="border:none;"><form:input class="w3-input" path="year" required='true' size="55"/></td>
			</tr>
			<tr>
			<%--I am passing the rest of the student information as hidden since I do not need to edit this but I still need the data to be saved into the DB --%>
			<form:hidden path="userID" />
			<form:hidden path="password" />
			<form:hidden path="userType" />
				<td colspan="2" style="border:none;">
					<input type="submit" class="btn btn-success btn-lg" value="Save Changes" /> 
					<input type="submit" name="cancel" value="Cancel" class="btn btn-danger btn-lg" form="homeForm" />
				</td>
			</tr>
		</table>
	</form:form>
</div>
<%--This form is the one that is telling to cancel input where to go if is pushed --%>
<form:form id="homeForm" method="get" action="${previousPage}"></form:form>


