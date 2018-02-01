<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>      
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">

<div class="form_css">
	<div class="w3-container w3-blue">	
		<h1>Project proposal form</h1>
	</div>   
       <form:form method="post" action="sendProposal">    
        <table >    
         <tr>    
          <td style="border:none;">Academic year : </td>   
          <td style="border:none;"><form:input class="w3-input" path="year"  value="${year}" readonly="true"/></td>  
         </tr>  
          <tr>    
          <td style="border:none;">Title : </td>   
          <td style="border:none;"><form:input class="w3-input" path="title"  required='true'/></td>  
         </tr>  
          <tr>    
          <td style="border:none;">Topic : </td>   
          <td style="border:none;"><form:input class="w3-input" path="topics"  required='true'/></td>  
         </tr>  
          <tr>    
          <td style="border:none;">Compulsory Readings : </td>   
          <td style="border:none;"><form:input class="w3-input" path="compulsoryReading"  required='true'/></td>  
         </tr>  
          <tr>    
          <td style="border:none;">Description : </td>   
          <td style="border:none;"><form:input class="w3-input" path="description"  required='true' size="50"/></td>  
         </tr>    
         <tr>    
          <td colspan="2" style="border:none;"><input type="submit" class="btn btn-success btn-lg"  value="Send Proposal" />
          <input type="submit" name="cancel" value="Cancel Proposal" class="btn btn-danger btn-lg" form="cancelForm" />
          </td>    
         </tr>    
        </table>    
       </form:form>   
       </div> 
<%--This form is the one that is telling to cancel input where to go if is pushed --%>
<form:form id="cancelForm" method="get" action="${previousPage}"></form:form>