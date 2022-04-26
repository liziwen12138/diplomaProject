package com.blog.demo.vo;

import com.blog.demo.common.ResultInfo;
import lombok.Data;

/**
 * 接口返回类
 */
@Data
public class Result<T> {
    /**
     * 返回状态
     */
    private Boolean flag;
    /**
     * 返回码
     */
    private Integer code;
    /**
     * 返回信息
     */
    private String message;
    /**
     * 返回数据
     */
    private T data;

    public static <T> Result<T> ok() {
        return restResult(true, null, ResultInfo.SUCCESS.getCode(), ResultInfo.SUCCESS.getMessage());
    }

    public static <T> Result<T> ok(T data) {
        return restResult(true, data, ResultInfo.SUCCESS.getCode(), ResultInfo.SUCCESS.getMessage());
    }

    public static <T> Result<T> ok(T data, String message) {
        return restResult(true, data, ResultInfo.SUCCESS.getCode(), message);
    }

    public static <T> Result<T> fail() {
        return restResult(false, null, ResultInfo.FAIL.getCode(), ResultInfo.FAIL.getMessage());
    }

    public static <T> Result<T> fail(ResultInfo statusCodeEnum) {
        return restResult(false, null, statusCodeEnum.getCode(), statusCodeEnum.getMessage());
    }

    public static <T> Result<T> fail(String message) {
        return restResult(false, message);
    }

    public static <T> Result<T> fail(T data) {
        return restResult(false, data, ResultInfo.FAIL.getCode(), ResultInfo.FAIL.getMessage());
    }

    public static <T> Result<T> fail(T data, String message) {
        return restResult(false, data, ResultInfo.FAIL.getCode(), message);
    }

    public static <T> Result<T> fail(Integer code, String message) {
        return restResult(false, null, code, message);
    }

    /**
     * 统一封对属性赋值，降低冗余
     * @param flag
     * @param message
     * @param <T>
     * @return
     */
    private static <T> Result<T> restResult(Boolean flag, String message) {
        Result<T> apiResult = new Result<>();
        apiResult.setFlag(flag);
        apiResult.setCode(flag ? ResultInfo.SUCCESS.getCode() : ResultInfo.FAIL.getCode());
        apiResult.setMessage(message);
        return apiResult;
    }

    private static <T> Result<T> restResult(Boolean flag, T data, Integer code, String message) {
        Result<T> apiResult = new Result<>();
        apiResult.setFlag(flag);
        apiResult.setData(data);
        apiResult.setCode(code);
        apiResult.setMessage(message);
        return apiResult;
    }
}
