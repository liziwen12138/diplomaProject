package com.blog.demo.mapper;

import com.blog.demo.dto.CategoryBackDTO;
import com.blog.demo.dto.CategoryDTO;
import com.blog.demo.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.demo.vo.ConditionVo;
import com.blog.demo.vo.PageResult;
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
 * @since 2022-03-16
 */
@Mapper
@Component(value = "categoryMapper")
public interface CategoryMapper extends BaseMapper<Category> {

    List<CategoryBackDTO> listCategoryBackDTO(@Param("current")Long current,@Param("size")Long size,@Param("condition")ConditionVo condition);

    List<CategoryDTO> listCategories();
}
