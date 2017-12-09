package com.classroom.zhu.EasyClassroom.service;
import com.classroom.zhu.EasyClassroom.dao.UserDAO;
import com.classroom.zhu.EasyClassroom.dto.UserCreateBean;
import com.classroom.zhu.common.model.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 12801 on 2017/11/27.
 */
@Service
public class UserService {
    @Autowired
    UserDAO userDAO;


    //1、注册用户
    public void addUser(ObjectId oId, UserCreateBean ccb){
        User user = new User();
        user.setOid(oId);
        user.setEmail(ccb.getEmail());
        userDAO.createUser(oId,user);
    }

    //2、根据邮箱返回User
    public User getUserByEmail(ObjectId oId, String email) {
        return userDAO.getUserByEmail(oId, email);
    }

    //1、注册用户
    public void addUser2(ObjectId oId, ObjectId tempId){
        userDAO.createUser2(oId,tempId);
    }

    //删除用户
    public void deleteContractorById(ObjectId oId, ObjectId id) {
        userDAO.deleteContractorById(oId,id);
    }
}
