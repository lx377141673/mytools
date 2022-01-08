package xyz.liangxin.utils.constant.date;

/**
 * 季度枚举
 * @author liangxin
 */
public enum QuarterEnum {
    /**
     * 第一季度
     */
    Q1(1),
    /**
     * 第二季度
     */
    Q2(2),
    /**
     * 第三季度
     */
    Q3(3),
    /**
     * 第四季度
     */
    Q4(4);

    /**
     * 季度对应的值
     */
    private final int value;

    /**
     * 构造函数
     *
     * @param value 季度对应的值
     */
    QuarterEnum(int value) {
        this.value = value;
    }

    /**
     * 获得季度中的 值
     *
     * @return 季度中的 值
     */
    public int getValue() {
        return this.value;
    }

    /**
     * 获得季度中的值
     *
     * @return 季度中的 值
     */
    public int value() {
        return getValue();
    }

    /**
     * 将 季度int转换为Season枚举对象<br>
     *
     * @param intValue 季度int表示
     * @return {@link QuarterEnum}
     * @see #Q1
     * @see #Q2
     * @see #Q3
     * @see #Q4
     */
    public static QuarterEnum of(int intValue) {
        switch (intValue) {
            case 1:
                return Q1;
            case 2:
                return Q2;
            case 3:
                return Q3;
            case 4:
                return Q4;
            default:
                return null;
        }
    }
}
