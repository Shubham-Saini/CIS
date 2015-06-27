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
    <%
 	
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
            <li><a href="about.jsp"  class="thispage">About us</a></li>
            <li><a href="contact.jsp">Contact Us</a></li>
        </ul>
    </nav>
    </header>
    
    <article id="main">
        <h2>Cloud Integration Services Inc.</h2>
        
        
        <p><a href="index.jsp">CIS</a> is a central management platform presented by CIS Inc. It mainly serves for the users who have multiple cloud drives. They are allowed to manage all the files of their cloud drives through just one single account in CIS.</p>
        
       
        <p>The company was founded in 2015.We see a lot of news about cloud storage. Among this news, the words "Dropbox", "Google Drive"and "OneDrive" constantly attracted our attention. Thus, the new idea to "Put multiple cloud drives into one" came into being. </p>
    </article>
    <aside id="sidebar">
        <h2>Contact Information</h2>
        <p>Feel free to contact us at: </p>
        <p> CIS Inc. </p>
        <p> The Technological Institute of textile and sciences.</p>
        <p> Birla Colony, Bhiwani-127021, Haryana</p>
        
        <h2>Reach Us At</h2>
        <p> Contact at:
        <p>Praveen Dhawan: 9718310911</p>
        <p>Shubham Saini: 9034626298</p>
        <p> <a href="mailto:cis.helpcenter@gmail.com" ?Subject=Hello%20Team%20CIS" target="_top">cis.helpcenter@gmail.com</a></p>
        <p>We would love get in touch with you. </p>
    </aside>
    <footer>
        <p>© Copyright 2015 Cloud Integration Services</p>
    <p>All trademarks, service marks, trade names, trade dress, product names and logos appearing on the site are the property of their respective owners, including in some instances CIS.
    <p> All Rights Reserved</p>
    </footer>
</div>
</body>
</html>
