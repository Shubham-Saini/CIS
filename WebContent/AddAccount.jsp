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
<script language="javascript">
	function change(userName)
	{
		var e = document.getElementById("usrname");
		e.innerHTML = "Welcome! " + userName;
		document.getElementById("usraccount").innerHTML="Account";
		document.getElementById("usrlogout").innerHTML="Logout";
		document.getElementById("file").innerHTML="File Manager";
		document.getElementById("file").innerHTML="File Manager";
		document.getElementById("cssmenu").style.display="block";
	}
	function func()
	{
		var cal ="<%=request.getParameter("caller")%>";
		var uname = document.getElementById("uname").value;
		document.location.href = "AuthHandler?caller=token&source="+cal+"&uname="+uname;
	}
</script>
</head>

<body>
<div id="wrapper">
    <header id="top">
        <div id="space">
    <h1>Cloud Integration Services</h1>
    </div>
    
    <div id="button">
    
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
  	{
  	//response.sendRedirect("LReg.jsp");
  	%>
  		<script language"javascript">
  			document.getElementById("cssmenu").style.visibility="hidden";
  		</script>
  	<%}
  %>
<nav id="mainnav">
        <ul>
            <li><a href="index.jsp">Home</a></li>
            <li><a href="security.jsp" >Security</a></li>
            <li><a href="OAuth.jsp">Under the hood</a></li>
            <li><a href="about.jsp">About Us</a></li>
            <li><a href="contact.jsp">Contact Us</a></li>
        </ul>
    </nav>
    </header>
    <h1> Add Account </h1>
    <br>
    <br><br><br>
    <br><br>
    <h2> Enter account name</h2>
    <div >
    	<style scoped>
    	input[type="text"], input[type="password"] 
{
    background: none repeat scroll 0 0 #FCFCFC;
    border: 1px solid #A6A6A6;
    border-radius: 2px 2px 2px 2px;
    color: #6C6C6C;
    height: 35px;
    margin-top: 10px;
    outline: medium none;
    padding-left: 10px;
    width: 230px;
}
input[type="text"]:focus, input[type="password"]:focus {
    background: none repeat scroll 0 0 white;
    border-color: #7DC9E2;
    box-shadow: 0 0 2px rgba(0, 0, 0, 0.3) inset;
    color: #A6A6A6;
 content:"";
}
.group{margin-top:10px;}
    	</style>
    	<%
    		String caller=request.getParameter("caller");
    	%>
    	<input type="text" id="uname"/>
    	<input type="button" id="btton" value="Proceed" onClick="func()"/>
    	
    </div>
    
    <footer>
        <p>© Copyright 2015 Cloud Integration Services</p>
    <p>All trademarks, service marks, trade names, trade dress, product names and logos appearing on the site are the property of their respective owners, including in some instances CIS.
    <p> All Rights Reserved</p>
    </footer>
</div>
</body>
</html>
