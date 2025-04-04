package com.sinon.bluecommunity.common.utils;

public class ThreadLocalUtil {
    // 使用泛型定义ThreadLocal对象，增强类型安全
    private static final ThreadLocal<Object> THREAD_LOCAL = new ThreadLocal<>();

    // 根据键获取值
    public static <T> T get() {
        return (T) THREAD_LOCAL.get();
    }

    // 存储键值对
    public static void set(Object value) {
        THREAD_LOCAL.set(value);
    }

    // 清除ThreadLocal 防止内存泄漏
    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
