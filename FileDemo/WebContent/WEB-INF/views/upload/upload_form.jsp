<!DOCTYPE html>
<%@ taglib uri="http://mobiarch" prefix="p" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>File Upload Demo</title>
</head>
<body>
<h3>Form File Upload</h3>
<p:form action="upload/form-upload" enctype="multipart/form-data">
	<p:errors property="fileToUpload" style="color: red"/>
	<p:input type="file" name="fileToUpload"/><br/>
	<p:textarea name="notes"></p:textarea><br/>
	<input type="submit" value="Upload"/>
</p:form>

<h3>Ajax File Upload</h3>
<p>
<input type="file" id="fileInput"/>
<button onclick="startUpload();">Upload</button>
<br/>
<progress id="progressBar" max="100" value="0"/>
</p>
<script>
function startUpload() {
	var fileInput = document.getElementById("fileInput");

	if (fileInput.files.length == 0) {
		alert("Please choose a file");
		return;
	}
	
	var progressBar = document.getElementById("progressBar");
	var xhr = new XMLHttpRequest();

	xhr.upload.onprogress = function(e) {
		var percentComplete = (e.loaded / e.total) * 100;
		progressBar.value = percentComplete;
	};

	xhr.onload = function() {
		if (xhr.status == 200) {
			alert("Sucess! Upload completed");
		} else {
			alert("Error! Upload failed");
		}
	};
	xhr.onerror = function() {
		alert("Error! Upload failed. Can not connect to server.");
	};
	
	progressBar.value = 0;
	xhr.open("POST", "ajax-upload", true);
	xhr.setRequestHeader("Content-Type", fileInput.files[0].type);
	xhr.send(fileInput.files[0]);
}
</script>
</body>
</html>