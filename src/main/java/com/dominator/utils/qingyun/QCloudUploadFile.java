package com.dominator.utils.qingyun;

import com.dominFramework.core.id.SystemIdUtils;
import com.dominator.AAAconfig.SysConfig;
import com.qingstor.sdk.config.EvnContext;
import com.qingstor.sdk.service.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;

/**
 * 青云上传文件
 */
@Slf4j
public class QCloudUploadFile {


    //文件上传
    public static String uploadFile(MultipartFile multipartFile) {
        EvnContext evn = new EvnContext(SysConfig.QYAccessKeyId, SysConfig.QYAccessKeySecret);
        Bucket bucket = new Bucket(evn, SysConfig.QYZoneKey, SysConfig.QYBucketName);
        Bucket.PutObjectInput input = new Bucket.PutObjectInput();
        String key = "";

        if (!multipartFile.isEmpty()) {
            String filePath = System.getProperty("user.dir") + "/src/main/resources/";
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdir();
            }
            String filename = multipartFile.getOriginalFilename();
            String fileType = filename.substring(filename.lastIndexOf(".")+1,filename.length());
            log.info("文件类型：" + fileType);
            // 默认不指定key的情况下，以文件内容的hash值作为文件名
            key = SystemIdUtils.uuid() + "." + fileType;
            log.info("指定的名称：" + key);
            String path = filePath + key;
            File tempFile = null;
            //save to the /upload path
            tempFile = new File(path);
            try {
                FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), tempFile);
                //图片是否直接显示
                input.setContentType("text/plain; charset=utf-8");
                input.setContentLength(tempFile.length());
                input.setBodyInputFile(tempFile);
                Bucket.PutObjectOutput out = bucket.putObject(key, input);
                tempFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
                log.error("qingyun uploadFile have error"+e);
            }
        }
        return SysConfig.QYHttpUrl + key;
    }


    public static String upload_QY_qn(byte[] uploadBytes) {
        EvnContext evn = new EvnContext(SysConfig.QYAccessKeyId, SysConfig.QYAccessKeySecret);
        Bucket bucket = new Bucket(evn, SysConfig.QYZoneKey, SysConfig.QYBucketName);
        Bucket.PutObjectInput input = new Bucket.PutObjectInput();
        String key = "QRCode/"+ System.currentTimeMillis() + ".jpg";
        ByteArrayInputStream byteInputStream=new ByteArrayInputStream(uploadBytes);
        try {
            input.setContentType("text/plain; charset=utf-8");
            input.setBodyInputStream(byteInputStream);
            Bucket.PutObjectOutput out = bucket.putObject(key, input);
        } catch (Exception ex) {
            log.error("upload by bytes have error"+ex);
        }
        return SysConfig.QYHttpUrl + key;
    }
}
