package com.drive.oauth;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dropbox.core.*;

@WebServlet("/ValidateServlet")
public class ValidateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String un_email = null;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String caller = request.getParameter("caller");
		if(caller == null) return;
		System.out.println("here");
		RequestDispatcher disp ;
		
		HttpSession session = request.getSession();
		if(caller.equalsIgnoreCase("login"))
		{
			//disp= request.getRequestDispatcher("/Login.jsp");
			//disp.forward(request, response);
			response.sendRedirect("/LReg.jsp");
		}
		else if(caller.equalsIgnoreCase("register"))
		{
			disp = request.getRequestDispatcher("/LReg.jsp#toregister");
			disp.forward(request, response);
		}
		else if(caller.equalsIgnoreCase("checkCredential"))
		{
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			String user = null;
			boolean res = DatabaseConnect.authenticate(email, password, user);
			if(res==true)
			{
				session.setAttribute("email", email);
				request.setAttribute("user", user);
				disp = request.getRequestDispatcher("/Welcome.jsp");
				disp.forward(request, response);
			}
			else 
			{
				request.setAttribute("message", "Your username/password combination is invalid. Try again!");
				disp = request.getRequestDispatcher("/LReg.jsp");
				disp.forward(request, response);
			}
		}
		else if(caller.equalsIgnoreCase("registerCred"))
		{
			String username = request.getParameter("usernamesignup");
			String email = request.getParameter("emailsignup");
			String password = request.getParameter("passwordsignup");
			int res = DatabaseConnect.register(username, email, password);
			if(res==1)
			{
				request.setAttribute("message", "You have successfully registered with us");
				disp = request.getRequestDispatcher("/LReg.jsp");
				disp.forward(request,response);
				return;
			}
			else if(res==0)
				request.setAttribute("message", "This email is already registered with us.");
			else if(res==2)
				request.setAttribute("message", "We are having trouble registering you with us.Try again!");
			System.out.println("IN validate servet");
			disp = request.getRequestDispatcher("LReg.jsp");
			disp.forward(request, response);
		}
		else if(caller.equalsIgnoreCase("logout"))
		{
			session.invalidate();
			disp = request.getRequestDispatcher("/index.jsp");
			disp.forward(request, response);
		}
		else if(caller.equalsIgnoreCase("recover"))
		{
			String email = request.getParameter("email_rec");
			String res = DatabaseConnect.generateCode(email);
			String[] params = res.split(":");
			int code = Integer.parseInt(params[0]);
			if(code==1)
			{
				String authCode = params[2];
				try
				{
					EmailService.sendEmail("recover", authCode, email);
				}catch(Exception e)
				{
					System.out.println("trouble sending email");
				}
			}
			request.setAttribute("message",res);
			request.setAttribute("message", "Follow Instructions");
			disp = request.getRequestDispatcher("/LReg.jsp");
			disp.forward(request,response);
		}
		else if(caller.equalsIgnoreCase("account_verification"))
		{
			String token = request.getParameter("token");
			String res = DatabaseConnect.removeCode(token);
			String[] params = res.split(":");
			int code = Integer.parseInt(params[0]);
			if(code==1)
			{
				String email = params[2];
				System.out.println("email in acc verification " + email);
				un_email = email;
				System.out.println("Now enter and re enter the password");
				request.setAttribute("code",1);
				request.setAttribute("email", email);
				disp = request.getRequestDispatcher("/Res.jsp");
				disp.forward(request, response);
			}
			else
			{
				System.out.println("Error in removing the code");
				request.setAttribute("code",0);
				disp = request.getRequestDispatcher("/Res.jsp");
				disp.forward(request, response);
			}
		}
		else if(caller.equals("feedback"))
		{
			System.out.println("Sending feedback");
			String message = request.getParameter("message");
			String from = request.getParameter("email");
			String subject = "Feedback from: "+from;
			boolean result = EmailService.sendEmail(subject, message, null);
			if(result == true)
			{
				request.setAttribute("message", "Thanks for the feedback. We Will get back with you shortly");
				disp = request.getRequestDispatcher("/contact.jsp");
				disp.forward(request, response);
				response.sendRedirect("contact.jsp");
			}
		}
		else if(caller.equalsIgnoreCase("resetPass"))
		{
			String password = request.getParameter("password");
			String email = request.getParameter("email");
			System.out.println("EMail  in resetPass us " +  un_email);
			boolean res  = DatabaseConnect.updatePassword(un_email, password);
			System.out.println("Password change result " + res);
			request.setAttribute("message", "Password Change successful");
			disp = request.getRequestDispatcher("/LReg.jsp");
			disp.forward(request,response);
		}
	}

}
