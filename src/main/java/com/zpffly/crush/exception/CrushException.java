package com.zpffly.crush.exception;

import lombok.Data;

@Data
public class CrushException extends RuntimeException{

    private String msg;

    public CrushException(String msg){
        super(msg);
        this.msg = msg;
    }
}
