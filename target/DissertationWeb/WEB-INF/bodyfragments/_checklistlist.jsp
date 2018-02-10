<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script type="text/javascript">
<%-- Global variable to keep the actual ID, this variable will be update in the modalPopulator function
in this way I always will have the actual ID of the project open in the modal --%>
var actualID;
<%
	//Area where I am getting the values from the session from the session
	int eventNum = 0;
	int oldEventNum = 0;
	int loopCounter = 1;
	//Since menu will be load the first thing is normal that session still not exist 
	//so I am catching the error and keep the webapp running
	try {
		eventNum = (Integer) session.getAttribute("eventNum");
		oldEventNum = (Integer) session.getAttribute("oldEventNum");
	} catch (java.lang.NullPointerException e) {
	}
%>
function modalPopulator(date,eventname,place, checklistID, description, hour, endhour) {
    $(".modal-eventname").html(eventname);
    $("#modal-date").html(date);
    $("#modal-hour").html(hour);
    $("#modal-endhour").html(endhour);
    $("#modal-place").html(place);
    $("#modal-description").html(description);
     actualID = checklistID;
    var visibletdiv = document.getElementById("visible");
   	var novisiblediv = document.getElementById("novisible");
    visibletdiv.style.display   = 'block';
    novisiblediv.style.display   = 'none';
}
function modalPopulatorNoVisible(date,eventname,place, checklistID, description, hour, endhour) {
    $(".modal-eventname").html(eventname);
    $("#modal-date").html(date);
    $("#modal-hour").html(hour);
    $("#modal-endhour").html(endhour);
    $("#modal-place").html(place);
    $("#modal-description").html(description);
     actualID = checklistID;
    var visibletdiv = document.getElementById("visible");
   	var novisiblediv = document.getElementById("novisible");
   	visibletdiv.style.display   = 'none';
    novisiblediv.style.display   = 'block';
}
<%-- Method that pass as value to the edit method on the backend the ID of the actual projet that the modal have open right now --%>
function getChecklistID() { 
    var checklistID = document.getElementById("modal-edit-id");
    var checklistIDRemove = document.getElementById("modal-remove-id");
    var editID = document.getElementById("modal-visible-id");
    var visibleID = document.getElementById("modal-edit2-id");
   
    checklistID.value = actualID;
    checklistIDRemove.value = actualID;
    editID.value = actualID;
    visibleID.value = actualID;
}
<%-- This function will decide which message to show based on the size of the list --%>
function chooseMessage(listSize){
	if(listSize == 0){
		$("#secondList").html("You do not have any hiden events");
	}else{
		$("#secondList").html("Here is the list of events that you make invisible to students");
	}
	menu(listSize);<%-- I am calling menu here since I have already one onload function and the first thing that I want to 
	happens is the menu load --%>
}
 <%-- Method that show options based on which kind of user you are --%>
function menu() {
	var user = document.getElementById("userType").value;
   	var userdiv = document.getElementById("modal-footer-user");
   	var dcdiv = document.getElementById("modal-footer-dc");
   	var notvisiblelist = document.getElementById("secondListProjects");
   	var secondList = document.getElementById("secondList");

    if(user == 1 || user == 2){<%--Lecturer or Student--%>
        userdiv.style.display = 'visible';
        dcdiv.style.display = 'none';
        notvisiblelist.style.display = 'none';
        secondList.style.display = 'none';
    }
    if(user == 3){<%--Dissetation coordinator--%>
        userdiv.style.display = 'none';
        dcdiv.style.display = 'visible';
    }
   
}
 </script>
 <h1>Schedule of events for CSCU9Z7 (In date order)</h1><h4>Click in any circle to see further details of the events</h4>
<h1 class="errormessage">${message}</h1><%--An error message to be show when need it --%>
<%-- The item within the {} must be the same name that the variable pass 
to the view from the controller or the variable names from the class --%>
<input type="hidden" id="userType" name="userType" value="${userType}">
<div class="divjumper2">
<%-- varStatus is an special JSP feature that is like a pointer, which have .last to tell you when you are in the last element of the loop --%>
<c:forEach items="${checklistList}" var="checklist" varStatus="status">
	<c:set var = "title" value="${fn:replace(checklist.eventName, '\"', '\\'')}" />
	<c:set var = "description" value="${fn:replace(checklist.description, '\"', '\\'')}" />
	<c:set var = "place" value="${fn:replace(checklist.place, '\"', '\\'')}" />
	<c:set var = "readings" value="${fn:replace(project.compulsoryReading, '\"', '\\'')}" />
		<div class="eventList"><b><a
			onclick='modalPopulator("${checklist.date}","${fn:escapeXml(title)}","${fn:escapeXml(place)}",
			"${checklist.checkListID}", "${fn:escapeXml(description)}", "${checklist.hour}","${checklist.endHour}")'
			href="#" class="test" id="userLoginButton" data-toggle="modal"
			data-target="#userModal">
			<div id="boxevents">Title: ${fn:escapeXml(title)}<br /> 
				<br /> Date: ${checklist.date}<br />
				<br />Place: ${fn:escapeXml(place)}<br />
				<br />Starting time: ${checklist.hour}
				<br />Ending time: ${checklist.endHour}
			</div></a></b>
		</div>
</c:forEach>
</div>
<c:if test="${not empty newEventList}">
<div class="divjumper2"><%--This div is here to force a new line between the first and second list--%>
<h2>New Events!</h2>
	<c:forEach items="${newEventList}" var="checklist" varStatus="status">
	<c:set var = "title" value="${fn:replace(checklist.eventName, '\"', '\\'')}" />
	<c:set var = "description" value="${fn:replace(checklist.description, '\"', '\\'')}" />
	<c:set var = "place" value="${fn:replace(checklist.place, '\"', '\\'')}" />
	<c:set var = "readings" value="${fn:replace(project.compulsoryReading, '\"', '\\'')}" />
		<div class="eventList"><b><a
			onclick='modalPopulator("${checklist.date}","${fn:escapeXml(title)}","${fn:escapeXml(place)}",
			"${checklist.checkListID}", "${fn:escapeXml(description)}", "${checklist.hour}","${checklist.endHour}")'
			href="#" class="test" id="userLoginButton" data-toggle="modal"
			data-target="#userModal"><div id="boxevents">Title: ${fn:escapeXml(title)}<br /> 
				<br /> Date: ${checklist.date}<br />
				<br />Place: ${fn:escapeXml(place)}<br />
				<br />Starting time: ${checklist.hour}
				<br />Ending time: ${checklist.endHour}
			</div></a></b>
			<% if(eventNum > oldEventNum){ 
				//if I have more events that the last time I logged in I will update the value of old event to event num
				//so in this way the special icon and message in the menu will dissapear
				session.setAttribute("oldEventNum", eventNum); 
		   		} %>
</c:forEach>
</div>
</c:if>

<div class="divjumper2"><%--This div is here to force a new line between the first and second list--%>
<body onload='chooseMessage("${notapprovedsize}")'>
	<h2 id="secondList"></h2>
</body>
<div id="secondListProjects">
<c:forEach items="${checklistListNotApproved}" var="checklist">
<c:set var = "title" value="${fn:replace(checklist.eventName, '\"', '\\'')}" />
<c:set var = "description" value="${fn:replace(checklist.description, '\"', '\\'')}" />
<c:set var = "place" value="${fn:replace(checklist.place, '\"', '\\'')}" />

	<div class="eventList"><b><a
		onclick='modalPopulatorNoVisible("${checklist.date}","${fn:escapeXml(title)}","${fn:escapeXml(place)}",
		"${checklist.checkListID}", "${fn:escapeXml(description)}","${checklist.hour}","${checklist.endHour}")'
		href="#" class="test" id="userLoginButton" data-toggle="modal"
		data-target="#userModal"><div id="boxevents">Title: ${fn:escapeXml(title)}<br /> 
				<br /> Date: ${checklist.date}<br />
				<br />Place: ${fn:escapeXml(place)}<br />
				<br />Starting time: ${checklist.hour}
				<br />Ending time: ${checklist.endHour}
			</div></a></b>
</c:forEach>
</div>
</div>
<!-- User login Modal -->
<div class="modal fade" id="userModal" tabindex="-1" role="dialog"
	aria-labelledby="profileModal" aria-hidden="true">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-eventname" id="exampleModalLongTitle">
					<span id="profileTitle"></span>
				</h5>
			</div>
			<div class="modal-body">
				<div class="container-fluid">
					<div class="row">
						<b>Date:</b>
						<div class="col-md-12" id="modal-date"></div>
						<b>Starting time:</b>
						<div class="col-md-12" id="modal-hour"></div>
						<b>Ending time:</b>
						<div class="col-md-12" id="modal-endhour"></div>
					</div>
					<div class="row">
						<b>Place:</b>
						<div class="col-md-12" id="modal-place"></div>
					</div>
					<div class="row">
						<b>Description:</b>
						<div class="col-md-12" id="modal-description"></div>
					</div>
				</div>
			</div>
			<%--This is the div that the coordinator will see, I have two of them since I have two list with different options
			One need edit and remove options and the other make it visible and edit options--%>
			<div class="modal-footer" id="modal-footer-dc">
				<div id="visible">
					<form:form method="post" action="editChecklist" class="formdisplay">
						<button onclick="getChecklistID();" id="modal-edit-id"
							name="checklistID" class="btn btn-success" value=" ">Edit Event</button>
					</form:form>
					<form:form method="post" action="removeChecklist" class="formdisplay">
						<button onclick="getChecklistID();" id="modal-remove-id"
							name="checklistID" class="btn btn-danger" value=" ">Remove Event</button>
						<button type="button" class="btn btn-secondary"
							data-dismiss="modal">Close</button>
					</form:form>
				</div>
				<div id="novisible">
					<form:form method="post" action="makeVisibleChecklist" class="formdisplay">
						<button onclick="getChecklistID();" id="modal-visible-id"
							name="checklistID" class="btn btn-primary" value=" ">Make
							Event Visible</button>
					</form:form>
					<form:form method="post" action="editChecklist" class="formdisplay">
						<button onclick="getChecklistID();" id="modal-edit2-id"
							name="checklistID" class="btn btn-success" value=" ">Edit Event</button>
						<button type="button" class="btn btn-secondary"
							data-dismiss="modal">Close</button>
					</form:form>
				</div>
			</div>
			<%--This is the button option that the student or lecturer will see--%>
			<div class="modal-footer" id="modal-footer-user">
				<button type="button" class="btn btn-secondary"
					data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>
