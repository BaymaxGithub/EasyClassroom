package com.classroom.zhu.common.exception;
import com.classroom.zhu.common.dto.Error;
/**
 * Created by 12801 on 2017/8/10.
 */
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
