<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="java.util.*,com.google.api.services.drive.model.File" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
     <title>File manager</title>
	 <style>
    html, body {
        width: 100%;      /*provides the correct work of a full-screen layout*/ 
        height: 100%;     /*provides the correct work of a full-screen layout*/
        overflow: hidden; /*hides the default body's space*/
        margin: 0px;      /*hides the body's scrolls*/
    }
	</style>
	<link rel="stylesheet" type="text/css" href="codebase/dhtmlxvault.css">
	   <script src="codebase/dhtmlx.js"></script> 
	   <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8/jquery.js" ></script>
		<script src="http://malsup.github.com/jquery.form.js" ></script>
		<script src="js/fileUploadScript.js" ></script>
<link href="css/style.css" rel="stylesheet" type="text/css" />
	     <script src="codebase/dhtmlxvault.js"></script>
	     <script src="codebase/swfobject.js"></script> 
	     <link rel="stylesheet" type="text/css" href="codebase/dhtmlx.css">
	     
	     <script type="text/javascript">
    if (document.addEventListener) {
        document.addEventListener('contextmenu', function(e) {
            e.preventDefault();
        }, false);
    } else {
        document.attachEvent('oncontextmenu', function() {
            window.event.returnValue = false;
        });
    }
</script>
  </head>
  <body>
  <%
	if(session.getAttribute("email")==null)
	{%>
		<script>
			document.location.href="LReg.jsp";
		</script>
	<% }
  %>
    <script type="text/javascript">
         dhtmlxEvent(window,"load",function(){
		 var myLayout = new dhtmlXLayoutObject(document.body,"2U"); 
		 myLayout.cells("a").setWidth(250);
		 myLayout.cells("a").setText("Accounts");
		 myLayout.cells("b").hideHeader(); 
		 var myToolbar = myLayout.attachToolbar(); 
		 myToolbar.setIconsPath("icons/");
		 myToolbar.loadStruct("data/toolbarStruct.xml");
		
		 //myTree.loadXML("data/treeStruct.xml");
		 
		 var myGrid = myLayout.cells("b").attachGrid();
		 myGrid.setImagePath("codebase/imgs/");    //sets the path to the source images
		 myGrid.setIconsPath("icons/");                //sets the path to custom images
		 myGrid.setHeader("&nbsp;,Name,Type,Modified,id,pid,root,type,uname");  
		 myGrid.enableMultiselect(true);
		 myGrid.setColTypes("img,ed,ro,ro,ro,ro,ro,ro,ro");           //sets the types of columns
		 myGrid.setInitWidths("70,250,100,*,0,0,0,0,0");   //sets the initial widths of columns
		 myGrid.setColAlign("center,left,left,left,left,left,left,left,left");
		 myGrid.init();
		 //document.location.href = "AuthHandler?caller=resource";
		 var myTree = myLayout.cells("a").attachTree();
		 myTree.setImagesPath("codebase/imgs/");
		 myGrid.load("http://localhost:8080/DriveProject/AuthHandler?caller=resource",function(){
			 myTree.loadXML("http://localhost:8080/DriveProject/ResultSet?caller=treeStruct",function(){
				var selectedId = myTree.getItemIdByIndex(0,0);
				myTree.selectItem(selectedId,false,false); 
				//myTree.findItem("Google",0,1);
				myGrid.sortRows(7,"str","desc");
				var id = myTree.getSelectedItemId();
				var fileId = document.getElementById("fileId");
				var unameValue = myGrid.cellById(myGrid.getRowId(0),8).getValue();
				var uname = document.getElementById("uname");
				uname.value = unameValue;
				fileId.value = id;
			 });
		 });
		 
		// myGrid.attachEvent("onDataReady",function(){
			// var idSelected = myTree.getSelectedItemId();
				//myGrid.filterBy(6,"Root"); 
		 //});
		 myGrid.attachEvent("onRowDblClicked", function(id,ind){
			 	var ro = myGrid.cellById(id,2).getValue();
			 	var cm = "Folder";
			 	if(ro.localeCompare(cm)==0)
			 	{
			 		myGrid.filterBy(5,myGrid.cellById(id,4).getValue());
			 		myTree.selectItem(myGrid.cellById(id,4).getValue());
			 		var id = myGrid.cellById(id,4).getValue();
					var fileId = document.getElementById("fileId");
					fileId.value = id;
			 	}
			 	myGrid.sortRows(7,"str","desc");
			});
		 myTree.attachEvent("onSelect",  function(id){
				
			 var parentId = myTree.getParentId(id);
				if(parentId==0)
				{
					var text = myTree.getItemText(id);
					myGrid.filterBy(6,"Root");
					myGrid.filterBy(8,text,true);
				}
				else
				{
					myGrid.filterBy(5,id);
				}
		 		myGrid.sortRows(7,"str","desc");
		
			var fileId = document.getElementById("fileId");
			fileId.value=id;
			 var unameValue = myGrid.cellById(myGrid.getRowId(0),8).getValue();
				var uname = document.getElementById("uname");
				uname.value = unameValue;
				return true;
		 	});
		 //myTree.loadXML("data/treeStruct.xml",function(){            
		 //myGrid.load("data/gridData.xml", function(){  
			// myTree.selectItem("books"); })
			 //});
		 
		 var myPop = new dhtmlXPopup({ toolbar: myToolbar, id: "add" });
		 myPop.attachObject("wrapper");
		 var  filePop = new dhtmlXPopup();
		 filePop.attachList("operation", [
		         {id: 1, operation: "Download"},
		         {id: 2, operation: "Copy"},
		         {id: 3, operation: "Cut"},
		         {id: 4, operation: "Paste"},
		         {id: 5, operation: "Delete"},
		         {id: 6, operation: "Rename"}
		 ]);
		 myToolbar.attachEvent("onClick", function(id){
			    if(id.localeCompare("reload")==0)
		    	{
		    		document.location.href="file.jsp";
		    	}
			    else if(id.localeCompare("logout")==0)
		    	{
		    		document.location.href = "ValidHandler?caller=logout";
		    	}
			    else if(id.localeCompare("home")==0)
		    	{
		    		document.location.href = "Welcome.jsp"
		    	}
			});
		 myGrid.attachEvent("onRightClick",function(id,ind,obj){
			 //alert(myGrid.getSelectedRowId());
			 obj.preventDefault();
			 var y=myGrid.cells(id,ind).cell.offsetTop+10;
		        var x = obj.screenX;
		 		filePop.show(x,y,75,75);
		 });
		 myGrid.attachEvent("onEmptyClick", function(ev){
			    filePop.hide();
			});
		 myGrid.attachEvent("onRowSelect", function(id,ind){
			 //ajaxRequest();
			 	filePop.hide();
			});
		 var folderSourceId = null,folderDestId=null;
		 var cutFileId = null; var operation=null; var copyFileId=null;
		 var fileTitle=null;
		 filePop.attachEvent("onClick", function(id){
			 var cellId = myGrid.getSelectedRowId();
			 var fileId = myGrid.cellById(cellId,4).getValue();
			 var uname = myGrid.cellById(cellId,8).getValue();
			 if(id==5)
			 {
		      	ajaxRequest(fileId,uname,"delete","thi","thi");
		      	myGrid.deleteRow(cellId);
			 }
			 else if(id==6)
			 {
				 var rowId = myGrid.getSelectedRowId();
			 	 var cellIndex = myGrid.getSelectedCellIndex();
			 		//myGrid.selectCell(rowId,cellIndex);
			 		myGrid.editCell();
			 }
			 else if(id==1)
			 {
				ajaxRequest(fileId,uname,"download","null","null"); 
			 }
			 else if(id==2)
			 {
				 operation = "copy";
				 fileTitle = myGrid.cellById(cellId,1).getValue();
				 copyFileId = fileId;
			 }
			 else if(id==3)
			 {
			 	folderSourceId = myGrid.cellById(cellId,5).getValue();
			 	fileTitle = myGrid.cellById(cellId,1).getValue();
			 	cutFileId = fileId;
			 	operation = "cut";
			 }
			 else if(id==4)
			 {
				if(operation.localeCompare("cut")==0)
				{
				 	folderDestId = myTree.getSelectedItemId();
				 	cutPaste(uname,folderSourceId,folderDestId,cutFileId,fileTitle);
				}
				else if(operation.localeCompare("copy")==0)
				{
					folderDestId = myTree.getSelectedItemId();
					copyPaste(uname,copyFileId,fileTitle,folderDestId);
				}
			 }
			});
		 myGrid.attachEvent("onEditCell", function(stage,rId,cInd,nValue,oValue){
			    if(stage==2)
			    {
			    	var fileId = myGrid.cellById(rId,4).getValue();
			    	var uname = myGrid.cellById(rId,8).getValue();
			    	alert(fileId +" "+ uname);
			    	ajaxRequest(fileId,uname,"rename",nValue,oValue);
			    	myGrid.setUserData(rId,"Name",nValue);
			    	return nValue;	
			    }
			});
// 		 var myVault;
// 		 myPop.attachEvent("onShow", function(){
// 		     if (!myVault) {
// 		         myVault = myPop.attachVault(350, 200, {         // width and height
// 		             uploadUrl:  "http://localhost:8080/DriveProject/upload_handler.jsp",          // html4/html5 upload url
		             
// 		         });
// 		         myVault.attachEvent("onUploadComplete", function(){
// 		             // you can hide popup here
// 		         });
// 		     }
// 		 });
		 
		 
		 });
    </script>
    <script language="javascript">
    var request;
	function ajaxRequest(fileId,uname,operation,newTitle,oValue)
	{
		
		if(window.XMLHttpRequest){  
		request=new XMLHttpRequest();  
		}  
		else if(window.ActiveXObject){  
		request=new ActiveXObject("Microsoft.XMLHTTP");  
		}  
		
		if(operation.localeCompare("delete")==0)
		{
			try
			{
				request.open("POST","AuthHandler",true);  
				request.setRequestHeader("Content-type","application/x-www-form-urlencoded");
				var params = "caller=ajax&operation=delete&fileId="+fileId+"&uname="+uname;
				request.send(params);  
			}
			catch(e)
			{
				alert("unable to connect");
			}
		}
		else if(operation.localeCompare("rename")==0)
		{
			alert('here rename');
			try
			{
				request.open("POST","AuthHandler",true);  
				request.setRequestHeader("Content-type","application/x-www-form-urlencoded");
				var params = "caller=ajax&operation=rename&fileId="+fileId+"&uname="+uname+"&newTitle="+newTitle+"&oldTitle="+oValue;
				request.send(params);		
			}
			catch(e)
			{
				alert('unable to rename');
			}
		}
		else if(operation.localeCompare("download")==0)
		{
			
			try
			{
				document.location.href = "AuthHandler?caller=ajax&operation=download&fileId="+fileId+"&uname="+uname;
			}
			catch(e)
			{
				alert('troble downloading file');
			}
		}
	}
	function cutPaste(uname,folderSourceId,folderDestId,fileId,fileTitle)
	{
		if(window.XMLHttpRequest)
		{  
			request=new XMLHttpRequest();  
		}  
		else if(window.ActiveXObject)
		{  
			request=new ActiveXObject("Microsoft.XMLHTTP");  
		}  
		alert('here cutpaste');
		try
		{
			request.open("POST","AuthHandler",true);  
			request.setRequestHeader("Content-type","application/x-www-form-urlencoded");
			var params = "caller=cutandpaste&fileId="+fileId+"&uname="+uname+"&folderSourceId="+folderSourceId+"&folderDestId="+folderDestId+"&fileTitle="+fileTitle;
			request.send(params);		
		}
		catch(e)
		{
			alert('unable to rename');
		}
	}
	function copyPaste(uname,fileId,fileTitle,folderDestId)
	{
		if(window.XMLHttpRequest)
		{  
			request=new XMLHttpRequest();  
		}  
		else if(window.ActiveXObject)
		{  
			request=new ActiveXObject("Microsoft.XMLHTTP");  
		}  
		try
		{
			request.open("POST","AuthHandler",true);  
			request.setRequestHeader("Content-type","application/x-www-form-urlencoded");
			var params = "caller=copypaste&fileId="+fileId+"&uname="+uname+"&folderDestId="+folderDestId+"&fileTitle="+fileTitle;
			request.send(params);		
		}
		catch(e)
		{
			alert('unable to rename');
		}
	}
	function fileLoc(fileId,uname)
	{
		if(window.XMLHttpRequest)
		{  
		request=new XMLHttpRequest();  
		}  
		else if(window.ActiveXObject)
		{  
		request=new ActiveXObject("Microsoft.XMLHTTP");  
		}  
		try
		{
			request.open("POST","FileUpload",true);  
			request.setRequestHeader("Content-type","application/x-www-form-urlencoded");
			var params = "caller=fileLoc&fileId="+fileId+"&uname="+uname;
			request.send(params);  
		}
		catch(e)
		{
			alert("unable to connect");
		}
	}
</script>
    <div id="wrapper">
	<form id="UploadForm" action="FileUpload" method="post"
		enctype="multipart/form-data" >
		<input type="file" size="60" id="myfile" name="myfile" multiple> <input
			type="submit" value="Upload">
		<input type="hidden" value="" id="uname" name="uname" style={visibility:hidden}/>
		<input type="hidden" value="" id="fileId" name="fileId" style={visibility:hidden}/>
		<div id="progressbox">
			<div id="progressbar"></div>
			<div id="percent">0%</div>
		</div>
		<br />

		<div id="message"></div>
	</form>
	</div>
  </body>
</html>