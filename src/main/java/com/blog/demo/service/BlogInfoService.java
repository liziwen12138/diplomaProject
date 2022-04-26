package com.blog.demo.service;

import com.blog.demo.dto.BlogHomeInfoDTO;
import com.blog.demo.vo.AboutMeVO;
import com.blog.demo.vo.WebsiteConfigVO;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface BlogInfoService {


    BlogHomeInfoDTO getBlogHomeInfo() throws JsonProcessingException;

    WebsiteConfigVO getWebsiteConfig() throws JsonProcessingException;

    /**
     * 更改网站配置信息
     * @param websiteConfigVO
     */
    void updateWebsiteConfig(WebsiteConfigVO websiteConfigVO) throws JsonProcessingException;

    String getAbout();

    /**
     * 修改关于我信息
     * @param aboutMeVO
     */
    void updateAbout(AboutMeVO aboutMeVO);
}
