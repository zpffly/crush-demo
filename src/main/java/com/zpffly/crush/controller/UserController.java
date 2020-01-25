package com.zpffly.crush.controller;

import com.zpffly.crush.entity.Result;
import com.zpffly.crush.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 压测使用接口
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/info")
    @ResponseBody
    public Result<User> user(User user){
        return Result.success(user);
    }
}
