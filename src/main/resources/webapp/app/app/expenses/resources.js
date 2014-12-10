angular.module('resources.expenses', ['ngResource']);
angular.module('resources.expenses').factory('Expenses', function ($resource) {
	return $resource('/expenses-tracker/expenses/:id', {}, {
		queryByWeek: { 
			method: 'GET',
			isArray: false,
			params: {
    	   		group: 'week'
        	}
		},
		update: { 
			method: 'PUT', 
			params: {
        		id: '@id'
	       	}
    	}
   	})
});