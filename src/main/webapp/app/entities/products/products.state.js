(function() {
    'use strict';

    angular
        .module('shopApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('products', {
            parent: 'entity',
            url: '/products',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Products'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/products/products.html',
                    controller: 'ProductsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('products-detail', {
            parent: 'entity',
            url: '/products/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Products'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/products/products-detail.html',
                    controller: 'ProductsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Products', function($stateParams, Products) {
                    return Products.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'products',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('products-detail.edit', {
            parent: 'products-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/products/products-dialog.html',
                    controller: 'ProductsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Products', function(Products) {
                            return Products.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('products.new', {
            parent: 'products',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/products/products-dialog.html',
                    controller: 'ProductsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                mainImage: null,
                                mainImageContentType: null,
                                image2: null,
                                image2ContentType: null,
                                image3: null,
                                image3ContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('products', null, { reload: 'products' });
                }, function() {
                    $state.go('products');
                });
            }]
        })
        .state('products.edit', {
            parent: 'products',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/products/products-dialog.html',
                    controller: 'ProductsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Products', function(Products) {
                            return Products.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('products', null, { reload: 'products' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('products.delete', {
            parent: 'products',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/products/products-delete-dialog.html',
                    controller: 'ProductsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Products', function(Products) {
                            return Products.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('products', null, { reload: 'products' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
