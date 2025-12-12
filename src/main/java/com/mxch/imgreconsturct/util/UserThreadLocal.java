package com.mxch.imgreconsturct.util;

public class UserThreadLocal {
    // 存储用户数据的threadlocal对象
    private static final ThreadLocal<String> USER_INFORMATION = new ThreadLocal<>();

    public static void setUserId(String userId) {
        USER_INFORMATION.set(userId);
    }

    public static String getUserId() {
        return USER_INFORMATION.get();
    }

    public static void clear() {
        USER_INFORMATION.remove();
    }
}
