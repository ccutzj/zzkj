package com.zzkj.sales.controller;

import com.zzkj.sales.common.JsonData;
import com.zzkj.sales.entity.User;
import com.zzkj.sales.exception.ParamException;
import com.zzkj.sales.param.LoginVo;
import com.zzkj.sales.service.UserService;
import com.zzkj.sales.util.BeanValidator;
import com.zzkj.sales.util.HttpServletRequestUtil;
import com.zzkj.sales.util.JsonMapper;
import com.zzkj.sales.util.Token;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    @ResponseBody
    public JsonData login(HttpServletRequest request, HttpServletResponse response, @RequestBody(required = false) String json){
        log.info(json);

        if(json == null || json.trim().equals("")){
            throw new ParamException("未接收到请求参数");
        }
        //解析客户端发来的username+password的json
        LoginVo loginVo = JsonMapper.string2Obj(json, LoginVo.class);
        if(loginVo == null){
            throw new ParamException("请求参数不正确");
        }
        //校验用户名和密码是否为空,如果为空返回提示
        BeanValidator.check(loginVo);
        String username = loginVo.getUsername();
        String password = loginVo.getPassword();
//        password = DigestUtils.md5Hex(password);//MD5加密
        log.info("用户名：" + username + "--------->密码:" + password);

        User user = userService.queryByUsername(username);
        if (user != null){
            if(password.equals(user.getPassword())){
                return JsonData.success(user, "登录成功", Token.getToken(user));
            }else{
                log.info("密码不正确:" + password + "--------->数据库：" + user.getPassword());
                return JsonData.fail("用户名或密码错误");
            }
        }
        return JsonData.fail("用户名或密码错误");
    }
}
