package com.blog.demo.mapper;

import com.blog.demo.entity.RoleResource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liziwen
 * @since 2022-04-19
 */
@Mapper
@Component(value = "roleResourceMapper")
public interface RoleResourceMapper extends BaseMapper<RoleResource> {

}
