package com.blog.demo.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * 页面
 * </p>
 *
 * @author liziwen
 * @since 2022-03-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("tb_page")
@ApiModel(value="Page对象", description="页面")
public class Page implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "页面id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "页面名")
    private String pageName;

    @ApiModelProperty(value = "页面标签")
    private String pageLabel;

    @ApiModelProperty(value = "页面封面")
    private String pageCover;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;


}
