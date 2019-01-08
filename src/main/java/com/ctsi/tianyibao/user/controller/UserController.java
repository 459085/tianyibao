package com.ctsi.tianyibao.user.controller;

import com.ctsi.tianyibao.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("Api/user/")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("get_weixin_sns_info")
    public Object getOpenId(@RequestParam("code") String code) {
        return userService.getOpenId(code);
    }

    @GetMapping("get_user_info")
    public Object getUserInfo(@RequestParam("open_id") String open_id, @RequestParam("gender") String gender,
                              @RequestParam("nick_name") String nickName, @RequestParam("image_url") String imageUrl,
                              @RequestParam("country") String country, @RequestParam("province") String province,
                              @RequestParam("city") String city, @RequestParam("language") String language,
                              @RequestParam("encryptedData") String encryptedData,@RequestParam("iv") String iv,@RequestParam("sessionKey") String sessionKey) {
        return userService.getUserInfo(open_id, gender, nickName, imageUrl,
                country, province, city, language,encryptedData,iv,sessionKey);
    }



}
