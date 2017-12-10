package com.classroom.zhu.common.model;

import lombok.Data;
import org.bson.types.ObjectId;

//用来在ABCDE_db数据库中保存所有用户的信息
@Data
public class UserDb {
    private String username;
    private ObjectId oid;
    private ObjectId uid;
    private String dbName;
    private String type; // phone or email
}