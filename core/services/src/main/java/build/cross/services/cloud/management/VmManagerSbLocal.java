package build.cross.services.cloud.management;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.ec2.domain.KeyPair;

import build.cross.models.jpa.Vm;
import build.cross.models.jpa.VmSetting;
import build.cross.services.exceptions.ServiceException;

@Local
public interface VmManagerSbLocal {

	List<NodeMetadata> listNodeMetadata();

	Set<? extends ComputeMetadata> listComputeMetadata();

	List<NodeMetadata> listNodeMetadataByGroup(String groupName);

	boolean keyPairExists(String name);

	KeyPair createKeyPair(String name) throws IOException;

	void createSecurityGroup(String name);

	Vm createVm(VmSetting vmSettings);

	boolean securityGroupExists(String name);

	NodeMetadata getNodeFromVm(Vm vm);

	Integer executeScriptAsRoot(NodeMetadata node, VmSetting vmSettings, String command) throws ServiceException;

}
