package build.cross.jenkins.mediation;

import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import build.cross.models.jpa.Container;

@Stateless
public class NodeMediationSb extends MediationCommonSb implements NodeMediationSbLocal {
	
	private Logger logger = Logger.getLogger(NodeMediationSb.class.getSimpleName());
	
	@EJB
	private SecurityMediationSbLocal jenkinsSecurity;
	
	@Override
	public Response createNode(Container container) {
		
		Client client = ClientBuilder.newClient();
		WebTarget wt = client.target(jenkinsApi);
		
		String credentialsId = jenkinsSecurity.getCrossBuildCredential();
		logger.info("Using credentials "+credentialsId);
		
		Form form = new Form();
		form.param("name", container.getName());
		form.param("_.nodeDescription", "");
		form.param("_.numExecutors", "1");
		form.param("_.remoteFS", "/home/crossbuild");
		form.param("_.labelString", "");
		form.param("mode", "NORMAL");
		form.param("stapler-class", "hudson.slaves.JNLPLauncher");
		form.param("$class", "hudson.slaves.JNLPLauncher");
		form.param("_.tunnel", "");
		form.param("_.vmargs", "");
		form.param("stapler-class", "hudson.slaves.CommandLauncher");
		form.param("$class", "hudson.slaves.CommandLauncher");
		form.param("_.command", "");
		form.param("stapler-class", "hudson.plugins.sshslaves.SSHLauncher");
		form.param("$class", "hudson.plugins.sshslaves.SSHLauncher");
		form.param("_.host", container.getVm().getIp());
		form.param("_.credentialsId", credentialsId);
		form.param("_.port", Integer.toString(container.getPort()));
		form.param("_.javaPath", "");
		form.param("_.jvmOptions", "");
		form.param("_.prefixStartSlaveCmd", "");
		form.param("_.suffixStartSlaveCmd", "");
		form.param("launchTimeoutSeconds", "");
		form.param("maxNumRetries", "");
		form.param("retryWaitTime", "");
		form.param("stapler-class", "hudson.os.windows.ManagedWindowsServiceLauncher");
		form.param("$class", "hudson.os.windows.ManagedWindowsServiceLauncher");
		form.param("_.userName", "");
		form.param("_.password", "");
		form.param("_.host", "");
		form.param("stapler-class", "hudson.os.windows.ManagedWindowsServiceAccount$LocalSystem");
		form.param("$class", "hudson.os.windows.ManagedWindowsServiceAccount$LocalSystem");
		form.param("stapler-class", "hudson.os.windows.ManagedWindowsServiceAccount$AnotherUser");
		form.param("$class", "hudson.os.windows.ManagedWindowsServiceAccount$AnotherUser");
		form.param("stapler-class", "hudson.os.windows.ManagedWindowsServiceAccount$Administrator");
		form.param("$class", "hudson.os.windows.ManagedWindowsServiceAccount$Administrator");
		form.param("_.javaPath", "");
		form.param("_.vmargs", "");
		form.param("stapler-class", "hudson.slaves.RetentionStrategy$Always");
		form.param("$class", "hudson.slaves.RetentionStrategy$Always");
		form.param("stapler-class", "hudson.slaves.SimpleScheduledRetentionStrategy");
		form.param("$class", "hudson.slaves.SimpleScheduledRetentionStrategy");
		form.param("retentionStrategy.startTimeSpec", "");
		form.param("retentionStrategy.upTimeMins", "");
		form.param("retentionStrategy.keepUpWhenActive", "on");
		form.param("stapler-class", "hudson.slaves.RetentionStrategy$Demand");
		form.param("$class", "hudson.slaves.RetentionStrategy$Demand");
		form.param("retentionStrategy.inDemandDelay", "");
		form.param("retentionStrategy.idleDelay", "");
		form.param("stapler-class-bag", "true");
		form.param("type", "hudson.slaves.DumbSlave");
		form.param("Submit", "Save");
		
		JSONObject json = new JSONObject();
		json.put("name", container.getName());
		json.put("nodeDescription", "");
		json.put("numExecutors", "1");
		json.put("remoteFS", "/home/crossbuild");
		json.put("labelString", "");
		json.put("mode", "NORMAL");
		
		JSONArray jarr = new JSONArray();
		jarr.put("hudson.plugins.sshslaves.SSHLauncher");
		jarr.put("hudson.slaves.RetentionStrategy$Always");
		json.put("", jarr);
		
		JSONObject launcher = new JSONObject();
		launcher.put("stapler-class", "hudson.plugins.sshslaves.SSHLauncher");
		launcher.put("$class", "hudson.plugins.sshslaves.SSHLauncher");
		launcher.put("host", container.getVm().getIp());
		launcher.put("credentialsId", credentialsId);
		launcher.put("port", container.getPort());
		launcher.put("javaPath", "");
		launcher.put("jvmOptions", "");
		launcher.put("prefixStartSlaveCmd", "");
		launcher.put("suffixStartSlaveCmd", "");
		launcher.put("launchTimeoutSeconds", "");
		launcher.put("maxNumRetries", "");
		launcher.put("retryWaitTime", "");
		json.put("launcher", launcher);
		
		JSONObject retention = new JSONObject();
		retention.put("stapler-class", "hudson.slaves.RetentionStrategy$Always");
		retention.put("$class", "hudson.slaves.RetentionStrategy$Always");
		json.put("retentionStrategy", retention);
		
		JSONObject nodeProperties = new JSONObject();
		nodeProperties.put("stapler-class-bag", true);
		json.put("nodeProperties", nodeProperties);
		
		json.put("type", "hudson.slaves.DumbSlave");
		
		form.param("json", json.toString());
		
		Response response = wt.path("/computer/doCreateItem")
				.request(MediaType.APPLICATION_FORM_URLENCODED)
				.header("Authorization", authorizationHeaderValue)
		    	.post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED));
		
		logger.info("Response from jenkins: "+response.getStatus());
		
		return response;
	}
}
