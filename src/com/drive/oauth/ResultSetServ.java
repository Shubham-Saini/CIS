package com.drive.oauth;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;

@WebServlet(name = "ResultSet", urlPatterns = { "/ResultSet" })
public class ResultSetServ extends HttpServlet {
	private static final long serialVersionUID = 1L;
	PrintWriter out = null;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doPost(request,response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		HttpSession session = request.getSession();
		String caller = request.getParameter("caller");
		System.out.println("caller is " + caller);
		if(caller.equals("xml"))
		{
			response.setContentType("text/xml");
			out = response.getWriter();
			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			
			List<DriveFile> list = (List<DriveFile>)request.getAttribute("list");
			int size = list.size();
			int count = 1;
			out.println("<rows>");
			for(int i=0;i<size;i++)
			{
				boolean isFile =list.get(i).isFile();
				boolean isRoot=list.get(i).isRoot();
				if(isFile==false)
				{
					out.println("<row id='"+count+"'>"); count++;
					out.println("<cell>../icons/grid_folder.png</cell>");
					out.println("<cell>"+list.get(i).getFolderName()+"</cell>");
					out.println("<cell>Folder</cell>");
					out.println("<cell>"+list.get(i).getLastModified()+"</cell>");
					out.println("<cell>"+list.get(i).getFileId()+"</cell>");
					out.println("<cell>"+list.get(i).getParentId()+"</cell>");
					if(isRoot==true)
						out.println("<cell>Root</cell>");
						else 
							out.println("<cell> </cell>");
					out.println("<cell>folder</cell>");
					out.println("<cell>"+list.get(i).getUname()+"</cell>");
					out.println("</row>");
				}
				else
				{
					out.println("<row id='"+count+"'>"); count++;
					out.println("<cell>../icons/grid_file1.png</cell>");
					out.println("<cell>"+list.get(i).getFileName()+"</cell>");
					out.println("<cell>File</cell>");
					out.println("<cell>"+list.get(i).getLastModified()+"</cell>");
					out.println("<cell>"+list.get(i).getFileId()+"</cell>");
					out.println("<cell>"+list.get(i).getParentId()+"</cell>");
					if(isRoot==true)
					{
						out.println("<cell>Root</cell>");
					}
					else 	
					{
						out.println("<cell> </cell>");
					}
					out.println("<cell>file</cell>");
					out.println("<cell>"+list.get(i).getUname()+"</cell>");
					out.println("</row>");
				}
			}
			out.println("</rows>");
		}
		else if(caller.equalsIgnoreCase("treeStruct"))
		{
			System.out.println("Here in rs");
			if(session.getAttribute("listFile")==null)
			{
				System.out.println("File is null");
				return;
			}
			else
			{
				System.out.println("all right");
			}
			List<DriveFile> files = (List<DriveFile>)session.getAttribute("listFile");
			response.setContentType("text/xml");
			out = response.getWriter();
			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			Map<String,List<DriveFile>> map = new HashMap<String,List<DriveFile>>();
			System.out.println("lll");
			int size = files.size();
			System.out.println("lll2");
			String rootId=null;
			String fileId=null;
			List<String>roots = new ArrayList<String>();    //this is added;
			List<String> unames = new ArrayList<String>();
			Map<String,Integer> hashRoot= new HashMap<String,Integer>();//this is added;
 			for(int i=0;i<size;i++)
			{
				boolean isRoot=files.get(i).isRoot();
				boolean isFile = files.get(i).isFile();
				String parentId = null;
				if(isFile==false)
				{
					parentId = files.get(i).getParentId();
					if(isRoot==true)
					{
						rootId = files.get(i).getParentId();
						if(hashRoot.get(rootId)==null)
						{
							hashRoot.put(rootId,1);
							roots.add(rootId);
							unames.add(files.get(i).getUname());
							System.out.println("rootId is "+rootId);
						}
					}
					if(map.get(parentId)==null)
					{
						List<DriveFile> ff = new ArrayList<DriveFile>();
						ff.add(files.get(i));
						map.put(parentId, ff);
					}
					else
						map.get(parentId).add(files.get(i));
				}
			}
 			for(Map.Entry<String, List<DriveFile>> entry : map.entrySet())
			{
				String key = entry.getKey();
				List<DriveFile> values = map.get(key);
				System.out.println("Folder's name is " + key);
				int s = values.size();
				for(int i=0;i<s;i++)
				{
					System.out.println("  SubFolder's name is " + values.get(i).getFolderName());
				}
			}
			System.out.println("Root id is "+rootId);
			out.println("<tree id=\"0\">");
			int sizeRoot = roots.size();
			for(int i=0;i<sizeRoot;i++)
			{
				
				System.out.println("Sent to dfs "+roots.get(i));
				out.println("<item id=\""+roots.get(i)+"\" text=\""+unames.get(i)+"\" open=\"1\">");
				
				dfs(map,roots.get(i),new HashMap<String,Integer>());
				out.println("</item>");
				
			}
			out.println("</tree>");
		}
	}
	public void dfs(Map<String,List<DriveFile>> map,String rootId,Map<String,Integer> hash)
	{
		System.out.println("Received root id is "+rootId);
		if(map.get(rootId)!=null)
		{
			if(hash.get(rootId)==null)
			{
				hash.put(rootId, 1);
				int size = map.get(rootId).size();
				for(int i=0;i<size;i++)
				{
					String id = map.get(rootId).get(i).getFileId();
					String dispId  = map.get(rootId).get(i).getFileId();
					System.out.println("Root id "+rootId +"folder name is "+map.get(rootId).get(i).getFolderName());
					out.println("<item id=\""+dispId+"\" text=\""+map.get(rootId).get(i).getFolderName()+"\" im0=\"folderClosed.gif\">");
					if(hash.get(id)==null) dfs(map,id,hash);
					out.println("</item>");
				}
			}
		}
	}

}
