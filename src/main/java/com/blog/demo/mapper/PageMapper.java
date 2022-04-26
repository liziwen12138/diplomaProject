package com.blog.demo.mapper;

import com.blog.demo.entity.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 页面 Mapper 接口
 * </p>
 *
 * @author liziwen
 * @since 2022-03-25
 */
@Mapper
@Component(value = "pageMapper")
public interface PageMapper extends BaseMapper<Page> {

}
