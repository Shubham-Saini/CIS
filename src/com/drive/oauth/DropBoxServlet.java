package com.drive.oauth;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/DBHandler")
public class DropBoxServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String code = request.getParameter("code");
		String caller = request.getParameter("caller");
		if(caller!=null && caller.equalsIgnoreCase("drop"))
		{
			String str = "https://www.dropbox.com/1/oauth2/authorize?response_type=code&client_id=s5x67zsjs7b8i15&redirect_uri=http://localhost:8080/DriveProject/DBHandler";
			response.sendRedirect(str);
		}
		if(code!=null)
		{
			System.out.println("Cod of auth is " +  code);
			//String accessToken = DropUtil.getAccessTokenDrop(code);
			//System.out.println("Here is the access token "+accessToken);
			//RequestDispatcher disp = request.getRequestDispatcher("Drop.jsp");
			//disp.forward(request, response);
			//DropUtil.listFiles(accessToken);
		}
	}

}
