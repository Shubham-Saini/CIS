<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

    <html>
    <head>
        <meta charset="UTF-8" />
        <!-- <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">  -->
        <title>Login and Registration Form</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"> 
        <meta name="description" content="Login and Registration Form" />
        <meta name="keywords" content="html5, css3, form, switch, animation, :target, pseudo-class" />
        <meta name="author" content="Codrops" />
        <link rel="shortcut icon" href="../favicon.ico"> 
        <link rel="stylesheet" type="text/css" href="style/demo.css" />
        <link rel="stylesheet" type="text/css" href="style/style.css" />
		<link rel="stylesheet" type="text/css" href="style/animate-custom.css" />
		<script language="javascript">
		function validate()
		{
			if(regi.password_signup.value!=regi.password_confirm.value)
			{	
				document.getElementById("lbl_reg").innerHTML = "Password and confirm password do not match.";
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
                   
                    <a class="hiddenanchor" id="toregister"></a>
                    <a class="hiddenanchor" id="tologin"></a>
                    <div id="wrapper">
                        <div id="login" class="animate form">
                            <form  action="ValidHandler?caller=checkCredential"  method="post" autocomplete="on"> 
                                <h1>Log in</h1> 
                                <p> 
                                    <label for="email" class="uname" data-icon="u" > Your email </label>
                                    <input id="username" name="email" required="required" type="text" placeholder="Enter your email"/>
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
										document.getElementById("res").innerHTML = "Username or Password is not correct";
									</script>
									<%}
									else if(message!=null && message.equals(an))
	
									{%>
									<script language="javascript">
										document.getElementById("res").innerHTML = "Registration successful. Login to continue";
										</script>
									<%}
								 %>
                        <div id="register" class="animate form">
                            <form  name="regi" action="ValidHandler?caller=registerCred"  method="post" autocomplete="on" onSubmit="return validate()"> 
                                <h1> Sign up </h1> 
                                <p> 
                                    <label for="username" class="uname" data-icon="u">Your username</label>
                                    <input id="usernamesignup" name="username" required="required" type="text" placeholder="Enter your name" />
                                </p>
                                <p> 
                                    <label for="email" class="youmail" data-icon="e" > Your email</label>
                                    <input id="emailsignup" name="email" required="required" type="email" placeholder="Enter your Email"/> 
                                </p>
                                <p> 
                                    <label for="password_signup" class="youpasswd" data-icon="p">Your password </label>
                                    <input id="passwordsignup" name="password_signup" required="required" type="password" placeholder="Enter Password"/>
                                </p>
                                <p> 
                                    <label for="password_confirm" class="youpasswd" data-icon="p">Please confirm your password </label>
                                    <input id="passwordsignup_confirm" name="password_confirm" required="required" type="password" placeholder="Confirm Password"/>
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
                            <form  action="" autocomplete="on"> 
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