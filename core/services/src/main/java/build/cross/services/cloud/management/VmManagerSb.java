package build.cross.services.cloud.management;

import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.getOnlyElement;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jboss.ejb3.annotation.TransactionTimeout;
import org.jclouds.aws.ec2.compute.extensions.AWSEC2SecurityGroupExtension;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.compute.domain.ExecResponse;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.NodeMetadata.Status;
import org.jclouds.compute.domain.SecurityGroup;
import org.jclouds.compute.domain.Template;
import org.jclouds.compute.domain.TemplateBuilder;
import org.jclouds.compute.options.RunScriptOptions;
import org.jclouds.domain.LoginCredentials;
import org.jclouds.ec2.EC2Api;
import org.jclouds.ec2.compute.options.EC2TemplateOptions;
import org.jclouds.ec2.domain.KeyPair;
import org.jclouds.ec2.features.KeyPairApi;
import org.jclouds.io.Payloads;
import org.jclouds.net.domain.IpPermission;
import org.jclouds.net.domain.IpPermission.Builder;
import org.jclouds.net.domain.IpProtocol;
import org.jclouds.ssh.SshClient;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;

import build.cross.models.jpa.Vm;
import build.cross.models.jpa.VmSetting;
import build.cross.services.cloud.metrics.LoadMonitorSbLocal;
import build.cross.services.common.CloudCommon;
import build.cross.services.exceptions.ServiceException;

@Stateless
public class VmManagerSb extends CloudCommon implements VmManagerSbLocal {

	@EJB
	private LoadMonitorSbLocal load;

	@EJB
	private CryptographySblocal crypto;

	@PersistenceContext(unitName = "crossbuild")
	private EntityManager em;

	private Logger logger = Logger.getLogger(VmManagerSb.class.getSimpleName());

	@Override
	public Set<? extends ComputeMetadata> listComputeMetadata() {

		Set<? extends ComputeMetadata> nodes = compute.listNodes();
		return nodes;
	}

	@Override
	public List<NodeMetadata> listNodeMetadata() {

		List<NodeMetadata> nmnodes = new ArrayList<NodeMetadata>();
		List<VmSetting> settings = em.createNamedQuery("VmSetting.findAll", VmSetting.class).getResultList();

		for (VmSetting vs : settings) {
			Set<? extends ComputeMetadata> nodes = compute.listNodesDetailsMatching(runningInGroup(vs.getGroupName()));
	
			for (ComputeMetadata c : nodes) {
				NodeMetadata nm = compute.getNodeMetadata(c.getId());
				nmnodes.add(nm);
			}
		}
		return nmnodes;
	}
	
	@Override
	public List<NodeMetadata> listNodeMetadataByGroup(String groupName) {

		List<NodeMetadata> nmnodes = new ArrayList<NodeMetadata>();
		
		Set<? extends ComputeMetadata> nodes = compute.listNodesDetailsMatching(runningInGroup(groupName));
		for (ComputeMetadata c : nodes) {
			NodeMetadata nm = compute.getNodeMetadata(c.getId());
			nmnodes.add(nm);
		}		
		return nmnodes;
	}

	@TransactionTimeout(1000)
	@Override
	public Vm createVm(VmSetting vmSettings) {

		TemplateBuilder templateBuilder;
		try {
			templateBuilder = prepareTemplate(vmSettings);
		
			Template template = templateBuilder.build();
	
			NodeMetadata node = getOnlyElement(compute.createNodesInGroup(vmSettings.getGroupName(), 1, template));
			logger.info("New node " + node.getId() + " " + concat(node.getPrivateAddresses(), node.getPublicAddresses())+" in group "+vmSettings.getGroupName());
	
			uploadFile(node, "bootstrap_" + vmSettings.getAmiId() + ".sh", true, false);
			uploadFile(node, "init_" + vmSettings.getAmiId() + ".sh", true, false);
			uploadFile(node, "loadmonitor_" + vmSettings.getAmiId() + ".sh", true, false);
			uploadFile(node, "loadmonitor.zip", false, true);
			uploadFile(node, "startmonitor.sh", true, false);
			uploadContainerFile(node, vmSettings.getContainerFile());
			
			executeBootstrapScript(node, vmSettings);
			executeInitScript(node, vmSettings);
			executeLoadScript(node, vmSettings);
			
			logger.info("Node in group "+node.getGroup()+" initiallized.");
			
			TypedQuery<build.cross.models.jpa.KeyPair> query = em.createNamedQuery("KeyPair.findByName",
					build.cross.models.jpa.KeyPair.class);
			query.setParameter("name", "crossbuild");
			build.cross.models.jpa.KeyPair kp = query.getResultList().get(0);
			
			Vm vm = new Vm();
			vm.setProvider(CLOUD_PROVIDER);
			vm.setCloudId(node.getId());
			vm.setGroupName(vmSettings.getGroupName());
			vm.setIp(node.getPublicAddresses().iterator().next());
			vm.setKeyPair(kp);
			vm.setVmSetting(vmSettings);
			em.persist(vm);
			
			return vm;
		} catch (ServiceException | IOException | RunNodesException e) {
			logger.severe("Error creating new vm");
			e.printStackTrace();
		}
		return null;
	}

	private TemplateBuilder prepareTemplate(VmSetting vmSettings) throws ServiceException, IOException {

		// need to accept subscription agreement for FreeBSD
		// https://aws.amazon.com/marketplace/pp/B00KSS55FY
		TemplateBuilder templateBuilder = compute.templateBuilder();
		templateBuilder.imageId(CLOUD_REGION + "/" + vmSettings.getAmiId());
		templateBuilder.locationId(CLOUD_REGION);
		templateBuilder.hardwareId(CLOUD_INSTANCE_TYPE);

		EC2TemplateOptions o = EC2TemplateOptions
				.Builder.keyPair("crossbuild")
				.overrideLoginCredentials(getLoginForCommandExecution(vmSettings))
				.securityGroups("crossbuild");
		templateBuilder.options(o);

		return templateBuilder;
	}
	
	private LoginCredentials getLoginForCommandExecution(VmSetting vmSettings) throws ServiceException {
		TypedQuery<build.cross.models.jpa.KeyPair> query = em.createNamedQuery("KeyPair.findByName",
				build.cross.models.jpa.KeyPair.class);
		query.setParameter("name", "crossbuild");

		build.cross.models.jpa.KeyPair kp = query.getResultList().get(0);
		String privateKey = "";
		if (kp != null) {
			privateKey = kp.getPrivMaterial();
		} else {
			throw new ServiceException("KeyPair crossbuild not found in database");
		}

		return LoginCredentials.builder().user(vmSettings.getLoginUser()).privateKey(privateKey).build();
	}
	
	@Override
	public void createSecurityGroup(String name) {
		AWSEC2SecurityGroupExtension client = (AWSEC2SecurityGroupExtension)compute.getSecurityGroupExtension().get();
		
		SecurityGroup sg = client.createSecurityGroup("crossbuild", CLOUD_REGION);
		Builder b = IpPermission.builder();
		b.fromPort(0);
		b.toPort(65535);
		b.groupId(sg.getProviderId());
		b.ipProtocol(IpProtocol.TCP);
		b.cidrBlock(CROSSBUILD_SECURITY_GROUP);
		client.addIpPermission(b.build(), sg);
		
		Builder b1 = IpPermission.builder();
		b1.fromPort(22);
		b1.toPort(22);
		b1.groupId(sg.getProviderId());
		b1.ipProtocol(IpProtocol.TCP);
		b1.cidrBlock("0.0.0.0/0");
		client.addIpPermission(b1.build(), sg);
	}
	
	@Override
	public boolean securityGroupExists(String name) {
		AWSEC2SecurityGroupExtension client = (AWSEC2SecurityGroupExtension)compute.getSecurityGroupExtension().get();
		
		Set<SecurityGroup> sgSet = client.listSecurityGroupsInLocation(CLOUD_REGION);
		
		for (SecurityGroup sg : sgSet) {
			logger.info("SG listing name: "+sg.getName());
			if (sg.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public KeyPair createKeyPair(String name) throws IOException {

		EC2Api ec2Api = compute.getContext().unwrapApi(EC2Api.class);
		KeyPairApi keyPairApi = ec2Api.getKeyPairApiForRegion(CLOUD_REGION).get();
		KeyPair keyPair = keyPairApi.createKeyPairInRegion(CLOUD_REGION, name);

		build.cross.models.jpa.KeyPair kp = new build.cross.models.jpa.KeyPair();
		kp.setName(name);
		kp.setPrivMaterial(keyPair.getKeyMaterial());
		try {
			kp.setPubMaterial(crypto.getPublicKeyFromPrivate(kp));
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			kp.setPubMaterial("unknown");
		}
		em.persist(kp);
		
		logger.info("New keypair created with name " + name + " and fingerprint " + keyPair.getFingerprint());

		return keyPair;
	}

	@Override
	public boolean keyPairExists(String name) {
		EC2Api ec2Api = compute.getContext().unwrapApi(EC2Api.class);
		Optional<? extends KeyPairApi> keyPairApiExtension = ec2Api.getKeyPairApiForRegion(CLOUD_REGION);
		KeyPairApi keyPairApi = keyPairApiExtension.get();

		for (KeyPair keyPair : keyPairApi.describeKeyPairsInRegion(CLOUD_REGION)) {
			logger.info(keyPair.getKeyName() + "-" + keyPair.getSha1OfPrivateKey());

			if (keyPair.getKeyName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	private KeyPair getPubKey(String name) {
		EC2Api ec2Api = compute.getContext().unwrapApi(EC2Api.class);
		Optional<? extends KeyPairApi> keyPairApiExtension = ec2Api.getKeyPairApiForRegion(CLOUD_REGION);
		KeyPairApi keyPairApi = keyPairApiExtension.get();

		for (KeyPair keyPair : keyPairApi.describeKeyPairsInRegion(CLOUD_REGION)) {
			logger.info(keyPair.getKeyMaterial());
			if (keyPair.getKeyName().equals(name)) {
				return keyPair;
			}
		}
		return null;
	}

	private Set<KeyPair> listKeyPairs() {
		EC2Api ec2Api = compute.getContext().unwrapApi(EC2Api.class);
		Optional<? extends KeyPairApi> keyPairApiExtension = ec2Api.getKeyPairApiForRegion(CLOUD_REGION);
		KeyPairApi keyPairApi = keyPairApiExtension.get();

		for (KeyPair keyPair : keyPairApi.describeKeyPairsInRegion(CLOUD_REGION)) {
			logger.info(keyPair.getKeyName() + "-" + keyPair.getSha1OfPrivateKey());
		}

		return keyPairApi.describeKeyPairsInRegion(CLOUD_REGION);
	}

	private void uploadFile(NodeMetadata node, String fname, boolean dos2unix, boolean binary) throws IOException {

		SshClient ssh = compute.getContext().utils().sshForNode().apply(node);
		ClassLoader classLoader = getClass().getClassLoader();
		logger.info(fname);
		InputStream file = classLoader.getResourceAsStream(fname);
		ssh.connect();
		
		if (!binary) {
			String content = IOUtils.toString(file, "UTF-8"); 
			if (dos2unix) {
				content=content.replaceAll("\r\n", "\n");
			}
			ssh.put(fname, Payloads.newStringPayload(content));
		} else {
			ssh.put(fname,  Payloads.newInputStreamPayload(file));
		}
		ssh.disconnect();
	}
	
	private void uploadContainerFile(NodeMetadata node, String fname) throws IOException {

		TypedQuery<build.cross.models.jpa.KeyPair> query = em.createNamedQuery("KeyPair.findByName",
				build.cross.models.jpa.KeyPair.class);
		query.setParameter("name", "crossbuild_container");

		build.cross.models.jpa.KeyPair kp = query.getResultList().get(0);
		
		SshClient ssh = compute.getContext().utils().sshForNode().apply(node);
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream file = classLoader.getResourceAsStream(fname);

		String content = IOUtils.toString(file, "UTF-8");
		content = content.replace("%PUBKEY%", kp.getPubMaterial());
		content = content.replaceAll("\r\n", "\n");
		
		ssh.connect();
		ssh.put(fname, Payloads.newStringPayload(content));
		ssh.disconnect();
	}

	private void executeBootstrapScript(NodeMetadata node, VmSetting vmSettings) throws ServiceException {

		RunScriptOptions opts = new RunScriptOptions();

		if (!vmSettings.isBootstrapAsSudo())
			opts.wrapInInitScript(false).runAsRoot(false);
		else {
			opts.wrapInInitScript(true).runAsRoot(true);
		}
		opts.overrideLoginCredentials(getLoginForCommandExecution(vmSettings));

		ExecResponse response = compute.runScriptOnNode(node.getId(), vmSettings.getBootstrapExecCmd(), opts);

		logger.info("Exit status: " + Integer.toString(response.getExitStatus()));
		logger.info("Error: " + response.getError());
		logger.info("Output: " + response.getOutput());
	}

	private void executeInitScript(NodeMetadata node, VmSetting vmSettings) throws ServiceException {

		RunScriptOptions opts = new RunScriptOptions();
		opts.wrapInInitScript(true).runAsRoot(true);

		opts.overrideLoginCredentials(getLoginForCommandExecution(vmSettings));

		ExecResponse response = compute.runScriptOnNode(node.getId(),
				"cd /home/"+vmSettings.getLoginUser()+" && pwd && chmod +x init_" + vmSettings.getAmiId() + ".sh && ./init_" + vmSettings.getAmiId() + ".sh",
				opts);

		logger.info("Exit status: " + Integer.toString(response.getExitStatus()));
		logger.info("Error: " + response.getError());
		logger.info("Output: " + response.getOutput());
	}
	
	@Override
	public Integer executeScriptAsRoot(NodeMetadata node, VmSetting vmSettings, String command) throws ServiceException {

		RunScriptOptions opts = new RunScriptOptions();
		opts.wrapInInitScript(true).runAsRoot(true);

		opts.overrideLoginCredentials(getLoginForCommandExecution(vmSettings));

		ExecResponse response = compute.runScriptOnNode(node.getId(),
				"cd /home/"+vmSettings.getLoginUser()+" && "+command, opts);

		logger.info("Exit status: " + Integer.toString(response.getExitStatus()));
		logger.info("Error: " + response.getError());
		logger.info("Output: " + response.getOutput());
		
		return response.getExitStatus();
	}
	
	private void executeLoadScript(NodeMetadata node, VmSetting vmSettings) throws ServiceException {

		RunScriptOptions opts = new RunScriptOptions();
		opts.wrapInInitScript(true).runAsRoot(false).overrideAuthenticateSudo(false);

		opts.overrideLoginCredentials(getLoginForCommandExecution(vmSettings));

		ExecResponse response = compute.runScriptOnNode(node.getId(),
				"cd /home/"+vmSettings.getLoginUser()+" && chmod +x loadmonitor_" + vmSettings.getAmiId() + ".sh && ./loadmonitor_" + vmSettings.getAmiId() + ".sh",
				opts);

		logger.info("Exit status: " + Integer.toString(response.getExitStatus()));
		logger.info("Error: " + response.getError());
		logger.info("Output: " + response.getOutput());
	}

	public static Predicate<ComputeMetadata> runningInGroup(final String group) {

		return new Predicate<ComputeMetadata>() {
			@Override
			public boolean apply(ComputeMetadata computeMetadata) {

				if (computeMetadata instanceof NodeMetadata) {
					NodeMetadata nodeMetadata = (NodeMetadata) computeMetadata;
					return group.equals(nodeMetadata.getGroup()) && nodeMetadata.getStatus() == Status.RUNNING;
				}
				return false;
			}

			@Override
			public String toString() {
				return "runningInGroup(" + group + ")";
			}
		};
	}
	
	@Override
	public NodeMetadata getNodeFromVm(Vm vm) {
		return compute.getNodeMetadata(vm.getCloudId());
	}
}
