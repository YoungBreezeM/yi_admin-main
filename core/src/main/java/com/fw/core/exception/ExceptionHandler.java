package com.fw.core.exception;

import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yqf
 */
@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler({MethodArgumentNotValidException.class})

    public Map<String, Object> methodDtoNotValidException(Exception ex, HttpServletRequest request) {

        MethodArgumentNotValidException c = (MethodArgumentNotValidException) ex;

        List<ObjectError> errors = c.getBindingResult().getAllErrors();

        StringBuffer errorMsg = new StringBuffer();

        errors.stream().forEach(x -> {

            errorMsg.append(x.getDefaultMessage()).append(";");

        });

        Map<String, Object> respMap = new HashMap<>(4);
        respMap.put("code", -1);
        respMap.put("msg", errorMsg);

        return respMap;
    }

}

