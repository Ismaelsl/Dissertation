<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">

<div class="form_css">
	<h2 class="errormessage">${message}</h2>
	<div class="w3-container w3-blue">	
		<h1>Edit Project</h1>
	</div> 
<form:form method="post" action="saveEdit">
	<table>
		<tr>
			<td style="border:none;"><b>Academic year : </b></td>   
			<td style="border:none;"><form:input class="w3-input" path="year"  value="${year}" /></td>
		</tr>  
        <tr>    
          	<td style="border:none;"><b>Title : </b></td>   
          	<td style="border:none;"><form:input class="w3-input" path="title"  required='true'/></td>  
        </tr>  
        <tr>    
        	<td style="border:none;"><b>Technologies : </b></td>   
          	<td style="border:none;"><form:input class="w3-input" path="topics"  required='true'/></td>  
        </tr>  
        <tr>    
          	<td style="border:none;"><b>Compulsory Readings : </b></td>   
          	<td style="border:none;"><form:input class="w3-input" path="compulsoryReading"  required='true'/></td>  
        </tr>  
        <tr>    
          	<td style="border:none;"><b>Description : </b></td>   
          	<td style="border:none;"><form:input class="w3-input" path="description"  required='true' size="45"/></td>  
        </tr>    
        <tr>    
        	<form:hidden path="projectID" /><%--This projectID is telling me which project will be updated in the DB --%>
          	<td colspan="2" style="border:none;">
          		<input type="submit" class="btn btn-success btn-lg"  value="Save Edition" />
          		<input type="submit" name="cancel" value="Cancel Edition" class="btn btn-danger btn-lg" form="cancelForm" />
          	</td>    
        </tr>    
   </table>    
</form:form>   
</div> 
<%--This form is the one that is telling to cancel input where to go if is pushed --%>
<form:form id="cancelForm" method="get" action="${previousPage}"></form:form>
