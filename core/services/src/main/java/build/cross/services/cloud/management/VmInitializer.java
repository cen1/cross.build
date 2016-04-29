package build.cross.services.cloud.management;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.ejb3.annotation.TransactionTimeout;

import build.cross.jenkins.mediation.SecurityMediationSbLocal;
import build.cross.models.jpa.VmSetting;

@Singleton
@Startup
public class VmInitializer {

	@EJB
	private VmManagerSbLocal vmm;
	
	@EJB
	private KeypairsAndSgInitLocal kpsg;
	
	@EJB
	private SecurityMediationSbLocal jenkinsSecurity;

	private Logger logger = Logger.getLogger(VmInitializer.class.getSimpleName());

	@Resource
	private TimerService timerService;

	@PostConstruct
	private void init() {
		timerService.createTimer(15000, 600000);
	}
	
	@PersistenceContext(unitName = "crossbuild")
	private EntityManager em;

	@Timeout
	@TransactionTimeout(1500)
	private void firstRun(Timer timer) {
		
		List<VmSetting> settings = em.createNamedQuery("VmSetting.findAll", VmSetting.class).getResultList();
		
		if (settings.size() == 0) {
			settings = kpsg.createInitialVmSettings();
			kpsg.createKeypairs();
			kpsg.createSecurityGroups();
			
			jenkinsSecurity.addGlobalCredentials();
			
			settings.parallelStream().forEach(s -> vmm.createVm(s));
		}
		timer.cancel();
	}
}
