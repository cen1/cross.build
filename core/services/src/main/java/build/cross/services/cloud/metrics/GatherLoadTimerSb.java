package build.cross.services.cloud.metrics;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import build.cross.models.enums.LoadType;
import build.cross.models.jpa.LoadHistory;
import build.cross.models.jpa.Vm;
import build.cross.services.cloud.management.VmManagerSbLocal;

@Singleton
@Startup
public class GatherLoadTimerSb {
    
	private Logger logger = Logger.getLogger(GatherLoadTimerSb.class.getSimpleName());
	
	@PersistenceContext(unitName = "crossbuild")
	private EntityManager em;
	
	@EJB
    private VmManagerSbLocal vmm;
	
	private Client client;

	@PostConstruct
	private void init() {
		client = ClientBuilder.newClient();
	}
	
	@Schedule(minute="*/5", hour="*", persistent=false)
    public void updateLoadData() {
        logger.info("Gathering load information from all nodes");
        
        TypedQuery<Vm> query = em.createNamedQuery("Vm.findAll", Vm.class);
        List<Vm> vms = query.getResultList();
        
        for (Vm vm : vms) {
    		logger.info("Getting load info from ip "+vm.getIp());
    		
    		try {
        		//CPU
        		WebTarget target = client.target("http://"+vm.getIp()+":8080/v1/load/cpu");
                Response response = target.request().get();
                Double cpuValue = Double.parseDouble(response.readEntity(String.class));
                if (response.getStatus()!=200) {
                	logger.info("Unreachable "+vm.getIp());
                	continue;
                }
                
                //MEMORY
        		target = client.target("http://"+vm.getIp()+":8080/v1/load/memory");
                response = target.request().get();
                Double memoryValue = Double.parseDouble(response.readEntity(String.class));
                if (response.getStatus()!=200) {
                	logger.info("Unreachable "+vm.getIp());
                	continue;
                }
                
                //DISK
        		target = client.target("http://"+vm.getIp()+":8080/v1/load/disk");
                response = target.request().get();
                Double diskValue = Double.parseDouble(response.readEntity(String.class));
                if (response.getStatus()!=200) {
                	logger.info("Unreachable "+vm.getIp());
                	continue;
                }
                
                //save history
                LoadHistory lhCpu = new LoadHistory(LoadType.CPU, cpuValue, vm);
                LoadHistory lhMemory = new LoadHistory(LoadType.MEMORY, memoryValue, vm);
                LoadHistory lhDisk = new LoadHistory(LoadType.DISK, diskValue, vm);
        	
                em.persist(lhCpu);
                em.persist(lhMemory);
                em.persist(lhDisk);
    		} catch(Exception e) {
    			logger.info("Connection failed for "+vm.getIp());
    		}
        }
	}
}
