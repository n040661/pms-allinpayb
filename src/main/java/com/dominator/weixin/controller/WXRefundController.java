package com.dominator.weixin.controller;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.utils.JsonUtils;
import com.dominator.enums.ReqEnums;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.exception.ApiException;
import com.dominator.weixin.service.WXRefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by zhangsuliang on 2017/9/21.
 */
@CrossOrigin
@RequestMapping("/api/v1/otosaas/order")
@RestController
public class WXRefundController {

    @Autowired
    private WXRefundService wxRefundService;

    /**
     * 申请退款
     * @param paramStr  必填字段：
     *                        appid 公众号ID
     *                        mch_id 商户号
     *                        nonce_str 随机字符串
     *                        sign   签名
     *                        transaction_id 微信订单号
     *                        out_trade_no  商户订单号（，商户系统内部的订单号，与transaction_id二选一）
     *                        out_refund_no 商户退款号（商户系统内部退单号）
     *                        total_fee	  订单金额
     *                        refund_fee  退款费用
     *
     *                  非必填字段：sign_type  签名类型
     *                            refund_fee_type  货币种类
     *                            refund_desc 退款原因
     *                            refund_account  退款资金来源
     * @return
     *                 return_code 返回状态码   SUCCESS/FAIL
     *                 return_msg  返回信息
     *                 以下字段在return_code为SUCCESS的时候有返回
     *                 result_code  业务结果
     *                 err_code  错误代码
     *                 err_code_des  错误代码描述
     *                 appid  公众账号ID
     *                 mch_id  商户号
     *                 nonce_str  随机字符串
     *                 sign  签名
     *                 transaction_id  微信订单号
     *                 out_trade_no  商户订单号
     *                 out_refund_no  商户退款单号
     *                 refund_id  微信退款单号
     *                 refund_fee  退款金额
     *                 settlement_refund_fee  应结退款金额
     *                 total_fee  标价金额
     *                 settlement_total_fee  应结订单金额
     *                 fee_type  标价币种
     *                 cash_fee  现金支付金额
     *                 cash_fee_type  现金支付币种
     *                 cash_refund_fee  现金退款金额
     *                 coupon_type_$n  代金券类型
     *                 coupon_refund_fee  代金券退款总金额
     *                 coupon_refund_fee_$n  单个代金券退款金额
     *                 coupon_refund_count  退款代金券使用数量
     *                 coupon_refund_id_$n  退款代金券ID
     */
    @PostMapping("/refund0")
    public Map<String, Object> refund4Wechat(@RequestParam String paramStr, HttpServletRequest request){
        try {
            Dto dto = JsonUtils.toDto(paramStr);
            return wxRefundService.refund4Wechat(dto, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 申请退款
     * @param paramStr  必填字段：
     *                        appid 公众号ID
     *                        mch_id 商户号
     *                        nonce_str 随机字符串
     *                        sign   签名
     *                        transaction_id 微信订单号
     *                        out_trade_no  商户订单号（，商户系统内部的订单号，与transaction_id二选一）
     *                        out_refund_no 商户退款号（商户系统内部退单号）
     *                        total_fee	  订单金额
     *                        refund_fee  退款费用
     *
     *                  非必填字段：sign_type  签名类型
     *                            refund_fee_type  货币种类
     *                            refund_desc 退款原因
     *                            refund_account  退款资金来源
     * @return
     *                 return_code 返回状态码   SUCCESS/FAIL
     *                 return_msg  返回信息
     *                 以下字段在return_code为SUCCESS的时候有返回
     *                 result_code  业务结果
     *                 err_code  错误代码
     *                 err_code_des  错误代码描述
     *                 appid  公众账号ID
     *                 mch_id  商户号
     *                 nonce_str  随机字符串
     *                 sign  签名
     *                 transaction_id  微信订单号
     *                 out_trade_no  商户订单号
     *                 out_refund_no  商户退款单号
     *                 refund_id  微信退款单号
     *                 refund_fee  退款金额
     *                 settlement_refund_fee  应结退款金额
     *                 total_fee  标价金额
     *                 settlement_total_fee  应结订单金额
     *                 fee_type  标价币种
     *                 cash_fee  现金支付金额
     *                 cash_fee_type  现金支付币种
     *                 cash_refund_fee  现金退款金额
     *                 coupon_type_$n  代金券类型
     *                 coupon_refund_fee  代金券退款总金额
     *                 coupon_refund_fee_$n  单个代金券退款金额
     *                 coupon_refund_count  退款代金券使用数量
     *                 coupon_refund_id_$n  退款代金券ID
     */
    @PostMapping("/refund")
    public Map<String, Object> refund(@RequestParam String paramStr, HttpServletRequest request) {
        try {
            Dto dto = JsonUtils.toDto(paramStr);
            return wxRefundService.refund(dto, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}