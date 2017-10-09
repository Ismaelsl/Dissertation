<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form:form method="post" action="saveEditChecklist">
	<table>
		<tr>    
          <td>Date : </td>   
          <td><form:input type="date" path="date"  value='<fmt:formatDate value="" pattern="dd-MM-yyyy" />'/></td>  
         </tr>  
         <tr>    
          <td>Event Name : </td>    
          <td><form:input path="eventName"  /></td>  
         </tr> 
          <tr>    
          <td>Place : </td>   
          <td><form:input path="place"  /></td>  
         </tr> 
         <tr>    
          <td>Description : </td>   
          <td><form:input path="description"  /></td>  
         </tr> 
		<%-- Hidden this since I need to pass the checkListID to the frontEnd and back to the backEnd --%>
		<form:hidden path="checkListID" />
		<tr>
			<td colspan="2"><input type="submit" class="btn btn-success"
				value="Save" /></td>
		</tr>
	</table>
</form:form>
<table>
	<tr>
		<td colspan="2"><form:form method="get" action="home">
				<button id="modal-edit-id" name="projectID" class="btn btn-danger"
					value=" ">Cancel</button>
			</form:form></td>
	</tr>
</table>
