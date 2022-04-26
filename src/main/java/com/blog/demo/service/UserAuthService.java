package com.blog.demo.service;

import com.blog.demo.dto.UserBackDTO;
import com.blog.demo.dto.UserRoleDTO;
import com.blog.demo.entity.UserAuth;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.demo.vo.ConditionVo;
import com.blog.demo.vo.PageResult;
import com.blog.demo.vo.UserVO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liziwen
 * @since 2022-03-06
 */
public interface UserAuthService extends IService<UserAuth> {

    /**
     * 根据用户名去查找用户的权限
     * @return
     */
    List<String> getUserRolesByUsername(Integer id);

    /**
     * 查询后台用户列表
     * @param conditionVo
     * @return
     */
    PageResult<UserBackDTO> listUserBackDTO(ConditionVo conditionVo);

    /**
     * 用户注册
     * @param userVO
     */
    void register(UserVO userVO) throws JsonProcessingException;
}
