package com.cxg.weChat.crm.service.impl;

import com.cxg.weChat.crm.mapper.UserInfoMapper;
import com.cxg.weChat.crm.pojo.*;
import com.cxg.weChat.crm.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @Description:    服务类接口实现类
* @Author:         Cheney Master
* @CreateDate:     2018/11/5 10:25
* @Version:        1.0
*/

@Transactional
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    public UserInfoDo getUserInfoById(int id) {
        return userInfoMapper.getUserInfoById(id);
    }

    @Override
    public PlanActivityDo getPlanActivityById(PlanActivityDo planActivityDo) {
        try{
            return userInfoMapper.getPlanActivityById(planActivityDo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getWxUserInfoById(WxUserInfoDo wxUserInfoDo) {
        try{
            return userInfoMapper.getWxUserInfoById(wxUserInfoDo);
        } catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void creatWxUserInfo(WxUserInfoDo wxUserInfoDo) {
        try{
            userInfoMapper.creatWxUserInfo(wxUserInfoDo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<PlanActivityDo> getPlanActivityList(String openId) {
        try {
            return userInfoMapper.getPlanActivityList(openId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int checkAdminRole(String code) {
        try {
            return userInfoMapper.checkAdminRole(code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int creatWxAdminSignInfo(WxAdminInfoDo wxAdminInfoDo) {
        try {
            userInfoMapper.creatWxAdminSignInfo(wxAdminInfoDo);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<WxUserInfoDo> getUserInfoByPlanId(String planId) {
        try{
            return userInfoMapper.getUserInfoByPlanId(planId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String findUserInfoStatus(WxUserInfoDo wxUserInfoDo) {
        try {
            return userInfoMapper.findUserInfoStatus(wxUserInfoDo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int updateUserInfoStatus(WxUserInfoDo wxUserInfoDo) {
        try {
            userInfoMapper.updateUserInfoStatus(wxUserInfoDo);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int creatWxAdminPhoto(WxPlanPhotoDo wxPlanPhoto) {
        try{
            userInfoMapper.creatWxAdminPhoto(wxPlanPhoto);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int getUserInfoCountByPlanId(String planId) {
        try {
            return userInfoMapper.getUserInfoCountByPlanId(planId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public String getWxUserInfoById4Status(WxUserInfoDo wxUserInfoDo) {
        try {
            return userInfoMapper.getWxUserInfoById4Status(wxUserInfoDo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
