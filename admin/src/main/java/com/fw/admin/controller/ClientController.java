package com.fw.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fw.core.domain.Result;
import com.fw.core.domain.ResultType;
import com.fw.core.entity.Client;
import com.fw.core.entity.User;
import com.fw.core.service.ClientService;
import com.fw.core.service.ClientService;
import com.fw.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yqf
 * @since 2020-11-07
 */
@RestController
@RequestMapping("/client")
public class ClientController {
    
    @Autowired
    private ClientService clientService;
    @Autowired
    private UserService userService;
    @GetMapping
    public ResponseEntity<Result> clientList(){

        List<Client> list = clientService.list();

        return new ResponseEntity<>(new Result(ResultType.SUCCESS,list), HttpStatus.OK);
    }

    @GetMapping("/page/{startNum}/{pageSize}")
    public ResponseEntity<Result> clientListPage(@PathVariable Integer startNum, @PathVariable Integer pageSize){

        IPage<Client> page = new Page<>(startNum, pageSize);
        QueryWrapper<Client> objectQueryWrapper = new QueryWrapper<>();
        System.out.println(startNum+":"+pageSize);

        IPage<Client> page1 = clientService.page(page, objectQueryWrapper);

        Map<String,Object> rs = new HashMap<>(10);
        rs.put("list",page1.getRecords());
        rs.put("total",page1.getTotal());
        rs.put("size",page1.getSize());

        return new ResponseEntity<>(new Result(ResultType.SUCCESS,rs), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<Result> updateOneClient(@RequestBody Client client){
        System.out.println(client);
        if (client!=null){
            boolean update = clientService.updateById(client);

            if(update){
                return new ResponseEntity<>(new Result(ResultType.SUCCESS),HttpStatus.OK);
            }
            return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
        }
        return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Result> addClient(@RequestBody Client client){
        if (client!=null){
            boolean save = clientService.save(client);
            if(save){
                return new ResponseEntity<>(new Result(ResultType.SUCCESS),HttpStatus.OK);
            }
            return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
        }

        return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Result> deleteClient(@PathVariable Integer id, @RequestBody User user){
        System.out.println(user);
        System.out.println(id);
        String login = userService.login(user);

        if(login!=null){
            boolean b = clientService.removeById(id);
            if(b){
                return new ResponseEntity<>(new Result(ResultType.SUCCESS),HttpStatus.OK);
            }
        }else {
            return new ResponseEntity<>(new Result(ResultType.LOGIN_FAIL),HttpStatus.OK);
        }

        return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
    }
}

