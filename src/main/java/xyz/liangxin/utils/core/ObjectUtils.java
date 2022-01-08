package xyz.liangxin.utils.core;

import xyz.liangxin.utils.core.text.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Object 工具类
 *
 * @author liangxin
 * @version V1.0
 * @Package xyz.liangxin.springbootdemo.common.uitls
 * @date 2021/4/17 23:34
 * @Description Object 工具类
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


    /**
     * 如果为null 则返回默认值
     *
     * @param object       原值
     * @param defaultValue 默认值
     * @param <T>          值类型
     * @return 判断后的值
     */
    public static <T> T getOrDefault(T object, T defaultValue) {
        return nonNull(object) ? object : defaultValue;
    }

    /**
     * 如果为null 则返回默认值
     *
     * @param list         原值
     * @param defaultValue 默认值
     * @param <T>          值类型
     * @return 判断后的值
     */
    public static <T> List<T> getOrDefault(List<T> list, List<T> defaultValue) {
        return nonNull(list) ? list : defaultValue;
    }

    /**
     * 如果为null 则返回默认值
     *
     * @param array        原值
     * @param defaultValue 默认值
     * @param <T>          值类型
     * @return 判断后的值
     */
    public static <T> T[] getOrDefault(T[] array, T[] defaultValue) {
        return nonNull(array) ? array : defaultValue;
    }

    /**
     * 如果为null 则返回默认值
     *
     * @param set          原值
     * @param defaultValue 默认值
     * @param <T>          值类型
     * @return 判断后的值
     */
    public static <T> Set<T> getOrDefault(Set<T> set, Set<T> defaultValue) {
        return nonNull(set) ? set : defaultValue;
    }


    /**
     * 如果为null 则返回默认值
     *
     * @param map          原值
     * @param defaultValue 默认值
     * @param <K>          值类型
     * @param <V>          值类型
     * @return 判断后的值
     */
    public static <K, V> Map<K, V> getOrDefault(Map<K, V> map, Map<K, V> defaultValue) {
        return nonNull(map) ? map : defaultValue;
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


//----------------------------------- 对象反射工具函数

    /**
     * 根据属性名获取属性值
     *
     * @param fieldName 属性名
     * @param o         待取值对象
     * @return 返回属性值
     */
    private static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter);
            return method.invoke(o);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取属性名数组
     *
     * @param o 待取值对象
     * @return 属性名数组
     */
    private static String[] getFiledName(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
// 属性类型
//            System.out.println(fields[i].getType());
            fieldNames[i] = fields[i].getName();
        }
        return fieldNames;
    }


    /**
     * 获取属性类型(type)，属性名(name)，属性值(value)的map组成的list
     *
     * @param o 待取值对象
     * @return 属性信息集合
     */
    public static List<Map<String, Object>> getFiledInfo(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> infoMap;
        for (Field field : fields) {
            infoMap = new HashMap<>(3);
            infoMap.put("type", field.getType().toString());
            infoMap.put("name", field.getName());
            infoMap.put("value", getFieldValueByName(field.getName(), o));
            list.add(infoMap);
        }
        return list;
    }

    /**
     * 获取对象的所有属性值，返回一个对象数组
     *
     * @param o 待取值对象
     * @return 字段值数组
     */
    public static Object[] getFiledValues(Object o) {
        String[] fieldNames = getFiledName(o);
        Object[] value = new Object[fieldNames.length];
        for (int i = 0; i < fieldNames.length; i++) {
            value[i] = getFieldValueByName(fieldNames[i], o);
        }
        return value;
    }

}
