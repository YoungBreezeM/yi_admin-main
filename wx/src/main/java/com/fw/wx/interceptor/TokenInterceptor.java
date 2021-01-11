package com.fw.wx.interceptor;



import com.alibaba.fastjson.JSON;
import com.fw.core.domain.Result;
import com.fw.core.domain.ResultType;
import com.fw.core.utils.TokenUtil;
import com.fw.wx.domain.WxClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author yqf
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenUtil tokenUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //跨域请求会首先发一个option请求，直接返回正常状态并通过拦截器
        String options = "OPTIONS";
        if (options.equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");


        String token = request.getHeader("API-Authorization");
        if (token != null) {
            try {
                WxClient verify = tokenUtil.verify(token, WxClient.class);
                System.out.println(verify);
                if (verify != null) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        try {
            Result result = new Result(ResultType.TOKEN_VERIFY_FAIL);
            String userJson = JSON.toJSONString(result);
            OutputStream out = response.getOutputStream();
            out.write(userJson.getBytes(StandardCharsets.UTF_8));
            out.flush();
            System.out.println("认证失败，未通过拦截器");
        } catch (Exception e) {
            return false;
        }

        return false;
    }
}
