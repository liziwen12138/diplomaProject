package com.blog.demo.controller;


import com.blog.demo.dto.RoleDTO;
import com.blog.demo.dto.UserRoleDTO;
import com.blog.demo.service.RoleService;
import com.blog.demo.vo.ConditionVo;
import com.blog.demo.vo.PageResult;
import com.blog.demo.vo.Result;
import com.blog.demo.vo.RoleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liziwen
 * @since 2022-03-07
 */
@Api(value = "角色控制器")
@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 获取用户角色列表
     * @return
     */
    @GetMapping("/admin/users/role")
    @ApiOperation(value = "获取用户角色列表")
    public Result<List<UserRoleDTO>> listUserRoles(){
        return Result.ok(roleService.listUserRoles());
    }

    /**
     * 获取角色列表
     * @param conditionVo
     * @return
     */
    @GetMapping("/admin/roles")
    @ApiOperation(value = "获取用户列表")
    public Result<PageResult<RoleDTO>> listRoles(ConditionVo conditionVo){
        return Result.ok(roleService.listRoles(conditionVo));
    }

    /**
     * 保存或更新用户信息
     * @param roleVO
     * @return
     */
    @PostMapping("/admin/role")
    @ApiOperation(value = "保存或更新用户信息")
    public Result<?> saveOrUpdateRole(@Valid @RequestBody RoleVO roleVO){
        roleService.saveOrUpdateRole(roleVO);
        return Result.ok();
    }

    /**
     * 根据角色id删除角色列表
     * @param roleIdList
     * @return
     */
    @DeleteMapping("/admin/roles")
    @ApiOperation(value = "根据id删除角色列表")
    public Result<?> deleteRoles(@RequestBody List<Integer>roleIdList){
        roleService.deleteRoles(roleIdList);
        return Result.ok();
    }
}

