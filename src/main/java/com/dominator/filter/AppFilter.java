package com.dominator.filter;

import com.auth0.jwt.interfaces.Claim;
import com.dominFramework.core.utils.JsonUtils;
import com.dominFramework.core.utils.SystemUtils;
import com.dominator.enums.ReqEnums;
import com.dominator.utils.api.ApiMessage;
import com.dominator.utils.dao.RedisUtil;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.jwttoken.JwtToken;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@Slf4j
@WebFilter(filterName = "appFilter",
        urlPatterns = {"/app/v1/*"},
        initParams = {
                @WebInitParam(name = "passURI", value = ".*login$,.*test.*,.*checkUserName$," +
                        ".*project/list$,.*space/list$," +
                        ".*garden/list$,.*/banner$,.*common.*,.*register.*,"+
                        ".*resetPassword$,.*/activity/all$,"+
                        ".*otosaas/getOrderDetail")
        }
)
public class AppFilter implements Filter {

    private String[] passURI;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        passURI = filterConfig.getInitParameter("passURI").split(",");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        log.info("apiFilter");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        log.info("content-type:{}", request.getHeader("content-type"));
        String reqURI = request.getRequestURI();
        log.info("uri[{}]:{}", request.getMethod(), reqURI);
        boolean pass = isPass(reqURI);
        if (pass) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        String token = request.getHeader("token");
        if (SystemUtils.isEmpty(token)) {
            response.setHeader("content-type", "application/json;charset=UTF-8");
            response.getWriter().print(JsonUtils.toJson(new ApiMessage(ReqEnums.REQ_TOKEN_FALSE)));
            return;
        } else {
            try {
                //展示params
                Map<String, Claim> claimMap = JwtToken.verifyToken(token);
                log.info("---params---");
                for (Map.Entry<String, Claim> entry : claimMap.entrySet()) {
                    if (entry.getValue().asString() != null)
                        log.info("{}:{}", entry.getKey(), entry.getValue().asString());
                    if (entry.getValue().asDate() != null)
                        log.info("{}:{}", entry.getKey(), entry.getValue().asDate());
                }
                log.info("---params---");
                //token比对redis
                filterChain.doFilter(servletRequest, servletResponse);
               /* String userName = claimMap.get("userName").asString();
                if (token.equals(ru.get("jwt_" + userName))) {
                    filterChain.doFilter(servletRequest, servletResponse);
                } else {
                    response.setHeader("content-type", "application/json;charset=UTF-8");
                    response.getWriter().print(JsonUtils.toJson(new ApiMessage(ReqEnums.REQ_TOKEN_DUO)));
                    return;
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isPass(String reqURI) {
        boolean pass = false;
        for (String str : passURI) {
            if (reqURI.matches(str)) {
                log.info("---pass---");
                pass = true;
                break;
            }
        }
        return pass;
    }

    @Override
    public void destroy() {

    }
}
