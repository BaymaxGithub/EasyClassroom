package com.classroom.zhu.EasyClassroom.dao.impl;

import com.classroom.zhu.EasyClassroom.dao.TokenDAO;
import com.classroom.zhu.common.model.TokenModel;
import com.classroom.zhu.common.service.MongoService;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import static com.classroom.zhu.common.service.Collections.TOKEN;

/**
 * Created by 12801 on 2017/12/10.
 */
@Component
public class TokenDAOImpl  extends MongoService implements TokenDAO{
    private static final Logger LOG = LoggerFactory.getLogger(TokenDAOImpl.class);
    private String tokenCollection = TOKEN;
    //1
    @Override
    public Boolean checkToken(ObjectId oid, ObjectId uid, TokenModel tokenModel) {
        Query query = new Query();
        MongoTemplate template = factory.getMongoTemplateByOid(oid);
        query.addCriteria(Criteria.where("uid").is(tokenModel.getUid()).
                and("token").is(tokenModel.getToken()));
        return template.exists(query,tokenCollection);
    }
}
