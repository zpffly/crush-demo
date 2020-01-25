package com.zpffly.crush.config;

import com.zpffly.crush.constant.CookieConstant;
import com.zpffly.crush.entity.User;
import com.zpffly.crush.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType() == User.class;
    }

    /**
     * 将对User参数的操作放在这里
     * 为了兼容某些将cookie通过url传输，使用RequestParam取url中参数
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request =  nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
        assert request != null;
        String paramToken = request.getParameter(CookieConstant.TOKEN);
        String cookieToken = getCookieValue(request, CookieConstant.TOKEN);
        String token = StringUtils.isEmpty(cookieToken) ? paramToken : cookieToken;
        if (StringUtils.isEmpty(token))
            return null;
        return userService.getByToken(response,token);
//        return UserContext.get();
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
