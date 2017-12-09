package com.classroom.zhu.EasyClassroom.controller;

import com.classroom.zhu.EasyClassroom.dao.OrgDAO;
import com.classroom.zhu.EasyClassroom.dao.UserDAO;
import com.classroom.zhu.EasyClassroom.dto.UserCreateBean;
import com.classroom.zhu.EasyClassroom.service.UserService;
import com.classroom.zhu.common.dto.OnlyResultDTO;
import com.classroom.zhu.common.exception.ErrorCode;
import com.classroom.zhu.common.exception.ErrorCodeException;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Created by 12801 on 2017/11/27.
 */

@RestController
@RequestMapping("api")
public class UserController {

    @Autowired
    OrgDAO orgdao;
    @Autowired
    UserDAO userDAO;
    @Autowired
    UserService userService;


    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    /**
     * 注册用户
     * 注意: 防止并发注册（最坏的情况是并发注册了同一个邮箱）
     */
    @RequestMapping(value = "/user",method = RequestMethod.POST)
    public
    @ResponseBody
    Object registerUser(
            @RequestParam(required = false,name = "org_email") String orgEmail,
            @RequestBody UserCreateBean ucb
    )throws IOException {
        if (ucb.getEmail() == null) {
            throw new ErrorCodeException(ErrorCode.REGISTER_EMAIL_NULL);
        }
        //ObjectId oid = orgdao.getOIdByEmail(orgEmail);    //注册的时候并没有用户存在，所以只能使用前端传来的email
        ObjectId oid = orgdao.getOidByEmail("1280157271@qq.com");    //注册的时候并没有用户存在，所以只能使用前端传来的email
        if (oid == null) {
            throw new ErrorCodeException(ErrorCode.INVALID_REQUEST);
        }
        //高并发出错可能：下面的检查发现没有aa@qq.com,但是线程切换，另一个用户注册aa@qq.com（不切换线程）
        //此时用户二会先注册成功，而第一个用户而后回复线程执行后也将用户注册到数据库中，就会重名
        //检查该邮箱是否已经注册( 防止高并发，健哥建议在检查是否已经存在的时候upsert占个位)
        /*if (userDAO.existsUser(oid,"email",ucb.getEmail())){
            throw new ErrorCodeException(ErrorCode.EMAIL_ALREADY_EXISTS,ucb.getEmail());
        }*/
        ObjectId tempId = userDAO.existsUserAndUpsert(oid,"email",ucb.getEmail());
        //将这个占位id和email绑定在一起使用
        if (tempId == null){  //说明upsert功能是更新,即邮箱被占用
            throw new ErrorCodeException(ErrorCode.EMAIL_ALREADY_EXISTS,ucb.getEmail());
        }else{ //是插入，并且同时会插入email,_id,repeat三个字段
            userService.addUser2(oid,tempId);
        }
        /*if (){
            throw new ErrorCodeException(ErrorCode.EMAIL_ALREADY_EXISTS,ucb.getEmail());
        }*/
        LOG.info("!!!tempId:"+tempId);
        LOG.info("!!!add user:"+ucb.getEmail());
        //userService.addUser(oid, ucb );
        return new OnlyResultDTO(userService.getUserByEmail(oid, ucb.getEmail()));
    }


    /**
     * 删除用户
     */
    @RequestMapping(value = "/user/{id}",method = RequestMethod.DELETE)
    public
    @ResponseBody
    void deleteUser(
            @PathVariable ObjectId id
    )throws IOException {
        if (id == null) {
            throw new ErrorCodeException(ErrorCode.PARAMETER_VALUE_INVALID);
        }
        LOG.info("Delete by id:"+id);
        ObjectId oId = orgdao.getOidByEmail("1280157271@qq.com");
        userService.deleteContractorById(oId,id);
    }
}
