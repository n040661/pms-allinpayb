//package com.dominator.utils.io;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//import com.dominator.utils.system.PropertiesLoader;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.google.gson.Gson;
//import com.qiniu.common.QiniuException;
//import com.qiniu.common.Zone;
//import com.qiniu.http.Response;
//import com.qiniu.storage.Configuration;
//import com.qiniu.storage.UploadManager;
//import com.qiniu.storage.model.DefaultPutRet;
//import com.qiniu.util.Auth;
//
///**
// * 文件上传类
// *
// * @author zdx
// *
// */
//public class UploadFile {
//
//	public static PropertiesLoader propertiesLoader = new PropertiesLoader("sysconfig.properties");
//
//
//	/**
//	 * 七牛配置
//	 */
//	public static final String QN_accessKey  = propertiesLoader.getProperty("qn_accessKey");
//	public static final String QN_secretKey  = propertiesLoader.getProperty("qn_secretKey");
//	public static final String QN_bucket  = propertiesLoader.getProperty("qn_bucket");
//	public static final String QN_url=propertiesLoader.getProperty("qn_http_url");
//
//	public static Map<String, String> upload_img_qn(MultipartFile file) {
//		Map<String, String> returnCode = new HashMap<String, String>();
//		// 构造一个带指定Zone对象的配置类
//		Configuration cfg = new Configuration(Zone.zone0());
//		// ...其他参数参考类注释
//		UploadManager uploadManager = new UploadManager(cfg);
//		// ...生成上传凭证，然后准备上传
//		String accessKey = QN_accessKey;
//		String secretKey = QN_secretKey;
//		String bucket = QN_bucket;// 七牛存储空间
//
//		String originalFileName = file.getOriginalFilename();
//		System.out.println("原始名称：" + originalFileName + "." + originalFileName.lastIndexOf("."));
//
//		String fileType = file.getContentType();
//		System.out.println("文件类型：" + fileType);
//		// 默认不指定key的情况下，以文件内容的hash值作为文件名
//		String key = "header_imager/"+System.currentTimeMillis() + "." + fileType.split("/")[1];
//		System.out.println("指定的名称：" + key);
//		// 将流转换成byte数组
//		byte[] uploadBytes = null;
//		try {
//			uploadBytes = file.getBytes();
//		} catch (IOException e) {
//			e.printStackTrace();
//			returnCode.put("code", e.getMessage());
//			return returnCode;
//		}
//		Auth auth = Auth.create(accessKey, secretKey);
//		String upToken = auth.uploadToken(bucket);// 获取上传Token
//		try {
//			Response response = uploadManager.put(uploadBytes, key, upToken);
//			// 解析上传成功的结果
//			DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
//			System.out.println(putRet.key);
//			System.out.println(putRet.hash);
//			returnCode.put("code", "SUCCESS");
//			returnCode.put("QN_saveUrl", QN_url+putRet.key);
//			return returnCode;
//		} catch (QiniuException ex) {
//			Response r = ex.response;
//			System.err.println(r.toString());
//			returnCode.put("code", ex.getMessage());
//			return returnCode;
//		}
//
//	}
//
//	public static String upload_QR_qn(byte[] uploadBytes) {
//		//构造一个带指定Zone对象的配置类
//		Configuration cfg = new Configuration(Zone.zone0());
//		//...其他参数参考类注释
//		UploadManager uploadManager = new UploadManager(cfg);
//		//默认不指定key的情况下，以文件内容的hash值作为文件名
//
//		String accessKey = QN_accessKey;
//		String secretKey = QN_secretKey;
//		String bucket = QN_bucket;// 七牛存储空间
//
//		String key = "QRCode/"+System.currentTimeMillis() + ".jpg";
//	    ByteArrayInputStream byteInputStream=new ByteArrayInputStream(uploadBytes);
//	    Auth auth = Auth.create(accessKey, secretKey);
//	    String upToken = auth.uploadToken(bucket);
//	    try {
//	        Response response = uploadManager.put(byteInputStream,key,upToken,null, null);
//	        //解析上传成功的结果
//	        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
//	        System.out.println(putRet.key);
//	        System.out.println(putRet.hash);
//	        return QN_url+key;
//	    } catch (QiniuException ex) {
//	        Response r = ex.response;
//	        System.err.println(r.toString());
//	        try {
//	            System.err.println(r.bodyString());
//	        } catch (QiniuException ex2) {
//	            //ignore
//	        }
//	    }
//		return null;
//	}
//}
