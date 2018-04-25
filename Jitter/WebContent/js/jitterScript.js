/**
 * 
 */

jitterGLOBAL = {};

$(document).ready(function()
{
	var loc = 'http://127.0.0.1:8080/Jitter/service/';
	var loc2 = 'http://192.168.1.13:8080/Jitter/service/';
	
	jitterGLOBAL.ajaxGetGlobalJitters = function()
	{
		var resp = $.ajax({
			url : loc + "jitter",
			method : "GET"

		}).done(function(data)
		{
			document.getElementById("my-text-area").innerHTML = "";
				
			$.each(data.jpl, function(index, element)
			{
				var item = ["||<[" + element.name + "]>|| : " + element.jitter + "\n"];
				$("#my-text-area").append(item);
			})
		});
		
	}
	
	jitterGLOBAL.ajaxPostJitter = function() {
		var resp = $.ajax({
			headers: { 
		        'Accept': 'application/json',
		        'Content-Type': 'application/json' 
		    },
			url : loc + 'jitter/',
			data : JSON.stringify({ "jitter" : $('#newjitter').val() }),
			method : "POST"

		}).done(function(data)
		{
			if (data == 0)
			{
				jitterGLOBAL.ajaxGetGlobalJitters();
				document.getElementById("newjitter").value = "";
			}
		});
		
		
	}
	
	jitterGLOBAL.ajaxPostFriendJitter = function() {
		var resp = $.ajax({
			headers: { 
		        'Accept': 'application/json',
		        'Content-Type': 'application/json' 
		    },
			url : loc + 'jitter/',
			data : JSON.stringify({ "jitter" : $('#newjitter2').val() }),
			method : "POST"

		}).done(function(data)
		{
			if (data == 0)
			{
				jitterGLOBAL.ajaxGetGlobalJitters();
				document.getElementById("newjitter2").value = "";
			}
		});
		
		
	}
	
	jitterGLOBAL.ajaxGetFriendsJitters = function()
	{
		var resp = $.ajax({
			url : loc + "jitter/",
			method : "GET"

		}).done(function(data)
		{
			document.getElementById("my-text-area2").innerHTML = "";
				
			$.each(data.jpl, function(index, element)
			{
				var item = ["||<[" + element.name + "]>|| : " + element.jitter + "\n"];
				$("#my-text-area2").append(item);
			})
		});
		
	}
	
	jitterGLOBAL.ajaxSearchByID = function()
	{
		document.getElementById("searchedID").innerHTML = "";
		var resp = $.ajax({
			headers: 
			{
				'Accept': 'application/json',
				'Content-Type' : 'application/json'
			},
			url : loc + "jitter/search",
			data : {
				'id' : $('#searchID').val()
			},
			method : "GET"

		}).done(function(data)
		{				
			document.getElementById("searchedID").innerHTML = "<span id=\"" + data.id + "\">" + data.username + "<span>";
		});
		
	}
	
	jitterGLOBAL.ajaxRequestFriend = function() {
		var resp = $.ajax({
			headers: { 
		        'Accept': 'application/json',
		        'Content-Type': 'application/json' 
		    },
			url : loc + 'jitter/request',
			data : JSON.stringify({ "id" :document.getElementById("searchedID").firstElementChild.id }),
			method : "POST"

		}).done(function(data)
		{
			alert("Request Sent!");
		});
		
		
	}
	
	jitterGLOBAL.ajaxGetAllFriendRequests = function()
	{
		var resp = $.ajax({

			url : loc + "jitter/friend",
			method : "GET"

		}).done(function(data)
		{
			document.getElementById("my-list").innerHTML = "";
			document.getElementById("my-list").innerHTML = data;
		});
		
	}
	
	jitterGLOBAL.ajaxAcceptFriendRequest = function() {
		var resp = $.ajax({
			headers: { 
		        'Accept': 'application/json',
		        'Content-Type': 'application/json' 
		    },
			url : loc + 'jitter/accept',
			data : JSON.stringify({ "id" : $("#my-list").children(":selected").attr("id") }),
			method : "PUT"

		}).done(function()
		{
			jitterGLOBAL.ajaxGetAllFriendRequests();
		});
		
		
	}
	
	jitterGLOBAL.ajaxGetMyJitters = function()
	{
		var resp = $.ajax({
			url : loc + "jitter/history",
			method : "GET"

		}).done(function(data)
		{
			document.getElementById("my-text-area3").innerHTML = "";
				
			$.each(data.jpl, function(index, element)
			{
				var item = ["||<[" + element.name + "]>|| : " + element.jitter + "\n"];
				$("#my-text-area3").append(item);
			})
		});
		
	}
	
	$("#btn1").click(function(){
        $("#item1").collapse('show');
        $("#item2").collapse('hide');
        $("#item3").collapse('hide');
        $("#item4").collapse('hide');
        document.getElementById("btn1").style.backgroundColor = "tan";
        document.getElementById("btn2").style.backgroundColor = "saddlebrown";
        document.getElementById("btn3").style.backgroundColor = "saddlebrown";
        document.getElementById("btn4").style.backgroundColor = "saddlebrown";
        jitterGLOBAL.ajaxGetGlobalJitters();
    });
    $("#btn2").click(function(){
    	$("#item1").collapse('hide');
        $("#item2").collapse('show');
        $("#item3").collapse('hide');
        $("#item4").collapse('hide');
        document.getElementById("btn2").style.backgroundColor = "tan";
        document.getElementById("btn1").style.backgroundColor = "saddlebrown";
        document.getElementById("btn3").style.backgroundColor = "saddlebrown";
        document.getElementById("btn4").style.backgroundColor = "saddlebrown";
        jitterGLOBAL.ajaxGetAllFriendRequests();
        jitterGLOBAL.ajaxGetFriendsJitters();
    });
    $("#btn3").click(function(){
    	$("#item1").collapse('hide');
        $("#item2").collapse('hide');
        $("#item3").collapse('show');
        $("#item4").collapse('hide');
        document.getElementById("btn3").style.backgroundColor = "tan";
        document.getElementById("btn2").style.backgroundColor = "saddlebrown";
        document.getElementById("btn1").style.backgroundColor = "saddlebrown";
        document.getElementById("btn4").style.backgroundColor = "saddlebrown";
        jitterGLOBAL.ajaxGetMyJitters();
    });
    $("#btn4").click(function(){
    	$("#item1").collapse('hide');
        $("#item2").collapse('hide');
        $("#item3").collapse('hide');
        $("#item4").collapse('show');
        document.getElementById("btn4").style.backgroundColor = "tan";
        document.getElementById("btn2").style.backgroundColor = "saddlebrown";
        document.getElementById("btn3").style.backgroundColor = "saddlebrown";
        document.getElementById("btn1").style.backgroundColor = "saddlebrown";
    });
    
    function timeout() {
        setTimeout(function () {
        	jitterGLOBAL.ajaxGetFriendsJitters();
            jitterGLOBAL.ajaxGetGlobalJitters();
            timeout();
        }, 5000);
    }
    
    //timeout();
    
    
	
});