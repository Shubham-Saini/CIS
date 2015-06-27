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
            <li><a href="OAuth.jsp" class="thispage">Under the hood</a></li>
            <li><a href="about.jsp">About us</a></li>
            <li><a href="contact.jsp">Contact Us</a></li>
        </ul>
    </nav>
    </header>
    
    <article id="main">
        <h2>OAuth Authorization 2.0</h2>
        
        <figure class="floatright"><img src="images/abstract_flow.png" width="400" height="266"  alt=""/>
            <figcaption>Authorization code flow</figcaption>
        </figure>
        <p>OAuth 2 is an authorization framework that enables applications to obtain limited access to user accounts on an HTTP service, such as Facebook, GitHub, and DigitalOcean. It works by delegating user authentication to the service that hosts the user account, and authorizing third-party applications to access the user account. OAuth 2 provides authorization flows for web and desktop applications, and mobile devices.</p>
        <h2> Grant Type: Authorization Code</h2>
        <figure class="floatleft"><img src="images/auth_code_flow.png" width="400" height="266"  alt=""/>
            <figcaption>Grant Type</figcaption>
        </figure>
        <p>The authorization code grant type is the most commonly used because it is optimized for server-side applications, where source code is not publicly exposed, and Client Secret confidentiality can be maintained. This is a redirection-based flow, which means that the application must be capable of interacting with the user-agent (i.e. the user's web browser) and receiving API authorization codes that are routed through the user-agent.
</p>
<br>
        <h2> OAuth 2.0 Authorization</h2>
        <p>We use the OAuth authorization protocol when connecting to Google Drive, Dropbox, Box, Picasa, and OneDrive. OAuth is an industry-standard protocol which allows us to connect to these services without knowing, storing, or having access to your login credentials.</p>
        <figure class="floatright"><img src="images/oauth.png" width="400" height="266"  alt=""/>
            <figcaption>OAuth 2.0 Authorization</figcaption>
        </figure>
        <p>Other than encrypted passwords, we only store the information you use to register with CIS – your first name, last name, and email address.</p>
    </article>
    <aside id="sidebar">
        <h2>Security Guarantee</h2>
        <p>CIS not only provides powerful functions but also ensures the highest security for data management through three ways below: </p>
        <p>Website connect and data transfer using SSL encrypted channels.</p>
        <p>Access cloud drives with OAuth authorization and does not save your password.</p>
        <p>Does not save or cache your data and files on our servers permanently.</p>
        <h2>PRIVACY</h2>
        <p>CIS strives to keep your accounts and information as private as possible.</p>
        <p>We are very concerned with the security of our users. </p>
    </aside>
    <footer>
        <p>© Copyright 2015 Cloud Integration Services</p>
    <p>All trademarks, service marks, trade names, trade dress, product names and logos appearing on the site are the property of their respective owners, including in some instances CIS.
    <p> All Rights Reserved</p>
    </footer>
</div>
</body>
</html>
