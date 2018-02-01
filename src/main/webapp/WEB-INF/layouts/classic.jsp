<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<link href="bootstrap/css/mycss.css" rel="stylesheet" type="text/css" />
<html>
<head>
<title><tiles:getAsString name="title" /></title>
</head>

<body>
	<table class="table">
		<tr>
			<td width = "12%" class="menudiv" rowspan="4" align="center"><tiles:insertAttribute
					name="menu" /></td>
			<td colspan="2" class="headerdiv"><tiles:insertAttribute
					name="header" /></td>
		</tr>
		<tr>
			<td width="90%" class="bodydiv"><tiles:insertAttribute name="body" /></td>
		</tr>
		<tr>
			<td colspan="2" class="footer"><tiles:insertAttribute
					name="footer" /></td>
		</tr>
	</table>
</body>
</html>