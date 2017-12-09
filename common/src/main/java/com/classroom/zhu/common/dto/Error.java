package com.classroom.zhu.common.dto;

import com.classroom.zhu.common.exception.ErrorCode;
import com.classroom.zhu.common.exception.ErrorCodeException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import lombok.Data;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import static com.classroom.zhu.common.exception.ErrorCode.*;
@Data
public class Error {
    private String request;
    @JsonProperty("error_code")
    private int errorCode;
    private String error;
    private Object[] params;

    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }
    //构造函数1
    public Error(){
    }
    //构造函数2
    public Error(String request,int errorCode,String error,Object... params){
        this.request = request;
        this.errorCode = errorCode;
        this.error = String.format(error,params);//格式化有点像c的打印函数，将他们两个拼在一起
        this.params = params;
    }
    //构造函数3
    public Error(String request, ErrorCode code, Object... params){
        this.request = request;
        this.errorCode = code.getErrorCode();
        this.error = String.format(code.getError(),params);
        this.params = params;
    }
    //构造函数4
    public Error(ErrorCode code, Object... params) {
        this.request = "";
        this.errorCode = code.getErrorCode();
        this.error = String.format(code.getError(), params);
        this.params = params;
    }

    private static Error handleServletRequestBindingException(ServletRequestBindingException ex){
        return new Error("",MISS_REQUIRED_PARAMETER.getErrorCode(),ex.getMessage());
    }

    //BindingResult代表数据绑定的结果，继承了Errors接口。
    //BindException：代表数据绑定的异常，它继承Exception，并实现了BindingResult
    private static Error handleBindingResult(BindingResult result){//BindingResult代表数据绑定的结果，继承了Errors接口。
        if(result.hasFieldErrors()){
            FieldError fieldError = result.getFieldError();
            if(fieldError.getCode().equals("NotNull")){
                return new Error(MISS_REQUIRED_PARAMETER,fieldError.getField());
            }else{
                System.out.println("fanqy:"+fieldError.getCode());
                return new Error(PARAMETER_VALUE_INVALID,fieldError.getCode());
            }
        }else if(result.hasGlobalErrors()){
            System.out.println("fanqy:"+result.getGlobalError().getDefaultMessage());
            return null;
        }else{
            return null;
        }
    }

    private static Error handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        return handleBindingResult(ex.getBindingResult());
    }

    private static Error handleBindException(BindException ex){
        return  handleBindingResult(ex.getBindingResult());
    }

    private static Error handleMissingServletRequestParameterException(MissingServletRequestParameterException ex){
        String parameterName = ex.getParameterName();
        return new Error(MISS_REQUIRED_PARAMETER,parameterName);
    }

    private static Error handleHttpMessageNotReadableException(HttpMessageNotReadableException ex){
        Throwable cause = ex.getCause();
        if(cause instanceof UnrecognizedPropertyException){
            return new Error(SYSTEM_ERROR,cause.getMessage());
        }else{
            if(cause instanceof JsonMappingException){
                JsonMappingException exception = (JsonMappingException) cause;
                if(exception.getPath().size() == 0){
                    return new Error(INVALID_REQUEST);
                }else{
                    String fieldName = exception.getPath().get(0).getFieldName();
                    return new Error(PARAMETER_VALUE_INVALID,fieldName);
                }
            }else if(cause instanceof JsonParseException){
                return new Error(INVALID_REQUEST);
            }else{
                return null;
            }
        }
    }

    private static Error handleTypeMismatchException(TypeMismatchException ex){
        Object value = ex.getValue();
        if(ex instanceof MethodArgumentTypeMismatchException){
            String name = ((MethodArgumentTypeMismatchException) ex).getName();
            return new Error(PARAMETER_VALUE_INVALID,name,value);
        }
        return new Error(PARAMETER_VALUE_INVALID,ex.getPropertyName(),value);
    }


    public static Error fromException(Exception ex) {
        if (ex instanceof TypeMismatchException) {
            return handleTypeMismatchException((TypeMismatchException) ex);
        } else if (ex instanceof HttpMessageNotReadableException) {
            return handleHttpMessageNotReadableException((HttpMessageNotReadableException) ex);
        } else if (ex instanceof MissingServletRequestParameterException) {
            return handleMissingServletRequestParameterException((MissingServletRequestParameterException) ex);
        } else if (ex instanceof BindException) {
            return handleBindException((BindException) ex);
        } else if (ex instanceof MethodArgumentNotValidException) {
            return handleMethodArgumentNotValidException((MethodArgumentNotValidException) ex);
        } else if (ex instanceof ErrorCodeException) {
            ErrorCodeException ex2 = (ErrorCodeException) ex;
            return ex2.getError();
        } else if (ex instanceof HttpMediaTypeNotSupportedException) {
            return new Error(INVALID_REQUEST);
        } else if (ex instanceof OAuthProblemException) {
            Error error = new Error(INVALID_REQUEST);
            error.setError(((OAuthProblemException) ex).getDescription());
            return error;
        } else if (ex instanceof ServletRequestBindingException) {
            return handleServletRequestBindingException((ServletRequestBindingException) ex);
        } else if (ex instanceof HttpRequestMethodNotSupportedException){
            return new Error(SERVICE_UNAVAILABLE);
        } else {
            return null;
        }
    }



}
