package com.blog.demo.controller;


import com.blog.demo.dto.LabelOptionDTO;
import com.blog.demo.dto.MenuDTO;
import com.blog.demo.dto.UserMenuDTO;
import com.blog.demo.entity.Menu;
import com.blog.demo.service.MenuService;
import com.blog.demo.vo.ConditionVo;
import com.blog.demo.vo.MenuVO;
import com.blog.demo.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.User;
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
 * @since 2022-03-12
 */
@Api(tags = "菜单模块")
@RestController
public class MenuController {

    @Autowired
    private MenuService menuService;

    @ApiOperation(value = "查看菜单列表")
    @GetMapping("/admin/menus")
    public Result<List<MenuDTO>> listMenus(ConditionVo conditionVo){
        System.out.println(menuService.listMenus(conditionVo));
        return Result.ok(menuService.listMenus(conditionVo));
    }

    /**
     * 查看登录用户的菜单列表（根据权限筛选）
     * @return
     */
    @ApiOperation(value = "查看用户拥有的权限的菜单列表")
    @GetMapping("/user/menus")
    public Result<List<UserMenuDTO>> listUserMenus(){
        List<UserMenuDTO> userMenuDTOS = menuService.listUserMenus();
        System.out.println(userMenuDTOS.toString());
        return Result.ok(userMenuDTOS);
    }

    /**
     * 获取角色菜单选项列表
     * @return
     */
    @GetMapping("/admin/role/menus")
    @ApiOperation(value = "获取角色菜单选项列表")
    public Result<List<LabelOptionDTO>> listMenuOptions(){
        return Result.ok(menuService.listMenuOptions());
    }

    @PostMapping("/admin/menus")
    @ApiOperation(value = "新增或更新菜单")
    public Result<?> saveOrUpdateMenu(@Valid @RequestBody MenuVO menuVO){
        menuService.saveOrUpdateMenu(menuVO);
        return Result.ok();
    }

    @DeleteMapping("/admin/menus/{menuId}")
    @ApiOperation(value = "根据id删除菜单")
    public Result<?> deleteMenuById(@PathVariable("menuId")Integer id){
        menuService.deleteMenuById(id);
        return Result.ok();
    }
}

