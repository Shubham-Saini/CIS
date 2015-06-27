package com.drive.oauth;
import java.util.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

@WebServlet("/UploadFiles")
public class UploadFiles extends HttpServlet {
	private static final long serialVersionUID = 1L;
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("In servlet");
		String uploadDir = "D:/";

		String mode = request.getParameter("mode");
		String action = "";

		ServletFileUpload uploader = null;
		List<FileItem> items = null;

		if (mode == null || (mode != null && !mode.equals("conf") && !mode.equals("sl"))) {
			uploader = new ServletFileUpload(new DiskFileItemFactory());
			try
			{
			items = uploader.parseRequest(request);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}

		if (mode == null) {
			mode = "";
			for (FileItem item : items) {
				if (item != null) {
					if (item.getFieldName().equals("mode")) {
						try
						{
						mode = getStringFromStream(item.getInputStream());
						}catch(Exception e)
						{
							e.printStackTrace();
						}
					}
					if (item.getFieldName().equals("action")) {
						try
						{
						action = getStringFromStream(item.getInputStream());
						}catch(Exception e)
						{
							e.printStackTrace();
						}
					}
				}
			}
		}
		

		if (mode.equals("conf")) {
			int maxPostSize = 2000000;
			
			response.setHeader("Content-type", "text/json");
			response.getWriter().write("{ \"maxFileSize\":" + maxPostSize + " }");
		}

		if (mode.equals("html4") || mode.equals("flash") || mode.equals("html5")) {
			if (action.equals("cancel")) {
				response.setHeader("Content-type", "text/json");
				response.getWriter().write("{\"state\":\"cancelled\"}");
			} else {
				String filename = "";
				Integer filesize = 0;
				
				for (FileItem item : items) {
					if (!item.isFormField()) {
						// Process form file field (input type="file").
						String fieldname = item.getFieldName();
						filename = FilenameUtils.getName(item.getName());
						InputStream filecontent = item.getInputStream();

						// Write to file
						/* File f=new File(uploadDir + filename);
						FileOutputStream fout=new FileOutputStream(f);
						byte buf[]=new byte[1024];
						int len;
						while((len=filecontent.read(buf))>0) {
							fout.write(buf,0,len);
							filesize+=len;
						}
						fout.close(); */
					}
				}
				// Manual filesize value only for demo!
				filesize = 28428;

				response.setHeader("Content-type", "text/html");
				response.getWriter().write("{\"state\":true,\"name\":\"" + filename.replace("\"","\\\"") + "\",\"size\":" + filesize + ",\"extra\":{\"info\":\"just a way to send some extra data\",\"param\":\"some value here\"}}");
			}
		}

		HashMap p = new HashMap();

		Enumeration params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String name = (String)params.nextElement();
			p.put(name, request.getParameter(name));
		}
		PrintWriter out = null;
		String fileName = request.getParameter("fileName");
		String fileSize = request.getParameter("fileSize");
		String fileKey = request.getParameter("fileKey");
		if (mode != null && mode.equals("sl") && fileName != null && fileSize != null && fileKey != null) {
			action = request.getParameter("action");

			if (action != null && action.equals("getUploadStatus")) {
				response.setContentType("text/json");
				out = response.getWriter();
				out.print("{state: true, name:'" + fileName + "'}");
			} else {
				/* FileOutputStream file = new FileOutputStream(uploadDir+fileName);
				file.write(IOUtils.readFully(request.getInputStream(), -1, false));
				file.close(); */
			}
		}
	}

	public String getStringFromStream(InputStream is) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

}
