package com.blog.demo.dto;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserMenuDTO {

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 路由路径
     */
    private String path;

    /**
     * 图片
     */
    private String icon;

    /**
     * 路由所对应的容器
     */
    private String component;

    /**
     * 是否隐藏
     */
    private Boolean hidden;

    /**
     * 子容器数组
     */
    private List<UserMenuDTO> children;

}
