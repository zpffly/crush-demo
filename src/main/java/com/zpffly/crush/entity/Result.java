package com.zpffly.crush.entity;


import com.zpffly.crush.myenum.CrushEnum;
import lombok.Data;

@Data
public class Result <T> {
    private int code;
    private String msg;
    private T data;

    private static <T> Result<T> get(){
        return new Result<>();
    }


    public static <T> Result<T> success(T data){
        Result<T> result = get();
        result.setCode(0);
        result.setData(data);
        result.setMsg("success");
        return result;
    }

    public static <T> Result<T> success(CrushEnum crushEnum, T data){
        Result<T> result = get();
        result.setCode(crushEnum.getCode());
        result.setMsg(crushEnum.getMsg());
        result.setData(data);
        return result;
    }

    public static <T> Result<T> success(CrushEnum crushEnum){
        return success(crushEnum, null);
    }

    public static <T> Result<T> error(CrushEnum crushEnum){
        Result<T> result = get();
        result.setMsg(crushEnum.getMsg());
        result.setCode(crushEnum.getCode());
        return result;
    }

    public static <T> Result<T> error(CrushEnum crushEnum, String msg){
        Result<T> result = get();
        result.setCode(crushEnum.getCode());
        String message = String.format(crushEnum.getMsg(), msg);
        result.setMsg(message);
        return result;
    }

    public static <T> Result<T> error(String msg){
        Result<T> result = get();
        result.setMsg(msg);
        return result;
    }
}
