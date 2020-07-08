package com.common.core.result;

import com.common.core.constant.Constant;
import com.common.core.constant.HttpCode;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName Result
 * @Description TODO
 * @Author xdk
 * @Date 20-07-08 14:59
 * @Version 1.0
 **/

@Data
public class Result implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 状态码 */
    private Integer code;

    /** 信息说明 */
    private String msg;

    /** 返回数据 */
    private Object data;

    /** 时间戳 */
    private long timestamp;

    public Result(){
        this.timestamp = System.currentTimeMillis();
    }

    public Result(Integer code, String msg){
        this.code = code;
        this.msg = msg;
        this.timestamp = System.currentTimeMillis();
    }

    public static Result success() {
        Result result = new Result();
        result.setCode(HttpCode.SUCCESS);
        result.setMsg(Constant.SUCCESS);
        return result;
    }

    public static Result success(Object data){
        Result result = new Result();
        result.setCode(HttpCode.SUCCESS);
        result.setMsg(Constant.SUCCESS);
        result.setData(data);
        return result;
    }

    public static Result error(){
        Result result = new Result();
        result.setCode(HttpCode.FAIL);
        result.setMsg(Constant.FAIL);
        return result;
    }

    public static Result error(String msg){
        Result result = new Result();
        result.setCode(HttpCode.FAIL);
        result.setMsg(msg);
        return result;
    }

    public static Result error(Integer code){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(Constant.FAIL);
        return result;
    }

    public static Result error(Object data){
        Result result = new Result();
        result.setCode(HttpCode.FAIL);
        result.setMsg(Constant.FAIL);
        result.setData(data);
        return result;
    }

    public static Result error(Integer code, String msg){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static Result error(Integer code, String msg, Object data){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }
}
