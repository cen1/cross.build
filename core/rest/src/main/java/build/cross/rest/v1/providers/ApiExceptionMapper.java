package build.cross.rest.v1.providers;

import java.util.logging.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import build.cross.rest.v1.exceptions.ApiError;
import build.cross.rest.v1.exceptions.ApiException;

@Provider
public class ApiExceptionMapper implements ExceptionMapper<ApiException> {

	private Logger logger = Logger.getLogger(ApiExceptionMapper.class.getSimpleName());
	
    @Override
    public Response toResponse(ApiException exception) {

        ApiError e = exception.getError();
        logger.info("heyo");
        return Response.status(e.getStatus()).entity(e).build();
    }
}