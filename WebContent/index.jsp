<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta charset="utf-8">
<title>Cloud Integration Service: Home</title>
<link href="styles/main.css" rel="stylesheet" type="text/css">
<!--The following script tag downloads a font from the Adobe Edge Web Fonts server for use within the web page. We recommend that you do not modify it.--><script>var __adobewebfontsappname__="dreamweaver"</script><script src="http://use.edgefonts.net/source-sans-pro:n6:default.js" type="text/javascript"></script>
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
        <li><a href="index.jsp" class="thispage">Home</a></li>
        <li><a href="security.jsp">Security</a></li>
        <li><a href="OAuth.jsp">Under the hood</a></li>
        <li><a href="about.jsp">About Us</a></li>
        <li><a href="contact.jsp">Contact Us</a></li>
      </ul>
    </nav>
  </header>
  <div id="hero">
    <article>
      <h2>Be at it's center</h2>
      <p>Cloud storage has become an integral part of our modern, mobile lives. Services such as Google Drive, Dropbox and OneDrive,all vie to hold our vital data on their servers. Now you don't have to choose.</p>
    </article>
  <img src="images/cloud_computing_services.jpg"  alt=""/></div>
  <article id="main">
    <h2>About Cloud Integation Service</h2>
    <p>Do you have several cloud drives such as different account in <a href="http://www.dropbox.com">Dropbox</a>,<a href="https://www.onedrive.live.com/"> OneDrive</a>, and <a href="http://www.drive.google.com">Google Drive</a>? Do you need to transfer files from one to other cloud drives? If you want to manage these cloud drives, it is necessary to login each account, and then do some upload, download or synchronized operations. Have you ever thought of combining them together to manage just by logging once?

Oh yeah, <a href="#">CIS </a>is the exact product! It will help you transfer files across cloud drives and do the centralized management. Luckily, it is FREE, safe and reliable. Now, just join us at once! </p>
    <p>It has provided the prefect support for various clouds. At the same time, you can handle multiple accounts in one provided by the same service provider. Supposing that you have registered two accounts in Google Drive, it can manage all the files of both accounts simultaneously.</p>
    <figure class="centered"><img src="images/almacenamiento-en-linea-como-elegir_002.png" width="400" height="266"  alt=""/>
      <figcaption>CIS Services</figcaption>
    </figure>
    <p>Although the cable cars are now mainly a tourist attraction,  they&rsquo;re still used by local commuters to get to and from work. The California  Street line is particularly popular among commuters on weekdays.</p>
  </article>
  <aside id="sidebar">
    <h2>Whole Crux Of It</h2>
    <p>Generally speaking, each cloud drive will just free provide a small storage space. Thus, a big one will come into begins after adding all free space of this cloud drives together. For instance, if you have registered five Dropbox accounts and each one has offered 2GB free space, you can use the free 10GB space conveniently with the help of <a href="#">CIS</a>.</p>
    <p>Bring all of your online storage services together in one interface using CIS. Many people have multiple cloud storage accounts - Dropbox, SkyDrive, Google Drive - but managing them all separately can be a chore. Here's how to control different services from a single interface using <a href="#">CIS</a>.</p>
  </aside>
  <footer>
    <p>© Copyright 2015 Cloud Integration Services</p>
    <p>All trademarks, service marks, trade names, trade dress, product names and logos appearing on the site are the property of their respective owners, including in some instances CIS.
    <p> All Rights Reserved</p>
  </footer>
</div>

</body>
</html>
