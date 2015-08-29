package com.drive.oauth;
import java.util.zip.*;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.simple.JSONObject;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Children;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.ChildList;
import com.google.api.services.drive.model.ChildReference;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

class Struct
{
	public String folder;
	public List<File> filesInside;
}
public class GoogleDrive 
{
	private static String clientId="";
	private static String clientSecret="";
	private static String redirectURI="http://localhost:8080/DriveProject/AuthHandler";
	private static String authenticationServerUrl="https://accounts.google.com/o/oauth2/auth";
	private static String tokenEndpointUrl="https://accounts.google.com/o/oauth2/token";
	private static String resourceServerUrl="https://www.googleapis.com/drive/v2/about";
	private static String scope = "https://www.googleapis.com/auth/drive https://www.googleapis.com/auth/userinfo.email";
	private static String state = null;
	private static String approvalPromptKey="approval_prompt";
	private static String approvalPromptValue="force";
	private static String accessTypeKey="access_type";
	private static String accessTypeValue="offline";
	private static String grantType = "authorization_code";
	
	public static String getAuthorizationCode(DriveUser user)
	{
		String location = null;
		List<BasicNameValuePair> parametersBody = new ArrayList<BasicNameValuePair>();
		HttpPost post = new HttpPost(getAuthenticationServerUrl());
		parametersBody.add(new BasicNameValuePair(OAuthConstants.RESPONSE_TYPE,
				OAuthConstants.CODE));
		parametersBody.add(new BasicNameValuePair(OAuthConstants.CLIENT_ID,getClientId()));
		parametersBody.add(new BasicNameValuePair(OAuthConstants.REDIRECT_URI,getRedirectURI()));
		if (isValid(getScope())) {
			parametersBody.add(new BasicNameValuePair(OAuthConstants.SCOPE,getScope()));
		}
		if (isValid(getApprovalPromptValue())) {
			parametersBody.add(new BasicNameValuePair(getApprovalPromptKey(),getApprovalPromptValue()));
		}
		if (isValid(getAccessTypeValue())) {
			parametersBody.add(new BasicNameValuePair(getAccessTypeKey(),getAccessTypeValue()));
		}
		if (isValid(state)) {
			parametersBody.add(new BasicNameValuePair(OAuthConstants.STATE,getState()));
		}
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = null;
		String accessToken = null;
		try
		{
			System.out.println("post is " + post);
			post.setEntity(new UrlEncodedFormEntity(parametersBody));
			response = client.execute(post);
			int code = response.getStatusLine().getStatusCode();
			System.out.println("Code is "+ code);
			//Map<String,String>map = OAuthUtil.handleResponse(response);
			//System.out.println("This is the response " +new Gson().toJson(map));
			if(OAuthConstants.HTTP_SEND_REDIRECT == code)
			{
				
				location = OAuthUtil.getHeader(response.getAllHeaders(),OAuthConstants.LOCATION_HEADER);
				if(location == null)
				{
					System.out.println("The endpoint did not pass in valid location header");
					throw new RuntimeException("The endpoint did not pass in valid location header");
				}
			}
			else
			{
				System.out.println("Application expected code 302 but recieved " + code + "insted.");
				throw new RuntimeException("Application expected code 302 but recieved " + code + "insted.");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return location;
	}
	public static Map<String,String> getAccessToken(String authorizationCode)
	{
		Map<String,String> map = new HashMap<String,String>();
		HttpPost post = new HttpPost(getTokenEndpointUrl());
		List<BasicNameValuePair> parametersBody = new ArrayList<BasicNameValuePair>();
		parametersBody.add(new BasicNameValuePair(OAuthConstants.GRANT_TYPE,getGrantType()));
		parametersBody.add(new BasicNameValuePair(OAuthConstants.CODE,authorizationCode));
		parametersBody.add(new BasicNameValuePair(OAuthConstants.CLIENT_ID,getClientId()));
		System.out.println(getClientSecret());
		if(isValid(getClientSecret()))
		{
			parametersBody.add(new BasicNameValuePair(OAuthConstants.CLIENT_SECRET,getClientSecret()));
		}
		parametersBody.add(new BasicNameValuePair(OAuthConstants.REDIRECT_URI,getRedirectURI()));
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = null;
		String accessToken=null;
		try
		{
			post.setEntity(new UrlEncodedFormEntity(parametersBody,HTTP.UTF_8));
			System.out.println(post.toString());
			response = client.execute(post);
			int code = response.getStatusLine().getStatusCode();
			map = OAuthUtil.handleResponse(response);
			accessToken = map.get(OAuthConstants.ACCESS_TOKEN);
			String refreshToken = map.get(OAuthConstants.REFRESH_TOKEN);
			System.out.println("access is " + accessToken+ " ref is " + refreshToken);
		}
		catch(Exception e)
		{
			System.out.println("Error in recieving Access Token");
			e.printStackTrace();
		}
		return map;
	}
	public static Drive getDriveService(DriveUser driveUser)
	{
		System.out.println("here!!!");
		HttpTransport httpTransport = new NetHttpTransport();
		JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
		TokenResponse tokenResponse = new TokenResponse();
		String clientId = getClientId();
		String clientSecret  = getClientSecret();
		tokenResponse.setAccessToken(driveUser.getAccessToken());
		tokenResponse.setRefreshToken(driveUser.getRefreshToken());
		Credential credential = new GoogleCredential.Builder().setTransport(httpTransport)
				.setJsonFactory(JSON_FACTORY)
				.setClientSecrets(clientId,clientSecret)
				.build().setFromTokenResponse(tokenResponse);
		Drive drive = new Drive.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName("Our").build();
		return drive;
	}
	public static List<File> getFileList(Drive drive)
	{
		List<File> result = new ArrayList<File>();
		try
		{
			Files.List request = drive.files().list();
			do
			{
				FileList files = request.execute();
				result.addAll(files.getItems());
				request.setPageToken(files.getNextPageToken());
			}while(request.getPageToken()!=null && request.getPageToken().length() > 0);
		}
		catch(IOException e)
		{
			System.out.println("Input output exception");
			e.printStackTrace();
		}
		catch(Exception ee)
		{
			System.out.println("More of a general exception");
			ee.printStackTrace();
		}
		return result;
	}
	
	public static List<DriveFile> getFileNames(Drive drive,String uname)
	{
		System.out.println("in printFileNames");
		List<File> list = getFileList(drive);
		String folderType = "application/vnd.google-apps.folder";
		List<DriveFile> files = new ArrayList<DriveFile>();
		int size = list.size();
		for(int i=0;i<size;i++)
		{
			String fileName = list.get(i).getTitle();
			String mimeType = list.get(i).getMimeType();
			boolean isFile = mimeType.equals(folderType)?false:true;
			String server = "google";
			if(fileName!=null || isFile==false)
			{
				String fileId = list.get(i).getId();
				boolean isRoot=true;
				String parentId="no parent";
				String folderName = "no folder",downloadLink="no link";
				List<ParentReference> parents = list.get(i).getParents();
				String ti = list.get(i).getModifiedDate().toString().split("T")[1].toString();
				String time = ti.substring(0,ti.indexOf('.'));
				String lastModified = list.get(i).getModifiedDate().toString().split("T")[0]+" "+time;
				System.out.println("File name " + fileName +" Explicitly trashed "+ list.get(i).getExplicitlyTrashed());
				if(parents.size() > 0)
				{
					if(parents.get(0).getIsRoot()==false)
					{
						isRoot=false;
					}
					parentId = parents.get(0).getId();
				}
				if(isFile == false)
				{
					folderName = list.get(i).getTitle();
					System.out.println("Folder name is " +  folderName);
				}
				else
					downloadLink = list.get(i).getDownloadUrl();
				files.add(new DriveFile(fileName,folderName,mimeType,downloadLink,isFile,isRoot,parentId
						,fileId,server,lastModified,uname));
			}
		}
		return files;
	}
//	public static void printFolders(List<DriveFile> files)
//	{
//		Map<String,List<DriveFile>> map = new HashMap<String,List<DriveFile>>();
//		int size = files.size(); String rootId=null;
//		String fileId=null;
//		for(int i=0;i<size;i++)
//		{
//			boolean isRoot=files.get(i).isRoot();
//			boolean isFile = files.get(i).isFile();
//			String parentId = null;
//			if(isFile==false)
//			{
//				parentId = files.get(i).getParentId();
//				if(isRoot==true)
//				{
//					System.out.println("it's root");
//					rootId = files.get(i).getParentId();
//				}
//				if(map.get(parentId)==null)
//				{
//					List<DriveFile> ff = new ArrayList<DriveFile>();
//					ff.add(files.get(i));
//					map.put(parentId, ff);
//				}
//				else
//					map.get(parentId).add(files.get(i));
//			}
//		}
//		for(Map.Entry<String, List<DriveFile>> entry : map.entrySet())
//			{
//				String key = entry.getKey();
//				List<DriveFile> values = map.get(key);
//				System.out.println("Folder's name is " + map.get(key));
//				int s = values.size();
//				for(int i=0;i<s;i++)
//				{
//					System.out.println("  SubFolder's name is " + values.get(i).getFolderName());
//				}
//			}
//		System.out.println("Root id is "+rootId);
//		System.out.println("<tree id=\"0\">");
//		System.out.println("<item id=\"google\" text=\"Google\" open=\"1\">");
//		
//		dfs(map,rootId,new HashMap<String,Integer>());
//		
//		System.out.println("</item>");
//		System.out.println("</tree>");
//	}
//	public static void dfs(Map<String,List<DriveFile>> map,String rootId,Map<String,Integer> hash)
//	{
//		if(map.get(rootId)!=null)
//		{
//			if(hash.get(rootId)==null)
//			{
//				hash.put(rootId, 1);
//				int size = map.get(rootId).size();
//				for(int i=0;i<size;i++)
//				{
//					String id = map.get(rootId).get(i).getFileId();
//					String dispId  = map.get(rootId).get(i).getFileId();
//					System.out.println("<item id=\""+dispId+"\" text=\""+map.get(rootId).get(i).getFolderName()+"\" im0=\"folderClosed.gif\">");
//					if(hash.get(id)==null) dfs(map,id,hash);
//					System.out.println("</item>");
//				}
//			}
//		}
//		
//	}
	public static InputStream downloadFile(Drive drive, String fileId)
	{
		try
		{
			File file = drive.files().get(fileId).execute();
			System.out.println("File's title is " + file.getTitle());
			if (file.getDownloadUrl() != null && file.getDownloadUrl().length() > 0)
			{
				com.google.api.client.http.HttpResponse resp =
			            drive.getRequestFactory().buildGetRequest(new GenericUrl(file.getDownloadUrl()))
			                .execute();
			        InputStream inputStream =  new BufferedInputStream(resp.getContent());
			        return inputStream;
			}
		}
		catch(Exception e)
		{
			System.out.println("Trouble downloading files");
			e.printStackTrace();
		}
		return null;
		 
	}
	public static void move(Drive drive,String fileId,DriveUser dest)
	{
		InputStream inStream = downloadFile(drive,fileId);
		OutputStream outStream = null;
		try
		{
			File file = drive.files().get(fileId).execute();
			String fileName = file.getTitle();
			if (file.getDownloadUrl() != null && file.getDownloadUrl().length() > 0)
			{
				com.google.api.client.http.HttpResponse resp =
			            drive.getRequestFactory().buildGetRequest(new GenericUrl(file.getDownloadUrl()))
			                .execute();
			       inStream =  new BufferedInputStream(resp.getContent());
			       java.io.File fileMove = new java.io.File("H:\\Java\\Web Development\\Apache Tomcat 7.0\\webapps\\temp\\"+fileName);
			       outStream = new FileOutputStream(fileMove);
			       int read = 0;
					byte[] bytes = new byte[1024];
			 
					while ((read = inStream.read(bytes)) != -1) {
						outStream.write(bytes, 0, read);
					}
					DropService.uploadFile(dest, fileMove, "/");
			}
		}
		catch(Exception e)
		{
			System.out.println("Trouble downloading files");
			e.printStackTrace();
		}
		
	}
//	public static String uploadFile(Drive drive,java.io.File file)
//	{
//		File body = new File();
//		body.setTitle(file.getName());
//		body.setDescription("A test upload");
//		body.setMimeType("text/plain");
//		FileContent mediaContent = new FileContent("text/plain",file);
//		try
//		{
//			File fileUpload = drive.files().insert(body,mediaContent).execute();
//			System.out.println("Google Drive uploaded file name is " +  fileUpload.getOriginalFilename());
//			return "Success";
//		}
//		catch(Exception e)
//		{
//			System.out.println("Could not upload file to google drive");
//			e.printStackTrace();
//		}
//		return "failure";
//	}
	public static String insertFile(Drive service, String title, String description,String parentId, String mimeType, String filename)
	{
		    // File's metadata.
		    File body = new File();
		    body.setTitle(title);
		    body.setDescription(description);
		    body.setMimeType(mimeType);

		    // Set the parent folder.
		    if (parentId != null && parentId.length() > 0) {
		      body.setParents(
		          Arrays.asList(new ParentReference().setId(parentId)));
		    }

		    // File's content.
		    java.io.File fileContent = new java.io.File(filename);
		    FileContent mediaContent = new FileContent(mimeType, fileContent);
		    try 
		    {
		      File file = service.files().insert(body, mediaContent).execute();
		      return "success";
		    } catch (IOException e) 
		    {
		      System.out.println("An error occured: " + e);
		    }
		    return "failure";
	}

	public static boolean renameFile(Drive drive, String fileId, String newTitle) {
	    try {
	      File file = new File();
	      file.setTitle(newTitle);

	      // Rename the file.
	      Files.Patch patchRequest = drive.files().patch(fileId, file);
	      patchRequest.setFields("title");

	      File updatedFile = patchRequest.execute();
	      return true;
	    } catch (IOException e) {
	      System.out.println("An error occurred: " + e);
	      e.printStackTrace();
	      return false;
	    }
	  }

	public static boolean deleteFile(Drive drive,String fileId)
	{
		boolean result = false;
		try
		{
			drive.files().delete(fileId).execute();
			result=true;
		}
		catch(Exception e)
		{
			System.out.println("Trouble deleting files");
			e.printStackTrace();
		}
		return result;
	}
	public static boolean cutPaste(Drive drive,String folderSourceId,String folderDestId,String fileId)
	{
		System.out.println("file id " + fileId);
		System.out.println("Folder id " + folderSourceId);
		System.out.println("Dest id "+folderDestId);
		try
		{
			drive.parents().delete(fileId, folderSourceId).execute();
			ParentReference newParent = new ParentReference();
		    newParent.setId(folderDestId);
		    drive.parents().insert(fileId, newParent).execute();
		    System.out.println("Cut and paste successful");
		    return true;
		}
		catch(Exception e)
		{
			System.out.println("Troouble in cut and paste");
			e.printStackTrace();
		}
		return false;
	}
	public static boolean copyPaste(Drive drive,String fileId,String folderId,String fileTitle)
	{
		File copiedFile = new File();
		System.out.println("file id "+fileId);
		System.out.println("folder id "+folderId);
		System.out.println("file title "+fileTitle);
		copiedFile.setTitle(fileTitle);
		copiedFile.setParents(Arrays.asList(new ParentReference().setId(folderId)));
		try
		{
			copiedFile =  drive.files().copy(fileId, copiedFile).execute();
			if(copiedFile!=null)
		    {
		    	System.out.println("ref is "+copiedFile.toPrettyString());
		    }
			return true;
		}
		catch(Exception e)
		{
			System.out.println("Trouble copying files google drive");
			e.printStackTrace();
		}
		return false;
	}
	public static String getClientId() {
		return clientId;
	}
	public static void setClientId(String clientId) {
		GoogleDrive.clientId = clientId;
	}
	public static String getClientSecret() {
		return clientSecret;
	}
	public static void setClientSecret(String clientSecret) {
		GoogleDrive.clientSecret = clientSecret;
	}
	public static String getRedirectURI() {
		return redirectURI;
	}
	public static void setRedirectURI(String redirectURI) {
		GoogleDrive.redirectURI = redirectURI;
	}
	public static String getAuthenticationServerUrl() {
		return authenticationServerUrl;
	}
	public static void setAuthenticationServerUrl(String authenticationServerUrl) {
		GoogleDrive.authenticationServerUrl = authenticationServerUrl;
	}
	public static String getTokenEndpointUrl() {
		return tokenEndpointUrl;
	}
	public static void setTokenEndpointUrl(String tokenEndpointUrl) {
		GoogleDrive.tokenEndpointUrl = tokenEndpointUrl;
	}
	public static String getResourceServerUrl() {
		return resourceServerUrl;
	}
	public static void setResourceServerUrl(String resourceServerUrl) {
		GoogleDrive.resourceServerUrl = resourceServerUrl;
	}
	public static String getScope() {
		return scope;
	}
	public static void setScope(String scope) {
		GoogleDrive.scope = scope;
	}
	public static boolean isValid(String arg)
	{
		if(arg==null || arg.trim().length()==0)
		{
			return false;
		}
		return true;
	}
	public static String getState() {
		return state;
	}
	public static void setState(String state) {
		GoogleDrive.state = state;
	}
	public static String getApprovalPromptKey() {
		return approvalPromptKey;
	}
	public static void setApprovalPromptKey(String approvalPromptKey) {
		GoogleDrive.approvalPromptKey = approvalPromptKey;
	}
	public static String getApprovalPromptValue() {
		return approvalPromptValue;
	}
	public static void setApprovalPromptValue(String approvalPromptValue) {
		GoogleDrive.approvalPromptValue = approvalPromptValue;
	}
	public static String getAccessTypeKey() {
		return accessTypeKey;
	}
	public static void setAccessTypeKey(String accessTypeKey) {
		GoogleDrive.accessTypeKey = accessTypeKey;
	}
	public static String getAccessTypeValue() {
		return accessTypeValue;
	}
	public static void setAccessTypeValue(String accessTypeValue) {
		GoogleDrive.accessTypeValue = accessTypeValue;
	}
	public static String getGrantType()
	{
		return grantType;
	}
}
