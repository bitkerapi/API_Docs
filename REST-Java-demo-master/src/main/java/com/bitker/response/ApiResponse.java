package com.bitker.response;

public class ApiResponse<T> {

    public String status;
    public String data;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}

    
}
