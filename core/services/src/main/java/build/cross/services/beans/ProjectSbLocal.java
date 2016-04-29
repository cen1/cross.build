package build.cross.services.beans;

import java.net.URL;

import javax.ejb.Local;

import build.cross.services.exceptions.ServiceException;

@Local
public interface ProjectSbLocal {
	
	public void addNew(URL repoUrl) throws ServiceException;
}
