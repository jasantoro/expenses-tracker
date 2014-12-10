angular.module('resources.userRegistration', ['ngResource']);
angular.module('resources.userRegistration').factory('UserRegistration', function ($resource) {
	return $resource('/expenses-tracker/signup', {}, {
		signup: { 
			method: 'POST' 
    	}
   	})
});