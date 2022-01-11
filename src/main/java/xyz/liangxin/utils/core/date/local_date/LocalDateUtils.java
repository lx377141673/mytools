package xyz.liangxin.utils.core.date.local_date;

import xyz.liangxin.utils.constant.date.CalendarUnitEnum;
import xyz.liangxin.utils.constant.date.DateConstant;
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
     *
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
    public static LocalDate of(int year, int month, int day) {
        return LocalDate.of(year, month, day);
    }


    /**
     * LocalDate --> Date
     *
     * @param localDate 日期对象
     * @return 时间对象
     */
    public static Date toDate(LocalDate localDate) {
        return DateUtils.of(localDate);
    }


    /**
     * 格式化成字符串信息
     *
     * @param localDate      日期对象
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
        return localDate.getYear();
    }

    /**
     * 月
     *
     * @param localDate 日期对象
     * @return 月信息
     */
    public static int month(LocalDate localDate) {
        return localDate.getMonthValue();
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
     * 获取指定时间中, 指定时间信息
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
    public static LocalDate getMonthFirstDay(LocalDate localDate) {
        return getInfo(localDate, TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * 获取指定日期中, 月最后一天的信息
     *
     * @param localDate 日期对象
     * @return 日期对象
     */
    public static LocalDate getMonthLastDay(LocalDate localDate) {
        return getInfo(localDate, TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 获取 从 1970 年到 指定时间, 一共有多少天
     *
     * @param date 指定时间
     * @return 从1970 年到 指定时间之间的天数
     */
    public static int getEpocDay(LocalDate date) {
        return Math.toIntExact(date.toEpochDay());
    }

    /**
     * 获取 从 1970/01/01 年到 指定时间, 一共有多少周 (一周七天)
     *
     * @param date 指定时间
     * @return 从 1970/01/01 年到 指定时间之间的 周数
     */
    public static int getEpochWeek(LocalDate date) {
        return Math.toIntExact(date.toEpochDay() / DateConstant.WEEK_TO_DAY);
    }

    /**
     * 获取 从 1970/01/01 年到 指定时间, 一共有多少月数 (一年十二个月)
     * 不记录当前月
     *
     * @param date 指定时间
     * @return 从 1970/01/01 年到 指定时间之间的 月数
     */
    public static int getEpochMonth(LocalDate date) {
        // 不记录当前月
        return getEpochYear(date) * DateConstant.YEAR_TO_MONTH + date.getMonthValue() - 1;
    }

    /**
     * 获取 从 1970 年到 指定时间, 一共有多少年
     * 不记录当前年
     *
     * @param date 指定时间
     * @return 从 1970/01/01 年到 指定时间之间的 年数
     */
    public static int getEpochYear(LocalDate date) {
        // 不记录当前年
        return date.getYear() - 1970 - 1;
    }



    /**
     * 是否是一周第一天 , 周一
     *
     * @param date 时间
     * @return 是否是一周第一天
     */
    public static boolean isWeekStart(LocalDate date) {
        return date.getDayOfWeek().getValue() == 1;
    }

    /**
     * 是否是一周最后一天 , 周末
     *
     * @param date 时间
     * @return 是否是一周最后一天
     */
    public static boolean isWeekEnd(LocalDate date) {
        return date.getDayOfWeek().getValue() == 7;
    }

    /**
     * 是否是月初第一天 1号
     *
     * @param date 时间
     * @return 是否是月初第一天
     */
    public static boolean isMonthStart(LocalDate date) {
        return date.getDayOfMonth() == 1;
    }

    /**
     * 是否是月末最后一天 2月(28号 || 29号) 其他(30号 || 31号)
     *
     * @param date 时间
     * @return 是否是月末最后一天
     */
    public static boolean isMonthEnd(LocalDate date) {
        return getMonthLastDay(date).getDayOfMonth() == date.getDayOfMonth();
    }

    /**
     * 是否是年初第一天 01月01日
     *
     * @param date 时间
     * @return 是否是年初第一天
     */
    public static boolean isYearStart(LocalDate date) {
        return date.getMonthValue() == 1 && isMonthStart(date);
    }

    /**
     * 是否是年末最后一天 12月31日
     *
     * @param date 时间
     * @return 是否是年末最后一天
     */
    public static boolean isYearEnd(LocalDate date) {
        return date.getMonthValue() == 12 && isMonthEnd(date);
    }


    /**
     * 获取两个时间之间, 时间更晚的一个
     *
     * @param date1 时间1
     * @param date2 时间2
     * @return 更晚的一个时间
     */
    public static LocalDate after(LocalDate date1, LocalDate date2) {
        return date1.isAfter(date2) ? date1 : date2;
    }

    /**
     * 获取两个时间之间, 时间更早的一个
     *
     * @param date1 时间1
     * @param date2 时间2
     * @return 更早的一个时间
     */
    public static LocalDate before(LocalDate date1, LocalDate date2) {
        return date1.isBefore(date2) ? date1 : date2;
    }

//--------------------------------- 计算 时间相差 ---------------------------
    /**
     * 获取两个时间之间的 相差数
     *
     * @param start        时间1
     * @param end          时间2
     * @param calendarUnit 计算单位
     *                     <ul>
     *                     <li> 天: {@link CalendarUnitEnum#DAY}</li>
     *                     <li> 周: {@link CalendarUnitEnum#WEEK}</li>
     *                     <li> 月: {@link CalendarUnitEnum#MONTH}</li>
     *                     <li> 年: {@link CalendarUnitEnum#YEAR}</li>
     *                     </ul>
     * @return 两个时间相差的指定单位数
     */
    public static int getLocalDateDifference(LocalDate start, LocalDate end, CalendarUnitEnum calendarUnit) {
        // 处理时间前后顺序问题
        LocalDate date1 = after(start, end);
        LocalDate date2 = before(start, end);
        int difference = 0;
        switch (calendarUnit) {
            case YEAR:
                difference = getMonthDifference(date1, date2) / DateConstant.YEAR_TO_MONTH;
                break;
            case MONTH:
                difference = getEpochMonth(date1) - getEpochMonth(date2)
                        + calculateMonthDeviation(date1, date2, false);
                break;
            case WEEK:
                difference = getDayDifference(date1, date2) / DateConstant.WEEK_TO_DAY;
                break;
            case DAY:
                difference = Math.toIntExact(date1.toEpochDay() - date2.toEpochDay());
                break;
            default:
                break;
        }
        return difference;
    }


    /**
     * 获取两个时间相差的天数
     *
     * @param start 时间1
     * @param end   时间2
     * @return 相差的天数
     */
    public static int getDayDifference(LocalDate start, LocalDate end) {
        return getLocalDateDifference(start, end, CalendarUnitEnum.DAY);
    }


    /**
     * 获取两个时间相差的周数
     *
     * @param start 时间1
     * @param end   时间2
     * @return 相差的周数
     */
    public int getWeekDifference(LocalDate start, LocalDate end) {
        return getLocalDateDifference(start, end, CalendarUnitEnum.WEEK);
    }

    /**
     * 获取两个时间之间相差的月份数
     * <p>
     * 例子:
     * <li> 01/27 ~ 02/27 = 1;</li>
     * <li> 01/28 ~ 02/28 = 1;</li>
     * <li> 01/31 ~ 02/28 = 1;</li>
     * <li> 02/27 ~ 03/27 = 1;</li>
     * <li> 02/28 ~ 03/28 = 1;</li>
     * <li> 02/28 ~ 03/31 = 1;</li>
     * <li> 03/27 ~ 04/27 = 1;</li>
     * <li> 03/29 ~ 04/29 = 1;</li>
     * <li> 03/30 ~ 04/30 = 1;</li>
     * <li> 03/31 ~ 04/30 = 1;</li>
     * <li>2021/01/02 - 2021/02/01 相差 不足一个月 0</li>
     * <li>2021/01/02 - 2021/02/02 相差 一个月 </li>
     * <li>2021/01/31 - 2021/02/28 相差 一个月 </li>
     * </p>
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 两个时间的相隔月数
     */
    public static int getMonthDifference(LocalDate start, LocalDate end) {
        return getLocalDateDifference(start, end, CalendarUnitEnum.MONTH);
    }

    /**
     * 根据 日数 计算 两个时间之间, , 月份数的偏差值
     *
     * <ul>
     *     <li>模式1: 根据 距离月末的时间, 计算是否需要 减1</li>
     *     <li>模式2: 如果 时间1的日数 < 时间1日数, 需要 减1,除非 时间1 是当前月最后一天 </li>
     * </ul>
     * <p>
     *     	模式1:
     * 		<li> 01/27(4) ~ 02/27(1) = 1;</li>
     * 		<li> 01/28(3) ~ 02/28(0) = 1;</li>
     * 		<li> 01/31(0) ~ 02/28(0) = 1;</li>
     * 		<li> 02/27(1) ~ 03/27(4) = 0;</li>
     * 		<li> 02/28(0) ~ 03/28(3) = 0;</li>
     * 		<li> 02/28(0) ~ 03/31(0) = 1;</li>
     * 		<li> 03/27(4) ~ 04/27(3) = 1;</li>
     * 		<li> 03/29(2) ~ 04/29(1) = 1;</li>
     * 		<li> 03/30(1) ~ 04/30(0) = 1;</li>
     * 	    <li> 03/31(0) ~ 04/30(0) = 1;</li>
     * </p>
     * <p>
     *     	模式2:
     * 		<li> 01/27 ~ 02/27 = 1;</li>
     * 		<li> 01/28 ~ 02/28 = 1;</li>
     * 		<li> 01/31 ~ 02/28 = 1;</li>
     * 		<li> 02/27 ~ 03/27 = 1;</li>
     * 		<li> 02/28 ~ 03/28 = 1;</li>
     * 		<li> 02/28 ~ 03/31 = 1;</li>
     * 		<li> 03/27 ~ 04/27 = 1;</li>
     * 		<li> 03/29 ~ 04/29 = 1;</li>
     * 		<li> 03/30 ~ 04/30 = 1;</li>
     * 	    <li> 03/31 ~ 04/30 = 1;</li>
     * </p>
     * <p>
     *     区别:
     *     <li>模式1: 02/27(1) ~ 03/27(4)= 0; 模式2: 02/27 ~ 03/27 = 1; </li>
     *     <li>模式1: 02/28(0) ~ 03/28(3) = 0; 模式2: 02/28 ~ 03/28 = 1;  </li>
     * </p>
     *
     * @param date1 开始时间
     * @param date2 截止时间
     * @param flag  计算模式
     * @return 相差月份数的偏差值
     */
    private static int calculateMonthDeviation(LocalDate date1, LocalDate date2, boolean flag) {
        int num = 0;
        if (flag) {
            // 模式1
            int day1 = getMonthLastDay(date1).getDayOfMonth() - date1.getDayOfMonth();
            int day2 = getMonthLastDay(date2).getDayOfMonth() - date2.getDayOfMonth();
            if (day1 > day2) {
                num--;
            }
            return num;
        }

        // 模式2
        if (date1.getDayOfMonth() < date2.getDayOfMonth()) {
            num--;
            if (isMonthEnd(date1)) {
                num++;
            }
        }
        return num;

    }

    /**
     * 获取两个时间之间相差的年数
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 两个时间的相隔年数
     */
    public static int getYearDifference(LocalDate start, LocalDate end) {
        return getLocalDateDifference(start, end, CalendarUnitEnum.YEAR);
    }


}
