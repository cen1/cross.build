package build.cross.rest.v1.resources;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import build.cross.models.jpa.VmSetting;
import build.cross.rest.v1.resources.common.CrudResource;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/public/vmsettings")
@RequestScoped
public class VmSettingResource extends CrudResource<VmSetting> {
	
	@GET
	public Response getList() {
		return super.getList();
	}
}
