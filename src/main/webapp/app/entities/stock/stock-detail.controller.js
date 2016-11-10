(function() {
    'use strict';

    angular
        .module('shopApp')
        .controller('StockDetailController', StockDetailController);

    StockDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Stock', 'Products'];

    function StockDetailController($scope, $rootScope, $stateParams, previousState, entity, Stock, Products) {
        var vm = this;

        vm.stock = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('shopApp:stockUpdate', function(event, result) {
            vm.stock = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
