package xyz.liangxin.utils.core.date;

import xyz.liangxin.utils.constant.date.CalendarUnitEnum;
import xyz.liangxin.utils.constant.date.WeekEnum;
import xyz.liangxin.utils.core.ObjectUtils;

import java.util.Calendar;
import java.util.Date;


/**
 * 日期工具类, 周
 *
 * @author liangxin
 * @version 1.0
 * @date 2019/2/15 15:41
 */
public class WeekUtils {
    /**
     * 获取指定时间是一周中的第几天
     * <p>周一为一个星期的第一天</p>
     *
     * @param date 时间
     * @return 一周中的第几天
     */
    public static int getWeekDay(Date date) {
        if (ObjectUtils.isNull(date)) {
            return 0;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return setMondayFist(calendar.get(Calendar.DAY_OF_WEEK));
    }

    /**
     * 获取指定时间偏移指定周数(之后[正数],之前[负数] )后的 指定周天数的 时间对象
     * 例如 获取 2019/1/2  1周之后的 周一
     * getDateWeekOfOffset(new Date(),1,1)
     *
     * @param date        时间
     * @param weekNum     偏移的 周 数
     * @param weekEnumDay 指定 获取偏移后 的 周几
     * @return 指定时间偏移指定周数后的 指定周天数的 时间对象
     * Gets the time after the offset specified week
     */
    public static Date getDateWeekOfOffset(Date date, int weekNum, WeekEnum weekEnumDay) {
        // 获取偏移指定周后的时间
        Date offsetWeekDate = DateUtils.getNextTime(date, weekNum, CalendarUnitEnum.WEEK);
        //处理获取当前日期所在周 的指定 周天数
        return DateUtils.getNextTime(offsetWeekDate, weekEnumDay.getValue() - getWeekDay(offsetWeekDate), CalendarUnitEnum.DAY);
    }

    /**
     * 按中国的习惯一个星期的第一天是星期一 转换成 周日为一个星期的第一天
     *
     * @param weekDay 周天数
     * @return 周日为第一天的周天数
     */
    public static int setSundayFist(int weekDay) {
        return weekDay + 1 >= 8 ? 7 : weekDay + 1;
    }

    /**
     * 将 周日为一个星期的第一天的习惯 转换成 周一为一个星期的第一天
     *
     * @param calendarWeekDay 周天数
     * @return 周一为第一天的周天数
     */
    public static int setMondayFist(int calendarWeekDay) {
        return (calendarWeekDay - 1) <= 0 ? 7 : calendarWeekDay - 1;
    }

    /**
     * 获取指定时间的上周的周一的日期
     *
     * @param date 时间信息
     * @return 上周一的时间信息
     */
    public static Date geLastWeekMonday(Date date) {
        return getDateWeekOfOffset(date, -1, WeekEnum.MONDAY);
    }

    /**
     * 获取指定时间的这周的周一的日期
     *
     * @param date 时间信息
     * @return 这周一的时间信息
     */
    public static Date getThisWeekMonday(Date date) {
        return getDateWeekOfOffset(date, 0, WeekEnum.MONDAY);
    }

    /**
     * 获取指定时间的下周的周一的日期
     *
     * @param date 时间信息
     * @return 下周一的时间信息
     */
    public static Date getNextWeekMonday(Date date) {
        return getDateWeekOfOffset(date, 1, WeekEnum.MONDAY);
    }
}
