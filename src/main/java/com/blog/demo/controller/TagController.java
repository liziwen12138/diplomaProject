package com.blog.demo.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.demo.dto.TagBackDTO;
import com.blog.demo.dto.TagDTO;
import com.blog.demo.dto.TagOptionDTO;
import com.blog.demo.service.TagService;
import com.blog.demo.util.PageUtils;
import com.blog.demo.vo.ConditionVo;
import com.blog.demo.vo.PageResult;
import com.blog.demo.vo.Result;
import com.blog.demo.vo.TagVO;
import io.swagger.annotations.Api;
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
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 根据搜索内容返回标签集合（用在标签选择）
     * @param conditionVo
     * @return
     */
    @ApiOperation(value = "搜索文章标签")
    @GetMapping("/admin/tags/search")
    public Result<List<TagOptionDTO>> listTagsBySearch(ConditionVo conditionVo){
        return Result.ok(tagService.listTagsBySearch(conditionVo));
    }

    @ApiOperation(value = "后台分类列表")
    @GetMapping("/admin/tags")
    public Result<PageResult<TagBackDTO>> listBackTags(ConditionVo conditionVo){
        Page<Object> page = new Page<>(conditionVo.getCurrent(), conditionVo.getSize());
        PageUtils.setCurrentPage(page);
        return Result.ok(tagService.listBackTags(conditionVo));
    }

    @ApiOperation(value = "新增或更新标签")
    @PostMapping("/admin/tags")
    public Result<?> saveOrUpdateTag(@Valid @RequestBody TagVO tagVO){
        tagService.saveOrUpdateTag(tagVO);
        return Result.ok();
    }

    @ApiOperation(value = "删除标签")
    @DeleteMapping("/admin/tags")
    public Result<?> deleteTags(@RequestBody List<Integer> idList){
        tagService.deleteTags(idList);
        return Result.ok();
    }

    @ApiOperation(value = "前台标签列表显示")
    @GetMapping("/tags")
    public Result<PageResult<TagDTO>> listTags(ConditionVo conditionVo){
        return Result.ok(tagService.listTags(conditionVo));
    }
}

