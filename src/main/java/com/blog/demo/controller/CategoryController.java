package com.blog.demo.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.demo.dto.CategoryBackDTO;
import com.blog.demo.dto.CategoryDTO;
import com.blog.demo.dto.CategoryOptionDTO;
import com.blog.demo.service.CategoryService;
import com.blog.demo.util.PageUtils;
import com.blog.demo.vo.CategoryVo;
import com.blog.demo.vo.ConditionVo;
import com.blog.demo.vo.PageResult;
import com.blog.demo.vo.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liziwen
 * @since 2022-03-16
 */
@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "前台分类列表")
    @GetMapping("/categories")
    public Result<PageResult<CategoryDTO>> listCategories(ConditionVo conditionVo){
        return Result.ok(categoryService.listCategories(conditionVo));
    }

    /**
     * 根据搜索内容返回文章类型列表
     * @param conditionVo
     * @return
     */
    @ApiOperation(value = "搜索文章分类")
    @GetMapping("/admin/categories/search")
    public Result<List<CategoryOptionDTO>> listCategoriesBySearch(ConditionVo conditionVo){
        return Result.ok(categoryService.listCategoriesBySearch(conditionVo));
    }

    @ApiOperation(value = "后台分类列表")
    @GetMapping("/admin/categories")
    public Result<PageResult<CategoryBackDTO>> listBackCategories(ConditionVo conditionVo){
        Page<Object> page = new Page<>(conditionVo.getCurrent(), conditionVo.getSize());
        PageUtils.setCurrentPage(page);
        return Result.ok(categoryService.listBackCategories(conditionVo));
    }

    /**
     * 新增或更改分类
     * @param categoryVo
     * @return
     */
    @ApiOperation(value = "新增或更改分类")
    @PostMapping("/admin/categories")
    public Result<?> saveOrUpdateCategory(@Valid @RequestBody CategoryVo categoryVo){
        categoryService.saveOrUpdateCategory(categoryVo);
        return Result.ok();
    }

    /**
     * 删除分类
     * @param idList
     * @return
     */
    @ApiOperation(value = "删除分类")
    @DeleteMapping("/admin/categories")
    public Result<?> deleteCategories(@RequestBody List<Integer> idList){
        categoryService.deleteCategories(idList);
        return Result.ok();
    }
}

