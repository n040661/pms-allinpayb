package com.dominator.utils.system;

import java.awt.Dimension;  
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dominator.utils.encode.Des3Utils;
import net.sf.json.JSONObject;

import org.apache.commons.lang.RandomStringUtils;


/**
 * 封装一些常用的方法
 * @author hong
 * @time 2015年4月29日14:29:47
 */
public class SeemseUtil {
	
	private static JSONObject jsonObj;
	
	/**
	 * 获得系统的当前时间
	 * @return
	 */
	public static String getCurrentTime(){
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String currentTime=df.format(new Date());
		return currentTime;
	}
	
	
	/**
	 * 获得系统的当前时间
	 * @return
	 */
	public static String getCurrentTimeyyyyMMddHHmmss(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime=df.format(new Date());
		return currentTime;
	}
	
	/** 
	 * 
	 * 获得系统的当前时间
	 * @return  yyyyMMdd
	 */
	public static String getCurrentTimeyyyyMMdd(){
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String currentTime=df.format(new Date());
		return currentTime;
	}
	
	/** 
	 * 
	 * 获得系统的当前时间
	 * @return  yyyyMMdd
	 */
	public static String getCurrentTimeyyyyMMddhm(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd|H:m");
		String currentTime=df.format(new Date());
		return currentTime;
	}
	/**
	 * 将时间转换成字符串
	 * @param date
	 * @return
	 */
	public  String dateToString(Date date){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		return sdf.format(date);
	}
	/**
	 * 将时间转换成字符串，指定转换格式
	 * @param date
	 * @return
	 */
	public  String dateToString(Date date,String type){
		SimpleDateFormat sdf=new SimpleDateFormat(type);  
		return sdf.format(date);
	}
	/**
	 * 将字符串转换成时间
	 * @param time
	 * @return
	 */
	public  Date stringToDate(String time){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		Date date=null;
		try {
			if(time.contains("|")){
				date=stringToDate(time,"yyyy-MM-dd|h:m");
			}else{
				date = sdf.parse(time);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	/**
	 * 将字符串转换成时间
	 * @param time
	 * @return
	 */
	public  Date stringToDate(String time,String type){
		SimpleDateFormat sdf=new SimpleDateFormat(type);  
		Date date=null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	/**
	 * 获取到用户的微信信息
	 * 这个地方取得用户的账号密码
	 * @return
	 */
	public static JSONObject getWeixinInfo(HttpServletRequest request){
		//一系列的操作之后得到账号密码
		String userName="yukun.wang";
		String userPassword="123456";
		String userType="1";
		jsonObj = new JSONObject();
		jsonObj.put("userName", userName);
		jsonObj.put("userPassword", userPassword);
		jsonObj.put("userType", userType);
		//String result = "{userName:"+userName+",userPassword:"+userPassword+"}";
		return jsonObj;
	}
	/**
	 * 非空验证，为空返回false，不为空返回true
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(Object obj){
		boolean flag= false;
		//if(obj instanceof String){}
		if(obj instanceof List<?>){
			List<?> list=(List<?>)obj;
			if(list!=null && ! list.isEmpty()){
				flag=true;
			}
		}else if(obj instanceof Set<?>){
			Set<?> set= (Set<?>)obj;
			if(set!=null && ! set.isEmpty()){
				flag=true;
			}
		}else if(obj!=null && !"".equals(obj.toString())){
			flag=true;
		}
		return flag;
	}
	
	/**
	 * 创建验证码
	 * @return
	 */
	public String createIdentifyCode() {
		StringBuffer sb = new StringBuffer();
		String str = "0123456789";
		Random r = new Random();
		for (int i = 0; i < 4; i++) {
			int num = r.nextInt(str.length());
			sb.append(str.charAt(num));
			str = str.replace((str.charAt(num) + ""), "");
		}
		return sb.toString();
	}
	
	
	
	
	
	/**
	 * 将得到的转码的值进行utf-8转换
	 * @param request
	 * @return
	 */
	public String convertURLDecoder(HttpServletRequest request){
		String info="";
		try {
			if(isEmpty(request.getParameter("orderJson"))){
				info = URLDecoder.decode(request.getParameter("orderJson"), "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return info;
	}
	/**
	 * 不解码 只获取值 进行转码
	 * 此处不知道在什么时候后台自动解码了。 需要再次转码返回给jsp
	 * @param request
	 * @return
	 */
	public String convertURL(HttpServletRequest request ){
		String info="";
		if(isEmpty(request.getParameter("orderJson"))){
			info =request.getParameter("orderJson");
			try {
				info=URLEncoder.encode(info, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return info;
	}
	/**
	 * 转码
	 * @param request
	 * @param info
	 * @return
	 */
	public String urlEncode(String info){
		try {
			info=URLEncoder.encode(info, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return info;
	}
	/**
	 * 解码
	 * @param request
	 * @param info
	 * @return
	 */
	public String urlDecode(String info){
		try {
			info = URLDecoder.decode(info, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return info;
	}
	
	
	
	/**
	 * 得到jsp的值。分为是否需要转换。
	 * @param request
	 * @param param
	 * @param isDecode
	 * @return
	 */
	public JSONObject convertRequestForSomeInfo(HttpServletRequest request,String[] param,boolean isDecode){
		jsonObj = new JSONObject();
		try {
			for(int i=0;i<param.length;i++){
				if(isDecode){
					jsonObj.put(param[i], URLDecoder.decode(request.getParameter(param[i]), "UTF-8"));
				}else{
					jsonObj.put(param[i], request.getParameter(param[i]));
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return jsonObj;
	}
	/**
	 * 得到jsp的值。分为是否需要转换。
	 * @param request
	 * @param param
	 * @param isDecode
	 * @return
	 */
	public JSONObject convertRequestForSomeInfo(HttpServletRequest request,String keyName,boolean isDecode){
		jsonObj = new JSONObject();
		try {
				if(isDecode){
					jsonObj.put(keyName, URLDecoder.decode(request.getParameter(keyName), "UTF-8"));
				}else{
					jsonObj.put(keyName, request.getParameter(keyName));
				}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return jsonObj;
	}
	
	
	

	/**
	 * id生成器格式化
	 * @param numberFormat
	 * @param currentValue
	 * @return
	 */
	public String idGeneratorFormat(String numberFormat,int currentValue){
		//numberFormat=0000 currentValue=1 11 111 1111 要注意长度
		String current=String.valueOf(currentValue);
		String str=numberFormat.substring(0, numberFormat.length()-current.length());
		return str+current;
	}
	/**
	 * 日期转换
	 * @param json
	 * @param keyName
	 * @return
	 */
	public JSONObject convertDateJson(JSONObject json,String keyName){
		if(isEmpty(json.get(keyName))){
			//json.put(keyName,stringToDate(jsonObj.getString(keyName)));
		}else{
			json.put(keyName,dateToString(new Date()));
		}
		return json;
	}
		
	
	/**
	 * 返回一个url
	 * @param url
	 * @return
	 */
	public String forwardUrl(String url){
		return "/WEB-INF/jsp/"+url+".jsp";
	}
	
	/**
	 * 微信授权方法
	 * @param request
	 * @param response
	 * @return 微信号
	 */
	public String weixinAuthorize(HttpServletRequest request,HttpServletResponse response){
		String weixinName="scarecrow_zh";
		return weixinName;
	}
	
	/**
	 * 生成用户的店铺url地址
	 * @param request
	 * @param storeId
	 * @return
	 */
	public String createStoreAddress(String storeId){
		//域名由系统配置。 此处只需要存入参数值。如：%7B%22storeId%22%3A%22U000015%22%7D
		jsonObj=new JSONObject();
		jsonObj.put("storeId", storeId);
		return urlEncode(jsonObj.toString());
	}
	
	
	public String createStoreAddress(String storeId,String commodityId){
		//域名由系统配置。 此处只需要存入参数值。如：%7B%22storeId%22%3A%22U000015%22%7D
		jsonObj=new JSONObject();
		jsonObj.put("storeId", storeId);
		jsonObj.put("commodityId", commodityId);
		return urlEncode(jsonObj.toString());
	}
	
	/**
	 * UUID生成器
	 */
	public static String getUUID(){
		return UUID.randomUUID().toString();
	}
	

	
	/**
	 * 对页面上的筛选条件进行处理  设置request
	 * @param param
	 * @param jsonObj
	 */
	public void searchOperator(HttpServletRequest request,String[] param,JSONObject jsonObj){
		for (String str : param) {
			if(isEmpty(jsonObj.get(str))){
				request.setAttribute(str, jsonObj.getString(str));
				jsonObj.remove(str);
			}else{
				request.setAttribute(str, "");
			}
		}
	}
	
	
	
	/**
	 * 是否需要分页 需要则返回true 保证传进来的jsonObj不为null
	 * @param jsonObj
	 * @return
	 */
	/*public boolean isPage(JSONObject jsonObj){
		boolean flag= false;
		if(isEmpty(jsonObj.get(ConstantUtil.IS_PAGE))){
			flag=true;
		}
		return flag;
	}*/

	/**
	 * 获取一年后的时间
	 * @return
	 */
	public Date getAfterOneYear(){
		Calendar calendar = Calendar.getInstance();
        Date date = new Date(System.currentTimeMillis());
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, 1);
        date = calendar.getTime();
        return date;
	}
	
	/**
	 * 对账户进行操作
	 * @param jsonObj
	 * @return
	 */
	/*public JSONObject operatorAmount(JSONObject jsonObj,UserAmount bean ,double deposit){
		if(isEmpty(jsonObj.get("detailType"))){
			//判断类型 做不同的操作
			String detailType=jsonObj.get("detailType").toString();
			double payPrice=Double.valueOf(jsonObj.getString("payPrice"));
			//充值操作 此时 更改 预存款金额，预存款余额
			if(ConstantUtil.DETAIL_TYPE_CZ.equals(detailType)){
				jsonObj.put("preDepositAmount", bean.getPreDepositAmount()+payPrice);
				jsonObj.put("preDepositBalance", bean.getPreDepositBalance()+payPrice);
			}
			//销售操作 销售收入  可提现额 预存款余额
			if(ConstantUtil.DETAIL_TYPE_XS.equals(detailType)){
				//余额需要计算销售的折扣率。 进货价。
				jsonObj.put("preDepositAmount", bean.getPreDepositAmount()-operatorDepositRate(payPrice,deposit));
				jsonObj.put("salesIncome", bean.getSalesIncome()+payPrice);
				jsonObj.put("salesAmount", bean.getSalesAmount()+payPrice);
				jsonObj.put("withdrawalAmount", bean.getWithdrawalAmount()+payPrice);
			}
			//奖金 奖金收入 可提现额
			if(ConstantUtil.DETAIL_TYPE_JJ.equals(detailType)){
				jsonObj.put("bonusIncome", bean.getBonusIncome()+payPrice);
				jsonObj.put("withdrawalAmount", bean.getWithdrawalAmount()+payPrice);
			}
			//分成 分成收入 可提现额
			if(ConstantUtil.DETAIL_TYPE_JJ.equals(detailType)){
				jsonObj.put("sharedRevenue", bean.getSharedRevenue()+payPrice);
				jsonObj.put("withdrawalAmount", bean.getWithdrawalAmount()+payPrice);
			}
			//提现 可提现额
			if(ConstantUtil.DETAIL_TYPE_JJ.equals(detailType)){
				jsonObj.put("withdrawalAmount", bean.getWithdrawalAmount()-payPrice);
			}
		}
		return jsonObj;
	}*/
	/**
	 * 处理折扣率 
	 * @param price
	 * @param deposit
	 * @return
	 */
	public double operatorDepositRate(double price , double deposit){
		if(deposit!=0){
			return (price*deposit)/100;
		}else {
			return price;
		}
	}
	

	
	public boolean jsonKeyIsExist(String jsonKey){
		boolean flag=false;
		if(isEmpty(jsonObj.get(jsonKey))){
			flag=true;
		}
		return flag;
	}
    
	
	
	
	/**
	 * 处理传入的map数据，转换成json格式
	 * @param map
	 * @return
	 */
	public String createForwardParam(Map<String,String> map){
		String json = "{";
		if (isEmpty(map)) {
			Iterator iter = map.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				json += "'" + entry.getKey().toString() + "':" + "'" + entry.getValue().toString() + "'"+",";
			}
		}
		//处理最后一个逗号
		String lastChar= json.substring(json.length()-1,json.length());
		if(lastChar.equals(",")){
			json= json.substring(0,json.length()-1);
		}
		return json + "}";
	}

	/**
	 * 创建目录
	 * @param path
	 */
	public void mkdir(String path){
		File file = new File(path);
		if (!file.exists()) {
			file.mkdir();
		}
	}
	/**
	 * 得到当前的项目路径
	 * @param request
	 * @return
	 */
	public String realPath(HttpServletRequest request){
		return request.getSession().getServletContext().getRealPath("/");
	}
	/**
	 * 得到当前的项目路径
	 * @param request
	 * @return
	 */
	public String realPath(HttpServletRequest request,String path){
		return request.getSession().getServletContext().getRealPath("/"+path);
	}
	/**
	 * 得到目标路径
	 * @param request
	 * @return
	 */
	public String destPath(String realPath,String fileName,String hz){
		if(!isEmpty(fileName)){
			fileName=getUUID();
		}
		return realPath+"/"+fileName + hz;
	}
	/**
	 * 导出文件。
	 * @param request
	 * @param response
	 * @param fileName
	 * @param url
	 */
	public void exportFile(HttpServletRequest request,
			HttpServletResponse response, String fileName,String url) {
		BufferedInputStream bis=null;
		BufferedOutputStream bos=null;
		try {
			response.reset();// 清空输出流
			response.setHeader("Content-disposition", "attachment; filename="
					+ new String(fileName.getBytes("GB2312"), "8859_1"));// 设定输出文件头
			//fileName= 123.jpg 
			response.setContentType("application/x-msdownload");
			// 通知客户文件的MIME类型：  
            bis = new BufferedInputStream(new java.io.FileInputStream((url)));  
            bos = new BufferedOutputStream(response.getOutputStream());  
            byte[] buff = new byte[2048];  
            int bytesRead;  
            int i = 0;  
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {  
                bos.write(buff, 0, bytesRead);  
                i++;  
            }  
            bos.flush();  
		}catch(Exception e){
			e.printStackTrace();
		}finally {  
            if (bis != null) {  
                try {  
                    bis.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
                bis = null;  
            }  
            if (bos != null) {  
                try {  
                    bos.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
                bos = null;  
            }  
        }  
		
	}
	/**
	 * 下载网上的图片到本地
	 * @param request
	 * @param response
	 * @param urlString
	 * @param filename
	 * @param savePath
	 * @return
	 */
	public String downPicToLocal(HttpServletRequest request,HttpServletResponse response,
			String urlString, String filename,String savePath){
		try{
			// 构造URL  
	        URL url = new URL(urlString);  
	        // 打开连接  
	        //URLConnection con = url.openConnection();  
	        HttpURLConnection con = (HttpURLConnection) url.openConnection();  
	        int state = con.getResponseCode();  
            if (state == 200) {  
               //成功！
            } else {  
                return null;  
            }  
	        //设置请求超时为5s  
	        con.setConnectTimeout(5*1000);  
	        // 输入流  
	        InputStream is = con.getInputStream();  
	      
	        // 1K的数据缓冲  
	        byte[] bs = new byte[1024];  
	        // 读取到的数据长度  
	        int len;  
	        // 输出的文件流  
	       File sf=new File(realPath(request)+"/"+savePath);  
	       if(!sf.exists()){  
	           sf.mkdirs();  
	       }  
	       OutputStream os = new FileOutputStream(sf.getPath()+"\\"+filename);  
	        // 开始读取  
	        while ((len = is.read(bs)) != -1) {  
	          os.write(bs, 0, len);  
	        }  
	        // 完毕，关闭所有链接  
	        os.close();  
	        is.close();  
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 上传base64的图片到后台
	 * 
	 * @param request
	 * @param response
	 * @param urlString
	 * @param filename
	 * @param savePath
	 * @return
	 */
	public String upLoadPicToLocal(HttpServletRequest request, HttpServletResponse response, String base64, String filename, String savePath) {
		String imagePath="";
		if (base64 == null) { // 图像数据为空
			return "";
		}
		try {
			File f = new File(realPath(request)+"/"+savePath);
			if(!f.exists()){
				f.mkdir();
			}
			//拼接文件名称，不存在就创建
			imagePath=f.getPath()+ "/" + filename;
			f = new File(imagePath);
			if(!f.exists()){
				f.mkdir();
			}
			 sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder(); 
			 byte[] bytes = decoder.decodeBuffer( base64 ); 
			//构造字节数组输入流
				ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
				//读取输入流的数据
				BufferedImage bi = ImageIO.read(bais);
				//有些照片不需要旋转 怎么判断？
				 //BufferedImage des = Rotate(bi, 90);  
				//将数据信息写进图片文件中
				ImageIO.write(bi, "jpg", f);// 不管输出什么格式图片，此处不需改动
				// ImageIO.write(des, "jpg", f);// 不管输出什么格式图片，此处不需改动
				bais.close();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return savePath+"/" + filename;
	}
	
	
	
	/***
	 * encode by Base64
	 */
	public  String encodeBase64(byte[]input) throws Exception{
		Class clazz=Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
		Method mainMethod= clazz.getMethod("encode", byte[].class);
		mainMethod.setAccessible(true);
		 Object retObj=mainMethod.invoke(null, new Object[]{input});
		 return (String)retObj;
	}
	/***
	 * decode by Base64
	 */
	public  byte[] decodeBase64(String input) throws Exception{
		Class clazz=Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
		Method mainMethod= clazz.getMethod("decode", String.class);
		mainMethod.setAccessible(true);
		 Object retObj=mainMethod.invoke(null, input);
		 return (byte[])retObj;
	}

	/**
	 * 设置session的值
	 * @param request
	 * @param response
	 */
	public void setSession(HttpServletRequest request,HttpServletResponse response
			,String name,Object value ){
		HttpSession session=request.getSession();
		session.setAttribute(name, value);
	}
	/**
	 * 设置session的值
	 * @param request
	 * @param response
	 */
	public void setSession(HttpSession session,String name,Object value ){
		session.setAttribute(name, value);
	}
	
	/**
	 * 第一次用户进入的时候
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
/*	public String firstWeiXin(String storeId) {
		String url=null;
		String redirect = "http://cyggjjt126.oicp.net/kelijin/weixin/manager/oauth/userinfo/redirect";
		String scope = "snsapi_userinfo";
		String state = storeId;
//		wxConfig.getAppId(), wxConfig.getAppSecret(),
		if ("".equals("")) {
			throw new RuntimeException("请联系  协助修整！");
		}
		String oauthCodeUrl = WxApiConfig.getOauthCodeUrl(null, null, redirect, scope, state);
		url="redirect:" + oauthCodeUrl;
		return url;
	}*/
	
	
	
	 public static Map<String,Object> parserToMap(String s){
			Map<String,Object> map=new HashMap<String,Object>();
			JSONObject json=JSONObject.fromObject(s);
			Iterator keys=json.keys();
			while(keys.hasNext()){
				String key=(String) keys.next();
				String value=json.get(key).toString();
				if(value.startsWith("{")&&value.endsWith("}")){
					map.put(key, parserToMap(value));
				}else{
					map.put(key, value);
				}

			}
			return map;
		}
	 public  Map<String,Object> parserToMap(JSONObject json){
			Map<String,Object> map=new HashMap<String,Object>();
			Iterator<?> keys=json.keys();
			while(keys.hasNext()){
				String key=(String) keys.next();
				String value=json.get(key).toString();
			    map.put(key, value);
			}
			return map;
		}
	/**
	 * 处理时间格式 2015-7-26|0:0
	 * Date date=seemseUtil.stringToDate(startTime,"yyyy-MM-dd|h:m"); 也可以得到值
	 * @param time
	 * @return
	 */
	 public String dateOperator(String date){
		 String [] time = date.split("\\|");
			String t1=time[0];
			String[] temp=time[1].split(":");
			String hour=temp[0];
			if(hour.length()<=1){
				hour="0"+hour;
			}
			String s=temp[1];
			if(s.length()<=1){
				s="0"+s;
			}
			String m="00";
			date=t1+" "+hour+":"+s+":"+m;
		 return date;
	 }
	
	 
	 
	 /**
	  * 处理支付接口
	  * @return
	  */
	 public int operatorPay(HttpServletRequest request,double price){
		 int state = 1;//1：交易成功 2 失败
		 return state;
	 } 
	 
	 
	 public  String[] chars = new String[] { "a", "b", "c", "d", "e", "f",  
         "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",  
         "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",  
         "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",  
         "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",  
         "W", "X", "Y", "Z" };  
	 
	 /**
	  * 生成不重复的8位邀请码 
	  * 不能保证唯一
	  * @return
	  */
	 public  String createEightUUID() {  
		    StringBuffer shortBuffer = new StringBuffer();  
		    String uuid = UUID.randomUUID().toString().replace("-", "");  
		    for (int i = 0; i < 8; i++) {  
		        String str = uuid.substring(i * 4, i * 4 + 4);  
		        int x = Integer.parseInt(str, 16);  
		        shortBuffer.append(chars[x % 0x3E]);  
		    }  
		    return shortBuffer.toString();  
		}
	 
	 /**
	  * 生成不重复的8位邀请码 
	  * 不能保证唯一
	  * @return
	  */
	 public  String createEightStr() {  
		 return RandomStringUtils.random(8, "abcdefghjklmnpqrstuvwxyz1234567890");
	}
    /**
     * 
     * @param salePrice    商城中销售价钱
     * @param saleRate     销售折扣率
     * @param depositRate  进货折扣率
     * @return  加盟商的进货价钱
     */
	 public static double getBisnessPayPrice(double salePrice,double saleRate,double depositRate){
		return formaterPrice((salePrice/saleRate)*depositRate);
	 }
	 /**
	  * 获取分销商从加盟商处 应该从加盟商产品库中扣除的
	  * @param jhPayRealPrice
	  * @return 如分销商 进货付 2000 将从加盟商产品为中减去 1400
	  */
	 public static double getFXJHPrice(double fxjhPayRealPrice){
		 //2000/0.5*0.35=1400
		 return formaterPrice(fxjhPayRealPrice/0.5*0.35);
	 }
	 
	    /**
	     * 
	     * @param salePrice    商城中销售价钱
	     * @param saleRate     销售折扣率
	     * @return  加盟商的收入如 出售 70元，收入为 70元
	     */
		 public static double getBIncoming(double salePrice,double saleRate){
//			return (salePrice/saleRate)*saleRate;
			 return salePrice;
		 }
		 /**
		  * 得到团队奖的收益比例
		  * @param teamTotalIncoming
		  * @return
		  */
		 public static double getIteamIncomingRate(double teamTotalIncoming){
			 if(teamTotalIncoming < 5000){
				 return 0.0;
			 }else if(teamTotalIncoming >=5000 && teamTotalIncoming <10000){
				 return 0.05;
			 }else if(teamTotalIncoming >=10000 && teamTotalIncoming <30000){
				 return 0.075;
			 }else if(teamTotalIncoming >= 30000){
				 return 0.1;
			 }
			 return 0.0;
		 }
	 
	 /**
	  * 获取加盟商的销售收入 
	  * @param salePrice  销售价钱
	  * @param saleRate  销售折扣
	  * @param depositRate  进货折扣
	  * @return  加盟商销售收入
	  * 70,0.7,0.35  返回 35 块钱 
	  */
	 public static double getBSaleIncoming(double salePrice,double saleRate,double depositRate){
		double enterPrince=(salePrice/saleRate)*depositRate;
		return formaterPrice(salePrice-enterPrince);
	 }
	 
	 /**
	  * 返回分销商的销售收入
	  * @param productSalePrice 商品在商城中的销售价 70
	  * @param saleRate       销售折扣率 0.7
	  * @param depositRate   进货折扣率 0.5
	  * @return   返回分销商的销售收入  20
	  */
	 public static double getDistIncoming(double productSalePrice,double saleRate,double depositRate){
		 double orgPrice=productSalePrice/saleRate; //产品原价 
		 double enterPrice=orgPrice*depositRate;    //进货时的价钱
		 return  formaterPrice(productSalePrice-enterPrice);
	 }
	 /**
	  * 返回 上级加盟的收入
	  * @param productSalePrice 产品商城销售价 
	  * @param saleRate     销售折扣率
	  * @param bdepositRate  小B进货折扣率
	  * @param ddepositRate  分销商进货折扣率
	  * @return  上级加盟的收入
	  */
	 
	 public static double getBincomingForDistIncoming(double productSalePrice,double saleRate,
		 double bdepositRate,double ddepositRate){
		 double orgPrice=productSalePrice/saleRate; //产品原价 
	    return formaterPrice(orgPrice*(ddepositRate-bdepositRate));
	 }
	 
	 public static double formaterPrice(double price){
		 DecimalFormat df=new DecimalFormat("#.00");
		return Double.valueOf(df.format(price));
	 }
	 /**
	  * 本月最后一天的日期
	  * @return
	  */
	 public static String getMonthLastDate(){
			// 获取Calendar
			Calendar calendar = Calendar.getInstance();
			// 设置时间,当前时间不用设置
			// calendar.setTime(new Date());
			// 设置日期为本月最大日期
			calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));

			// 打印
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateStr=format.format(calendar.getTime());
			System.out.println(dateStr);
			return dateStr;
			
			/*
			 * nt MaxDay=c.getActualMaximum(Calendar.DAY_OF_MONTH);
			  //按你的要求设置时间
			  c.set( c.get(Calendar.YEAR), c.get(Calendar.MONTH), MaxDay, 23, 59, 59);
			  //按格式输出
			  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
			  String gtime = sdf.format(c.getTime()); //上月最后一天
			 */

	 }
	 /**
	  * 下个月1号
	  * @return
	  */
	 public static String getNextMonth(){
		 Calendar c = Calendar.getInstance();
	    c.set(Calendar.MONTH, c.get(Calendar.MONTH)+1);
	    c.set(Calendar.DAY_OF_MONTH, 1);
	    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    String dateStr=format.format(c.getTime());
	    
	    /*
	     *  c.set(Calendar.MONTH, c.get(Calendar.MONTH)+1);
		    c.set(Calendar.DAY_OF_MONTH, 0);
		    System.out.println("下个月的最后一天: " + c.getTime());
	     */
	    return dateStr+" 00:00:00";
	 }
	 
	 /** 
	   * 得到几天后的时间 
	   * @param d 
	   * @param day 
	   * @return 
	   */
	 
	public static Date getDateAfter(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		return now.getTime();
	}
	  
	  /** 
	   * 得到几天后的时间 
	   * @param d 
	   * @param day 
	   * @return 
	   */
	 
	public static String getDateAfter(int day) {
		Calendar now = Calendar.getInstance();
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
	    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    String dateStr=format.format(now.getTime());
		return dateStr+" 00:00:00";
	}
	
	public static Date getDateFromStr(String str){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = sdf.parse(str);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static boolean isToday(String compDate){
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		  String d1 = sdf.format(new Date());//第一个时间
//		  String d2 = sdf.format(new Date());//第二个时间  今天的时间
		  String d1 = sdf.format(getDateFromStr(compDate));//第一个时间
		  String d2 = sdf.format(new Date());//第二个时间  今天的时间
//		  System.out.println(d1.equals(d2));//判断是否是同一天
		  return d1.equals(d2);
	}
	 
	 public static void main(String[] args) {
		 
//		 System.out.println(getBisnessPrice(70L,0.7,0.35));
//		 System.out.println(getMonthLastDate());
//		 System.out.println(getNextMonth());
//		 System.out.println(getDateAfter(2));
		 
//		  System.out.println(isToday("2015-08-08 00:00:00"));
//		 DecimalFormat df=new DecimalFormat("#.00");
//		 double bincoming=getBincomingForDistIncoming(70,0.7,0.35,0.5);
//		 System.out.println(getDistIncoming(70,0.7,0.5));
//		 System.out.println( formaterPrice(bincoming));
//		 System.out.println(getBSaleIncoming(70,0.7,0.35));
//		 System.out.println(getBisnessPayPrice(200, 0.7, 0.5));
//		 double imcon=50000;
//		 System.out.println(imcon*getIteamIncomingRate(imcon));
		 System.out.println(getFXJHPrice(2000));
	}
	 
	 
	 
	 
	 
	 /**
	  * 得到ip地址
	  * @param request
	  * @return
	  */
	 public static String getIpAddr(HttpServletRequest request) {
			String ipAddress = request.getHeader("x-forwarded-for");
			if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
				ipAddress = request.getHeader("Proxy-Client-IP");
			}
			if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
				ipAddress = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
				ipAddress = request.getRemoteAddr();
				if (ipAddress.equals("127.0.0.1")) {
					// 根据网卡取本机配置的IP
					InetAddress inet = null;
					try {
						inet = InetAddress.getLocalHost();
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
					ipAddress = inet.getHostAddress();
				}
			}
			// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
			if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length() = 15
				if (ipAddress.indexOf(",") > 0) {
					ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
				}
			}
			return ipAddress;
		}
	 
	 /**
	  * 创建xml格式数据
	  * @param map
	  * @return
	  */
	 public static String createXml(Map<String,String> map){
		 System.out.println(Des3Utils.encResponse(map));
		 String xml="<xml>";
		 if (isEmpty(map)) {
				Iterator iter = map.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					System.out.println(entry);
					xml+="<"+entry.getKey().toString()+">"+entry.getValue().toString()+"</"+entry.getKey().toString()+">";
				}
			}
		 xml+="</xml>";
		 return xml;
	 }
	 /**
	  * 创建xml格式数据
	  * @param map
	  * @return
	  */
	 public static String createXml2(Map<String,String> map){
		 SortedMap<String, Object> reqMap = new TreeMap<String, Object>();
		 String sign ="";
		 if (isEmpty(map)) {
				Iterator iter = map.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					if(!entry.getKey().toString().equals("sign"))
					reqMap.put(entry.getKey().toString(), entry.getValue().toString());
					else
						sign =  entry.getValue().toString();
				}
			}
		 String xml="<xml>";
		 if (isEmpty(reqMap)) {
				Iterator iter = reqMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					xml+="<"+entry.getKey().toString()+">"+entry.getValue().toString()+"</"+entry.getKey().toString()+">";
				}
			}
		 xml+="<sign>"+sign+"</sign>";
		 xml+="</xml>";
		 return xml;
	 }
	 /**判断是否超过48小时
	   * @param date1
	   * @param date2
	   * @return boolean true 未超过  false 超过48 小时
	   * @throws Exception
	   */
	public static boolean judgmentDate(String date1, String date2)
			throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date start = sdf.parse(date1);
		Date end = sdf.parse(date2);
		long cha = end.getTime() - start.getTime();
		if (cha < 0) {
			return false;
		}
		double result = cha * 1.0 / (1000 * 60 * 60);
		if (result <= 48) {
			return true;
		} else {
			return false;
		}

	}
	
	/**
     * 旋转图片为指定角度
     * 
     * @param bufferedimage
     *            目标图像
     * @param degree
     *            旋转角度
     * @return
     */
    public static BufferedImage rotateImage(final BufferedImage bufferedimage,
            final int degree) {
        int w = bufferedimage.getWidth();
        int h = bufferedimage.getHeight();
        int type = bufferedimage.getColorModel().getTransparency();
        BufferedImage img;
        Graphics2D graphics2d;
        (graphics2d = (img = new BufferedImage(w, h, type))
                .createGraphics()).setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2d.rotate(Math.toRadians(degree), w / 2, h / 2);
        graphics2d.drawImage(bufferedimage, 0, 0, null);
        graphics2d.dispose();
        return img;
    }

    public static BufferedImage Rotate(Image src, int angel) {  
        int src_width = src.getWidth(null);  
        int src_height = src.getHeight(null);  
        // calculate the new image size  
        Rectangle rect_des = CalcRotatedSize(new Rectangle(new Dimension(  
                src_width, src_height)), angel);  
  
        BufferedImage res = null;  
        res = new BufferedImage(rect_des.width, rect_des.height,  
                BufferedImage.TYPE_INT_RGB);  
        Graphics2D g2 = res.createGraphics();  
        // transform  
        g2.translate((rect_des.width - src_width) / 2,  
                (rect_des.height - src_height) / 2);  
        g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);  
  
        g2.drawImage(src, null, null);  
        return res;  
    }  
  
    public static Rectangle CalcRotatedSize(Rectangle src, int angel) {  
        // if angel is greater than 90 degree, we need to do some conversion  
        if (angel >= 90) {  
            if(angel / 90 % 2 == 1){  
                int temp = src.height;  
                src.height = src.width;  
                src.width = temp;  
            }  
            angel = angel % 90;  
        }  
  
        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;  
        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;  
        double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;  
        double angel_dalta_width = Math.atan((double) src.height / src.width);  
        double angel_dalta_height = Math.atan((double) src.width / src.height);  
  
        int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha  
                - angel_dalta_width));  
        int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha  
                - angel_dalta_height));  
        int des_width = src.width + len_dalta_width * 2;  
        int des_height = src.height + len_dalta_height * 2;  
        return new Rectangle(new Dimension(des_width, des_height));
    }  
    
    
	
    
    /**
     * 图片信息获取metadata元数据信息
     * @param fileName 需要解析的文件
     * @return
     */
     public void parseImgInfo (String fileName)
     {
      //   File file = new File(fileName);
         System.out.println(fileName+"-------fileName");
         try {
             //Map.Entry map=  (Map.Entry)tags.entrySet();
             /*for (Map.Entry entry : map) {
                 System.out.println(entry.getKey() + "[" + entry.getKey()
                         + "]:" + entry.getValue());
             }*/
      
            /* // 修改EXIF的拍照日期
             exifHeader.setValue(Tag.DATETIMEORIGINAL, "2007:11:04 07:42:56");
             // 保存
             jpegHeaders.save(true);*/
             //imgInfoBean = printImageTags(file, metadata);
         } catch (Exception e) {
             System.err.println("error 1a: " + e);
         } 
     }
  
     /**
      * 经纬度转换  度分秒转换
      * @param point 坐标点
      * @return
      */
     public String pointToLatlong (String point ) {
         Double du = Double.parseDouble(point.substring(0, point.indexOf("°")).trim());
         Double fen = Double.parseDouble(point.substring(point.indexOf("°")+1, point.indexOf("'")).trim());
         Double miao = Double.parseDouble(point.substring(point.indexOf("'")+1, point.indexOf("\"")).trim());
         Double duStr = du + fen / 60 + miao / 60 / 60 ;
         return duStr.toString();
     }
     
     /**
      * 随机字符串
      * @param 
      * @return String
      */
     public static String randoms(){
    	 String str = RandomStringUtils.random(32, "abcdefghjklmnpqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890");
    	 return str;
     }
 
}
