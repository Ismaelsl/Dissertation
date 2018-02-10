<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link href="bootstrap/css/mycss.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<script>
<%--Function to remove the text from the textarea when you click over--%>
function clearContents(element) {
  element.value = '';
}
</script>

<div class="form_css">
<h1 class ="errormessage">${message}</h1>
	<div class="w3-container w3-teal">	
		<h1>New event for the schedule</h1>
	</div>
	<form:form method="post" action="savechecklist">
		<table class ="inputtable">
			<tr>
				<td style="border:none;"><b>Date :</b></td>
				<td style="border:none;"><form:input class="w3-input" type="date" path="date" required='true'
						value='<fmt:formatDate value="" pattern="dd-MM-yyyy" />' /></td>
			</tr>
			<tr>
				<td style="border:none;"><b>Event Name :</b></td>
				<td style="border:none;"><form:input class="w3-input" path="eventName" required='true' /></td>
			</tr>
			<tr>
				<td style="border:none;"><b>Place :</b></td>
				<%--I am adding and extra info here to be as much clear as possible
				I am assuming that for an event the room maybe is now known, so if you write TBA
				You can always edit the event later --%>
				<td style="border:none;" ><form:input class="w3-input" path="place" required='true' 
				placeholder="TBA if you are not sure" onfocus="clearContents(this);"/></td>
			</tr>
			<tr>
				<td style="border:none;"><b>Description :</b></td>
				<td style="border:none;"><form:input class="w3-input" path="description" required='true' size="45" /></td>
			</tr>
			<tr>
				<td style="border:none;"><b>Starting Time :</b></td>
				<td style="border:none;"><form:input class="w3-input" type="time" path="hour" required='true' size="45" /></td>
			</tr>
			<tr>
				<td style="border:none;"><b>Ending Time :</b></td>
				<td style="border:none;"><form:input class="w3-input" type="time" path="endHour" required='true' size="45" /></td>
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

