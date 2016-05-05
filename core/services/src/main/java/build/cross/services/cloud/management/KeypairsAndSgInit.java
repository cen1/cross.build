package build.cross.services.cloud.management;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import build.cross.models.enums.Arch;
import build.cross.models.enums.CloudProvider;
import build.cross.models.enums.Kernel;
import build.cross.models.jpa.Platform;
import build.cross.models.jpa.VmSetting;

@Stateless
public class KeypairsAndSgInit implements KeypairsAndSgInitLocal {
	
	private Logger logger = Logger.getLogger(VmInitializer.class.getSimpleName());
	
	@EJB
	private VmManagerSbLocal vmm;
	
	@PersistenceContext(unitName = "crossbuild")
	private EntityManager em;
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<VmSetting> createInitialVmSettings() {
		List<VmSetting> settingsInitial = new ArrayList<VmSetting>();
		logger.info("First run initiated, creating settings, keypairs and slaves");
		
		//define platforms
		Platform freebsd10 = new Platform("FreeBSD", Kernel.FREEBSD, Arch.amd64, "10.2-RELEASE");
		Platform ubuntu1404 = new Platform("Ubuntu", Kernel.LINUX, Arch.amd64, "14.04");
		em.persist(freebsd10);
		em.persist(ubuntu1404);
		
		//define provider specific settings
		VmSetting ec2_euc1_freebsd_64_10 = new VmSetting(
				"ec2-user", 
				"ami-9f5549f3", 
				"/home",
				"su - root -c \"chmod +x /home/ec2-user/bootstrap_ami-9f5549f3.sh && /home/ec2-user/bootstrap_ami-9f5549f3.sh\"",
				false, 
				true, 
				"ezjail.flavour",
				"su - root -c \"ezjail-admin create -f base %NAME% 'lo1|%IP%' && ezjail-admin start %NAME% && "+
				"echo 'rdr pass on xn0 proto tcp from any to $IP_PUB port %PORT% -> %IP% port 22' >> /etc/pf.conf &&"+
				"pfctl -f /etc/pf.conf\"",
				"su - root -c \"ezjail-admin stop %NAME% && ezjail-admin delete -w %NAME%\"",
				freebsd10,
				CloudProvider.EC2);

		VmSetting ec2_euc1_ubuntu_64_1404 = new VmSetting(
				"ubuntu", 
				"ami-87564feb", 
				"/home",
				"chmod +x /home/ubuntu/bootstrap_ami-87564feb.sh && sudo /home/ubuntu/bootstrap_ami-87564feb.sh", 
				true, 
				true, 
				"Dockerfile",
				"docker run -d -p %PORT%:22 --name %NAME% base",
				"docker rm -f %NAME%",
				ubuntu1404,
				CloudProvider.EC2);
		
		em.persist(ec2_euc1_ubuntu_64_1404);
		em.persist(ec2_euc1_freebsd_64_10);
		
		settingsInitial.add(ec2_euc1_ubuntu_64_1404);
		settingsInitial.add(ec2_euc1_freebsd_64_10);
		
		return settingsInitial;
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void createKeypairs() {
		// create main slave keypair if it does not yet exist
		if (!vmm.keyPairExists("crossbuild")) {
			try {
				vmm.createKeyPair("crossbuild");
			} catch (IOException e) {
				logger.severe("IOException creating first KeyPair " + e.getMessage());
			}
		}

		// create main container keypair if it does not yet exist
		if (!vmm.keyPairExists("crossbuild_container")) {
			try {
				vmm.createKeyPair("crossbuild_container");
			} catch (IOException e) {
				logger.severe("IOException creating first container KeyPair " + e.getMessage());
			}
		}
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void createSecurityGroups() {
		if (!vmm.securityGroupExists("crossbuild")) {
			vmm.createSecurityGroup("crossbuild");
		}
	}
}
