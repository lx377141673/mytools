package xyz.liangxin.utils.core.date.local_date;


import xyz.liangxin.utils.constant.date.DateConstant;
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
        return LocalTime.of(hour, minute, second, ms * DateConstant.MS_TO_NANO);
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
        return LocalTime.of(hour, minute, second);

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

    /**
     * 获取 当前时间所在天的 纳秒总数
     *
     * @param time 时间
     * @return 当天的 纳秒总数
     */
    public static long toNanoOfDay(LocalTime time) {
        return time.toNanoOfDay();
    }

    /**
     * 获取 当前时间所在天的 毫秒总数
     *
     * @param time 时间
     * @return 当天的 毫秒总数
     */
    public static int toMsOfDay(LocalTime time) {
        return Math.toIntExact(toNanoOfDay(time) / DateConstant.MS_TO_NANO);
    }

    /**
     * 获取 当前时间所在天的 秒总数
     *
     * @param time 时间
     * @return 当天的 秒总数
     */
    public static int toSecondOfDay(LocalTime time) {
        return time.toSecondOfDay();
    }

    /**
     * 获取 当前时间所在天的 分钟总数
     *
     * @param time 时间
     * @return 当天的 分钟总数
     */
    public static int toMinuteOfDay(LocalTime time) {
        return time.toSecondOfDay() / DateConstant.MINUTE_TO_SECOND;
    }

    /**
     * 获取 当前时间所在天的 小时总数
     *
     * @param time 时间
     * @return 当天的 小时总数
     */
    public static int toHourOfDay(LocalTime time) {
        return time.getHour();
    }

    /**
     * 获取时间的毫秒部分, 与 获取时间的 纳秒部分有冲突,
     * 不可同时使用, 或者需要处理后, 使用
     *
     * @param time 时间
     * @return 毫秒部分
     */
    public static int getMs(LocalTime time) {
        return time.getNano() / DateConstant.MS_TO_NANO;
    }


    /**
     * 获取两个时间中, 更靠后的时间
     *
     * @param time1 时间1
     * @param time2 时间2
     * @return 更靠后的时间
     */
    public static LocalTime after(LocalTime time1, LocalTime time2) {
        return time1.isAfter(time2) ? time1 : time2;
    }

    /**
     * 获取两个时间中, 更靠前的时间
     *
     * @param time1 时间1
     * @param time2 时间2
     * @return 更靠前的时间
     */
    public static LocalTime before(LocalTime time1, LocalTime time2) {
        return time1.isBefore(time2) ? time1 : time2;
    }

    //----------------------------------- 计算时间差 ---------------------------

    /**
     * 获取两个时间 相差的 纳秒数
     *
     * @param time1 时间1
     * @param time2 时间2
     * @return 相差的 纳秒数
     */
    public static long getNanoDifference(LocalTime time1, LocalTime time2) {
        return after(time1, time2).toNanoOfDay() - before(time1, time2).toNanoOfDay();
    }

    /**
     * 获取两个时间 相差的 毫秒数
     *
     * @param time1 时间1
     * @param time2 时间2
     * @return 相差的 毫秒数
     */
    public static int getMsDifference(LocalTime time1, LocalTime time2) {
        return Math.toIntExact(getNanoDifference(time1, time2) / DateConstant.MS_TO_NANO);
    }

    /**
     * 获取两个时间 相差的 秒数
     *
     * @param time1 时间1
     * @param time2 时间2
     * @return 相差的 秒数
     */
    public static int getSecondDifference(LocalTime time1, LocalTime time2) {
        return getMsDifference(time1, time2) / DateConstant.SECOND_TO_MS;
    }

    /**
     * 获取两个时间 相差的 分钟数
     *
     * @param time1 时间1
     * @param time2 时间2
     * @return 相差的 分钟数
     */
    public static int getMinuteDifference(LocalTime time1, LocalTime time2) {
        return getSecondDifference(time1, time2) / DateConstant.MINUTE_TO_SECOND;
    }


    /**
     * 获取两个时间 相差的 小时数
     *
     * @param time1 时间1
     * @param time2 时间2
     * @return 相差的 小时数
     */
    public static int getHourDifference(LocalTime time1, LocalTime time2) {
        return getMinuteDifference(time1, time2) / DateConstant.HOUR_TO_MINUTE;
    }

    /**
     * 判断当前是否是 整点 0分0秒
     *
     * @param time 时间
     * @return 当前整点数, 如果不是返回 -1;
     */
    public static int isChimeHourlyChime(LocalTime time) {
        if (time.getMinute() == 0 && time.getSecond() == 0) {
            return time.getHour();
        }
        return -1;
    }
}
