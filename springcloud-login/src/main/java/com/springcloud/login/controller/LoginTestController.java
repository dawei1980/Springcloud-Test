package com.springcloud.login.controller;

import com.springcloud.login.entity.LoginTest;
import com.springcloud.login.entity.SysUser;
import com.springcloud.login.repository.LoginTestRepository;
import com.springcloud.login.result.JsonObjectResult;
import com.springcloud.login.result.ResultCode;
import com.springcloud.login.utils.JwtUtil;
import com.springcloud.login.utils.UploadFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api")
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

    @PostMapping(value = "/userLogin")
    public Object userLogin(@RequestParam(value = "username") String username,
                            @RequestParam(value = "password") String password){

        if(!"".equals(username) && !"".equals(password)){
            LoginTest loginTest = loginTestRepository.findByUsernameAndPassword(username,password);
            return new JsonObjectResult(ResultCode.SUCCESS, "Success",loginTest);
        }else {
            return new JsonObjectResult(ResultCode.PARAMS_ERROR, "The parameter is wrong");
        }
    }

    /**
     * 登录
     */
    @PostMapping("/authLogin")
    public Object login(@RequestParam(value = "username") String username,
                        @RequestParam(value = "password") String password){

        if(!"".equals(username) && !"".equals(password)){

            //密码是正确的
            //生成jwt令牌,返回到客户端
            Map<String,String> info = new HashMap<>();
            info.put("username",username);
            //基于工具类生成jwt令牌
            String jwt = JwtUtil.createJWT(UUID.randomUUID().toString(), username, null);
            info.put("token",jwt);
            return new JsonObjectResult(ResultCode.SUCCESS, "Success",info);
        }else {
            return new JsonObjectResult(ResultCode.NO_DATA, "No data");
        }
    }

    @PostMapping("/addUser")
    public Object addUser(@RequestParam(value = "username") String username,
                          @RequestParam(value = "password") String password,
                          @RequestParam(value = "header_image") MultipartFile headerImage){

        try {
            //保存图片
            String path = UploadFileUtil.saveImg(headerImage);
            SysUser sysUser = new SysUser();
            sysUser.setUsername(username);
            sysUser.setPassword(password);
            sysUser.setHeaderUrl(path);
            return new JsonObjectResult(ResultCode.SUCCESS, "Success",sysUser);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonObjectResult(ResultCode.PARAMS_ERROR, "The parameter is wrong");
        }
    }
}
