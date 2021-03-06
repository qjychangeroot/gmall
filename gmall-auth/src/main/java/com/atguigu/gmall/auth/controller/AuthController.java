package com.atguigu.gmall.auth.controller;

import com.atguigu.core.bean.Resp;
import com.atguigu.core.utils.CookieUtils;
import com.atguigu.gmall.auth.config.JwtProperties;
import com.atguigu.gmall.auth.service.AuthService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/accredit")
    public Resp<Object> accredit(@RequestParam("username")String userName, @RequestParam("password")String password, HttpServletRequest request, HttpServletResponse response){

        String jwtToken = this.authService.accredit(userName, password);
        if (StringUtils.isEmpty(jwtToken)){
            return Resp.fail("登录认证失败");
        }

        //4. 把生成的jwt放入redis中
        CookieUtils.setCookie(request,response,this.jwtProperties.getCookieName(),jwtToken,this.jwtProperties.getExpire() * 60);

        return Resp.ok(null);
    }


}
