package com.drive.oauth;

import java.util.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.simple.*;
import org.json.simple.parser.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;


public class OAuthUtil 
{
	public static Properties getConfigProps(String path)
	{
		InputStream in = OAuthUtil.class.getClassLoader().getResourceAsStream(path);
		Properties config = new Properties();
		try
		{
			config.load(in);
		}
		catch(Exception e)
		{
			System.out.println("Could not load properties from " + path+"\n");
			e.printStackTrace();
			return null;
		}
		return config;
	}
	public static Map<String,String> getUserInfo(DriveUser user)
	{
		Map<String,String> map = new HashMap<String,String>();
		String access_token = user.getAccessToken();
		try
		{
			URL url = new URL("https://www.googleapis.com/oauth2/v1/userinfo?access_token="+ access_token);
			URLConnection urlConn = url.openConnection();
			String outputString = "",line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			while ((line = reader.readLine()) != null) {
			        outputString += line;
			}
			Gson gson = new Gson();
			map = new HashMap<String,String>();
			map = (Map<String,String>)gson.fromJson(outputString,map.getClass());
			System.out.println(outputString);
			return map;
		}
		catch(Exception e)
		{
			System.out.println("Trouble getting email");
			e.printStackTrace();
		}
		return map;
	}
	
	public static String getHeader(Header headers[],String name)
	{
		String header = null;
		if(headers!=null)
		{
			for(Header h: headers)
			{
				if(h.getName().equalsIgnoreCase(name))
				{
					header = h.getValue();
				}
			}
		}
		return header;
	}
	public static Map<String,String> handleResponse(HttpResponse response)
	{
		String contentType = OAuthConstants.JSON_CONTENT;
		if(response.getEntity().getContentType()!=null)
		{
			contentType = response.getEntity().getContentType().getValue();
		}
		if(contentType.contains(OAuthConstants.JSON_CONTENT))
		{
			return handleJsonResponse(response);
		}
		else if(contentType.contains(OAuthConstants.XML_CONTENT))
		{
			return handleXMLResponse(response);
		}
		else if(contentType.contains(OAuthConstants.URL_ENCODED_CONTENT))
		{
			return handleURLEncodedResponse(response);
		}
		else
		{
			throw new RuntimeException("Content type is not supported");
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
	public static Map handleURLEncodedResponse(HttpResponse response) {
		Map<String, Charset> map = Charset.availableCharsets();
		Map<String, String> oauthResponse = new HashMap<String, String>();
		Set<Map.Entry<String, Charset>> set = map.entrySet();
		Charset charset = null;
		HttpEntity entity = response.getEntity();

		System.out.println();
		System.out.println("********** Response Received **********");

		for (Map.Entry<String, Charset> entry : set) {
			System.out.println(String.format("  %s = %s", entry.getKey(),
					entry.getValue()));
			if (entry.getKey().equalsIgnoreCase(HTTP.UTF_8)) {
				charset = entry.getValue();
			}
		}

		try {
			List<NameValuePair> list = URLEncodedUtils.parse(
					EntityUtils.toString(entity), Charset.forName(HTTP.UTF_8));
			for (NameValuePair pair : list) {
				System.out.println(String.format("  %s = %s", pair.getName(),
						pair.getValue()));
				oauthResponse.put(pair.getName(), pair.getValue());
			}

		} catch (IOException e) 
		{
			e.printStackTrace();
			throw new RuntimeException("Could not parse URLEncoded Response");
		}

		return oauthResponse;
	}
	public static Map handleXMLResponse(HttpResponse response) {
		Map<String, String> oauthResponse = new HashMap<String, String>();
		try {

			String xmlString = EntityUtils.toString(response.getEntity());
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder db = factory.newDocumentBuilder();
			InputSource inStream = new InputSource();
			inStream.setCharacterStream(new StringReader(xmlString));
			Document doc = db.parse(inStream);

			System.out.println("********** Response Receieved **********");
			parseXMLDoc(null, doc, oauthResponse);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(
					"Exception occurred while parsing XML response");
		}
		return oauthResponse;
	}

	public static void parseXMLDoc(Element element, Document doc,
			Map<String, String> oauthResponse) {
		NodeList child = null;
		if (element == null) {
			child = doc.getChildNodes();

		} else {
			child = element.getChildNodes();
		}
		for (int j = 0; j < child.getLength(); j++) {
			if (child.item(j).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
				org.w3c.dom.Element childElement = (org.w3c.dom.Element) child
						.item(j);
				if (childElement.hasChildNodes()) {
					System.out.println(childElement.getTagName() + " : "
							+ childElement.getTextContent());
					oauthResponse.put(childElement.getTagName(),
							childElement.getTextContent());
					parseXMLDoc(childElement, null, oauthResponse);
				}

			}
		}
	}
	
	
	public static String getAuthorizationHeaderForAccessToken(String accessToken)
	{
		return OAuthConstants.BEARER + " " + accessToken;
	}
	public static boolean isValid(String arg)
	{
		if(arg==null || arg.trim().length()==0)
		{
			return false;
		}
		return true;
	}
}
