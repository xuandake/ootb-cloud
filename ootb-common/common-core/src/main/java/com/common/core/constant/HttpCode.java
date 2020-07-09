package com.common.core.constant;

/**
 * @ClassName HttpStatus
 * @Description 常用thhp状态码
 * @Author xdk
 * @Date 20-07-08 12:39
 * @Version 1.0
 **/

public class HttpCode {

    /**********************成功****************************/
    /**
     * @Description //成功
     **/
   public static final int OK = 200;

    /**
     * @Description //创建成功
     **/
    public static final int CREATED = 201;

    /**
     * @Description //请求已被接收
     **/
    public static final int ACCEPTED = 202;

    /************************客户端错误码***********************/
    /**
     * @Description //请求参数有误
     **/
    public static final int BAD_REQUEST = 400;

    /**
     * @Description //当前请求用户未被授权
     **/
    public static final int UNAUTHORIZED = 401;

    /**
     * @Description //当前请求被拒绝
     **/
    public static final int FORBIDDEN = 403;

    /**
     * @Description //无法找到请求资源
     **/
    public static final int NOT_FOUND = 404;

    /**
     * @Description //使用无效的HTTP请求类型对请求的URL进行请求
     **/
    public static final int METHOD_NOT_ALLOWED = 405;

    /**
     * @Description //服务器不支持的content type
     **/
    public static final int NOT_ACCEPTABLE = 406;

    /**
     * @Description //请求超时
     **/
    public static final int REQUEST_TIMEOUT = 408;

    /**
     * @Description //请求体太大
     **/
    public static final int REQUEST_ENTITY_TOO_LARGE = 413;

    /**
     * @Description //请求url太长
     **/
    public static final int REQUEST_URL_TOO_LONG = 414;

    /************************服务端错误码***********************/

    /**
     * @Description //服务内部错误
     **/
    public static final int INTERNAL_SERVER_ERROR = 500;

    /**
     * @Description //服务器不支持所需功能
     **/
    public static final int NOT_IMPLEMENTED = 501;

    /**
     * @Description //服务器网关错误
     **/
    public static final int BAD_GATEWAY = 502;

    /**
     * @Description //服务器超载或死机
     **/
    public static final int SERVICE_UNAVAILABLE = 503;

    /**
     * @Description //服务器网关超时
     **/
    public static final int GATEWAY_TIMEOUT= 504;

    /**
     * @Description //服务器不支持服务“http协议”版本
     **/
    public static final int HTTP_VERSION_NOT_SUPPORTED = 505;

   /************************自定义 错误码***********************/

     /**
       * @Description   //成功
      **/
      public static final int SUCCESS = 0;

     /**
      * @Description    //失败
      **/
      public static final int FAIL = 1;

     /**
      * @Description    //服务器启用限流
      **/
     public static final int SERVER_SENTINEL = 5001;

}
