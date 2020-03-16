package com.sun.springboot_aop.common;

import lombok.Builder;
import lombok.Data;

/**
 * 结果封装类
 * @author angtai
 */

@Builder
@Data
public class Result<T> {

    private Integer code;

    private String message;

    private T data;

    public static <T>Result<T> ok(T data){
        return ok(null);
    }

}