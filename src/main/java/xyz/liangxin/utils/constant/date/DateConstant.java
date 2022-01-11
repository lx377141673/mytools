package xyz.liangxin.utils.constant.date;

import xyz.liangxin.utils.constant.text.CharSequenceConstant;

/**
 * 时间工具类常量
 *
 * @author liangxin
 * @version 1.0
 * @date 2019/2/13 13:19
 */
public class DateConstant {

    //----------------------------------------------时间格式 -----------------------------
    /**
     * 年
     */
    private static final String YEAR = "yyyy";
    /**
     * 月
     */
    private static final String MONTH = "MM";
    /**
     * 日
     */
    private static final String DAY = "dd";
    /**
     * 时 默认24小时制
     */
    private static final String HOUR = "HH";

    /**
     * 时 12小时制
     */
    private static final String HOUR_12 = "hh";
    /**
     * 时 24小时制
     */
    private static final String HOUR_24 = HOUR;

    /**
     * 分
     */
    private static final String MINUTE = "mm";
    /**
     * 秒
     */
    private static final String SECOND = "ss";
    /**
     * 毫秒
     */
    private static final String MILLISECOND = "SSS";

    /**
     * 标准日期时间格式:
     * yyyy-MM-dd;
     */
    public static final String DATE_PATTERN_YEAR_TO_DAY = YEAR + CharSequenceConstant.STR_DASHED + MONTH + CharSequenceConstant.STR_DASHED + DAY;
    /**
     * 标准日期时间格式:
     * yyyy年MM月dd;
     */
    public static final String DATE_PATTERN_YEAR_TO_DAY_B = YEAR + "年" + MONTH + "月" + DAY + "日";
    /**
     * 标准日期时间格式:
     * yyyy/MM/dd;
     */
    public static final String DATE_PATTERN_YEAR_TO_DAY_C = YEAR + CharSequenceConstant.STR_SLASH + MONTH + CharSequenceConstant.STR_SLASH + DAY;
    /**
     * 标准日期时间格式:
     * yyyy.MM.dd;
     */
    public static final String DATE_PATTERN_YEAR_TO_DAY_D = YEAR + CharSequenceConstant.STR_DOT + MONTH + CharSequenceConstant.STR_DOT + DAY;

    /**
     * 标准日期时间格式:
     * HH:mm
     */
    public static final String DATE_PATTERN_HOUR_MINUTE = HOUR + CharSequenceConstant.STR_COLON + MINUTE;
    /**
     * 标准日期时间格式:
     * HH时mm分
     */
    public static final String DATE_PATTERN_HOUR_MINUTE_B = HOUR + "时" + MINUTE + "分";
    /**
     * 标准日期时间格式:
     * HH:mm:ss
     */
    public static final String DATE_PATTERN_HOUR_SECOND = DATE_PATTERN_HOUR_MINUTE + CharSequenceConstant.STR_COLON + SECOND;
    /**
     * 标准日期时间格式:
     * HH时mm分ss秒
     */
    public static final String DATE_PATTERN_HOUR_SECOND_B = DATE_PATTERN_HOUR_MINUTE_B + SECOND + "秒";
    /**
     * 标准日期时间格式:
     * HH:mm:ss.SSS
     */
    public static final String DATE_PATTERN_HOUR_MILLISECOND = DATE_PATTERN_HOUR_SECOND + CharSequenceConstant.STR_DOT + MILLISECOND;
    /**
     * 标准日期时间格式:
     * HH时mm分ss秒 SSS毫秒
     */
    public static final String DATE_PATTERN_HOUR_MILLISECOND_B = DATE_PATTERN_HOUR_SECOND_B + " " + MILLISECOND + "毫秒";

    /**
     * 标准日期时间格式:
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String DATE_PATTERN_YEAR_TO_SECOND = DATE_PATTERN_YEAR_TO_DAY + " " + DATE_PATTERN_HOUR_SECOND;
    /**
     * 标准日期时间格式:
     * yyyy年MM月dd日 HH时mm分ss秒
     */
    public static final String DATE_PATTERN_YEAR_TO_SECOND_B = DATE_PATTERN_YEAR_TO_DAY_B + " " + DATE_PATTERN_HOUR_SECOND_B;

    /**
     * 标准日期时间格式:
     * yyyy-MM-dd HH:mm:ss.SSS
     */
    public static final String DATE_PATTERN_YEAR_TO_MILLISECOND = DATE_PATTERN_YEAR_TO_SECOND + CharSequenceConstant.STR_DOT + MILLISECOND;
    /**
     * 标准日期时间格式:
     * yyyy年MM月dd日 HH时mm分ss秒 SSS毫秒
     */
    public static final String DATE_PATTERN_YEAR_TO_MILLISECOND_B = DATE_PATTERN_YEAR_TO_SECOND_B + " " + MILLISECOND + "毫秒";


//--------------------------------------- 时间比例---------------------------
    /**
     * 纳秒与毫秒的比例 : 十亿/1000= 1000000
     * 1纳秒等于 百万分之一毫秒
     */
    public static final int MS_TO_NANO = 0xF4240;

    /**
     * 毫秒与秒的比例: 1000
     * 1毫秒等于 千分之一秒
     */
    public static final int SECOND_TO_MS = 0x3E8;
    /**
     * 纳秒与秒的比例 : 十亿=1000000000
     * 1纳秒等于 十亿分之一秒
     */
    public static final int SECOND_TO_NANO = 0x3B9ACA00;
    /**
     * 分钟与秒的比例 : 60
     * 1秒 等于 六十分之一 分钟
     */
    public static final int MINUTE_TO_SECOND = 60;
    /**
     * 分钟与毫秒的比例 : 60*1000 = 60000
     * 1毫秒 等于 六万分之一 分钟
     */
    public static final int MINUTE_TO_MS = 0xEA60;
    /**
     * 时与分钟的比例 : 60
     * 1分钟 等于 六十分之一 小时
     */
    public static final int HOUR_TO_MINUTE = MINUTE_TO_SECOND;

    /**
     * 时与秒的比例 : 60*60 = 3600
     * 1秒 等于 三千六百分之一 小时
     */
    public static final int HOUR_TO_SECOND = 0xE10;

    /**
     * 时与毫秒的比例 : 60*60*1000 = 3600000
     * 1毫秒 等于 三百六十万分之一 小时
     */
    public static final int HOUR_TO_MS = 0x36EE80;

    /**
     * 天与小时的比例 : 24
     * 1小时 等于 二十四分之一 天
     */
    public static final int DAY_TO_HOUR = 24;
    /**
     * 天与分钟的比例 : 24*60 = 1440
     * 1分钟 等于 一千四百四十分之一 天
     */
    public static final int DAY_TO_MINUTE = 0x5A0;

    /**
     * 天与秒的比例 : 24*60*60 = 86400
     * 1秒 等于 八万六千四百分之一 天
     */
    public static final int DAY_TO_SECOND = 0x15180;

    /**
     * 天与毫秒的比例 : 24*60*60*1000 = 86400000
     * 1毫秒 等于 八千六百四十万分之一 天
     */
    public static final int DAY_TO_MS = 0x5265C00;

    /**
     * 周 与 天的比例 : 7
     */
    public static final int WEEK_TO_DAY = 7;
    /**
     * 年 与 月的比例 : 12
     */
    public static final int YEAR_TO_MONTH = 12;

}
