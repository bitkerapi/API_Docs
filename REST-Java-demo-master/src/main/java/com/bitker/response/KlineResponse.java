package com.bitker.response;

/**
 * @Author lumingxing
 * @Date 2018/12/06
 * @Time 11:56
 */

public class KlineResponse<T> {

    private String status;
    private String ts;
    public T tick;



    public T getTick() {
		return tick;
	}

	public void setTick(T tick) {
		this.tick = tick;
	}

	public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

   

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }
}
