package com.blog.demo.controller;


import com.blog.demo.dto.UserBackDTO;
import com.blog.demo.dto.UserRoleDTO;
import com.blog.demo.service.UserAuthService;
import com.blog.demo.vo.ConditionVo;
import com.blog.demo.vo.PageResult;
import com.blog.demo.vo.Result;
import com.blog.demo.vo.UserVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liziwen
 * @since 2022-03-06
 */
@Api(tags = "用户账户模块")
@RestController
public class UserAuthController {

    @Autowired
    private UserAuthService userAuthService;

    /**
     * 查询后台所有用户列表
     * @param conditionVo
     * @return
     */
    @GetMapping("/admin/users")
    public Result<PageResult<UserBackDTO>> listUser(ConditionVo conditionVo){
        return Result.ok(userAuthService.listUserBackDTO(conditionVo));
    }

    /**
     * 注册用户
     * @param userVO
     * @return
     */
    @PostMapping("register")
    public Result<?> register(@Valid @RequestBody UserVO userVO) throws JsonProcessingException {
        userAuthService.register(userVO);
        return Result.ok();
    }

}

