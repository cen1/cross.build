export default /*@ngInject*/ ($scope, ProjectGroups) => {
	
	$scope.myprojects = [];
	
	ProjectGroups.get().success((data) => {
		$scope.myprojects = data;
	});
};