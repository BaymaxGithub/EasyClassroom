package com.classroom.zhu.common.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.util.Date;

import static com.classroom.zhu.common.util.DateUtil.compatibilityDate;


/**
 * Created by 12801 on 2017/8/11.
 */

@ReadingConverter
public class LongToDateConverter implements Converter<Long,Date> {
    @Override
    public Date convert(Long source){
        if(source != null){
            return compatibilityDate(source);
        }
        return null;
    }
}
