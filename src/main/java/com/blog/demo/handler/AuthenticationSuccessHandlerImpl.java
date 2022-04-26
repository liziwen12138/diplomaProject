package com.blog.demo.handler;

import com.blog.demo.dto.UserInfoDTO;
import com.blog.demo.mapper.UserAuthMapper;
import com.blog.demo.util.BeanCopyUtils;
import com.blog.demo.util.UserUtils;
import com.blog.demo.vo.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {
    @Autowired
    private UserAuthMapper userAuthMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        获取当前已登录用户的信息
        UserInfoDTO userInfoDTO = BeanCopyUtils.copyObject(UserUtils.getLoginUser(), UserInfoDTO.class);
//        设置返回以json传输数据
        response.setContentType("application/json;charset=UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(Result.ok(userInfoDTO));
        response.getWriter().write(jsonString);
        System.out.println("登录验证成功");
    }

    /**
     * 更新用户信息，暂时不完成。
     */
//    public void updateUserInfo() {
//        UserAuth userAuth = UserAuth.builder()
//                .id(UserUtils.getLoginUser().getId())
//                .ipAddress(UserUtils.getLoginUser().getIpAddress())
//                .ipSource(UserUtils.getLoginUser().getIpSource())
//                .lastLoginTime(UserUtils.getLoginUser().getLastLoginTime())
//                .build();
//        userAuthDao.updateById(userAuth);
//    }

}
