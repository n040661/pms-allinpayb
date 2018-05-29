package com.dominator.utils.sms;

import com.dominator.utils.exception.ApiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ShortUrlUtil {
    private static Logger logger = LoggerFactory.getLogger(ShortUrlUtil.class);

    public static int TIMEOUT = 30 * 1000;
    public static String ENCODING = "UTF-8";

    /**
     * JSON
     * get value by key
     *
     * @param replyText
     * @param key
     * @return
     */
    private static String getValueByKey_JSON(String replyText, String key) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node;
        String tinyUrl = null;
        try {
            node = mapper.readTree(replyText);
            tinyUrl = node.get(key).asText();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tinyUrl;
    }


    /**
     * 通过HttpConnection 获取返回的字符串
     *
     * @param connection
     * @return
     * @throws IOException
     */
    private static String getResponseStr(HttpURLConnection connection)
            throws IOException {
        StringBuffer result = new StringBuffer();
        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(in, ENCODING));
            String inputLine = "";
            while ((inputLine = reader.readLine()) != null) {
                result.append(inputLine);
            }
        }
        return String.valueOf(result);
    }


    /**
     * ‘
     * 百度短链接接口  无法处理不知名网站，会安全识别报错
     */
    public static void main(String[] args) {
        try{
            URL url = new URL("http://dwz.cn/create.php");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            //POST Request Define:
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setConnectTimeout(TIMEOUT);
            connection.setRequestMethod("GET");
            String longUrl = "url=https://webapp.pmssaas.com/h5/#/app/business/bill-detail?appId=wx7dbda27aa5a445ed&masterplate=%7B%22bill_id%22%3A%225b3d2788a8aa4fb8abcde7ebba0db0ff%22,%22garden_id%22%3A%220ef5762b601a44c2bf19742853e766f2%22%7D";

            connection.getOutputStream().write(longUrl.getBytes());
            connection.connect();

            String responseStr = getResponseStr(connection);
            System.out.println("response string: " + responseStr);

            String tinyUrl = getValueByKey_JSON(responseStr, "tinyurl");
            System.out.println("tinyurl: " + tinyUrl);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public static class HttpUtil {
        private static Logger log = LoggerFactory.getLogger(HttpUtil.class);

        private static final String DEFAULT_HTTP_CHARSET = "UTF-8";

        // private static final String GETID_URL = ProPertiesUtil.getValue("/server.properties", "getidurl");

        private static DefaultHttpClient httpClient = new DefaultHttpClient();

        private HttpUtil() {
        }



        public static String httpGet(String url) {
        String strReturn = "";
        HttpUriRequest request = new HttpGet(url);
        try {
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            strReturn = EntityUtils.toString(entity, DEFAULT_HTTP_CHARSET);
        } catch (Exception e) {
            // 网络异常
            log.error(e.getMessage());
        }
        return strReturn;
    }
    public static String getShortUrl(String longUrl) throws ApiException {
        String url = "http://api.weibo.com/2/short_url/shorten.json?source=2849184197&url_long="+URLEncoder.encode(longUrl);
        JSONObject json = JSONObject.fromObject(httpGet(url));
        JSONArray json1 = json.getJSONArray("urls");
        //JSONArray json = JSONArray.parseArray(httpGet(url));
        if(!json1.getJSONObject(0).getString("type").equals("0")){
            throw  new ApiException("104",json1.getJSONObject(0).getString("type"));
        }
        return json1.getJSONObject(0).getString("url_short");
    }





        public static void main(String[] args) throws Exception {


            String strUrl1 = "https://webapp.pmssaas.com/h5/#/app/business/bill-detail?appId=wx7dbda27aa5a445ed&masterplate=%7B%22bill_id%22%3A%225b3d2788a8aa4fb8abcde7ebba0db0ff%22,%22garden_id%22%3A%220ef5762b601a44c2bf19742853e766f2%22%7D";
            //String url = "http://api.weibo.com/2/short_url/shorten.json?source=2849184197&url_long=www.baidu.com";
            getShortUrl(strUrl1);
            //System.out.println(httpGet(url));
        }

    }

}



