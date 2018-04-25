/**
 * 
 */

jittererGLOBAL = {};

$(document).ready(function()
{
	var loc = 'http://127.0.0.1:8080/Jitter/service/';
	var loc2 = 'http://192.168.1.13:8080/Jitter/service/';
	
	jittererGLOBAL.ajaxRegister = function()
	{
		$.ajax(
				{
					headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
					url : loc + 'login/',
					data : JSON.stringify({"newusername" : $('#newusername').val(),"newpassword" : $('#newpassword').val()}),
					method : "POST"

				}).done(function(data)
				{
					if (data == 0)
						alert('Jitterer Creation Successful!');
					else
						alert('Jitterer Creation Failed!');
				});
	}

	jittererGLOBAL.ajaxLogin = function()
	{
		var resp = $.ajax({
			headers: 
			{
				'Accept': 'application/json',
				'Content-Type' : 'application/json'
			},
			url : loc + "login",
			data : {
				'username' : $('#username').val(),
				'password' : $('#password').val()
			},
			method : "GET"

		}).done(function(data)
		{				
			if (data != "")
			{
				window.open("http://127.0.0.1:8080/Jitter/JitterFeed.html", "_self");
			}
			else
			{
				alert('Login Failed');
			}
		});
		
	}
	
	jitterGLOBAL.ajaxGetGlobalJitters();
})