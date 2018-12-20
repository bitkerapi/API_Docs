package com.bitker.response;

/**
 * @param <T>
 * @Author lumingxing
 * @Date 2018/12/06
 * @Time 14:52
 */

public class TradeResponse<T> {


    private String status;
    private long ts;
    private T tick;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

	public T getTick() {
		return tick;
	}

	public void setTick(T tick) {
		this.tick = tick;
	}


  
}
