package com.zpffly.crush.service;

import com.zpffly.crush.constant.CookieConstant;
import com.zpffly.crush.constant.RedisConstant;
import com.zpffly.crush.dao.UserDao;
import com.zpffly.crush.entity.Result;
import com.zpffly.crush.entity.User;
import com.zpffly.crush.myenum.CrushEnum;
import com.zpffly.crush.util.MD5Util;
import com.zpffly.crush.util.UUIDUtil;
import com.zpffly.crush.vo.LoginVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    private static Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisTemplate<String, User> template;

    public Result<String> login(LoginVO loginVO, HttpServletResponse response){
        if (loginVO == null)
            return Result.error(CrushEnum.USER_IS_NULL);
        String mobile = loginVO.getMobile();
        String password = loginVO.getPassword();
        // 从数据库查询
        User user = userDao.getUserById(Long.parseLong(mobile));
        if (user == null){
            logger.info("[不存在的用户登录]，手机号码：{}", mobile);
            return Result.error(CrushEnum.USER_NOT_FOUND);
        }
        // 存储在数据库的密码
        String dbPass = user.getPassword();
        String salt = user.getSalt();
        // 将客户端传过来的密码再次加盐，判断是否跟数据库加盐后的密码一致
        if (!MD5Util.serverEncoding(password, salt).equals(dbPass)){
            logger.info("[用户登录密码错误]，手机号码：{}，密码{}", mobile, password);
            return Result.error(CrushEnum.PASSWORD_ERROR);
        }
        logger.info("[用户登录成功]，手机号码：{}", mobile);

        // 登录成功，添加cookie
        String token = UUIDUtil.generate();
        addCookie(response, user,token);

        return Result.success(token);
    }

    public void addCookie(HttpServletResponse response, User user, String token){
        template.opsForValue().set(token, user, RedisConstant.EXPIRY, TimeUnit.SECONDS);
        Cookie cookie = new Cookie(CookieConstant.TOKEN, token);
        cookie.setMaxAge(CookieConstant.EXPIRY);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public void updateCookieAndToken(HttpServletResponse response, String token){
        // 更新key最后活跃时间
        template.expire(token, RedisConstant.EXPIRY, TimeUnit.SECONDS);
        Cookie cookie = new Cookie(CookieConstant.TOKEN, token);
        cookie.setMaxAge(CookieConstant.EXPIRY);
        cookie.setPath("/");
        response.addCookie(cookie);
    }


    /**
     * 根据token查找用户
     * @param token
     * @return
     */
    public User getByToken(HttpServletResponse response, String token){
        if (StringUtils.isEmpty(token))
            return null;
//        logger.info("根据token：{}查找用户", token);
        User user = template.opsForValue().get(token);
        // 更新用户生存时间
        if (user != null)
            updateCookieAndToken(response, token);
        return user;
    }
}
