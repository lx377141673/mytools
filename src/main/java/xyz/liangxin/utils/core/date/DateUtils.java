package xyz.liangxin.utils.core.date;


import xyz.liangxin.utils.constant.date.CalendarUnitEnum;
import xyz.liangxin.utils.constant.date.DateConstant;
import xyz.liangxin.utils.constant.date.DateFormatEnum;
import xyz.liangxin.utils.constant.date.DateUnit;
import xyz.liangxin.utils.core.date.local_date.LocalDateTimeUtils;
import xyz.liangxin.utils.core.date.local_date.LocalDateUtils;
import xyz.liangxin.utils.core.date.local_date.LocalTimeUtils;
import xyz.liangxin.utils.core.number.NumberUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

/**
 * 时间 工具类
 *
 * @author liangxin
 * @version V1.0
 * @Package xyz.liangxin.springbootdemo.common.uitls
 * @date 2021/5/7 18:51
 * @Description 时间 工具类
 */
public class DateUtils extends DateConstant {
//################################################################################## 获取时间信息_部分 [开始]####################################################################################################################################################################################################################################################################################
//#############################################################################################################################################################################################################################################################################################################################################################################################


    /**
     * 获取当前时间
     *
     * @return 当前时间 对象
     */
    public static Date now() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * 获取当前时间戳
     *
     * @return 当前时间戳
     */
    public static long nowMillisecond() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间 字符串 格式为 yyyy-MM-dd HH:mm:ss
     *
     * @return 当前时间字符串
     */
    public static String nowStr() {
        return toString(now());
    }

    /**
     * 获取本系统 时区
     *
     * @return 时区对象
     */
    public static ZoneId getZoneId() {
        return getTimeZone().toZoneId();
    }

    /**
     * 获取本系统 时区
     *
     * @return 时区对象
     */
    public static TimeZone getTimeZone() {
        return TimeZone.getDefault();
    }


    /**
     * 获取指定时间里 包含的时间单位信息
     *
     * @param date             时间信息
     * @param calendarUnitEnum 想要获取的时间单位 {@link CalendarUnitEnum}
     *                         <p>{@link CalendarUnitEnum#YEAR}: 年;</p>
     *                         <p>{@link CalendarUnitEnum#MONTH} :月;</p>
     *                         <p>{@link CalendarUnitEnum#WEEK}: 周;</p>
     *                         <p>{@link CalendarUnitEnum#DAY}: 日;</p>
     *                         <p>{@link CalendarUnitEnum#HOUR}: 时;</p>
     *                         <p>{@link CalendarUnitEnum#MINUTE}: 分;</p>
     *                         <p>{@link CalendarUnitEnum#SECOND}: 秒;</p>
     *                         <p>{@link CalendarUnitEnum#MILLISECOND}= 毫秒;</p>
     * @return 指定时间部分信息
     */
    public static int getDateInfo(Date date, CalendarUnitEnum calendarUnitEnum) {
        return toCalendar(date).get(calendarUnitEnum.getValue());
    }


    /**
     * 获取时间信息里面的年 部分
     *
     * @param date 时间对象
     * @return 年份信息
     */
    public static int year(Date date) {
        return getDateInfo(date, CalendarUnitEnum.YEAR);
    }

    /**
     * 获取时间信息里面的月 部分
     *
     * @param date 时间对象
     * @return 月份信息
     */
    public static int month(Date date) {
        return getDateInfo(date, CalendarUnitEnum.MONTH) + 1;
    }

    /**
     * 获取时间信息里面的 周(所在这个月的第几周) 部分
     *
     * @param date 时间对象
     * @return 周 信息
     */
    public static int week(Date date) {
        return getDateInfo(date, CalendarUnitEnum.WEEK);
    }

    /**
     * 获取时间信息里面的 日 部分
     *
     * @param date 时间对象
     * @return 日 信息
     */
    public static int day(Date date) {
        return getDateInfo(date, CalendarUnitEnum.DAY);
    }

    /**
     * 获取时间信息里面的 时 部分 24小时制
     *
     * @param date 时间对象
     * @return 时 信息
     */
    public static int hour(Date date) {
        return getDateInfo(date, CalendarUnitEnum.HOUR);
    }

    /**
     * 获取时间信息里面的 时 部分 12小时制
     *
     * @param date 时间对象
     * @return 时 信息
     */
    public static int hourIs12(Date date) {
        return getDateInfo(date, CalendarUnitEnum.HOUR_12);
    }

    /**
     * 获取时间信息里面的 分 部分
     *
     * @param date 时间对象
     * @return 分信息
     */
    public static int minute(Date date) {
        return getDateInfo(date, CalendarUnitEnum.MINUTE);
    }

    /**
     * 获取时间信息里面的 秒 部分
     *
     * @param date 时间对象
     * @return 秒信息
     */
    public static int second(Date date) {
        return getDateInfo(date, CalendarUnitEnum.SECOND);
    }

    /**
     * 获取时间信息里面的 毫秒 部分
     *
     * @param date 时间对象
     * @return 毫秒信息
     */
    public static int millisecond(Date date) {
        return getDateInfo(date, CalendarUnitEnum.MILLISECOND);
    }

//#################################################################################################获取时间信息_部分 [结束]##############################################################################################################################################################################################################################################################################
//#############################################################################################################################################################################################################################################################################################################################################################################################


//################################################################################## 时间信息处理_部分 [开始]####################################################################################################################################################################################################################################################################################
//#############################################################################################################################################################################################################################################################################################################################################################################################

    /**
     * 获取时间格式化对象
     *
     * @param dateFormat 格式化字符串 yyyy-MM-dd HH:mm:ss
     * @return 时间格式化对象
     */
    public static SimpleDateFormat getDateFormat(String dateFormat) {
        return new SimpleDateFormat(dateFormat);
    }

    /**
     * 获取时间格式化对象
     *
     * @param dateFormat 格式化字符串
     * @return 时间格式化对象
     */
    public static SimpleDateFormat getDateFormat(DateFormatEnum dateFormat) {
        Objects.requireNonNull(dateFormat,"时间格式 不允许为null");
        return new SimpleDateFormat(dateFormat.value());
    }


    /**
     * 将时间字符串 转换成 时间对象  格式 默认为 yyyy-MM-dd HH:mm:ss
     *
     * @param dateStr 时间字符串
     * @return 时间对象
     */
    public static Date of(String dateStr) {
        return of(dateStr, DateConstant.DATE_PATTERN_YEAR_TO_SECOND);
    }

    /**
     * 将时间戳转换成时间对象
     *
     * @param millisecond 时间戳
     * @return 时间对象
     */
    public static Date of(long millisecond) {
        return new Date(millisecond);
    }


    /**
     * 时间字符串转换成 时间对象
     *
     * @param dateStr   时间字符串
     * @param formatStr 时间格式 例: "yyyy-mm-dd HH:MM:DD:SS"
     * @return 时间对象
     */
    public static Date of(String dateStr, String formatStr) {
        return of(dateStr, DateFormatEnum.of(formatStr));
    }

    /**
     * 时间字符串转换成 时间对象
     *
     * @param dateStr    时间字符串
     * @param dateFormat 时间格式枚举值
     * @return 时间对象
     */
    public static Date of(String dateStr, DateFormatEnum dateFormat) {
        Date date = null;
        try {
            date = getDateFormat(dateFormat).parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * 将指定瞬时时间, 转换为 Date 对象
     * @param instant  瞬时时间
     * @return 时间对象
     */
    public static Date of(Instant instant) {
        return Date.from(instant);
    }

    /**
     * 将日历时间对象 转换为 Date 对象
     * @param localDateTime 日历时间对象
     * @return 时间对象
     */
    public static Date of(LocalDateTime localDateTime) {
        return DateUtils.of(LocalDateTimeUtils.toInstant(localDateTime));
    }

    /**
     * 将日历日期对象 转换为 Date 对象
     * @param localDate 日历日期对象
     * @return 时间对象
     */
    public static Date of(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(DateUtils.getZoneId()).toInstant());
    }

    /**
     * 时间对象转换成指定格式字符串
     *
     * @param date      时间对象
     * @param formatStr 格式字符串  例: "yyyy-MM-dd HH:mm:ss"
     * @return 时间字符串
     */
    public static String toString(Date date, String formatStr) {
        return toString(date, DateFormatEnum.of(formatStr));
    }

    /**
     * 时间对象转换成指定格式字符串
     *
     * @param date       时间对象
     * @param dateFormat 时间格式枚举值
     * @return 时间字符串
     */
    public static String toString(Date date, DateFormatEnum dateFormat) {
        return getDateFormat(dateFormat).format(date);
    }

    /**
     * 时间对象转换成指定格式字符串 格式为 yyyy-MM-dd HH:mm:ss
     *
     * @param date 时间对象
     * @return 时间字符串
     */
    public static String toString(Date date) {
        return toString(date, DateConstant.DATE_PATTERN_YEAR_TO_SECOND);
    }


    /**
     * 转换为Calendar对象
     *
     * @param date 日期对象
     * @return Calendar对象
     */
    public static Calendar toCalendar(Date date) {
        return toCalendar(date.getTime());
    }

    /**
     * 转换为Calendar对象
     *
     * @param millis 时间戳
     * @return Calendar对象
     */
    public static Calendar toCalendar(long millis) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        return cal;
    }

    /**
     * 转换为 LocalDateTime 对象
     *
     * @param date 时间对象
     * @return 日期时间对象
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTimeUtils.of(date);
    }

    /**
     * 转换为 LocalDate 对象
     *
     * @param date 时间对象
     * @return 日期对象
     */
    public static LocalDate toLocalDate(Date date) {
        return LocalDateUtils.of(date);
    }

    /**
     * 转换为 LocalTime 对象
     *
     * @param date 时间对象
     * @return 时间信息对象
     */
    public static LocalTime toLocalTime(Date date) {
        return LocalTimeUtils.of(date);
    }

    /**
     * 转换为 Instant 对象
     *
     * @param date 时间对象
     * @return 瞬时时间
     */
    public static Instant toInstant(Date date) {
        return date.toInstant();
    }


    /**
     * 重新格式化时间信息
     *
     * @param date              时间信息
     * @param newDateFormatEnum 新的时间格式
     * @return 时间对象
     */
    public static Date formatDate(Date date, DateFormatEnum newDateFormatEnum) throws ParseException {
        DateFormat dateFormat = getDateFormat(newDateFormatEnum);
        return dateFormat.parse(dateFormat.format(date));
    }


//################################################################################## 时间信息处理_部分 [结束]####################################################################################################################################################################################################################################################################################
//#############################################################################################################################################################################################################################################################################################################################################################################################


//#################################################################################################时间信息运算处理_部分 [开始]##############################################################################################################################################################################################################################################################################
//#############################################################################################################################################################################################################################################################################################################################################################################################


    /**
     * 两个时间 比大小 时间越往后越大 两数相等也等于 false
     * 时间1 是否在 时间2 之后
     * @param date1 时间对象1
     * @param date2 时间对象2
     * @return  时间1 是否在 时间2 之后
     */
    public static boolean isAfter(Date date1, Date date2) {
        // date1.before(date2); // date1 是否在 date2 之前 返回 true || false  相等也返回 false
        // date1.after(date2); // date1 是否在date2之后,返回 true || false     相等也返回 false
        // date1.compareTo(date2); // date1 是否大于 date2   date1小于date2返回-1，date1大于date2返回1，相等返回0
        // date1.compareTo(date2); // date1 是否在 date2 之后  date1 在date2之前返回 -1 date1在date2之后返回1 date1=date2 返回 0
        return date1.after(date2);
    }



    //    相差天数 和 相隔天数的区别
    //    只要两个日期在同一天，那相隔天数就是0，不在同一天，相隔天数就不是0。比如：
    //    2019-01-30 23:59:59 和 2019-01-31 00:00:00
    //    相差天数为0，但相隔天数为1

    /**
     * 获取两个时间的相差时间
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param dateUnit  {@link DateUnit}
     *                  <p>
     *                  {@link DateUnit#MS}: 毫秒
     *                  {@link DateUnit#SECOND}: 秒
     *                  {@link DateUnit#MINUTE}: 分钟
     *                  {@link DateUnit#HOUR}: 小时
     *                  {@link DateUnit#DAY}: 天数
     *                  {@link DateUnit#WEEK}: 星期
     *                  </p>
     * @return 相差的时间 (保留两位小数)
     */
    public static double getBetweenTime(Date startDate, Date endDate, DateUnit dateUnit) {
        double result;
        double betweenTime = isAfter(endDate, startDate) ? endDate.getTime() - startDate.getTime() + 0.0D : startDate.getTime() - endDate.getTime() + 0.0D;
        result = NumberUtils.divide(betweenTime, dateUnit.getMillis());
        return result;
    }


    /**
     * 获取两个时间的相差天数
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 返回两个时间直接相差的天数
     */
    public static int getDaysDifference(Date startDate, Date endDate) {
        return (int) getBetweenTime(startDate, endDate, DateUnit.DAY);
    }


    /**
     * 获取两个时间的相隔天数
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 两个时间的相隔天数
     */
    public static int getDaysApart(Date startDate, Date endDate) throws ParseException {
        startDate = formatDate(startDate, DateFormatEnum.YEAR_TO_DAY);
        endDate = formatDate(endDate, DateFormatEnum.YEAR_TO_DAY);
        return getDaysDifference(startDate, endDate);
    }


    /**
     * 获取指定时间 某段时间之后 的时间
     *
     * @param date             待区间的时间
     * @param nextNum          推迟的时间数
     * @param calendarUnitEnum 推迟的时间类型
     *                         <p>CALENDAR_YEAR: 年;</p>
     *                         <p>CALENDAR_MONTH :月;</p>
     *                         <p>CALENDAR_WEEK: 周;</p>
     *                         <p>CALENDAR_DAY: 日;</p>
     *                         <p>CALENDAR_HOUR: 时;</p>
     *                         <p>CALENDAR_MINUTE: 分;</p>
     *                         <p>CALENDAR_SECOND: 秒;</p>
     *                         <p>CALENDAR_MILLISECOND= 毫秒;</p>
     * @return 指定时间 某段时间之后 的时间
     */
    public static Date getNextTime(Date date, int nextNum, CalendarUnitEnum calendarUnitEnum) {
        Calendar calendar = toCalendar(date);
        calendar.add(calendarUnitEnum.getValue(), nextNum);
        return calendar.getTime();
    }

    /**
     * 获取指定时间 某段时间之前 的时间
     *
     * @param date             待区间的时间
     * @param nextNum          推前的时间数
     * @param calendarUnitEnum 推前的时间类型
     *                         <p>CALENDAR_YEAR: 年;</p>
     *                         <p>CALENDAR_MONTH :月;</p>
     *                         <p>CALENDAR_WEEK: 周;</p>
     *                         <p>CALENDAR_DAY: 日;</p>
     *                         <p>CALENDAR_HOUR: 时;</p>
     *                         <p>CALENDAR_MINUTE: 分;</p>
     *                         <p>CALENDAR_SECOND: 秒;</p>
     *                         <p>CALENDAR_MILLISECOND= 毫秒;</p>
     * @return 指定时间 某段时间之前 的时间
     */
    public static Date getPreviousTime(Date date, int nextNum, CalendarUnitEnum calendarUnitEnum) {
        return getNextTime(date, -nextNum, calendarUnitEnum);
    }


}
