package com.classroom.zhu.EasyClassroom.dao.impl;

import cn.com.fanqy.common.model.User;
import cn.com.fanqy.common.service.MongoService;
import cn.com.fanqy.common.util.DateUtil;
import cn.com.fanqy.common.util.UpdateUtil;
import cn.com.fanqy.myblog.dao.UserDAO;
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

import static cn.com.fanqy.common.service.Collections.USERS;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by 12801 on 2017/11/27.
 */
@Component
public class UserDAOImpl extends MongoService implements UserDAO {
    private String collectionName = USERS;

    private static final Logger LOG = LoggerFactory.getLogger(UserDAOImpl.class);


    //1
    @Override
    public boolean existsUser(ObjectId oId, String field, Object value){
        Assert.notNull(field);
        Assert.notNull(value);
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
        Assert.notNull(field);
        Assert.notNull(value);
        MongoTemplate template = factory.getMongoTemplateByOid(oId);

        ////////////////upsert在有满足条件的记录存在时，不会返回新的_id，并且如果update不设置的话会删除
        //_id以外的属性，不存在时返回_id
        //所有 if(tempid == null) 回滚（保证记录不被更新）       //mongodb没有回滚
        Query query = Query.query(Criteria.where(field).is(value));
        //Update update = new Update().set("repeat",new Date());
        Update update = new Update().setOnInsert("email",value);
        ObjectId tempId = (ObjectId)template.upsert(query, update, collectionName).getUpsertedId();
        LOG.info("!!!!upsert:"+tempId);
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
        Assert.notNull(field);
        Assert.notNull(value);
        Query query = new Query();
        MongoTemplate template = factory.getMongoTemplateByOid(oId);
        query.addCriteria(Criteria.where(field).is(value));
        User user = template.findOne(query, User.class, collectionName);
        return user;
    }

    //5
    public void createUser2(ObjectId oid, ObjectId tempId){
        MongoTemplate template = factory.getMongoTemplateByOid(oid);
        Query query = Query.query(Criteria.where("_id").is(tempId));
        User user = new User(); //只需要更新时间就可以了
        user.setCreateTime(new Date(DateUtil.getUTCms()));  //获取时间戳来创建时间，0828更新Contractor的字段属性Long为Data
        user.setUpdateTime(new Date(DateUtil.getUTCms()));  //0828更新Contractor的字段属性Long为Data
        //template.save(user, collectionName);
        template.updateFirst(query, UpdateUtil.convertBeanToUpdate(user, "_id", "email"), collectionName);
        LOG.info("!!! add Contractor ");
    }

    //6
    public void deleteContractorById(ObjectId oid, ObjectId upsertId){
        MongoTemplate template = factory.getMongoTemplateByOid(oid);
        template.remove(query(where("_id").is(upsertId)), collectionName);
    }
}
