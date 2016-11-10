(function() {
    'use strict';

    angular
        .module('shopApp')
        .controller('ProductsDialogController', ProductsDialogController);

    ProductsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Products', 'Stock'];

    function ProductsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Products, Stock) {
        var vm = this;

        vm.products = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.stocks = Stock.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.products.id !== null) {
                Products.update(vm.products, onSaveSuccess, onSaveError);
            } else {
                Products.save(vm.products, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('shopApp:productsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setMainImage = function ($file, products) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        products.mainImage = base64Data;
                        products.mainImageContentType = $file.type;
                    });
                });
            }
        };

        vm.setImage2 = function ($file, products) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        products.image2 = base64Data;
                        products.image2ContentType = $file.type;
                    });
                });
            }
        };

        vm.setImage3 = function ($file, products) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        products.image3 = base64Data;
                        products.image3ContentType = $file.type;
                    });
                });
            }
        };

    }
})();
