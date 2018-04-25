/**
 * Partial controller for the flightController that handles the purchasing and retention of tickets.
 */

app.flightTickets = function ($scope, $http, flightService)
{	
	// On customer interaction, records tickets purchased
	$scope.buyTickets = function(path)
	{
		var ticketsProxy = [];
		var test = true;
		var duplicates = [];
		
		// Checks each flight in the current path against already purchased
		// tickets to check for flights during duplicate time periods
		path.forEach(function(flight)
		{
			$scope.tickets.forEach(function(ticket)
			{
				// Combined ETA and departure times
				var flightETA = flight.eta + flight.departure + $scope.searchDay;
				var flightDeparture = flight.departure + $scope.searchDay;
				
				// Checks current flight times against current ticket times
 				if ((flightDeparture >= ticket.departure && flightDeparture < ticket.eta) ||
					(flightETA > ticket.departure && flightETA <= ticket.eta))
				{
 					// Duplicates are found, so it flags this batch of
 					// flights as bad and records duplicated flight
					test = false;
					duplicates.push(flight.flightId);
				}
			});
			
			// Creates ticket object with custom values
			var ticket = {flight: flight, departure: (flight.departure + $scope.searchDay), eta: (flight.eta + flight.departure + $scope.searchDay), status: "on time"};
			
			// Records the flights
			ticketsProxy.push(ticket);
		});
		
		// Checks if flag was tripped indicating duplicate values
		if(test)
		{
			$scope.tickets.push.apply($scope.tickets, ticketsProxy);
			$scope.bank += 20 * ticketsProxy.length;
		}
		else
		{
			alert("Error: You have already purchased tickets during these tickets flight times: " + duplicates);
		}
	}
	
	// Gets alerts for arrivals and delay of flights from server
	$scope.getFlightAlerts = function()
	{
		$http.get($scope.flightService.serverURL + "alerts").then(function (response)
		{
			var flightAlerts = response.data;
			$scope.checkTickets(flightAlerts);
		});
	}
	
	// Checks tickets to see if any of them have been delayed or arrived
	$scope.checkTickets = function(flightAlerts)
	{
		var delayedFlights = [];
		
		// Iterates through each ticket and each alert to see if any match
		$scope.tickets.forEach(function(ticket)
		{
			
			
			flightAlerts.forEach(function(flightAlert)
			{
				console.log("Comparing" + ticket.flight.flightId + " and " + flightAlert.flight.flightId);
				if (ticket.flight.flightId == flightAlert.flight.flightId)
				{
					console.log("A Match Was Found!!!")
					// A match was found. Checks if the alert was for arrived or delayed
					if (flightAlert.delayed)
					{
						if (!(ticket.status == "delayed"))
							delayedFlights.push(ticket.flight.flightId);
						
						ticket.status = "delayed";
					}
					else
					{
						ticket.status = "arrived";
					}
				}
			});
		});
		
		// If any tickets were delayed, alerts customer 
		if(delayedFlights.length > 0)
		{
			alert("Your flights " + delayedFlights + " have been delayed.\nYour ticket payments have been refunded.");
			$scope.bank -= 20 * delayedFlights.size;
		}
	}
	
	// Removes tickets a few days after the flight has landed
	$scope.clearTickets = function()
	{
		$scope.tickets.forEach(function(ticket)
		{
			if (ticket.eta < $scope.day - 3)
			{
				$scope.tickets.splice($scope.tickets.indexOf(ticket), 1);
			}
		});
	}
	
}