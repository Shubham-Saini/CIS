package com.drive.oauth;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import com.dropbox.core.*;
import com.google.gson.Gson;

public class DropService
{
	private static DbxClient dbxClient;
	private static String accessToken;
	private static String clientId="s5x67zsjs7b8i15";
	private static String clientSecret="evevu1o6hjao0ge";
	private static String redirectURI="http://localhost:8080/DriveProject/AuthHandler";
	private static String authenticationServerUrl="https://www.dropbox.com/1/oauth2/authorize";
	private static String tokenEndpointUrl="https://api.dropbox.com/1/oauth2/token";
	private static String state = null;
	private static String grantType = "authorization_code";
	public static List<DriveFile> files;
	public static int count=0;
	public static String getAuthorizationCode()
	{
		String location = getAuthenticationServerUrl();
		String params = "?response_type=code"+"&client_id="+getClientId()+"&redirect_uri="+getRedirectURI(); 
		location = location+params;
		return location;
	}
	public static void setAccessToken(String access_token)
	{
		accessToken = access_token;
	}
	public static void connect(String accessToken)
	{
		DbxRequestConfig dbxRequestConfig = new DbxRequestConfig(
				"JavaDropboxTutorial/1.0", Locale.getDefault().toString());
		 dbxClient = new DbxClient(dbxRequestConfig,accessToken);
		 setAccessToken(accessToken);
		 System.out.println("Connect count is "+(++count));
	}
	public static DbxClient getClient(DriveUser user)
	{
		if(!accessToken.equals(user.getAccessToken()))
		{
			connect(user.getAccessToken());
		}
		return dbxClient;
	}
	public static String getUserInfo(String access_token)
	{
		String name=null;
		connect(access_token);
		try
		{
			name = dbxClient.getAccountInfo().displayName;
			System.out.println("name is " + name);
		}
		catch(Exception e)
		{
			System.out.println("Error getting user info..");
			e.printStackTrace();
		}
		return name;
	}
	public static Map<String,String> getAccessToken(String code)
	{
		String accessToken = null;
		Map<String,String> map = new HashMap<String,String>();
		try
		{
			HttpPost post = new HttpPost("https://api.dropbox.com/1/oauth2/token");
			List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
			list.add(new BasicNameValuePair("code",code));
			list.add(new BasicNameValuePair("grant_type","authorization_code"));
			list.add(new BasicNameValuePair("client_id",getClientId()));
			list.add(new BasicNameValuePair("client_secret", getClientSecret()));
			list.add(new BasicNameValuePair("redirect_uri",getRedirectURI()));
			HttpClient client = HttpClientBuilder.create().build();
			HttpResponse response = null;
			post.setEntity(new UrlEncodedFormEntity(list));
			response = client.execute(post);
			map = OAuthUtil.handleJsonResponse(response);
			accessToken = map.get(OAuthConstants.ACCESS_TOKEN);
			System.out.println(new Gson().toJson(map));
		}
		catch(Exception e)
		{
			System.out.println("Trouble gettting DropBox token");
			e.printStackTrace();
		}
		return map;
	}
	public static void listFolders(String access_token)
	{
		if(!access_token.equals(accessToken))
		{
			connect(access_token);
		}
		try
		{
			DbxEntry.WithChildren listing = dbxClient.getMetadataWithChildren("/");
			System.out.println("Files in the root path:");
			for (DbxEntry child : listing.children) 
			{
			    System.out.println("	" + child.name + ": " + child.toString());
			}
		}
		catch(Exception e)
		{
			System.out.println("Error in get folders..");
			e.printStackTrace();
		}
	}
	public static void listFiles(String parent,Map<String,Integer>hash,String uname)
	{
		try
		{
			if(hash.get(parent)==null)
			{
				hash.put(parent,1);
				DbxEntry.WithChildren listing = dbxClient.getMetadataWithChildren(parent);
				for (DbxEntry child : listing.children)       
				{
					String fileName="not",folderName="not",mimeType="not",downloadLink="not",parentId="not",fileId="not",server="dropbox",lastModified="not";
					boolean isFile,isRoot=false;
					if(child.isFolder())
					{
						
						folderName = child.name;
						parentId = parent;
						fileId = child.path;
						isFile=false;
						if(parent.equals("/"))
							isRoot=true;
						else isRoot = false;
						DriveFile driveFile=null;
						try
						{
							driveFile = new DriveFile(fileName,folderName,mimeType,downloadLink,isFile,isRoot,parentId,fileId,server,lastModified,uname);
						}
						catch(Exception e)
						{
							System.out.println("not generating ///");
						}
						if(driveFile!=null)
						files.add(driveFile);
						else
						{
							System.out.println("null thing");
						}
						System.out.println("Adding folder "+ folderName);
						listFiles(fileId,hash,uname);
					}
					else if(child.isFile())
					{
						System.out.println("after file");
						fileName = child.name;
						downloadLink = dbxClient.createShareableUrl(child.path);
						isFile = true;
						if(parent.equals("/"))
							isRoot=true;
						else isRoot = false;
						parentId = parent;
						fileId = child.path;
						lastModified = child.asFile().lastModified.toString();
						files.add(new DriveFile(fileName,folderName,mimeType,downloadLink,isFile,isRoot,parentId
								,fileId,server,lastModified,uname));
						if(isRoot==false)
						System.out.println("Adding file "+ fileName);
					}
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Trouble getting dropbox files");
			e.printStackTrace();
		}
		
	}
	public static List<DriveFile> getFileNames(DriveUser driveUser,String uname)
	{
		String access_token = driveUser.getAccessToken();
		if(!access_token.equals(accessToken))
		{
			connect(access_token);
		}
		try
		{
			files = new ArrayList<DriveFile>();
			Map<String,Integer> hash = new HashMap<String,Integer>();
			listFiles("/", hash, uname);
			int size = files.size();
			for(int i=0;i<size;i++)
			{
				if(files.get(i).isFile())
				{
					System.out.println("File name is " + files.get(i).getFileName());
					System.out.println("Contained in "+files.get(i).getParentId());
				}
				else
				{
					System.out.println("Folder's name is "+files.get(i).getFolderName());
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("trouble geting file names...");
			e.printStackTrace();
		}
		return files;
	}
	public static InputStream downloadFile(DriveUser driveUser,String path)
	{
		if(driveUser==null || path==null)
		{
			System.out.println("Here in dropbox null");
		}
		String access_token = driveUser.getAccessToken();
		if(!access_token.equals(accessToken))
		{
			connect(access_token);
		}
		ByteArrayOutputStream outputStream=null;
		ByteArrayInputStream inputStream = null;
		try 
		{
			DbxEntry file = dbxClient.getMetadata(path);
			String fileName = file.asFile().name;
			outputStream = new ByteArrayOutputStream();
			System.out.println("file namee " + fileName);
		    DbxEntry.File downloadedFile = dbxClient.getFile(path, null,outputStream);
		    System.out.println("After");
		    byte b[] = outputStream.toByteArray();
		    inputStream = new ByteArrayInputStream(b);
		    System.out.println("Metadata: " + downloadedFile.toString());
		} catch(Exception e) 
		{
		   System.out.println("trouble downloading dropbox files.");
		   e.printStackTrace();
		}
		return inputStream;
	}
	public static boolean uploadFile(DriveUser user,File file,String location)
	{
		String access_token = user.getAccessToken();
		if(!accessToken.equals(access_token))
		{
			connect(access_token);
		}
		boolean result = false;
		FileInputStream inputStream=null;
		try
		{
			inputStream = new FileInputStream(file);
			location = location+"/"+file.getName();
			System.out.println("Location for saving is "+location);
			DbxEntry.File uploadedFile = dbxClient.uploadFile(location,
			        DbxWriteMode.add(), file.length(), inputStream);
			    System.out.println("Uploaded: " + uploadedFile.toString());
			    if(uploadedFile!=null)
			    	result = true;
		}
		catch(Exception e)
		{
			System.out.println("Trouble uploading files.");
			e.printStackTrace();
		}
		return result;
	}
	public static boolean copyPaste(DriveUser user,String source,String destination,String fileTitle)
	{
		boolean result = false;
		String access_token = user.getAccessToken();
		destination = destination +"/"+fileTitle;
		if(!accessToken.equals(access_token))
		{
			connect(access_token);
		}
		try
		{
			DbxEntry entry = dbxClient.copy(source, destination);
			if(entry==null)
				System.out.println("Copy paste failed dropbox");
		}
		catch(Exception e)
		{
			System.out.println("Copying failed in dropbox.");
			e.printStackTrace();
		}
		return result;
	}
	public static boolean cutPaste(DriveUser user,String source,String destination,String fileTitle)
	{
		boolean result = false;
		String access_token = user.getAccessToken();
		destination = destination +"/"+fileTitle;
		if(!accessToken.equals(access_token))
		{
			connect(access_token);
		}
		try
		{
			DbxEntry entry = dbxClient.move(source, destination);
			if(entry==null)
				System.out.println("Cut paste failed dropbox.");
			else
				System.out.println("Cut paste succesful");
			System.out.println("Source is "+source+" destination is "+destination);
		}
		catch(Exception e)
		{
			System.out.println("Copying failed in dropbox.");
			e.printStackTrace();
		}
		return result;
	}
	public static boolean delete(DriveUser driveUser,String path)
	{
		boolean result = false;
		String access_token = driveUser.getAccessToken();
		if(!accessToken.equals(access_token))
		{
			connect(accessToken);
		}
		try
		{
			dbxClient.delete(path);
			result =true;
			System.out.println("File deleted "+path);
		}
		catch(Exception e)
		{
			System.out.println("trouble deleting dropbox");
		}
		return result;
	}
	
	
	public static String getRedirectURI() {
		return redirectURI;
	}
	public static void setRedirectURI(String redirectURI) {
		DropService.redirectURI = redirectURI;
	}
	public static String getAuthenticationServerUrl() {
		return authenticationServerUrl;
	}
	public static void setAuthenticationServerUrl(String authenticationServerUrl) {
		DropService.authenticationServerUrl = authenticationServerUrl;
	}
	public static String getTokenEndpointUrl() {
		return tokenEndpointUrl;
	}
	public static void setTokenEndpointUrl(String tokenEndpointUrl) {
		DropService.tokenEndpointUrl = tokenEndpointUrl;
	}
	public static String getState() {
		return state;
	}
	public static void setState(String state) {
		DropService.state = state;
	}
	public static String getGrantType() {
		return grantType;
	}
	public static void setGrantType(String grantType) {
		DropService.grantType = grantType;
	}
	public static String getAccessToken() {
		return accessToken;
	}
	public static String getClientId() {
		return clientId;
	}
	public static String getClientSecret() {
		return clientSecret;
	}
}
