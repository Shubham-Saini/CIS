<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0"> 
        <meta name="description" content="Login and Registration Form with HTML5 and CSS3" />
        <meta name="keywords" content="html5, css3, form, switch, animation, :target, pseudo-class" />
        <meta name="author" content="Codrops" />
        <link rel="shortcut icon" href="../favicon.ico"> 
        <link rel="stylesheet" type="text/css" href="style/demo.css" />
        <link rel="stylesheet" type="text/css" href="style/style.css" />
		<link rel="stylesheet" type="text/css" href="style/animate-custom.css" />
</head>
<body>
<div class="container">
        <!-- Codrops top bar -->
        <div class="codrops-top">
                
                <div class="clr"></div>
            </div><!--/ Codrops top bar -->
            <header>
                <h1>&nbsp;</h1>
				<nav class="codrops-demos"></nav>
            </header>
            <section>
            <%
        	if(request.getAttribute("code")!=null)
        	{
        		int res = (Integer)request.getAttribute("code");
            	if(res==0)
            	{%>
	            <script language="javascript">
	            	document.getElementById("wrapper").style.visibility="hidden";
	            	document.getElementById("tt").innerHTML="Bad or Expired link."
	            </script>
	            	<%}
            	else if(res==1)
            	{%>
	            <script language="javascript">
	            	document.getElementById("message").style.visibility="hidden";
	            </script>
	            	<%}
            }
        	else
        	{
        		%>
	            <script language="javascript">
	            	document.getElementById("wrapper").style.visibility="hidden";
	            	//document.getElementById("message_res").style.visibility="visible";
	            </script>
	            	<%
        	}
          %>			
            <div id="text"><center><h1 style="font-size: 72px; margin-top: 0px; margin-bottom: 0px; text-align: justify; text-transform: uppercase; color: #3399CC; font-family: source-sans-pro; /* [disabled]font-style: normal; */ font-weight: 300; border-top-right-radius: 0px;"> Cloud Integration Services</h1></center> </div>	
                <div id="container_demo" >
                <!-- <div id="message_res"> <span id="tt" style="font size:30px; color:red;">You can't be here.</span></div> -->
                    <div id="wrapper">
                    
                        <div id="login" class="animate form">
                            <form  action="ValidHandler?caller=resetPass" method="post" autocomplete="on"> 
                                <h1>Recovery Wizard</h1> 
                                <p> 
                                    <label for="password" class="youpasswd" data-icon="p" > Enter New Password </label>
                                    <input id="password" name="password" required="required" type="password" placeholder="Enter new password"/>
                                </p>
                                <p> 
                                    <label for="password_confirm" class="youpasswd" data-icon="p"> Confirm New Password </label>
                                    <input id="password_confirm" name="password_confirm" required="required" type="password" placeholder="Confirm new password" /> 
                                </p>
                                <p class="login button"> 
                                 <label for="frm" id="res"></label>
                                    <input type="submit" value="Submit" /> 
								</p>
                            </form>
                        </div>
                      </div>
                     </div>
                 </section>
       </div>
        <%
        	if(request.getAttribute("code")!=null)
        	{
        		int res = (Integer)request.getAttribute("code");
            	if(res==0)
            	{%>
	            <script language="javascript">
	            	document.getElementById("wrapper").style.visibility="hidden";
	            	document.getElementById("tt").innerHTML="Bad or Expired link."
	            </script>
	            	<%}
            	else if(res==1)
            	{%>
	            <script language="javascript">
	            	document.getElementById("message").style.visibility="hidden";
	            </script>
	            	<%}
            }
        	else
        	{
        		%>
	            <script language="javascript">
	            	document.getElementById("wrapper").style.visibility="hidden";
	            	document.getElementById("message_res").style.visibility="visible";
	            </script>
	            	<%
        	}
          %>
</body>
</html>