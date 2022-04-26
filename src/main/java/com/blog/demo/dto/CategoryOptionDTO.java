package com.blog.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分类选项数据传递
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryOptionDTO {

    /**
     * 分类ID
     */
    private Integer id;

    /**
     * 分类名
     */
    private String categoryName;

}

