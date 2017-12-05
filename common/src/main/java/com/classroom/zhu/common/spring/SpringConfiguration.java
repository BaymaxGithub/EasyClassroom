package com.classroom.zhu.common.spring;


import com.classroom.zhu.common.config.Config;
import com.classroom.zhu.common.jackson.ObjectMapperFactory;
import com.classroom.zhu.common.service.MongoFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.http.MediaType;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by 12801 on 2017/8/11.
 */

@Configuration
@ComponentScan("com.classroom.zhu")
//Spring的@PropertySource注解主要是让Spring的Environment接口读取属性配置文件用的，这个注解是标识在@Configuration配置类上的
@PropertySource("classpath:mongo.properties")    //谁加载了这个配置文件这个classpath就是那个类所在的路径
public class SpringConfiguration {
    private static Logger LOG = LoggerFactory.getLogger(SpringConfiguration.class);

    @Value("${config.mongo.host}")  //从属性文件中注入属性进来${}是属性占位符。#{}是SpEL
    String host;

    @Value("${config.mongo.port}")
    int port;

    @Value("${config.mongo.db}")
    String dbName;

    @Value("${config.mongo.table}")
    String collection;

    @Value("${config.mongo.username}")
    String username;

    @Value("${config.mongo.password}")
    String password;

    @Value("${config.mongo.name}")
    String name;

     //相关的bean
    @Bean
    public ObjectMapperFactory getObjectMapperFactory() {
        return new ObjectMapperFactory();
    }
    @Bean
    public ObjectMapper objectMapper(ObjectMapperFactory factory){
        return factory.getMapper();
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(ObjectMapper objectMapper){
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }

    @Bean
    public HttpMessageConverters customConverters(MappingJackson2HttpMessageConverter jackson){
        BufferedImageHttpMessageConverter bihm = new BufferedImageHttpMessageConverter();
        bihm.setDefaultContentType(MediaType.IMAGE_JPEG);

        return new HttpMessageConverters(false, Arrays.asList(new ResourceHttpMessageConverter(),
                bihm,
                new StringHttpMessageConverter(),
                new ByteArrayHttpMessageConverter(),
                jackson
                ));
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper){
        Jackson2JsonMessageConverter conv = new Jackson2JsonMessageConverter();
        conv.setJsonObjectMapper(objectMapper);
        return conv;
    }

    //mongo数据库,获取MongoTemplate所需的MongoDBFactory
    @Bean
    public MongoDbFactory mongoDbFactory()throws UnknownHostException {  //MongoCredential证书，凭借
        MongoCredential credential = MongoCredential.createCredential(username,dbName,password.toCharArray());
        ArrayList<MongoCredential> credentials = Lists.newArrayList(credential);
        ServerAddress address = new ServerAddress(host, port);
        MongoClient mongo = new MongoClient(address,credentials);
        //DB db = mongoClient.getDB("mydb");
        return new SimpleMongoDbFactory(mongo,dbName);
        //MongoTemplate的另一组重要构造方法中，有一个是必须的：MongoDBFactory，这是一个接口，定义返回DB实例的方法：
        //SimpleMongoDbFactory，是MongoDbFactory的简单实现。 这个类以Mongo实例为参数，可以设置数据库名作为第二个可选参数；
    }

    @Bean
    public Config config(MongoDbFactory mongoDbFactory)throws IOException {//MongoDbFactory在上面的Bean中造出来
        MongoTemplate template = new MongoTemplate(mongoDbFactory);
        System.out.println("看这里SpringConfiguration.java:120,看一下template:"+template);
        LOG.info("日志-zhu:init config bean of {} from {}",name,collection);
        return new Config(template,collection,name);  //name传过去与projectName相对
    }

    @Bean
    public MongoClient mongoClient(Config config)throws UnknownHostException{
        return config.createMongoClient();
    }

    @Bean   //Mongo类会在将来的版本中被MongoClient替换，所以这里返回的是MongoClient
    public Mongo mongo(MongoClient client){return client;}

    @Bean
    public MongoFactory mongoFactory(MongoClient mongo, ApplicationContext cxt){
        return new MongoFactory(mongo);
    }

    /*@Bean
    public ConnectionFactory rabbitConnectionFactory(Config config) {}*/




/*    ByteArrayHttpMessageConverter: 负责读取二进制格式的数据和写出二进制格式的数据；
    StringHttpMessageConverter：   负责读取字符串格式的数据和写出二进制格式的数据；
    ResourceHttpMessageConverter：负责读取资源文件和写出资源文件数据；
    FormHttpMessageConverter：       负责读取form提交的数据（能读取的数据格式为 application/x-www-form-urlencoded，不能读取multipart/form-data格式数据）；负责写入application/x-www-from-urlencoded和multipart/form-data格式的数据；
    MappingJacksonHttpMessageConverter:  负责读取和写入json格式的数据；
    SouceHttpMessageConverter：                   负责读取和写入 xml 中javax.xml.transform.Source定义的数据；
    Jaxb2RootElementHttpMessageConverter:  负责读取和写入xml 标签格式的数据；
    AtomFeedHttpMessageConverter:              负责读取和写入Atom格式的数据；
    RssChannelHttpMessageConverter:           负责读取和写入RSS格式的数据；*/
    @Bean
    @Lazy
    public RestTemplate restTemplate(MappingJackson2HttpMessageConverter jackson2HttpMessageConverter){
        return new RestTemplate(Lists.newArrayList(
                new ResourceHttpMessageConverter(),
                new ByteArrayHttpMessageConverter(),
                jackson2HttpMessageConverter
        ));
    }


}
