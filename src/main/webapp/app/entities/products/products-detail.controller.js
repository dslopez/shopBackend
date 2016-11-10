(function() {
    'use strict';

    angular
        .module('shopApp')
        .controller('ProductsDetailController', ProductsDetailController);

    ProductsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Products', 'Stock'];

    function ProductsDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Products, Stock) {
        var vm = this;

        vm.products = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('shopApp:productsUpdate', function(event, result) {
            vm.products = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
