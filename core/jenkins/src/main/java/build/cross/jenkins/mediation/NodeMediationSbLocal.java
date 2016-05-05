package build.cross.jenkins.mediation;

import javax.ejb.Local;
import javax.ws.rs.core.Response;

import build.cross.models.jpa.Container;

@Local
public interface NodeMediationSbLocal {

	Response createNode(Container container);

	Response deleteNode(String nodeId);

}
