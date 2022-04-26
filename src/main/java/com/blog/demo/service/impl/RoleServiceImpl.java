package com.blog.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.blog.demo.constant.CommonConst;
import com.blog.demo.dto.RoleDTO;
import com.blog.demo.dto.UserRoleDTO;
import com.blog.demo.entity.Role;
import com.blog.demo.entity.RoleMenu;
import com.blog.demo.entity.RoleResource;
import com.blog.demo.entity.UserRole;
import com.blog.demo.exception.BizException;
import com.blog.demo.handler.FilterInvocationSecurityMetadataSourceImpl;
import com.blog.demo.mapper.RoleMapper;
import com.blog.demo.mapper.RoleMenuMapper;
import com.blog.demo.mapper.RoleResourceMapper;
import com.blog.demo.mapper.UserRoleMapper;
import com.blog.demo.service.RoleMenuService;
import com.blog.demo.service.RoleResourceService;
import com.blog.demo.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.demo.service.UserRoleService;
import com.blog.demo.util.BeanCopyUtils;
import com.blog.demo.util.PageUtils;
import com.blog.demo.vo.ConditionVo;
import com.blog.demo.vo.PageResult;
import com.blog.demo.vo.RoleVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liziwen
 * @since 2022-03-07
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleResourceService roleResourceService;

    @Autowired
    private RoleResourceMapper roleResourceMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMenuService roleMenuService;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private FilterInvocationSecurityMetadataSourceImpl filterInvocationSecurityMetadataSource;

    @Override
    public List<UserRoleDTO> listUserRoles() {
        //获取角色列表信息
        List<Role> roleList = roleMapper.selectList(new LambdaQueryWrapper<Role>().select(Role::getId, Role::getRoleName));
        return BeanCopyUtils.copyList(roleList,UserRoleDTO.class);
    }

    @Override
    public PageResult<RoleDTO> listRoles(ConditionVo conditionVo) {
        //获取角色列表
        List<RoleDTO> roleDTOList = roleMapper.listRoles(PageUtils.getLimitCurrent(), PageUtils.getSize(), conditionVo);
        //获取角色个数
        Integer count = roleMapper.selectCount(new LambdaQueryWrapper<Role>().like(StringUtils.isNotBlank(conditionVo.getKeywords()), Role::getRoleName, conditionVo.getKeywords()));
        return new PageResult<>(roleDTOList,count);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateRole(RoleVO roleVO) {
        //判断角色是否已经存在
        Role existRole = roleMapper.selectOne(new LambdaQueryWrapper<Role>().select(Role::getId).eq(Role::getRoleName, roleVO.getRoleName()));
        if(Objects.nonNull(existRole) && !existRole.getId().equals(roleVO.getId())){
            throw new BizException("角色名已存在");
        }
        //新建角色对象并保存
        Role role = Role.builder()
                .id(roleVO.getId()).roleName(roleVO.getRoleName()).roleLabel(roleVO.getRoleLabel()).isDisable(CommonConst.FALSE)
                .build();
        this.saveOrUpdate(role);
        //更新角色资源关系
        if(CollectionUtils.isNotEmpty(roleVO.getResourceIdList())){
            //如果roleVO中存在id，则为更新操作，先将之前的资源关系删去
            if(Objects.nonNull(roleVO.getId())){
                roleResourceService.remove(new LambdaQueryWrapper<RoleResource>().eq(RoleResource::getRoleId, roleVO.getId()));
            }
            List<RoleResource> roleResourceList = roleVO.getResourceIdList()
                    .stream()
                    .map(resourceId -> RoleResource.builder().resourceId(resourceId).roleId(role.getId()).build())
                    .collect(Collectors.toList());
            roleResourceService.saveBatch(roleResourceList);
            //重新加载角色资源信息
            filterInvocationSecurityMetadataSource.clearDataSource();
        }
        //更新角色菜单关系
        if(CollectionUtils.isNotEmpty(roleVO.getMenuIdList())){
            if(Objects.nonNull(roleVO.getId())){
                roleMenuService.remove(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId,roleVO.getId()));
            }
            List<RoleMenu> roleMenuList = roleVO.getMenuIdList()
                    .stream()
                    .map(menuId -> RoleMenu.builder().menuId(menuId).roleId(role.getId()).build())
                    .collect(Collectors.toList());
            roleMenuService.saveBatch(roleMenuList);
        }
    }

    @Transactional
    @Override
    public void deleteRoles(List<Integer> roleIdList) {
        Integer count = userRoleMapper.selectCount(new LambdaQueryWrapper<UserRole>().in(UserRole::getRoleId, roleIdList));
        if(count > 0){
            throw new BizException("角色下仍有用户，删除失败");
        }
//        List<Integer> roleResourcesId = new ArrayList<>();
//        List<Integer> roleMenusId = new ArrayList<>();
        //删除与角色相对应的资源和菜单信息
        roleResourceMapper.delete(new LambdaQueryWrapper<RoleResource>().in(RoleResource::getRoleId,roleIdList));
        roleMenuMapper.delete(new LambdaQueryWrapper<RoleMenu>().in(RoleMenu::getRoleId,roleIdList));
//        for(Integer roleId : roleIdList){
//            List<RoleResource> roleResourcesIdList = roleResourceMapper
//                    .selectList(new LambdaQueryWrapper<RoleResource>().select(RoleResource::getId).eq(RoleResource::getRoleId, roleId));
//            List<RoleMenu> roleMenuIdList = roleMenuMapper
//                    .selectList(new LambdaQueryWrapper<RoleMenu>().select(RoleMenu::getId).eq(RoleMenu::getRoleId, roleId));
//            roleResourcesIdList.forEach(item -> roleResourcesId.add(item.getId()));
//            roleMenuIdList.forEach(item -> roleMenusId.add(item.getId()));
//        }
//        roleResourceMapper.deleteBatchIds(roleResourcesId);
//        roleMenuMapper.deleteBatchIds(roleMenusId);
        roleMapper.deleteBatchIds(roleIdList);
    }
}
