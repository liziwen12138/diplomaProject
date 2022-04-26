package com.blog.demo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "标签视图")
public class TagVO {

    /**
     * id
     */
    @ApiModelProperty(name = "id", value = "id", dataType = "Integer")
    private Integer id;

    /**
     * 标签名称
     */
    @ApiModelProperty(name = "tagName", value = "标签名字", dataType = "String")
    private String tagName;

}
