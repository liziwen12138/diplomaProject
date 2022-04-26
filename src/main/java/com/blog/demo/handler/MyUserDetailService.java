//package com.blog.demo.handler;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.blog.demo.entity.UserAuth;
//import com.blog.demo.mapper.UserAuthMapper;
//import com.blog.demo.service.UserAuthService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//@Service
//public class MyUserDetailService implements UserDetailsService {
//
//    @Autowired
//    private UserAuthService userAuthService;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        QueryWrapper<UserAuth> wrapper = new QueryWrapper<>();
//        wrapper.eq("username","admin");
////        get current role's auth detail
//        UserAuth userAuth = userAuthService.getOne(wrapper);
//        if(userAuth == null){
//            throw new UsernameNotFoundException("no username");
//        }
//        //        get current roles
//        List<String> roles = userAuthService.getUserRolesByUsername(userAuth.getId());
//        MyUserDetail userDetail = new MyUserDetail();
//        //      make judge for roles, if roles is not null,
//        //      create a set collection to reserve roles's authority
//        if(roles.size() > 0) {
//            Set<GrantedAuthority> authorities = new HashSet<>();
//            SimpleGrantedAuthority authority;
//            for (String role : roles) {
//                authority = new SimpleGrantedAuthority(role);
//                authorities.add(authority);
//            }
////            set roles's authority to myUserDetail
//            userDetail.setAuthorities(authorities);
//        }
//        userDetail.setUsername(userAuth.getUsername());
//        userDetail.setPassword("{noop}" + userAuth.getPassword());
//        return userDetail;
//    }
//}
