package com.blog.demo.mapper;

import com.blog.demo.dto.ArticleBackDTO;
import com.blog.demo.dto.ArticleDTO;
import com.blog.demo.dto.ArticleHomeDTO;
import com.blog.demo.dto.ArticlePreviewDTO;
import com.blog.demo.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
 * @since 2022-03-16
 */
@Mapper
@Component(value = "articleMapper")
public interface ArticleMapper extends BaseMapper<Article> {

    Integer countArticleBacks(@Param("current")Long current, @Param("size")Long size,@Param("condition")ConditionVo conditionVo);

    List<ArticleBackDTO> listArticleBacks(@Param("current")Long current,@Param("size")Long size,@Param("condition")ConditionVo conditionVo);

    List<ArticleHomeDTO> listArticles(@Param("current")Long limitCurrent,@Param("size")Long size);

    ArticleDTO getArticleById(@Param("articleId")Integer articleId);

    List<ArticlePreviewDTO> listArticlesByCondition(@Param("current") Long limitCurrent,@Param("size") Long size,@Param("condition") ConditionVo conditionVo);
}
