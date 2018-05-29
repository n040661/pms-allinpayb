package com.dominator.weixin.service;

public interface WXCoreService {
  String  getComponentVerifyTicket(String timeStamp, String nonce, String msgSignature, String encryptType, String postData) throws Exception ;
}
