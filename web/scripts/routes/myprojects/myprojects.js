export default /*@ngInject*/ ($scope, $state, $location, ProjectGroups, Notification) => {
	
	$scope.myprojects = [];
	$scope.selectedProjectGroup = null;
	$scope.pauseRefresh = false;
	
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
	
	$scope.buildNow = () => {
		ProjectGroups.buildNow($scope.selectedProjectGroup.id).success((data) => {
			Notification.success("Project build requested.");				
		}).error((data, status) => {
			Notification.error("Project build request failed: "+data.description);
		});
	}
	
	$scope.deleteProject = () => {
		if (confirm("Are you sure you want to delete this project?")) {
			
			$scope.pauseRefresh = true;
			var submit = angular.element(document.getElementById("delete-project"));
			submit.addClass("glyphicon-refresh glyphicon-refresh-animate");
		
			ProjectGroups.delete($scope.selectedProjectGroup.id).success((data) => {
				submit.removeClass("glyphicon-refresh glyphicon-refresh-animate");
				Notification.success("Project deleted successfully.");
				$location.search('pgid', null)
				$state.go($state.current, {}, {reload: true});	
			}).error((data, status) => {
				$scope.pauseRefresh = false;
				submit.removeClass("glyphicon-refresh glyphicon-refresh-animate");
				Notification.error("Error deleting the project: "+data.description);
			});
		}
	}
};