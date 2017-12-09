package com.classroom.zhu.common.model;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
public class User {
    @Id
    private ObjectId id;   //这个就是uid
    private ObjectId oid;

    private String email;
    private String name;
    private String phone;
    private Date createTime;
    private Date updateTime;

    private Date repeat;  //有人重复注册时更新
}
