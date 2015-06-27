package com.drive.oauth;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

public class DriveService 
{
	
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
	public static List<File> printFileNames(Drive drive)
	{
		System.out.println("in printFileNames");
		List<File> list = getFileList(drive);
		int size  = list.size();
		for(int i=0;i<size;i++)
		{
			System.out.println("File's name is " + list.get(i).getOriginalFilename());
			System.out.println("open with link is "+list.get(i).getDownloadUrl());
		}
		return list;
	}
	public static String uploadFile(Drive drive,java.io.File file)
	{
		File body = new File();
		body.setTitle(file.getName());
		body.setDescription("A test upload");
		body.setMimeType("text/plain");
		FileContent mediaContent = new FileContent("text/plain",file);
		try
		{
			File fileUpload = drive.files().insert(body,mediaContent).execute();
			System.out.println("Google Drive uploaded file name is " +  fileUpload.getOriginalFilename());
			return "Success";
		}
		catch(Exception e)
		{
			System.out.println("Could not upload file to google drive");
			e.printStackTrace();
		}
		return "failure";
	}
}
