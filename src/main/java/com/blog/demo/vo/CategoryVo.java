package com.blog.demo.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "分类")
public class CategoryVo {

    /**
     * id
     */
    @ApiModelProperty(name = "id",value = "分类id", dataType = "Integer")
    private Integer id;

    /**
     * 类型名称
     */
    @NotBlank(message = "分类名称不能为空")
    @ApiModelProperty(name = "categoryName",value = "分类名称",dataType = "String")
    private String categoryName;

}
