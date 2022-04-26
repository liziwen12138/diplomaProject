package com.blog.demo.mapper;

import com.blog.demo.dto.CommentBackDTO;
import com.blog.demo.dto.CommentDTO;
import com.blog.demo.dto.ReplyCountDTO;
import com.blog.demo.dto.ReplyDTO;
import com.blog.demo.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.demo.vo.CommentVO;
import com.blog.demo.vo.ConditionVo;
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
 * @since 2022-04-02
 */
@Mapper
@Component(value = "commentMapper")
public interface CommentMapper extends BaseMapper<Comment> {

    Integer countCommentDTO(@Param("condition")ConditionVo conditionVo);

    List<CommentBackDTO> listCommentBackDTO(@Param("current")Long limitCurrent,@Param("size")Long size,@Param("condition")ConditionVo conditionVo);

    List<CommentDTO> listComments(@Param("current")Long limitCurrent,@Param("size")Long size,@Param("commentVO") CommentVO commentVO);

    List<ReplyDTO> listReplies(@Param("commentIdList") List<Integer> commentIdList);

    List<ReplyCountDTO> listReplyCountByCommentId(@Param("commentIdList") List<Integer> commentIdList);
}
