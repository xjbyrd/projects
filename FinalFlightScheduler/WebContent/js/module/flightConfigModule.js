/**
 * Handles the redirection of views when the appropriate call is used.
 */

app.config(function($routeProvider)
{
	$routeProvider
	
	.when('/', {
		templateUrl : 'login.html',
		controller  : 'loginController'
	})
	
	.when('/flight', {
		templateUrl : 'flight.html',
		controller  : 'flightController'
	})
	
});