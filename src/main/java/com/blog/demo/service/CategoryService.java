package com.blog.demo.service;

import com.blog.demo.dto.CategoryBackDTO;
import com.blog.demo.dto.CategoryDTO;
import com.blog.demo.dto.CategoryOptionDTO;
import com.blog.demo.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.demo.util.PageUtils;
import com.blog.demo.vo.CategoryVo;
import com.blog.demo.vo.ConditionVo;
import com.blog.demo.vo.PageResult;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liziwen
 * @since 2022-03-16
 */
public interface CategoryService extends IService<Category> {

    /**
     * 根据搜索条件返回分类列表
     * @param conditionVo
     * @return
     */
    List<CategoryOptionDTO> listCategoriesBySearch(ConditionVo conditionVo);

    /**
     * 查看后台分类列表
     * @param conditionVo
     * @return
     */
    PageResult<CategoryBackDTO> listBackCategories(ConditionVo conditionVo);

    /**
     * 新增或更改分类
     * @param categoryVo
     */
    void saveOrUpdateCategory(CategoryVo categoryVo);

    /**
     * 根据idList删除分类
     * @param idList
     */
    void deleteCategories(List<Integer> idList);

    /**
     * 前台分类列表
     * @param conditionVo
     * @return
     */
    PageResult<CategoryDTO> listCategories(ConditionVo conditionVo);
}
