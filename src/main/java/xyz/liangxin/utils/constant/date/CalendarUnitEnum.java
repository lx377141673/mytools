package xyz.liangxin.utils.constant.date;

import java.util.Calendar;

/**
 * 日期各个部分的枚举[年_月_日...]<br>
 * 与Calendar相应值对应
 *
 * @author Looly
 */
public enum CalendarUnitEnum {
    /**
     * 时间单位: 年
     */
    YEAR(Calendar.YEAR),
    /**
     * 时间单位: 月
     */
    MONTH(Calendar.MONTH),
    /**
     * 时间单位: 周/星期
     */
    WEEK(Calendar.WEDNESDAY),
    /**
     * 时间单位: 日
     */
    DAY(Calendar.DATE),
    /**
     * 时间单位: 时 24小时
     */
    HOUR(Calendar.HOUR_OF_DAY),
    /**
     * 时间单位: 分
     */
    MINUTE(Calendar.MINUTE),
    /**
     * 时间单位: 秒
     */
    SECOND(Calendar.SECOND),
    /**
     * 时间单位: 毫秒
     */
    MILLISECOND(Calendar.MILLISECOND),


    /**
     * 一年中第几周
     *
     * @see Calendar#WEEK_OF_YEAR
     */
    WEEK_OF_YEAR(Calendar.WEEK_OF_YEAR),
    /**
     * 一月中第几周
     *
     * @see Calendar#WEEK_OF_MONTH
     */
    WEEK_OF_MONTH(Calendar.WEEK_OF_MONTH),
    /**
     * 一月中的第几天
     *
     * @see Calendar#DAY_OF_MONTH
     */
    DAY_OF_MONTH(Calendar.DAY_OF_MONTH),
    /**
     * 一年中的第几天
     *
     * @see Calendar#DAY_OF_YEAR
     */
    DAY_OF_YEAR(Calendar.DAY_OF_YEAR),
    /**
     * 周几，1表示周日，2表示周一
     *
     * @see Calendar#DAY_OF_WEEK
     */
    DAY_OF_WEEK(Calendar.DAY_OF_WEEK),
    /**
     * 天所在的周是这个月的第几周
     *
     * @see Calendar#DAY_OF_WEEK_IN_MONTH
     */
    DAY_OF_WEEK_IN_MONTH(Calendar.DAY_OF_WEEK_IN_MONTH),
    /**
     * 上午或者下午 0:AM  1:PM
     *
     * @see Calendar#AM_PM
     */
    AM_PM(Calendar.AM_PM),
    /**
     * 小时，用于12小时制
     *
     * @see Calendar#HOUR
     */
    HOUR_12(Calendar.HOUR),
    /**
     * 小时，用于24小时制
     *
     * @see Calendar#HOUR
     */
    HOUR_24(HOUR.getValue()),
    /**
     * 此枚举用于创建新的枚举值,不能直接使用 需配合 of 方法使用
     */
    OF(-1);


    /**
     * 日历单位枚举值
     */
    private int dateUnit;

    CalendarUnitEnum(int dateUnit) {
        this.dateUnit = dateUnit;
    }

    /**
     * 获取日历单位枚举值
     *
     * @return 日历单位枚举值
     */
    public int getValue() {
        return dateUnit;
    }
    private void setDateUnit(int dateUnit){
        this.dateUnit=dateUnit;
    }

    /**
     * 获取日历单位枚举值
     *
     * @return 日历单位枚举值
     */
    public int value() {
        return getValue();
    }

    public static CalendarUnitEnum of(int calendarDateIntValue){
        CalendarUnitEnum calendarUnitEnum=OF;
        calendarUnitEnum.setDateUnit(calendarDateIntValue);
        return calendarUnitEnum;
    }
}
