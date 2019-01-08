package com.ctsi.tianyibao.user.mapper;

import java.util.Map;

public interface UserMapper {
    Map getUserInfo(String open_id);
    void insertUser(Map userMap);
}
