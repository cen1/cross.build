package build.cross.services.cloud.containers;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.codec.binary.Hex;
import org.jclouds.compute.domain.NodeMetadata;

import com.google.common.net.InetAddresses;

import build.cross.models.jpa.Container;
import build.cross.models.jpa.KeyPair;
import build.cross.models.jpa.LoadHistory;
import build.cross.models.jpa.Project;
import build.cross.models.jpa.VmSetting;
import build.cross.services.cloud.management.VmManagerSbLocal;
import build.cross.services.common.CloudCommon;
import build.cross.services.exceptions.ServiceException;

@Stateless
public class ContainerManagerSb extends CloudCommon implements ContainerManagerSbLocal {
	
	private Logger logger = Logger.getLogger(ContainerManagerSb.class.getSimpleName());

	
	@PersistenceContext(unitName = "crossbuild")
	private EntityManager em;
	
	@EJB
	private VmManagerSbLocal vmm;
	
	@Override
	public Project createNewContainer(Project project) throws ServiceException {
		
		VmSetting vmSetting = em.find(VmSetting.class, project.getVmSetting().getId());
		if (vmSetting==null) {
			throw new ServiceException("Invalid vm settings id.");
		}
		
		Query query = em.createNamedQuery("LoadHistory.findMinLoad");
		query.setParameter("groupName", vmSetting.getGroupName());
		query.setMaxResults(1);
		
		List<Object[]> lhList = query.getResultList();
		if (lhList.size()==0) {
			throw new ServiceException("No slaves avalible");
		}
		LoadHistory lh = em.find(LoadHistory.class, lhList.get(0)[0]);
		
		NodeMetadata node = vmm.getNodeFromVm(lh.getVm());
		
		String cmd = vmSetting.getCreateContainerCmd();
		// %NAME% %IP% %PORT%
		String containerName = project.getProjectGroup().getUser().getId()+project.getProjectGroup().getName()+vmSetting.getGroupName();
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e1) {
			throw new ServiceException("MD5 alghoritm not found");
		}
		byte[] digest = md.digest(containerName.getBytes());
		
		containerName = Hex.encodeHexString(digest);
		containerName = "c"+containerName; //name cannot be numeric for jail
		cmd = cmd.replace("%NAME%", containerName);
		
		TypedQuery<Container> queryC = em.createNamedQuery("Container.findLatest", Container.class);
		queryC.setMaxResults(1);
		queryC.setParameter("vmId", lh.getVm().getId());
		List<Container> containers = queryC.getResultList();
		
		InetAddress nextIp = null;
		try {
			nextIp = InetAddress.getByName("192.168.0.1");
		} catch (UnknownHostException e) {
			throw new ServiceException("Could not parse initial IP");
		}
		Integer port = 10000;
		
		if (containers.size()>0) {
			try {
				Container latestContainer = containers.get(0);
				InetAddress lastIp = InetAddress.getByName(latestContainer.getIp());
				nextIp = InetAddresses.increment(lastIp);
				port = latestContainer.getPort()+1;
			}
			catch (IllegalArgumentException | UnknownHostException ex) {
				throw new ServiceException("Error creating new container, reason: invalid IP");
			}
		}
		
		cmd = cmd.replace("%IP%", InetAddresses.toAddrString(nextIp));
		
		if (port>65535) {
			throw new ServiceException("Error creating new container, reason: invalid port");
		}
		cmd = cmd.replace("%PORT%", port.toString());
		
		TypedQuery<Container> queryCF = em.createNamedQuery("Container.findByName", Container.class);
		queryCF.setParameter("name", containerName);
		if (queryCF.getResultList().size()>0) {
			throw new ServiceException("Project with this name already exists.");
		}
		
		logger.info("Creating new container with command: "+cmd);
		Integer exitStatus = vmm.executeScriptAsRoot(node, vmSetting, cmd);
		
		if (exitStatus!=0) {
			throw new ServiceException("Error creating a container.");
		}
		
		TypedQuery<KeyPair> queryK = em.createNamedQuery("KeyPair.findByName", KeyPair.class);
		queryK.setParameter("name", "crossbuild_container");
		KeyPair kp = queryK.getResultList().get(0);
		
		Container c = new Container();
		c.setIp(InetAddresses.toAddrString(nextIp));
		c.setName(containerName);
		c.setPort(port);
		c.setProject(project);
		c.setVm(lh.getVm());
		c.setKeyPair(kp);
		
		project.setContainer(c);
		
		return project;
		
	}
	
	@Override
	public void deleteContainer(Project project) throws ServiceException {
		NodeMetadata node = vmm.getNodeFromVm(project.getContainer().getVm());
		String delCmd = project.getVmSetting().getDeleteContainerCmd();
		delCmd = delCmd.replace("%NAME%", project.getContainer().getName());
		
		logger.info("Deleting container with command: "+delCmd);
		Integer status = vmm.executeScriptAsRoot(node, project.getVmSetting(), delCmd);
		if (status!=0) throw new ServiceException("Nonzero status on exit");
	}
}
