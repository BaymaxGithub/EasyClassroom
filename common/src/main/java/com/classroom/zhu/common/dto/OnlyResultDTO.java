package com.classroom.zhu.common.dto;

public class OnlyResultDTO {
    private Object result;

    //setter/getter
    public Object getResult() {
        return result;
    }
    public void setResult(Object result){ this.result = result;}

    //构造函数
    public OnlyResultDTO(Object result){
        this.result = result;
    }
    public OnlyResultDTO(){
    }

    public static OnlyResultDTO success(){
        return new OnlyResultDTO("success");
    }
    public static OnlyResultDTO failed(){
        return new OnlyResultDTO("failed");
    }
}
