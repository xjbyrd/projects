/**
 * Partial controller that handles the searching of a flight path for the flightController.
 */

app.flightSearch = function ($scope, $http, flightService)
{	
	$scope.searchError = false;
	
	// Gets all the possible locations a plane can fly to populate the search fields
	$http.get($scope.flightService.serverURL + "location").then(function (response)
	{
		$scope.locations = response.data;
	});
	
	// Finds all the flight paths that have the shortest time
	$scope.findPath = function($routeProvider)
	{
		$scope.searchDay = $scope.day;
		
		// Gets selected origin and destination from html
		var selectedIndex = document.getElementById("originList").selectedIndex;
		$scope.origin = $scope.locations[selectedIndex];
		
		var selectedIndex = document.getElementById("destinationList").selectedIndex;
		$scope.destination = $scope.locations[selectedIndex];
		
		// "flights" had to be stringified in order to pass a list
		$routeProvider
		$http({
			url : $scope.flightService.serverURL + "flightPath",
			method: "GET",
			params : {"origin" : $scope.origin,"destination" : $scope.destination, "flights" : JSON.stringify($scope.departures)}
		}).then(function (response) {
			$scope.minPaths = response.data;
			
			if (!$scope.minPaths || $scope.minPaths.length < 1)
			{
				$scope.searchError = true;
			}
			else
			{
				$scope.searchError = false;
			}
		});
	}
};