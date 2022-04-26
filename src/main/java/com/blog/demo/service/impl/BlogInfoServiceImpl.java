package com.blog.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blog.demo.constant.CommonConst;
import com.blog.demo.constant.RedisPrefixConst;
import com.blog.demo.dto.BlogHomeInfoDTO;
import com.blog.demo.entity.Article;
import com.blog.demo.entity.WebsiteConfig;
import com.blog.demo.enums.ArticleStatusEnum;
import com.blog.demo.mapper.*;
import com.blog.demo.service.BlogInfoService;
import com.blog.demo.service.PageService;
import com.blog.demo.service.WebsiteConfigService;
import com.blog.demo.util.BeanCopyUtils;
import com.blog.demo.util.RedisUtils;
import com.blog.demo.vo.AboutMeVO;
import com.blog.demo.vo.PageVO;
import com.blog.demo.vo.WebsiteConfigVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;


@Service
public class BlogInfoServiceImpl implements BlogInfoService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private WebsiteConfigMapper websiteConfigMapper;
    @Autowired
    private PageService pageService;
    @Autowired
    private RedisUtils redisUtils;


    @Override
    public BlogHomeInfoDTO getBlogHomeInfo() throws JsonProcessingException {
        //查询文章的数量
        Integer articleCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getStatus, ArticleStatusEnum.PUBLIC.getStatus())
                .eq(Article::getIsDelete, CommonConst.FALSE));
        //查询分类的数量
        Integer categoryCount = categoryMapper.selectCount(null);
        //查询标签的数量
        Integer tagCount = tagMapper.selectCount(null);
        //查询访问量（需要redis）
        //查询网站配置（需要redis）
        WebsiteConfigVO websiteConfig = this.getWebsiteConfig();
        //查询页面图片
        List<PageVO> pageVOList = pageService.listPages();
        //封装数据
        BlogHomeInfoDTO blogHomeInfoDTO = BlogHomeInfoDTO.builder()
                .articleCount(articleCount)
                .categoryCount(categoryCount)
                .tagCount(tagCount)
                .viewsCount("0")
                .websiteConfig(websiteConfig)
                .pageList(pageVOList)
                .build();
        return blogHomeInfoDTO;
    }

    @Override
    public WebsiteConfigVO getWebsiteConfig() throws JsonProcessingException {
        WebsiteConfigVO websiteConfigVO;
        //内部逻辑需要redis来实现
        String config = websiteConfigMapper.selectById(1).getConfig();
        //数据库中将网站设置内容设置为json字符串，需要转换成对象
        ObjectMapper mapper = new ObjectMapper();
        websiteConfigVO = mapper.readValue(config, WebsiteConfigVO.class);
        return websiteConfigVO;
    }

    @Override
    public void updateWebsiteConfig(WebsiteConfigVO websiteConfigVO) throws JsonProcessingException {
        //将前端传递的网站配置参数转化为json字符串保存到数据库中
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(websiteConfigVO);
        WebsiteConfig websiteConfig = WebsiteConfig.builder().id(1).config(jsonString).build();
        websiteConfigMapper.updateById(websiteConfig);
    }

    @Override
    public String getAbout() {
        Object value = redisUtils.get(RedisPrefixConst.ABOUT);
        //事先判断一下redis中是否存在value值,若不存在则从数据库中查找，若数据库中仍不存在则返回空
        if(Objects.nonNull(value)){
            return value.toString();
        }else {
            String aboutMe = websiteConfigMapper.selectById(1).getAboutMe();
            return Objects.nonNull(aboutMe) ? aboutMe: "";
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAbout(AboutMeVO aboutMeVO) {
        //关于我信息直接修改仅redis中
        redisUtils.set(RedisPrefixConst.ABOUT,aboutMeVO.getAboutContent());
    }
}
