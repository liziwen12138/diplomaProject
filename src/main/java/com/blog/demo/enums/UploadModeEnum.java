package com.blog.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  UploadModeEnum {
    /**
     * 网络上传
     */
    OSS("oss","ossUploadStrategyImpl"),
    /**
     * 本地上传
     */
    LOCAL("local","localUploadStrategyImpl");

    /**
     * 模式
     */
    private final String mode;
    /**
     * 策略
     */
    private final String strategy;

    public static String getStrategy(String mode){
        for(UploadModeEnum value: UploadModeEnum.values()){
            if(value.getMode().equals(mode)){
                return value.getStrategy();
            }
        }
        return null;
    }
}
