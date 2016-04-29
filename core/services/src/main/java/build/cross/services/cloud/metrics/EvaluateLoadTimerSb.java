package build.cross.services.cloud.metrics;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import build.cross.models.enums.LoadType;
import build.cross.models.jpa.VmSetting;
import build.cross.services.cloud.management.VmManagerSbLocal;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.LockType;

@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Singleton
@Startup
public class EvaluateLoadTimerSb {
    
	private Logger logger = Logger.getLogger(EvaluateLoadTimerSb.class.getSimpleName());
	
	private final Double CPU_TRESHOLD = 0.8;
	private final Double MEMORY_TRESHOLD = 0.99;
	private final Double DISK_TRESHOLD = 0.8;
	
	@PersistenceContext(unitName = "crossbuild")
	private EntityManager em;
	
	@EJB
    private VmManagerSbLocal vmm;
	
	@Lock(LockType.READ)
    @Schedule(minute="*/3", hour="*", persistent=false)
    public void evaluateLoad() {
    	logger.info("Evaluating load history");
    	
    	Calendar calendar = Calendar.getInstance();
    	calendar.add(Calendar.DATE, -7);
    	Date sevenDaysAgo = calendar.getTime();
    	
    	List<VmSetting> settings = em.createNamedQuery("VmSetting.findAll", VmSetting.class).getResultList();
    	
    	for (VmSetting vs : settings) {
			TypedQuery<Double> query1 = em.createNamedQuery("LoadHistory.getGroupAverage", Double.class);
			query1.setParameter("type", LoadType.CPU);
			query1.setParameter("group", vs.getGroupName());
			query1.setParameter("date", sevenDaysAgo);
			query1.setMaxResults(1);
			Double cpuAvg = query1.getSingleResult();
			if (cpuAvg == null) return;
			
			TypedQuery<Double> query2 = em.createNamedQuery("LoadHistory.getGroupAverage", Double.class);
			query2.setParameter("type", LoadType.MEMORY);
			query2.setParameter("group", vs.getGroupName());
			query2.setParameter("date", sevenDaysAgo);
			query2.setMaxResults(1);
			Double memoryAvg = query2.getSingleResult();
			if (memoryAvg == null) return;
			
			TypedQuery<Double> query3 = em.createNamedQuery("LoadHistory.getGroupAverage", Double.class);
			query3.setParameter("type", LoadType.DISK);
			query3.setParameter("group", vs.getGroupName());
			query3.setParameter("date", sevenDaysAgo);
			query3.setMaxResults(1);
			Double diskAvg = query3.getSingleResult();
			if (diskAvg == null) return;
						
			if (cpuAvg >= CPU_TRESHOLD || memoryAvg >= MEMORY_TRESHOLD || diskAvg >= DISK_TRESHOLD) {
				logger.info("Cpi: "+cpuAvg+", Mem: "+memoryAvg+", Disk: "+diskAvg);
				logger.info("Slave load is too big. Creating new instance.");
				vmm.createVm(vs);
			}
    	}   		
    }
}
