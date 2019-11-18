package com.oy.secondskill.common.entity;

import java.util.HashMap;
import java.util.Map;

public class Result extends HashMap<String,Object> {

    private static final long serialVersionUID = 1L;
    public Result(){
        this.put("code",0);
    }

    public static Result error(){
        return error(500,"找不到，系统问题");
    }
    public static Result error(int code,String msg){
        Result r = new Result();
        r.put("code",code);
        r.put("msg",msg);
        return r;
    }
    public static Result error(String msg){
        return error(500,msg);
    }
    public static Result error(Object msg){
        Result r = new Result();
        r.put("msg",msg);
        return r;
    }
    public static Result ok(Object msg) {
        Result r = new Result();
        r.put("msg", msg);
        return r;
    }


    public static Result ok(Map<String, Object> map) {
        Result r = new Result();
        r.putAll(map);
        return r;
    }

    public static Result ok() {
        return new Result();
    }

    @Override
    public Object put(String key, Object value) {
        return super.put(key, value);
    }
}
