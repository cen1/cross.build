package build.cross.services.beans;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.ejb.Stateless;

import build.cross.services.exceptions.ServiceException;
import build.cross.services.types.ProjectSettings;

/**
 * Session Bean implementation class ProjectSb
 */
@Stateless
public class ProjectSb implements ProjectSbLocal {

    public ProjectSb() {
        
    }
    
    private Logger logger = Logger.getLogger(ProjectSb.class.getSimpleName());
    
    public void addNew(URL repoUrl) throws ServiceException {
    	logger.info("Received new project with URL: "+repoUrl);
    	
    	ProjectSettings ps = new ProjectSettings();
    	
    	//validate url
    	ArrayList<String> tokens = validateUrl(repoUrl);
    	
    	int i = 0;
    	for (String token : tokens) {
    		if (i==1) ps.setProjectDescription(token);
    		if (i==2) ps.setProjectName(token.substring(0, -4));
    	}
    	
    	//get free vm
    	
    	//create jail
    	
    	//submit to jenkins
    }

	private ArrayList<String> validateUrl(URL url) throws ServiceException {
		String host = url.getHost();
    	String path = url.getPath();
    	
    	if (!host.equals("github.com") || !url.toString().endsWith(".git")) {
    		throw new ServiceException("Bad URL");
    	}
    	
    	String[] tokensArr = path.split("/");
    	ArrayList<String> tokens = new ArrayList<String>(Arrays.asList(tokensArr));
    	
    	if (tokens.size() != 3) {
    		throw new ServiceException("Bad URL");
    	}
    	
    	return tokens;
	}
}
