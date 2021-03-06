/**
 * Handles all operations for the login html.  Verifies customer login
 * information and either registers, log in, or notifies customer of error.
 */

app.controller("loginController", function ($scope, $http, flightService) 
{	
	$scope.registerError = false;
	$scope.loginError = false;
	$scope.flightService = flightService;
	
	// Sends GET to register username/password with database
	$scope.register = function ($routeProvider)
	{
		// Encrypts password
		if ($scope.newPassword)
			var encryptedPassword = $scope.encrypt($scope.newPassword);
		
		$routeProvider
		$http({
			url : $scope.flightService.serverURL + "register",
			method: "GET",
			params : {"newUsername" : $scope.newUsername,"newPassword" : encryptedPassword}
		}).then(function (response) {
			if (response.data == 1)
			{	
				$scope.flightService.authorizedLogin = true;
				flightService.username = $scope.newUsername;
				$scope.registerError = false;
				window.location = "#/flight";
			}
			else
			{
				// Error text becomes visible because username/password wasn't found
				$scope.registerError = true;
				$scope.loginError = false;
			}
		});
	};
	
	// Sends GET to check username/password against database
	$scope.login = function ($routeProvider)
	{
		// Encrypt password
		if ($scope.password)
			var encryptedPassword = $scope.encrypt($scope.password);
		
		$routeProvider
		$http({
			url : $scope.flightService.serverURL + "login",
			method: "GET",
			params : {"username" : $scope.username,"password" : encryptedPassword}
		}).then(function (response) {
			if (response.data == 1)
			{
				flightService.username = $scope.username;
				$scope.flightService.authorizedLogin = true;
				$scope.loginError = false;
				window.location = "#/flight";
			}
			else
			{
				// Error text becomes visible because username/password wasn't found
				$scope.loginError = true;
				$scope.registerError = false;
			}
		});
	};
	
	// Encryption function for password. Adds and changes characters in the
	// password in a obscure manner in order to  make password safe to be
	// transmitted to server and saved in database
	$scope.encrypt = function sha1(msg)
	{
	  function rotl(n,s) { return n<<s|n>>>32-s; }; function fcc(x){return x};
	  function tohex(i) { for(var h="", s=28;;s-=4) { h+=(i>>>s&0xf).toString(16); if(!s) return h; } };
	  var H0=0x67452301, H1=0xEFCDAB89, H2=0x98BADCFE, H3=0x10325476, H4=0xC3D2E1F0, M=0x0ffffffff; 
	  var i, t, W=new Array(80), ml=msg.length, wa=new Array();
	  msg += fcc(0x80);
	  while(msg.length%4) msg+=fcc(0);
	  for(i=0;i<msg.length;i+=4) wa.push(msg<<24|msg<<16|msg<<8|msg);
	  while(wa.length%16!=14) wa.push(0);
	  wa.push(ml>>>29),wa.push((ml<<3)&M);
	  for( var bo=0;bo<wa.length;bo+=16 ) {
	    for(i=0;i<16;i++) W[i]=wa[bo+i];
	    for(i=16;i<=79;i++) W[i]=rotl(W[i-3]^W[i-8]^W[i-14]^W[i-16],1);
	    var A=H0, B=H1, C=H2, D=H3, E=H4;
	    for(i=0 ;i<=19;i++) t=(rotl(A,5)+(B&C|~B&D)+E+W[i]+0x5A827999)&M, E=D, D=C, C=rotl(B,30), B=A, A=t;
	    for(i=20;i<=39;i++) t=(rotl(A,5)+(B^C^D)+E+W[i]+0x6ED9EBA1)&M, E=D, D=C, C=rotl(B,30), B=A, A=t;
	    for(i=40;i<=59;i++) t=(rotl(A,5)+(B&C|B&D|C&D)+E+W[i]+0x8F1BBCDC)&M, E=D, D=C, C=rotl(B,30), B=A, A=t;
	    for(i=60;i<=79;i++) t=(rotl(A,5)+(B^C^D)+E+W[i]+0xCA62C1D6)&M, E=D, D=C, C=rotl(B,30), B=A, A=t;
	    H0=H0+A&M;H1=H1+B&M;H2=H2+C&M;H3=H3+D&M;H4=H4+E&M;
	  }
	  return tohex(H0)+tohex(H1)+tohex(H2)+tohex(H3)+tohex(H4);
	}
	
});