export default /*@ngInject*/ ($scope, Vms) => {

	$scope.vms = {};
	
	$scope.listVms = () => {
		console.log("listVMs called");
		Vms.getvms().success((data) => {
			console.log("success");
			console.log(data);
		}).error((data, status) => {
			console.log(data);
			console.log(status);
		});
	}
	
	$scope.createVmUbuntu = () => {
		console.log("createVmubuntu called");
        var ostype = "UBUNTU_64";
		Vms.createvm(ostype).success((data) => {
			console.log("success");
			console.log(data);
		}).error((data, status) => {
			console.log(data);
			console.log(status);
		});
	}
    
    $scope.createVmfreebsd = () => {
		console.log("createVmfreebsd called");
        var ostype = "FREEBSD_64";
		Vms.createvm(ostype).success((data) => {
			console.log("success");
			console.log(data);
		}).error((data, status) => {
			console.log(data);
			console.log(status);
		});
	}
};