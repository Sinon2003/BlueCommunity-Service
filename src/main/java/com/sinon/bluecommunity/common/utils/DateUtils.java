package com.sinon.bluecommunity.common.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日期时间工具类
 */
public class DateUtils {
    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    private static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    private DateUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 获取当前日期时间
     * @return LocalDateTime对象
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    /**
     * 格式化日期时间（默认格式：yyyy-MM-dd HH:mm:ss）
     * @param dateTime LocalDateTime对象
     * @return 格式化后的字符串
     */
    public static String format(LocalDateTime dateTime) {
        return format(dateTime, DEFAULT_DATE_TIME_FORMAT);
    }

    /**
     * 格式化日期时间
     * @param dateTime LocalDateTime对象
     * @param pattern 格式模式
     * @return 格式化后的字符串
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return null;
        }
        return DateTimeFormatter.ofPattern(pattern).format(dateTime);
    }

    /**
     * 解析日期时间字符串（默认格式：yyyy-MM-dd HH:mm:ss）
     * @param dateTimeStr 日期时间字符串
     * @return LocalDateTime对象
     */
    public static LocalDateTime parse(String dateTimeStr) {
        return parse(dateTimeStr, DEFAULT_DATE_TIME_FORMAT);
    }

    /**
     * 解析日期时间字符串
     * @param dateTimeStr 日期时间字符串
     * @param pattern 格式模式
     * @return LocalDateTime对象
     */
    public static LocalDateTime parse(String dateTimeStr, String pattern) {
        if (StringUtils.isEmpty(dateTimeStr)) {
            return null;
        }
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * Date转LocalDateTime
     * @param date Date对象
     * @return LocalDateTime对象
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * LocalDateTime转Date
     * @param dateTime LocalDateTime对象
     * @return Date对象
     */
    public static Date toDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 计算两个日期时间之间的差（毫秒）
     * @param start 开始时间
     * @param end 结束时间
     * @return 时间差（毫秒）
     */
    public static long diffInMillis(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0;
        }
        return toDate(end).getTime() - toDate(start).getTime();
    }

    /**
     * 检查日期是否在指定范围内
     * @param date 待检查的日期
     * @param start 开始日期
     * @param end 结束日期
     * @return 是否在范围内
     */
    public static boolean isBetween(LocalDateTime date, LocalDateTime start, LocalDateTime end) {
        if (date == null || start == null || end == null) {
            return false;
        }
        return !date.isBefore(start) && !date.isAfter(end);
    }

    /**
     * 获取友好的时间显示
     * @param dateTime 日期时间
     * @return 友好的时间显示
     */
    public static String getFriendlyTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }

        LocalDateTime now = now();
        long diff = diffInMillis(dateTime, now);

        if (diff < 1000 * 60) { // 1分钟内
            return "刚刚";
        } else if (diff < 1000 * 60 * 60) { // 1小时内
            return diff / (1000 * 60) + "分钟前";
        } else if (diff < 1000 * 60 * 60 * 24) { // 1天内
            return diff / (1000 * 60 * 60) + "小时前";
        } else if (diff < 1000 * 60 * 60 * 24 * 30) { // 30天内
            return diff / (1000 * 60 * 60 * 24) + "天前";
        } else {
            return format(dateTime, DEFAULT_DATE_FORMAT);
        }
    }
}
