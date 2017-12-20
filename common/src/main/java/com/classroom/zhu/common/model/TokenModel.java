package com.classroom.zhu.common.model;

import lombok.Data;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by 12801 on 2017/12/10.
 */

@Data
public class TokenModel {
    //用户id
    private ObjectId uid;
    private String token;
    private Date timestamp;
}
