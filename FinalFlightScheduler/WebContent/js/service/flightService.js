/**
 * Holds a set of global values that all controllers can use.
 */

app.service('flightService', function()
{	
	this.username = "";
	this.authorizedLogin = false;
	this.serverURL = "http://127.0.0.1:8080/FinalFlightScheduler/service/";
	this.flightGeneratorURL = "http://127.0.0.1:8080/FinalInstructorWebService/";
});