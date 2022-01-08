package xyz.liangxin.utils.core.number;

import xyz.liangxin.utils.constant.number.OperatorsEnum;
import xyz.liangxin.utils.core.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * <p> 数字计算工具类
 *
 * @author liangxin
 * @version V1.0
 * @since 创建时间: 2022/1/8 19:53
 */
public class CalculateUtils {

//    1、 RoundingMode.ROUND_UP：远离零方向舍入。向绝对值最大的方向舍入，只要舍弃位非0即进位。零舍一入
//
//    2、 RoundingMode.ROUND_DOWN：趋向零方向舍入。向绝对值最小的方向输入，所有的位都要舍弃，不存在进位情况。全舍不入
//
//    3、 RoundingMode.ROUND_CEILING：向正无穷方向舍入。向正最大方向靠拢。若是正数，舍入行为类似于ROUND_UP，若为负数，舍入行为类似于ROUND_DOWN。Math.round()方法就是使用的此模式。
//
//    4、 RoundingMode.ROUND_FLOOR：向负无穷方向舍入。向负无穷方向靠拢。若是正数，舍入行为类似于ROUND_DOWN；若为负数，舍入行为类似于ROUND_UP。
//
//    5、 RoundingMode.HALF_UP：最近数字舍入(5进)。这是我们最经典的四舍五入。
//
//    6、 RoundingMode.HALF_DOWN：最近数字舍入(5舍)。在这里5是要舍弃的。 五舍六入
//
//    7、 RoundingMode.HAIL_EVEN：银行家舍入法


    /**
     * 四舍五入 保留指定位精度的浮点数
     *
     * @param number    待处理的 数字
     * @param precision 保留精度位数
     * @return 处理精度后的数字
     */
    public static BigDecimal round(BigDecimal number, int precision) {
        return number.setScale(precision, RoundingMode.HALF_UP);
    }

    /**
     * 四舍五入 保留指定位精度的浮点数
     *
     * @param doubleNumber 待处理的 浮点数
     * @param precision    保留精度位数
     * @return 处理精度后的小数
     */
    public static double round(double doubleNumber, int precision) {
        return round(NumberUtils.toBigDecimal(doubleNumber), precision).doubleValue();
    }

    /**
     * 四舍五入 保留指定位精度的浮点数
     *
     * @param floatNumber 待处理的 浮点数
     * @param precision   保留精度位数
     * @return 处理精度后的小数
     */
    public static float round(float floatNumber, int precision) {
        return round(NumberUtils.toBigDecimal(floatNumber), precision).floatValue();
    }


    //-----------------------------------------  数字计算方法工具

    /**
     * <p>数字运算运算 不丢失精度</p>
     * <P>结果精度处理方式为 四舍五入</P>
     * <P>除法结果, 最大精度为小数点一百位</P>
     *
     * @param num1      数值1
     * @param num2      数值2
     * @param operators {@link OperatorsEnum} 运算符号
     *                  <p>
     *                  {@link OperatorsEnum#ADD} : 加号
     *                  {@link OperatorsEnum#SUBTRACT} : 减号
     *                  {@link OperatorsEnum#MULTIPLY} : 乘号
     *                  {@link OperatorsEnum#DIVIDE} : 除号
     *                  </p>
     * @param precision 数据处理精度位数  null 则不处理
     * @return 两数运算的结果值
     */
    public static BigDecimal calculation(BigDecimal num1, BigDecimal num2, OperatorsEnum operators, Integer precision) {
        BigDecimal result = BigDecimal.ZERO;
        switch (operators) {
            case ADD:
                result = num1.add(num2);
                break;
            case SUBTRACT:
                result = num1.subtract(num2);
                break;
            case MULTIPLY:
                result = num1.multiply(num2);
                break;
            case DIVIDE:
                /*
                通过BigDecimal的divide方法进行除法时当不整除，出现无限循环小数时，
                就会抛异常：java.lang.ArithmeticException: Non-terminating decimal expansion; no exact representable decimal result. 
                给divide方法设置精确的小数点后 100 位(实际到不了) 超过的舍弃 ,不四舍五入
                 */
                result = num1.divide(num2, 100, RoundingMode.HALF_DOWN);
                break;
            default:
                break;
        }

        // 非空处理 精度 保留 指定位小数...
        if (ObjectUtils.nonNull(precision)) {
            result = round(result, precision);
        }
        return result;
    }

    /**
     * 浮点数运算 不丢失精度
     *
     * @param num1      数值1
     * @param num2      数值2
     * @param operators {@link OperatorsEnum} 运算符号
     *                  <p>
     *                  {@link OperatorsEnum#ADD} : 加号
     *                  {@link OperatorsEnum#SUBTRACT} : 减号
     *                  {@link OperatorsEnum#MULTIPLY} : 乘号
     *                  {@link OperatorsEnum#DIVIDE} : 除号
     *                  </p>
     * @param precision 数据处理精度位数  null 则不处理
     * @return 两数运算的结果值
     */
    public static double calculation(double num1, double num2, OperatorsEnum operators, Integer precision) {
        return calculation(NumberUtils.toBigDecimal(num1), NumberUtils.toBigDecimal(num2), operators, precision).doubleValue();
    }

    /**
     * 数字运算 不丢失精度 自动对所有数值类型进行转换 成 Double 不存在精度损耗
     *
     * @param num1      数值1
     * @param num2      数值2
     * @param operators {@link OperatorsEnum} 运算符号
     *                  <p>
     *                  {@link OperatorsEnum#ADD} : 加号
     *                  {@link OperatorsEnum#SUBTRACT} : 减号
     *                  {@link OperatorsEnum#MULTIPLY} : 乘号
     *                  {@link OperatorsEnum#DIVIDE} : 除号
     *                  </p>
     * @return 结果值 不做精度处理
     */
    public static double calculation(Number num1, Number num2, OperatorsEnum operators) {
        return calculation(NumberUtils.toBigDecimal(num1), NumberUtils.toBigDecimal(num2), operators, null).doubleValue();
    }

    /**
     * 浮点数运算 不丢失精度
     *
     * @param num1      数值1
     * @param operators {@link OperatorsEnum} 运算符号
     *                  <p>
     *                  {@link OperatorsEnum#ADD} : 加号
     *                  {@link OperatorsEnum#SUBTRACT} : 减号
     *                  {@link OperatorsEnum#MULTIPLY} : 乘号
     *                  {@link OperatorsEnum#DIVIDE} : 除号
     *                  </p>
     * @param num2      数值2
     * @return 两数运算的结果值  不做精度处理
     */
    public static double calculation(double num1, OperatorsEnum operators, double num2) {
        return calculation(num1, num2, operators);
    }

    /**
     * 加法
     *
     * @param num1 数值1
     * @param num2 数值2
     * @return 结果值默认四舍五入, 保留两位小数
     */
    public static double add(double num1, double num2) {
        return calculation(num1, num2, OperatorsEnum.ADD, 2);
    }

    /**
     * 减法
     *
     * @param num1 数值1
     * @param num2 数值2
     * @return 结果值默认四舍五入, 保留两位小数
     */
    public static double subtract(double num1, double num2) {
        return calculation(num1, num2, OperatorsEnum.SUBTRACT, 2);
    }

    /**
     * 乘法
     *
     * @param num1 数值1
     * @param num2 数值2
     * @return 结果值默认四舍五入, 保留两位小数
     */
    public static double multiply(double num1, double num2) {
        return calculation(num1, num2, OperatorsEnum.MULTIPLY, 2);
    }

    /**
     * 除法
     *
     * @param num1 被除数
     * @param num2 除数
     * @return 结果值默认四舍五入, 保留两位小数
     */
    public static double divide(double num1, double num2) {
        return calculation(num1, num2, OperatorsEnum.DIVIDE, 2);
    }

}
