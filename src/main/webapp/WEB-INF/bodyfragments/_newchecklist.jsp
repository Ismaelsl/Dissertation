<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>      
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>    
       <form:form method="post" action="savechecklist">    
        <table >      
          <tr>    
          <td>Date : </td>   
          <td><form:input type="date" path="date" required='true' value='<fmt:formatDate value="" pattern="dd-MM-yyyy" />'/></td>  
         </tr>  
         <tr>    
          <td>Event Name : </td>   
          <td><form:input path="eventName"  required='true'/></td>  
         </tr> 
          <tr>    
          <td>Place : </td>   
          <td><form:input path="place"  required='true'/></td>  
         </tr>
         <tr>    
          <td>Description : </td>   
          <td><form:input path="description"  required='true'/></td>  
         </tr>    
         <tr>    
          <td colspan="2"><input type="submit" class="btn btn-success"  value="Save" /></td>    
         </tr>    
        </table>    
       </form:form>    