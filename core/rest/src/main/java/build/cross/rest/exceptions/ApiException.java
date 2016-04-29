package build.cross.rest.exceptions;

public class ApiException extends Exception {

	private static final long serialVersionUID = 1L;
	
	protected ApiError error;

    public ApiException(ApiError error) {
        this.error = error;
    }

    public ApiException(String message, ApiError error) {
        super(message);
        this.error = error;
    }

    public ApiException(String message, Throwable cause, ApiError error) {
        super(message, cause);
        this.error = error;
    }

    public ApiException(Throwable cause, ApiError error) {
        super(cause);
        this.error = error;
    }

    public ApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace,
                        ApiError error) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.error = error;
    }

    /**
     * <p>Gets the value of error and returns error.</p>
     *
     * @return Value of error.
     */
    public ApiError getError() {
        return error;
    }

    /**
     * <p>Sets the error.</p>
     *
     * <p>You can use getError() to get the value of error.</p>
     */
    public void setError(ApiError error) {
        this.error = error;
    }
}
