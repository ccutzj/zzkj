package com.zzkj.sales.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.zzkj.sales.entity.User;

/**
 * Algorithm.HMAC256():使用HS256生成token,密钥则是用户的密码，唯一密钥的话可以保存在服务端。
 * withAudience()存入需要保存在token的信息，这里我把用户username存入token中
 */
public class Token {

    public static String getToken(User user){
        String token = "";
        token = JWT.create()
                .withAudience(user.getUserName())
                .sign(Algorithm.HMAC256(user.getPassword()));

        return token;
    }
}
