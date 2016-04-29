export default /*@ngInject*/ ($scope, ProjectGroups, VmSettings, Notification) => {
	
	$scope.vmset = {};
	$scope.projectgroup = {
		projects: []
	};
	
	VmSettings.getVmSettings().success((data) => {
		$scope.vmset = data;
	});
	
	$scope.addnew = () => {
		
		var submit = angular.element(document.getElementById("submit-addnew"));
		submit.addClass("glyphicon-refresh glyphicon-refresh-animate");
		
		ProjectGroups.addnew($scope.projectgroup).success((data) => {
			submit.removeClass("glyphicon-refresh glyphicon-refresh-animate");
			Notification.success("Project added successfully.");				
		}).error((data, status) => {
			submit.removeClass("glyphicon-refresh glyphicon-refresh-animate");
			Notification.error("Error adding the project: "+data.description);
		});
	}
};