package com.classroom.zhu.common.dto;
public class BasicResultDTO {
    private long total;    //记录符合条件的总数
    private int cursor;    //相当于在符合条件的集合中要跳过的记录数
    private int limit;     //在符合条件的集合中规定返回的记录数
    private Object result; //结果放在这里

    //构造函数

    public BasicResultDTO(){

    }
    public BasicResultDTO(long total, int cursor, int limit, Object result) {
        this.total = total;
        this.cursor = cursor;
        this.limit = limit;
        this.result = result;
    }

    public int getCursor() {
        return cursor;
    }

    public int getLimit() {
        return limit;
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * @return the result
     */
    public Object getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(Object result) {
        this.result = result;
    }

    /**
     * @return the total
     */
    public long getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(long total) {
        this.total = total;
    }


}