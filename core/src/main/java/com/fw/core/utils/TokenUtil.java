package com.fw.core.utils;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author yqf
 */
@Slf4j
@Data
public class TokenUtil {

    private long expireTime = 2 * 60 * 60 * 1000;
    private String tokenSecret = "token1";


    /**
     * 签名生成
     * @param sign
     * @return token
     */
    public  <T> String sign(T sign) {
        String token = null;
        try {
            Date expireAt = new Date(System.currentTimeMillis() + expireTime);
            token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("store", JSON.toJSONString(sign))
                    .withExpiresAt(expireAt)
                    .sign(Algorithm.HMAC256(tokenSecret));
        } catch (IllegalArgumentException | JWTCreationException je) {

            throw new RuntimeException(je);
        }
        return token;
    }


    public  <T> T  verify(String token,Class<T> tClass){
        try {
            JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256(tokenSecret)).withIssuer("auth0").build();
            DecodedJWT decodedJwt =jwtVerifier.verify(token);
            log.info("认证通过");
            log.info("token:{}",decodedJwt .getClaim("store").asString());
            log.info("过期时间：{}" , decodedJwt .getExpiresAt());
            return JSON.parseObject(decodedJwt .getClaim("store").asString(),tClass);
        } catch (Exception e) {
            log.error("tokenError:{}",e.toString());
            //抛出错误即为验证不通过
            return null;
        }
    }

}

