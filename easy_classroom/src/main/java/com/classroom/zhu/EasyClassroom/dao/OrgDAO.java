package com.classroom.zhu.EasyClassroom.dao;

import org.bson.types.ObjectId;


/**
 * Created by 12801 on 2017/11/27.
 */
public interface OrgDAO {
    //1
    public ObjectId getOidByEmail(String email);
    //2
    //public List<ObjectId> getOidList();
}
