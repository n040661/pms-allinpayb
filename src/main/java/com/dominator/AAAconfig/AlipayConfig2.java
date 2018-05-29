package com.dominator.AAAconfig;

/**说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 *商户配置文件:
 */
public class AlipayConfig2 {

    //↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    /**
     * 网关地址
     */
    private static String URL ="https://openapi.alipay.com/gateway.do";

    /**
     *合作身份者ID，签约账号，以2088开头由16位纯数字组成的字符串，查看地址：https://openhome.alipay.com/platform/keyManage.htm?keyType=partner
     */
    public static final String SELLER_ID = "2088621208345438";

    /**
     *商户的私钥,需要PKCS8格式，RSA公私钥生成：https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.nBDxfy&treeId=58&articleId=103242&docType=1
     */
    public static final String PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCF1lS1tYtWCi42MnkIl0py272xega5+Xiu1f3b5HnyLWFuXixa7S3BNCLXvNYzNzrNgATKXMtfoj7h4lV6PWwWtm1BD96SfRVtWFH3nagjyhPUaXbLf7nPfy3tPnQ0Sa79G2BvKEVu4jPl3T7CD23akjFJqJlcuY/iDr2H9s0x07HscCyyXVWe34oHju5XulRo7t0lWQz+YqPCEcWJtzfv5rF8EM7qbZ/gwToSD3l8LRohtJfiAErCdCzHTDAGvFMRrAHhQ7viasrC6p/hGomT2qoTByOFV8xeLFk67AHZiXSsIfTGCwbz7mkU6cikBqWvEerK+ZZIhd340kCJX4DFAgMBAAECggEAMBUtf87GTVYA/jWp6B06gQNTJkTZz8zBv8ZfmrV93jTRkS5CLzSQwJ2zeuB78BCCjrvUfMNTyXrnro16+hzBDXPi0oIaC3Tm0s7tO9GnS5xY/XIaXMf5gIOHg3lxlCt4s7Q3NBg6wbiAvq960YtrgyAMlxL8biS4fZWgIA3Z8HFBsCf1/Qg3/u0nyj9bkesFvfr2SnOxUg4jJzRO6TbpRMw8ccwLM45azfOYcfWuMqYTOwppkNXc3HBkZyWSTgEnUvpmhZ5Fj1euYoYb4NwByHQQ2Q/ZSpmNTYX5n2xF46T4lMGJ3kMzx2bLDwiDKS48VPcGSKDq4XoV2M5TkNaSwQKBgQC8YbtDfAv2Q9CZN48zEMTgzrIHE5/IolBgL/i5cePk6nEBYa1YrcNQ4GQFxk0zpD1INf9vHdTdJQOyfXZfty6VSKOv8AfCtVbv2ACKaDcq6Lxr6O5iVq6x8eETs/ASTephEBC+NVX9TUu5/mKX4KcOx8cv/cUxaxnKK88LGMU1lQKBgQC14IpU3+udGcmNtqRZAX82rqNirr+yAPEDPlb0qBDhfaGZufwo0O56JC+DMZvCeYwNcrKCtA/3gCJpqHYg0Us1BpuvPdijNYXp63w7TIzWS/uZTIBj6RkdpsPRE0bmd66k3L+KcK+fYm9hjlRILPc3RmoE1pXwb0q/qxZn8/zycQKBgCEUOEHdYmxX9CUzcF3TL/8ZzIEZnREqD1sUkWRe71mfIUDQ2hOcT5PAE1BARgYvbONlN/lXD63VdhS9my+rhu6H1tZqG7LBDQcIsu+a5TUPsSZxvIpgFkUA6DkBe8J7FBD/NTFxRCw6inWaic2JkUwzIHL46MuItvqdProAG4ERAoGBAJv3z4jTcZEKkS6G5s8xGm/BOXmh0+AJMKWQay9phPhqLQ/QT26NJUT3DQubmjAe3byWlIDDe0HKzzf+cDbaZH03ioCwL7xeZNVhzOppTgljpYIj+aI0PgaQgTIlLtLq5YiZiWRmGCU9W7i0PpCahvIO31IA1xoVxmsxg4hn0o3xAoGAFS3lEmLPAnrBU6Zd3QI8x6oVOeuYxgp9Ke/jxwSc9jv8C8zqaCK/OYZ5Idp7eYuBMlTYLEmVZghWHHj20D5nrjsYW/DZv1choKzYUYmw6059oJN5RUmtkeqoHI3gvHqVFpB/8SpbpoKDFCJlDUbsMd0yTAqOMbSCkXuZBLYEbxY=";
    /**
     *支付宝的公钥，查看地址：https://openhome.alipay.com/platform/keyManage.htm?keyType=partner
     */
    public static final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAye4q1lgeTrxRqybtZU0RZbaS2fC/vJ+FXgDEPpjGoxOrc/z/Y7zke3N7Lej/u97usysQMQNn02l3L2CzzE5gMxEWmz4+vxywD6k+I/Mz4YJtq1KjlwYEWEFscx4wRdWFfqVsomcYUpSY6Xs9SIhv0EDtcPZOMh7qaOEMRZMBfe3+IEmfxARaDFDemLsTYTc4De58g8ErEfTuXyLaSBfIgFVYvI0PwlUW59aCkHj9YuwW+7KyDGXzk7OjHuouPyxiW5iFqMAMb392FgVp8ck2EJyZTPlABtiS6jCygaf8GHijw2XIDzT0YSeDRVYmYkK5k7nxwKMUiJnf3RuFFdoSOQIDAQAB";

    /**
     * 签名方式
     */
    public static final String SIGN_TYPE = "RSA2";

    /**
     * 调试用，创建TXT日志文件夹路径，见AlipayCore.java类中的logResult(String sWord)打印方法
     */
    public static final String LOG_PATH = "C://";
    /**
     * 字符编码格式 目前支持 gbk 或 utf-8
     */
    public static final String CHARSET = "utf-8";
    /**
     * 接收通知的接口名(回调地址)
     */
    public static final String NOTIFY_URL = "https://dev.pmssaas.com/pms/app/v1/order/callback";

   // public static final String NOTIFY_URL = "https://test.pmssaas.com/pms/api/v1/weixin/pay/aliCallback";
    /**
     * APPID
     */
    public static final String APP_ID = "2016091600523046";
    /**
     * app支付接口名
     */
    public static final String APP_INT_NAME ="alipay.trade.app.pay";
    /**
     *支付宝支付码
     */
    public static final String PRODUCT_CODE = "QUICK_MSECURITY_PAY";

    /**
     * 更新公区服务接口
     */
    public static final String  PUBLIC_SERVICE_URL ="https://test.pmssaas.com/pms/app/v1/public/order";

    //↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

}