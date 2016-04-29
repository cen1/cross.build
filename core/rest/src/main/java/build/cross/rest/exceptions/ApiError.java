package build.cross.rest.exceptions;

public class ApiError {

    protected Integer status;
    protected String code;
    protected String description;
    protected String moreInfo;

    public ApiError(Integer status, String description) {
        this.status = status;
        this.code = "";
        this.description = description;
    }
    
    public ApiError(Integer status, String code, String description) {
        this.status = status;
        this.code = code;
        this.description = description;
    }

    public ApiError(Integer status, String code, String description, String moreInfo) {
        this.status = status;
        this.code = code;
        this.description = description;
        this.moreInfo = moreInfo;
    }

    /**
     * <p>Gets the value of status and returns status.</p>
     *
     * @return Value of status.
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * <p>Sets the status.</p>
     *
     * <p>You can use getStatus() to get the value of status.</p>
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * <p>Gets the value of code and returns code.</p>
     *
     * @return Value of code.
     */
    public String getCode() {
        return code;
    }

    /**
     * <p>Sets the code.</p>
     *
     * <p>You can use getCode() to get the value of code.</p>
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * <p>Gets the value of description and returns description.</p>
     *
     * @return Value of description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * <p>Sets the description.</p>
     *
     * <p>You can use getDescription() to get the value of description.</p>
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * <p>Gets the value of moreInfo and returns moreInfo.</p>
     *
     * @return Value of moreInfo.
     */
    public String getMoreInfo() {
        return moreInfo;
    }

    /**
     * <p>Sets the moreInfo.</p>
     *
     * <p>You can use getMoreInfo() to get the value of moreInfo.</p>
     */
    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }
}