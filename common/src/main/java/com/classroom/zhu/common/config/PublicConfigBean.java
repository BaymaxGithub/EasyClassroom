package com.classroom.zhu.common.config;

import lombok.Data;

/**
 * Created by 12801 on 2017/8/12.
 */
@Data
public class PublicConfigBean {
    private String apiServerIP;//ip地址
    private int apiServerPort;//端口号

    //MongoDB
    private String mongodbHost;       //mongoDB的主机
    private int mongodbPort;          //mongoDB的端口号
    private String mongodbUsername;   //mongoDB用户名
    private String mongodbPassword;   //mongoDB密码

    public String getApiServerIP(){
        return apiServerIP;
    }
    public int getApiServerPort(){
        return apiServerPort;
    }
    public String getMongodbHost(){
        return mongodbHost;
    }
    public int getMongodbPort(){
        return mongodbPort;
    }
    public String getMongodbUsername(){
        return mongodbUsername;
    }
    public String getMongodbPassword(){
        return mongodbPassword;
    }
}
