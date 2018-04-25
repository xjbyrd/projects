/**
 * Handles the operations for the flight view. Verifies customer has a valid login.
 * Shows and hides different partial web pages based on customer interaction.
 * Gets flights from FinalInstructorWebService.
 */

app.controller("flightController", function ($scope, $http, flightService) 
{	
	// Scoped Variables
	$scope.arrivals = [];
	$scope.departures = [];
	$scope.locations = [];
	$scope.day = 0;
	$scope.tickets = [];
	$scope.searchDay;
	$scope.bank = 0;
	$scope.alerts;
	$scope.showDepartures;
	$scope.showArrivals;
	$scope.showSearch;
	
	// Sets initial values. Toggles partial view and sets color of active link
	document.getElementById("heading").innerHTML = "Departures";
	$scope.showDepartures = true;
	$scope.showArrivals = false;
	$scope.showSearch = false;
	document.getElementById("departuresLink").style.color = "darkblue";
    document.getElementById("arrivalsLink").style.color = "#8C4646";
    document.getElementById("searchLink").style.color = "#8C4646";
	
	// Sets up service to be used
	$scope.flightService = flightService;
	
	// Pulls in other partial controllers
	app.flightSearch($scope, $http, flightService);
	app.flightTickets($scope, $http, flightService); 
	
	$scope.checkAuthorization = function()
	{
		if (!$scope.flightService.authorizedLogin)
			window.location = "#/"
	}
	
	$scope.checkAuthorization();
	
	$scope.selectDepartures = function()
	{
		// Toggles partial view and sets color of active link
		document.getElementById("heading").innerHTML = "Departures";
		$scope.showDepartures = true;
		$scope.showArrivals = false;
		$scope.showSearch = false;
		document.getElementById("departuresLink").style.color = "darkblue";
	    document.getElementById("arrivalsLink").style.color = "#8C4646";
	    document.getElementById("searchLink").style.color = "#8C4646";
	};
	
	$scope.selectArrivals = function()
	{
		// Toggles partial view and sets color of active link
		document.getElementById("heading").innerHTML = "Arrivals";
		$scope.showDepartures = false;
		$scope.showArrivals = true;
		$scope.showSearch = false;
		document.getElementById("departuresLink").style.color = "#8C4646";
	    document.getElementById("arrivalsLink").style.color = "darkblue";
	    document.getElementById("searchLink").style.color = "#8C4646";
	};
	
	$scope.selectSearch = function()
	{
		// Toggles partial view and sets color of active link
		document.getElementById("heading").innerHTML = "Search";
		$scope.showDepartures = false;
		$scope.showArrivals = false;
		$scope.showSearch = true;
		document.getElementById("departuresLink").style.color = "#8C4646";
	    document.getElementById("arrivalsLink").style.color = "#8C4646";
	    document.getElementById("searchLink").style.color = "darkblue";
	};
    
	
	// Gets flights from FinalInstructorWebService.
	$scope.getFlights = function()
	{
		$http.get($scope.flightService.flightGeneratorURL + "getFlightModel").then(function (response)
		{
			flights = response.data.flights;
			$scope.arrivals.length = 0;
			$scope.departures.length = 0;
			
			// Sorts flights in ascending order
			flights.sort(function(a, b) {
			    return parseFloat(a.flightId) - parseFloat(b.flightId);
			});
			
			// Divides up flights based on if they are currently in the air or not
			flights.forEach(function(flight)
			{
				if(flight.departure < 0)
				{
					$scope.arrivals.push(flight);
				}
				else
				{
					$scope.departures.push(flight);
				}
			});
			
			$scope.day = response.data.currentDay;
		});
	}
	
	// Loops all functions that need to poll the server
	function flightLoop()
	{
        setTimeout(function ()
        {
        	$scope.getFlights();
        	$scope.getFlightAlerts();
        	$scope.clearTickets();
            flightLoop();
        }, 2000);
    }
	
    flightLoop();
    
});