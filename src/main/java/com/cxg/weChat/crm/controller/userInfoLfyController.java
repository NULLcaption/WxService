package com.cxg.weChat.crm.controller;

import com.cxg.weChat.core.utils.JSONUtils;
import com.cxg.weChat.crm.pojo.PlanActivityDo;
import com.cxg.weChat.crm.pojo.UserInfoDo;
import com.cxg.weChat.crm.pojo.WxUserInfoDo;
import com.cxg.weChat.crm.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
* @Description:    后台基础类
* @Author:         Cheney Master
* @CreateDate:     2018/11/5 9:53
* @Version:        1.0
*/
@Controller
@RequestMapping("/api/userInfo/lfy")
public class userInfoLfyController {
    private static Logger logger = LoggerFactory.getLogger(userInfoLfyController.class);

    @Autowired
    UserInfoService userInfoService;

    @GetMapping("/userInfo")
    @ResponseBody
    public String creatWxUserInfo(String openId, String avatarUrl, String nickName, String id) {
        //保存至数据数据中
        WxUserInfoDo wxUserInfoDo = new WxUserInfoDo();
        wxUserInfoDo.setOpenId(openId);
        wxUserInfoDo.setAvatarUrl(avatarUrl);
        wxUserInfoDo.setNickName(nickName);
        wxUserInfoDo.setActivityId(id);
        wxUserInfoDo.setStatus("N");
        //先根据id和openId检查是否存在该用户
        int num = userInfoService.getWxUserInfoById(wxUserInfoDo);
        if (num == 0) {//没有的话就插入
            userInfoService.creatWxUserInfo(wxUserInfoDo);
        } else {
            //查看是不是已经已领取的状态
            String status = userInfoService.getWxUserInfoById4Status(wxUserInfoDo);
            if ("Y".equals(status)) {
                return "error";
            }
        }
        return "success";
    }

    @GetMapping("/plans")
    @ResponseBody
    public String getPlanActivityList(String openId) {
        List<PlanActivityDo> planActivityDoList = userInfoService.getPlanActivityList(openId);
        String json = JSONUtils.beanToJson(planActivityDoList);
        return json;
    }

    @GetMapping("/plan")
    @ResponseBody
    public  String getPlanActivityById(String QRCode) {
        //处理获取到的二维码
        String[] strs = QRCode.split("-");
        String id = strs[0].toString();
        String planId = strs[1].toString();
        //从后台获取数据
        PlanActivityDo planActivityDo = new PlanActivityDo();
        planActivityDo.setId(id);
        planActivityDo.setPlanId(planId);

        planActivityDo = userInfoService.getPlanActivityById(planActivityDo);
        if (planActivityDo != null) {
            if (planActivityDo.getPlanStates().equals("0")) {
                planActivityDo.setPlanStates("简单派送");
            }
            if (planActivityDo.getPlanStates().equals("1")) {
                planActivityDo.setPlanStates("集赞派送");
            }
            if (planActivityDo.getPlanStates().equals("2")) {
                planActivityDo.setPlanStates("分享派送");
            }
            String[] string = planActivityDo.getPlanPhotoUrl().split(",");
            planActivityDo.setUrls(string);
        }
        String json = JSONUtils.beanToJson(planActivityDo);

        return json;
    }
}
