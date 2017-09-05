<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>   
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>    
<%-- the item within the {} must be the same name that the variable pass to the view from the controller --%>
<c:forEach items="${projectList}" var="project">
Project title: ${project.title}
<br>
ID: ${project.projectID}
<br>
Description: ${project.description}
<br>
</c:forEach>