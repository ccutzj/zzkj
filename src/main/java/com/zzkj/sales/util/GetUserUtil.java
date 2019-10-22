package com.zzkj.sales.util;

import com.auth0.jwt.JWT;

public class GetUserUtil {
    public static String getUserName(String token){
        return JWT.decode(token).getAudience().get(0);
    }
}
