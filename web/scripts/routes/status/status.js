export default /*@ngInject*/ ($scope, Vms) => {
	
	$scope.vms = {};
	Vms.getVms().success((data) => {
		$scope.vms = data;
		console.log($scope.vms);
	});
};