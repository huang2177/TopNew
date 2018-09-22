package com.kw.top.bean;

/**
 * author  ： zy
 * date    ： 2018/6/26
 * des     ：
 */

public class OrderInfoBean {

    /**
     * appOrderInfo : alipay_sdk=alipay-sdk-java-3.1.0&app_id=2018061360364297&biz_content=%7B%22body%22%3A%22%E6%8B%A5%E6%9C%89%E6%9B%B4%E5%A4%9A%E7%9A%84%E9%92%BB%E7%9F%B3%22%2C%22out_trade_no%22%3A%22CZ20180626214115543820%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22TOP%E5%85%85%E5%80%BC%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%220.01%22%7D&charset=utf-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fy2071m6032.iask.in%3A42354%2Fapi%2FaliPayController%2FaliPayNotify&sign=fyvixv7nv%2FbLqIPPo35bAcJaVU9YiM4zBK71g8Lga8KRlcvl7OX5bJpn%2B6fHieIQRaD7phodT4SmIMjOMtckIFKUV2%2BEWNyFkg2uMZodNURelSpO6CxsYwnAuS1j4%2BIP%2Bgy%2BpkJj6Czbl3AW796s%2FY5deVzW3%2Ba1VDvO2QGhAxKPbE2sqP0f9ahRpj3ORwrQqnn%2FXMweKRPkrHjpIeTraGfJXqC5mzQ8HdpU%2FP7tZsY8pZGnrIeqVyx1i4hKDiTm43rLpMES75wlAYDKKI1k8M%2BFmHTXzXcE0ePnGsWnrimlL0iwjANJPozHfBHNbJ0SOfIxTnL6VH0swykOk6kH9w%3D%3D&sign_type=RSA2&timestamp=2018-06-26+21%3A41%3A15&version=1.0
     */

    private String appOrderInfo;

    public String getAppOrderInfo() {
        return appOrderInfo;
    }

    public void setAppOrderInfo(String appOrderInfo) {
        this.appOrderInfo = appOrderInfo;
    }

}
