package xyz.liangxin.utils.core.date.local_date;

import xyz.liangxin.utils.constant.date.DateFormatEnum;
import xyz.liangxin.utils.core.date.DateUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * 日期对象 工具类
 *
 * @author liangxin
 * @version 1.0
 * @date 2019/2/18 10:32
 */
public class LocalDateUtils {


    /**
     * 获取当前 日期信息
     * @return 日期对象
     */
    public static LocalDate now() {
        return LocalDate.now();
    }

    /**
     * Date --> LocalDate
     *
     * @param date 时间对象
     * @return 日期对象
     */
    public static LocalDate of(Date date) {
        return LocalDateTimeUtils.of(date).toLocalDate();
    }

    /**
     * 日期时间对象 转换成 日期对象
     *
     * @param localDateTime 日期时间对象
     * @return 日期对象
     */
    public static LocalDate of(LocalDateTime localDateTime) {
        return LocalDateTimeUtils.toLocalDate(localDateTime);
    }

    /**
     * 时间字符串, 转换成 日期对象, 格式为  yyyy-MM-dd
     * <p>
     * yyyy-MM-dd  2019-01-01
     * 严格按照yyyy-MM-dd验证，01写成1都不行，
     * </p>
     *
     * @param date 时间字符串
     * @return 日期对象
     */
    public static LocalDate of(String date) {
        return LocalDate.parse(date);
    }

    /**
     * 时间字符串, 转换成日期对象
     *
     * @param date              时间字符串
     * @param dateTimeFormatter 时间格式对象模型
     * @return 日期对象
     */
    public static LocalDate of(String date, DateTimeFormatter dateTimeFormatter) {
        return LocalDate.parse(date, dateTimeFormatter);
    }

    /**
     * @param date           时间字符串
     * @param dateFormatEnum 时间格式枚举
     * @return 日期对象
     */
    public static LocalDate of(String date, DateFormatEnum dateFormatEnum) {
        return LocalDate.parse(date, LocalDateTimeUtils.getFormatters(dateFormatEnum));
    }

    /**
     * @param date      时间字符串
     * @param formatter 时间格式字符串
     * @return 日期对象
     */
    public static LocalDate of(String date, String formatter) {
        return LocalDate.parse(date, LocalDateTimeUtils.getFormatters(DateFormatEnum.of(formatter)));
    }

    /**
     * 获取 LocalDate对象
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return 日期对象
     */
    public static LocalDate getLocalDate(int year, int month, int day) {
        return LocalDate.of(year, month, day);
    }


    /**
     * LocalDate --> Date
     *
     * @param localDate 日期对象
     * @return 时间对象
     */
    public static Date toDate(LocalDate localDate) {
        return DateUtils.of(LocalDateTimeUtils.of(localDate));
    }


    /**
     * 格式化成字符串信息
     *
     * @param localDate 日期对象
     * @param dateFormatEnum 日期格式枚举
     * @return 日期信息字符串
     */
    public static String toString(LocalDate localDate, DateFormatEnum dateFormatEnum) {
        return localDate.format(LocalDateTimeUtils.getFormatters(dateFormatEnum));
    }

    /**
     * 格式化成字符串信息
     *
     * @param localDate 日期对象
     * @param formatter 日期格式字符串
     * @return 日期信息字符串
     */
    public static String toString(LocalDate localDate, String formatter) {
        return toString(localDate, DateFormatEnum.of(formatter));
    }

    /**
     * 格式化成字符串信息
     *
     * @param localDate 日期对象
     * @return 日期信息字符串
     */
    public static String toString(LocalDate localDate) {
        return toString(localDate, DateFormatEnum.YEAR_TO_DAY);
    }


    /**
     * 年
     *
     * @param localDate 日期对象
     * @return 年信息
     */
    public static int year(LocalDate localDate) {
        return localDate.getDayOfYear();
    }

    /**
     * 月
     *
     * @param localDate 日期对象
     * @return 月信息
     */
    public static int month(LocalDate localDate) {
        return localDate.getDayOfMonth();
    }

    /**
     * 日
     *
     * @param localDate 日期对象
     * @return 日信息
     */
    public static int day(LocalDate localDate) {
        return localDate.getDayOfMonth();
    }

    /**
     * 周几
     *
     * @param localDate 日期对象
     * @return 周几
     */
    public static int weekDay(LocalDate localDate) {
        return localDate.getDayOfWeek().getValue();
    }

    /**
     * 获取指定试卷中, 指定时间信息
     *
     * @param localDate        日期对象
     * @param temporalAdjuster 时间调节器
     *                         <p>
     *                         {@link TemporalAdjusters#firstDayOfMonth()}: 一个月的第一天
     *                         {@link TemporalAdjusters#firstDayOfNextMonth()} ()}: 下个月的第一天
     *                         {@link TemporalAdjusters#firstDayOfNextYear()}: 下一年第一天的新日期
     *                         {@link TemporalAdjusters#firstDayOfYear()}: 当年第一天
     *                         {@link TemporalAdjusters#lastDayOfMonth()}: 当前月最后一天
     *                         {@link TemporalAdjusters#lastDayOfYear()}: 当年最后一天
     *                         ....
     *                         </p>
     * @return 日期对象
     */
    public static LocalDate getInfo(LocalDate localDate, TemporalAdjuster temporalAdjuster) {
        return localDate.with(temporalAdjuster);
    }

    /**
     * 获取指定日期中, 1号的 日期信息
     *
     * @param localDate 日期对象
     * @return 日期对象
     */
    public static LocalDate getFirstDay(LocalDate localDate) {
        return getInfo(localDate, TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * 获取指定日期中, 月最后一天的信息
     *
     * @param localDate 日期对象
     * @return 日期对象
     */
    public static LocalDate getLastDay(LocalDate localDate) {
        return getInfo(localDate, TemporalAdjusters.lastDayOfMonth());
    }


}
