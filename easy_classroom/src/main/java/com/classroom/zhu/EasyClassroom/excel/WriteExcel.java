package com.classroom.zhu.EasyClassroom.excel;

import cn.com.fanqy.myblog.excel.dao.ExcelUserDAO;
import cn.com.fanqy.myblog.excel.dto.ExcelUser;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by 12801 on 2017/11/12.
 *参考：http://www.jianshu.com/p/7041aa03c086
 */
@Component
public class WriteExcel {
    @Autowired
    ExcelUserDAO excelUserDAO;
    private static final Logger LOG= LoggerFactory.getLogger(WriteExcel.class);



    //该方法直接输出到输出流
    public  void writeToOutputStream(OutputStream outputStream){
        //1、初始化一个workbook 工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        List<ExcelUser> userList = excelUserDAO.getUserList();
        LOG.info("userList:"+userList);
        //2、创建单个sheet
        HSSFSheet sheet = workbook.createSheet("sheet0");
        //3、创建多行
        //创建第一行，设置列名
        HSSFRow row0 = sheet.createRow(0);
        for (int cellIndex = 0;cellIndex<4;cellIndex++){ //每行有多少个格子
            HSSFCell cell = row0.createCell(cellIndex);
            switch (cellIndex){
                case 0:
                    cell.setCellValue("num");
                    break;
                case 1:
                    cell.setCellValue("name");
                    break;
                case 2:
                    cell.setCellValue("age");
                    break;
                case 3:
                    cell.setCellValue("degree");
                    break;

            }
        }
        //第一行创建结束
        //创建剩余行
        for (int rowIndex = 1;rowIndex<=userList.size();rowIndex++){
            HSSFRow row = sheet.createRow(rowIndex);
            ExcelUser user = userList.get(rowIndex-1);
            //创建每行的列
            for (int cellIndex=0;cellIndex<4;cellIndex++){
                HSSFCell cell = row.createCell(cellIndex);
                switch (cellIndex){
                    case 0:
                        cell.setCellValue(rowIndex);
                        break;
                    case 1:
                        cell.setCellValue(user.getName());
                        break;
                    case 2:
                        cell.setCellValue(user.getAge());
                        break;
                    case 3:
                        cell.setCellValue(user.getDegree());
                        break;
                }
            }
        }
        //剩余行创建完毕
        try {
            workbook.write(outputStream);
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    //设置response的参数
    public void responseSetProperties(
            String fileName,
            HttpServletResponse response) throws UnsupportedEncodingException {
        // 设置文件后缀
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm");
        String fn = fileName + sdf.format(new Date()).toString() + ".xls";
        // 读取字符编码
        String utf = "UTF-8";
        // 设置响应
        response.setContentType("application/ms-txt.numberformat:@");
        //response.setContentType("application/csv");
        // response.setContentType("text/csv");
        //response.setContentType("APPLICATION/OCTET-STREAM");
        response.setCharacterEncoding(utf);
        /*response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "max-age=30");*/
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fn, "UTF-8"));
    }


}
