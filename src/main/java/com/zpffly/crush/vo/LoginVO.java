package com.zpffly.crush.vo;

import com.zpffly.crush.annotation.IsMobile;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class LoginVO {

    @NotBlank(message = "电话号码不能为空")
    @IsMobile
    private String mobile;

    @NotBlank(message = "密码不能为空")
    private String password;
}
