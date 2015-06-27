<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta charset="utf-8">
<title>Cloud Integration Services: Security</title>

<link href="styles/main.css" rel="stylesheet" type="text/css">
<script src="dist/sweetalert.min.js"></script>
<link rel="stylesheet" type="text/css" href="dist/sweetalert.css">
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
 	if(request.getAttribute("uname")!=null)
 	{
 		String uname = (String)request.getAttribute("uname");
 		%>
 		<script>
 		var nn = "<%=uname%>";
 		swal("Account has been added successfully", nn, "success");
 		//alert(nn+"Account has been added successfully");
 		</script>
 		
 <%	}
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
  	response.sendRedirect("LReg.jsp");
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
    <article id="main">
        <h2>Google Drive</h2>
        
        <figure class="floatright"><a href="AddAccount.jsp?caller=google"><img src="images/GoogleDrive.png" width="400" height="266"  alt=""/></a>
            <figcaption>Google Drive</figcaption>
        </figure>
        <p>Google Drive is a file storage and synchronization service created by Google. It allows users to store files in the cloud, share files, and edit documents, spreadsheets, and presentations with collaborators. Google Drive encompasses Google Docs, Sheets, and Slides, an office suite that permits collaborative editing of documents, spreadsheets, presentations, drawings, forms, and more.</p>
        <h2> Dropbox</h2>
        <figure class="floatleft"><a href="AddAccount.jsp?caller=dropbox"><img src="images/dropbox.png" width="400" height="266"  alt=""/></a>
            <figcaption>Dropbox</figcaption>
        </figure>
        <p>Dropbox is a file hosting service operated by Dropbox, Inc., headquartered in San Francisco, California, that offers cloud storage, file synchronization, personal cloud, and client software. Dropbox allows users to create a special folder on their computers, which Dropbox then synchronizes so that it appears to be the same folder (with the same contents) regardless of which computer is used to view it.
</p>
<br>
        <h2> Microsoft OneDrive</h2>
        
        <figure class="floatright"><a href="AddAccount.jsp?caller=onedrive"><img src="images/drive.png" width="400" height="266"  alt=""/></a>
            <figcaption>Microsoft OneDrive</figcaption>
        </figure>
        <p>OneDrive (previously SkyDrive, Windows Live SkyDrive and Windows Live Folders) is a file hosting service that allows users to upload and sync files to a cloud storage and then access them from a web browser or their local device. It is part of the suite of online services formerly known as Windows Live and allows users to keep the files private, share them with contacts, or make the files public.</p>
    </article>
    
    <footer>
        <p>© Copyright 2015 Cloud Integration Services</p>
    <p>All trademarks, service marks, trade names, trade dress, product names and logos appearing on the site are the property of their respective owners, including in some instances CIS.
    <p> All Rights Reserved</p>
    </footer>
</div>
</body>
</html>
