package com.xiaxinyu.mall.exception;

import com.xiaxinyu.mall.common.ApiRestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

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


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ApiRestResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.error("MethodArgumentNotValidException : ",e);
        return handleBindingResult(e.getBindingResult());
    }

    private ApiRestResponse handleBindingResult(BindingResult result){
        //把异常处理为对外暴露的提示
        List<String> list = new ArrayList<>();
        boolean isError = result.hasErrors();
        if(isError){
            List<ObjectError> objectErrors = result.getAllErrors();
            for(ObjectError objectError : objectErrors){
                String message = objectError.getDefaultMessage();
                list.add(message);
            }
        }

        if(list.isEmpty()){
            return ApiRestResponse.error(ExceptionEnum.REQUEST_PARAM_ERROR);
        }
        return ApiRestResponse.error(ExceptionEnum.REQUEST_PARAM_ERROR.getCode(),list.toString());
    }
}