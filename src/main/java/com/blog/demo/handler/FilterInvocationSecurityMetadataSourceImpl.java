package com.blog.demo.handler;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.blog.demo.dto.ResourceDTO;
import com.blog.demo.dto.ResourceRoleDTO;
import com.blog.demo.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;

/**
 * 接口拦截规则
 * 实现动态权限
 */
@Component
public class FilterInvocationSecurityMetadataSourceImpl implements FilterInvocationSecurityMetadataSource {

    private static List<ResourceRoleDTO> resourceRoleList;

    @Autowired
    private RoleMapper roleMapper;

    /**
     * 加载资源角色信息
     */
    @PostConstruct
    private void loadDataSource() {
        resourceRoleList = roleMapper.listResourceRoles();
    }

    /**
     * 清空接口角色信息
     */
    public void clearDataSource() {
        resourceRoleList = null;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        //修改接口角色关系后重新加载
        if(CollectionUtils.isEmpty(resourceRoleList)){
            this.loadDataSource();
        }
        FilterInvocation fi = (FilterInvocation) object;
        //获取用户请求方式
        String method = fi.getRequest().getMethod();
        //获取请求url
        String url = fi.getRequest().getRequestURI();
        AntPathMatcher matcher = new AntPathMatcher();
        //获取接口角色信息，若为匿名接口就放行，若无对应角色则禁止
        for(ResourceRoleDTO resourceRoleDTO : resourceRoleList){
            if(matcher.match(resourceRoleDTO.getUrl(), String.valueOf(url)) && resourceRoleDTO.getRequestMethod().equals(method)){
                List<String> roleList = resourceRoleDTO.getRoleList();
                if(CollectionUtils.isEmpty(roleList)){
                    return SecurityConfig.createList("disable");
                }
                return SecurityConfig.createList(roleList.toArray(new String[]{}));
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
