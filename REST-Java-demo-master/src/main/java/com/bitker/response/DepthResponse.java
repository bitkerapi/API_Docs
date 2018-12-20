package com.bitker.response;


public class DepthResponse<T> {


    /**
     * status : ok
     * ts : 1489472598812
     * tick : {"id":"1489464585407","ts":"1489464585407","bids":[[7964,0.0678],[7963,0.9162]],"asks":[[7979,0.0736],[8020,13.6584]]}
     */

    private String status;
    private String ts;

    /**
     * tick 说明:
     * "tick": {
     * "id": 消息id,
     * "ts": 消息生成时间，单位：毫秒,
     * "bids": 买盘,[price(成交价), amount(成交量)], 按price降序,
     * "asks": 卖盘,[price(成交价), amount(成交量)], 按price升序
     * }
     */
    private Depth tick;


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

    public Depth getTick() {
        return tick;
    }

    public void setTick(Depth tick) {
        this.tick = tick;
    }

}
