package com.springcloud.login.controller;

import com.springcloud.login.entity.LoginTest;
import com.springcloud.login.reponsitory.LoginTestRepository;
import com.springcloud.login.result.JsonObjectResult;
import com.springcloud.login.result.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/login")
public class LoginTestController {
    @Autowired
    private LoginTestRepository loginTestRepository;

    @GetMapping(value = "/getAllLogin")
    public Object getAllLogin(){
        List<LoginTest> loginTestList = loginTestRepository.findAll();

        if(loginTestList.size() != 0){
            return new JsonObjectResult(ResultCode.SUCCESS, "Success",loginTestList);
        }else {
            return new JsonObjectResult(ResultCode.NO_DATA, "No data");
        }
    }
}
