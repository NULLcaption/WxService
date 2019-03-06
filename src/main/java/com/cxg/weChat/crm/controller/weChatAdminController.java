package com.cxg.weChat.crm.controller;

import com.cxg.weChat.core.config.BootdoConfig;
import com.cxg.weChat.core.utils.HttpUtil;
import com.cxg.weChat.core.utils.JSONUtils;
import com.cxg.weChat.crm.pojo.WxAdminInfoDo;
import com.cxg.weChat.crm.pojo.WxPlanPhotoDo;
import com.cxg.weChat.crm.pojo.WxUserInfoDo;
import com.cxg.weChat.crm.service.UserInfoService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * @Description: 微信小程序后台服务类
 * @Author: Cheney Master
 * @CreateDate: 2018/10/30 11:39
 * @Version: 1.0
 */

@Controller
        @RequestMapping("/api/wx/admin")
public class weChatAdminController {
    private static Logger logger = LoggerFactory.getLogger(weChatAdminController.class);

    private String appid = "wxe581c3b3928cfece";
    private String appSercret = "8cf3f824d625e516181db2b506d6db60";
    private String grant_type = "authorization_code";
    private String requestUrl = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    BootdoConfig weChatConfig;

    /**
     * @Description: 管理员上传图片并保存
     * @param request
     * @param planId
     * @param openId
     * @return
     */
    @RequestMapping("/upload")
    @ResponseBody
    public String upload(HttpServletRequest request, String planId, String openId, String index) {
        String flagFile = uploadImageFiles(request,planId,index);
        if (flagFile.equals("error")) {
            return "error";
        } else {
            WxPlanPhotoDo wxPlanPhoto = new WxPlanPhotoDo();
            wxPlanPhoto.setActivityId(planId);
            wxPlanPhoto.setOpenId(openId);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String nowStr = sf.format(new Date());
            wxPlanPhoto.setCreartDate(nowStr);
            wxPlanPhoto.setPhotoUrl(flagFile);

            int num = userInfoService.creatWxAdminPhoto(wxPlanPhoto);
            if (num == 0) {
                return "error";
            }
        }
        return "success";
    }

    /**
     * @Description 图片上传至服务器
     * @Author xg.chen
     * @Date 8:56 2018/12/5
     **/
    public String uploadImageFiles(HttpServletRequest request,String planId,String index) {
        MultipartHttpServletRequest req =(MultipartHttpServletRequest)request;
        MultipartFile multipartFile =  req.getFile("file");
        String realPath = weChatConfig.getUploadPath()+"/plan";
        String imageUrl;
        try {
            File dir = new File(realPath);
            if (!dir.exists()) {
                dir.mkdir();
            }
            long time = System.currentTimeMillis();
            imageUrl  = "/files/"+planId+"_"+index+"_"+ String.valueOf(time)+".jpg";
            File file  =  new File(realPath,planId+"_"+index+"_"+ String.valueOf(time)+".jpg");
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return "error";
        }
        return imageUrl;
    }

    /**
     * @param address   签到地址
     * @param date      签到时间
     * @param openid    openId
     * @param avatarUrl 微信头像
     * @param nickName  微信昵称
     * @param planId    活动详细ID
     * @return String
     * @Description: 管理员签到
     * @Author: Cheney Master
     * @CreateDate: 2018/11/1 15:15
     * @Version: 1.0
     */
    @RequestMapping("/sigin")
    @ResponseBody
    public String userSigin(String address, String date, String openid, String avatarUrl, String nickName, String planId) {
        WxAdminInfoDo wxAdminInfoDo = new WxAdminInfoDo();
        wxAdminInfoDo.setOpenId(openid);
        wxAdminInfoDo.setAvatarUrl(avatarUrl);
        wxAdminInfoDo.setNickName(nickName);
        wxAdminInfoDo.setSignAddress(address);
        wxAdminInfoDo.setSignDate(date);
        wxAdminInfoDo.setActivityId(planId);

        int num = userInfoService.creatWxAdminSignInfo(wxAdminInfoDo);
        if (num == 0) {
            return "error";
        }
        return "success";
    }

    /**
     * 扫码之前验证用户是否领取过了
     * @param QRCode
     * @param openid
     * @return
     */
    @RequestMapping("/confirm")
    @ResponseBody
    public String checkCcomfrim(String QRCode,String openid) {
        //处理获取到的二维码
        String[] strs = QRCode.split("-");
        String id = strs[0].toString();
        WxUserInfoDo wxUserInfoDo = new WxUserInfoDo();
        wxUserInfoDo.setOpenId(openid);
        wxUserInfoDo.setActivityId(id);
        String status = userInfoService.findUserInfoStatus(wxUserInfoDo);
        if (status.equals("Y")){
            return "success";
        }
        return "error";
    }

    /**
     * @Description: 验证用户领取奖品的二维码
     * @Author: Cheney Master
     * @CreateDate: 2018/11/1 15:15
     * @Version: 1.0
     */
    @RequestMapping("/userInfo")
    @ResponseBody
    public String checkUserQRCode(String scanCodeInfo, String planId) {
        //将传入的openid去后台数据中验证是否领取过了
        WxUserInfoDo wxUserInfoDo = new WxUserInfoDo();
        wxUserInfoDo.setOpenId(scanCodeInfo);
        wxUserInfoDo.setActivityId(planId);
        String status = userInfoService.findUserInfoStatus(wxUserInfoDo);
        if (status.equals("N")){
            int num = userInfoService.updateUserInfoStatus(wxUserInfoDo);
            if (num == 0) {
                return "error";//用户已领取
            }
        }
        return "success";//用户未领取
    }

    /**
     * @Description: 根据活动的id获取参加活动的用户的列表
     * @Author: Cheney Master
     * @CreateDate: 2018/11/8 16:09
     * @Version: 1.0
     */
    @GetMapping("/userInfos")
    @ResponseBody
    public String getUserInfoByPlanId(String planId) {
        List<WxUserInfoDo> wxUserInfoDoList = userInfoService.getUserInfoByPlanId(planId);
        String json = JSONUtils.beanToJson(wxUserInfoDoList);
        return json;
    }

    /**
     * 根据活动的id获取参加活动的用户的数量
     * @param planId
     * @return
     */
    @GetMapping("/userCount")
    @ResponseBody
    public String getUserInfoCountByPlanId(String planId) {
        int count = userInfoService.getUserInfoCountByPlanId(planId);
        return String.valueOf(count);
    }

    /**
     * @Description: 验证管理员权限
     * @Author: Cheney Master
     * @CreateDate: 2018/11/1 11:29
     * @Version: 1.0
     */
    @RequestMapping("/adminCode")
    @ResponseBody
    public String checkAdminRole(String code, String openid) {
        logger.debug("验证码{}：" + code);
        logger.debug("验证人的openid{}：" + openid);
        //获取后台数据中提前随机生成的验证码
        int num = userInfoService.checkAdminRole(code);
        if (String.valueOf(num) != null) {
            return String.valueOf(num);
        } else {
            return "error";
        }
    }

    /**
     * @Description: 通过java后台获取微信的相关的信息
     * @Author: Cheney Master
     * @CreateDate: 2018/10/30 13:44
     * @Version: 1.0
     */
    @RequestMapping("/weChatInfo")
    @ResponseBody
    public Map<String, Object> getSession(String code) {
        return this.getSessionByCode(code);
    }

    /**
     * @Description: 通过java后台获取函数
     * @Author: Cheney Master
     * @CreateDate: 2018/10/30 13:30
     * @Version: 1.0
     */
    private Map<String, Object> getSessionByCode(String code) {
        String url = requestUrl + "?appid=" + appid + "&secret=" + appSercret + "&js_code=" + code + "&grant_type=" + grant_type;
        //http发送请求
        String data = HttpUtil.get(url);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> json = null;
        try {
            json = mapper.readValue(data, Map.class);
            logger.debug("返回的json数据{}：", json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
}
