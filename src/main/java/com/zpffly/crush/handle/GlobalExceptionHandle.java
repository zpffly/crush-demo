package com.zpffly.crush.handle;

import com.zpffly.crush.entity.Result;
import com.zpffly.crush.exception.CrushException;
import com.zpffly.crush.myenum.CrushEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;

@RestControllerAdvice
public class GlobalExceptionHandle {

    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandle.class);

    @ExceptionHandler(BindException.class)
    public Result handleMethodArgumentNotValidException(BindException e){
        logger.error("[参数{}校验失败]-----{}" , e.getBindingResult().getFieldError().getField()  ,e.getBindingResult().getFieldError().getDefaultMessage());
        return Result.error(CrushEnum.VALIDATOR_ERROR, e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(CrushException.class)
    public Result handleCrushException(CrushException e){
//        logger.error("全局异常：{}", e.getMsg());
        return Result.error(e.getMsg());
    }
}
