package build.cross.services.common;

import java.util.Properties;

import javax.annotation.PostConstruct;

import org.jclouds.ContextBuilder;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.config.ComputeServiceProperties;
import org.jclouds.enterprise.config.EnterpriseConfigurationModule;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.jclouds.sshj.config.SshjSshClientModule;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

public class CloudCommon {
	
	protected String CLOUD_PROVIDER;
	protected String CLOUD_IDENTITY;
	protected String CLOUD_CREDENTIALS;
	protected String CLOUD_REGION;
	protected String CLOUD_INSTANCE_TYPE;
	protected String CROSSBUILD_SECURITY_GROUP;
	
	protected ComputeService compute;

	@PostConstruct
	protected void init() {

		CLOUD_PROVIDER = System.getProperty("CLOUD_PROVIDER");
		CLOUD_IDENTITY = System.getProperty("CLOUD_IDENTITY");
		CLOUD_CREDENTIALS = System.getProperty("CLOUD_CREDENTIALS");
		CLOUD_REGION = System.getProperty("CLOUD_REGION");
		CLOUD_INSTANCE_TYPE = System.getProperty("CLOUD_INSTANCE_TYPE");
		CROSSBUILD_SECURITY_GROUP = System.getProperty("CROSSBUILD_SECURITY_GROUP");

		compute = initComputeService(CLOUD_PROVIDER, CLOUD_IDENTITY, CLOUD_CREDENTIALS);
	}
	
	protected ComputeService initComputeService(String provider, String identity, String credential) {

		Iterable<Module> modules = ImmutableSet.<Module> of(new SshjSshClientModule(), new SLF4JLoggingModule(),
				new EnterpriseConfigurationModule());
		Properties properties = new Properties();
		properties.setProperty(ComputeServiceProperties.RESOURCENAME_PREFIX, "");
		
		ContextBuilder builder = ContextBuilder.newBuilder(provider)
				.credentials(identity, credential)
				.modules(modules)
				.overrides(properties);

		return builder.buildView(ComputeServiceContext.class).getComputeService();

	}

}
