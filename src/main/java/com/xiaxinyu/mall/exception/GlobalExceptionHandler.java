package com.xiaxinyu.mall.exception;

import com.xiaxinyu.mall.common.ApiRestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description: 统一异常handler
 * @author: xiaxinyu
 * @Email: xiaxinyuxy@163.com
 * @date: 2022年12月03日 16:03
 * @Copyright: 个人版权所有
 * @version: 1.0.0
 */

@ControllerAdvice
public class GlobalExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handleException(Exception e){
        log.error("Default Exception : ",e);
        return ApiRestResponse.error(ExceptionEnum.SYSTEM_ERROR);
    }

    @ExceptionHandler(MyException.class)
    @ResponseBody
    public Object handleMyException(MyException e){
        log.error("My Exception : ",e);
        return ApiRestResponse.error(e.getCode(),e.getMessage());
    }

}