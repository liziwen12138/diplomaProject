package com.blog.demo.controller;


import com.blog.demo.dto.FriendLinkBackDTO;
import com.blog.demo.dto.FriendLinkDTO;
import com.blog.demo.service.FriendLinkService;
import com.blog.demo.vo.ConditionVo;
import com.blog.demo.vo.FriendLinkVO;
import com.blog.demo.vo.PageResult;
import com.blog.demo.vo.Result;
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
 * @since 2022-04-21
 */
@RestController
@Api(value = "页面控制器")
public class FriendLinkController {

    @Autowired
    private FriendLinkService friendLinkService;


    /**
     * 获取前台友链列表
     * @return
     */
    @GetMapping("/links")
    public Result<List<FriendLinkDTO>> listFriendLink(){
        return Result.ok(friendLinkService.listFriendLink());
    }

    /**
     * 获取后台页面列表
     * @param conditionVo
     * @return
     */
    @GetMapping("/admin/links")
    @ApiOperation("获取后台页面列表")
    public Result<PageResult<FriendLinkBackDTO>> listFriendLinkDTO(ConditionVo conditionVo){
        return Result.ok(friendLinkService.listFriendLinkDTO(conditionVo));
    }

    /**
     * 新增或修改友链
     * @param friendLinkVO
     * @return
     */
    @PostMapping("/admin/links")
    @ApiOperation("新增或修改友链")
    public Result<?> saveOrUpdateFriendLink(@Valid @RequestBody FriendLinkVO friendLinkVO){
        friendLinkService.saveOrUpdateFriendLink(friendLinkVO);
        return Result.ok();
    }

    /**
     * 根据id列表删除友链
     * @param idList
     * @return
     */
    @DeleteMapping("/admin/links")
    @ApiOperation("根据id列表删除友链")
    public Result<?> deleteFriendLink(@RequestBody List<Integer> idList){
        friendLinkService.removeByIds(idList);
        return Result.ok();
    }
}

