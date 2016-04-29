package build.cross.rest.v1.application;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import build.cross.rest.exceptions.ApiError;
import build.cross.rest.exceptions.ApiException;

@Provider
public class ApiExceptionMapper implements ExceptionMapper<ApiException> {

    @Override
    public Response toResponse(ApiException exception) {

        ApiError e = exception.getError();

        return Response.status(e.getStatus()).entity(e).build();
    }
}
