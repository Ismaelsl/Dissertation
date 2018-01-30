<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<link href="bootstrap/css/mycss.css" rel="stylesheet" type="text/css" />
<html>
<body>
	<div id="parent">
		<form:form method="POST" action="logincheck" id="form_login">
			<b>User Name:</b>
			<form:input path="username" required='true' /><br><br>
			<b>Password:</b>
			<form:password path="password" required='true' /><br><br>
			<input type="submit" class="btn btn-success" value="Submit" />
		</form:form>
	</div>
</body>
</html>