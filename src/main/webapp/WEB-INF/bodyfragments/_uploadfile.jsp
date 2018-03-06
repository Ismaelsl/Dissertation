<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link href="bootstrap/css/mycss.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">

<div class="form_css">
<h1 class ="errormessage">${message}</h1>
	<div class="w3-container w3-teal">	
		<h1>Please choose the Excel file with students information</h1>
	</div>
	 <form method="POST" action="uploadFile" enctype="multipart/form-data">
	<table class ="inputtable">
   
           <tr><td> File to upload: <input type="file" name="file">
            <input type="submit" value="Upload"> Upload the file!</td></tr>
       
        </table>
         </form>
</div>
<%--This form is the one that is telling to cancel input where to go if is pushed --%>
<form:form id="homeForm" method="get" action="home"></form:form>

