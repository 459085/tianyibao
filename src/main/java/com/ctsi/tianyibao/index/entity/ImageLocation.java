package com.ctsi.tianyibao.index.entity;

import lombok.Data;

/**
 * 图片相关pojo对应数据库yqb_image_location
 */
@Data
public class ImageLocation {
    /**
     * 主键
     */
    private Integer id;
    /**
     *图片模式
     */
    private String mode;
    /**
     * 排序
     */
    private String image_path;
    /**
     * 是否使用：1,使用;0未使用
     */
    private String is_online;
    /**
     * 是否删除：1,删除;0未删除
     */
    private String is_del;
    /**
     * 跳转路径
     */
    private String image_href;
    /**
     * 图片名称
     */
}
