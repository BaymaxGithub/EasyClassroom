package com.classroom.zhu.EasyClassroom.controller;

import com.classroom.zhu.EasyClassroom.dao.OrgDAO;
import com.classroom.zhu.EasyClassroom.dao.UserDAO;
import com.classroom.zhu.EasyClassroom.dto.LoginBean;
import com.classroom.zhu.EasyClassroom.dto.UserCreateBean;
import com.classroom.zhu.EasyClassroom.service.UserService;
import com.classroom.zhu.common.dto.OnlyResultDTO;
import com.classroom.zhu.common.exception.ErrorCode;
import com.classroom.zhu.common.exception.ErrorCodeException;
import com.classroom.zhu.common.model.TokenModel;
import com.classroom.zhu.common.model.User;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

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
    @Value("#{config.project.secretKey}")
    String secretKey;

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    /**
     * 注册用户
     * 注意: 防止并发注册（最坏的情况是并发注册了同一个邮箱）
     */
    @RequestMapping(value = "/user",method = RequestMethod.POST)
    public
    @ResponseBody
    Object registerUser(
            @RequestParam(required = true,name = "oid") ObjectId oid,
            @RequestBody UserCreateBean ucb
    )throws IOException {
        if (ucb.getEmail() == null) {
            throw new ErrorCodeException(ErrorCode.REGISTER_EMAIL_NULL,"email");
        }

        //高并发出错可能：下面的检查发现没有aa@qq.com,但是线程切换，另一个用户注册aa@qq.com（不切换线程）
        //此时用户二会先注册成功，而第一个用户而后回复线程执行后也将用户注册到数据库中，就会重名
        //检查该邮箱是否已经注册( 防止高并发，健哥建议在检查是否已经存在的时候upsert占个位)

        ObjectId tempId = userDAO.existsUserAndUpsert(oid,"email",ucb.getEmail());
        LOG.info("!!!tempId:{}",tempId);
        //将这个占位id和email绑定在一起使用
        if (tempId == null){  //说明upsert功能是更新,即邮箱被占用
            throw new ErrorCodeException(ErrorCode.EMAIL_ALREADY_EXISTS,ucb.getEmail());
        }else{ //是插入，并且同时会插入email,_id,repeat三个字段
            userService.addUser2(oid,tempId,ucb);
        }
        LOG.info("!!!add user:{}",ucb.getEmail());
        //userService.addUser(oid, ucb );
        return new OnlyResultDTO(userService.getUserByEmail(oid, ucb.getEmail()));
    }

    /**
     * 用户登录
     */
    @RequestMapping(value = "/user/login",method = RequestMethod.POST)
    public
    @ResponseBody
    Object login(
            @RequestParam(required = true,name = "oid") ObjectId oid,
            @RequestBody LoginBean loginBean
    )throws IOException {
        if ((loginBean.getEmail() == null) || loginBean.getPassword()==null ) {
            throw new ErrorCodeException(ErrorCode.REGISTER_EMAIL_NULL,"email or password");
        }
        if (userService.checkUser(oid,loginBean)){  //验证通过，生成token
            User user = userService.getUserByEmail(oid, loginBean.getEmail());
            TokenModel tokenModel = new TokenModel();
            tokenModel.setUid(user.getId());
            tokenModel.setToken(UUID.randomUUID().toString());

            userService.saveToken(oid,tokenModel);
            return new OnlyResultDTO(tokenModel);

        }
        throw new ErrorCodeException(ErrorCode.LOGIN_FAILED);
    }


    /**
     * 删除用户
     */
    @RequestMapping(value = "/user/{id}",method = RequestMethod.DELETE)
    public
    @ResponseBody
    void deleteUser(
            @RequestParam(required = true,name = "oid") ObjectId oid,
            @PathVariable ObjectId id
    )throws IOException {
        if (id == null) {
            throw new ErrorCodeException(ErrorCode.PARAMETER_VALUE_INVALID);
        }
        LOG.info("Delete by id:{}",id);
        userService.deleteContractorById(oid,id);
    }
}
