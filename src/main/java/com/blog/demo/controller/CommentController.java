package com.blog.demo.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.demo.dto.CommentBackDTO;
import com.blog.demo.dto.CommentDTO;
import com.blog.demo.service.CommentService;
import com.blog.demo.util.PageUtils;
import com.blog.demo.vo.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.locks.Condition;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liziwen
 * @since 2022-04-02
 */
@Api(tags = "评论模块")
@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ApiOperation(value = "查询后台评论列表")
    @GetMapping("/admin/comments")
    public Result<PageResult<CommentBackDTO>> listCommentBackDTO(ConditionVo conditionVo){
        Page<Object> page = new Page<>(conditionVo.getCurrent(), conditionVo.getSize());
        PageUtils.setCurrentPage(page);
        return Result.ok(commentService.listCommentBackDTO(conditionVo));
    }

    @ApiOperation(value = "根据评论id列表删除后台评论")
    @DeleteMapping("/admin/comments")
    public Result<?> deleteComments(@RequestBody List<Integer> commentIdList){
        //直接调用mybatis-plus现成的api
        commentService.removeByIds(commentIdList);
        return Result.ok();
    }

    @ApiOperation(value = "审核评论")
    @PutMapping("/admin/comments/review")
    public Result<?> updateCommentsReview(@Valid @RequestBody ReviewVO reviewVO){
        commentService.updateCommentsReview(reviewVO);
        return Result.ok();
    }

    @ApiOperation(value = "获取评论列表")
    @GetMapping("/comments")
    public Result<PageResult<CommentDTO>> listComments(CommentVO commentVO, ConditionVo conditionVo){
        Page<Object> page = new Page<>(conditionVo.getCurrent(), 10);
        PageUtils.setCurrentPage(page);
        return Result.ok(commentService.listComments(commentVO));
    }

    @ApiOperation(value = "新增评论")
    @PostMapping("/comments")
    public Result<?> saveComment(@Valid @RequestBody CommentVO commentVO) throws JsonProcessingException {
        commentService.saveComment(commentVO);
        return Result.ok();
    }
}

