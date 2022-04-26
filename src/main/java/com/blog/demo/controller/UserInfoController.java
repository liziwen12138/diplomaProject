package com.blog.demo.controller;


import com.blog.demo.mapper.UserInfoMapper;
import com.blog.demo.service.UserInfoService;
import com.blog.demo.vo.Result;
import com.blog.demo.vo.UserRoleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liziwen
 * @since 2022-03-07
 */
@RestController
@Api(value = "用户详细控制器")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @PutMapping("/admin/users/role")
    @ApiOperation(value = "更新用户角色")
    public Result<?> updateUserRole(@Valid @RequestBody UserRoleVO userRoleVO){
        userInfoService.updateUserRole(userRoleVO);
        return Result.ok();
    }
}

