package com.bitker.api;

import java.io.IOException;

import com.bitker.request.CreateOrderRequest;
import com.bitker.request.DepthRequest;
import com.bitker.request.IntrustOrdersDetailRequest;
import com.bitker.response.AccountsResponse;
import com.bitker.response.ApiResponse;
import com.bitker.response.DepthResponse;
import com.bitker.response.IntrustDetailResponse;
import com.bitker.response.KlineResponse;
import com.bitker.response.OrdersDetailResponse;
import com.bitker.response.SubmitcancelResponse;
import com.bitker.response.TimestampResponse;
import com.bitker.response.TradeResponse;

public class Test {


    static final String API_KEY = "value";
  
    static final String API_SECRET = "value";
    
    public static void main(String[] args) {
        try {
            apiSample();
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
    
    static void apiSample() {
        // create ApiClient using your api key and api secret:
        ApiClient client = new ApiClient(API_KEY, API_SECRET);


        //获取 K 线
        //------------------------------------------------------ kline -------------------------------------------------------
        KlineResponse kline = client.kline("btc_usdt", "5min", "100");
        print(kline);


        //------------------------------------------------------ depth -------------------------------------------------------

        DepthRequest depthRequest = new DepthRequest();
        depthRequest.setSymbol("btc_usdt");
        depthRequest.setType("0");
        DepthResponse depth = client.depth(depthRequest);
        print(depth);

        //------------------------------------------------------ trade -------------------------------------------------------
        TradeResponse trade = client.trade("eth_usdt");
        print(trade);



        //------------------------------------------------------ timestamp -------------------------------------------------------
        TimestampResponse timestamp = client.timestamp();
        print(timestamp);

        //------------------------------------------------------ accounts -------------------------------------------------------
        AccountsResponse accounts = client.accounts();
        print(accounts);



        Long orderId = 277967289L;

        CreateOrderRequest createOrderReq = new CreateOrderRequest();
        createOrderReq.amount = "1.0";
        createOrderReq.price = "0.18";
        createOrderReq.symbol = "btc_usdt";
        createOrderReq.consignDirection = CreateOrderRequest.OrderType.CONSIGN_DIRECTION_BUY;
        createOrderReq.consignType = CreateOrderRequest.OrderType.CONSIGN_TYPE_LIMIT;

        //------------------------------------------------------order place 创建订单  -------------------------------------------------------
        ApiResponse resp = client.createOrder(createOrderReq);
        print(resp);
   
        

        //------------------------------------------------------ submitcancel 取消订单 -------------------------------------------------------

        SubmitcancelResponse submitcancel = client.submitcancel("38651");
        print(submitcancel);


        //------------------------------------------------------ ordersDetail 订单详情 -------------------------------------------------------
        OrdersDetailResponse ordersDetail = client.ordersDetail("38653");
        print(ordersDetail);




        //------------------------------------------------------ order 查询当前委托、历史委托 -------------------------------------------------------
        IntrustOrdersDetailRequest req = new IntrustOrdersDetailRequest();
        req.symbol = "btc_usdt";
        req.consignType = IntrustOrdersDetailRequest.OrderType.CONSIGN_TYPE_LIMIT;
        req.transDirection = IntrustOrdersDetailRequest.OrderType.CONSIGN_DIRECTION_BUY;
        req.states = IntrustOrdersDetailRequest.OrderStates.CANCELED+","+IntrustOrdersDetailRequest.OrderStates.CREATED;

        IntrustDetailResponse intrustDetail = client.intrustOrdersDetail(req);
        print(intrustDetail);

        //------------------------------------------------------ matchresults 查询当前委托、历史委托 -------------------------------------------------------
        IntrustOrdersDetailRequest matchReq = new IntrustOrdersDetailRequest();
        matchReq.symbol = "btc_usdt";
        matchReq.consignType = IntrustOrdersDetailRequest.OrderType.CONSIGN_TYPE_LIMIT;
        matchReq.transDirection = IntrustOrdersDetailRequest.OrderType.CONSIGN_DIRECTION_BUY;
        
        IntrustDetailResponse transDetail = client.intrustTransDetail(matchReq);
        print(transDetail);
        

    }

    static void print(Object obj) {
        try {
            System.out.println(JsonUtil.writeValue(obj));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
