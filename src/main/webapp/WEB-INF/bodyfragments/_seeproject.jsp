<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%-- The item within the {} must be the same name that the string use on the modal on the controller method --%>
Project title: ${title}
<br>
ID: ${projectID}
<br>
Year: ${year}
<br>
Description: ${description}
<br>
Lecturer
<br>
Name: ${lecturername}
<br>
Email: ${lectureremail}
<br>
Visible: ${visible}
<br>
Document: ${documentID}
<br>
Waiting to be approved: ${waitingToBeApproved}
<br>
CheckList: ${checklistID}
<br>
Topics: ${topics}
<br>
Compulsory Readings: ${compulsoryReading}
<br>
<br>

<br>
<form:form method="get" action="projectlist">
	<button id="gobackhome" class="btn btn-success" value=" ">Go
		back</button>
</form:form>
