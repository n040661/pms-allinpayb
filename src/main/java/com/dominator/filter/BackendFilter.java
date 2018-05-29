package com.dominator.filter;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominFramework.core.utils.JsonUtils;
import com.dominFramework.core.utils.SystemUtils;
import com.dominator.enums.ReqEnums;
import com.dominator.utils.api.ApiUtils;
import com.dominator.utils.dao.RedisUtil;
import com.dominator.utils.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * 不需要过滤的地址的关键词写在passURI，逗号隔开
 */
@Slf4j
@Order(2)
@WebFilter(filterName = "backendFilter",
        urlPatterns = {"/backend/v2/*"},
        initParams = {
                @WebInitParam(name = "passURI", value = ".*test.*,.*login.*,.*checkUserName$")
        }
)
public class BackendFilter implements Filter {

    private String[] passURI;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        passURI = filterConfig.getInitParameter("passURI").split(",");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        servletRequest.setCharacterEncoding("utf-8");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String reqURI = request.getRequestURI();
        log.info("BackendFilter");
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        try {
            req.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        log.info("content-type:{}", req.getHeader("content-type"));
        Dto dto = JsonUtils.toDto(servletRequest.getParameter("paramStr"));
        if (dto == null) {
            log.info("uri[{}]:{}", req.getMethod(), reqURI);
            throw new ApiException(ReqEnums.REQ_PARAM_ERROR);
        } else {
            log.info("uri[{}]:{}?paramStr={}", req.getMethod(), reqURI, dto);
        }
        boolean pass = isPass(reqURI);
        if (pass) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        String token = dto.getString("token");
        if (ApiUtils.checkToken(token)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        try {
            servletResponse.getWriter().print("{\"code\":\"103\",\"message\":\"token非法\"}");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            servletResponse.getWriter().close();
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
