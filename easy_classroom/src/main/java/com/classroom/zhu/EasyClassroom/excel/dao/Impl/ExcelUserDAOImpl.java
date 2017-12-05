package com.classroom.zhu.EasyClassroom.excel.dao.Impl;

import cn.com.fanqy.common.service.MongoService;
import cn.com.fanqy.myblog.excel.dao.ExcelUserDAO;
import cn.com.fanqy.myblog.excel.dto.ExcelUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

import static cn.com.fanqy.common.service.Collections.EXCEL_TEST_USER;

@Component
public class ExcelUserDAOImpl extends MongoService implements ExcelUserDAO {
    private static final Logger LOG = LoggerFactory.getLogger(ExcelUserDAOImpl.class);

    private String collectionName = EXCEL_TEST_USER;
    @Override
    public List<ExcelUser> getUserList() {
        LOG.info("!!!!!");
        MongoTemplate template = factory.getMongoTemplateByDbName("test");
        Query query = new Query();
        List<ExcelUser> userList = template.find(query,ExcelUser.class,collectionName);
        LOG.info("!!!userList:"+userList);
        return userList;
    }
}