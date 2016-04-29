import subModule from '../index'

import template from './vmcard.html'
import c3 from 'c3'

export default subModule.directive('vmcard', () => {

    return {
        restrict: 'E',
        replace: true,
        scope: {
            vm: "="
        },
        template,
        controller: /*@ngInject*/ ($scope, $timeout, Vms) => {
            
            $scope.showLoad = false;
            
            $scope.randId = () => {  
                var S4 = function() {
                    return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
                };
                return btoa(S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
            }
            
            $scope.safeIdCPU = $scope.randId();
            $scope.safeIdMEMORY = $scope.randId();
            $scope.safeIdDISK = $scope.randId();
            
            $scope.drawChart = (timeseries, data, safeId) => {
                c3.generate({
                bindto: '#'+safeId,
                size: {
                    height:120
                },
                data: {
                    x: 'x',
                    columns: [
                        timeseries,
                        data
                    ]
                }
            });         
            }
            
            $scope.toggleLoad = () => {
                $scope.showLoad=!$scope.showLoad;
                
                if ($scope.showLoad) {
                    $timeout( () => {
                    Vms.getLoad($scope.vm.id, 'CPU').success((data) => {
                        var timeseries = data.dates;
                        var loaddata = data.values;
                        timeseries.unshift('x')
                        loaddata.unshift('CPU load');
                        
                        $scope.drawChart(timeseries, loaddata, $scope.safeIdCPU);
                    });
                    Vms.getLoad($scope.vm.id, 'MEMORY').success((data) => {
                        var timeseries = data.dates;
                        var loaddata = data.values;
                        timeseries.unshift('x')
                        loaddata.unshift('MEMORY load');
                        
                        $scope.drawChart(timeseries, loaddata, $scope.safeIdMEMORY);
                    });
                    Vms.getLoad($scope.vm.id, 'DISK').success((data) => {
                        var timeseries = data.dates;
                        var loaddata = data.values;
                        timeseries.unshift('x')
                        loaddata.unshift('DISK load');
                        
                        $scope.drawChart(timeseries, loaddata, $scope.safeIdDISK);
                    }); 
                    }, 500);
                }
            }        
        }
    }
});