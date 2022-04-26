package com.blog.demo.service;

import com.blog.demo.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.demo.vo.UserRoleVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liziwen
 * @since 2022-03-07
 */
public interface UserInfoService extends IService<UserInfo> {

    /**
     * 更新用户角色
     * @param userRoleVO
     */
    void updateUserRole(UserRoleVO userRoleVO);
}
