package com.cxg.weChat.crm.service;

import com.cxg.weChat.core.utils.Query;
import com.cxg.weChat.crm.pojo.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description 获取微信活动数据之接口
 * @Author xg.chen
 * @Date 15:45 2018/11/29
**/
@Service
public interface PlanActivitySrevice {

    List<PlanActivityDo> getPlanList(Query query);

    int countPlanData(Query query);

    List<WxAdminInfoDo> getPlanDoAdminList(Query query);

    int countPlanAdminData(Query query);

    List<WxPlanPhotoDo> getPlanPhotoList(Query query);

    int countPlanPhotoData(Query query);

    List<TestDo> getItemIdList(String detailId);

    int updateImptentByUpdate(TestDo2 test2);

    List<String> getDetailIdList(String detailId);
}
