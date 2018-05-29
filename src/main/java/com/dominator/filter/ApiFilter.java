package com.dominator.filter;

import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.utils.JsonUtils;
import com.dominator.utils.api.ApiUtils;
import com.dominator.utils.dao.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author gsh
 */
@Slf4j
@WebFilter(filterName = "apiFilter",
        urlPatterns = {"/api/v2/*"}
        /*initParams = {
                @WebInitParam(name = "passURI", value = "login,sms,weixin,uploadpics,yeel,picture,homepage,repair/detail,visitor/detail,garden/gardenList")*/
        //}
)
@Order(3)
public class ApiFilter implements Filter {

    private String[] passURI;

    private static RedisUtil ru = RedisUtil.getRu();

   /* *//**
     * 忽略url
     *
     * @param
     * @return
     *//*
    private static  boolean ignoreUrl(String url) {
        // 验证规则
        String regEx = ".*(login|sms|weixin|uploadpics|yeel|picture|homepage|v1|repair/detail|visitor/detail|garden/gardenList).*";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regEx);
        // 忽略大小写的写法
        Matcher matcher = pattern.matcher(url);

        System.out.printf(String.valueOf(matcher.matches()));
        // 字符串是否与正则表达式相匹配
        if(matcher.matches()  ){
            return  true;
        }
        return false;
    }*/

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;


        //解决跨域问题
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        //这里填写你允许进行跨域的主机ip
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        //允许的访问方法set
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, PATCH");
        //Access-Control-Max-Age 用于 CORS 相关配置的缓存
        httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        try {
            req.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String reqUrl = req.getRequestURI();
        if (ignoreUrl(reqUrl)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        Dto dto = JsonUtils.toDto(req.getParameter("paramStr"));
        if (dto==null){
            return ;
        }
        log.info("uri[{}]:{}?paramStr={}", req.getMethod(), reqUrl, dto);
        //checktoken
        String token = dto.getString("token");

        if (ApiUtils.checkToken(token)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        try {
            System.out.printf("{\"code\":\"103\",\"message\":\"token非法\"}");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 忽略url
     *
     * @param url
     * @return
     */
    private static  boolean ignoreUrl(String url) {
        // 验证规则
        String regEx = ".*(login|sms|weixin|uploadpics|yeel|picture|homepage|v1|repair/detail|visitor/detail|garden/gardenList).*";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regEx);
        // 忽略大小写的写法
        Matcher matcher = pattern.matcher(url);

        System.out.printf(String.valueOf(matcher.matches()));
        // 字符串是否与正则表达式相匹配
        if(matcher.matches()  ){
            return  true;
        }
        return false;
    }

    @Override
    public void destroy() {

    }
}
