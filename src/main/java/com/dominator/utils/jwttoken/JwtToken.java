package com.dominator.utils.jwttoken;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dominFramework.core.typewrap.Dto;
import com.dominFramework.core.typewrap.impl.HashDto;
import com.dominator.AAAconfig.SysConfig;
import com.dominator.enums.ReqEnums;
import com.dominator.utils.exception.ApiException;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtToken {

    private static String SECRET = "18616386705";

    /**
     * 生成token
     */
    public static String createToken(Map<String, Object> map) throws Exception {
        //签发时间
        Date iatDate = new Date();
        //过期时间
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.DATE, SysConfig.TokenExpiresTime);
        Date expiresDate = nowTime.getTime();

        Map<String, Object> map1 = new HashMap<>();
        map1.put("alg", "HS256");
        map1.put("typ", "JWT");
        JWTCreator.Builder builder = JWT.create()
                .withHeader(map1)
                .withExpiresAt(expiresDate)
                .withIssuedAt(iatDate);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            builder.withClaim(entry.getKey(), String.valueOf(entry.getValue()));
        }
        return builder.sign(Algorithm.HMAC256(SECRET));
    }

    public static Map<String, Claim> verifyToken(String token) throws Exception {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        DecodedJWT jwt;
        try {
            jwt = verifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new ApiException(ReqEnums.REQ_TOKEN_FALSE);
        }
        return jwt.getClaims();
    }

    public static String updateToken(String token, Map<String, Object> map) throws Exception {
        Map<String, Object> map1 = getTokenClaims(token);
        map1.putAll(map);
        return createToken(map1);
    }

    public static String updateToken(String token) throws Exception {
        token = createToken(getTokenClaims(token));
        return token;
    }

    private static Map<String, Object> getTokenClaims(String token) throws Exception {
        DecodedJWT jwt = JWT.decode(token);
        Map<String, Claim> claims = jwt.getClaims();
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, Claim> entry : claims.entrySet()) {
            if (entry.getValue().asString() != null)
                map.put(entry.getKey(), entry.getValue().asString());
        }
        return map;
    }

    public static String getString(String token, String str) throws Exception {
        Map<String, Object> map = JwtToken.getTokenClaims(token);
        return map.get(str) == null ? null : (String) map.get(str);
    }

    public static void main(String[] args) throws Exception {
        Dto dto = new HashDto();
        dto.put("userame", "admin");
        dto.put("password", "password");
        dto.put("propertyId", "propertyId");
        dto.put("gardenId", "gardenId");
        dto.put("companyId", "companyId");
        dto.put("userId", "userId");
        String token = JwtToken.createToken(dto);
        log.info("token:{}", token);
//        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwYXNzd29yZCI6InBhc3N3b3JkIiwidXNlcl9uYW1lIjoiYWRtaW4iLCJleHAiOjE1MjI3NjUyOTgsImlhdCI6MTUyMjc2NDk5OH0.Z44y2vq18rWZ-p-dKnXMfT5x0wTXV5BEMfTTh-x7hVg";


//        Map<String, Claim> claims = JwtToken.verifyToken(token);
//        for (Map.Entry<String, Claim> entry : claims.entrySet()) {
//            log.info("key:{}, value:{}", entry.getKey(), entry.getValue().asString());
//            log.info("key:{}, value:{}", entry.getKey(), entry.getValue().asDate());
//        }
//        token = JwtToken.updateToken(token);
//        log.info("token:{}", token);
    }
}
