   <%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>   
     <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>    
<c:forEach items="${projectList}" var="project">
Project title: ${project.title}
<br>
ID: ${project.projectID}
<br>
Description: ${project.description}
<br>
</c:forEach>