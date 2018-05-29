package com.dominator.weixin.util;

import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * @Package: honeybee.beebill.util
 * @Description: 工具类:
 * @Description: 验证签名方法
 * @author: liuxin
 * @date: 17/6/3 上午10:52
 * @date: 17/6/6 上午9:25
 */
public class BlmSignature {

    private static RSAPublicKey rsaPublicKey;
    private static RSAPrivateKey rsaPrivateKey;
    private static String ALGORITHM = "SHA256withRSA";

    /**
     * 私钥加密
     *
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static String sign(String privateKey, String text) throws Exception {
       // String text = splitParams(lexicographicOrder(getParamsName(maps)), maps);
        loadPrivateKey(privateKey);
        Signature signature = Signature.getInstance(ALGORITHM);
        signature.initSign(rsaPrivateKey);
        signature.update(text.getBytes());
        byte[] result = signature.sign();
        return Base64.encodeBase64String(result);
    }

    /**
     * 公钥加密
     *
     * @param publicKey 公钥
     * @param text      明文
     * @return
     * @throws Exception
     */
    public static String publicSign(String publicKey,String text) throws Exception {
        //String text = splitParams(lexicographicOrder(getParamsName(maps)), maps);
        loadPublicKey(publicKey);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);
        byte[] result = cipher.doFinal(text.getBytes());
        return Base64.encodeBase64String(result);
    }


    /**
     * 公钥解密
     *
     * @param publicKey 公钥
     * @param text      明文
     * @param sign      密文
     * @return
     * @throws Exception
     */
    public static boolean verify(String publicKey, String text, String sign) {
        //String text = splitParams(lexicographicOrder(getParamsName(maps)), maps);
        loadPublicKey(publicKey);
        boolean flag = false;
        try {
            Signature signature = Signature.getInstance(ALGORITHM);
            signature.initVerify(rsaPublicKey);
            signature.update(text.getBytes());
            byte[] signVar = Base64.decodeBase64(sign);
            flag = signature.verify(signVar);
        } catch (Exception e) {
        }
        return flag;
    }

    /**
     * 私钥解密
     *
     * @param privateKey 私钥
     * @param text       明文
     * @param sign       密文
     * @return
     * @throws Exception
     */
    public static boolean privateVerify(String privateKey, String text, String sign) {
        //String text = splitParams(lexicographicOrder(getParamsName(maps)), maps);
        loadPrivateKey(privateKey);
        boolean flag = false;
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey);
            byte[] signVar = cipher.doFinal(Base64.decodeBase64(sign));
            String str = new String(signVar);
            flag = str.equals(text);
        } catch (Exception e) {

        }
        return flag;
    }


    /**
     * 从字符串中加载公钥
     *
     * @param publicKeyStr 公钥数据字符串
     * @throws Exception 加载公钥时产生的异常
     */
    public static void loadPublicKey(String publicKeyStr) {
        try {
            BASE64Decoder base64Decoder = new BASE64Decoder();
            byte[] buffer = base64Decoder.decodeBuffer(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            rsaPublicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param privateKeyStr
     */
    public static void loadPrivateKey(String privateKeyStr) {
        try {
            BASE64Decoder base64Decoder = new BASE64Decoder();
            byte[] buffer = base64Decoder.decodeBuffer(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            rsaPrivateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取参数名称 key
     *
     * @param maps 参数key-value map集合
     * @return
     */
    private static List<String> getParamsName(Map<String, Object> maps) {
        List<String> paramNames = new ArrayList<>();
        for (Map.Entry<String, Object> entry : maps.entrySet()) {
            paramNames.add(entry.getKey());
        }
        return paramNames;
    }

    /**
     * 参数名称按字典排序
     *
     * @param paramNames 参数名称List集合
     * @return 排序后的参数名称List集合
     */
    private static List<String> lexicographicOrder(List<String> paramNames) {
        Collections.sort(paramNames);
        return paramNames;
    }

    /**
     * 拼接排序好的参数名称和参数值
     *
     * @param paramNames 排序后的参数名称集合
     * @param maps       参数key-value map集合
     * @return String 拼接后的字符串
     */
    private static String splitParams(List<String> paramNames, Map<String, Object> maps) {
        StringBuilder paramStr = new StringBuilder();
        for (String paramName : paramNames) {
            paramStr.append(paramName);
            for (Map.Entry<String, Object> entry : maps.entrySet()) {
                if (paramName.equals(entry.getKey())) {
                    paramStr.append("=").append(String.valueOf(entry.getValue())).append("&");
                }
            }
        }
        return paramStr.toString().substring(0, paramStr.length() - 1);
    }


    public static void main(String[] args) throws Exception {
        /**
         * 私钥匙加密
         */
        String rsaprivateKeyPkcs8 = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKvYjwSFnrezVCBr\n" +
                "d98R9OkDKEdpKcBeOhJi9dTr42ozQ4eyEo0o570ROfOvqSEhXAp8+YhXMg+gCa3f\n" +
                "I29Pxfh/ePjVK1fY1KgS9/sK7Ejtff+dnRUz4snklO/LhBMM8r7kDiJUtyxDqLKV\n" +
                "PngPpE5hJbrsAUgMpz9qGXeVVdxlAgMBAAECgYEAqET0vhvNL9oUfW5eQXsG0we5\n" +
                "cDuEm45o1Mo8J9MXq00pSf1PdxPf31233mtadEuDIe4ANcYUCgLOl4fQ3dVCZpg+\n" +
                "N3osfZA1qUU9AXZv/05C2FzKQF82gplnnUpjeVw5Dbk2HUXY+5GaFD49j0uYRywQ\n" +
                "lNRr8ns0dzNbZl6j3D0CQQDTCD1NqMRHMuzqyGxF44IxQZt63Pjf8tb0KhGnxa+c\n" +
                "lIDGWP0nPsiQJFBH40pW9Uq7q+ZxDUkzxKMkJjPrdyrnAkEA0Ha3rJgNK1v8VlHo\n" +
                "99O/qVB/kSpe6wNt1972j8TFrbh7oPMRYh5yaUBUHYCXYRL9KqIIo6ft88fccs+C\n" +
                "ksaA0wJACvesp/KTcNfWtEUCS4eNZp3wRKxjStBGF55wGHYPsGWPY7+QOI/swIEC\n" +
                "oxnO7UIGRkaizVFRcp1PlTtfC2fAFQJAd/Z5iHcPzQ3oTnxgho1yVCsg3hb1/GRC\n" +
                "x+FlHiLsZyYs7tgx/rbqvCrqvPeNJa3ZxrjsI9G1m34+HEvSJkjZRQJAAPe1XieE\n" +
                "8WSRvvJoPxsoT6ZHNiEO8FNxzuHc6Z3ku/XH1QlLtYgtFFFnZdBWOJnt3/wXUVKI\n" +
                "CiibM4iQSwgPYQ==";

        Map maps = new LinkedHashMap();
        maps.put("orderId", "100000000123");
        maps.put("payOrderId", "1000000003424");
        maps.put("timestamp", "1496324193838");// status=2, amount=1234
        maps.put("status", 2);
        maps.put("amount", 1234);


        //私钥加密
       // String pass = sign(rsaprivateKeyPkcs8, maps);
        //System.out.println("私钥加密后:" + pass);


        String rsapublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCr2I8EhZ63s1Qga3ffEfTpAyhH\n" +
                "aSnAXjoSYvXU6+NqM0OHshKNKOe9ETnzr6khIVwKfPmIVzIPoAmt3yNvT8X4f3j4\n" +
                "1StX2NSoEvf7CuxI7X3/nZ0VM+LJ5JTvy4QTDPK+5A4iVLcsQ6iylT54D6ROYSW6\n" +
                "7AFIDKc/ahl3lVXcZQIDAQAB";
        //公钥解密
       // System.out.println(verify(rsapublicKey, maps, pass));


        /************************************************************/
        //公钥加密
        //String pass1 = publicSign(rsapublicKey, maps);
       // System.out.println("公钥加密后:" + pass1);

        //私钥解密
        //System.out.println(privateVerify(rsaprivateKeyPkcs8, maps, pass1));


    }
    public static String getText(Map<String, Object> maps) {
        return splitParams(lexicographicOrder(getParamsName(maps)), maps);
    }
}