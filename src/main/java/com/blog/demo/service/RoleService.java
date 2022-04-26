package com.blog.demo.service;

import com.blog.demo.dto.RoleDTO;
import com.blog.demo.dto.UserRoleDTO;
import com.blog.demo.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.demo.vo.ConditionVo;
import com.blog.demo.vo.PageResult;
import com.blog.demo.vo.RoleVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liziwen
 * @since 2022-03-07
 */
public interface RoleService extends IService<Role> {

    /**
     * 查询角色列表
     * @return
     */
    List<UserRoleDTO> listUserRoles();

    /**
     * 获取角色列表
     * @param conditionVo
     * @return
     */
    PageResult<RoleDTO> listRoles(ConditionVo conditionVo);

    /**
     * 保存或更新角色
     * @param roleVO
     */
    void saveOrUpdateRole(RoleVO roleVO);

    /**
     * 根据id删除角色列表
     * @param roleIdList
     */
    void deleteRoles(List<Integer> roleIdList);
}
