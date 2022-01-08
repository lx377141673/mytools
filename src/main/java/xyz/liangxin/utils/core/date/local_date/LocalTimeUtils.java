package xyz.liangxin.utils.core.date.local_date;


import xyz.liangxin.utils.constant.date.DateFormatEnum;
import xyz.liangxin.utils.core.date.DateUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * 时间信息对象 工具类
 *
 * @author liangxin
 * @version 1.0
 * @date 2019/2/18 10:32
 */
public class LocalTimeUtils {

    /**
     * 获取当前时间信息对象
     *
     * @return 时间信息对象
     */
    public static LocalTime now() {
        return LocalTime.now();
    }

    /**
     * Date --> LocalTime
     *
     * @param date 时间对象
     * @return 时间信息对象
     */
    public static LocalTime of(Date date) {
        return LocalDateTimeUtils.of(date).toLocalTime();
    }

    /**
     * LocalDateTime --> LocalTime, 即获取 日期时间信息中的 时间信息部分
     *
     * @param localDateTime 日期时间对象
     * @return 时间信息对象
     */
    public static LocalTime of(LocalDateTime localDateTime) {
        return LocalDateTimeUtils.toLocalTime(localDateTime);
    }

    /**
     * 时间信息字符串, 转换为 时间信息对象, 默认格式为 HH:mm:ss (24小时制)
     *
     * @param time 时间信息字符串
     * @return 时间信息对象
     */
    public static LocalTime of(String time) {
        return LocalTimeUtils.of(time, DateFormatEnum.HOUR_TO_SECOND);
    }

    /**
     * 时间信息字符串, 转换为 时间信息对象,
     *
     * @param time           时间信息字符串
     * @param dateFormatEnum 时间格式枚举
     * @return 时间信息对象
     */
    public static LocalTime of(String time, DateFormatEnum dateFormatEnum) {
        return LocalTime.parse(time, LocalDateTimeUtils.getFormatters(dateFormatEnum));
    }

    /**
     * 时间信息字符串, 转换为 时间信息对象,
     *
     * @param time      时间信息字符串
     * @param formatter 时间信息格式
     * @return 时间信息对象
     */
    public static LocalTime of(String time, String formatter) {
        return LocalTime.parse(time, LocalDateTimeUtils.getFormatters(DateFormatEnum.of(formatter)));
    }


    /**
     * 根据各部分时间信息, 创建时间信息对象
     *
     * @param hour   时
     * @param minute 分
     * @param second 秒
     * @param ms     毫秒
     * @return 时间信息对象
     */
    public static LocalTime of(int hour, int minute, int second, int ms) {
        // 00:00:00
        return LocalTime.of(hour, minute, second, ms);

    }

    /**
     * 根据各部分时间信息, 创建时间信息对象
     *
     * @param hour   时
     * @param minute 分
     * @param second 秒
     * @return 时间信息对象
     */
    public static LocalTime of(int hour, int minute, int second) {
        // 00:00:00
        return LocalTimeUtils.of(hour, minute, second, 0);

    }


    /**
     * LocalTime --> Date
     *
     * @param localTime 时间信息对象
     * @return 时间对象
     */
    public static Date toDate(LocalTime localTime) {
        return DateUtils.of(LocalDateTimeUtils.toInstant(LocalDateTimeUtils.of(localTime)));
    }


}
