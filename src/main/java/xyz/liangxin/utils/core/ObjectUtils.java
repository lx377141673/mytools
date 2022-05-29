package xyz.liangxin.utils.core;

import xyz.liangxin.utils.core.array.ArrayUtil;
import xyz.liangxin.utils.core.text.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Object 工具类
 *
 * @author liangxin
 * @version V1.0
 * @date 2021/4/17 23:34
 */
public class ObjectUtils {

    private ObjectUtils() {
    }


    //-------------------------- 非空拆箱检查 unboxing

    /**
     * 拆箱 Long  值
     *
     * @param result Long 值
     * @return 如果 result 为 null 返回 0 , 或者 result.longValue();
     */
    public static long unboxing(Long result) {
        return Optional.ofNullable(result).orElse(0L);
    }

    /**
     * 拆箱 Number 值
     *
     * @param result number 值
     * @return 如果 result 为 null 返回 {@link BigDecimal#ZERO} , 或者 result.longValue();
     */
    public static Number unboxing(Number result) {
        return Optional.ofNullable(result).orElse(BigDecimal.ZERO);
    }

    /**
     * 拆箱 Boolean  值
     *
     * @param result Boolean 值
     * @return 如果 result 为 null , 返回 false, 否则返回 本身的值
     */
    public static boolean unboxing(Boolean result) {
        return Optional.ofNullable(result).orElse(Boolean.FALSE);
    }


    // ------------------------------------------- 检查强转类型 checked

    /**
     * 检查强转类型
     *
     * @param o   转换对象
     * @param <T> 转换类型
     * @return 如果是 null 直接返回, 不是空,强转返回
     */
    @SuppressWarnings("unchecked")
    public static <T> T checked(Object o) {
        return (T) Optional.ofNullable(o).orElse(null);
    }

    /**
     * 检查强转类型
     *
     * @param array 转换的 数组对象
     * @param <T>   转换类型
     * @return 如果是 null 直接返回, 不是空,强转返回
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] checked(Object[] array) {
        return (T[]) Optional.ofNullable(array).orElse(null);
    }

    /**
     * 检查 List 强转类型
     *
     * @param list 转换的List 对象
     * @param <T>  元素的转换类型
     * @return 如果是 null 直接返回 {@link Collections#emptyList()}, 不是空,强转返回
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> checked(List list) {
        return Optional.ofNullable(list).orElse(Collections.emptyList());
    }

    /**
     * 检查 Set 强转类型
     *
     * @param set 转换对象
     * @param <T> 元素的转换类型
     * @return 如果是 null 直接返回 {@link Collections#emptySet()}, 不是空,强转返回
     */
    @SuppressWarnings("unchecked")
    private static <T> Set<T> checked(Set set) {
        return Optional.ofNullable(set).orElse(Collections.emptySet());
    }

    /**
     * 检查 Map 强转类型
     *
     * @param map 转换对象
     * @param <T> 元素的转换类型
     * @return 如果是 null 直接返回 {@link Collections#emptyMap()}, 不是空,强转返回
     */
    @SuppressWarnings("unchecked")
    private static <T> Map<String, T> checked(Map map) {
        return Optional.ofNullable(map).orElse(Collections.emptyMap());
    }


    /**
     * 获取 一个 过滤字符串 NotEmpty 的 Optional
     *
     * @param string 需要过滤的字符串
     * @return String NotEmpty Optional
     */
    public static Optional<String> filterStringNotEmpty(String string) {
        return Optional.ofNullable(string).filter(StringUtils::isNotEmpty);
    }


    //-------------------------- 空检查 isNull OR isEmpty

    /**
     * 对象是 null
     *
     * @param object 对象
     * @return 如果为 null  返回 true
     */
    public static boolean isNull(Object object) {
        return Objects.isNull(object);
    }

    /**
     * 对象数组 是 空的, null || 长度为 0
     *
     * @param object 对象数组
     * @return 如果是 空的对象数组, 返回 true
     */
    public static boolean isEmpty(Object[] object) {
        return Objects.isNull(object) || object.length == 0;
    }

    /**
     * 对象集合是空的, null || 长度为 0
     *
     * @param object 集合对象
     * @return 如果是 空的集合对象, 返回 true
     */
    public static boolean isEmpty(Collection<?> object) {
        return Objects.isNull(object) || object.isEmpty();
    }

    /**
     * 字典对象是空的, null || 长度为 0
     *
     * @param object map 对象
     * @return 如果是空的 字典对象, 返回 true
     */
    public static boolean isEmpty(Map<?, ?> object) {
        return Objects.isNull(object) || object.isEmpty();
    }

    //------------------------------------- 非空检查

    /**
     * 对象 非空 null
     *
     * @param object 对象
     * @return 是否非 null
     */
    public static boolean nonNull(Object object) {
        return Objects.nonNull(object);
    }

    /**
     * 数组对象 非空, != null && 长度不为 0
     *
     * @param object 数组对象
     * @return 数组对象非空, 则返回 true
     */
    public static boolean nonEmpty(Object[] object) {
        return !isEmpty(object);
    }

    /**
     * 对象集合 非空, != null && 长度不为 0
     *
     * @param object 集合对象
     * @return 如果是 非空的集合对象, 返回 true
     */
    public static boolean nonEmpty(Collection<?> object) {
        return !isEmpty(object);
    }

    /**
     * 字典对象 是非空, != null && 长度不为 0
     *
     * @param object map 对象
     * @return 如果是非空的 字典对象, 返回 true
     */
    public static boolean nonEmpty(Map<?, ?> object) {
        return !isEmpty(object);
    }


    //------------------------------------ getNext


    /**
     * 断定是否执行下一步操作,
     * <li>如果断定失败, 则返回 defaultValue</li>
     * <li>断定成功 , 返回 next.apply(object)</li>
     *
     * @param object       操作值
     * @param defaultValue 默认值
     * @param next         操作函数
     * @param predicate    断定条件
     * @param <T>          操作值类型
     * @param <R>          返回值类型
     * @return 断定后的处理结果
     * <li>如果断定失败, 则返回 defaultValue</li>
     * <li>断定成功 , 返回 next.apply(object)</li>
     */
    public static <T, R> R getNext(T object, R defaultValue, Function<T, R> next, Predicate<T> predicate) {
        return predicate.test(object) ? next.apply(object) : defaultValue;
    }


    /**
     * 根据是否为 null, 判断是否执行下一步操作,并获得结果,
     * <li>如果断定失败, 则返回 defaultValue </li>
     * <li>断定成功 , 返回 next.apply(object)</li>
     * <p> 等价于 {@code getNext(object, defaultValue, next, ObjectUtils::nonNull)}</p>
     *
     * @param object       操作值
     * @param next         操作函数
     * @param defaultValue 默认值
     * @param <T>          操作值类型
     * @param <R>          返回值类型
     * @return 断定后的处理结果
     * <li>如果断定失败, 则返回 defaultValue</li>
     * <li>断定成功 , 返回 next.apply(object)</li>
     */
    public static <T, R> R getNext(T object, Function<T, R> next, R defaultValue) {
        return getNext(object, defaultValue, next, ObjectUtils::nonNull);
    }


    /**
     * 根据 操作值是否不为 null , 判断是否 执行下一步操作,
     * <li>如果操作值null, 则返回 null </li>
     * <li>如果操作值 不为 null , 返回 next.apply(object)</li>
     * <p> 等价于 {@code getNext(object, null, next, ObjectUtils::nonNull)}</p>
     * <p> 等价于 {@code getNext(object, next, ObjectUtils::nonNull)}</p>
     *
     * @param object 操作值
     * @param next   操作函数
     * @param <T>    操作值类型
     * @param <R>    返回值类型
     * @return 断定后的处理结果
     * <li>如果断定失败, 则返回 null</li>
     * <li>断定成功 , 返回 next.apply(object)</li>
     */
    public static <T, R> R getNext(T object, Function<T, R> next) {
        return getNext(object, null, next, ObjectUtils::nonNull);
    }


    //------------------------------------ getOrDefault


    /**
     * 根据条件断定 判断 返回 原值 还是 默认值
     * <p> 如果 {@link Predicate#test(Object)}  断定成功 返回 value, 否则返回 defaultValue</p>
     *
     * @param value        原值
     * @param defaultValue 默认值函数
     * @param predicate    断定条件
     * @param <T>          操作类型
     * @return 返回处理后的值
     */
    public static <T> T getOrDefault(T value, Supplier<T> defaultValue, Predicate<T> predicate) {
//        getNext(value, x -> defaultValue, predicate);
        return predicate.test(value) ? value : defaultValue.get();
    }

    /**
     * 如果为null 则返回默认值
     * <p> 等价于 {@code getOrDefault(value,defaultValue,ObjectUtils::nonNull)}</p>
     *
     * @param value        原值
     * @param defaultValue 默认值函数
     * @param <T>          值类型
     * @return 判断后的值
     */
    public static <T> T getOrDefault(T value, Supplier<T> defaultValue) {
        return getOrDefault(value, defaultValue, ObjectUtils::nonNull);
    }

    /**
     * 根据条件断定 判断 返回 原值 还是 默认值
     * <p> 如果 {@link Predicate#test(Object)}  断定成功 返回 value, 否则返回 defaultValue</p>
     * <p> 等价于 {@code getOrDefault(value, () -> defaultValue, predicate)}</p>
     *
     * @param value        原值
     * @param predicate    断定条件
     * @param defaultValue 默认值
     * @param <T>          操作类型
     * @return 返回处理后的值
     */
    public static <T> T getOrDefault(T value, Predicate<T> predicate, T defaultValue) {
        return getOrDefault(value, () -> defaultValue, predicate);
    }

    /**
     * 如果为null 则返回默认值
     * <p> 等价于 {@code getOrDefault(value, ObjectUtils::nonNull, defaultValue)}</p>
     *
     * @param value        原值
     * @param defaultValue 默认值
     * @param <T>          值类型
     * @return 判断后的值
     */
    public static <T> T getOrDefault(T value, T defaultValue) {
        return getOrDefault(value, ObjectUtils::nonNull, defaultValue);
    }

    /**
     * 如果为 Empty 则返回默认值
     * <p> 等价于 {@code getOrDefault(list, ObjectUtils::nonEmpty, defaultValue)}</p>
     *
     * @param list         原值
     * @param defaultValue 默认值
     * @param <T>          值类型
     * @return 判断后的值
     */
    public static <T> List<T> getEmptyDefault(List<T> list, List<T> defaultValue) {
        return getOrDefault(list, ObjectUtils::nonEmpty, defaultValue);
    }

    /**
     * 如果为null 则返回默认值
     * <p> 等价于 {@code getOrDefault(array, ObjectUtils::nonEmpty, defaultValue)}</p>
     *
     * @param array        原值
     * @param defaultValue 默认值
     * @param <T>          值类型
     * @return 判断后的值
     */
    public static <T> T[] getEmptyDefault(T[] array, T[] defaultValue) {
        return getOrDefault(array, ObjectUtils::nonEmpty, defaultValue);
    }

    /**
     * 如果为null 则返回默认值
     * <p> 等价于 {@code getOrDefault(set, ObjectUtils::nonEmpty, defaultValue)}</p>
     *
     * @param set          原值
     * @param defaultValue 默认值
     * @param <T>          值类型
     * @return 判断后的值
     */
    public static <T> Set<T> getEmptyDefault(Set<T> set, Set<T> defaultValue) {
        return getOrDefault(set, ObjectUtils::nonEmpty, defaultValue);
    }


    /**
     * 如果为null 则返回默认值
     * <p> 等价于 {@code getOrDefault(map, ObjectUtils::nonEmpty, defaultValue)}</p>
     *
     * @param map          原值
     * @param defaultValue 默认值
     * @param <K>          值类型
     * @param <V>          值类型
     * @return 判断后的值
     */
    public static <K, V> Map<K, V> getEmptyDefault(Map<K, V> map, Map<K, V> defaultValue) {
        return getOrDefault(map, ObjectUtils::nonEmpty, defaultValue);
    }


    //--------------------------------------------- 多个对象 匹配

    /**
     * 比较两个对象是否相等。<br>
     * 相同的条件有两个，满足其一即可：
     * 1. obj1 == null && obj2 == null 2. obj1.equals(obj2)
     *
     * @param obj1 对象1
     * @param obj2 对象2
     * @return 是否相等
     */
    public static boolean objEqual(Object obj1, Object obj2) {
        return Objects.equals(obj1, obj2);
    }

    /**
     * 比较两个对象是否不相等。<br>
     *
     * @param obj1 对象1
     * @param obj2 对象2
     * @return 是否不等
     */
    public static boolean notEqual(Object obj1, Object obj2) {
        return !objEqual(obj1, obj2);
    }

    /**
     * 判断 对象是否是指定类型
     * 判断两个对象是否同一类型
     *
     * @param object     任意对象
     * @param typeObject 目标类型对象
     * @return 两个对象是否为同一类型
     */
    public static boolean objetEqType(Object object, Object typeObject) {
        try {
            return object.getClass().equals(typeObject.getClass());
        } catch (NullPointerException e) {
            return false;
        }
    }


    public static <T> Object clone(T o) {
        T result = ArrayUtil.clone(o);
        result = isNull(result) ? (T) cloneByCloneable(o) : result;
        result = isNull(result) ? (T) cloneByStream(o) : result;
        return result;
    }

    /**
     * 序列化克隆 , 浅复制,
     * <p>执行 clone 方法</p>
     *
     * @param o 目标对象 需要继承{@link Cloneable}
     * @return 复制对象
     * <li>如果目标对象没有继承 {@link Cloneable} 则 返回空</li>
     * <li>clone异常返回null</li>
     * <li>目标类为 null 返回null</li>
     */
    public static <T> Object cloneByCloneable(T o) {
        if (isNull(o) || !(o instanceof Cloneable)) {
            return null;
        }
        return ReflectUtils.invoke(o, "clone");
    }


    /**
     * 序列化克隆 , 深复制
     *
     * @param o 目标对象 需要继承{@link Serializable}
     * @return 复制对象
     * <li>如果目标对象没有继承 {@link Serializable} 则 返回空</li>
     * <li>序列化异常返回null</li>
     * <li>目标类为 null 返回null</li>
     */
    public static <T> Object cloneByStream(T o) {
        if (isNull(o) || !(o instanceof Serializable)) {
            return null;
        }
        //将对象写入流中
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(bao);
            oos.writeObject(o);
            //将对象从流中取出
            ByteArrayInputStream bis = new ByteArrayInputStream(bao.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            oos.close();
            ois.close();
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}

