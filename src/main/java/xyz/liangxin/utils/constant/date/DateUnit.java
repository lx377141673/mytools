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
    SECOND( 1000),
    /**
     * 一分钟的毫秒数 60 * 1000 = 60000
     */
    MINUTE(60000),
    /**
     * 一小时的毫秒数 60 * 1000 * 60 = 3600000
     */
    HOUR(3600000),
    /**
     * 一天的毫秒数  60 * 1000 * 60 * 24 = 86400000
     */
    DAY(86400000),
    /**
     * 一周的毫秒数  60 * 1000 * 60 * 24 * 7 = 604800000
     */
    WEEK(604800000);

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