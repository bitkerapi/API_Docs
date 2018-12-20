package com.bitker.request;

public class CreateOrderRequest {
    public static interface OrderType {
        /**
         * 买入
         */
        static final String CONSIGN_DIRECTION_BUY = "1";
        /**
         * 卖出
         */
        static final String CONSIGN_DIRECTION_SELL = "2";
        /**
         * 市价交易
         */
        static final String CONSIGN_TYPE_LIMIT = "1";
        /**
         * 限价交易
         */
        static final String CONSIGN_TYPE_MARKET = "2";
    }

    /**
     * 交易对，必填，例如："eth_usdt"，
     */
    public String symbol;

 
    /**
     * 当订单类型为buy-limit,sell-limit时，表示订单数量， 当订单类型为buy-market时，表示订单总金额， 当订单类型为sell-market时，表示订单总数量
     */
    public String amount;

    /**
     * 订单价格，仅针对限价单有效，例如："1234.56"
     */
    public String price = "0.0";

    
    /**
     * 交易方向
     */
    public String consignDirection;
    
    /**
     * 订单类型
     */
    public String consignType;

}
