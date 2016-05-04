package build.cross.rest.v1.resources.common;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import com.github.tfaga.lynx.beans.QueryFilter;
import com.github.tfaga.lynx.beans.QueryParameters;
import com.github.tfaga.lynx.enums.FilterOperation;
import com.github.tfaga.lynx.utils.JPAUtils;

import build.cross.models.jpa.User;
import build.cross.models.jpa.common.BaseEntity;
import build.cross.models.jpa.common.BaseUserEntity;
import build.cross.rest.exceptions.ApiError;
import build.cross.rest.exceptions.ApiException;

import static build.cross.rest.v1.application.CrossBuildRest.queryDefaults;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CrudUserResource<T extends BaseUserEntity> {

    protected Class<T> type;

    @PersistenceContext(unitName = "crossbuild")
	protected EntityManager em;

    @Context
    protected Request request;

    @Context
    protected HttpServletRequest httpRequest;

    @Context
    protected UriInfo uriInfo;
    
    @Context
	protected SecurityContext sc;
    
    protected static final String HEADER_TOTAL_COUNT = "X-Total-Count";

    @SuppressWarnings("unchecked")
    public CrudUserResource() {

        Type superclass = getClass().getGenericSuperclass();

        if (superclass instanceof Class) {
            superclass = ((Class) superclass).getGenericSuperclass();
        }

        type = (Class<T>) (((ParameterizedType) superclass).getActualTypeArguments()[0]);
    }
        
    public Response getList(String userId) {

        QueryParameters query = queryDefaults.builder().queryEncoded(uriInfo.getRequestUri().getRawQuery()).build();
        query.getFilters().add(new QueryFilter("user.id", FilterOperation.EQ, userId));
        
        List<T> allObjects = JPAUtils.queryEntities(em, type, query);
        Long allObjectsCount = JPAUtils.queryEntitiesCount(em, type, query);

        return Response.ok(allObjects).header(HEADER_TOTAL_COUNT, allObjectsCount).build();
    }
    
    public Response getCount(String userId) {		

        QueryParameters query = queryDefaults.builder().queryEncoded(uriInfo.getRequestUri().getRawQuery()).build();
        query.getFilters().add(new QueryFilter("user.id", FilterOperation.EQ, userId));
        Long allObjectsCount = JPAUtils.queryEntitiesCount(em, type, query);

        return Response.ok(allObjectsCount).build();
    }

    public Response get(String userId, String id) throws ApiException {

        T object = em.find(type, id);

        if (object == null || object.getUser().getId().equals(userId)) {
            throw new ApiException(new ApiError(404, "Resource not found", type.getSimpleName(), id));
        }
        
        return Response.ok(object).build();
    }

    
    public Response create(String userId, T object) throws ApiException {
    	User u = em.find(User.class, userId);
		if (u == null) {
            throw new ApiException(new ApiError(404, "resource.not.found", User.class.getSimpleName()+" "+userId));
        }
		object.setUser(u);
        em.persist(object);

        return Response.ok(object).build();
    }

    protected T validateEntity(String userId, String id) throws ApiException {

        T entity = em.find(type, id);

        if (entity == null) {
            throw new ApiException(
                    new ApiError(404, "resource.not.found", type.getSimpleName()+" "+id));
        }
        if (!entity.getUser().getId().equals(userId)) {
        	throw new ApiException(new ApiError(403, "insufficient"));
        }
        
        return entity;
    }
    
    public Response update(String userId, String id, T object) throws ApiException {
    	T entity = validateEntity(userId, id);
    	
    	if (entity == null) {
    		throw new ApiException(new ApiError(404, "resource.not.found", type.getSimpleName()+" "+id));
        }
    	User t = em.find(User.class, userId);
    	if (t == null) {
            throw new ApiException(new ApiError(404, "resource.not.found", User.class.getSimpleName()+" "+userId));
        }
		object.setUser(t);
    	object.setId(id);
        em.merge(object);

        return Response.ok(object).build();
    }
}
