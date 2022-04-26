package com.blog.demo.service;

import com.blog.demo.dto.CommentBackDTO;
import com.blog.demo.dto.CommentDTO;
import com.blog.demo.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.demo.vo.CommentVO;
import com.blog.demo.vo.ConditionVo;
import com.blog.demo.vo.PageResult;
import com.blog.demo.vo.ReviewVO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liziwen
 * @since 2022-04-02
 */
public interface CommentService extends IService<Comment> {

    /**
     * 返回后台评论列表
     * @param conditionVo
     * @return
     */
    PageResult<CommentBackDTO> listCommentBackDTO(ConditionVo conditionVo);

    /**
     * 根据评论id列表删除评论
     * @param commentIdList
     */
    void deleteComments(List<Integer> commentIdList);

    /**
     * 审核评论信息
     * @param reviewVO
     */
    void updateCommentsReview(ReviewVO reviewVO);

    /**
     * 获取评论列表
     * @param commentVO
     * @return
     */
    PageResult<CommentDTO> listComments(CommentVO commentVO);

    /**
     * 新增评论
     * @param commentVO
     */
    void saveComment(CommentVO commentVO) throws JsonProcessingException;
}
