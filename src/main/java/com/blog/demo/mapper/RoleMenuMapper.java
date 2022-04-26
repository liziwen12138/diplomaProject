package com.blog.demo.mapper;

import com.blog.demo.entity.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liziwen
 * @since 2022-03-12
 */
@Mapper
@Component(value = "roleMenuMapper")
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

}
