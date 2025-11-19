package com.example.cloudserviceplatform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    // 处理404异常
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(NoHandlerFoundException ex, Model model) {
        model.addAttribute("errorCode", 404);
        model.addAttribute("errorMessage", "请求的页面不存在");
        model.addAttribute("exception", ex);
        return "error/404";
    }
    
    // 处理用户不存在异常
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleUserNotFound(UserNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/error";
    }
    
    // 处理通用的运行时异常
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleRuntimeException(RuntimeException ex, Model model) {
        model.addAttribute("errorCode", 500);
        model.addAttribute("errorMessage", "服务器内部错误: " + ex.getMessage());
        model.addAttribute("exception", ex);
        return "error/500";
    }
    
    // 处理所有其他异常
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleAllExceptions(Exception ex, Model model) {
        model.addAttribute("errorCode", 500);
        model.addAttribute("errorMessage", "发生未预期的错误: " + ex.getMessage());
        model.addAttribute("exception", ex);
        return "error/500";
    }
}