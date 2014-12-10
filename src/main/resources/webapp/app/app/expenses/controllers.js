'use strict';

/* Controllers */

var app = angular.module('controllers.expenses', ['ui.bootstrap', 'ngResource']);

app.controller('ExpensesListCtrl', ['$scope', 'Expenses', '$location',
    function ($scope, Expenses, $location) {
        // callback for ng-click 'editExpense':
        $scope.editExpense = function (expense) {
            $location.path('/expense-detail/' + expense.id);
        };
        // callback for ng-click 'deleteExpense':
        $scope.deleteExpense = function (expense) {
            Expenses.remove({ id: expense.id }, function(data) {
            	if(data.deleted) {
		        	$scope.expenses.splice($scope.expenses.indexOf(expense),1);
					$scope.totalItems = $scope.expenses.length;
		        }
            });
        };
        // callback for ng-click 'createNewExpense':
        $scope.createNewExpense = function () {
            $location.path('/new-expense');
        };
        
        $scope.expenses = Expenses.query(function(data) {
			$scope.totalItems = $scope.expenses.length;
			$scope.currentPage = 1;
			$scope.numPerPage = 10;
        });

		$scope.paginate = function(value) {
			var begin, end, index;
			begin = ($scope.currentPage - 1) * $scope.numPerPage;
			end = begin + $scope.numPerPage;
			index = $scope.expenses.indexOf(value);
			return (begin <= index && index < end);
  		};
  		
  		$scope.predicate = '-date';
    }]);

app.controller('ExpensesByWeekCtrl', ['$scope', 'Expenses',
    function ($scope, Expenses) {
        $scope.expensesAggregator = Expenses.queryByWeek();
		$scope.currentPage = 0;
		$scope.prevPage = function() {
			$scope.currentPage++;
		};
		$scope.nextPageDisabled = function() {
			return $scope.currentPage == 0;
		};
		$scope.nextPage = function() {
			if ($scope.currentPage > 0) {
				$scope.currentPage--;
			}
		};
		$scope.$watch("currentPage", function(newValue, oldValue) {
			$scope.expensesAggregator = Expenses.queryByWeek({week:$scope.currentPage});
		});        
    }]);

app.controller('ExpenseDetailCtrl', ['$scope', '$routeParams', 'Expenses', '$location',
    function ($scope, $routeParams, Expenses, $location) {
        // callback for ng-click 'updateExpense':
        $scope.updateExpense = function () {
            Expenses.update($scope.expense, function(data) {
	            $location.path('/expenses');
            });
        };
        
        // callback for ng-click 'cancel':
        $scope.cancel = function () {
            $location.path('/expenses');
        };
        
        $scope.expense = Expenses.get({id: $routeParams.id});
    }]);

app.controller('ExpenseCreationCtrl', ['$scope', 'Expenses', '$location',
    function ($scope, Expenses, $location) {
        // callback for ng-click 'createNewExpense':
        $scope.createNewExpense = function () {
            Expenses.save($scope.expense, function(data) {
	            $location.path('/expenses');
            });
        }
        
        // callback for ng-click 'cancel':
        $scope.cancel = function () {
            $location.path('/expenses');
        };
    }]);
