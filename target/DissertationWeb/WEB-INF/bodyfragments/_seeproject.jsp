<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<%-- The item within the {} must be the same name that the string use on the modal on the controller method --%>
<div class="form_css">	
<h1>This is how the projects will looks</h1>
<div class="w3-ul w3-card-4">
<b>Project title:</b> ${title}
<br>
<b>Year:</b> ${year}
<br>
<b>Description:</b> ${description}
<br>
<b>Lecturer</b>
<br>
<b>Name:</b> ${lecturername}
<br>
<b>Email:</b> ${lectureremail}
<br>
<b>Visible:</b> ${visible}
<br>
<%--A small if statement to show a more clear message based on the boolean value --%>
<c:if test="${not waitingapprove}"><b>Waiting to be approved:</b> Waiting approval</c:if>
<c:if test="${waitingapprove}"><b>Waiting to be approved:</b> Approve!</c:if>
<br>
<b>Topics:</b> ${topics}
<br>
<b>Compulsory Readings:</b> ${compulsoryReading}
<br>
<br>
</div>
<br>
<%--This if statement it is checking where you are coming from and redirecting to the previous page
I need to have two situations since the first one only works when you create new projects but the second one works with the rest
of situations --%>
<c:if test="${not fn:containsIgnoreCase(previousPage, 'projectlisttoapprove')}">
    <form:form method="get" action="projectlecturerlist">
		<button id="gobackhome" class="btn btn-success" value=" ">Go back</button>
	</form:form>
</c:if>
<c:if test="${fn:containsIgnoreCase(previousPage, 'projectlisttoapprove')}">
    <form:form method="get" action="${previousPage}">
		<button id="gobackhome" class="btn btn-success" value=" ">Go back</button>
	</form:form>
</c:if>
</div>
