<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<h2 class="errormessage">${message}</h2>
<div class="form_css" id="box2">
	<%--I have two messages since one is for general message and the other is for a welcome message --%>
	<h2>${welcomeMessage}
		<span class="importantmessage">${username}</span>
	</h2>
	<p class="welcometext" style="font-size: 14px;">
		This web application has been created to help students and lecturers
		with their dissertations. If you find any errors or have any
		comments please send it to the dissertation coordinator.<br />
		<br /> You can find contact information in the contact us <br />section
		on the menu
	</p>
</div>