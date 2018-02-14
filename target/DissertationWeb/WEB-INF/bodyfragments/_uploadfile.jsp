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
		<h1>Please choose the Excel file with students information</h1>
	</div>
    <form method="POST" action="uploadFile" enctype="multipart/form-data">
            File to upload: <input type="file" name="file"> <input
                type="submit" value="Upload"> Press here to upload the
            file!
        </form>
</div>
<%--This form is the one that is telling to cancel input where to go if is pushed --%>
<form:form id="homeForm" method="get" action="home"></form:form>

