package com.blog.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagOptionDTO {

    /**
     * ID
     */
    private Integer id;

    /**
     * 标签名
     */
    private String tagName;

}
