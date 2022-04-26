package com.blog.demo.controller;

import com.blog.demo.dto.BlogHomeInfoDTO;
import com.blog.demo.service.BlogInfoService;
import com.blog.demo.service.WebsiteConfigService;
import com.blog.demo.vo.AboutMeVO;
import com.blog.demo.vo.Result;
import com.blog.demo.vo.WebsiteConfigVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
public class BlogInfoController {

    @Autowired
    private BlogInfoService blogInfoService;

    @ApiOperation("查看博客首页信息")
    @GetMapping("/")
    public Result<BlogHomeInfoDTO> getBlogHomeInfo() throws JsonProcessingException {
        return Result.ok(blogInfoService.getBlogHomeInfo());
    }

    @GetMapping("/admin/website/config")
    @ApiOperation("获取网站配置信息")
    public Result<WebsiteConfigVO> getWebsiteConfig() throws JsonProcessingException {
        return Result.ok(blogInfoService.getWebsiteConfig());
    }

    @PutMapping("/admin/website/config")
    @ApiOperation("更改网站配置信息")
    public Result<?> updateWebsiteConfig(@Valid @RequestBody WebsiteConfigVO websiteConfigVO) throws JsonProcessingException {
        blogInfoService.updateWebsiteConfig(websiteConfigVO);
        return Result.ok();
    }

    /**
     * 获取关于我信息
     * @return
     */
    @GetMapping("/about")
    public Result<String> getAbout(){
        return Result.ok(blogInfoService.getAbout());
    }

    /**
     * 修改关于我信息
     * @param aboutMeVO
     * @return
     */
    @PutMapping("/admin/about")
    public Result<?> updateAbout(@Valid @RequestBody AboutMeVO aboutMeVO){
        blogInfoService.updateAbout(aboutMeVO);
        return Result.ok();
    }
}
