package com.bitker.request;



public class DepthRequest {

    //交易对
    public String symbol;

    //Depth 类型 0,1,2,3,4,5（合并深度0-5）；0时，不合并深度
    public String type;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
