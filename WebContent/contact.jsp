<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta charset="utf-8">
<title>Cloud Integration Services: Security</title>
<link href="styles/main.css" rel="stylesheet" type="text/css">
<!--The following script tag downloads a font from the Adobe Edge Web Fonts server for use within the web page. We recommend that you do not modify it.-->
<script>var __adobewebfontsappname__="dreamweaver"</script>
<script src="http://use.edgefonts.net/source-sans-pro:n6:default.js" type="text/javascript"></script>
</head>

<body>
<div id="wrapper">
    <header id="top">
        <div id="space">
    <h1>Cloud Integration Services</h1>
    </div>
    
    <div id="button">
    <a href="http://localhost:8080/DriveProject/LReg.jsp" class="myButton" id="loginbtn">Login</a>
	<a href="http://localhost:8080/DriveProject/LReg.jsp#toregister" class="myButton" id="regbtn">Register</a>
	<div id="cssmenu">
	<ul>
		<li class="active has-sub"><a href='#'><span id="usrname"></span></a>
		<ul>
         <li class='has-sub'><a href="Welcome.jsp" id="usraccount"><span></span></a>
          <li class='has-sub'><a href="file.jsp" id="file"><span></span></a>
          <li class='has-sub'><a href="ValidHandler?caller=logout" id="usrlogout"><span></span></a>
          
            </li></li>
            </ul></li>
    </ul>
	</div>
    </div>
    <script language="javascript">
	function change(userName)
	{
		
		document.getElementById("loginbtn").style.visibility="hidden";
		document.getElementById("regbtn").style.visibility="hidden";
		var e = document.getElementById("usrname");
		e.innerHTML = "Welcome! " + userName;
		document.getElementById("usraccount").innerHTML="Account";
		document.getElementById("usrlogout").innerHTML="Logout";
		document.getElementById("file").innerHTML="File Manager";
		document.getElementById("cssmenu").style.display="block";
	}
</script>
    <%
 	if(request.getAttribute("message")!=null)
 	{
 		String message = (String)request.getAttribute("message");
 		%>
 		<script language="javascript">
 		var ms = "<%=message%>";
 		alert(ms);
 		</script>
 	<%}
  	String email=null;
  	if(session.getAttribute("email")!=null)
  	{
  		email = (String)session.getAttribute("email");
  		%>
  		<script language="javascript">
  		   var x = "<%=email%>";
  		   change(x);
  		</script>
  	<%}
  	else
  	{%>
  		<script language"javascript">
  			document.getElementById("cssmenu").style.visibility="hidden";
  		</script>
  	<%}
  %>
<nav id="mainnav">
        <ul>
            <li><a href="index.jsp">Home</a></li>
            <li><a href="security.jsp">Security</a></li>
            <li><a href="OAuth.jsp">Under the hood</a></li>
            <li><a href="about.jsp">About Us</a></li>
            <li><a href="#"  class="thispage">Contact Us</a></li>
        </ul>
    </nav>
    </header>
    <div id="wrap">
	<style scoped>
p {text-shadow:0 1px 0 #fff; font-size:21px;}
h1 {margin-bottom:-150px; text-align:center;font-size:48px;font-color:#f7f2ec text-shadow:0 1px 0 #ede8d9; }
	</style>
		<h1>Drop us a Message!</h1>
		<div id='form_wrap'>
			<form id="contact-form" action="ValidHandler?caller=feedback" method="post">

		<p id="formstatus"></p>
				<p>Hello Team CIS,</p>
				<label for="email">Your Message : </label>
				<textarea  name="message" value="Your Message" id="message" ></textarea>
				<p>Best,</p>	
				<label for="name">Name: </label>
				<input type="text" name="name" value="" id="name" />
				<label for="email">Email: </label>
				<input type="text" name="email" value="" id="email" />
				<input type="submit" name ="submit" value="OK send it now, thanks! :)" />
			</form>
		</div>
	</div>
    <footer>
        <p>© Copyright 2015 Cloud Integration Services</p>
    <p>All trademarks, service marks, trade names, trade dress, product names and logos appearing on the site are the property of their respective owners, including in some instances CIS.
    <p> All Rights Reserved</p>
    </footer>
</div>
</body>
</html>
