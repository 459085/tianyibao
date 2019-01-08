package com.ctsi.tianyibao.index.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Map;


public interface IndexMapper{
    List<Map> getLbt();
    Map getSysConfig();
    List<Map> getShopType();
    List<Map> getUnShopType();
    List<Map> getShopList(@Param("now") Date now, @Param("type") String type, @Param("shop_type") String shopType,@Param("latitude") double latitude,@Param("longitude") double longitude);
    List<Map> get30FinishOrderList(@Param("id") String id);
    Long getOrderTimeStampByStatus(@Param("orderId") String orderId,@Param("orderStatus") String orderStatus);
    Double getOrderRemarkRating(@Param("shopId") String shopId , @Param("now") Date now);
    Integer getFinishOrderCount(@Param("shopId") String shopId , @Param("nowBefore31Day") Date nowBefore31Day);
}
