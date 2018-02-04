<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<link href="bootstrap/css/mycss.css" rel="stylesheet" type="text/css" />
<html>
<head>
<title><tiles:getAsString name="title" /></title>
</head>

<body>
	<div class="table">
			<div class="menudiv" align="center">
			<a href="${pageContext.request.contextPath}/home">
				<img src="bootstrap/images/logomenu.png" align="middle" alt="logomenuicon" class="logomenustyle">
			</a><tiles:insertAttribute
					name="menu" /></div>
			<div class="headerdiv"><tiles:insertAttribute
					name="header" /></div>
			<div class="bodydiv"><tiles:insertAttribute 
					name="body" /></div>
			<div class="footer"><tiles:insertAttribute
					name="footer" /></div>
	</div>
</body>
</html>