package com.drive.oauth;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpConnection;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class OneDriveService 
{
	private static String clientId="";
	private static String clientSecret="";
	private static String redirectURI="http://localhostred.com:8080/DriveProject/Hand";
	private static String authenticationServerUrl="https://login.live.com/oauth20_authorize.srf";
	private static String tokenEndpointUrl="https://login.live.com/oauth20_token.srf";
	private static String scope = "wl.offline_access%20wl.skydrive_update";
	private static String state = null;
	private static String grantType = "authorization_code";
	public static String currentAccessToken=null;
	public static List<DriveFile> files=null;
	public static String getAuthorizationCode()
	{
		String param = "?client_id="+getClientId()+"&scope="+scope+"&response_type=code&redirect_uri="+getRedirectURI();
		String url = getAuthenticationServerUrl()+param;
		return url;
	}
	public static Map<String,String> getAccessToken(String code)
	{
		Map<String,String> map = new HashMap<String,String>();
		HttpPost post = new HttpPost(getTokenEndpointUrl());
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = null;
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		list.add(new BasicNameValuePair(OAuthConstants.CLIENT_ID,getClientId()));
		list.add(new BasicNameValuePair(OAuthConstants.REDIRECT_URI,getRedirectURI()));
		list.add(new BasicNameValuePair(OAuthConstants.CLIENT_SECRET,getClientSecret()));
		list.add(new BasicNameValuePair(OAuthConstants.CODE,code));
		list.add(new BasicNameValuePair(OAuthConstants.GRANT_TYPE,getGrantType()));
		try
		{
			post.setEntity(new UrlEncodedFormEntity(list));
			System.out.println("up to here");
			response = client.execute(post);
			int cod = response.getStatusLine().getStatusCode();
			map = OAuthUtil.handleJsonResponse(response);
			String accessToken = map.get("access_token");
			System.out.println("Access Token is "+accessToken);
			System.out.println("here "+new Gson().toJson(map));
		}
		catch(Exception e)
		{
			System.out.println("Error getting access tokens in one drive");
			e.printStackTrace();
		}
		return map;
	}
	public static boolean refreshToken(String uname,String accessToken,String refreshToken)
	{
		boolean result = false;
		try
		{
			String params = "?client_id="+getClientId()+"&client_secret="+getClientSecret()+"&refresh_token="+refreshToken+"&grant_type=refresh_token"+"&redirect_uri="+getRedirectURI();
			URL url = new URL(getTokenEndpointUrl()+params);
			URLConnection urlConn = url.openConnection();
			String outputString = "",line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			while ((line = reader.readLine()) != null) {
			        outputString += line;
			}
			Gson gson = new Gson();
			Map<String,String>map = new HashMap<String,String>();
			map = (Map<String,String>)gson.fromJson(outputString,map.getClass());
			String access_token = map.get("access_token");
			DatabaseConnect.updateTokens(uname, access_token);
			currentAccessToken = access_token;
			System.out.println(outputString);
		
		}
		catch(Exception e)
		{
			System.out.println("Trouble in refreshing token");
			e.printStackTrace();
		}
		return result;
	}
	public static JSONArray listFiles(String accessToken,String parentFolder)
	{
		String urlRoot ="https://apis.live.net/v5.0/me/skydrive/files";
		String urlFolder = "https://apis.live.net/v5.0/";
		String append=null;
		URL url = null;
		JSONArray data=null;
		String params = "?access_token="+accessToken;
		try
		{
			if(parentFolder.equals("Root"))
			{
				url = new URL(urlRoot+params);
			}
			else 
			{
				append = urlFolder+parentFolder+"/files";
				url = new URL(append+params);
			}
			URLConnection urlConn = url.openConnection();			
			String outputString = "",line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			while ((line = reader.readLine()) != null) {
			        outputString += line;
			}
			JSONObject obj =new JSONObject(outputString);
			data = obj.getJSONArray("data");
			System.out.println("Output string is "+outputString);
		}
		catch(Exception e)
		{
			System.out.println("Trouble in listing files");
			e.printStackTrace();
		}
		return data;
	}
	public static void getFiles(String accessToken,String parent,String uname,Map<String,Integer> hash)
	{
		try
		{
			if(hash.get(parent)==null)
			{
				hash.put(parent, 1);
				JSONArray data = listFiles(accessToken,parent);
				int len = data.length();
				for(int i=0;i<len;i++)
				{
					String fileName="not",folderName="not",mimeType="not",downloadLink="not",parentId="not",fileId="not",server="onedrive",lastModified="not";
					boolean isFile=false,isRoot=false;
					JSONObject file = data.getJSONObject(i);
					String type = file.getString("type");
					if(type.equals("folder"))
					{
						if(parent.equals("Root"))
							isRoot=true;
						folderName = file.getString("name");
						parentId = file.getString("parent_id");
						fileId = file.getString("id");
						lastModified = file.getString("client_updated_time");
						DriveFile driveFile = new DriveFile(fileName,folderName,mimeType,downloadLink,isFile,isRoot,parentId,fileId,server,lastModified,uname);
						files.add(driveFile);
						getFiles(accessToken,fileId,uname,hash);
					}
					else if(type.equals("file"))
					{
						isFile=true;
						if(parent.equals("Root"))
							isRoot=true;
						fileName = file.getString("name");
						downloadLink = file.getString("source");
						parentId = file.getString("parent_id");
						fileId = file.getString("id");
						lastModified = file.getString("client_updated_time");
						DriveFile driveFile = new DriveFile(fileName,folderName,mimeType,downloadLink,isFile,isRoot,parentId,fileId,server,lastModified,uname);
						files.add(driveFile);
					}
				}
			}
		}
		catch(Exception e)
		{
				System.out.println("trouble in getFiles");
				e.printStackTrace();
		}
	}
	public static List<DriveFile> getFileNames(DriveUser user,String uname)
	{
		user = checkToken(user);
		try
		{
			files = new ArrayList<DriveFile>();
			Map<String,Integer> hash = new HashMap<String,Integer>();
			getFiles(user.getAccessToken(),"Root",uname,hash);
			int size = files.size();
			for(int i=0;i<size;i++)
			{
				if(files.get(i).isFile())
				System.out.println("File name is "+files.get(i).getFileName());
				else
					System.out.println("Folder name is "+files.get(i).getFolderName());
			}
		}
		catch(Exception e)
		{
			System.out.println("Trouble in getFileNames");
			e.printStackTrace();
		}
		return files;
	}
	public static InputStream downloadFile(DriveUser user,String fileId)
	{
		user = checkToken(user);
		InputStream inputStream = null;
		try
		{
			String downloadUrl = "https://apis.live.net/v5.0/"+fileId+"/content";
			String params = "?access_token="+user.getAccessToken();
			URL url = new URL(downloadUrl+params);
			URLConnection urlConn = url.openConnection();
			inputStream = urlConn.getInputStream();
		}
		catch(Exception e)
		{
			System.out.println("trouble downloading file onedrive");
			e.printStackTrace();
		}
		return inputStream;
	}
	public static String getFileName(DriveUser user,String fileId)
	{
		user = checkToken(user);
		String urlName = "https://apis.live.net/v5.0/"+fileId;
		String params = "?access_token="+user.getAccessToken();
		String fname= null;
		try
		{
			URL url = new URL(urlName+params);
			URLConnection urlConn = url.openConnection();			
			String outputString = "",line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			while ((line = reader.readLine()) != null) {
			        outputString += line;
			}
			System.out.println("File name out put ");
			JSONObject fileName = new JSONObject(outputString);
			fname = fileName.getString("name");
			System.out.println("File's name is "+fname);
			System.out.println(outputString);	
		}
		catch(Exception e)
		{
			System.out.println("trouble getting filename in onedrive");
			e.printStackTrace();
		}
		return fname;
	}
	public static boolean renameFile(DriveUser user,String fileId,String newTitle)
	{
		user = checkToken(user);
		boolean result = false;
		try
		{
			String urlRename = "https://apis.live.net/v5.0/"+fileId;
			String jsonString = "{\"name\": \""+newTitle+"\"}";
			URL url = new URL(urlRename);
			HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setDoOutput(true);
			httpCon.setRequestProperty(OAuthConstants.AUTHORIZATION, OAuthConstants.BEARER+" "+user.getAccessToken());
			httpCon.setRequestProperty("Content-Type", "application/json");
			httpCon.setRequestMethod("PUT");
			OutputStreamWriter out = new OutputStreamWriter(
			    httpCon.getOutputStream());
			out.write(jsonString);
			out.close();
			InputStream stream = httpCon.getInputStream();
			System.out.println("Rename successful");
		}
		catch(Exception e)
		{
			System.out.println("Trouble renameing the file onedrive");
			e.printStackTrace();
		}
		return result;
	}
	public static boolean cutPaste(DriveUser user,String fileId,String folderId)
	{
		boolean result = false;
		user = checkToken(user);
		try
		{
			String moveUrl = "https://apis.live.net/v5.0/"+fileId;
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpUriRequest moveRequest = RequestBuilder
		            .create("MOVE")
		            .setUri(moveUrl)
		            .addHeader(OAuthConstants.AUTHORIZATION, OAuthConstants.BEARER+" "+user.getAccessToken())
		            .addHeader("Content-Type", "application/json")
		            .build(); 
			
			System.out.println("fileId is "+fileId);
			
//			URL url = new URL(moveUrl);
//			HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
//			httpCon.setDoOutput(true);
//			httpCon.setRequestProperty(OAuthConstants.AUTHORIZATION, OAuthConstants.BEARER+" "+user.getAccessToken());
//			httpCon.setRequestProperty("Content-Type", "application/json");
//			httpCon.setRequestProperty("X-HTTP-Method-Override", "MOVE");
//			httpCon.setRequestMethod("POST");
//			String jsonParam = "{\"destination\": \""+folderId+"\"}";
//			OutputStreamWriter out = new OutputStreamWriter(
//				    httpCon.getOutputStream());
//			out.write(jsonParam);
//			int responseCode = httpCon.getResponseCode();
//			System.out.println("response code is "+responseCode);
			
		}
		catch(Exception e)
		{
			System.out.println("Trouble moving files in onedrive");
			e.printStackTrace();
		}
		return result;
	}
	public static boolean deleteFile(DriveUser user,String fileId)
	{
		user = checkToken(user);
		boolean result = false;
		String urlDelete = "https://apis.live.net/v5.0/";
		String params=fileId+"?access_token="+user.getAccessToken();
		try
		{
			URL url = new URL(urlDelete+params);
			HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
			
			httpCon.setRequestMethod("DELETE");
			httpCon.connect();
			int code = httpCon.getResponseCode();
			System.out.println("url is");
			System.out.println(urlDelete+params);
			System.out.println("Response code is "+code);
			result = true;
			if(code==204)
			System.out.println("deletion successful");
			else
				System.out.println("deletion not successful");
		}
		catch(Exception e)
		{
			System.out.println("trouble deleting files");
			e.printStackTrace();
		}
		return result;
	}
	public static DriveUser checkToken(DriveUser user)
	{
		System.out.println("checking token");
		String accessToken = user.getAccessToken();
		String refreshToken = user.getRefreshToken();
		if(accessToken.equals(currentAccessToken))
			return user;
		String uname = user.getName();
		try
		{
			System.out.println("in try checking token");
			URL url = new URL("https://apis.live.net/v5.0/me/skydrive/quota?access_token="+ accessToken);
			URLConnection urlConn = url.openConnection();
			String outputString = "",line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			while ((line = reader.readLine()) != null) {
			        outputString += line;
			}
			Gson gson = new Gson();
			Map<String,String>map = new HashMap<String,String>();
			map = (Map<String,String>)gson.fromJson(outputString,map.getClass());
			if(map.get("quota")!=null)
			{
				currentAccessToken = accessToken;
			}
			else
			{
				refreshToken(uname,accessToken,refreshToken);
				user.setAccessToken(currentAccessToken);
			}
			System.out.println(outputString);
		}
		catch(Exception e)
		{
			System.out.println("Trouble checking token");
			//e.printStackTrace();
			refreshToken(uname,accessToken,refreshToken);
			user.setAccessToken(currentAccessToken);
		}
		return user;
	}
	public static void uploadFileOne(DriveUser user,File file,String folderId)
	{
		 user  = checkToken(user);
		 String fileName = file.getName();
		 String LINE_FEED ="\r\n";
		 try
		 {
			 System.out.println("in try upload file");
			 String urlUpload = "https://apis.live.net/v5.0/";
			 String params = folderId+"/files"+"?access_token="+user.getAccessToken();
			 URL url = new URL(urlUpload+params);
			 String boundary = "A300x";
			 HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			 conn.setUseCaches(false); conn.setDoOutput(true); conn.setDoInput(true);
		     conn.setRequestProperty("Content-Type","multipart/form-data; boundary=" + boundary);
		     OutputStream out = conn.getOutputStream();
		     PrintWriter writer = new PrintWriter(new OutputStreamWriter(out,"UTF-8"),true);
		     writer.append("--" + boundary).append(LINE_FEED);
		     writer.append("Content-Disposition: form-data; name=\"" + "file"+ "\"; filename=\"" + fileName + "\"")
		                .append(LINE_FEED);
		     writer.append("Content-Type: "+ URLConnection.guessContentTypeFromName(fileName)).append(LINE_FEED);
		     writer.append(LINE_FEED);
		     writer.flush();
		     FileInputStream inputStream = new FileInputStream(file);
		     byte[] buffer = new byte[4096];
		     int bytesRead = -1;
		     while ((bytesRead = inputStream.read(buffer)) != -1) 
		     {
		           out.write(buffer, 0, bytesRead);
		     }
		     out.flush();
		     inputStream.close();   
		     writer.append(LINE_FEED);
		     writer.flush();     
		     writer.append(LINE_FEED).flush();
		        writer.append("--" + boundary + "--").append(LINE_FEED);
		        writer.close();
		        int code = conn.getResponseCode();
		        System.out.println("response code is "+code);
		     System.out.println("end");
			 }
		 catch(Exception e)
		 {
			 System.out.println("Trouble uploading files..");
			 e.printStackTrace();
		 }
	}
	public static Map<String,String> handleJsonResponse(HttpResponse response)
	{
		Map<String,String> map = null;
		try
		{
			map = (Map<String,String>) new JSONParser().parse(EntityUtils.toString(response.getEntity()));
		}
		catch(Exception e)
		{
			System.out.println("Could not parse JSON Content");
			e.printStackTrace();
		}
		return map;
	}
	public static String getClientId() {
		return clientId;
	}
	public static void setClientId(String clientId) {
		OneDriveService.clientId = clientId;
	}
	public static String getClientSecret() {
		return clientSecret;
	}
	public static void setClientSecret(String clientSecret) {
		OneDriveService.clientSecret = clientSecret;
	}
	public static String getRedirectURI() {
		return redirectURI;
	}
	public static void setRedirectURI(String redirectURI) {
		OneDriveService.redirectURI = redirectURI;
	}
	public static String getAuthenticationServerUrl() {
		return authenticationServerUrl;
	}
	public static void setAuthenticationServerUrl(String authenticationServerUrl) {
		OneDriveService.authenticationServerUrl = authenticationServerUrl;
	}
	public static String getTokenEndpointUrl() {
		return tokenEndpointUrl;
	}
	public static void setTokenEndpointUrl(String tokenEndpointUrl) {
		OneDriveService.tokenEndpointUrl = tokenEndpointUrl;
	}
	public static String getScope() {
		return scope;
	}
	public static void setScope(String scope) {
		OneDriveService.scope = scope;
	}
	public static String getState() {
		return state;
	}
	public static void setState(String state) {
		OneDriveService.state = state;
	}
	public static String getGrantType() {
		return grantType;
	}
	public static void setGrantType(String grantType) {
		OneDriveService.grantType = grantType;
	}
	
}
