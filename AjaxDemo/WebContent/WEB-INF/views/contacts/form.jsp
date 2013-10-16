<!DOCTYPE html>
<%@taglib prefix="p" uri="http://mobiarch"%>

<html>
<head>
<style type="text/css">
.invalid {
	background: pink;
}
</style>
<script src="/AjaxDemo/script/jquery.min.js"></script>
<script>
$(function() {
	$("form button").click(ajaxSubmitForm);
	showContactList();
});
/**
 * Example form submission with validation using Ajax.
 */
function ajaxSubmitForm() {
	//Clear invalid class
	$("form input").removeClass("invalid");
	//Clear error message
	$("form span").html("");
	
	var form = $("form");
	
	$.ajax({
		url: form.attr("action"),
		type: "POST",
		data: form.serialize(),
		dataType: "json",
		error: function (xhr, e1, e2) {console.log(e1); console.log(e2); alert("Failed");},
		success: function (response) {
			console.log(response);
			if (response.status == "SUCCESS") {
				showContactList();
				//alert("Form submitted successfully");
			} else {
				for (var i = 0; i < response.violations.length; ++i) {
					var id = response.violations[i].propertyName;
					//Set class for invalid input element
					$("#" + id).addClass("invalid");
					//Show error message
					$("#" + id + "-message").html(response.violations[i].message);
				}
				alert("Please fix input errors");
			}
		}
	});

	return false;
}
/**
 * This function shows how Pink can be used to create a simple RESTful service.
 */
function showContactList() {
	$.ajax({
		url: "/AjaxDemo/contacts/list",
		type: "GET",
		dataType: "json",
		error: function (xhr, e1, e2) {console.log(e1); console.log(e2); alert("Failed to get contact list");},
		success: function (response) {
			console.log(response);
			var div = $("#contact_list");

			div.html("");
			for (var i = 0; i < response.length; ++i) {
				console.log("Adding: " + response[i].name);
				var p = $("<p>" + response[i].name + " (" + response[i].email + ")</p>");
				div.append(p);
			}
		}
	});
}
</script>
</head>
<body>
<h3>Contacts Addressbook</h3>
<p:form action="contacts">
Name:<br/>
<p:input name="contact.name" id="name"/><span id="name-message"></span><br/>
Email:<br/>
<p:input name="contact.email" id="email"/><span id="email-message"></span><br/>

<button>Add Contact</button>
</p:form>

<div id="contact_list"></div>
</body>
</html>