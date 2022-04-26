package com.blog.demo.common;

public enum ResultInfo {

    LOGIN_SUCCESS(666,"登录成功"),
    LOGIN_ERROR(405,"登录失败"),
    NOT_FOUND(404,"没有访问到该网页"),
    SUCCESS(201, "访问成功"),
    EXCEPTION(101,"全局出现异常"),
    FAIL(51000,"操作异常"),
    ;

    ResultInfo(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private Integer code;
    private String message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
