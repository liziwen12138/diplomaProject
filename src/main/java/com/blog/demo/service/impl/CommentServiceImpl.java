package com.blog.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blog.demo.constant.CommonConst;
import com.blog.demo.dto.CommentBackDTO;
import com.blog.demo.dto.CommentDTO;
import com.blog.demo.dto.ReplyCountDTO;
import com.blog.demo.dto.ReplyDTO;
import com.blog.demo.entity.Comment;
import com.blog.demo.enums.ArticleStatusEnum;
import com.blog.demo.enums.CommentTypeEnum;
import com.blog.demo.enums.StatusCodeEnum;
import com.blog.demo.mapper.CommentMapper;
import com.blog.demo.service.BlogInfoService;
import com.blog.demo.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.demo.util.HTMLUtils;
import com.blog.demo.util.PageUtils;
import com.blog.demo.util.UserUtils;
import com.blog.demo.vo.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.blog.demo.enums.CommentTypeEnum.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.blog.demo.enums.CommentTypeEnum.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liziwen
 * @since 2022-04-02
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private BlogInfoService blogInfoService;

    @Override
    public PageResult<CommentBackDTO> listCommentBackDTO(ConditionVo conditionVo) {
        Integer count = commentMapper.countCommentDTO(conditionVo);
        if(count <= 0){
            return new PageResult<>();
        }
        List<CommentBackDTO> commentBackDTOS = commentMapper.listCommentBackDTO(PageUtils.getLimitCurrent(), PageUtils.getSize(), conditionVo);
        return new PageResult<>(commentBackDTOS,count);
    }

    @Override
    public void deleteComments(List<Integer> commentIdList) {

    }

    @Override
    public void updateCommentsReview(ReviewVO reviewVO) {
        List<Comment> commentList = reviewVO.getIdList().stream().map(item -> Comment.builder()
                .id(item)
                .isReview(reviewVO.getIsReview())
                .build()).collect(Collectors.toList());
        this.updateBatchById(commentList);
    }

    @Override
    public PageResult<CommentDTO> listComments(CommentVO commentVO) {
        //查询评论量
        Integer count = commentMapper.selectCount(new LambdaQueryWrapper<Comment>()
                .eq(ARTICLE.getType().equals(commentVO.getType()), Comment::getArticleId, commentVO.getArticleId())
                .eq(LINK.getType().equals(commentVO.getType()),Comment::getType,commentVO.getType())
                .eq(TALK.getType().equals(commentVO.getType()),Comment::getTalkId,commentVO.getTalkId())
                .isNull(Comment::getParentId)
                .eq(Comment::getIsReview, CommonConst.TRUE));
        if(count == 0){
            return new PageResult<>();
        }
        //获取评论列表
        List<CommentDTO> commentDTOList = commentMapper.listComments(PageUtils.getLimitCurrent(), PageUtils.getSize(), commentVO);
        if(Objects.isNull(commentDTOList)){
            return new PageResult<>();
        }
        //获取评论的id集合
        List<Integer> commentIdList = commentDTOList.stream().map(CommentDTO::getId).collect(Collectors.toList());
        //根据评论id分组获取子评论
        List<ReplyDTO> replyDTOList = commentMapper.listReplies(commentIdList);
        //根据评论id分组子评论
        Map<Integer, List<ReplyDTO>> replyMap = replyDTOList.stream().collect(Collectors.groupingBy(ReplyDTO::getParentId));
        //根据评论id查询回复量
        Map<Integer, Integer> replyCountMap = commentMapper.listReplyCountByCommentId(commentIdList).stream().collect(Collectors.toMap(ReplyCountDTO::getCommentId, ReplyCountDTO::getReplyCount));
        commentDTOList.forEach(item -> {
            item.setLikeCount(1);
            item.setReplyCount(replyCountMap.get(item.getId()));
            item.setReplyDTOList(replyMap.get(item.getId()));
        });
        return new PageResult<>(commentDTOList,count);
    }

    @Override
    public void saveComment(CommentVO commentVO) throws JsonProcessingException {
        //是否需要经过审核（通过系统配置信息获取设置信息）
        WebsiteConfigVO websiteConfig = blogInfoService.getWebsiteConfig();
        Integer isReview = websiteConfig.getIsCommentReview();
        //对评论内容进行不符合标签进行过滤，并重新协会commentVO中
        commentVO.setCommentContent(HTMLUtils.deleteTag(commentVO.getCommentContent()));
        //封装comment并保存
        Comment comment = Comment.builder().isReview(isReview == CommonConst.TRUE ? CommonConst.FALSE : CommonConst.TRUE)
                .userId(UserUtils.getLoginUser().getUserInfoId())
                .replyUserId(commentVO.getReplyUserId())
                .articleId(commentVO.getArticleId())
                .talkId(commentVO.getTalkId())
                .commentContent(commentVO.getCommentContent())
                .parentId(commentVO.getParentId())
                .type(commentVO.getType())
                .build();
        commentMapper.insert(comment);
        //判断是否开启邮箱通知用户，咱不实现
    }
}
