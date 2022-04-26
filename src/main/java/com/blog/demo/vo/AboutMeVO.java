package com.blog.demo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "关于我信息类")
public class AboutMeVO {

    /**
     * 关于我详情
     */
    @ApiModelProperty(name = "aboutMeDetail",value = "关于我详情",dataType = "String",required = true)
    private String aboutContent;;
}
