package build.cross.rest.v1.application;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import build.cross.rest.exceptions.ApiError;

@Provider
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {

    @Override
    public Response toResponse(RuntimeException exception) {

        ApiError e = new ApiError(500, exception.getMessage());

        return Response.status(e.getStatus()).entity(e).build();
    }
}