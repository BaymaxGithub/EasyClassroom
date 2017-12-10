package com.classroom.zhu.common.exception;
import com.classroom.zhu.common.dto.Error;
public class ErrorCodeException extends RuntimeException{

    private Error error;
    public Error getError() {
        return error;
    }
    public void setError(Error error) {
        this.error = error;
    }

    public ErrorCodeException(Error error) {
        super(error.getError());
        this.error = error;
    }
    public ErrorCodeException(ErrorCode code, Object... params) {
        this(new Error(code, params));
    }

}
