package com.mxch.imgreconsturct.util;

public class RedisConstants {
    // 验证码关键词
    public static final String LOGIN_CODE_KEY = "login:code:";
    // token关键词
    public static final String TOKEN_USER_KEY = "user:token:";
    // 房间关键词
    public static final String ROOM_COUNT_KEY = "room:count:";
    // 用户锁关键词
    public static final String USER_LOCK_KEY = "user:lock:";
    // 验证码存储时间
    public static final Long LOGIN_CODE_TTL = 5L;

    // token过期时间
    public static final Long TOKEN_USER_TTL = 24L;

}
