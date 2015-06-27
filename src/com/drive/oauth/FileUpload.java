package com.drive.oauth;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.api.services.drive.Drive;

@WebServlet("/FileUpload")
public class FileUpload extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private boolean isMultipart;
	private String filePath;
	private java.io.File file;
	private int maxFileSize = 50*1024*1024;
	private int maxMemSize = 4*1024;
	private Drive drive;
	private final String UPLOAD_DIRECTORY = "H:\\Java\\Web Development\\Apache Tomcat 7.0\\webapps\\temp\\";
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		System.out.println("Hello from upload");
		isMultipart = ServletFileUpload.isMultipartContent(request);
		if(!isMultipart)
		{
			System.out.println("Must be multipart");
			return;
		}
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(maxMemSize);
		factory.setRepository(new java.io.File("H:\\Java\\Web Development\\Apache Tomcat 7.0\\webapps\\temp"));
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setSizeMax(maxFileSize);
		try
		{
			List fileItems = upload.parseRequest(request);
			Iterator i = fileItems.iterator();
			String uname=null;
			String fileId = null;
			while(i.hasNext())
			{
				FileItem fi = (FileItem)i.next();
				if(fi.isFormField())
				{
					if(fi.getFieldName().equalsIgnoreCase("uname"))
					{
						uname = fi.getString();
					}
					else if(fi.getFieldName().equals("fileId"))
					{
						fileId = fi.getString();
					}
				}
			}
			i = fileItems.iterator();
			DriveUser driveUser = DatabaseConnect.getAccount(uname);
			drive = GoogleDrive.getDriveService(driveUser);
			while(i.hasNext())
			{
				FileItem fi = (FileItem)i.next();
				if(!fi.isFormField())
				{
					String fieldName = fi.getFieldName();
					String fileName = fi.getName();
					String contentType = fi.getContentType();
					boolean isInMemory = fi.isInMemory();
					long sizeInBytes = fi.getSize();
					String title=null;
					if(fileName.lastIndexOf("\\")>=0)
					{
						file = new java.io.File(fileName.substring(fileName.lastIndexOf("\\")));
						title = fileName.substring(fileName.lastIndexOf("\\"));
					}
					else
					{
						file = new java.io.File(fileName.substring(fileName.lastIndexOf("\\")+1));
						title = fileName.substring(fileName.lastIndexOf("\\")+1);
					}
					fi.write(file);
					System.out.println("Uploaded File's name "+fileName);
					String result = null;
					DriveUser user = DatabaseConnect.getAccount(uname);
					if(user.getServer().equals("google"))
					{
						result = GoogleDrive.insertFile(drive, title, "null", fileId, fi.getContentType(), fi.getName());
						if(result.equalsIgnoreCase("success"))
						{
							if(file.delete())
							{
								System.out.println("File uploaded and deleted successfully.");
							}
							else
							{
								System.out.println("File uploaded but not deleted.");
							}
						}
						else
						{
							System.out.println("File's not uploaded.");
						}
					}
					else if(user.getServer().equals("dropbox"))
					{
						DropService.uploadFile(user,file,fileId);
					}
					else if(user.getServer().equals("onedrive"))
					{
						System.out.println("In onedrive file upload");
						OneDriveService.uploadFileOne(user, file,fileId);
					}
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Could not Upload File");
			e.printStackTrace();
		}

	}

}
