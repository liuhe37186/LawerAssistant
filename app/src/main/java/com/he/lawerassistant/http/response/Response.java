package com.he.lawerassistant.http.response;

import com.corelibs.subscriber.ResponseHandler;

/**
 * Created by Zaifeng on 2018/2/28.
 * 返回结果封装
 */

public class Response<T> implements ResponseHandler.IBaseData {
    private String status;
    private T data; // 具体的数据结果
    private String msg; // message 可用来返回接口的说明
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Response{" +
                "status='" + status + '\'' +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }

    @Override
    public boolean isSuccess() {
        if (status.equals("1")) {
            return true;
        }
        return false;
    }

    @Override
    public String status() {
        return status;
    }

    @Override
    public Object data() {
        return data;
    }

    @Override
    public String msg() {
        return msg;
    }
}
