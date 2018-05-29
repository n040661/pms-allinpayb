package com.dominator.weixin.service;

import com.dominator.entity.TblWxopenCompAccessToken;

/**
 * Created by zhangsuliang on 2017/8/28.
 */

public interface AuthorizerAccessTokenService {

    int insert(TblWxopenCompAccessToken AuthorizerAccessToken);

    int update(TblWxopenCompAccessToken authorizerAccessToken);

}
