package com.cxg.weChat.crm.controller;

import com.cxg.weChat.core.utils.PageUtils;
import com.cxg.weChat.core.utils.Query;
import com.cxg.weChat.core.utils.R;
import com.cxg.weChat.crm.pojo.*;
import com.cxg.weChat.crm.service.PlanActivitySrevice;
import org.apache.poi.hssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;


/**
 * @Description 微信小程序领取数据报表
 * @Author xg.chen
 * @Date 14:43 2018/11/29
 */
@Controller
@RequestMapping("/data")
public class PlanActivityController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(PlanActivityController.class);

    @Autowired
    PlanActivitySrevice planActivitySrevice;

    @GetMapping("/updateData")
    @ResponseBody
    public String updateData(String detailId) {
        System.out.println(detailId);//137115
        List<String> detailIds = planActivitySrevice.getDetailIdList(detailId);
        if (detailIds != null) {
            for (int i = 0; i < detailIds.size(); i++) {
                List<TestDo> list = planActivitySrevice.getItemIdList(detailIds.get(i));
                if (list != null) {
                    int count;
                    for (TestDo test : list) {
                        TestDo2 test2 = new TestDo2();
                        test2.setItemId(test.getItemId());
                        test2.setActualSales(test.getActualSales());
                        count = planActivitySrevice.updateImptentByUpdate(test2);
                        if (count ==0 ) {
                            return  "更新失败";
                        }
                        count++;
                    }
                }
            }
        }
        return "更新成功";
    }

    /**
     * @Description 现场执行活动照片
     * @Author xg.chen
     * @Date 12:12 2018/12/5
     **/
    @GetMapping("/photodata")
    public String photoData() {
        return "data/photodata";
    }

    @GetMapping("/photoDataList")
    @ResponseBody
    public PageUtils photoDataList(@RequestParam Map<String ,Object> params) {
        Query query = new Query(params);
        List<WxPlanPhotoDo> planList = planActivitySrevice.getPlanPhotoList(query);
        if (planList != null) {
            for (WxPlanPhotoDo wxPlanPhoto: planList) {
                wxPlanPhoto.setPhotoUrl("/images/"+wxPlanPhoto.getPhotoUrl().replace("/files/",""));
            }
        }
        int count = planActivitySrevice.countPlanPhotoData(query);
        PageUtils pageUtils = new PageUtils(planList, count);
        return pageUtils;
    }

    /**
     * @Description 现场执行人员信息
     * @Author xg.chen
     * @Date 10:03 2018/12/5
     */
    @GetMapping("/admindata")
    public String adminData() {
        return "data/admindata";
    }

    /**
     * @Description 获取管理员的信息列表
     * @Author xg.chen
     * @Date 10:12 2018/12/5
     **/
    @GetMapping("/adminDataList")
    @ResponseBody
    public PageUtils adminDataList(@RequestParam Map<String ,Object> params) {
        Query query = new Query(params);
        List<WxAdminInfoDo> planList = planActivitySrevice.getPlanDoAdminList(query);
        int count = planActivitySrevice.countPlanAdminData(query);
        PageUtils pageUtils = new PageUtils(planList, count);
        return pageUtils;
    }

    /**
     * @Description 导出管理员的信息
     * @Author xg.chen
     * @Date 11:33 2018/12/5
     **/
    @GetMapping("/downloadAdmin")
    @ResponseBody
    public void downloadAdmin(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        params.put("offset", "0");
        params.put("limit", "9999999");
        Query query = new Query(params);
        List<WxAdminInfoDo> queryAll = planActivitySrevice.getPlanDoAdminList(query);
        //工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        OutputStream out = null;
        HSSFSheet sheet = workbook.createSheet("数据");
        //设置导出的表的名字
        String fileName = "推广活动管理员者数据.xls";
        //设置头
        int rowNum = 1;
        String[] headers = {"活动详细ID", "活动Id", "活动标题", "活动地址",
                "活动开始时间", "活动结束时间", "openid",
                "微信昵称", "微信头像", "签到地点", "签到时间"};
        HSSFRow row = sheet.createRow(0);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setFont(font);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
            cell.setCellStyle(style);

        }

        for (WxAdminInfoDo planActivity : queryAll) {
            HSSFRow rown = sheet.createRow(rowNum);
            rown.createCell(0).setCellValue(planActivity.getId());
            rown.createCell(1).setCellValue(planActivity.getPlanId());
            rown.createCell(2).setCellValue(planActivity.getPlanTitle());
            rown.createCell(3).setCellValue(planActivity.getPlanAddress());
            rown.createCell(4).setCellValue(planActivity.getPlanStartDate());
            rown.createCell(5).setCellValue(planActivity.getPlanEndDate());
            rown.createCell(6).setCellValue(planActivity.getOpenId());
            rown.createCell(7).setCellValue(planActivity.getNickName());
            rown.createCell(8).setCellValue(planActivity.getAvatarUrl());
            rown.createCell(9).setCellValue(planActivity.getSignAddress());
            rown.createCell(10).setCellValue(planActivity.getSignDate());
            rowNum++;
        }
        try {
            response.setContentType("application/msexcel");
            response.setHeader("Content-Disposition", "attachment; filename=\""
                    + new String(fileName.getBytes("GBK"), ("ISO8859-1"))
                    + ".xls\"");
            response.flushBuffer();
            out = response.getOutputStream();
            workbook.write(out);
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
    /**
     * @Description 领取数据页面跳转
     * @Author xg.chen
     * @Date 15:04 2018/11/29
     * @Param
     */
    @GetMapping("/data")
    public String index() {
        return "data/data";
    }

    /**
     * @Description 获取微信用户数据
     * @Date 13:52 2018/12/3
     * @Param
     */
    @GetMapping("/dataList")
    @ResponseBody
    public PageUtils dataList(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        List<PlanActivityDo> planList = planActivitySrevice.getPlanList(query);
        int count = planActivitySrevice.countPlanData(query);
        PageUtils pageUtils = new PageUtils(planList, count);
        return pageUtils;
    }

    /**
     * 根据条件下载数据
     *
     * @param params
     * @param response
     * @Author xg.chen
     */
    @GetMapping("/downloadData")
    @ResponseBody
    public void downloadData(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        params.put("offset", "0");
        params.put("limit", "9999999");
        Query query = new Query(params);
        List<PlanActivityDo> queryAll = planActivitySrevice.getPlanList(query);
        //工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        OutputStream out = null;
        HSSFSheet sheet = workbook.createSheet("数据");
        //设置导出的表的名字
        String fileName = "推广活动消费者数据.xls";
        //设置头
        int rowNum = 1;
        String[] headers = {"活动详细ID", "活动Id", "活动标题", "活动地址", "活动开始时间", "活动结束时间", "openid", "微信昵称", "微信头像", "状态"};
        HSSFRow row = sheet.createRow(0);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setFont(font);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
            cell.setCellStyle(style);

        }

        for (PlanActivityDo planActivity : queryAll) {
            HSSFRow rown = sheet.createRow(rowNum);
            rown.createCell(0).setCellValue(planActivity.getId());
            rown.createCell(1).setCellValue(planActivity.getPlanId());
            rown.createCell(2).setCellValue(planActivity.getPlanTitle());
            rown.createCell(3).setCellValue(planActivity.getPlanAddress());
            rown.createCell(4).setCellValue(planActivity.getPlanStartDate());
            rown.createCell(5).setCellValue(planActivity.getPlanEndDate());
            rown.createCell(6).setCellValue(planActivity.getOpenId());
            rown.createCell(7).setCellValue(planActivity.getNickName());
            rown.createCell(8).setCellValue(planActivity.getAvatarUrl());
            rown.createCell(9).setCellValue(planActivity.getStatus());

            rowNum++;
        }
        try {
            response.setContentType("application/msexcel");
            response.setHeader("Content-Disposition", "attachment; filename=\""
                    + new String(fileName.getBytes("GBK"), ("ISO8859-1"))
                    + ".xls\"");
            response.flushBuffer();
            out = response.getOutputStream();
            workbook.write(out);
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
