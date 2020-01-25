package com.zpffly.crush.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class ValidatorUtil {

    public static boolean isMobile(String s){
        if (StringUtils.isEmpty(s))
            return false;
        Pattern pattern = Pattern.compile("1\\d{10}");
        return pattern.matcher(s).matches();
    }
}
