package com.dominator.service;

import javax.servlet.http.HttpServletRequest;

public interface ApiLogService {

    /**
     * 记录日志
     * @param request
     */
    void writeLog(HttpServletRequest request);


}
