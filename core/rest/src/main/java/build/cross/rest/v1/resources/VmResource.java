package build.cross.rest.v1.resources;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import build.cross.models.enums.LoadType;
import build.cross.models.jpa.Vm;
import build.cross.rest.v1.beans.LoadBean;
import build.cross.rest.v1.resources.common.CrudResource;
import build.cross.utils.MathUtil;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/public/vms")
@RequestScoped
public class VmResource extends CrudResource<Vm> {
	
	private Logger logger = Logger.getLogger(VmResource.class.getSimpleName()); 
	
	@GET
	public Response getList() {
		return super.getList();
	}
	
	@GET
	@Path("/{vmId}/load")
	public Response getVmload(@PathParam("vmId") String vmId, @QueryParam("loadType") LoadType loadType) {
		
		Query query = em.createNativeQuery("SELECT timeTable.dt, countTable.num FROM ("+
				"SELECT date_trunc('hour', CAST(myInterval AS timestamp)) AS dt FROM generate_series(CURRENT_TIMESTAMP - interval '4 hours', CURRENT_TIMESTAMP, interval '1 hour') myInterval"+
				") timeTable LEFT JOIN ("+
				"SELECT date_trunc('hour', l.created_at) AS dt, avg(l.value) AS num FROM crossbuild.load_history l "+
				"WHERE l.created_at>CURRENT_TIMESTAMP - interval '4 hours' AND vm_id=?1 AND type=?2 GROUP BY 1"+
				") countTable ON timeTable.dt=countTable.dt");
		
		query.setParameter(1, vmId);
		query.setParameter(2, loadType.toString());
		
		List<Object[]> results = query.getResultList();
		
		LoadBean lb = new LoadBean();
		DateFormat df = new SimpleDateFormat("HH");
		
		for (Object[] o : results) {
			//date
			Date d = (Date)(o[0]);
			String dateString = df.format(d);
			lb.getDates().add(dateString);
			
			//value
			Double val = (Double)o[1];
			if (val!=null)
				val=MathUtil.round(val, 2);
			lb.getValues().add(val);
		}
		
		return Response.ok(lb).build();
	}
}
