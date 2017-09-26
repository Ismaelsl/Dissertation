<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<form:form method="POST" action="logincheck">
   <table>
      <tr>
         <td>User Name</td>
         <td><form:input path="username" /></td>
      </tr>
      <tr>
         <td>Password</td>
         <td><form:password path="password" /></td>
      </tr>    
      <tr>
         <td colspan="2">
            <input type="submit" class="btn btn-success" value="Submit"/>
         </td>
      </tr>
   </table>  
</form:form>
</body>
</html>