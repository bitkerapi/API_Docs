# encoding: UTF-8

import hashlib
import hmac
import math
import requests
import time


apiKey = "value"
secretKey = "value"
tradeSymbol = "btc_usdt"
tradeVolume = "0.1"

# restUrl = 'https://api.bitker.com'#REST API address, 接口地址
restUrl = 'http://47.100.231.50:8716'
# Endpoints
API_account = restUrl + "/v1/account/accounts?apikey=%(apikey)s&signature=%(signature)s"  # user account, 用户账户，http get
API_trade = restUrl + "/v1/orders/place"  # place order, 下单，http post
API_cancel_order = restUrl + "/v1/order/orders/%(order_id)s/submitcancel"  #cancel order, 撤单， http post
API_order_info = restUrl + "/v1/order/orders/%(order_id)s?apikey=%(apikey)s&signature=%(signature)s"  #order information, 订单信息， http get
API_batch_order_info = restUrl + \
    "/v1/order/orders?symbol=%(symbol)s&consignType=%(consignType)s&transDirection=%(transDirection)s&states=%(states)s&apikey=%(apikey)s&signature=%(signature)s" #current orders, 查询当前委托

def getAccount(symbol):
    ret = None
    params = {'apikey':apiKey}
    params['signature'] = generateSign(params, secretKey)
    url = API_account %{'apikey': apiKey, 'signature':params['signature']}
    #print url
    try:
        respond = requests.session().get(url)
        if respond.status_code == 200:
            ret = respond.json()
    except:
        pass
    print(ret)
    return ret


def depthUrl(symbol, apiKey, nLevel=5):
    """generate market depth endpoint, 生成行情地址"""
    return restUrl + '/market/depth?symbol=%(symbol)s&type=%(level)s&apikey=%(apikey)s' \
           % {'symbol': symbol, 'apikey': apiKey, 'level': nLevel}

def generateSign(params, secretKey):
    """generate signature, 生成签名"""
    l = []
    for key in sorted(params.keys()):
        l.append('%s' % params[key])
    sign = ''.join(l)
    return hashlib.md5(
        hmac.new(secretKey, sign.encode('utf-8'), digestmod=hashlib.sha256).hexdigest()
        ).hexdigest()

def getMarketDepth(symbol):
    """get current market depth， 获取当前深度行情"""
    ret = None
    url = depthUrl(symbol, apiKey)
    print (url)
    #print url
    try:
        respond = requests.session().get(url)
        if respond.status_code == 200:
            ret = respond.json()
    except:
        pass
    return ret


def placeOrder(symbol, amount, consignDirection, consignType, price=None):
    """place order, 订单"""
    ret = None
    params = {'symbol': str(symbol), 'price': str(price), 'amount': str(amount), 'consignDirection': str(consignDirection),
              'consignType': str(consignType), 'apikey': str(apiKey)}
    params['signature'] = generateSign(params, secretKey)
    #print params
    try:
        respond = requests.Session().post(API_trade, data=params)
        if respond.status_code == 200:
            ret = respond.json()
    except:
        pass
    return ret



def placeLimitOrder(symbol, amount, price, consignDirection):
    """place limite order，限价单"""
    return placeOrder(symbol=symbol, amount=amount,consignType=1, price=price, consignDirection=consignDirection)


def cancelOrder(orderId):
    """cancel order, 撤单"""
    ret = None
    params = {'apikey':apiKey}
    params['signature'] = generateSign(params, secretKey)
    try:
        respond = requests.Session().post(API_cancel_order % {'order_id': orderId}, data=params)
        if respond.status_code == 200:
            ret = respond.json()
    except:
        pass
    return ret


def checkOrder(orderId):
    """check order, 查询订单"""
    ret = None
    params = {'apikey':apiKey}
    params['signature'] = generateSign(params, secretKey)
    try:
        respond = requests.Session().get(
            API_order_info % {'order_id': orderId, 'apikey': apiKey, 'signature': params['signature']})
        if respond.status_code == 200:
            ret = respond.json()
    except:
        pass
    return ret


def mine():
    print ('------------------------------------------------------------------------')
    try:
        depth = getMarketDepth(tradeSymbol)
    except:
        depth = None
    
    print(depth)
  
    ID1 = placeLimitOrder(symbol=tradeSymbol, amount=tradeVolume, price=0.1, consignDirection=1) #多，amount = volume
    print(ID1)
    print(checkOrder(ID1['data']))
    
    ret = cancelOrder(ID1['data'])
    print(ret)
    time.sleep(3) 
    print(checkOrder(ID1['data']))
    print ('------------------------------------------------------------------------')

def get_float_minsize(x):
    x = float(x) + 1
    number = len(str(x)) - str(x).find(".") - 1
    y = pow(10, -1 * number)
    return y

def digits(num, digit):
    site = pow(10, digit)
    tmp = num * site
    tmp = math.floor(tmp) / site
    return tmp


if __name__ == '__main__':
    print (time.ctime())
    print ('策略正在启动...')
    try:
        accountInfo = getAccount(tradeSymbol)
    except:
        accountInfo = None
    if accountInfo:
        for i in range(0, len(accountInfo['data'])):
            print (accountInfo['data'][i]['coinName'], '可用余额:', accountInfo['data'][i]['useMoney'], '冻结:', accountInfo['data'][i]['freeze'])
            print ('============================')
    else:
        print ('读取账户信息失败，程序继续运行...')
    while True:
        print ('策略运行中...')
        print (time.ctime())
        try:
            mine()
        except:
            pass
        time.sleep(5)
