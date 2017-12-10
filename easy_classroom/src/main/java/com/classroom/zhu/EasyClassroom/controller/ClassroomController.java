package com.classroom.zhu.EasyClassroom.controller;

import com.classroom.zhu.EasyClassroom.dao.ClassroomDAO;
import com.classroom.zhu.EasyClassroom.dto.LoginBean;
import com.classroom.zhu.EasyClassroom.service.UserService;
import com.classroom.zhu.common.dto.BasicResultDTO;
import com.classroom.zhu.common.dto.OnlyResultDTO;
import com.classroom.zhu.common.exception.ErrorCode;
import com.classroom.zhu.common.exception.ErrorCodeException;
import com.classroom.zhu.common.model.Classroom;
import com.classroom.zhu.common.model.TokenModel;
import com.classroom.zhu.common.model.User;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created by 12801 on 2017/12/10.
 */
@Controller
@RequestMapping("api/classroom")
public class ClassroomController {

    private static final Logger LOG = LoggerFactory.getLogger(ClassroomController.class);

    @Value("#{config.project.secretKey}")
    String secretKey;

    @Autowired
    ClassroomDAO classroomDAO;
    @Autowired
    UserService userService;

    /**
     * 新增教室信息
     * 校验接口使用者的权限
     */
    @RequestMapping(value = "",method = RequestMethod.POST)
    public
    @ResponseBody
    void addClassroom(
            @RequestParam(required = true,name = "oid") ObjectId oid,
            @RequestParam(required = true,name = "token") String token,
            @RequestBody Classroom classroom
    )throws IOException {
        String role = userService.getRoleByToken(token);
        if (!role.equals("admin"))
        {
            throw new ErrorCodeException(ErrorCode.INVALID_REQUEST);
        }else {
            LOG.info("!!!add classroom");
            classroom.setState("available");  //新增时默认可用Available / Appointment
            classroomDAO.addClasstoom(oid,classroom);
        }
    }

    /**
     * 删除教室 做权限验证
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public
    @ResponseBody
    void deleteUser(
            @RequestParam(required = true,name = "oid") ObjectId oid,
            @RequestParam(required = true,name = "token") String token,
            @PathVariable ObjectId id
    )throws IOException {
        String role = userService.getRoleByToken(token);
        if (!role.equals("admin"))
        {
            throw new ErrorCodeException(ErrorCode.INVALID_REQUEST);
        }
        if (id == null) {
            throw new ErrorCodeException(ErrorCode.PARAMETER_VALUE_INVALID);
        }
        LOG.info("Delete classroom by id:{}",id);
        classroomDAO.deleteClassroomById(oid,id);
    }


    /**
     * 获取教室列表  不做权限验证
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public
    @ResponseBody
    Object getClassroomList(
            @RequestParam(value = "oid", required = true) ObjectId oid,
            @RequestParam(value = "token",required = true) String token,
            @RequestParam(value = "cursor",required = false,defaultValue="0") int cursor,
            @RequestParam(value = "limit",required = false,defaultValue="30") int limit,

            @RequestParam(value = "building_name",required = false) String building_name,
            @RequestParam(value = "number",required = false) String number,
            @RequestParam(value = "free_day",required = false) Integer free_day,
            @RequestParam(value = "state",required = false) String state
    ) {         //email不传时默认为@即可匹配所有的email值
        BasicResultDTO resultDTO = new BasicResultDTO();
        Classroom queryClassroom = new Classroom();
        queryClassroom.setBuilding_name(building_name);
        queryClassroom.setNumber(number);
        queryClassroom.setState(state);
        queryClassroom.setFree_day(free_day);


        List<Classroom> result= classroomDAO.getClassroomList(oid,queryClassroom,cursor,limit);
        long total = classroomDAO.getClassroomCount(oid, queryClassroom);

        resultDTO.setTotal(total);
        resultDTO.setCursor(cursor);
        resultDTO.setLimit(limit);
        resultDTO.setResult(result);
        return resultDTO;
    }


}
