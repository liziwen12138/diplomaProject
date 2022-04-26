package com.blog.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoDTO {

    /**
     * 用户账号id
     */
    @JsonProperty
    private Integer id;

    /**
     * 用户信息id
     */
    @JsonProperty
    private Integer userInfoId;

    /**
     * 邮箱号
     */
    @JsonProperty
    private String email;

    /**
     * 登录方式
     */
    @JsonProperty
    private Integer loginType;

    /**
     * 用户名
     */
    @JsonProperty
    private String username;

    /**
     * 用户昵称
     */
    @JsonProperty
    private String nickname;

    /**
     * 用户头像
     */
    @JsonProperty
    private String avatar;

    /**
     * 用户简介
     */
    @JsonProperty
    private String intro;

    /**
     * 个人网站
     */
    @JsonProperty
    private String webSite;

    /**
     * 点赞文章集合
     */
    @JsonProperty
    private Set<Object> articleLikeSet;

    /**
     * 点赞评论集合
     */
    @JsonProperty
    private Set<Object> commentLikeSet;

    /**
     * 点赞评论集合
     */
    @JsonProperty
    private Set<Object> talkLikeSet;

    /**
     * 用户登录ip
     */
    @JsonProperty
    private String ipAddress;

    /**
     * ip来源
     */
    @JsonProperty
    private String ipSource;

    /**
     * 最近登录时间
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime lastLoginTime;
}
