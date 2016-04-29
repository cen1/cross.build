package build.cross.services.types;

import java.net.URL;
import java.util.List;

public class ProjectSettings {
	
	private String username;
	
	private String projectName;
	
	private String projectDescription;
	
	private boolean discardOldBuilds; //extra
	
	private boolean parameterizedBuild; //extra
	
	private boolean disableBuild;
	
	private boolean executeConcurrentBuilds;
	
	private boolean restrictWhereProjectCanBeRun; //extra
	
	private boolean quietPeriod; //extra
	
	private boolean retryCount; //extra
	
	private boolean blockBuildWhenUpstream;
	
	private boolean blockBuildWhenDownstream;
	
	private boolean useCustomWorkspace; //extra
	
	private boolean useCustomCHildWorkspace; //extra
	
	private String displayName;
	
	private ScmType scm;
	
	private URL repoUrl; //extra
	
	private List<String> branches;
	
	private String repoBrowser;
	
	private boolean additionalBehaviours;
	
	private boolean triggerRemotely; //extra
	
	private boolean buildAfterBuild; //extra
	
	private boolean buildPeriodically; //extra
	
	private boolean pollScm;
	
	private String pollScmSchedule;
	
	private boolean ignorePostCommitHooks;
	
	private List<Axis> axis;
	
	private String combinationFilter;
	
	private boolean runConfigurationSequentially;
	
	private boolean touchstoneFirst; //extra
	
	private String buildCommand;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public boolean isDiscardOldBuilds() {
		return discardOldBuilds;
	}

	public void setDiscardOldBuilds(boolean discardOldBuilds) {
		this.discardOldBuilds = discardOldBuilds;
	}

	public boolean isParameterizedBuild() {
		return parameterizedBuild;
	}

	public void setParameterizedBuild(boolean parameterizedBuild) {
		this.parameterizedBuild = parameterizedBuild;
	}

	public boolean isDisableBuild() {
		return disableBuild;
	}

	public void setDisableBuild(boolean disableBuild) {
		this.disableBuild = disableBuild;
	}

	public boolean isExecuteConcurrentBuilds() {
		return executeConcurrentBuilds;
	}

	public void setExecuteConcurrentBuilds(boolean executeConcurrentBuilds) {
		this.executeConcurrentBuilds = executeConcurrentBuilds;
	}

	public boolean isRestrictWhereProjectCanBeRun() {
		return restrictWhereProjectCanBeRun;
	}

	public void setRestrictWhereProjectCanBeRun(boolean restrictWhereProjectCanBeRun) {
		this.restrictWhereProjectCanBeRun = restrictWhereProjectCanBeRun;
	}

	public boolean isQuietPeriod() {
		return quietPeriod;
	}

	public void setQuietPeriod(boolean quietPeriod) {
		this.quietPeriod = quietPeriod;
	}

	public boolean isRetryCount() {
		return retryCount;
	}

	public void setRetryCount(boolean retryCount) {
		this.retryCount = retryCount;
	}

	public boolean isBlockBuildWhenUpstream() {
		return blockBuildWhenUpstream;
	}

	public void setBlockBuildWhenUpstream(boolean blockBuildWhenUpstream) {
		this.blockBuildWhenUpstream = blockBuildWhenUpstream;
	}

	public boolean isBlockBuildWhenDownstream() {
		return blockBuildWhenDownstream;
	}

	public void setBlockBuildWhenDownstream(boolean blockBuildWhenDownstream) {
		this.blockBuildWhenDownstream = blockBuildWhenDownstream;
	}

	public boolean isUseCustomWorkspace() {
		return useCustomWorkspace;
	}

	public void setUseCustomWorkspace(boolean useCustomWorkspace) {
		this.useCustomWorkspace = useCustomWorkspace;
	}

	public boolean isUseCustomCHildWorkspace() {
		return useCustomCHildWorkspace;
	}

	public void setUseCustomCHildWorkspace(boolean useCustomCHildWorkspace) {
		this.useCustomCHildWorkspace = useCustomCHildWorkspace;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public ScmType getScm() {
		return scm;
	}

	public void setScm(ScmType scm) {
		this.scm = scm;
	}

	public URL getRepoUrl() {
		return repoUrl;
	}

	public void setRepoUrl(URL repoUrl) {
		this.repoUrl = repoUrl;
	}

	public List<String> getBranches() {
		return branches;
	}

	public void setBranches(List<String> branches) {
		this.branches = branches;
	}

	public String getRepoBrowser() {
		return repoBrowser;
	}

	public void setRepoBrowser(String repoBrowser) {
		this.repoBrowser = repoBrowser;
	}

	public boolean isAdditionalBehaviours() {
		return additionalBehaviours;
	}

	public void setAdditionalBehaviours(boolean additionalBehaviours) {
		this.additionalBehaviours = additionalBehaviours;
	}

	public boolean isTriggerRemotely() {
		return triggerRemotely;
	}

	public void setTriggerRemotely(boolean triggerRemotely) {
		this.triggerRemotely = triggerRemotely;
	}

	public boolean isBuildAfterBuild() {
		return buildAfterBuild;
	}

	public void setBuildAfterBuild(boolean buildAfterBuild) {
		this.buildAfterBuild = buildAfterBuild;
	}

	public boolean isBuildPeriodically() {
		return buildPeriodically;
	}

	public void setBuildPeriodically(boolean buildPeriodically) {
		this.buildPeriodically = buildPeriodically;
	}

	public boolean isPollScm() {
		return pollScm;
	}

	public void setPollScm(boolean pollScm) {
		this.pollScm = pollScm;
	}

	public String getPollScmSchedule() {
		return pollScmSchedule;
	}

	public void setPollScmSchedule(String pollScmSchedule) {
		this.pollScmSchedule = pollScmSchedule;
	}

	public boolean isIgnorePostCommitHooks() {
		return ignorePostCommitHooks;
	}

	public void setIgnorePostCommitHooks(boolean ignorePostCommitHooks) {
		this.ignorePostCommitHooks = ignorePostCommitHooks;
	}

	public List<Axis> getAxis() {
		return axis;
	}

	public void setAxis(List<Axis> axis) {
		this.axis = axis;
	}

	public String getCombinationFilter() {
		return combinationFilter;
	}

	public void setCombinationFilter(String combinationFilter) {
		this.combinationFilter = combinationFilter;
	}

	public boolean isRunConfigurationSequentially() {
		return runConfigurationSequentially;
	}

	public void setRunConfigurationSequentially(boolean runConfigurationSequentially) {
		this.runConfigurationSequentially = runConfigurationSequentially;
	}

	public boolean isTouchstoneFirst() {
		return touchstoneFirst;
	}

	public void setTouchstoneFirst(boolean touchstoneFirst) {
		this.touchstoneFirst = touchstoneFirst;
	}

	public String getBuildCommand() {
		return buildCommand;
	}

	public void setBuildCommand(String buildCommand) {
		this.buildCommand = buildCommand;
	}

	public String getProjectDescription() {
		return projectDescription;
	}

	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}

	
	
	//add build step
	
	//post-build actions
	
	
}
