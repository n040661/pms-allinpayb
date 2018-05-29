package com.dominator.utils.api;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominFramework.core.utils.JsonUtils;
import com.dominFramework.core.utils.SystemUtils;
import com.dominator.enums.ReqEnums;
import com.dominator.utils.dao.RedisUtil;
import com.dominator.utils.encode.MD5;
import com.dominator.utils.exception.ApiException;
import com.dominator.utils.system.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * api 工具类
 *
 * @author dl
 */
public class ApiUtils {

    //single
    private static RedisUtil ru = RedisUtil.getRu();

    private static final Logger log = LoggerFactory.getLogger(ApiUtils.class);

    /**
     * 获取流参数
     * @param request
     * @return
     */
    public static Dto getParams(HttpServletRequest request) throws IOException{
        Dto dto = new HashDto();
        BufferedReader reader = request.getReader();
        String input = null;
        StringBuffer requestBody = new StringBuffer();
        while((input = reader.readLine()) != null) {
            requestBody.append(input);
        }
        if (SystemUtils.isNotEmpty(requestBody.toString()))
            dto = JsonUtils.toDto(requestBody.toString());
        return dto;
    }

    /**
     * 根据用户名获取token
     *
     * @param userName
     * @return
     */
    public static String getTokenByName(String userName, String type) {

        //type
        // c端用户0
        // 园区用户1
        // 企业用户2
        // 其他3

        if (SystemUtils.isEmpty(userName)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateStr = sdf.format(new Date());
        String enc = MD5.GetMD5Code(String.format("%s_%s", userName, dateStr));
        String result = ru.setex(type + userName, enc, Constants.TOKEN_TIMES_INT);
        System.out.println("token放redis" + result);
        return enc;
    }


    /**
     * 获取token
     *
     * @param tokenDto type   c端用户0,园区用户1,企业用户2,其他3
     *                 userName
     *                 userId
     *                 propertyId
     *                 gardenId
     *                 companyId
     *                 roleId
     *                 time
     * @return
     */
    public static String getToken(Dto tokenDto) {
        Long time = System.currentTimeMillis();
        String userName = tokenDto.getString("userName");
        String token = MD5.GetMD5Code(userName + time);
        log.info("token:{}", token);
        tokenDto.put("time", time);
        String result = ru.setObjectEx(token, tokenDto, Constants.TOKEN_TIMES_INT);
        log.info("token放redis:{}", result);
        return token;
    }

    /**
     * 修改密码后，更新token
     * @param dto token
     * @return
     */
    public static String resetToken(Dto dto) {
        String token = dto.getString("token");
        Dto tokenDto = (Dto) ru.getObject(token);
        ru.del(token);
        Long time = System.currentTimeMillis();
        String userName = tokenDto.getString("userName");
        token = MD5.GetMD5Code(userName + time);
        tokenDto.put("time", time);
        log.info("token:{}", token);
        String result = ru.setObjectEx(token, tokenDto, Constants.TOKEN_TIMES_INT);
        log.info("tokenDto:{}", ru.getObject(token));
        log.info("token放redis:{}", result);
        return token;
    }

    /**
     * 更新token内容
     *
     * @param token property_id
     *              garden_id
     *              company_id
     *              role_id
     * @return
     */
    public static String updateToken(String token, Dto dto) {
        Dto tokenDto = (Dto) ru.getObject(token);

        String propertyId = dto.getString("propertyId");
        String gardenId = dto.getString("gardenId");
        String companyId = dto.getString("companyId");
        String roleId = dto.getString("roleId");
        tokenDto.put("propertyId", propertyId);
        tokenDto.put("gardenId", gardenId);
        tokenDto.put("companyId", companyId);
        tokenDto.put("roleId", roleId);
        log.info("tokenDto:{}", tokenDto);
        String result = ru.setObjectEx(token, tokenDto, Constants.TOKEN_TIMES_INT);
        log.info("token放redis:{}", result);

        return token;
    }

    /**
     * @param token
     * @return
     */
    public static boolean checkToken(String token) throws ApiException {

        if (!Constants.TOKEN_CHECK) {
            return true;
        }
        Dto tokenDto = (Dto) ru.getObject(token);
        log.info("tokenDto:{}", tokenDto);
        if (SystemUtils.isEmpty(tokenDto)) {
            //throw new ApiException("200", "用户未登录");
            return false;
        }
        String userName = tokenDto.getString("userName");
        String time = tokenDto.getString("time");
        String redisToken = MD5.GetMD5Code(userName + time);
        if (SystemUtils.isEmpty(redisToken) || !redisToken.equals(token)) {
            throw new ApiException(Constants.REQ_TOKEN_FALSE, "请重新登录");
        }
        String res = ru.setObjectEx(token, tokenDto, Constants.TOKEN_TIMES_INT);
        if (!res.equalsIgnoreCase("ok")) {
            log.error("checkToken error-res:{}", res);
            throw new ApiException(Constants.REQ_TOKEN_FALSE, "请重新登录");
        }
        return true;
    }


    /**
     * 检查token
     *
     * @param userName
     * @param token
     * @throws ApiException
     */
    public static boolean checkToken(String userName, String token, String type) throws ApiException {
        //type
        // c端用户0
        // 园区用户1
        // 企业用户2
        // 其他3
        boolean res = false;

        try {
            if (!Constants.TOKEN_CHECK) {
                res = true;
            } else {
                if (SystemUtils.isEmpty(userName) || SystemUtils.isEmpty(token)) {
                    throw new ApiException(Constants.REQ_TOKEN_FALSE, "请重新登录");
                }
                String redisToken = ru.get(type + userName);
                if (SystemUtils.isEmpty(redisToken) || !redisToken.equals(token)) {
                    throw new ApiException(Constants.REQ_TOKEN_FALSE, "请重新登录");
                }
                // 如果redis上有给加时间
                log.info("redis上面有token");
                ru.setex(type + userName, token, Constants.TOKEN_TIMES_INT);
                res = true;
            }
        } catch (Exception e) {
            throw new ApiException(Constants.REQ_TOKEN_FALSE, "请重新登录");
        } finally {
            return res;
        }
    }

    /**
     * 检查传过来的token和redis中token是否一样
     *
     * @param userName
     * @param token
     * @param type
     * @return
     */
    public static boolean checkTokenIsNotEquals(String userName, String token, String type) {
        //type
        // c端用户0
        // 园区用户1
        // 企业用户2
        // 其他3
        boolean res = false;
        String redisToken = ru.get(type + userName);
        if (SystemUtils.isEmpty(token)) {
            return res;
        }
        if (SystemUtils.isEmpty(redisToken) || !redisToken.equals(token)) {
            res = true;
        }
        return res;
    }

    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,6,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /**
     * 校验名字合法
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isName(String str) {
        return str.matches("^[\u4e00-\u9fa5]{1,20}?$");

    }


    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    //设置x个随机组成的数
    public static String CreateRandom(int x) {
        //随机数
        Random r = new Random();
        String R = "";
        for (int i = 0; i < x; i++) {
            R = R + r.nextInt(10);
        }
        return R;
    }


//    public static boolean checkFixTime(ServletResponse response) {
//        String fixTime = SysConfig.;
//        try {
//            response.setContentType("text/json; charset=utf-8");
//            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//            Date tmBegin = sdf.parse(fixTime.split("-")[0]);
//            Date tmEnd = sdf.parse(fixTime.split("-")[1]);
//            Date dt = new Date();
//            Date now = sdf.parse(String.format("%s:%s", dt.getHours(), dt.getMinutes()));
//            if (now.getTime() >= tmBegin.getTime() && now.getTime() < tmEnd.getTime()) {
//                response.getWriter().print("{\"code\":\"401\",\"message\":\"系统维护中,暂停服务\"}");
//                return false;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            log.error(String.format("维护时间配置错误：%s。\n%s", fixTime, e.getMessage()));
//            e.printStackTrace();
//        }
//        return true;
//    }

    public static void checkParam(Dto dto, String params) throws ApiException {
        if (SystemUtils.isEmpty(params)) {
            throw new ApiException(ReqEnums.REQ_PARAM_ERROR.getCode(), "参数为空," + params);
        }
        String[] paramArr = params.split(",");
        for (int i = 0; i < paramArr.length; i++) {
            if (SystemUtils.isEmpty(dto.getString(paramArr[i]))) {
                throw new ApiException(ReqEnums.REQ_PARAM_ERROR.getCode(), "参数(" + paramArr[i] + ")为空," + params);
            }
        }
    }

}
