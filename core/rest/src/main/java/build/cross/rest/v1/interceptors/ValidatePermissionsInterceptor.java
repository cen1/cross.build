package build.cross.rest.v1.interceptors;

import java.security.Principal;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import build.cross.models.jpa.User;
import build.cross.rest.v1.exceptions.ApiError;
import build.cross.rest.v1.exceptions.ApiException;

@ValidatePermissions
@Interceptor
public class ValidatePermissionsInterceptor {
	
	private static final Logger logger = Logger.getLogger(ValidatePermissionsInterceptor.class.getName());
	
	@PersistenceContext(unitName = "crossbuild")
	protected EntityManager em;
	
    @Inject
    private HttpServletRequest request;
    
    @AroundInvoke
    public Object checkUser(InvocationContext context) throws Exception {

    	logger.info("ValidatePermissions interceptor");
    	
    	String pathInfo = request.getPathInfo();
    	String[] pathParts = pathInfo.split("/");
    	if (pathParts.length < 2) {
    		throw new ApiException(new ApiError(400, "Invalid path"));
    	}
    	String userKey = pathParts[1];
    	String userVal = pathParts[2];
    	
    	Principal principal = request.getUserPrincipal();
    	
    	User user = em.find(User.class, principal.getName());
    	if (user == null) {
        	user = new User();
        	user.setId(principal.getName());
        	em.persist(user);
    	}
        
    	if (!userVal.equals(principal.getName()) || !userKey.equals("users")) {
    		throw new ApiException(new ApiError(403, "Insufficient rights"));
    	}
        
        return context.proceed();
    }
}