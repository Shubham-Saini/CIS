<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8/jquery.js" ></script>
<script language="javascript">
$("#multiform").submit(function(e)
		{
		 
		    var formObj = $(this);
		    var formURL = formObj.attr("action");
		    var formData = new FormData(this);
		    $.ajax({
		        url: formURL,
		    type: 'POST',
		        data:  formData,
		    mimeType:"multipart/form-data",
		    contentType: false,
		        cache: false,
		        processData:false,
		    success: function(data, textStatus, jqXHR)
		    {
		 
		    },
		     error: function(jqXHR, textStatus, errorThrown)
		     {
		     }         
		    });
		    e.preventDefault(); //Prevent Default action.
		    e.unbind();
		});
		$("#multiform").submit(); //Submit the form
</script>
</head>
<body>
	<form name="multiform" id="multiform" action="FileUpload" method="POST" enctype="multipart/form-data">
    	Name: <input type="text" name="name" id="name" value="Ravi"/> <br/>
    	Age :<input type="text" name="age"  value="1" /> <br/>
    	Image :<input type="file" name="photo" /><br/>
    	<input type="submit" value="submit"/>
</form>
</body>
</html>