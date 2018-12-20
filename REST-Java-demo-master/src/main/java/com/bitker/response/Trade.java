package com.bitker.response;

/**
 * @Author lumingxing
 * @Date 2018/12/06
 * @Time 14:52
 */

public class Trade<T> {

    private long id;
    private T data;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
