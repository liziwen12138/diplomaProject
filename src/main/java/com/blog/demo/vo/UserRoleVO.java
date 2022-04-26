package com.blog.demo.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "用户权限")
public class UserRoleVO {

    @NotNull(message = "id不能为空")
    private Integer userInfoId;
    @NotBlank(message = "昵称不能为空")
    private String nickname;;
    @NotNull(message = "角色列表不能为空")
    private List<Integer> roleIdList;

}
