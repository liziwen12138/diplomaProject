package com.blog.demo.service;

import com.blog.demo.dto.*;
import com.blog.demo.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.demo.vo.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liziwen
 * @since 2022-03-16
 */
public interface ArticleService extends IService<Article> {

    /**
     * 文章新增和更新
     * @param articleVO
     */
    void saveOrUpdateArticle(ArticleVO articleVO);

    /**
     * 上传图片接口（使用策略模式实现（strategy目录中）此处不实现
     * @param file
     * @return
     */
    String saveArticleImages(MultipartFile file);

    /**
     * 获取后台文章列表
     * @param conditionVo
     * @return
     */
    PageResult<ArticleBackDTO> listArticleBacks(ConditionVo conditionVo);

    void updateArticleDelete(DeleteVO deleteVO);

    void deleteArticles(List<Integer> articleIdList);

    /**
     * 获取主页文章列表
     * @return
     */
    List<ArticleHomeDTO> listArticles();

    /**
     * 根据文章ID获取文章详情
     * @param articleId
     * @return
     */
    ArticleDTO getArticleById(Integer articleId);

    /**
     * 根据条件获取文章预览列表
     * @param conditionVo
     * @return
     */
    ArticlePreviewListDTO listArticlesByCondition(ConditionVo conditionVo);

    /**
     * 获取归档文章列表
     * @param conditionVo
     * @return
     */
    PageResult<ArchiveDTO> listArchives(ConditionVo conditionVo);

    /**
     * 保存文章点赞
     * @param articleId
     */
    void saveArticleLike(Integer articleId);

    /**
     * 修改文章置顶设置
     * @param articleTopVO
     */
    void updateArticleTop(ArticleTopVO articleTopVO);
}
