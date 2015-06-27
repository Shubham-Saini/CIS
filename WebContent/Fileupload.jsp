<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8/jquery.js" ></script>
<script src="http://malsup.github.com/jquery.form.js" ></script>
<script src="js/fileUploadScript.js" ></script>
<link href="css/style.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<h3>Ajax File Upload with Progress Bar</h3>
	<div id="wrapper">
	<form id="UploadForm" action="FileUpload" method="post"
		enctype="multipart/form-data">
		<input type="file" size="60" id="myfile" name="myfile"> <input
			type="submit" value="Ajax File Upload">

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