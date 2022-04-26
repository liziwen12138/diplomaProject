package com.blog.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  RoleEnum {
    ADMIN(1,"管理员","admin"),
    USER(2,"用户","user"),
    TEST(3,"测试账户","test");
    /**
     * 角色id
     */
    private final Integer RoleId;

    /**
     * 角色名称
     */
    private final String name;

    /**
     * 权限标签
     */
    private final String label;

}
