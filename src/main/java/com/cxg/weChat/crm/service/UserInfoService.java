package com.cxg.weChat.crm.service;

import com.cxg.weChat.crm.pojo.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @Description:    后端服务接口
* @Author:         Cheney Master
* @CreateDate:     2018/11/5 10:20
* @Version:        1.0
*/
@Service
public interface UserInfoService {

    UserInfoDo getUserInfoById(int i);

    PlanActivityDo getPlanActivityById(PlanActivityDo planActivityDo);

    int getWxUserInfoById(WxUserInfoDo wxUserInfoDo);

    void creatWxUserInfo(WxUserInfoDo wxUserInfoDo);

    List<PlanActivityDo> getPlanActivityList(String openId);

    int checkAdminRole(String code);

    int creatWxAdminSignInfo(WxAdminInfoDo wxAdminInfoDo);

    List<WxUserInfoDo> getUserInfoByPlanId(String planId);

    String findUserInfoStatus(WxUserInfoDo wxUserInfoDo);

    int updateUserInfoStatus(WxUserInfoDo wxUserInfoDo);

    int creatWxAdminPhoto(WxPlanPhotoDo wxPlanPhoto);

    int getUserInfoCountByPlanId(String planId);
}
