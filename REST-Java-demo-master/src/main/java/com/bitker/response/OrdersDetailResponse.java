package com.bitker.response;

/**
 * @Author lumingxing
 * @Date 2018/12/05
 * @Time 18:21
 */

public class OrdersDetailResponse<T> {

    private String status;
    private T data;

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

    public static class DataBean {

    }


}
