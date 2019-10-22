package com.zzkj.sales.controller;

import com.zzkj.sales.common.JsonData;
import com.zzkj.sales.exception.ParamException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @ControllerAdvice是一个@Component,用于定义@ExceptionHandler
 * 发生异常返回JsonData 保证全局json接口统一
 */

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(value = ParamException.class)
    @ResponseBody
    public JsonData jsonExceptionHandler(HttpServletRequest request, ParamException e){
        return JsonData.fail(e.getMessage());
    }
}
