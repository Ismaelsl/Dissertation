<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form:form method="post" action="saveEditChecklist">
	<table>
		<tr>    
          <td>Date : </td>   
          <td><form:input type="date" path="date" value="" required='true' /></td>  
         </tr>  
         <tr>    
          <td>Event Name : </td>    
          <td><form:input path="eventName"  required='true' /></td>  
         </tr> 
          <tr>    
          <td>Place : </td>   
          <td><form:input path="place"  required='true' /></td>  
         </tr> 
         <tr>    
          <td>Description : </td>   
          <td><form:input path="description"  required='true' /></td>  
         </tr> 
		<%-- Hidden this since I need to pass the checkListID to the frontEnd and back to the backEnd --%>
		<form:hidden path="checkListID" />
		<tr>
			<td colspan="2"><input type="submit" class="btn btn-success"
				value="Save" /></td>
		</tr>
	</table>
</form:form>
<form:form method="get" action="${previousPage}">
	<button class="btn btn-success" value=" ">Go back</button>
</form:form>
