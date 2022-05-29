package xyz.liangxin.utils.core.date;


import xyz.liangxin.utils.constant.date.WeekEnum;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

/**
 * 时间对象封装拓展类
 * @author liangxin
 */
public class DateTime extends Date {

    private static final long serialVersionUID = -5395712593979185936L;

    /**
     * /**
     * 是否可变对象
     */
    private final boolean mutable = true;
    /**
     * 一周的第一天，默认是周一， 在设置或获得 WEEK_OF_MONTH 或 WEEK_OF_YEAR 字段时，Calendar 必须确定一个月或一年的第一个星期，以此作为参考点。
     */
    private final WeekEnum firstDayOfWeek = WeekEnum.MONDAY;

    /**
     * 时区
     */
    private TimeZone timeZone;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DateTime)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        DateTime dateTime = (DateTime) o;
        return mutable == dateTime.mutable && firstDayOfWeek == dateTime.firstDayOfWeek && Objects.equals(timeZone, dateTime.timeZone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), mutable, firstDayOfWeek, timeZone);
    }




    /**
     * 给定日期毫秒数的构造
     *
     * @param timeMillis 日期毫秒数
     * @param timeZone   时区
     * @since 1.0
     */
    public DateTime(long timeMillis, TimeZone timeZone) {
        super(timeMillis);
        if (null != timeZone) {
            this.timeZone = timeZone;
        }
    }

    /**
     * 当前时间
     *
     * @param timeZone 时区
     * @since 1.0
     */
    public DateTime(TimeZone timeZone) {
        this(System.currentTimeMillis(), timeZone);
    }


    /**
     * 当前时间
     */
    public DateTime() {
        this(TimeZone.getDefault());
    }

    /**
     * 给定日期的构造
     *
     * @param date 日期
     */
    public DateTime(Date date) {
        this(date.getTime(), TimeZone.getDefault());
    }

    /**
     * 给定日期的构造
     *
     * @param date 日期
     * @param timeZone 时区
     * @since 1.0
     */
    public DateTime(Date date, TimeZone timeZone) {
        this(date.getTime(), timeZone);
    }
    /**
     * 给定日期的构造
     *
     * @param calendar {@link Calendar}
     */
    public DateTime(Calendar calendar) {
        this(calendar.getTime(), null);
    }

    /**
     * 给定日期毫秒数的构造
     *
     * @param timeMillis 日期毫秒数
     * @since 1.0
     */
    public DateTime(long timeMillis) {
        this(timeMillis, null);
    }

    /**
     *  创建当前时间实例
     * @return 当前时间实例
     */
    public static  DateTime now(){
        return new DateTime();
    }


}
