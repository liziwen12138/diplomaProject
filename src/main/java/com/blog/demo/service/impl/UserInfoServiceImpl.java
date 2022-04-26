package com.blog.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blog.demo.entity.UserInfo;
import com.blog.demo.entity.UserRole;
import com.blog.demo.mapper.UserInfoMapper;
import com.blog.demo.mapper.UserRoleMapper;
import com.blog.demo.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.demo.service.UserRoleService;
import com.blog.demo.vo.UserRoleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserRoleService userRoleService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserRole(UserRoleVO userRoleVO) {
        UserInfo userInfo = UserInfo.builder().nickname(userRoleVO.getNickname()).id(userRoleVO.getUserInfoId()).build();
        userInfoMapper.updateById(userInfo);
        userRoleService.remove(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId,userRoleVO.getUserInfoId()));
        List<UserRole> userRoleList = userRoleVO.getRoleIdList().stream().map(roleId -> UserRole.builder()
                .roleId(roleId)
                .userId(userRoleVO.getUserInfoId())
                .build()).collect(Collectors.toList());
        userRoleService.saveBatch(userRoleList);
    }
}
