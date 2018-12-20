package com.bitker.response;

/**
 * @Author lumingxing
 * @Date 2018/12/06
 * @Time 19:08
 */

public class IntrustDetailResponse<T> {


    private String status;
    private Object data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
