export default /*@ngInject*/ ($scope, ProjectGroups, Notification) => {
	
	$scope.myprojects = [];
	$scope.selectedProjectGroup = null;
	
	ProjectGroups.get().success((data) => {
		$scope.myprojects = data;
	});
	
	$scope.updateProject = () => {
		ProjectGroups.update($scope.selectedProjectGroup).success((data) => {
			Notification.success("Project updated successfully.");				
		}).error((data, status) => {
			Notification.error("Error updating the project: "+data.description);
		});
	}
};