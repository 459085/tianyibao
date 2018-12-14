package com.ctsi.tianyibao.index.controller;

import com.ctsi.tianyibao.index.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("Api/index/")
public class IndexController {
    @Autowired
    private IndexService indexService;

    @GetMapping("get_carousel_figure_list")
    public Object getLbt(){
        return indexService.getLbt();
    }
}
