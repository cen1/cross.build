package build.cross.jenkins.mediation;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import build.cross.models.jpa.Container;
import build.cross.models.jpa.KeyPair;
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
		
		logger.info("Response from jenkins: "+response.getStatus());
		
		client = ClientBuilder.newClient();
		WebTarget wt2 = client.target(jenkinsApi);
		Form form2 = new Form();
		
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
		
		logger.info(json.toString());
		
		//generate form
		form2.param("name", project.getId());
		form2.param("description", "");
		form2.param("stapler-class-bag", "true");
		form2.param("_.daysToKeepStr", "");
		form2.param("_.numToKeepStr", "");
		form2.param("_.artifactDaysToKeepStr", "");
		form2.param("_.artifactNumToKeepStr", "");
		form2.param("stapler-class", "hudson.tasks.LogRotator");
		form2.param("$class", "hudson.tasks.LogRotator");
		form2.param("_.projectUrlStr", "");
		form2.param("_.displayName", "");
		form2.param("_.count", "1");
		form2.param("_.durationName", "hour");
		form2.param("hasSlaveAffinity", "on");
		form2.param("_.label", project.getContainer().getName());
		form2.param("quiet_period", "5");
		form2.param("scmCheckoutRetryCount", "0");
		form2.param("_.customWorkspace", "");
		form2.param("_.displayNameOrNull", "");
		form2.param("scm", "1");
		form2.param("_.url", project.getProjectGroup().getRepository());
		form2.param("_.credentialsId", "");
		form2.param("_.name", "");
		form2.param("_.refspec", "");
		form2.param("_.name", "*/master");
		form2.param("stapler-class", "hudson.plugins.git.browser.AssemblaWeb");
		form2.param("$class", "hudson.plugins.git.browser.AssemblaWeb");
		form2.param("stapler-class", "hudson.plugins.git.browser.FisheyeGitRepositoryBrowser");
		form2.param("$class", "hudson.plugins.git.browser.FisheyeGitRepositoryBrowser");
		form2.param("stapler-class", "hudson.plugins.git.browser.KilnGit");
		form2.param("$class", "hudson.plugins.git.browser.KilnGit");
		form2.param("stapler-class", "hudson.plugins.git.browser.TFS2013GitRepositoryBrowser");
		form2.param("$class", "hudson.plugins.git.browser.TFS2013GitRepositoryBrowser");
		form2.param("stapler-class", "hudson.plugins.git.browser.BitbucketWeb");
		form2.param("$class", "hudson.plugins.git.browser.BitbucketWeb");
		form2.param("stapler-class", "hudson.plugins.git.browser.CGit");
		form2.param("$class", "hudson.plugins.git.browser.CGit");
		form2.param("stapler-class", "hudson.plugins.git.browser.GitBlitRepositoryBrowser");
		form2.param("$class", "hudson.plugins.git.browser.GitBlitRepositoryBrowser");
		form2.param("stapler-class", "hudson.plugins.git.browser.GithubWeb");
		form2.param("$class", "hudson.plugins.git.browser.GithubWeb");
		form2.param("stapler-class", "hudson.plugins.git.browser.Gitiles");
		form2.param("$class", "hudson.plugins.git.browser.Gitiles");
		form2.param("stapler-class", "hudson.plugins.git.browser.GitLab");
		form2.param("$class", "hudson.plugins.git.browser.GitLab");
		form2.param("stapler-class", "hudson.plugins.git.browser.GitList");
		form2.param("$class", "hudson.plugins.git.browser.GitList");
		form2.param("stapler-class", "hudson.plugins.git.browser.GitoriousWeb");
		form2.param("$class", "hudson.plugins.git.browser.GitoriousWeb");
		form2.param("stapler-class", "hudson.plugins.git.browser.GitWeb");
		form2.param("$class", "hudson.plugins.git.browser.GitWeb");
		form2.param("stapler-class", "hudson.plugins.git.browser.Phabricator");
		form2.param("$class", "hudson.plugins.git.browser.Phabricator");
		form2.param("stapler-class", "hudson.plugins.git.browser.RedmineWeb");
		form2.param("$class", "hudson.plugins.git.browser.RedmineWeb");
		form2.param("stapler-class", "hudson.plugins.git.browser.RhodeCode");
		form2.param("$class", "hudson.plugins.git.browser.RhodeCode");
		form2.param("stapler-class", "hudson.plugins.git.browser.Stash");
		form2.param("$class", "hudson.plugins.git.browser.Stash");
		form2.param("stapler-class", "hudson.plugins.git.browser.ViewGitWeb");
		form2.param("$class", "hudson.plugins.git.browser.ViewGitWeb");
		form2.param("_.remote", "");
		form2.param("_.credentialsId", "");
		form2.param("_.local", ".");
		form2.param("depthOption", "infinity");
		form2.param("_.ignoreExternalsOption", "on");
		form2.param("stapler-class", "hudson.scm.subversion.UpdateUpdater");
		form2.param("$class", "hudson.scm.subversion.UpdateUpdater");
		form2.param("stapler-class", "hudson.scm.subversion.CheckoutUpdater");
		form2.param("$class", "hudson.scm.subversion.CheckoutUpdater");
		form2.param("stapler-class", "hudson.scm.subversion.UpdateWithCleanUpdater");
		form2.param("$class", "hudson.scm.subversion.UpdateWithCleanUpdater");
		form2.param("stapler-class", "hudson.scm.subversion.UpdateWithRevertUpdater");
		form2.param("$class", "hudson.scm.subversion.UpdateWithRevertUpdater");
		form2.param("stapler-class", "hudson.scm.browsers.Assembla");
		form2.param("$class", "hudson.scm.browsers.Assembla");
		form2.param("stapler-class", "hudson.scm.browsers.CollabNetSVN");
		form2.param("$class", "hudson.scm.browsers.CollabNetSVN");
		form2.param("stapler-class", "hudson.scm.browsers.FishEyeSVN");
		form2.param("$class", "hudson.scm.browsers.FishEyeSVN");
		form2.param("stapler-class", "hudson.scm.browsers.SVNWeb");
		form2.param("$class", "hudson.scm.browsers.SVNWeb");
		form2.param("stapler-class", "hudson.scm.browsers.Sventon");
		form2.param("$class", "hudson.scm.browsers.Sventon");
		form2.param("stapler-class", "hudson.scm.browsers.Sventon2");
		form2.param("$class", "hudson.scm.browsers.Sventon2");
		form2.param("stapler-class", "hudson.scm.browsers.ViewSVN");
		form2.param("$class", "hudson.scm.browsers.ViewSVN");
		form2.param("stapler-class", "hudson.scm.browsers.WebSVN");
		form2.param("$class", "hudson.scm.browsers.WebSVN");
		form2.param("_.excludedRegions", "");
		form2.param("_.includedRegions", "");
		form2.param("_.excludedUsers", "");
		form2.param("_.excludedCommitMessages", "");
		form2.param("_.excludedRevprop", "");
		form2.param("authToken", "");
		form2.param("_.upstreamProjects", "");
		form2.param("ReverseBuildTrigger.threshold", "SUCCESS");
		form2.param("_.spec", "");
		form2.param("hudson-triggers-SCMTrigger", "on");
		form2.param("scmpoll_spec", "* * * * *");
		form2.param("_.cleanupParameter", "");
		form2.param("_.externalDelete", "");
		form2.param("_.timeoutMinutes", "3");
		form2.param("stapler-class", "hudson.plugins.build_timeout.impl.AbsoluteTimeOutStrategy");
		form2.param("$class", "hudson.plugins.build_timeout.impl.AbsoluteTimeOutStrategy");
		form2.param("stapler-class", "hudson.plugins.build_timeout.impl.DeadlineTimeOutStrategy");
		form2.param("$class", "hudson.plugins.build_timeout.impl.DeadlineTimeOutStrategy");
		form2.param("stapler-class", "hudson.plugins.build_timeout.impl.ElasticTimeOutStrategy");
		form2.param("$class", "hudson.plugins.build_timeout.impl.ElasticTimeOutStrategy");
		form2.param("stapler-class", "hudson.plugins.build_timeout.impl.LikelyStuckTimeOutStrategy");
		form2.param("$class", "hudson.plugins.build_timeout.impl.LikelyStuckTimeOutStrategy");
		form2.param("stapler-class", "hudson.plugins.build_timeout.impl.NoActivityTimeOutStrategy");
		form2.param("$class", "hudson.plugins.build_timeout.impl.NoActivityTimeOutStrategy");
		form2.param("_.timeoutEnvVar", "");
		form2.param("command", project.getBuildCommand());
		form2.param("stapler-class", "hudson.tasks.Shell");
		form2.param("$class", "hudson.tasks.Shell");
		form2.param("core:apply", "");
		form2.param("json", json.toString());
		form2.param("Submit", "Save");
		
		Response response2 = wt2.path("/job/"+project.getId()+"/configSubmit")
				.request(MediaType.APPLICATION_FORM_URLENCODED)
				.header("Authorization", authorizationHeaderValue)
		    	.post(Entity.entity(form2,MediaType.APPLICATION_FORM_URLENCODED));
		
		logger.info("Response from jenkins: "+response.getStatus());
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
		
		logger.info("Response from jenkins: "+response.getStatus());
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
		
		logger.info("Response from jenkins: "+response.getStatus());
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
		
		logger.info("Response from jenkins: "+response.getStatus());
		return response;
	}
}
