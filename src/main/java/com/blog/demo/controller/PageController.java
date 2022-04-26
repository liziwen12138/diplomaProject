package com.blog.demo.controller;


import com.blog.demo.service.PageService;
import com.blog.demo.vo.PageVO;
import com.blog.demo.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 页面 前端控制器
 * </p>
 *
 * @author liziwen
 * @since 2022-03-25
 */
@RestController
@Api(value = "页面控制器")
public class PageController {

    @Autowired
    private PageService pageService;

    /**
     * 获得页面列表
     * @return
     */
    @GetMapping("/admin/pages")
    @ApiOperation("获得页面列表")
    public Result<List<PageVO>> listPages(){
        return Result.ok(pageService.listPages());
    }

    /**
     * 新增或修改页面
     * @param pageVO
     * @return
     */
    @PostMapping("/admin/pages")
    @ApiOperation("新增或修改页面")
    public Result<?> saveOrUpdatePage(@Valid @RequestBody PageVO pageVO){
        pageService.saveOrUpdatePage(pageVO);
        return Result.ok();
    }

    /**
     * 根据id删除页面
     * @param pageId
     * @return
     */
    @DeleteMapping("/admin/pages/{pageId}")
    @ApiOperation("根据id删除页面")
    public Result<?> deletePage(@PathVariable("pageId") Integer pageId){
        pageService.deletePage(pageId);
        return Result.ok();
    }

}

