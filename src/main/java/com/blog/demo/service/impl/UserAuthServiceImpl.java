package com.blog.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.blog.demo.constant.CommonConst;
import com.blog.demo.dto.UserBackDTO;
import com.blog.demo.dto.UserRoleDTO;
import com.blog.demo.entity.UserAuth;
import com.blog.demo.entity.UserInfo;
import com.blog.demo.entity.UserRole;
import com.blog.demo.enums.RoleEnum;
import com.blog.demo.exception.BizException;
import com.blog.demo.mapper.UserAuthMapper;
import com.blog.demo.mapper.UserInfoMapper;
import com.blog.demo.mapper.UserRoleMapper;
import com.blog.demo.service.BlogInfoService;
import com.blog.demo.service.UserAuthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.demo.util.PageUtils;
import com.blog.demo.vo.ConditionVo;
import com.blog.demo.vo.PageResult;
import com.blog.demo.vo.UserVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liziwen
 * @since 2022-03-06
 */
@Service
public class UserAuthServiceImpl extends ServiceImpl<UserAuthMapper, UserAuth> implements UserAuthService {

    @Autowired
    private UserAuthMapper userAuthMapper;

    @Autowired
    private BlogInfoService blogInfoService;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public List<String> getUserRolesByUsername(Integer id) {
        return this.baseMapper.getUserRolesByUsername(id);
    }


    @Override
    public PageResult<UserBackDTO> listUserBackDTO(ConditionVo conditionVo) {
        Integer count = userAuthMapper.countUser(conditionVo);
        if(count == 0){
            return new PageResult<>();
        }
        List<UserBackDTO> userBackDTOS = userAuthMapper.listUsers(PageUtils.getLimitCurrent(), PageUtils.getSize(), conditionVo);
        return new PageResult<>(userBackDTOS,count);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(UserVO userVO) throws JsonProcessingException {
        //检查邮箱是否已经存在
        if(checkUser(userVO)){
            throw new BizException("邮箱已注册");
        }
        //新增用户信息(用户头像用默认头像，名称用mybatis工具自动生成
        UserInfo userInfo = UserInfo.builder().email(userVO.getUsername()).avatar(blogInfoService.getWebsiteConfig().getUserAvatar())
                .nickname(CommonConst.DEFAULT_NICKNAME + IdWorker.getId()).build();
        userInfoMapper.insert(userInfo);
        //新增用户角色对照
        UserRole userRole = UserRole.builder().userId(userInfo.getId()).roleId(RoleEnum.USER.getRoleId()).build();
        userRoleMapper.insert(userRole);
        //新增用户权限账户
        UserAuth userAuth = UserAuth.builder()
                .password(userVO.getPassword())
                .username(userVO.getUsername())
                .userInfoId(userInfo.getId())
                .loginType(1)
                .build();
        userAuthMapper.insert(userAuth);
    }

    /**
     * 检测用户名是否存在
     * 若存在返回true
     * @param userVO
     * @return
     */
    private boolean checkUser(UserVO userVO){
        UserAuth userAuth = userAuthMapper.selectOne(new LambdaQueryWrapper<UserAuth>().select(UserAuth::getUsername)
                .eq(UserAuth::getUsername, userVO.getUsername()));
        return Objects.nonNull(userAuth);
    }
}
