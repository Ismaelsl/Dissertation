<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>      
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>    
       <form:form method="post" action="save">    
        <table >    
         <tr>    
          <td>Academic year : </td>   
          <td><form:input path="year"  value="${year}" readonly="true"/></td>  
         </tr>  
          <tr>    
          <td>Title : </td>   
          <td><form:input path="title"  required='true'/></td>  
         </tr>  
          <tr>    
          <td>Topic : </td>   
          <td><form:input path="topics"  required='true'/></td>  
         </tr>  
          <tr>    
          <td>Compulsory Readings : </td>   
          <td><form:input path="compulsoryReading"  required='true'/></td>  
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