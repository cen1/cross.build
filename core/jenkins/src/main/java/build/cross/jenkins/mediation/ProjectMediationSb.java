package build.cross.jenkins.mediation;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import build.cross.models.jpa.Project;

@Stateless
public class ProjectMediationSb extends MediationCommonSb implements ProjectMediationSbLocal {
	
	private Logger logger = Logger.getLogger(ProjectMediationSb.class.getSimpleName());
	
	@Override
	public Response addProject(Project project) {
		
		Client client = ClientBuilder.newClient();
		WebTarget wt = client.target(jenkinsApi);
		
		Form form = new Form();
		form.param("name", project.getId());
		form.param("mode", "hudson.model.FreeStyleProject");
		
		Response response = wt.path("/view/All/createItem")
				.request(MediaType.APPLICATION_FORM_URLENCODED)
				.header("Authorization", authorizationHeaderValue)
		    	.post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED));
		
		//logger.info("Response from jenkins: "+response.getStatus());
		
		return response;
	}
	
	@Override
	public Response configureProject(Project project) {
		Client client = ClientBuilder.newClient();
		WebTarget wt = client.target(jenkinsApi);
		Form form = new Form();
		
		JSONObject json = new JSONObject();
		json.put("name", project.getId());
		json.put("description", "");
		json.put("disable", false);
		json.put("concurrentBuild", false);
		json.put("hasSlaveAffinity",true);
		json.put("label", project.getContainer().getName());
		json.put("hasCustomQuietPeriod", false);
		json.put("quiet_period", "5");
		json.put("hasCustomScmCheckoutRetryCount", false);
		json.put("scmCheckoutRetryCount", "0");
		json.put("blockBuildWhenUpstreamBuilding", false);
		json.put("blockBuildWhenDownstreamBuilding", false);
		json.put("hasCustomWorkspace", false);
		json.put("customWorkspace", "");
		json.put("displayNameOrNull", "");
		
		JSONObject scm = new JSONObject();
		scm.put("value", "1");
		JSONObject userRemoteConfigs = new JSONObject();
		userRemoteConfigs.put("url", project.getProjectGroup().getRepository());
		userRemoteConfigs.put("credentialsId", "");
		userRemoteConfigs.put("name", "");
		userRemoteConfigs.put("refspec", "");
		scm.put("userRemoteConfigs", userRemoteConfigs);
		JSONObject branches = new JSONObject();
		branches.put("name", "*/master");
		scm.put("branches", branches);
		scm.put("", "auto");
		json.put("scm", scm);
		
		JSONObject hudson_triggers_SCMTrigger = new JSONObject();
		hudson_triggers_SCMTrigger.put("scmpoll_spec", "* * * * *");
		hudson_triggers_SCMTrigger.put("ignorePostCommitHooks", false);
		json.put("hudson-triggers-SCMTrigger", hudson_triggers_SCMTrigger);
		
		JSONObject properties = new JSONObject();
		properties.put("stapler-class-bag", "true");
		JSONObject jenkins_model_BuildDiscarderProperty = new JSONObject();
		jenkins_model_BuildDiscarderProperty.put("specified", false);
		jenkins_model_BuildDiscarderProperty.put("", "0");
		JSONObject strategy = new JSONObject();
		strategy.put("daysToKeepStr", "");
		strategy.put("numToKeepStr", "");
		strategy.put("artifactDaysToKeepStr", "");
		strategy.put("artifactNumToKeepStr", "");
		strategy.put("stapler-class", "hudson.tasks.LogRotator");
		strategy.put("$class", "hudson.tasks.LogRotator");
		jenkins_model_BuildDiscarderProperty.put("strategy", strategy);
		properties.put("jenkins-model-BuildDiscarderProperty", jenkins_model_BuildDiscarderProperty);
		properties.put("com-coravy-hudson-plugins-github-GithubProjectProperty", new JSONObject("{}"));
		JSONObject hudson_model_ParametersDefinitionProperty = new JSONObject();
		hudson_model_ParametersDefinitionProperty.put("specified", false);
		properties.put("hudson-model-ParametersDefinitionProperty", hudson_model_ParametersDefinitionProperty);
		properties.put("jenkins-branch-RateLimitBranchProperty$JobPropertyImpl", new JSONObject("{}"));
		json.put("properties", properties);
		
		JSONObject builder = new JSONObject();
		builder.put("command", project.getBuildCommand());
		builder.put("", project.getBuildCommand());
		builder.put("stapler-class", "hudson.tasks.Shell");
		builder.put("$class", "hudson.tasks.Shell");
		json.put("builder", builder);
		
		json.put("core:apply", "");
		
		//logger.info(json.toString());
		
		//generate form
		form.param("name", project.getId());
		form.param("description", "");
		form.param("stapler-class-bag", "true");
		form.param("_.daysToKeepStr", "");
		form.param("_.numToKeepStr", "");
		form.param("_.artifactDaysToKeepStr", "");
		form.param("_.artifactNumToKeepStr", "");
		form.param("stapler-class", "hudson.tasks.LogRotator");
		form.param("$class", "hudson.tasks.LogRotator");
		form.param("_.projectUrlStr", "");
		form.param("_.displayName", "");
		form.param("_.count", "1");
		form.param("_.durationName", "hour");
		form.param("hasSlaveAffinity", "on");
		form.param("_.label", project.getContainer().getName());
		form.param("quiet_period", "5");
		form.param("scmCheckoutRetryCount", "0");
		form.param("_.customWorkspace", "");
		form.param("_.displayNameOrNull", "");
		form.param("scm", "1");
		form.param("_.url", project.getProjectGroup().getRepository());
		form.param("_.credentialsId", "");
		form.param("_.name", "");
		form.param("_.refspec", "");
		form.param("_.name", "*/master");
		form.param("stapler-class", "hudson.plugins.git.browser.AssemblaWeb");
		form.param("$class", "hudson.plugins.git.browser.AssemblaWeb");
		form.param("stapler-class", "hudson.plugins.git.browser.FisheyeGitRepositoryBrowser");
		form.param("$class", "hudson.plugins.git.browser.FisheyeGitRepositoryBrowser");
		form.param("stapler-class", "hudson.plugins.git.browser.KilnGit");
		form.param("$class", "hudson.plugins.git.browser.KilnGit");
		form.param("stapler-class", "hudson.plugins.git.browser.TFS2013GitRepositoryBrowser");
		form.param("$class", "hudson.plugins.git.browser.TFS2013GitRepositoryBrowser");
		form.param("stapler-class", "hudson.plugins.git.browser.BitbucketWeb");
		form.param("$class", "hudson.plugins.git.browser.BitbucketWeb");
		form.param("stapler-class", "hudson.plugins.git.browser.CGit");
		form.param("$class", "hudson.plugins.git.browser.CGit");
		form.param("stapler-class", "hudson.plugins.git.browser.GitBlitRepositoryBrowser");
		form.param("$class", "hudson.plugins.git.browser.GitBlitRepositoryBrowser");
		form.param("stapler-class", "hudson.plugins.git.browser.GithubWeb");
		form.param("$class", "hudson.plugins.git.browser.GithubWeb");
		form.param("stapler-class", "hudson.plugins.git.browser.Gitiles");
		form.param("$class", "hudson.plugins.git.browser.Gitiles");
		form.param("stapler-class", "hudson.plugins.git.browser.GitLab");
		form.param("$class", "hudson.plugins.git.browser.GitLab");
		form.param("stapler-class", "hudson.plugins.git.browser.GitList");
		form.param("$class", "hudson.plugins.git.browser.GitList");
		form.param("stapler-class", "hudson.plugins.git.browser.GitoriousWeb");
		form.param("$class", "hudson.plugins.git.browser.GitoriousWeb");
		form.param("stapler-class", "hudson.plugins.git.browser.GitWeb");
		form.param("$class", "hudson.plugins.git.browser.GitWeb");
		form.param("stapler-class", "hudson.plugins.git.browser.Phabricator");
		form.param("$class", "hudson.plugins.git.browser.Phabricator");
		form.param("stapler-class", "hudson.plugins.git.browser.RedmineWeb");
		form.param("$class", "hudson.plugins.git.browser.RedmineWeb");
		form.param("stapler-class", "hudson.plugins.git.browser.RhodeCode");
		form.param("$class", "hudson.plugins.git.browser.RhodeCode");
		form.param("stapler-class", "hudson.plugins.git.browser.Stash");
		form.param("$class", "hudson.plugins.git.browser.Stash");
		form.param("stapler-class", "hudson.plugins.git.browser.ViewGitWeb");
		form.param("$class", "hudson.plugins.git.browser.ViewGitWeb");
		form.param("_.remote", "");
		form.param("_.credentialsId", "");
		form.param("_.local", ".");
		form.param("depthOption", "infinity");
		form.param("_.ignoreExternalsOption", "on");
		form.param("stapler-class", "hudson.scm.subversion.UpdateUpdater");
		form.param("$class", "hudson.scm.subversion.UpdateUpdater");
		form.param("stapler-class", "hudson.scm.subversion.CheckoutUpdater");
		form.param("$class", "hudson.scm.subversion.CheckoutUpdater");
		form.param("stapler-class", "hudson.scm.subversion.UpdateWithCleanUpdater");
		form.param("$class", "hudson.scm.subversion.UpdateWithCleanUpdater");
		form.param("stapler-class", "hudson.scm.subversion.UpdateWithRevertUpdater");
		form.param("$class", "hudson.scm.subversion.UpdateWithRevertUpdater");
		form.param("stapler-class", "hudson.scm.browsers.Assembla");
		form.param("$class", "hudson.scm.browsers.Assembla");
		form.param("stapler-class", "hudson.scm.browsers.CollabNetSVN");
		form.param("$class", "hudson.scm.browsers.CollabNetSVN");
		form.param("stapler-class", "hudson.scm.browsers.FishEyeSVN");
		form.param("$class", "hudson.scm.browsers.FishEyeSVN");
		form.param("stapler-class", "hudson.scm.browsers.SVNWeb");
		form.param("$class", "hudson.scm.browsers.SVNWeb");
		form.param("stapler-class", "hudson.scm.browsers.Sventon");
		form.param("$class", "hudson.scm.browsers.Sventon");
		form.param("stapler-class", "hudson.scm.browsers.Sventon2");
		form.param("$class", "hudson.scm.browsers.Sventon2");
		form.param("stapler-class", "hudson.scm.browsers.ViewSVN");
		form.param("$class", "hudson.scm.browsers.ViewSVN");
		form.param("stapler-class", "hudson.scm.browsers.WebSVN");
		form.param("$class", "hudson.scm.browsers.WebSVN");
		form.param("_.excludedRegions", "");
		form.param("_.includedRegions", "");
		form.param("_.excludedUsers", "");
		form.param("_.excludedCommitMessages", "");
		form.param("_.excludedRevprop", "");
		form.param("authToken", "");
		form.param("_.upstreamProjects", "");
		form.param("ReverseBuildTrigger.threshold", "SUCCESS");
		form.param("_.spec", "");
		form.param("hudson-triggers-SCMTrigger", "on");
		form.param("scmpoll_spec", "* * * * *");
		form.param("_.cleanupParameter", "");
		form.param("_.externalDelete", "");
		form.param("_.timeoutMinutes", "3");
		form.param("stapler-class", "hudson.plugins.build_timeout.impl.AbsoluteTimeOutStrategy");
		form.param("$class", "hudson.plugins.build_timeout.impl.AbsoluteTimeOutStrategy");
		form.param("stapler-class", "hudson.plugins.build_timeout.impl.DeadlineTimeOutStrategy");
		form.param("$class", "hudson.plugins.build_timeout.impl.DeadlineTimeOutStrategy");
		form.param("stapler-class", "hudson.plugins.build_timeout.impl.ElasticTimeOutStrategy");
		form.param("$class", "hudson.plugins.build_timeout.impl.ElasticTimeOutStrategy");
		form.param("stapler-class", "hudson.plugins.build_timeout.impl.LikelyStuckTimeOutStrategy");
		form.param("$class", "hudson.plugins.build_timeout.impl.LikelyStuckTimeOutStrategy");
		form.param("stapler-class", "hudson.plugins.build_timeout.impl.NoActivityTimeOutStrategy");
		form.param("$class", "hudson.plugins.build_timeout.impl.NoActivityTimeOutStrategy");
		form.param("_.timeoutEnvVar", "");
		form.param("command", project.getBuildCommand());
		form.param("stapler-class", "hudson.tasks.Shell");
		form.param("$class", "hudson.tasks.Shell");
		form.param("core:apply", "");
		form.param("json", json.toString());
		form.param("Submit", "Save");
		
		Response response = wt.path("/job/"+project.getId()+"/configSubmit")
				.request(MediaType.APPLICATION_FORM_URLENCODED)
				.header("Authorization", authorizationHeaderValue)
		    	.post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED));
		
		//logger.info("Response from jenkins: "+response.getStatus());
		logger.info("Created job "+project.getId());
		
		return response;
	}
	
	@Override
	public Response getProjectInfo(String projectId) {
		Client client = ClientBuilder.newClient();
		WebTarget wt = client.target(jenkinsApi);
		
		Response response = wt.path("/job/"+projectId+"/api/json")
				.request(MediaType.APPLICATION_JSON)
				.header("Authorization", authorizationHeaderValue)
		    	.get(); //.queryParam("pretty", "true")
		
		//logger.info("Response from jenkins: "+response.getStatus());
		return response;
	}
	
	@Override
	public Response getBuildDetails(String projectId, String buildNumber) {
		Client client = ClientBuilder.newClient();
		WebTarget wt = client.target(jenkinsApi);
		
		Response response = wt.path("/job/"+projectId+"/"+buildNumber+"/api/json")
				.request(MediaType.APPLICATION_JSON)
				.header("Authorization", authorizationHeaderValue)
		    	.get(); //.queryParam("pretty", "true")
		
		//logger.info("Response from jenkins: "+response.getStatus());
		return response;
	}
	
	@Override
	public Response getBuildConsoleText(String projectId, String buildNumber) {
		Client client = ClientBuilder.newClient();
		WebTarget wt = client.target(jenkinsApi);
		
		Response response = wt.path("/job/"+projectId+"/"+buildNumber+"/consoleText")
				.request(MediaType.APPLICATION_JSON)
				.header("Authorization", authorizationHeaderValue)
		    	.get();
		
		//logger.info("Response from jenkins: "+response.getStatus());
		return response;
	}
	
	@Override
	public Response buildNow(String projectId) {
		Client client = ClientBuilder.newClient();
		WebTarget wt = client.target(jenkinsApi);
		
		Response response = wt.path("/job/"+projectId+"/build")
				.queryParam("delay", "0sec")
				.request(MediaType.APPLICATION_JSON)
				.header("Authorization", authorizationHeaderValue)
		    	.post(Entity.json(null));
		
		//logger.info("Response from jenkins: "+response.getStatus());
		return response;
	}
	
	@Override
	public Response getProgressiveConsole(String projectId, String buildNumber, String offset) {
		Client client = ClientBuilder.newClient();
		WebTarget wt = client.target(jenkinsApi);
		
		Form form = new Form();
		form.param("start", offset);
		
		Response response = wt.path("/job/"+projectId+"/"+buildNumber+"/logText/progressiveHtml")
				.request(MediaType.TEXT_HTML)
				.header("Authorization", authorizationHeaderValue)
		    	.post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED));
		
		//logger.info("Response from jenkins: "+response.getStatus());
		return response;
	}
	
	@Override
	public Response deleteProject(String projectId) {
		Client client = ClientBuilder.newClient();
		WebTarget wt = client.target(jenkinsApi);
		
		Form form = new Form();
		
		Response response = wt.path("/job/"+projectId+"/doDelete")
				.request(MediaType.APPLICATION_JSON)
				.header("Authorization", authorizationHeaderValue)
		    	.post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED));
		
		//logger.info("Response from jenkins: "+response.getStatus());
		return response;
	}
}
