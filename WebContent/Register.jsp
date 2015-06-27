<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<h1>
		Register for Drive Services
	</h1>
	<br>
	<%
		String message = (String)request.getAttribute("message");
		if(message!=null)
		{
			out.println(message+"<br>");
		}
	%>
		<form action="ValidHandler?caller=registerCred" method="post">
		Username <input type="text" name="username"/><br>
		Email <input type="text" name="email" /><br>
		Password <input type="password" name="password"/><br>
		<input type="submit" name="Submit" value="Submit"/>
		</form>
</body>
</html>