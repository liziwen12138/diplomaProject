package com.blog.demo.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.demo.dto.*;
import com.blog.demo.entity.Article;
import com.blog.demo.enums.FilePathEnum;
import com.blog.demo.service.ArticleService;
import com.blog.demo.strategy.context.UploadStrategyContext;
import com.blog.demo.util.PageUtils;
import com.blog.demo.vo.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

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
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UploadStrategyContext uploadStrategyContext;

    /**
     *获取归档文章列表
     * @param conditionVo
     * @return
     */
    @ApiOperation("获取归档文章列表")
    @GetMapping("/articles/archives")
    public Result<PageResult<ArchiveDTO>> listArchives(ConditionVo conditionVo){
        Page<Object> page = new Page<>(conditionVo.getCurrent(), 10);
        PageUtils.setCurrentPage(page);
        return Result.ok(articleService.listArchives(conditionVo));
    }

    /**
     * 获取文章列表
     * @param conditionVo
     * @return
     */
    @ApiOperation("获取文章列表")
    @GetMapping("/articles")
    public Result<List<ArticleHomeDTO>> listArticles(ConditionVo conditionVo){
        Page<Object> page = new Page<>(conditionVo.getCurrent(),5);
        PageUtils.setCurrentPage(page);
        return Result.ok(articleService.listArticles());
    }

    /**
     * 添加或更新文章
     * @param articleVO
     * @return
     */
    @ApiOperation(value = "添加或更新文章")
    @PostMapping("/admin/articles")
    public Result<?> saveOrUpdateArticle(@Valid @RequestBody ArticleVO articleVO){
        //此处的两个注解@Valid @RequestBody，是将前端提交的数据进行规则检验（就是用valid校验requestBody参数，并装载于articleVo中
        articleService.saveOrUpdateArticle(articleVO);
        return Result.ok();
    }

    @ApiOperation(value = "后台查看文章列表")
    @GetMapping("/admin/articles")
    public Result<PageResult<ArticleBackDTO>> listArticleBacks(ConditionVo conditionVo){
        Page<Object> page = new Page<>(conditionVo.getCurrent(), conditionVo.getSize());
        PageUtils.setCurrentPage(page);
        return Result.ok(articleService.listArticleBacks(conditionVo));
    }

    /**
     * 上传文章图片
     * @param file
     * @return
     */
    @ApiOperation(value = "上传文章图片")
    @PostMapping("/admin/articles/images")
    public Result<String> saveArticleImages(MultipartFile file){
        return Result.ok(uploadStrategyContext
                .executeUploadStrategy(file, FilePathEnum.ARTICLE.getPath()));
    }

    /**
     * 恢复或逻辑删除文章
     * 这里的删除是逻辑删除，只是隐藏而不是真实删除
     * 注意恢复的策略实际就是再发送一次逻辑删除请求，只是这次请求返回的isDelete属性为0
     * 恢复后的文章撤销置顶
     * @param deleteVO
     * @return
     */
    @PutMapping("/admin/articles")
    public Result<?> updateArticleDelete(@Valid @RequestBody DeleteVO deleteVO){
        articleService.updateArticleDelete(deleteVO);
        return Result.ok();
    }

    /**
     * 物理真实删除文章
     * @param articleIdList
     * @return
     */
    @DeleteMapping("/admin/articles")
    public Result<?> deleteArticles(@RequestBody List<Integer> articleIdList){
        articleService.deleteArticles(articleIdList);
        return Result.ok();
    }

    /**
     * 根据id查看文章
     *
     * @param articleId 文章id
     * @return {@link Result<ArticleDTO>} 文章信息
     */
    @ApiOperation(value = "根据id查看文章")
    @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "Integer")
    @GetMapping("/articles/{articleId}")
    public Result<ArticleDTO> getArticleById(@PathVariable("articleId") Integer articleId) {
        return Result.ok(articleService.getArticleById(articleId));
    }

    /**
     * 根据条件获取文章预览列表
     * @param conditionVo
     * @return
     */
    @ApiOperation(value = "根据条件获取文章预览列表")
    @GetMapping("/articles/condition")
    public Result<ArticlePreviewListDTO> listArticlesByCondition(ConditionVo conditionVo){
        Page<Object> page = new Page<>(conditionVo.getCurrent(),5);
        PageUtils.setCurrentPage(page);
        return Result.ok(articleService.listArticlesByCondition(conditionVo));
    }

    /**
     * 保存点赞
     * @param articleId
     * @return
     */
    @PostMapping("/articles/{articleId}/like")
    public Result<?> saveArticleLike(@PathVariable("articleId") Integer articleId){
        articleService.saveArticleLike(articleId);
        return Result.ok();
    }

    /**
     * 修改文章的置顶信息
     * @param articleTopVO
     * @return
     */
    @PutMapping("/admin/articles/top")
    public Result<?> updateArticleTop(@Valid @RequestBody ArticleTopVO articleTopVO){
        articleService.updateArticleTop(articleTopVO);
        return Result.ok();
    }

}

