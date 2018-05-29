package com.dominator.service;

import com.dominator.utils.api.ApiMessage;


public interface CheckMessageService {
    ApiMessage checkcode(String usercode, String phone);

    ApiMessage sendmobile(String mobile);
}
