package com.xiaxinyu.mall.exception;

/**
 * @Description: 统一异常
 * @author: xiaxinyu
 * @Email: xiaxinyuxy@163.com
 * @date: 2022年12月03日 15:44
 * @Copyright:
 * @version: 1.0.0
 */

public class MyException extends Exception{
    private final Integer code;
    private final String message;

    public MyException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public MyException(ExceptionEnum ex){
        this(ex.getCode(),ex.getMsg());
    }


    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}