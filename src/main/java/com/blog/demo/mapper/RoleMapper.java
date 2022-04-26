package com.blog.demo.mapper;

import com.blog.demo.dto.ResourceRoleDTO;
import com.blog.demo.dto.RoleDTO;
import com.blog.demo.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.demo.vo.ConditionVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liziwen
 * @since 2022-03-07
 */
@Mapper
@Component(value = "roleMapper")    //此处的component是非必要的，这里是为了消除idea 在mapper自动注入时警示
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 根据用户名获取角色的权限列表。
     * @param userInfoId
     * @return
     */
    List<String> listRolesByUserInfoId(Integer userInfoId);

    /**
     * 查询路由角色列表
     *
     * @return 角色标签
     */
    List<ResourceRoleDTO> listResourceRoles();


    /**
     *
     * @param current
     * @param size
     * @param conditionVo
     * @return
     */
    List<RoleDTO> listRoles(@Param("current") Long current,@Param("size") Long size,@Param("conditionVO") ConditionVo conditionVo);
}
