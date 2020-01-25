package com.zpffly.crush.config;

import com.zpffly.crush.entity.User;

public class UserContext {
    private static final ThreadLocal<User> threadLocal = new ThreadLocal<>();

    public static void set(User user){
        threadLocal.set(user);
    }

    public static User get(){
        return threadLocal.get();
    }
}
