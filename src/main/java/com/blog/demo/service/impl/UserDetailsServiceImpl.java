package com.blog.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blog.demo.dto.UserDetailDTO;
import com.blog.demo.entity.UserAuth;
import com.blog.demo.entity.UserInfo;
import com.blog.demo.mapper.RoleMapper;
import com.blog.demo.mapper.UserAuthMapper;
import com.blog.demo.mapper.UserInfoMapper;
import com.blog.demo.mapper.UserRoleMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserAuthMapper userAuthMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private RoleMapper roleMapper;
//    @resource 基本与@autowired作用一样，都是自动注入容器，但是@resource不是spring提供的，且按名划分
    @Resource
    private HttpServletRequest request;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        检查username是否为空
        if(StringUtils.isBlank(username)){
//            抛出自定义异常：用户名不能为空
        }
//         username存在，则查询账户是否存在
        UserAuth userAuth = userAuthMapper.selectOne(new LambdaQueryWrapper<UserAuth>()
                .select(UserAuth::getId, UserAuth::getUserInfoId, UserAuth::getUsername,
                        UserAuth::getPassword, UserAuth::getLoginType)
                .eq(UserAuth::getUsername, username));
        if(Objects.isNull(userAuth)){
//            抛出自定义异常：账户名不存在
        }
//        完成用户查询，且用户存在，则对用户登录信息进行封装：
        return convertUserDetail(userAuth,request);
    }

    /**
     * 封装User的详细信息并用以交付给前端
     * @param user
     * @param request
     * @return
     */
    public UserDetailDTO convertUserDetail(UserAuth user, HttpServletRequest request){
//        查询账号信息
        UserInfo userInfo = userInfoMapper.selectById(user.getUserInfoId());
//        查找账户角色（角色既是对应的权限）
        List<String> roleList = roleMapper.listRolesByUserInfoId(userInfo.getId());
        /**
         * 中间作者写的代码是一系列扩展功能的代码，暂时空着，后期填补。
         */
        UserDetailDTO userDetailDTO = new UserDetailDTO();
        userDetailDTO.setId(user.getId());
        userDetailDTO.setLoginType(user.getLoginType());
        userDetailDTO.setUserInfoId(userInfo.getId());
        userDetailDTO.setUsername(user.getUsername());
        userDetailDTO.setPassword("{noop}"+user.getPassword());
        userDetailDTO.setEmail(userInfo.getEmail());
        userDetailDTO.setRoleList(roleList);
        userDetailDTO.setNickname(userInfo.getNickname());
        userDetailDTO.setAvatar(userInfo.getAvatar());
        userDetailDTO.setIntro(userInfo.getIntro());
        userDetailDTO.setWebSite(userInfo.getWebSite());
        userDetailDTO.setIsDisable(userInfo.getIsDisable());
        userDetailDTO.setLastLoginTime(LocalDateTime.now(ZoneId.systemDefault()));

//        封装user权限对象
        return userDetailDTO;
    }
}
