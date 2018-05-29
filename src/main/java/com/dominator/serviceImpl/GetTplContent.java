package com.dominator.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GetTplContent {
	
	private static Logger logger = LoggerFactory.getLogger(GetTplContent.class);
	//根据云片网给我们的唯一标识获取模板
	public String getcontent(String apikey,String text){
		String value = "";
		try {
			if (StringUtils.isNotEmpty(apikey)) {
				String sendSms = sendSms(apikey);
				ObjectMapper mapper = new ObjectMapper();     
			    JsonNode node = mapper.readTree(sendSms); 
			    JsonNode readTree = mapper.readTree( node.get("template").toString());
			    String str = readTree.get(0).get("tpl_content").toString();
				value = str.replace("#code#", text);
				value = value.substring(1, value.length()-1);
			}
		} catch (Exception e) {
			logger.error("get tplcontent have a error : "+e);
			return value;
		}
		return value;
	}

	public static String sendSms(String apikey) throws IOException {  
	    Map<String, String> params = new HashMap<String, String>();  
	    params.put("apikey", apikey);  
	   return post("https://sms.yunpian.com/v1/tpl/get.json", params);  
	}  
	  
	/** 
	* 基于HttpClient 4.3的通用POST方法 
	* 
	* @param url       提交的URL 
	* @param paramsMap 提交<参数，值>Map 
	* @return 提交响应 
	*/  
	  
	public static String post(String url, Map<String, String> paramsMap) {  
	    CloseableHttpClient client = HttpClients.createDefault();
	    String responseText = "";  
	    CloseableHttpResponse response = null;
	    try {  
	        HttpPost method = new HttpPost(url);
	        if (paramsMap != null) {  
	            List<NameValuePair> paramList = new ArrayList<NameValuePair>();
	            for (Map.Entry<String, String> param : paramsMap.entrySet()) {  
	                NameValuePair pair = new BasicNameValuePair(param.getKey(), param.getValue());
	                paramList.add(pair);  
	            }  
	            method.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
	        }  
	        response = client.execute(method);  
	        HttpEntity entity = response.getEntity();
	        if (entity != null) {  
	            responseText = EntityUtils.toString(entity);
	        }  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    } finally {  
	        try {  
	            response.close();  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	    }
		return responseText;                
	}
}
