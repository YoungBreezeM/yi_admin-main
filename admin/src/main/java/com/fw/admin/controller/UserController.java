package com.fw.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fw.core.service.UserService;
import com.fw.core.domain.Result;
import com.fw.core.domain.ResultType;
import com.fw.core.entity.User;
import com.fw.core.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author yqf
 * @since 2020-11-07
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private TokenUtil tokenUtil;

    @PostMapping("/login")
    public ResponseEntity<Result> login(@RequestBody User user) {
        String token = userService.login(user);

        if (token != null) {
            Map<String, Object> rs = new HashMap<>(10);
            rs.put("token", token);
            return new ResponseEntity<>(new Result(ResultType.SUCCESS, rs), HttpStatus.OK);
        }

        return new ResponseEntity<>(new Result(ResultType.TOKEN_VERIFY_FAIL), HttpStatus.OK);
    }


    @GetMapping("/info")
    public ResponseEntity<Result> userInfo(HttpServletRequest request) {
        String token = request.getHeader("X-Token");

        if (token != null) {

            User verify = tokenUtil.verify(token, User.class);
            if (verify != null) {
                QueryWrapper<User> userQueryWrapper = new QueryWrapper<>(verify);
                User one = userService.getOne(userQueryWrapper);
                if (one != null) {
                    Map<String, Object> rs = new HashMap<>(10);
                    List<String> roles = new ArrayList<>();

                    roles.add("admin");
                    rs.put("username", one.getUsername());
                    rs.put("roles", roles);

                    return new ResponseEntity<>(new Result(ResultType.SUCCESS, rs), HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(new Result(ResultType.TOKEN_VERIFY_FAIL), HttpStatus.OK);
        }

        return new ResponseEntity<>(new Result(ResultType.FAIL), HttpStatus.OK);
    }
}

