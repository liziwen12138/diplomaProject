package com.blog.demo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 注册用户类
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "用户")
public class UserVO {

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不对")
    @ApiModelProperty(name = "username",value = "用户名",required = true,dataType = "String")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, message = "密码不能少于6为")
    @ApiModelProperty(name = "password", value = "密码", required = true, dataType = "String")
    private String password;

}
