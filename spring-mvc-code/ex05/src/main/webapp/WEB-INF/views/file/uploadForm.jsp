<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>File Upload Form</title>
</head>
<body>
	<!-- 
		UploadForm 
		- enctype 속성값 : multipart/form-data
	-->
	<form action="uploadFormAction" method="post" enctype="multipart/form-data">
		<input type='file' name='uploadFile' multiple>
		<button>전송</button>
	</form>
</body>
</html>
