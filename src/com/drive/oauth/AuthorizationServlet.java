package com.drive.oauth;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;


import com.dropbox.core.DbxEntry;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.gson.Gson;

public class AuthorizationServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	private boolean isMultipart;
	private String filePath;
	private java.io.File file;
	private int maxFileSize = 50*1024*1024;
	private int maxMemSize = 4*1024;
	private Drive drive;
	private static String unameFull=null;
	public void init()
	{
		filePath = getServletContext().getInitParameter("file-upload");
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doPost(request,response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		PrintWriter out = null;
		HttpSession session = request.getSession();
		String caller = request.getParameter(OAuthConstants.CALLER);
		String code = request.getParameter(OAuthConstants.CODE);
		String serverName = request.getParameter(OAuthConstants.SERVER);
		Properties config = OAuthUtil.getConfigProps(OAuthConstants.CONFIG_FILE_PATH);
		
		System.out.println("Hello from AUth servlet caller is " + caller);
		{
			if(OAuthUtil.isValid(caller))
			{
				System.out.println("Hello");
				if(caller.equalsIgnoreCase(OAuthConstants.TOKEN))
				{
					String source = request.getParameter("source");
					DriveUser driveUser = new DriveUser();
					unameFull = request.getParameter("uname");
					System.out.println("uname is "+unameFull);
					driveUser.setName(unameFull);
					driveUser.setServer(source);
					driveUser.setUserId((String)session.getAttribute("email"));
					session.setAttribute("server", driveUser);
					if(source.equals("google"))
					{
						String location = GoogleDrive.getAuthorizationCode(driveUser);
						System.out.println("Location = "+location);
						//session.setAttribute("server",driveUser);
						response.sendRedirect(location);
						return;
					}
					else if(source.equals("dropbox"))
					{
						String location = DropService.getAuthorizationCode();
						System.out.println("Location is "+ location);
						//session.setAttribute("server",driveUser);
						response.sendRedirect(location);
						return;
					}
					else if(source.equals("onedrive"))
					{
						String location = OneDriveService.getAuthorizationCode();
						System.out.println("Location is "+location);
						//session.setAttribute("server",driveUser);
						response.sendRedirect(location);
						return;
					}
				}
				else if(caller.equalsIgnoreCase("Upload"))
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
								if(fileName.lastIndexOf("\\")>=0)
								{
									file = new java.io.File(filePath + fileName.substring(fileName.lastIndexOf("\\")));
								}
								else
								{
									file = new java.io.File(filePath + fileName.substring(fileName.lastIndexOf("\\")+1));
								}
								fi.write(file);
								out = response.getWriter();
								out.println("Uploaded File's name "+fileName);
								String email = (String)session.getAttribute("email");
								out.println(DriveService.uploadFile(drive,file));
							}
						}
					}
					catch(Exception e)
					{
						System.out.println("Could not Upload File");
						e.printStackTrace();
					}
				}
				else if(caller.equalsIgnoreCase("up"))
				{
					System.out.println("IN up");
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload
					upload = new ServletFileUpload(factory);
					 
					java.util.List uploadedItems = null;
					FileItem fileItem = null;
					String filePath = "H:\\Java\\Web Development\\Apache Tomcat 7.0\\webapps\\temp";
					 
					try
					{
						uploadedItems = upload.parseRequest(request);
						Iterator i = uploadedItems.iterator();
					 
						while (i.hasNext())
						{
							fileItem = (FileItem) i.next();
					 
							if (fileItem.isFormField() == false)
							{
								if (fileItem.getSize() > 0)
								{
									java.io.File uploadedFile = null;
									String myFullFileName = fileItem.getName(),
									myFileName = "",slashType = (myFullFileName.lastIndexOf("\\")> 0) ? "\\" : "/";
									int	startIndex = myFullFileName.lastIndexOf(slashType);
									myFileName = myFullFileName.substring(startIndex + 1, myFullFileName.length());
									uploadedFile = new java.io.File(filePath, myFileName); 
									fileItem.write(uploadedFile);
								}
							}
						}
					}
					catch (FileUploadException e)
					{
						e.printStackTrace();
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					
				}
				else if(caller.equalsIgnoreCase("resource"))
				{
					
					String userId = (String)session.getAttribute("email");
					List<DriveUser>users = DatabaseConnect.getAccounts(userId);
					List<DriveFile> end = new ArrayList<DriveFile>();
					out = response.getWriter();
					int size = users.size();
					out.println("list's size is"+size);
					for(int i=0;i<size;i++)
					{
						if(users.get(i).getServer().equals("google"))
						{
							String uname = users.get(i).getName();
							drive = GoogleDrive.getDriveService(users.get(i));
							List<DriveFile> files  = GoogleDrive.getFileNames(drive,uname);
							end.addAll(files);
						}
						else if(users.get(i).getServer().equals("dropbox"))
						{
							System.out.println("Working on it...");
							String accessToken = users.get(i).getAccessToken();
							System.out.println("Access token is "+accessToken);
							List<DriveFile> files = DropService.getFileNames(users.get(i),users.get(i).getName());
							end.addAll(files);
						}
						else if(users.get(i).getServer().equals("onedrive"))
						{
							System.out.println("Working on it...OneDrive");
							String uname = users.get(i).getName();
							List<DriveFile> files = OneDriveService.getFileNames(users.get(i), uname);
							end.addAll(files);
						}
					}
					session.setAttribute("listFile", end);
					request.setAttribute("list",end);
					RequestDispatcher req = request.getRequestDispatcher("/ResultSet?caller=xml");
					req.forward(request, response);
					return;
					//System.out.println("and here");
				}
				else if(caller.equalsIgnoreCase("logout"))
				{
					request.getSession().invalidate();
					RequestDispatcher disp = request.getRequestDispatcher("/Login.jsp");
					disp.forward(request, response);
				}
				else if(caller.equalsIgnoreCase("ajax"))
				{
					System.out.println("ajax hello");
					String operation = request.getParameter("operation");
					if(operation.equalsIgnoreCase("delete"))
					{
						String fileId = request.getParameter("fileId");
						String uname = request.getParameter("uname");
						System.out.println("File id: "+fileId);
						DriveUser user = DatabaseConnect.getAccount(uname);
						if(user.getServer().equals("google"))
						{
							drive = GoogleDrive.getDriveService(user);
							GoogleDrive.deleteFile(drive, fileId);
						}
						else if(user.getServer().equals("dropbox"))
						{
							DropService.delete(user, fileId);
						}
						else if(user.getServer().equals("onedrive"))
						{
							System.out.println("In one drive delete");
							OneDriveService.deleteFile(user, fileId);
						}
					}
					else if(operation.equalsIgnoreCase("rename"))
					{
						String fileId = request.getParameter("fileId");
						String uname = request.getParameter("uname");
						String oldTitle = request.getParameter("oldName");
						String newTitle = request.getParameter("newTitle");
						DriveUser user = DatabaseConnect.getAccount(uname);
						if(user.getServer().equals("google"))
						{
							drive = GoogleDrive.getDriveService(user);
							boolean result = GoogleDrive.renameFile(drive, fileId,newTitle);
						}
						else if(user.getServer().equals("dropbox"))
						{
							System.out.println("in rename fileId "+fileId+" newTitle "+newTitle);
							String dest = fileId.substring(0,fileId.lastIndexOf("/"));
							System.out.println("destination is "+dest);
							DropService.cutPaste(user, fileId, dest,newTitle);
						}
						else if(user.getServer().equals("onedrive"))
						{
							System.out.println("in rename onedrive fileId "+fileId+" new Title "+newTitle);
							OneDriveService.renameFile(user, fileId, newTitle);
						}
					}
					else if(operation.equalsIgnoreCase("download"))
					{
						String fileId = request.getParameter("fileId");
						String uname = request.getParameter("uname");
						DriveUser user = DatabaseConnect.getAccount(uname);
						if(user.getServer().equalsIgnoreCase("google"))
						{
							drive = GoogleDrive.getDriveService(user);
							
							File file = drive.files().get(fileId).execute();
							String fileType = file.getMimeType();
							String fileTitle = file.getTitle();
							response.setContentType(fileType);
							response.setHeader("Content-Disposition",
				                     "attachment;filename="+fileTitle);
							InputStream stream = GoogleDrive.downloadFile(drive, fileId);
							int read=0;
							byte[] bytes = new byte[1024];
							OutputStream os = response.getOutputStream();
						    
							while((read = stream.read(bytes))!= -1)
							{
								os.write(bytes, 0, read);
							}
							os.flush();
							os.close();
						}
						else if(user.getServer().equals("dropbox"))
						{
							System.out.println("in dropbox download");
							try
							{
								DbxEntry.File file = DropService.getClient(user).getMetadata(fileId).asFile();
								String fileTitle = file.name;
								response.setHeader("Content-Disposition",
					                     "attachment;filename="+fileTitle);
								
								InputStream stream = DropService.downloadFile(user, fileId);
								int read=0;
								byte[] bytes = new byte[1024];
								OutputStream os = response.getOutputStream();
							    
								while((read = stream.read(bytes))!= -1)
								{
									os.write(bytes, 0, read);
								}
								os.flush();
								os.close();
							}
							catch(Exception e)
							{
								System.out.println("Trouble in downloading files.. dropbox");
								e.printStackTrace();
							}	
						}
						else if(user.getServer().equals("onedrive"))
						{
							try
							{
								System.out.println("In download onedrive");
								String fileName = OneDriveService.getFileName(user, fileId);
								response.setHeader("Content-Disposition",
					                     "attachment;filename="+fileName);
								
								InputStream stream = OneDriveService.downloadFile(user, fileId);
								int read=0;
								byte[] bytes = new byte[1024];
								OutputStream os = response.getOutputStream();
								while((read = stream.read(bytes))!= -1)
								{
									os.write(bytes, 0, read);
								}
								os.flush();
								os.close();
							}
							catch(Exception e)
							{
								System.out.println("trouble downloading file onedrive in auth handler");
								e.printStackTrace();
							}
						}
					}
				}
				else if(caller.equalsIgnoreCase("test"))
				{
					System.out.println("Here in test");
					System.out.println("Request url "+ request.getRequestURL().toString() );
					if(request.getParameter("uname")!=null)
					{
						String uname  = request.getParameter("uname");
					}
					else
					{
						System.out.println("not received");
					}
				}
				else if(caller.equalsIgnoreCase("cutandpaste"))
				{
					String uname = request.getParameter("uname");
					DriveUser driveUser = DatabaseConnect.getAccount(uname);
					String folderSourceId = request.getParameter("folderSourceId");
					String folderDestId = request.getParameter("folderDestId");
					String fileId = request.getParameter("fileId");
					String fileTitle = request.getParameter("fileTitle");
					if(driveUser.getServer().equals("google"))
					{
						drive = GoogleDrive.getDriveService(driveUser);
						GoogleDrive.cutPaste(drive, folderSourceId, folderDestId, fileId);
					}
					else if(driveUser.getServer().equals("dropbox"))
					{
						System.out.println("File title "+fileTitle);
						if(fileTitle!=null || !fileTitle.equals("null"))
						{
							DropService.cutPaste(driveUser, fileId, folderDestId,fileTitle);
						}
					}
					else if(driveUser.getServer().equals("onedrive"))
					{
						System.out.println("In cutpaste onedrive fileTitle " +fileTitle);
						OneDriveService.cutPaste(driveUser, fileId, folderDestId);
					}
				}
				else if(caller.equalsIgnoreCase("copypaste"))
				{
					String uname = request.getParameter("uname");
					String fileId = request.getParameter("fileId");
					String fileTitle = request.getParameter("fileTitle");
					String folderId = request.getParameter("folderDestId");
					DriveUser driveUser = DatabaseConnect.getAccount(uname);
					if(driveUser.getServer().equals("google"))
					{
						drive = GoogleDrive.getDriveService(driveUser);
						GoogleDrive.copyPaste(drive, fileId, folderId, fileTitle);
					}
					else if(driveUser.getServer().equals("dropbox"))
					{
						DropService.copyPaste(driveUser, fileId, folderId,fileTitle);
					}
				}
			}
			else if(OAuthUtil.isValid(code))
			{
				DriveUser driveUser = (DriveUser)session.getAttribute("server");
				System.out.println("entry");
				if(driveUser == null)
				{
					System.out.println("Object is null");
				}
				else 
					System.out.println("server is " + driveUser.getServer());
				String server = driveUser.getServer();
				System.out.println("server is " + server);
				if(server.equals("google"))
				{
					Map<String,String> map = GoogleDrive.getAccessToken(code);
					String accessToken = map.get(OAuthConstants.ACCESS_TOKEN);
					String refreshToken = map.get(OAuthConstants.REFRESH_TOKEN);
					System.out.println("Refresh token is" + refreshToken);
					driveUser.setAccessToken(accessToken); driveUser.setRefreshToken(refreshToken);
					map = OAuthUtil.getUserInfo(driveUser);
					driveUser.setEmail(map.get("email").toString());
					boolean result = DatabaseConnect.storeTokens(driveUser);
					System.out.println("Result is "+ result);
					System.out.println("in code uname is "+unameFull);
					request.setAttribute("uname", unameFull);
					RequestDispatcher disp  = request.getRequestDispatcher("/Welcome.jsp");
					disp.forward(request, response);
				}
				else if(server.equals("dropbox"))
				{
					System.out.println("in here");
					Map<String,String> map = DropService.getAccessToken(code);
					String accessToken = map.get(OAuthConstants.ACCESS_TOKEN);
					String uid = map.get("uid");
					driveUser.setAccessToken(accessToken); driveUser.setEmail(uid);
					DatabaseConnect.storeTokens(driveUser);
					request.setAttribute("uname", unameFull);
					RequestDispatcher disp  = request.getRequestDispatcher("/Welcome.jsp");
					disp.forward(request, response);
				}
				else if(server.equals("onedrive"))
				{
					System.out.println("onedrive");
					Map<String,String> map = OneDriveService.getAccessToken(code);
					String accessToken = map.get(OAuthConstants.ACCESS_TOKEN);
					String refreshToken = map.get(OAuthConstants.REFRESH_TOKEN);
					driveUser.setRefreshToken(refreshToken);
					driveUser.setAccessToken(accessToken);
					DatabaseConnect.storeTokens(driveUser);
					System.out.println("Sizeof accessToken is "  + accessToken.length());
					System.out.println("Refresh token is "+ refreshToken.length());
					request.setAttribute("uname", unameFull);
					RequestDispatcher disp  = request.getRequestDispatcher("/Welcome.jsp");
					disp.forward(request, response);
				}
			}
		}
		
	}

}
