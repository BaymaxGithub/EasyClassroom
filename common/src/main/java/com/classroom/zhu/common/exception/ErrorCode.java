package com.classroom.zhu.common.exception;

public enum ErrorCode {

    //Http
    NOT_FOUND(404,"fanqy:Not Found"),

    //System Error
    SYSTEM_ERROR(10001, "fanqy:System error"),
    SERVICE_UNAVAILABLE(10002, "Service unavailable"),
    MISS_REQUIRED_PARAMETER(10016, "Miss required parameter (%s)"),
    PARAMETER_VALUE_INVALID(10017, "Parameter (%s)'s value invalid"),
    RESOURCE_ALREADY_EXISTS(20007, "Resource (%s) already exists"),
    RESOURCE_ALREADY_NOT_EXISTS(20008, "Resource (%s) not exists"),
    REGISTER_EMAIL_NULL(20009,"email is null."),
    EMAIL_ALREADY_EXISTS(20010,"sorry,email %s already  exists."),
    //oAuth error
    REDIRECT_URI_MISMATCH(21322, "Redirect uri mismatch"),
    INVALID_REQUEST(21323, "Invalid request");
    private  int errorCode;
    private  String error;

    public int getErrorCode(){return errorCode;}
    public void setErrorCode(int errorCode){this.errorCode=errorCode;}

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    private ErrorCode(int errorCode,String error){
        this.errorCode = errorCode;
        this.error = error;
    }

}
