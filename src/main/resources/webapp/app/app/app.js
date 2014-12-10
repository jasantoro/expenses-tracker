angular.module('app', [
  'ngRoute',
  'resources.expenses',
  'resources.userRegistration',
  'users-edit-validateEquals',
  'controllers.expenses',
  'services.breadcrumbs',
  'services.i18nNotifications',
  'services.httpRequestTracker',
  'security',
  'CustomFilter']);

//TODO: move those messages to a separate module
angular.module('app').constant('I18N.MESSAGES', {
  'errors.route.changeError':'Route change error',
  'login.reason.notAuthorized':"You do not have the necessary access permissions.  Do you want to login as someone else?",
  'login.reason.notAuthenticated':"You must be logged in to access this part of the application.",
  'login.error.invalidCredentials': "Login failed.  Please check your credentials and try again.",
  'login.error.serverError': "There was a problem with authenticating: {{exception}}."
});

angular.module('app').config(['$routeProvider', '$locationProvider', '$httpProvider', 'securityAuthorizationProvider', 
function ($routeProvider, $locationProvider, $httpProvider, securityAuthorizationProvider) {
	$routeProvider.otherwise({redirectTo:'/home'});

	$routeProvider.when('/home', {templateUrl: 'partials/home.html'});
	
	$routeProvider.when('/expenses', {
		templateUrl: 'partials/expenses.html', 
		controller: 'ExpensesListCtrl',
		resolve:{
			authenticatedUser: securityAuthorizationProvider.requireAuthenticatedUser
    	}		
	});
	$routeProvider.when('/expenses-by-week', {
		templateUrl: 'partials/expenses-by-week.html', 
		controller: 'ExpensesByWeekCtrl',
		resolve:{
			authenticatedUser: securityAuthorizationProvider.requireAuthenticatedUser
    	}		
	});
	$routeProvider.when('/expense-detail/:id', {
		templateUrl: 'partials/expense-detail.html', 
		controller: 'ExpenseDetailCtrl',
		resolve:{
			authenticatedUser: securityAuthorizationProvider.requireAuthenticatedUser
    	}		
	});
	$routeProvider.when('/new-expense', {
		templateUrl: 'partials/new-expense.html', 
		controller: 'ExpenseCreationCtrl',
		resolve:{
			authenticatedUser: securityAuthorizationProvider.requireAuthenticatedUser
    	}		
	});
	
	$routeProvider.when('/signup', {
		templateUrl: 'partials/signup.html', 
		controller: 'HeaderCtrl'
	});
	
	 // delete header from client:
	// http://stackoverflow.com/questions/17289195/angularjs-post-data-to-external-rest-api
	$httpProvider.defaults.useXDomain = true;
	delete $httpProvider.defaults.headers.common['X-Requested-With'];
}]);

angular.module('app').run(['security', function(security) {
  // Get the current user when the application starts
  // (in case they are still logged in from a previous session)
  security.requestCurrentUser();
}]);

angular.module('app').controller('AppCtrl', ['$scope', 'i18nNotifications', 'localizedMessages', function($scope, i18nNotifications, localizedMessages) {

  $scope.notifications = i18nNotifications;

  $scope.removeNotification = function (notification) {
    i18nNotifications.remove(notification);
  };

  $scope.$on('$routeChangeError', function(event, current, previous, rejection){
    i18nNotifications.pushForCurrentRoute('errors.route.changeError', 'error', {}, {rejection: rejection});
  });
}]);

angular.module('app').controller('HeaderCtrl', ['$scope', '$location', '$route', 'security', 'breadcrumbs', 'notifications', 'httpRequestTracker', 'UserRegistration',
  function ($scope, $location, $route, security, breadcrumbs, notifications, httpRequestTracker, UserRegistration) {
  	
  	$scope.location = $location;
  	$scope.breadcrumbs = breadcrumbs;

  	$scope.isAuthenticated = security.isAuthenticated;
  	$scope.isAdmin = security.isAdmin;

  	$scope.home = function () {
    	if (security.isAuthenticated()) {
      		$location.path('/expenses');
    	} else {
      		$location.path('/home');
    	}
  	};

  	$scope.isNavbarActive = function (navBarPath) {
    	return navBarPath === breadcrumbs.getFirst().name;
  	};

  	$scope.hasPendingRequests = function () {
    	return httpRequestTracker.hasPendingRequests();
  	};

	$scope.signup = function () {
	    UserRegistration.save($scope.user, function(data) {
			$scope.home();
	    });
	};
	
	// callback for ng-click 'cancel':
	$scope.cancel = function () {
		$scope.home();
	};

}]);
