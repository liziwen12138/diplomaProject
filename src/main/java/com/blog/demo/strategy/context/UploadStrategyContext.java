package com.blog.demo.strategy.context;

import com.blog.demo.strategy.UploadStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

import static com.blog.demo.enums.UploadModeEnum.getStrategy;

/**
 * 上传策略模式
 */
@Service
public class UploadStrategyContext {

    /**
     * 上传模式,可以再application.yml中配置
     */
    @Value("${upload.mode}")
    private String uploadMode;

    @Autowired
    private Map<String, UploadStrategy> uploadStrategyMap;

    /**
     * 上传文件
     * @param file
     * @param path
     * @return
     */
    public String executeUploadStrategy(MultipartFile file, String path){
        return uploadStrategyMap.get(getStrategy(uploadMode)).uploadFile(file,path);
    }
}
