package com.zpffly.crush.intercept;

import com.zpffly.crush.config.UserContext;
import com.zpffly.crush.constant.CookieConstant;
import com.zpffly.crush.entity.User;
import com.zpffly.crush.service.UserService;
import org.aopalliance.intercept.Interceptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CrushIntercept implements HandlerInterceptor {

    @Autowired
    UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = getUser(request, response);
        if (user != null){
            UserContext.set(user);
            return true;
        }
        return false;
    }



    private User getUser(HttpServletRequest request, HttpServletResponse response){
        assert request != null;
        String paramToken = request.getParameter(CookieConstant.TOKEN);
        String cookieToken = getCookieValue(request, CookieConstant.TOKEN);
        String token = StringUtils.isEmpty(cookieToken) ? paramToken : cookieToken;
        if (StringUtils.isEmpty(token))
            return null;
        return userService.getByToken(response,token);
    }

    /**
     * 获取cookie的值
     * @param request
     * @param token
     * @return
     */
    private String getCookieValue(HttpServletRequest request, String token) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length <= 0)
            return null;
        for (Cookie cookie : cookies){
            if (cookie.getName().equals(token))
                return cookie.getValue();
        }
        return null;
    }
}
