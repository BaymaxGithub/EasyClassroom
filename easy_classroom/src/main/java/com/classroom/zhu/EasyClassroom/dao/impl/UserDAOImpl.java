package com.classroom.zhu.EasyClassroom.dao.impl;

import com.classroom.zhu.EasyClassroom.dao.UserDAO;
import com.classroom.zhu.EasyClassroom.dto.LoginBean;
import com.classroom.zhu.EasyClassroom.dto.UserCreateBean;
import com.classroom.zhu.common.model.TokenModel;
import com.classroom.zhu.common.model.User;
import com.classroom.zhu.common.service.MongoService;
import com.classroom.zhu.common.util.DateUtil;
import com.classroom.zhu.common.util.UpdateUtil;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Date;

import static com.classroom.zhu.common.service.Collections.TOKEN;
import static com.classroom.zhu.common.service.Collections.USERS;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by 12801 on 2017/11/27.
 */
@Component
public class UserDAOImpl extends MongoService implements UserDAO {
    private String collectionName = USERS;
    private String tokenCollection = TOKEN;

    private static final Logger LOG = LoggerFactory.getLogger(UserDAOImpl.class);


    //1
    @Override
    public boolean existsUser(ObjectId oId, String field, Object value){

        Query query = new Query();
        MongoTemplate template = factory.getMongoTemplateByOid(oId);
        query.addCriteria(Criteria.where(field).is(value));
        //这里考虑如何占个位
        ////////////////
        Query query2 = Query.query(Criteria.where("email").is(value));
        Update update = new Update();
        template.upsert(query, update, User.class);
        /////////////////
        return template.exists(query,collectionName);
    }

    //1//这里考虑如何占个位
    @Override
    public ObjectId existsUserAndUpsert(ObjectId oId, String field, Object value){

        MongoTemplate template = factory.getMongoTemplateByOid(oId);

        ////////////////upsert在有满足条件的记录存在时，不会返回新的_id，并且如果update不设置的话会删除
        //_id以外的属性，不存在时返回_id
        //所有 if(tempid == null) 回滚（保证记录不被更新）       //mongodb没有回滚
        Query query = Query.query(Criteria.where(field).is(value));
        //Update update = new Update().set("repeat",new Date());
        Update update = new Update().setOnInsert("email",value);
        ObjectId tempId = (ObjectId)template.upsert(query, update, collectionName).getUpsertedId();
        /////////////////
        return tempId;
    }
    //2
    @Override
    public void createUser(ObjectId oid, User user){
        MongoTemplate template = factory.getMongoTemplateByOid(oid);
        user.setCreateTime(new Date(DateUtil.getUTCms()));  //获取时间戳来创建时间，0828更新Contractor的字段属性Long为Data
        user.setUpdateTime(new Date(DateUtil.getUTCms()));  //0828更新Contractor的字段属性Long为Data
        template.save(user, collectionName);
        LOG.info("!!! add Contractor ");
    }

    //3
    public User getUserByEmail(ObjectId oId, String email){
        Assert.notNull(oId);
        Assert.notNull(email);

        return getUser(oId, "email", email);
    }

    //4
    @Override
    public User getUser(ObjectId oId, String field, Object value) {

        Query query = new Query();
        MongoTemplate template = factory.getMongoTemplateByOid(oId);
        query.addCriteria(Criteria.where(field).is(value));
        User user = template.findOne(query, User.class, collectionName);
        return user;
    }

    //5
    public void createUser2(ObjectId oid, ObjectId tempId,UserCreateBean ucb){
        MongoTemplate template = factory.getMongoTemplateByOid(oid);
        Query query = Query.query(Criteria.where("_id").is(tempId));
        User user = new User(); //只需要更新时间就可以了
        user.setCreateTime(new Date(DateUtil.getUTCms()));  //获取时间戳来创建时间，0828更新Contractor的字段属性Long为Data
        user.setUpdateTime(new Date(DateUtil.getUTCms()));  //0828更新Contractor的字段属性Long为Data
        user.setPassword(ucb.getPassword());
        user.setOid(oid);
        user.setRoleType("user");  //注册的用户角色都是 “user”
        template.updateFirst(query, UpdateUtil.convertBeanToUpdate(user, "_id", "email"), collectionName);
        LOG.info("!!! add Contractor done.");
    }

    //6
    public void deleteContractorById(ObjectId oid, ObjectId upsertId){
        MongoTemplate template = factory.getMongoTemplateByOid(oid);
        template.remove(query(where("_id").is(upsertId)), collectionName);
    }

    //7
    @Override
    public Boolean checkUser(ObjectId oid, LoginBean loginBean){
        MongoTemplate template = factory.getMongoTemplateByOid(oid);
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(loginBean.getEmail()).and("password").is(loginBean.getPassword()));
        return template.exists(query,collectionName);
    }

    //8
    public void saveToken(ObjectId oid, TokenModel tokenModel){
        MongoTemplate template = factory.getMongoTemplateByOid(oid);
        tokenModel.setTimestamp(new Date(DateUtil.getUTCms()));
        template.save(tokenModel, tokenCollection);   //每次请求都生成新的code保存起来，不替换
    }
}
