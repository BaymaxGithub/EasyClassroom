package com.classroom.zhu.EasyClassroom.dao;

import cn.com.fanqy.common.model.User;
import org.bson.types.ObjectId;

/**
 * Created by 12801 on 2017/11/27.
 */
public interface UserDAO {
     //1
    public boolean existsUser(ObjectId oId, String field, Object value);
    //1
    public ObjectId existsUserAndUpsert(ObjectId oId, String field, Object value);
    //2
    public void createUser(ObjectId oid, User user);
    //3
    public User getUserByEmail(ObjectId oId, String email);
    //4
    public User getUser(ObjectId oId, String field, Object value);
    //5
    public void createUser2(ObjectId oid, ObjectId tempId);
    //6
    public void deleteContractorById(ObjectId oid, ObjectId upsertId);
}
