<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>      
  
       <form:form method="post" action="save">    
        <table >    
         <tr>    
          <td>Name : </td>   
          <td><form:input path="title"  /></td>  
         </tr>    
         <tr>    
          <td>Salary :</td>    
          <td><form:input path="description" /></td>  
         </tr>   
         <tr>    
          <td colspan="2"><input type="submit" value="Save" /></td>    
         </tr>    
        </table>    
       </form:form>    