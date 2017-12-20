package com.classroom.zhu.EasyClassroom.dao;

import com.classroom.zhu.common.model.TokenModel;
import org.bson.types.ObjectId;

/**
 * Created by 12801 on 2017/12/10.
 */
public interface TokenDAO {
    //1
    public Boolean checkToken(ObjectId oid, ObjectId uid,TokenModel tokenModel);
}
