package xyz.liangxin.utils.constant.date;

/**
 * 日期时间单位，每个单位都是以毫秒为基数
 *
 * @author liangxin
 */
public enum DateUnit {
    /**
     * 一毫秒
     */
    MS(1),
    /**
     * 一秒的毫秒数 1000
     */
    SECOND(MS.getMillis() * 1000),
    /**
     * 一分钟的毫秒数 60000
     */
    MINUTE(SECOND.getMillis() * 60),
    /**
     * 一小时的毫秒数 3600000
     */
    HOUR(MINUTE.getMillis() * 60),
    /**
     * 一天的毫秒数  86400000
     */
    DAY(HOUR.getMillis() * 24),
    /**
     * 一周的毫秒数  604800000
     */
    WEEK(DAY.getMillis() * 7);

    /**
     * 单位对应的毫秒值
     */
    private final long millis;

    DateUnit(long millis) {
        this.millis = millis;
    }

    /**
     * @return 单位对应的毫秒数
     */
    public long getMillis() {
        return this.millis;
    }

    /**
     * @return 单位对应的毫秒数
     */
    public long getValue() {
        return getMillis();
    }

    /**
     * @return 单位对应的毫秒数
     */
    public long value() {
        return getMillis();
    }
}