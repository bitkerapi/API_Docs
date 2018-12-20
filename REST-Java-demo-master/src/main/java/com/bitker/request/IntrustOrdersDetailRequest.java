package com.bitker.request;

/**
 * @Author ISME
 * @Date 2018/1/14
 * @Time 19:08
 */

public class IntrustOrdersDetailRequest {

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

    public static interface OrderStates {
        /**
         * created已创建
         */
        static final String CREATED = "0";
        /**
         * partial-filled 部分成交
         */
        static final String PARTIAL_FILLED = "1";

        /**
         * filled 完全成交
         */
        static final String FILLED = "2";
        /**
         * canceled 已撤销
         */
        static final String CANCELED = "3";
    }

    public String symbol;       //true	string	交易对		btcusdt, bccbtc, rcneth ...
    public String consignType;       //true	string	查询的订单类型组合，使用','分割      1限价委托   2 市价委托
    public String transDirection;    //true String   1买入  2卖出
    public String startDate;   //false	string	查询开始日期, 日期格式yyyy-mm-dd
    public String endDate;       //false	string	查询结束日期, 日期格式yyyy-mm-dd
    public String states;       //true	string	查询的订单状态组合，使用','分割    0 委托中    1 部分成交    2 已成交   3 已撤销

    // partial-canceled 部分成交撤销, filled 完全成交, canceled 已撤销
    public String from;           //false	string	查询起始 ID
    public String direct;       //false	string	查询方向		prev 向前，next 向后
    public String size;           //false	string	查询记录大小
}
