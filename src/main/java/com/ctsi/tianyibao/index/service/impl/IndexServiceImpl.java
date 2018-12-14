package com.ctsi.tianyibao.index.service.impl;

import com.ctsi.tianyibao.index.mapper.IndexMapper;
import com.ctsi.tianyibao.index.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IndexServiceImpl implements IndexService {
    @Autowired
    private IndexMapper IndexMapper;

    @Override
    public Object getLbt(){
        List carouselFigureList =IndexMapper.getLbt();
        Map returnMap = new HashMap();
        returnMap.put("status","1");
        returnMap.put("carousel_figure_list",carouselFigureList);
        return returnMap;
    }
}
