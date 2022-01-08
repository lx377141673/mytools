package xyz.liangxin.utils.core.number;


import java.math.BigDecimal;

/**
 * 数字工具类
 *
 * @author liangxin
 * @version 1.0
 * @date 2019/1/31 15:00
 */
public class NumberUtils extends CalculateUtils {

    /**
     * 将数字转换成 BigDecimal 对象,便于计算
     *
     * @param num 待转换数字字符串
     * @return 数字 BigDecimal 对象
     */
    public static BigDecimal toBigDecimal(String num) {
        return new BigDecimal(num);
    }


    /**
     * 将数字转换成 BigDecimal 对象,便于计算
     *
     * @param num 待转换数字
     * @return 数字 BigDecimal 对象
     */
    public static BigDecimal toBigDecimal(Number num) {
        return toBigDecimal(num.toString());
    }


    /**
     * 将数字转换成 BigDecimal 对象,便于计算
     *
     * @param num 待转换数字
     * @return 数字 BigDecimal 对象
     */
    public static BigDecimal toBigDecimal(double num) {
        return BigDecimal.valueOf(num);
    }

    /**
     * 将数字转换成 BigDecimal 对象,便于计算
     *
     * @param num 待转换数字
     * @return 数字 BigDecimal 对象
     */
    public static BigDecimal toBigDecimal(float num) {
        return BigDecimal.valueOf(num);
    }

    /**
     * 将数字转换成 BigDecimal 对象,便于计算
     *
     * @param num 待转换数字
     * @return 数字 BigDecimal 对象
     */
    public static BigDecimal toBigDecimal(short num) {
        return new BigDecimal(num);
    }
    /**
     * 将数字转换成 BigDecimal 对象,便于计算
     *
     * @param num 待转换数字
     * @return 数字 BigDecimal 对象
     */

    public static BigDecimal toBigDecimal(int num) {
        return new BigDecimal(num);
    }

    /**
     * 将数字转换成 BigDecimal 对象,便于计算
     *
     * @param num 待转换数字
     * @return 数字 BigDecimal 对象
     */
    public static BigDecimal toBigDecimal(long num) {
        return new BigDecimal(num);
    }
}
