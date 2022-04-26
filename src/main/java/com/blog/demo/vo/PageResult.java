package com.blog.demo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "分页对象")
public class PageResult<T> {

    @ApiModelProperty(name = "recordList",value = "分页列表",required = true,dataType = "List<T>")
    private List<T> recordList;

    @ApiModelProperty(name = "count",value = "总数",required = true,dataType = "Integer")
    private Integer count;
}
