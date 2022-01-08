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


}
