package build.cross.services.cloud.containers;

import javax.ejb.Local;

import build.cross.models.jpa.Project;
import build.cross.services.exceptions.ServiceException;

@Local
public interface ContainerManagerSbLocal {

	Project createNewContainer(Project project) throws ServiceException;

	void deleteContainer(Project project) throws ServiceException;

}
