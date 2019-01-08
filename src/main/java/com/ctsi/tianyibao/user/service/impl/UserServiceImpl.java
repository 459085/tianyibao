package com.ctsi.tianyibao.user.service.impl;

import com.ctsi.tianyibao.user.mapper.UserMapper;
import com.ctsi.tianyibao.user.service.UserService;
import com.ctsi.tianyibao.util.AesCbcUtil;
import com.ctsi.tianyibao.util.WebUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Value("${weixin.xiaochengxu.appid}")
    private String appid;
    @Value("${weixin.xiaochengxu.secret}")
    private String secret;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Object getOpenId(String code) {
        Map requestUrlParam = new HashMap();
        requestUrlParam.put("appid", appid);    //开发者设置中的appId
        requestUrlParam.put("secret", secret);    //开发者设置中的appSecret
        requestUrlParam.put("js_code", code);    //小程序调用wx.login返回的code
        requestUrlParam.put("grant_type", "authorization_code");

        String result = WebUtil.getResponseResult("https://api.weixin.qq.com/sns/jscode2session",requestUrlParam);
        requestUrlParam.put("grant_type","client_credential");
        requestUrlParam.remove("js_code");

        String result2 = WebUtil.getResponseResult("https://api.weixin.qq.com/cgi-bin/token",requestUrlParam);
        Map returnMap = new HashMap();
        JSONObject openIdMap = JSONObject.fromObject(result);
        JSONObject tokenMap = JSONObject.fromObject(result2);
        returnMap.put("openIdMap",openIdMap);
        returnMap.put("tokenMap",tokenMap);
        return returnMap;
    }


    public Object getUserInfo(String open_id, String gender, String nickName, String imageUrl,
                              String country, String province, String city, String language, String encryptedData, String iv, String sessionKey) {
        Map userInfo = userMapper.getUserInfo(open_id);
        Map returnMap = new HashMap();
        if (userInfo != null) {
            returnMap.put("status", "1");
//            userInfo.put("nickName",EmojiParser.parseToUnicode(String.valueOf(userInfo.get("nickName"))));
            returnMap.put("user_info", userInfo);
        } else {
            userInfo = new HashMap();
            try {
                String result = AesCbcUtil.decrypt(encryptedData, sessionKey, iv, "UTF-8");
                if (null != result && result.length() > 0) {
                    JSONObject userInfoJSON = JSONObject.fromObject(result);
                    userInfo.put("union_id", userInfoJSON.get("unionId"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            userInfo.put("qx", 9);
            userInfo.put("status", 0);
            userInfo.put("open_id", open_id);
            userInfo.put("gender", gender);
//            userInfo.put("nickName", EmojiParser.parseToAliases(nickName));
            userInfo.put("nickName", nickName);
            userInfo.put("avatarUrl", imageUrl);
            userInfo.put("country", country);
            userInfo.put("province", province);
            userInfo.put("city", city);
            userInfo.put("language", language);
            userInfo.put("add_time", new Date());
            userMapper.insertUser(userInfo);
            returnMap.put("status", "1");
            returnMap.put("user_info", userInfo);
        }
        return returnMap;

    }


}
