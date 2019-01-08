package com.ctsi.tianyibao.index.service;

public interface IndexService {
    Object getLbt();
    Object getSqrzConfig();
    Object getShopTypeImageList(double latitude, double longitude);
    Object getShopList(double latitude,double longitude,int offset,int limit,String type,String shopType);
}
