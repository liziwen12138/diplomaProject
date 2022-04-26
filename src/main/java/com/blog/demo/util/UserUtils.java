package com.blog.demo.util;

import com.blog.demo.dto.UserDetailDTO;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 用户工具类
 */
@Component
public class UserUtils {

    /**
     * 获取当前登录用户
     * @return
     */
    public static UserDetailDTO getLoginUser(){
//      通过Authentication.getPrincipal()可以获取到代表当前用户的信息，这个对象通常是UserDetails的实例。
//      获取当前用户的用户名是一种比较常见的需求。
//      此外，调用SecurityContextHolder.getContext()获取SecurityContext时，
//      如果对应的SecurityContext不存在，则Spring Security将为我们建立一个空的SecurityContext并进行返回。
        return (UserDetailDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
