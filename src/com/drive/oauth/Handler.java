package com.drive.oauth;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Hand")
public class Handler extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String caller = request.getParameter("caller");
		String code = request.getParameter("code");
		String client_id="000000004C14FF13";
		String client_secret = "hIHhWXYiBsbBTHW0r0k7iCZPeU8YycZa";
		String scope = "wl.offline_access%20wl.skydrive_update";
		String redirect = "http://localhostred.com:8080/DriveProject/Hand";
		System.out.println("Request url is "+ request.getRequestURL().toString());
		if(caller!=null && caller.equals("one"))
		{
			String param = "?client_id="+client_id+"&scope="+scope+"&response_type=code&redirect_uri="+redirect;
			String url = "https://login.live.com/oauth20_authorize.srf"+param;
			System.out.println(url);
			response.sendRedirect(url);
		}
		else if(code!=null)
		{
			System.out.println("Authorization code is " + code);
			String url = "http://localhost:8080/DriveProject/AuthHandler?code="+code;
			response.sendRedirect(url);
			return;
			
		}
	}

}
