package com.ctsi.tianyibao.index.controller;

import com.ctsi.tianyibao.index.service.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("Api/index/")
@Slf4j
public class IndexController {

    @Value("${weixin.xiaochengxu.appid}")
    private String appid;
    @Value("${weixin.xiaochengxu.secret}")
    private String secret;

    @Autowired
    private IndexService indexService;

    @GetMapping("get_carousel_figure_list")
    public Object getLbt() {
        return indexService.getLbt();
    }

    //TODO ("微信小程序审核时，因品目问题需伪装-不能加入申请入驻字样")
    @GetMapping("get_sqrz_config")
    public Object getSqrzConfig() {
        return indexService.getSqrzConfig();
    }

    //TODO ("微信小程序审核时，因品目问题需伪装-距离运营中心远需改变页面样式")
    @GetMapping("get_shop_type_image_list")
    public Object getShopTypeImageList(@RequestParam("latitude") double latitude, @RequestParam("longitude") double longitude) {
        return indexService.getShopTypeImageList(latitude, longitude);
    }


    /**
     * 小程序首页面商铺信息
     * */
    @GetMapping("home")
    public Object home(@RequestParam("latitude") double latitude, @RequestParam("longitude") double longitude, @RequestParam("offset") int offset,
                       @RequestParam("limit") int limit, String type,String shop_type) {

        return indexService.getShopList(latitude, longitude,offset,limit,type,shop_type);
    }

}
