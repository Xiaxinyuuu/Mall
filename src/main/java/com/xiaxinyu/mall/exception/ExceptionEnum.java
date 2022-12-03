package com.xiaxinyu.mall.exception;

/**
 * @Description: 异常枚举
 * @author: xiaxinyu
 * @Email: xiaxinyuxy@163.com
 * @date: 2022年12月03日 15:26
 * @Copyright:
 * @version: 1.0.0
 */

public enum ExceptionEnum {
    NEED_USER_NAME(10001,"用户名不能为空"),
    NEED_PASSWORD(10002,"密码不能为空"),
    PASSWORD_TOO_SHORT(10003,"密码长度不能小于8位"),

    NAME_EXISTED(10004,"不允许重名，注册失败"),

    INSERT_FAILED(10005,"插入失败，请重试"),
    ;
    private Integer code;
    private String msg;

    ExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}