package xyz.liangxin.utils.constant.date;

import java.util.Calendar;

/**
 * 月份枚举<br>
 * 与Calendar中的月份int值对应
 *
 * @see Calendar#JANUARY
 * @see Calendar#FEBRUARY
 * @see Calendar#MARCH
 * @see Calendar#APRIL
 * @see Calendar#MAY
 * @see Calendar#JUNE
 * @see Calendar#JULY
 * @see Calendar#AUGUST
 * @see Calendar#SEPTEMBER
 * @see Calendar#OCTOBER
 * @see Calendar#NOVEMBER
 * @see Calendar#DECEMBER
 * @see Calendar#UNDECIMBER
 *
 * @author liangxin
 *
 */
public enum MonthEnum {
    /**
     * 一月
     */
    JANUARY(Calendar.JANUARY),
    /**
     * 二月
     */
    FEBRUARY(Calendar.FEBRUARY),
    /**
     * 三月
     */
    MARCH(Calendar.MARCH),
    /**
     * 四月
     */
    APRIL(Calendar.APRIL),
    /**
     * 五月
     */
    MAY(Calendar.MAY),
    /**
     * 六月
     */
    JUNE(Calendar.JUNE),
    /**
     * 七月
     */
    JULY(Calendar.JULY),
    /**
     * 八月
     */
    AUGUST(Calendar.AUGUST),
    /**
     * 九月
     */
    SEPTEMBER(Calendar.SEPTEMBER),
    /**
     * 十月
     */
    OCTOBER(Calendar.OCTOBER),
    /**
     * 十一月
     */
    NOVEMBER(Calendar.NOVEMBER),
    /**
     * 十二月
     */
    DECEMBER(Calendar.DECEMBER),
    /**
     * 十三月，仅用于农历
     */
    UNDECIMBER(Calendar.UNDECIMBER);

    /** 月份对应{@link Calendar} 中的Month值*/
    private final int value;

    /**
     * 设置 月份对应{@link Calendar} 中的Month值
     * @param value 月份对应{@link Calendar} 中的Month值
     */
    MonthEnum(int value) {
        this.value = value;
    }

    /**
     * 获取月份对应{@link Calendar} 中的Month值
     * @return 月份对应{@link Calendar} 中的Month值
     */
    public int getValue() {
        return this.value;
    }

    /**
     * 获得月份对应{@link Calendar} 中的Month值
     *
     * @return 月份对应{@link Calendar} 中的Month值
     */
    public int value(){
        return getValue();
    }

    /**
     * 将 {@link Calendar}月份相关值转换为Month枚举对象<br>
     *
     * @param calendarMonthIntValue Calendar中关于Month的int值
     * @return {@link MonthEnum}
     * @see Calendar#JANUARY
     * @see Calendar#FEBRUARY
     * @see Calendar#MARCH
     * @see Calendar#APRIL
     * @see Calendar#MAY
     * @see Calendar#JUNE
     * @see Calendar#JULY
     * @see Calendar#AUGUST
     * @see Calendar#SEPTEMBER
     * @see Calendar#OCTOBER
     * @see Calendar#NOVEMBER
     * @see Calendar#DECEMBER
     * @see Calendar#UNDECIMBER
     */
    public static MonthEnum of(int calendarMonthIntValue) {
        switch (calendarMonthIntValue) {
            case Calendar.JANUARY:
                return JANUARY;
            case Calendar.FEBRUARY:
                return FEBRUARY;
            case Calendar.MARCH:
                return MARCH;
            case Calendar.APRIL:
                return APRIL;
            case Calendar.MAY:
                return MAY;
            case Calendar.JUNE:
                return JUNE;
            case Calendar.JULY:
                return JULY;
            case Calendar.AUGUST:
                return AUGUST;
            case Calendar.SEPTEMBER:
                return SEPTEMBER;
            case Calendar.OCTOBER:
                return OCTOBER;
            case Calendar.NOVEMBER:
                return NOVEMBER;
            case Calendar.DECEMBER:
                return DECEMBER;
            case Calendar.UNDECIMBER:
                return UNDECIMBER;
            default:
                return null;
        }
    }
}
