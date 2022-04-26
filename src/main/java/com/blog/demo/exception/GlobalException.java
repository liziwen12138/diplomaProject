package com.blog.demo.exception;


import com.blog.demo.vo.Result;
import com.blog.demo.common.ResultInfo;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//@ControllerAdvice来声明一些全局性的东西，最常见的是结合@ExceptionHandler注解用于全局异常的处理。
//@ExceptionHandler注解标注的方法：用于捕获Controller中抛出的不同类型的异常，从而达到异常全局处理的目的；
//@InitBinder注解标注的方法：用于请求中注册自定义参数的解析，从而达到自定义请求参数格式的目的；
//@ModelAttribute注解标注的方法：表示此方法会在执行目标Controller方法之前执行 。
@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public<T> Result<T> exception(Exception e){
        e.printStackTrace();
        return Result.fail(ResultInfo.EXCEPTION);
    }
}
