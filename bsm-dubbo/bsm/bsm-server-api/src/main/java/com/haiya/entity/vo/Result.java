package com.haiya.entity.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Result<T> {
    private int status;      // 状态码
    private String msg;      // 消息
    private List<T> data;    // 数据列表

    // 成功静态方法
    public static <T> Result<T> success(List<T> data) {
        Result<T> result = new Result<>();
        result.setStatus(200);
        result.setMsg("ok");
        result.setData(data);
        return result;
    }

    // 失败静态方法
    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.setStatus(404);
        result.setMsg(msg);
        return result;
    }

}