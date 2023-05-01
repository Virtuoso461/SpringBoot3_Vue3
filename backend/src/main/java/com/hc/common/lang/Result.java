package com.hc.common.lang;

import lombok.Data;

/**
 * @author: 何超
 * @date: 2023-04-29 16:14
 * @description: 返回包装类
 */
@Data
public class Result<T> {

    private int status;
    private boolean success;
    private T message;

    private Result(int status, boolean success, T message) {
        this.status = status;
        this.success = success;
        this.message = message;
    }

    /**
     * 操作成功不返回数据
     *
     * @param <T>
     * @return
     */
    public static <T> Result<T> success() {
        return new Result<>(200, true, null);
    }

    /**
     * 操作成功返回数据
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, true, data);
    }

    /**
     * 操作失败不返回数据
     *
     * @param status
     * @param <T>
     * @return
     */
    public static <T> Result<T> failure(int status) {
        return new Result<>(status, false, null);
    }

    /**
     * 操作失败返回数据
     *
     * @param status
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> failure(int status, T data) {
        return new Result<>(status, false, data);
    }
}
