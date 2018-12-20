package com.bitker.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.bitker.request.CreateOrderRequest;
import com.bitker.request.DepthRequest;
import com.bitker.request.IntrustOrdersDetailRequest;
import com.bitker.response.Account;
import com.bitker.response.Accounts;
import com.bitker.response.AccountsResponse;
import com.bitker.response.ApiResponse;
import com.bitker.response.Depth;
import com.bitker.response.DepthResponse;
import com.bitker.response.IntrustDetailResponse;
import com.bitker.response.Kline;
import com.bitker.response.KlineResponse;
import com.bitker.response.OrdersDetailResponse;
import com.bitker.response.SubmitcancelResponse;
import com.bitker.response.TimestampResponse;
import com.bitker.response.TradeResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;

import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;

/**
 * API client.
 *
 * @Date 2018/12/06
 * @Time 16:02
 */
public class ApiClient {

    static final int CONN_TIMEOUT = 15000;
    static final int SOCK_TIMEOUT = 15000;
    static final int REQU_TIMEOUT = 5000;
    static final int READ_TIMEOUT = 5;
    static final int WRITE_TIMEOUT = 5;


    static final String API_URL = "https://api.bitker.com";

    static final String API_HOST = getHost();
    

    final String accessKeyId;
    final String accessKeySecret;
    final String assetPassword;
    
    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5', 
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 创建一个ApiClient实例
     *
     * @param accessKeyId     AccessKeyId
     * @param accessKeySecret AccessKeySecret
     */
    public ApiClient(String accessKeyId, String accessKeySecret) {
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.assetPassword = null;
    }

    /**
     * 创建一个ApiClient实例
     *
     * @param accessKeyId     AccessKeyId
     * @param accessKeySecret AccessKeySecret
     * @param assetPassword   AssetPassword
     */
    public ApiClient(String accessKeyId, String accessKeySecret, String assetPassword) {
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.assetPassword = assetPassword;
    }

    

    /**
     * 查询所有账户信息
     *
     * @return List of accounts.
     */
    public ApiResponse getAccounts() {
        ApiResponse<List<Account>> resp =
                get("/v1/account/accounts", null, new TypeReference<ApiResponse<List<Account>>>() {
                }, true);
        return resp;
    }

    /**
     * 创建订单
     *
     * @param request CreateOrderRequest object.
     * @return Order id.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public ApiResponse createOrder(CreateOrderRequest request) {
    	HashMap map = new HashMap();
    	map.put("amount", request.amount);
    	map.put("consignDirection", request.consignDirection);
    	map.put("consignType", request.consignType);
    	map.put("price", request.price);
    	map.put("symbol", request.symbol);
        ApiResponse<Object> resp =
                        post("/v1/orders/place", request,map, new TypeReference<ApiResponse<Object>>() {
                        });
        return resp;
    }

 


    // ----------------------------------------行情API-------------------------------------------

    /**
     * GET /market/history/kline 获取K线数据
     *
     * @param symbol
     * @param period
     * @param size
     * @return
     */
    public KlineResponse kline(String symbol, String period, String size) {
        HashMap map = new HashMap();
        map.put("symbol", symbol);
        map.put("period", period);
        map.put("size", size);
        KlineResponse resp = get("/market/history/kline", map, new TypeReference<KlineResponse<List<Kline>>>() {
        }, false);
        return resp;
    }

  

    /**
     * GET /market/depth 获取 Market Depth 数据
     *
     * @param request
     * @return
     */
    public DepthResponse depth(DepthRequest request) {
        HashMap map = new HashMap();
        map.put("symbol", request.getSymbol());
        map.put("type", request.getType());

        DepthResponse resp = get("/market/depth", map, new TypeReference<DepthResponse<List<Depth>>>() {
        }, false);
        return resp;
    }

    /**
     * GET /market/trade 获取 Trade Detail 数据
     *
     * @param symbol
     * @return
     */
    public TradeResponse trade(String symbol) {
        TradeResponse resp = get("/market/trade/"+symbol, null, new TypeReference<TradeResponse>() {
        }, false);
        return resp;
    }

 
    /**
     * GET /v1/common/timestamp 查询系统当前时间
     *
     * @return
     */
    public TimestampResponse timestamp() {
        TimestampResponse resp = get("/v1/common/getTimestamp", null, new TypeReference<TimestampResponse>() {
        },true);
        return resp;
    }

    /**
     * GET /v1/account/accounts 查询当前用户的所有账户(即account-id)
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
	public AccountsResponse accounts() {
        AccountsResponse resp = get("/v1/account/accounts", null, new TypeReference<AccountsResponse<List<Accounts>>>() {
        },true);
        return resp;
    }

   

    /**
     * POST /v1/order/orders/{order-id}/submitcancel 申请撤销一个订单请求
     *
     * @param orderId
     * @return
     */
    public SubmitcancelResponse submitcancel(String orderId) {
        SubmitcancelResponse resp = post("/v1/order/orders/" + orderId + "/submitcancel", null,new HashMap<String, String>(), new TypeReference<SubmitcancelResponse>() {
        });
        return resp;
    }

   

    /**
     * GET /v1/order/orders/{order-id} 查询某个订单详情
     *
     * @param orderId
     * @return
     */
    public OrdersDetailResponse ordersDetail(String orderId) {
        OrdersDetailResponse resp = get("/v1/order/orders/" + orderId, null, new TypeReference<OrdersDetailResponse>() {
        },true);
        return resp;
    }



    // send a GET request.
    <T> T get(String uri, Map<String, String> params, TypeReference<T> ref, boolean needSign) {
        if (params == null) {
            params = new HashMap<>();
        }
        return call("GET", uri, null, params, ref, needSign);
    }

    // send a POST request.
    <T> T post(String uri, Object object,Map<String, String> params, TypeReference<T> ref) {
        return call("POST", uri, null, params, ref, true);
    }

    // call api by endpoint.
    <T> T call(String method, String uri, Object object, Map<String, String> params,
               TypeReference<T> ref, boolean needSign) {
    	HttpClient client = HttpClients.createDefault();
    	RequestConfig requestConfig = RequestConfig.custom()  
    	        .setConnectTimeout(CONN_TIMEOUT).setConnectionRequestTimeout(REQU_TIMEOUT)  
    	        .setSocketTimeout(SOCK_TIMEOUT).build();  
    	HttpResponse response = null;
     	BufferedReader in = null; 
    	
        try {
        	params.put("apikey", accessKeyId);
        	if (needSign) {
        		String req = sortMapString(params);
        		String sign = sign(req, accessKeySecret);
            	params.put("signature", sign);
			}
  
        	Set<Entry<String, String>> set = params.entrySet();
        	List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
        	for (Entry<String, String> entry : set) {
				BasicNameValuePair pair = new BasicNameValuePair(entry.getKey(),entry.getValue());
				pairs.add(pair);
			}
 
            if ("POST".equals(method)) {
            	HttpPost post = new HttpPost(API_URL+uri);
            	post.setConfig(requestConfig);
            	post.setEntity(new UrlEncodedFormEntity(pairs,HTTP.UTF_8));
            	response = client.execute(post);

            } else {
            	String paramsStr = EntityUtils.toString(new UrlEncodedFormEntity(pairs, "UTF-8"));
            	HttpGet get = new HttpGet(API_URL+uri+"?"+paramsStr);
            	get.setConfig(requestConfig);
            	response = client.execute(get);
            }
            /**请求发送成功，并得到响应**/
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /**读取服务器返回过来的json字符串数据**/
            	BufferedReader responseBuffer = new BufferedReader(
    			        new InputStreamReader(response.getEntity().getContent()));
    			String output = responseBuffer.readLine();
    			return JsonUtil.readValue(output, ref);
            }
            return null;
        } catch (Exception e) {
            throw new ApiException(e);
        }
    }

    public IntrustDetailResponse intrustOrdersDetail(IntrustOrdersDetailRequest req) {
        HashMap map = new HashMap();
        map.put("symbol", req.symbol);
        map.put("states", req.states);
        map.put("consignType",req.consignType);
        map.put("transDirection",req.transDirection);
        if (req.startDate!=null) {
            map.put("start_date",req.startDate);
 		}

        if (req.endDate!=null) {
             map.put("end_date",req.endDate);
  		}
        
        if (req.direct!=null) {
            map.put("direct",req.direct);
 		}
        
        if (req.from!=null) {
             map.put("from",req.from);
  		}

        if (req.size!=null) {
             map.put("size",req.size);
  		}
        IntrustDetailResponse resp = get("/v1/order/orders/", map, new TypeReference<IntrustDetailResponse<List<Object>>>() {
        }, true);
        return resp;
    }
    
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public IntrustDetailResponse intrustTransDetail(IntrustOrdersDetailRequest req) {
        HashMap map = new HashMap();
        map.put("symbol", req.symbol);
        map.put("consignType",req.consignType);
        map.put("transDirection",req.transDirection);
        if (req.startDate!=null) {
            map.put("start_date",req.startDate);
 		}

        if (req.endDate!=null) {
             map.put("end_date",req.endDate);
  		}
        
        if (req.direct!=null) {
            map.put("direct",req.direct);
 		}
        
        if (req.from!=null) {
             map.put("from",req.from);
  		}

        if (req.size!=null) {
             map.put("size",req.size);
  		}
        IntrustDetailResponse resp = get("/v1/orders/matchresults/", map, new TypeReference<IntrustDetailResponse<List<Object>>>() {
        }, true);
        return resp;
    }



    // create OkHttpClient:
    static OkHttpClient createOkHttpClient() {
        return new Builder().connectTimeout(CONN_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS).writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }
    
    static String getHost() {
        String host = null;
        try {
            host = new  URL(API_URL).getHost();
        } catch (MalformedURLException e) {
            System.err.println("parse API_URL error,system exit!,please check API_URL:" + API_URL ); 
            System.exit(0);
        }
        return host;
    }
    
    
    /**
	 * 簽名。
	 * 
	 * @param message
	 *            明文
	 * @param secKey
	 *            秘鑰
	 * @return 基於Base64編碼的密文
	 * @throws Exception
	 */
	public static String sign(String message, String secKey) throws Exception {
		Mac sha256HMAC = Mac.getInstance("HmacSHA256");
		SecretKeySpec restoreSecretKey = new SecretKeySpec(secKey.getBytes(), "HmacSHA256");
		sha256HMAC.init(restoreSecretKey);
		byte[] digestdByte = sha256HMAC.doFinal(message.getBytes());

		return MD5Encode(bytesToHexFun(digestdByte));
	}
	
	
	public static String bytesToHexFun(byte[] bytes) {
        char[] buf = new char[bytes.length * 2];
        int index = 0;
        for(byte b : bytes) { // 利用位运算进行转换，可以看作方法一的变种
            buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
            buf[index++] = HEX_CHAR[b & 0xf];
        }

        return new String(buf);
    }
	
	public static String MD5Encode(String origin)
    {
        origin = origin.trim();
        String resultString = null;
        try
        {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = bytesToHexFun(md.digest(resultString.getBytes("UTF-8")));
        }
        catch(Exception ex) { }
        return resultString;
    }
	
	public static void main(String[] args) {

	}
	
    
    /**
     * 顺序排序取值
     * @param map
     * @return
     */
    public static String sortMapString(Map<String, String> map){
        //按Key进行排序
        Map<String, String> resultMap = sortMapByKey(map);
        StringBuffer buffer=new StringBuffer();
        for (Map.Entry<String, String> entry : resultMap.entrySet()) {
            buffer.append(entry.getValue());
        }
        return buffer.toString();
    }
    
    
    /**
     * 使用 Map按key进行排序
     * @param map
     * @return
     */
    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> sortMap = new TreeMap<String, String>(
                new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }
    
    
}


class JsonUtil {

    public static String writeValue(Object obj) throws IOException {
        return objectMapper.writeValueAsString(obj);
    }

    public static <T> T readValue(String s, TypeReference<T> ref) throws IOException {
        return objectMapper.readValue(s, ref);
    }

    static final ObjectMapper objectMapper = createObjectMapper();

    static ObjectMapper createObjectMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        // disabled features:
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    

}


class MapKeyComparator implements Comparator<String> {

    @Override
    public int compare(String str1, String str2) {
        return str1.compareTo(str2);
    }
}
