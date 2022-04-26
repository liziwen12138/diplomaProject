package com.blog.demo.controller;


import com.blog.demo.dto.LabelOptionDTO;
import com.blog.demo.dto.ResourceDTO;
import com.blog.demo.service.ResourceService;
import com.blog.demo.vo.ConditionVo;
import com.blog.demo.vo.ResourceVO;
import com.blog.demo.vo.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 *
 * 前端资源控制类
 * @author liziwen
 * @since 2022-04-15
 */
@RestController
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    /**
     * 获取资源选项列表
     * @return
     */
    @GetMapping("/admin/role/resources")
    @ApiOperation(value = "获取资源选项列表")
    public Result<List<LabelOptionDTO>> listResourceOption(){
        return Result.ok(resourceService.listResourceOption());
    }


    @ApiOperation(value = "获取资源列表")
    @GetMapping("/admin/resources")
    public Result<List<ResourceDTO>> listResources(ConditionVo conditionVo){
        return Result.ok(resourceService.listResources(conditionVo));
    }

    @ApiOperation(value = "新增资源或更新")
    @PostMapping("/admin/resources")
    public Result<?> saveOrUpdate(@Valid @RequestBody ResourceVO resourceVO){
        resourceService.saveOrUpdateByIdList(resourceVO);
        return Result.ok();
    }

    @DeleteMapping("/admin/resources/{resourceId}")
    @ApiOperation(value = "根据id删除资源")
    public Result<?> deleteResource(@PathVariable("resourceId")Integer resourceId){
        resourceService.deleteResource(resourceId);
        return Result.ok();
    }

}

