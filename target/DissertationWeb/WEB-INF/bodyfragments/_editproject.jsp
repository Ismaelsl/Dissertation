<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>      
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>    
       <form:form method="post" action="saveEdit">    
        <table >    
         <tr>    
          <td>Year : </td>   
          <td><form:input path="year"  /></td>  
         </tr>  
          <tr>    
          <td>Title : </td>   
          <td><form:input path="title"  /></td>  
         </tr>  
          <tr>    
          <td>Topic : </td>   
          <td><form:input path="topics"  /></td>  
         </tr>  
          <tr>    
          <td>Compulsory Readings : </td>   
          <td><form:input path="compulsoryReading"  /></td>  
         </tr>  
          <tr>    
          <td>Description : </td>   
          <td><form:input path="description"  /></td>  
         </tr>
         <%-- Hidden this since I need to pass the projectID to the frontEnd and back to the backEnd --%>
         <form:hidden path="projectID" />    
         <tr>    
          <td colspan="2"><input type="submit" class="btn btn-success"  value="Save" /></td>    
         </tr>    
        </table>    
       </form:form>    