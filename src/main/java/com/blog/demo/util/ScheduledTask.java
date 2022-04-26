package com.blog.demo.util;

import com.blog.demo.constant.RedisPrefixConst;
import com.blog.demo.entity.WebsiteConfig;
import com.blog.demo.mapper.WebsiteConfigMapper;
import com.blog.demo.service.WebsiteConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Component
public class ScheduledTask {

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private WebsiteConfigService websiteConfigService;

    /**
     * 将redis中的aboutMe信息存入数据库并在redis中删除
     */
    @Scheduled(fixedRate = 180000)
    @Transactional(rollbackFor = Exception.class)
    public void deleteAboutInRedisAndSaveInDB(){
        Object value = redisUtils.get(RedisPrefixConst.ABOUT);
//        String aboutDetail = Objects.nonNull(value) ? value.toString() : "";
        if(Objects.nonNull(value)){
            String aboutDetail = value.toString();
            WebsiteConfig websiteConfig = websiteConfigService.getById(1);
            websiteConfig.setAboutMe(aboutDetail);
            websiteConfigService.saveOrUpdate(websiteConfig);
            redisUtils.del(RedisPrefixConst.ABOUT);
        }
    }
}
