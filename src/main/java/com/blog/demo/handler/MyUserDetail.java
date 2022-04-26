//package com.blog.demo.handler;
//
//import lombok.*;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//import java.util.Set;
//
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@ToString
//public class MyUserDetail implements UserDetails {
//
//    private String password;
//    private String username;
////    身份集合
//    private Set<GrantedAuthority> authorities;
//    private  boolean accountNonExpired = true;
//    private  boolean accountNonLocked = true;
//    private  boolean credentialsNonExpired = true;
//    private  boolean enabled = true;
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return this.authorities;
//    }
//
//    @Override
//    public String getPassword() {
//        return this.password;
//    }
//
//    @Override
//    public String getUsername() {
//        return this.username;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return this.isAccountNonLocked();
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return this.accountNonLocked;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return this.credentialsNonExpired;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return this.enabled;
//    }
//}
