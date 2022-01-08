package xyz.liangxin.utils.core.array;

import xyz.liangxin.utils.core.ObjectUtils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 数组工具类
 *
 * @author liangxin
 * @version V1.0
 * @date 2021/11/9 20:47
 */
public class ArrayUtil extends PrimitiveArrayUtil {

    // ---------------------------------------------------------------------- isEmpty

    /**
     * 数组是否为空
     *
     * @param <T>   数组元素类型
     * @param array 数组
     * @return 是否为空
     */
    public static <T> boolean isEmpty(T[] array) {
        return ObjectUtils.isEmpty(array);
    }

    /**
     * 如果给定数组为空，返回默认数组
     *
     * @param <T>          数组元素类型
     * @param array        数组
     * @param defaultArray 默认数组
     * @return 非空（empty）的原数组或默认数组
     * @since 4.6.9
     */
    public static <T> T[] defaultIfEmpty(T[] array, T[] defaultArray) {
        return isEmpty(array) ? defaultArray : array;
    }

    /**
     * 数组是否为空<br>
     * 此方法会匹配单一对象，如果此对象为{@code null}则返回true<br>
     * 如果此对象为非数组，理解为此对象为数组的第一个元素，则返回false<br>
     * 如果此对象为数组对象，数组长度大于0情况下返回false，否则返回true
     *
     * @param array 数组
     * @return 是否为空
     */
    public static boolean isEmpty(Object array) {
        if (isArray(array)) {
            return 0 == Array.getLength(array);
        }
        return false;
    }
    // ---------------------------------------------------------------------- isNotEmpty

    /**
     * 数组是否为非空
     *
     * @param <T>   数组元素类型
     * @param array 数组
     * @return 是否为非空
     */
    public static <T> boolean isNotEmpty(T[] array) {
        return !isEmpty(array);
    }

    /**
     * 数组是否为非空<br>
     * 此方法会匹配单一对象，如果此对象为{@code null}则返回false<br>
     * 如果此对象为非数组，理解为此对象为数组的第一个元素，则返回true<br>
     * 如果此对象为数组对象，数组长度大于0情况下返回true，否则返回false
     *
     * @param array 数组
     * @return 是否为非空
     */
    public static boolean isNotEmpty(Object array) {
        return !isEmpty(array);
    }

    /**
     * 是否包含{@code null}元素
     *
     * @param <T>   数组元素类型
     * @param array 被检查的数组
     * @return 是否包含{@code null}元素
     * @since 3.0.7
     */
    @SuppressWarnings("unchecked")
    public static <T> boolean hasNull(T... array) {
        if (isNotEmpty(array)) {
            for (T element : array) {
                if (null == element) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 多个字段是否全为null
     *
     * @param <T>   数组元素类型
     * @param array 被检查的数组
     * @return 多个字段是否全为null
     * @author dahuoyzs
     * @since 5.4.0
     */
    @SafeVarargs
    public static <T> boolean isAllNull(T... array) {
        return null == firstNonNull(array);
    }

    /**
     * 返回数组中第一个非空元素
     *
     * @param <T>   数组元素类型
     * @param array 数组
     * @return 非空元素，如果不存在非空元素或数组为空，返回{@code null}
     * @since 3.0.7
     */
    @SafeVarargs
    public static <T> T firstNonNull(T... array) {
        return firstMatch(Objects::nonNull, array);
    }

    /**
     * 返回数组中第一个匹配规则的值
     *
     * @param <T>     数组元素类型
     * @param predicate 匹配接口，实现此接口自定义匹配规则
     * @param array   数组
     * @return 非空元素，如果不存在非空元素或数组为空，返回{@code null}
     * @since 3.0.7
     */
    @SafeVarargs
    public static <T> T firstMatch(Predicate<T> predicate, T... array) {
        if (isNotEmpty(array)) {
            for (final T val : array) {
                if (predicate.test(val)) {
                    return val;
                }
            }
        }
        return null;
    }


    /**
     * 对象是否为数组对象
     *
     * @param obj 对象
     * @return 是否为数组对象，如果为{@code null} 返回false
     */
    public static boolean isArray(Object obj) {
        if (null == obj) {
            // throw new NullPointerException("Object check for isArray is null");
            return false;
        }
        return obj.getClass().isArray();
    }


    // ---------------------------------------------------------------------- 翻转数组 reverse
    /**
     * 翻转数组, 不覆盖原数组
     *
     * @param array 原数组
     * @param <T>   数组类型
     * @return 翻转后的数组
     */
    public static <T> T[] reverse(T[] array) {
        return reverse(array, false);
    }

    /**
     * 翻转数组
     *
     * @param array 原数组
     * @param cover 是否覆盖原数组
     * @param <T>   数组类型
     * @return 翻转后的数组
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] reverse( T[] array, boolean cover) {
        if (isEmpty(array)) {
            return (T[]) new Object[0];
        }
        T[] clone = cover ? array : array.clone();
        List<T> list = Arrays.asList(clone);
        Collections.reverse(list);
        return list.toArray(clone);
    }

    /**
     * 合并数组
     *
     * @param first 目标数组
     * @param rest  合并数组 列表
     * @param <T>   数组类型
     * @return 合并后的数组
     */
    @SafeVarargs
    public static <T> T[] concatAll(T[] first, T[]... rest) {
        Objects.requireNonNull(first, "合并目标数组不能为 空");
        int totalLength = first.length;
        int offset = totalLength;
        T[] result = null;
        if (ObjectUtils.nonEmpty(rest)) {
            totalLength += (int) Arrays.stream(rest)
                    .filter(ObjectUtils::nonEmpty)
                    .collect(Collectors.summarizingInt(Array::getLength))
                    .getSum();
            result = Arrays.copyOf(first, totalLength);
            for (T[] array : rest) {
                if (ObjectUtils.nonEmpty(array)) {
                    System.arraycopy(array, 0, result, offset, array.length);
                    offset += array.length;
                }
            }
        }
        return result;
    }

}
