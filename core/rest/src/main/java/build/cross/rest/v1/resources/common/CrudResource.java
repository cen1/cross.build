package build.cross.rest.v1.resources.common;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import com.github.tfaga.lynx.beans.QueryParameters;
import com.github.tfaga.lynx.utils.JPAUtils;

import build.cross.models.jpa.common.BaseEntity;
import build.cross.rest.v1.exceptions.ApiError;
import build.cross.rest.v1.exceptions.ApiException;

import static build.cross.rest.v1.application.CrossBuildRest.queryDefaults;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CrudResource<T extends BaseEntity> {

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
    public CrudResource() {

        Type superclass = getClass().getGenericSuperclass();

        if (superclass instanceof Class) {
            superclass = ((Class) superclass).getGenericSuperclass();
        }

        type = (Class<T>) (((ParameterizedType) superclass).getActualTypeArguments()[0]);
    }
        
    public Response getList() {

        QueryParameters query = queryDefaults.builder().queryEncoded(uriInfo.getRequestUri().getRawQuery()).build();

        List<T> allObjects = JPAUtils.queryEntities(em, type, query);
        Long allObjectsCount = JPAUtils.queryEntitiesCount(em, type, query);

        return Response.ok(allObjects).header(HEADER_TOTAL_COUNT, allObjectsCount).build();
    }
    
    public Response getCount() {		

        QueryParameters query = queryDefaults.builder().queryEncoded(uriInfo.getRequestUri().getRawQuery()).build();
        Long allObjectsCount = JPAUtils.queryEntitiesCount(em, type, query);

        return Response.ok(allObjectsCount).build();
    }

    public Response get(String id) throws ApiException {

        T object = em.find(type, id);

        if (object == null) {
            throw new ApiException(new ApiError(404, "Resource not found", type.getSimpleName(), id));
        }
        
        return Response.ok(object).build();
    }

    
    public Response create(T object) {
        em.persist(object);

        return Response.ok(object).build();
    }

    // From here on out are helper methods for easier request processing

    protected T validateEntity(String id) throws ApiException {

        T entity = em.find(type, id);

        if (entity == null) {
        	throw new ApiException(new ApiError(404, "Resource not found", type.getSimpleName(), id));
        }
        
        return entity;
    }
    
    public Response update(String id, T object) throws ApiException {
    	T entity = validateEntity(id);
    	
    	if (entity == null) {
    		throw new ApiException(new ApiError(404, "Resource not found", type.getSimpleName(), id));
        }
    	
    	object.setId(id);
        em.merge(object);

        return Response.ok(object).build();
    }
}
