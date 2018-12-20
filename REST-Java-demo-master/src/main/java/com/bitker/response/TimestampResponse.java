package com.bitker.response;

/**
 * @Author lumingxing
 * @Date 2018/12/06
 * @Time 15:53
 */

public class TimestampResponse {

    /**
     * status : ok
     * data : 1494900087029
     */

    private String status;
    private long data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }

}
