package com.ctsi.tianyibao.user.service;

public interface UserService {
    Object getOpenId(String code);
    Object getUserInfo(String open_id,String gender,String nickName,String imageUrl,
                       String country,String province,String city,String language,String encryptedData,String iv,String sessionKey);
}
