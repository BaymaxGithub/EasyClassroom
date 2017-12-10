package com.classroom.zhu.EasyClassroom.dao;

import com.classroom.zhu.EasyClassroom.dto.LoginBean;
import com.classroom.zhu.EasyClassroom.dto.UserCreateBean;
import com.classroom.zhu.common.model.TokenModel;
import com.classroom.zhu.common.model.User;
import jdk.nashorn.internal.parser.Token;
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
    public void createUser2(ObjectId oid, ObjectId tempId,UserCreateBean ucb);
    //6
    public void deleteContractorById(ObjectId oid, ObjectId upsertId);
    //7
    public Boolean checkUser(ObjectId oid, LoginBean loginBean);
    //8
    public void saveToken(ObjectId oid, TokenModel tokenModel);
}
