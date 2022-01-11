package xyz.liangxin.utils.core.date.local_date;


import xyz.liangxin.utils.constant.date.DateConstant;
import xyz.liangxin.utils.constant.date.DateFormatEnum;
import xyz.liangxin.utils.core.date.DateUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;


//        从Java 8之后，Java里面添加了许多的新特性，其中一个最常见也是最实用的便是日期处理的类——LocalDate。
//        新增的日期jar主要有三种：
//        java.time.LocalDate  ->只对年月日做出处理
//        java.time.LocalTime  ->只对时分秒纳秒做出处理
//        java.time.LocalDateTime ->同时可以处理年月日和时分秒


//        JDBC
//        最新JDBC映射将把数据库的日期类型和Java 8的新类型关联起来：
//        SQL -> Java
//                --------------------------
//        date -> LocalDate
//        time -> LocalTime
//        timestamp -> LocalDateTime


/**
 * 日期时间对象 工具类
 *
 * @author liangxin
 * @version 1.0
 * @date 2019/2/15 15:54
 */
public class LocalDateTimeUtils {
    /**
     * 获取当前时间 日期时间信息
     *
     * @return 当前时间信息
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }


    /**
     * 将时间信息, 转换为 日期间信息
     *
     * @param date 时间信息
     * @return 日期间信息
     */
    public static LocalDateTime of(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), DateUtils.getZoneId());
    }


    /**
     * 日期信息, 转换成 日期时间, 时间信息 为 当前时间
     *
     * @param localDate 日期信息
     * @return 日期时间
     */
    public static LocalDateTime of(LocalDate localDate) {
        return localDate.atTime(LocalTime.now());
    }


    /**
     * 时间信息, 转换成  日期时间, 日期信息为 当前日期
     *
     * @param localTime 时间信息
     * @return 日期时间
     */
    public static LocalDateTime of(LocalTime localTime) {
        return localTime.atDate(LocalDate.now());
    }


    /**
     * 将时间字符串 转换成 日期时间对象
     *
     * @param dateTime       时间字符串
     * @param dateFormatEnum 时间格式枚举
     * @return 日期时间对象
     */
    public static LocalDateTime of(String dateTime, DateFormatEnum dateFormatEnum) {
        return LocalDateTime.parse(dateTime, getFormatters(dateFormatEnum));
    }

    /**
     * 将时间字符串 转换成 日期时间对象
     *
     * @param dateTime  时间字符串
     * @param formatter 时间格式字符串
     * @return 日期时间对象
     */
    public static LocalDateTime of(String dateTime, String formatter) {
        return LocalDateTime.parse(dateTime, getFormatters(DateFormatEnum.of(formatter)));
    }

    /**
     * 将时间字符串 转换成 日期时间对象, 默认格式为 yyyy-MM-dd HH:mm:ss
     *
     * @param dateTime 时间字符串
     * @return 日期时间对象
     */
    public static LocalDateTime of(String dateTime) {
        return LocalDateTime.parse(dateTime, getFormatters(DateFormatEnum.YEAR_TO_SECOND));
    }

    /**
     * 将日期时间信息, 转成瞬时时间, 用于计算
     *
     * @param localDateTime 日期时间对象
     * @return 瞬时时间对象
     */
    public static Instant toInstant(LocalDateTime localDateTime) {
        return localDateTime.atZone(DateUtils.getZoneId()).toInstant();
    }

    /**
     * 将日期时间转换成 Date 对象
     * <p> 基于{@link DateUtils#of(LocalDateTime)}</p>
     *
     * @param localDateTime 日期时间信息
     * @return 时间对象
     */
    public static Date toDate(LocalDateTime localDateTime) {
        return DateUtils.of(localDateTime);
    }

    /**
     * 将日期时间转换成 日期对象, 即获取 日期部分信息
     *
     * @param localDateTime 日期时间信息
     * @return 日期信息
     */
    public static LocalDate toLocalDate(LocalDateTime localDateTime) {
        return localDateTime.toLocalDate();
    }

    /**
     * 将日期时间转换成 时间信息对象, 即获取 时间部分信息
     *
     * @param localDateTime 日期时间信息
     * @return 时间信息对象
     */
    public static LocalTime toLocalTime(LocalDateTime localDateTime) {
        return localDateTime.toLocalTime();
    }

    /**
     * 获取格式化模型对象
     *
     * @param dateFormatEnum 时间格式类型枚举
     * @return 格式化模型对象
     */
    public static DateTimeFormatter getFormatters(DateFormatEnum dateFormatEnum) {
        return DateTimeFormatter.ofPattern(dateFormatEnum.getValue());
    }

//---------------------------------------- 计算 相差时间 --------------------------
    /**
     * 获取两个时间的相差 秒数
     *
     * @param dateTime1 时间1
     * @param dateTime2 时间2
     * @return 返回两个时间之间相差的 秒数
     */
    public static int getSecondDifference(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        LocalDateTime d1 = after(dateTime1, dateTime2);
        LocalDateTime d2 = before(dateTime1, dateTime2);
        int dayDifference = LocalDateUtils.getDayDifference(d1.toLocalDate(), d2.toLocalDate());
        return dayDifference * DateConstant.DAY_TO_SECOND + d1.toLocalTime().toSecondOfDay() - d2.toLocalTime().toSecondOfDay();
    }

    /**
     * 获取两个时间的相差 分钟数
     *
     * @param dateTime1 时间1
     * @param dateTime2 时间2
     * @return 返回两个时间之间相差的 分钟数
     */
    public static int getMinuteDifference(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return getSecondDifference(dateTime1, dateTime2) / DateConstant.MINUTE_TO_SECOND;
    }

    /**
     * 获取两个时间的相差 小时数
     *
     * @param dateTime1 时间1
     * @param dateTime2 时间2
     * @return 返回两个时间之间相差的 小时数
     */
    public static int getHourDifference(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return getMinuteDifference(dateTime1, dateTime2) / DateConstant.HOUR_TO_MINUTE;
    }

    /**
     * 获取两个时间的相差 天数
     *
     * @param dateTime1 时间1
     * @param dateTime2 时间2
     * @return 返回两个时间之间相差的 天数
     */
    public static int getDayDifference(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return getHourDifference(dateTime1, dateTime2) / DateConstant.DAY_TO_HOUR;
    }

    /**
     * 获取两个时间的相差 周数
     *
     * @param dateTime1 时间1
     * @param dateTime2 时间2
     * @return 返回两个时间之间相差的 周数
     */
    public static int getWeekDifference(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return getDayDifference(dateTime1, dateTime2) / DateConstant.WEEK_TO_DAY;
    }

    /**
     * 获取两个时间的相差月数
     *
     * @param dateTime1 时间1
     * @param dateTime2 时间2
     * @return 返回两个时间之间相差的 月数
     */
    public static int getMonthDifference(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        LocalDateTime d1 = after(dateTime1, dateTime2);
        LocalDateTime d2 = before(dateTime1, dateTime2);
        int monthDifference = LocalDateUtils.getMonthDifference(d1.toLocalDate(), d2.toLocalDate());
        if (d1.toLocalTime().toSecondOfDay() - d2.toLocalTime().toSecondOfDay() < 0) {
            monthDifference--;
        }
        return monthDifference;
    }

    /**
     * 获取两个时间的相差 年数
     *
     * @param dateTime1 时间1
     * @param dateTime2 时间2
     * @return 返回两个时间之间相差的 年数
     */
    public static int getYearDifference(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return getMonthDifference(dateTime1, dateTime2) / DateConstant.YEAR_TO_MONTH;
    }


    /**
     * 获取两个时间中, 更晚的时间
     *
     * @param dateTime1 时间1
     * @param dateTime2 时间2
     * @return 更晚的时间
     */
    public static LocalDateTime after(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.isAfter(dateTime2) ? dateTime1 : dateTime2;
    }

    /**
     * 获取两个时间中,更早的时间
     *
     * @param dateTime1 时间1
     * @param dateTime2 时间2
     * @return 更早的时间
     */
    public static LocalDateTime before(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.isBefore(dateTime2) ? dateTime1 : dateTime2;
    }

}

