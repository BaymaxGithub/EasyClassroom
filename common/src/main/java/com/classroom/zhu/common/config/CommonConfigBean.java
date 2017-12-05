package com.classroom.zhu.common.config;

import lombok.Data;

import java.util.Map;

/**
 * Created by 12801 on 2017/8/12.
 */
@Data
public class CommonConfigBean {
    private String id;
    private String name;
    private Map<String,Object> config;
    public Map<String,Object> getConfig(){
        return config;
    }
}
