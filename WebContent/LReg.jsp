<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login and Registration Form with HTML5 and CSS3</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"> 
        <meta name="description" content="Login and Registration Form with HTML5 and CSS3" />
        <meta name="keywords" content="html5, css3, form, switch, animation, :target, pseudo-class" />
        <meta name="author" content="Codrops" />
        <link rel="shortcut icon" href="../favicon.ico"> 
        <link rel="stylesheet" type="text/css" href="style/demo.css" />
        <link rel="stylesheet" type="text/css" href="style/style.css" />
		<link rel="stylesheet" type="text/css" href="style/animate-custom.css" />
		<script language="javascript">
		function validate()
		{
			if(regi.passwordsignup.value!=regi.passwordsignup_confirm.value)
			{	
				document.getElementById("lbl_reg").innerHTML = "<font color='red'>Password and confirm password do not match.</font>";
				return false;
			}
			else
			{
				return true;
			}
		}
		</script>
    <!--The following script tag downloads a font from the Adobe Edge Web Fonts server for use within the web page. We recommend that you do not modify it.--><script>var __adobewebfontsappname__="dreamweaver"</script><script src="http://use.edgefonts.net/source-sans-pro:n2,n3:default.js" type="text/javascript"></script>
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
            <div id="text"><center><h1 style="font-size: 72px; margin-top: 0px; margin-bottom: 0px; text-align: justify; text-transform: uppercase; color: #3399CC; font-family: source-sans-pro; /* [disabled]font-style: normal; */ font-weight: 300; border-top-right-radius: 0px;"> Cloud Integration Services</h1></center> </div>	
                <div id="container_demo" >
                    <!-- hidden anchor to stop jump http://www.css3create.com/Astuce-Empecher-le-scroll-avec-l-utilisation-de-target#wrap4  -->
                    <a class="hiddenanchor" id="toregister"></a>
                    <a class="hiddenanchor" id="tologin"></a>
                    <a class="hiddenanchor" id="toforgot"></a>
                    <div id="wrapper">
                        <div id="login" class="animate form">
                            <form  action="ValidHandler?caller=checkCredential" method="post" autocomplete="on"> 
                                <h1>Log in</h1> 
                                <p> 
                                    <label for="email" class="uname" data-icon="u" > Your email or username </label>
                                    <input id="username" name="email" required="required" type="text" placeholder="Enter your email" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$" title="Enter a valid email"/>
                                </p>
                                <p> 
                                    <label for="password" class="youpasswd" data-icon="p"> Your password </label>
                                    <input id="password" name="password" required="required" type="password" placeholder="Enter your password" /> 
                                </p>
                                <p class="login button"> 
                                 <label for="frm" id="res"></label>
                                    <input type="submit" value="Login" /> 
								</p>
                                <p class="change_link">
									Not a member yet ?
									<a href="#toregister" class="to_register">Join us</a>
									<a href="#toforgot" class="to_register">Forgot Password</a>
								</p>
                            </form>
                        </div>
						
						<%
									String res = "Your username/password combination is invalid. Try again!";
									String an ="You have successfully registered with us";
                                		String message = (String)request.getAttribute("message");
									if(message!=null && message.equals(res))
									{%>
									<script language="javascript">
										document.getElementById("res").innerHTML = "<font color='red'>Username or Password is not correct</font>";
									</script>
									<%}
									else if(message!=null && message.equals(an))
	
									{%>
									<script language="javascript">
										document.getElementById("res").innerHTML = "<font color='green'>Registration successful. Login to continue</font>";
										</script>
									<%}
									else if(message!=null && message.equals("This email is already registered with us."))
									{%>
									<script language="javascript">
									
									document.getElementById("res").innerHTML = "<font color='red'>This email is already registered with us.</font>";
									</script>
								<%}
									else if(message!=null && message.equals("We are having trouble registering you with us.Try again!"))
									{
										%>
										<script language="javascript">
										
										document.getElementById("res").innerHTML = "<font color='red'>We have trouble registering you with us.Try again!</font>";
										</script>
									<%
										
									}
									else if(message!=null && message.equals("Password Change successful"))
									{
										%>
										<script language="javascript">
										
										document.getElementById("res").innerHTML = "<font color='green'>Password Change Successful. Login to continue</font>";
										</script>
									<%
										
									}
									else if(message!=null && message.equals("Follow Instructions"))
									{
										%>
										<script language="javascript">
										
										document.getElementById("res").innerHTML = "<font color='green'>Recovery Email sent.</font>";
										</script>
									<%
										
									}
								 %>
                        <div id="register" class="animate form">
                            <form  name="regi" action="ValidHandler?caller=registerCred"  method="post" autocomplete="on" onSubmit="return validate()"> 
                                <h1> Sign up </h1> 
                                <p> 
                                    <label for="usernamesignup" class="uname" data-icon="u">Your username</label>
                                    <input id="usernamesignup" name="usernamesignup" required="required" type="text" placeholder="Enter your name" pattern="^[a-zA-Z0-9.- ]$" title="Enter a valid name"/>
                                </p>
                                <p> 
                                    <label for="emailsignup" class="youmail" data-icon="e" > Your email</label>
                                    <input id="emailsignup" name="emailsignup" required="required" type="email" placeholder="Enter your Email" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$" title="Enter a valid email"/> 
                                </p>
                                <p> 
                                    <label for="passwordsignup" class="youpasswd" data-icon="p">Your password </label>
                                    <input id="passwordsignup" name="passwordsignup" required="required" type="password" placeholder="Enter Password" pattern=".{6,}" title="Six or more characters"/>
                                </p>
                                <p> 
                                    <label for="passwordsignup_confirm" class="youpasswd" data-icon="p">Please confirm your password </label>
                                    <input id="passwordsignup_confirm" name="passwordsignup_confirm" required="required" type="password" placeholder="Confirm Password" pattern=".{6,}" title="Six or more characters"/>
                                </p>
                                <p class="signin button"> 
                                <label for="register" id="lbl_reg"></label>
									<input type="submit" value="Sign up"/> 
								</p>
                                <p class="change_link">  
									Already a member ?
									<a href="#tologin" class="to_register"> Go and log in </a>
								</p>
                            </form>
                        </div>
						<div id="forgot" class="animate form">
                            <form  action="ValidHandler?caller=recover" method="post" autocomplete="on"> 
                                <h1>Recovery</h1> 
                                <p> 
                                    <label for="email_rec" class="uname" data-icon="u" > Your email or username </label>
                                    <input id="username_rec" name="email_rec" required="required" type="text" placeholder="Enter your email"/>
                                </p>
                                <p class="login button"> 
                                    <input type="submit" value="Submit" /> 
								</p>
                                <p class="change_link">
									Not a member yet ?
									<a href="#toregister" class="to_register">Join us</a>
								</p>
                            </form>
                        </div>
                    </div>
                </div>  
            </section>
        </div>
</body>
</html>