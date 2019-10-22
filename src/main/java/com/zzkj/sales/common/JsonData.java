package com.zzkj.sales.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class JsonData {
    Integer  code;
    private String msg;
    private Object data;
    private String token;
    private Integer pageTotal;

    public JsonData(Integer code){
        this.code = code;
    }
    public static JsonData success(Object object, String msg){
        JsonData jsonData = new JsonData(200);
        jsonData.data = object;
        jsonData.msg = msg;
        return jsonData;
    }
    public static JsonData success(Object object, String msg, String token){
        JsonData jsonData = new JsonData(200);
        jsonData.data = object;
        jsonData.msg = msg;
        jsonData.token = token;
        return jsonData;
    }
    //test ResponseEntity responseEntity;
    public static JsonData success(Object object, String msg, Integer pageTotal){
        JsonData jsonData = new JsonData(200);
        jsonData.data = object;
        jsonData.msg = msg;
        jsonData.pageTotal = pageTotal;
        return jsonData;
    }
    public static JsonData success(Object object){
        JsonData jsonData = new JsonData(200);
        jsonData.data = object;
        return jsonData;
    }
    public static JsonData fail(String msg){
        JsonData jsonData = new JsonData(400);
        jsonData.msg = msg;
        return jsonData;
    }
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("code", code);
        result.put("msg", msg);
        result.put("data", data);
        return result;
    }
}
