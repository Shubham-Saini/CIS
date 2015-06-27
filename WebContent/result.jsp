<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="java.util.*,com.google.api.services.drive.model.File" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link href="C:\Users\Shubham\workspace\DriveProject\WebContent\design.css" rel="stylesheet" type="text/css">
</head>
<body>
	<h1>
		List of user's Google Drive Files:
	</h1>
	<br>
	 
	 <%
	 response.setContentType("text/xml");
	 out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	 out.println("<rows>");
		out.println("<row id='"+1+"'>");
			out.println("<cell>../icons/grid_folder.png</cell>");
			out.println("<cell>Books</cell>");
			out.println("<cell>File folder</cell>");
			out.println("<cell>2013-10-07 18:59</cell>");
			out.println("<cell> </cell>");
		out.println("</row>");
		out.println("</rows>");
	 %>
<%
//		List<File> list = (List<File>)request.getAttribute("list");
	//	int size=list.size();
		//for(int i=0;i<size;i++)
//		{
	//		String name = list.get(i).getOriginalFilename();
		//	if(name!=null)
			//{
				//out.println(name);
				//out.println("<a href=\""+list.get(i).getWebContentLink()+"\"> Download </a>");
				//out.println("<br>");
			//}
		//}
	%>


</body>
</html>