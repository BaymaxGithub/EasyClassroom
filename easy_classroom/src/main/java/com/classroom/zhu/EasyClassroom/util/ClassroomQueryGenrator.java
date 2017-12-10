package com.classroom.zhu.EasyClassroom.util;

import com.classroom.zhu.common.model.Classroom;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

/**
 * Created by 12801 on 2017/12/10.
 */
@Component
public class ClassroomQueryGenrator {
    //获取查询Contractor列表的查询条件
    public Query getQuery(Query query, Classroom cr){
        addName(query,cr.getBuilding_name());
        addNumber(query,cr.getNumber());
        addFreeDay(query,cr.getFree_day());
        addState(query,cr.getState());
        return query;
    }

    public void addName(Query query, String name) {
        if (name != null) {
            query.addCriteria(Criteria.where("building_name").is(name));
        }
    }
    public void addNumber(Query query, String number) {
        if (number != null) {
            query.addCriteria(Criteria.where("number").is(number));
        }
    }
    public void addFreeDay(Query query, Integer free_day) {
        if (free_day != null) {
            query.addCriteria(Criteria.where("free_day").is(free_day));
        }
    }

    public void addState(Query query, String state) {
        if (state != null) {
            query.addCriteria(Criteria.where("state").is(state));
        }
    }
}
