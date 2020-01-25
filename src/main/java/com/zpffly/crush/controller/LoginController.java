package com.zpffly.crush.controller;

import com.zpffly.crush.entity.Result;
import com.zpffly.crush.myenum.CrushEnum;
import com.zpffly.crush.service.UserService;
import com.zpffly.crush.vo.LoginVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.print.attribute.standard.PresentationDirection;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class LoginController {
    private static Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/doLogin")
    @ResponseBody
    public Result<String> doLogin(@Valid LoginVO loginVO, HttpServletResponse response){
        return userService.login(loginVO, response);
    }
}
