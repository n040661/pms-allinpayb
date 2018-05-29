package com.dominator.weixin.errorCode;

/**
 * Created by zhangsuliang on 2017/8/31.
 */
public class PMSaaSErrorCodeType {
    /**
     * component_verify_ticket
     */
    public final static String COMPONENT_VERYFY_TICKET_IS_NULL="component_verify_ticket值为空";
    public final static String COMPONENT_VERYFY_TICKET_EXPIRE="component_verify_ticket已过期";

    /**
     * 预授权码
     */
    public final static String PRE_AUTH_CODE_EXPIRED="预授权码已过期";


    /**
     * 授权码
     */
    public final static String AUTHORIZATION_CODE_EXPIRED="授权码已过期";

    public final static String NO_AUTHORIZATION_CODE="未获取到授权码";

    public final static String UNAUTHORIZED="第三方应用未授权";

    /**
     * 网页授权
     */

    public final static String WEBPAGE_ACCESS_TOKEN_INVALIDE="accessToken值无效";

    /**
     * 支付参数
     */

    public final static String PAY_PARAM_SAVE_UPDATE_ERROR="支付参数保存或更新错误";

}
