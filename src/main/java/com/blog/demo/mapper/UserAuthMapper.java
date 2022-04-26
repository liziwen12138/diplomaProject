package com.blog.demo.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.demo.dto.UserBackDTO;
import com.blog.demo.dto.UserRoleDTO;
import com.blog.demo.entity.UserAuth;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.demo.vo.ConditionVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liziwen
 * @since 2022-03-06
 */
@Mapper
@Component(value = "userAuthMapper")
public interface UserAuthMapper extends BaseMapper<UserAuth> {

    /**
     * 根据用户名去查找用户的权限
     * @return
     */
    List<String> getUserRolesByUsername(@Param("id") Integer id);

    /**
     * 获取后台所有的用户列表
     * @param current
     * @param size
     * @param condition
     * @return
     */
    List<UserBackDTO> listUsers(@Param("current")Long current, @Param("size")Long size, @Param("condition")ConditionVo condition);

    /**
     * 获取后台用户的数量
     * @return
     */

    Integer countUser(@Param("condition")ConditionVo condition);
}
