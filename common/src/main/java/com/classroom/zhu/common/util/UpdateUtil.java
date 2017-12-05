package com.classroom.zhu.common.util;

import com.classroom.zhu.common.jackson.EmbeddedObjectSerializer;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class UpdateUtil {
    public static UpdateUtil instance = new UpdateUtil();

    private UpdateUtil() {
        mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter();
        SimpleModule module = new SimpleModule("ObjectIdModule");
        EmbeddedObjectSerializer objectSerializer = new EmbeddedObjectSerializer();
        module.addSerializer(Date.class, objectSerializer);
        module.addSerializer(ObjectId.class, objectSerializer);
        mapper.registerModule(module);

        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setSerializationInclusion(Include.NON_NULL);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public ObjectMapper mapper;

    public static UpdateUtil getInstance() {
        return instance;
    }

    @SuppressWarnings({"unchecked"})
    public void convertDBObjectToUpdate(Update update, String prefix, Map<String, Object> object,
                                        String... exclude) {
        List<String> excludeList = Arrays.asList(exclude);
        Object value;
        for (String key : object.keySet()) {
            value = object.get(key);
            String fullKey = (prefix == null ? key : prefix + "." + key);
            if (excludeList.contains(fullKey)) {
                continue;
            }
            if (value instanceof Map) {
                convertDBObjectToUpdate(update, fullKey, (Map<String, Object>) value, exclude);
            } else {
                update.set(fullKey, object.get(key));
            }
        }
    }

    public Update convertDBObjectToUpdate(String prefix, Map<String, Object> object, String... exclude) {
        if (object == null || object.size() == 0) {
            return Update.update("update", new Date());
        }
        Update update = new Update();
        convertDBObjectToUpdate(update, prefix, object, exclude);
        return update;
    }

    @SuppressWarnings("unchecked")
    public static Update convertBeanToUpdate(Object bean, String... exclude) {
        Map<String, Object> object = getInstance().mapper.convertValue(bean, Map.class);
        return getInstance().convertDBObjectToUpdate(null, object, exclude);
    }

    public static Update from(Object bean, String... excludes) {
        return convertBeanToUpdate(bean, excludes);
    }
}
