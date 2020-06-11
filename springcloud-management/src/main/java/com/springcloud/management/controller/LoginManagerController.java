package com.springcloud.management.controller;

import com.springcloud.management.entity.LoginManager;
import com.springcloud.management.repository.LoginManagerRepository;
import com.springcloud.management.result.JsonObjectResult;
import com.springcloud.management.result.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/loginManager")
public class LoginManagerController {
    @Autowired
    private LoginManagerRepository loginTestRepository;

    @GetMapping(value = "/getAllLogin")
    public Object getAllLogin(){
        List<LoginManager> loginTestList = loginTestRepository.findAll();

        if(loginTestList.size() != 0){
            return new JsonObjectResult(ResultCode.SUCCESS, "Success",loginTestList);
        }else {
            return new JsonObjectResult(ResultCode.NO_DATA, "No data");
        }

        //localhost:8084/springcloud-login/login/getAllLogin?token=1
    }

    @PostMapping(value = "/userLogin")
    public Object userLogin(@RequestParam(value = "username") String username,
                            @RequestParam(value = "password") String password){

        if(!"".equals(username) && !"".equals(password)){
            LoginManager loginTest = loginTestRepository.findByUsernameAndPassword(username,password);
            return new JsonObjectResult(ResultCode.SUCCESS, "Success",loginTest);
        }else {
            return new JsonObjectResult(ResultCode.PARAMS_ERROR, "The parameter is wrong");
        }
    }

}
