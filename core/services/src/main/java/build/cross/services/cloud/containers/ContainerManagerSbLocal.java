package build.cross.services.cloud.containers;

import javax.ejb.Local;

import build.cross.models.jpa.Container;
import build.cross.models.jpa.Project;
import build.cross.models.jpa.VmSetting;
import build.cross.services.exceptions.ServiceException;

@Local
public interface ContainerManagerSbLocal {

	Project createNewContainer(Project project) throws ServiceException;

}
