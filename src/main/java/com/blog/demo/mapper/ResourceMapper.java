package com.blog.demo.mapper;

import com.blog.demo.entity.Resource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liziwen
 * @since 2022-04-15
 */
@Mapper
@Component(value = "resourceMapper")
public interface ResourceMapper extends BaseMapper<Resource> {

}
