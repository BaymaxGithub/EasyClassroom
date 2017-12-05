package com.classroom.zhu.EasyClassroom.csv;

/**
 * Created by 12801 on 2017/11/10.
 */
public class CreateCsvFile {

    /**
     * 购买订单CSV
     * OrderParamsVo 传入的参数
     */
    /*@RequestMapping("findBuyCSV")
    @ResponseBody*/
  /*  public JsonResult findBuyCSV(OrderParamsVo params, HttpServletResponse response) {

        List<Map<String, Object>> dataList=null；
        Validator.getInstance().validate(params);
        List<OrderBo> orderBos = orderService.findBuyCSV(params);// 查询到要导出的信息
        if (orderBos.size() == 0) {
            JsonResult.buildFailedResult("无数据导出");
        }
        String sTitle = "投资日期,订单号,姓名,购买产品,金额,状态";
        String fName = "buywater_";
        String mapKey = "createDate,no,realname,productName,money,state";
        dataList = new ArrayList<>();
        Map<String, Object> map = null;
        for (OrderBo order : orderBos) {
            map = new HashMap<String, Object>();
            map.put("createDate", DateFormatUtils.format(order.getCreateDate(), "yyyy/MM/dd HH:mm"));
            map.put("no", order.getNo());
            map.put("realname", order.getUserName());
            map.put("productName", order.getProductName());
            map.put("money", order.getMoney());
            map.put("state", order.getState());

            dataList.add(map);
        }
        try (final OutputStream os = response.getOutputStream()) {
            ExportUtil.responseSetProperties(fName, response);
            ExportUtil.doExport(dataList, sTitle, mapKey, os);
            return null;

        } catch (Exception e) {
            logger.error("购买CSV失败", e);

        }
        return JsonResult.buildFailedResult("数据导出出错");
    }*/
}
