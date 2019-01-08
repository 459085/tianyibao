package com.ctsi.tianyibao.index.service.impl;

import com.ctsi.tianyibao.index.mapper.IndexMapper;
import com.ctsi.tianyibao.index.service.IndexService;
import com.ctsi.tianyibao.util.DateUtil;
import com.ctsi.tianyibao.util.DistanceUtil;
import com.sun.xml.internal.bind.v2.TODO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Service
public class IndexServiceImpl implements IndexService {
    @Autowired
    private IndexMapper indexMapper;

    @Override
    public Object getLbt() {
        List carouselFigureList = indexMapper.getLbt();
        Map returnMap = new HashMap();
        returnMap.put("status", "1");
        returnMap.put("carousel_figure_list", carouselFigureList);
        return returnMap;
    }

    @Override
    public Object getSqrzConfig() {
        Map returnMap = new HashMap();
        Map sqrzConfigMap = getSysConfig();
        returnMap.put("status", "1");
        returnMap.put("sqrz_switch", sqrzConfigMap.get("sqrz_switch"));
        return returnMap;
    }

    private Map getSysConfig() {
        Map returnMap = indexMapper.getSysConfig();
        return returnMap;
    }

    private String getShopTypeImageSwitch() {
        Map sqrzConfigMap = getSysConfig();
        return sqrzConfigMap.get("shop_type_image_switch").toString();
    }

    @Override
    public Object getShopTypeImageList(double latitude, double longitude) {
        /*
         * 运营中心位置
         * */
        double center_latitude = 40.06897;
        double center_longitude = 116.13073;
        Map returnMap = new HashMap();
        if ("1".equals(getShopTypeImageSwitch())) {
            double distance = DistanceUtil.getDistance(latitude, longitude, center_latitude, center_longitude);
            if (distance <= 100000) {
                List shopTypeImageList = indexMapper.getShopType();
                returnMap.put("status", "1");
                returnMap.put("shop_type_image_list", shopTypeImageList);
            } else {
                List shopTypeImageList = indexMapper.getUnShopType();
                returnMap.put("status", "1");
                returnMap.put("shopTypeImageList", shopTypeImageList);
            }
        } else {
            List shopTypeImageList = indexMapper.getShopType();
            returnMap.put("status", "1");
            returnMap.put("shop_type_image_list", shopTypeImageList);
        }
        return returnMap;
    }

    @Override
    public Object getShopList(double latitude, double longitude, int offset, int limit, String type, String shopType) {
        Date now = new Date();
        //为避免数据量过大。取经度差0.2，纬度差0.3范围内，国内基本保证在20公里以内全覆盖。
        List<Map> shopList = indexMapper.getShopList(now, type, shopType, latitude, longitude);
        /**
        * 清理过远距离店铺，计算配送时间，计算评分，清理配送不到店铺
        * */
        getShopList$cal$leadTime(shopList);
        getShopList$cal$remark(shopList,now);
        getShopList$remove$FarShop(shopList,latitude,longitude);
        return shopList;
    }

    private void getShopList$cal$leadTime(List<Map> shopList){
        for (Map shop : shopList) {
            int sendResultTime;
            String id = shop.get("id").toString();
            //根据商家id获取近30个完成（status>40）的订单，计算平均的送货时间,订单数大于3才走此逻辑，小于3从库中获取
            List<Map> lastOrder30List = indexMapper.get30FinishOrderList(id);
            //有订单时，且大于3单
            if (lastOrder30List.size()> 10){
                //计算真实配送时间：时间规则，10单权值0.7，10-30单内权值0.3
                int lastOrderCount1 = 10;
                long sendTimeCount1 = 0;
                int lastOrderCount2 = lastOrder30List.size()-10;
                long sendTimeCount2 = 0;
                Long sendTime1 =0l;
                Long sendTime2 =0l;
                for(int i=0;i<lastOrder30List.size();i++){
                    String orderId = lastOrder30List.get(i).get("order_id").toString();
                    Long orderOf40StatusTimeStamp = indexMapper.getOrderTimeStampByStatus(orderId, "40");
                    Long orderOf20StatusTimeStamp = indexMapper.getOrderTimeStampByStatus(orderId, "20");
                    if(i<10){
                        if(orderOf40StatusTimeStamp!=null&&orderOf20StatusTimeStamp!=null){
                            long sendTime = orderOf40StatusTimeStamp-orderOf20StatusTimeStamp;
                            sendTimeCount1 +=sendTime;
                        }else{
                            lastOrderCount1--;
                        }
                        sendTime1 =sendTimeCount1/lastOrderCount1;
                    }else{
                        if(orderOf40StatusTimeStamp!=null&&orderOf20StatusTimeStamp!=null){
                            long sendTime = orderOf40StatusTimeStamp-orderOf20StatusTimeStamp;
                            sendTimeCount2 +=sendTime;
                        }else{
                            lastOrderCount2--;
                        }
                        sendTime2 =sendTimeCount2/lastOrderCount2;
                    }
                }
                sendResultTime = (int)(sendTime1/60*0.7+sendTime2/60*0.2);
                shop.put("order_lead_time",sendResultTime);
            }else if(lastOrder30List.size() > 3){
                int lastOrder30Count = lastOrder30List.size();
                long sendTimeCount = 0;
                for(Map m:lastOrder30List){
                    String orderId = m.get("order_id").toString();
                    Long orderOf40StatusTimeStamp = indexMapper.getOrderTimeStampByStatus(orderId, "40");
                    Long orderOf20StatusTimeStamp = indexMapper.getOrderTimeStampByStatus(orderId, "20");
                    if(orderOf40StatusTimeStamp!=null&&orderOf20StatusTimeStamp!=null){
                        long sendTime = orderOf40StatusTimeStamp-orderOf20StatusTimeStamp;
                        sendTimeCount +=sendTime;
                    }else{
                        lastOrder30Count--;
                    }
                }
                Long sendTime = sendTimeCount/lastOrder30Count;
                sendResultTime = (int)(sendTime/60);
                shop.put("order_lead_time",sendResultTime);
            }
        }
    }

    private void getShopList$cal$remark(List<Map> shopList,Date now){
        //获取一个月前0:00时间
        Date nowBefore31Day = DateUtil.getSartTimeOfAddSomeDays(now, -31);
        shopList.forEach(Map->{
            String shopId = Map.get("id").toString();
            //获取评分平均值
            DecimalFormat df = new DecimalFormat("0.0");
            Double avgRemarkD = indexMapper.getOrderRemarkRating(shopId,now);
            if(avgRemarkD==null){
                avgRemarkD=0.0;
            }
            String avgRemark = df.format(avgRemarkD);
            Map.put("rating",avgRemark);

            //获取近31天总单量
            Integer finishOrderCount = indexMapper.getFinishOrderCount(shopId,nowBefore31Day);
            if(finishOrderCount==null){
                finishOrderCount=0;
            }
            Map.put("recent_order_num",finishOrderCount);
        });

    }

    private void getShopList$remove$FarShop(List<Map> shopList,double latitude,double longitude){
        //shopVisibleSwitch为1，表示过滤过远距离（已弃用）
        //String shopVisibleSwitch =indexMapper.getSysConfig().get("shop_visible_switch").toString();
        Iterator<Map> it = shopList.iterator();
        while(it.hasNext()){
            Map shopMap = it.next();
            Integer maxDeliveryDistance = (Integer) shopMap.get("max_delivery_distance");
            double shopDistance =DistanceUtil.getDistance(latitude, longitude, Double.valueOf((String) shopMap.get("latitude")), Double.valueOf((String) shopMap.get("longitude")));
            if (maxDeliveryDistance != null && shopDistance > maxDeliveryDistance) {
             //   it.remove();
            }else{
                shopMap.put("shop_distance",shopDistance);
            }
        }
    }

//    public static void main(String args[]){
//        double center_latitude = 53.06897;
//        double center_longitude = 116.13073;
//        double center_latitude2 = 53.26897;
//        double center_longitude2 = 116.13073;
//        double distance = DistanceUtil.getDistance(center_latitude2,center_longitude2,center_latitude,center_longitude);
//        System.out.println(distance);
//    }


}
