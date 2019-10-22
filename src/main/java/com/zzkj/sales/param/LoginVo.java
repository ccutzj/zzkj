package com.zzkj.sales.param;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginVo {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

//    public LoginVo(){}
//    public LoginVo(String username, String password){
//        this.username = username;
//        this.password = password;
//    }
}
