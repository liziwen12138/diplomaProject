package com.blog.demo.service;

import com.blog.demo.dto.LabelOptionDTO;
import com.blog.demo.dto.MenuDTO;
import com.blog.demo.dto.UserMenuDTO;
import com.blog.demo.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.demo.vo.ConditionVo;
import com.blog.demo.vo.MenuVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liziwen
 * @since 2022-03-12
 */
public interface MenuService extends IService<Menu> {

    /**
     * 查看菜单列表
     * @param conditionVo
     * @return
     */
    List<MenuDTO> listMenus(ConditionVo conditionVo);

    /**
     * 查看当前登录用户的菜单列表
     * @return
     */
    List<UserMenuDTO> listUserMenus();

    /**
     * 角色菜单列表
     * @return
     */
    List<LabelOptionDTO> listMenuOptions();

    /**
     * 新增或更新菜单
     * @param menuVO
     */
    void saveOrUpdateMenu(MenuVO menuVO);

    /**
     * 根据id删除菜单
     * @param id
     */
    void deleteMenuById(Integer id);
}
