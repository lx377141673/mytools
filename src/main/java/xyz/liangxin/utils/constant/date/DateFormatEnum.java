package xyz.liangxin.utils.constant.date;

/**
 *  时间格式化枚举列表
 *  @author liangxin
 *  @date 2019/02/18 09:43
 *  @version 1.0
 */
public enum DateFormatEnum {
    /**
     * 时间格式: HH:mm
     */
    HOUR_TO_MINUTE(DateConstant.DATE_PATTERN_HOUR_MINUTE),
    /**
     * 时间格式: HH时mm分
     */
    HOUR_TO_MINUTE_B(DateConstant.DATE_PATTERN_HOUR_MINUTE_B),
    /**
     * 时间格式: HH:mm:ss
     */
    HOUR_TO_SECOND(DateConstant.DATE_PATTERN_HOUR_SECOND),
    /**
     * 时间格式: HH时mm分ss秒
     */
    HOUR_TO_SECOND_B(DateConstant.DATE_PATTERN_HOUR_SECOND_B),
    /**
     * 时间格式: HH:mm:ss.SSS
     */
    HOUR_TO_MILLISECOND(DateConstant.DATE_PATTERN_HOUR_MILLISECOND),
    /**
     * 时间格式: HH时mm分ss秒 SSS毫秒
     */
    HOUR_TO_MILLISECOND_B(DateConstant.DATE_PATTERN_HOUR_MILLISECOND_B),
    /**
     * 时间格式: yyyy-MM-dd
     */
    YEAR_TO_DAY(DateConstant.DATE_PATTERN_YEAR_TO_DAY),
    /**
     * 时间格式: yyyyy年MM月dd
     */
    YEAR_TO_DAY_B(DateConstant.DATE_PATTERN_YEAR_TO_DAY_B),
    /**
     * 时间格式: yyyy/MM/dd
     */
    YEAR_TO_DAY_C(DateConstant.DATE_PATTERN_YEAR_TO_DAY_C),
    /**
     * 时间格式: yyyy.MM.dd
     */
    YEAR_TO_DAY_D(DateConstant.DATE_PATTERN_YEAR_TO_DAY_D),
    /**
     * 时间格式: yyyy-MM-dd HH:mm:ss
     */
    YEAR_TO_SECOND(DateConstant.DATE_PATTERN_YEAR_TO_SECOND),
    /**
     * 时间格式: yyyy年MM月dd日 HH时mm分ss秒
     */
    YEAR_TO_SECOND_B(DateConstant.DATE_PATTERN_YEAR_TO_SECOND_B),
    /**
     * 时间格式: yyyy-MM-dd HH:mm:ss.SSS
     */
    YEAR_TO_MILLISECOND(DateConstant.DATE_PATTERN_YEAR_TO_MILLISECOND),
    /**
     * 时间格式: yyyy年MM月dd日 HH时mm分ss秒 SSS毫秒
     */
    YEAR_TO_MILLISECOND_B(DateConstant.DATE_PATTERN_YEAR_TO_MILLISECOND_B),
    OF("----");


    /**
     * 日期格式化字符串
     */
    private String formatStr;

    DateFormatEnum(String formatStr) {
        this.formatStr = formatStr;
    }

    public String getFormatStr() {
        return this.formatStr;
    }

    public String getValue() {
        return getFormatStr();
    }

    public String value() {
        return getFormatStr();
    }

    private void setFormatStr(String formatStr){
        this.formatStr=formatStr;
    }

    public static DateFormatEnum of(String dateFormat){
        DateFormatEnum dateFormatEnum=OF;
        dateFormatEnum.setFormatStr(dateFormat);
        return dateFormatEnum;
    }

}
