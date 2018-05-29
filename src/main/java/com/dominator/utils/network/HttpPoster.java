package com.dominator.utils.network;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominFramework.core.utils.JsonUtils;
import com.dominFramework.core.utils.SystemUtils;
import com.dominator.utils.qrcode.QRCodeUtil;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * <b>HTTP POST工具</b> 支持 http https(url以https://开头自动加上SSL加密)
 *
 * @author
 */
public class HttpPoster {

    private static Logger log = LoggerFactory.getLogger(HttpPoster.class);

    private static final String CONTENT_TYPE_TEXT_JSON = "text/json";

    private static String charset = "utf-8";

    /**
     * 使用http/https协议发送
     *
     * @param url    将要发送的地址
     * @param params Map<String,String> 将要发送的内容
     * @return http 返回码
     * @throws Exception
     */
    public static String post(String url, Map<String, String> params) throws Exception {
        NameValuePair[] nameValuePairs = null;
        if (SystemUtils.isNotEmpty(params)) {
            nameValuePairs = new NameValuePair[params.size()];
            int nameValuePairIndex = 0;
            for (String key : params.keySet()) {
                nameValuePairs[nameValuePairIndex] = new NameValuePair(key, params.get(key));
                nameValuePairIndex++;
            }
        }
        return post(url, nameValuePairs);
    }

    public static String postByDto(String url, Dto dto) throws Exception {
        NameValuePair[] nameValuePairs = null;
        if (SystemUtils.isNotEmpty(dto)) {
            nameValuePairs = new NameValuePair[dto.size()];
            int nameValuePairIndex = 0;
            for (String key : dto.keySet()) {
                nameValuePairs[nameValuePairIndex] = new NameValuePair(key, dto.getString(key));
                nameValuePairIndex++;
            }
        }
        return post(url, nameValuePairs);
    }

    /**
     * 使用http/https协议发送
     *
     * @param url            将要发送的地址
     * @param nameValuePairs NameValuePair 将要发送的内容
     * @return http 返回码
     * @throws Exception
     */
    public static String post(String url, NameValuePair[] nameValuePairs) {
        // 如果为安全连接 https 创建SSL连接
        if (SystemUtils.isEmpty(nameValuePairs)) {
            log.info("请求url：" + url + "参数");
        } else {
            log.info("请求url：" + url + "参数" + nameValuePairs.toString());
        }
        String resStr = null;
        PostMethod post = null;
        try {
            Protocol protocol = null;
            if (url.indexOf("https://") != -1) {
                protocol = new Protocol("https", (ProtocolSocketFactory) (new SSLSocketFactory()), 443);
                Protocol.registerProtocol("https", protocol);
            }

            HttpClient httpclient = new HttpClient();
            httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(1000 * 60);
            post = new PostMethod(url);
            httpclient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charset);

            if (SystemUtils.isNotEmpty(nameValuePairs)) {
                post.setRequestBody(nameValuePairs);
            }


            // 执行请求
            log.info("http工具请求" + post.getRequestEntity());

            httpclient.executeMethod(post);

            Cookie[] cookies = httpclient.getState().getCookies();
            httpclient.getState().addCookies(cookies);

            // 获取返回信息
            InputStream resIns = post.getResponseBodyAsStream();

            resStr = IOUtils.toString(resIns, charset);

            log.info("http工具返回" + resStr);

            if (protocol != null) {
                Protocol.unregisterProtocol("https");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("请求失败" + e.getMessage());
        }

        return resStr;
    }

    /**
     * post方法通过json
     *
     * @param dto
     * @param url
     * @throws IOException
     */
    public static String postByJson(Dto dto, String url) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
        StringEntity se = null;
        String res = null;
        try {
            se = new StringEntity(JsonUtils.toJson(dto));
            se.setContentType(CONTENT_TYPE_TEXT_JSON);
            httpPost.setEntity(se);
            CloseableHttpResponse response = null;
            response = client.execute(httpPost);
            res = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }


    /**
     * post方法通过json
     *
     * @param dto
     * @param url
     * @throws IOException
     */
    public static String postJson(Dto dto, String url) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        StringEntity se = null;
        String res = null;
        try {
            se = new StringEntity(JsonUtils.toJson(dto));
            se.setContentType(CONTENT_TYPE_TEXT_JSON);
            httpPost.setEntity(se);
            CloseableHttpResponse response = null;
            response = client.execute(httpPost);
            res = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }
}