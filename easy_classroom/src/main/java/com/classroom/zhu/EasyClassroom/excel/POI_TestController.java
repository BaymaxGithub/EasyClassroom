package com.classroom.zhu.EasyClassroom.excel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by 12801 on 2017/11/20.
 */
@Controller
@RequestMapping("api")
public class POI_TestController {
    @Autowired
    WriteExcel writeExcel;
    private static final Logger LOG= LoggerFactory.getLogger(POI_TestController.class);
    public static final String separator = File.separator;

    /**
     * 方式一：直接指定保存路径，保存到本地硬盘
     */
    @RequestMapping(value = "/excel/test/file",method = RequestMethod.GET)
    public void get(){

        OutputStream out = null;
        try{
            String path = "C:"+separator+"test"+separator+"export.xls";
            out = new FileOutputStream(new File(path));
            writeExcel.writeToOutputStream(out);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        } finally{
            if(out !=null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }//finally
    }


    /**
     * 方式二：请求该接口即可下载Excel文件文件
     */
    @RequestMapping(value = "/excel/test/stream",method = RequestMethod.GET,produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public void downLoadByStream(
            HttpServletResponse response
    ){
        try (final OutputStream out = response.getOutputStream()) {
            writeExcel.responseSetProperties("ExcelTest_",response);
            writeExcel.writeToOutputStream(out);
        }catch (IOException e){
            e.printStackTrace();
            LOG.info("IOException!!!");
        }
    }



}
