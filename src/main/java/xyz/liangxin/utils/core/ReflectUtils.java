package xyz.liangxin.utils.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.liangxin.utils.core.text.CharSequenceUtil;
import xyz.liangxin.utils.core.text.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Function;

/**
 * <p> 反射工具
 *
 * @author liangxin
 * @version V1.0
 * @since 创建时间: 2022/1/18 0:39
 */
public class ReflectUtils {
    private static final Logger logger = LoggerFactory.getLogger(ReflectUtils.class);

    private static final String CLASS_ERROR = "操作 Class 不能为 null";
    private static final String METHOD_ERROR = "操作 method 不能为 null";
    private static final String OBJECT_ERROR = "操作 Object 不能为 null";

    //----------------------------------- 对象 函数 反射工具

    /**
     * <p>返回此Object的运行时类。 返回的Class对象是被表示类的static synchronized方法锁定的对象。
     * <p>实际结果类型是Class<? extends |X|> Class<? extends |X|>其中|X| 是调用getClass的表达式的静态类型的擦除。 例如，此代码片段中不需要强制转换：
     *
     * @param obj 操作对象
     * @param <T> 操作类型
     * @return 表示此对象的运行时类的Class对象, 如果为 null 返回 null
     */
    public static <T> Class<?> getClass(T obj) {
        return ObjectUtils.getNext(obj, (Function<T, ? extends Class<?>>) T::getClass);
    }

    public static <T> Class<?>[] getClass(T[] obj) {
        Class<?>[] array = new Class[ObjectUtils.isEmpty(obj) ? 0 : obj.length];
        for (int i = 0; i < array.length; i++) {
            array[i] = getClass(obj[i]);
        }
        return array;
    }

    /**
     * <p>返回一个包含Method对象的数组，该对象反映了由此Class对象表示的类或接口的所有公共方法，包括由类或接口声明的那些以及从超类和超接口继承的那些。
     * <p>如果此Class对象表示具有多个具有相同名称和参数类型但返回类型不同的公共方法的类型，则返回的数组对于每个此类方法都有一个Method对象。
     * <p>如果此Class对象表示具有类初始化方法<clinit>的类型，则返回的数组没有对应的Method对象。
     * <p>如果此Class对象表示一个数组类型，则返回的数组具有一个Method对象，用于该数组类型从Object继承的每个公共方法。 它不包含clone()的Method对象。
     * <p>如果此Class对象表示一个接口，则返回的数组不包含任何来自Object的隐式声明的方法。 因此，如果在此接口或其任何超接口中未显式声明任何方法，则返回的数组的长度为 0。（请注意，表示类的Class对象始终具有从Object继承的公共方法。）
     * <p>如果此Class对象表示原始类型或 void，则返回的数组的长度为 0。
     * <p>在此Class对象表示的类或接口的超接口中声明的静态方法不被视为类或接口的成员。
     * <p>返回数组中的元素没有排序，也没有任何特定的顺序
     *
     * @param clazz 操作类型
     * @return 表示此类的公共方法的Method对象数组
     */
    public static Method[] getMethods(Class<?> clazz) {
        Objects.requireNonNull(clazz, CLASS_ERROR);
        return ObjectUtils.getNext(clazz, Class::getMethods, new Method[0]);
    }


    /**
     * <p> 获取对象的 所有 公共[public] 函数
     * <p>包括由类或接口声明的那些以及从超类和超接口继承的那些。
     * <p>不包括 构造函数</p>
     *
     * @param obj 对象
     * @return 公共函数数组
     */
    public static Method[] getMethod(Object obj) {
        return getMethods(getClass(obj));
    }

    /**
     * <p> 当前类型内部定义的所有函数 (不包括 构造函数)</p>
     * <p> 返回一个包含Method对象的数组，该对象反映了此Class对象表示的类或接口的所有声明方法，
     * <p> 包括公共[public]、受保护[protected]、默认（包）[default (package)]访问和私有方法[private]，但不包括继承方法
     *
     * @param clazz 操作类型
     * @return 函数数组
     */
    public static Method[] getDeclaredMethods(Class<?> clazz) {
        Objects.requireNonNull(clazz, CLASS_ERROR);
        return ObjectUtils.getNext(clazz, Class::getDeclaredMethods, new Method[0]);
//        return clazz.getDeclaredMethods();
    }

    /**
     * <p> 当前对象类型内部定义的所有函数 (不包括 构造函数)</p>
     * <p> 返回一个包含Method对象的数组，该对象反映了此Class对象表示的类或接口的所有声明方法，
     * <p> 包括公共[public]、受保护[protected]、默认（包）[default (package)]访问和私有方法[private]，但不包括继承方法
     *
     * @param obj 操作对象
     * @return 自定义函数数组
     */
    public static Method[] getDeclaredMethods(Object obj) {
        return getDeclaredMethods(getClass(obj));
    }

    /**
     * 获取指定类型对象中的 指定公共[public]函数对象
     *
     * @param clazz          对象操作类型
     * @param name           函数名
     * @param parameterTypes 函数参数类型列表
     * @return 与指定name和parameterTypes匹配的Method对象 (公共方法)
     * <li> 操作失败, 返回 null </li>
     */
    public static Method getPublicMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        try {
            Objects.requireNonNull(clazz, CLASS_ERROR);
            StringUtils.requireNonEmpty(name, "方名参数 isEmpty");
            return clazz.getMethod(name, parameterTypes);
        } catch (NullPointerException | NoSuchMethodException e) {
            logger.error("方法 [" + name + "] 未找到", e);
        }
        return null;
    }

    /**
     * 获取指定对象中的 指定函数对象
     *
     * @param obj            操作对象
     * @param name           函数名
     * @param parameterTypes 函数参数类型列表
     * @return 与指定name和parameterTypes匹配的Method对象 (公共方法)
     * <li> 操作失败, 返回 null </li>
     */
    public static Method getPublicMethod(Object obj, String name, Class<?>... parameterTypes) {
        return getPublicMethod(getClass(obj), name, parameterTypes);
    }


    /**
     * <p> 当前类型内部定义的 指定函数名的 函数 (不包括 构造函数)</p>
     * <p> 返回一个包含Method对象的数组，该对象反映了此Class对象表示的类或接口的所有声明方法，
     * <p> 包括公共[public]、受保护[protected]、默认（包）[default (package)]访问和私有方法[private]，但不包括继承方法
     *
     * @param clazz          对象操作类型
     * @param name           函数名
     * @param parameterTypes 函数参数类型列表
     * @return 与指定name和parameterTypes匹配的Method对象 (公共方法)
     * <li> 操作失败, 返回 null </li>
     */
    public static Method getDeclaredMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        try {
            Objects.requireNonNull(clazz, CLASS_ERROR);
            StringUtils.requireNonEmpty(name, "方名参数 isEmpty");
            return clazz.getDeclaredMethod(name, parameterTypes);
        } catch (NullPointerException | NoSuchMethodException e) {
            logger.error("方法 [" + name + "] 未找到", e);
        }
        return null;
    }


    /**
     * <p> 当前对象类型内部定义的 指定函数名的 函数 (不包括 构造函数)</p>
     * <p> 返回一个包含Method对象的数组，该对象反映了此Class对象表示的类或接口的所有声明方法，
     * <p> 包括公共[public]、受保护[protected]、默认（包）[default (package)]访问和私有方法[private]，但不包括继承方法
     *
     * @param obj            操作对象
     * @param name           函数名
     * @param parameterTypes 函数参数类型列表
     * @return 与指定name和parameterTypes匹配的Method对象 (公共方法)
     * <li> 操作失败, 返回 null </li>
     */
    public static Method getDeclaredMethod(Object obj, String name, Class<?>... parameterTypes) {
        return getDeclaredMethod(getClass(obj), name, parameterTypes);
    }

    /**
     * 查找指定类型对象中 的 指定 函数名的 的函数对象
     * <p>包括公共[public]、受保护[protected]、默认（包）[default (package)]访问和私有方法[private],继承的函数</p>
     * <p>不包括 构造函数</p>
     *
     * @param clazz          类型对象
     * @param name           函数名
     * @param parameterTypes 参数类型数组
     * @return 函数对象
     */
    public static Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        return ObjectUtils.getOrDefault(getDeclaredMethod(clazz, name, parameterTypes),
                () -> getPublicMethod(clazz, name, parameterTypes));
    }

    /**
     * 查找指定对象类型中 的 指定 函数名的 的函数对象
     * <p>包括公共[public]、受保护[protected]、默认（包）[default (package)]访问和私有方法[private],继承的函数</p>
     * <p>不包括 构造函数</p>
     *
     * @param obj            操作对象
     * @param name           函数名
     * @param parameterTypes 参数类型数组
     * @return 函数对象
     */
    public static Method getMethod(Object obj, String name, Class<?>... parameterTypes) {
        return getMethod(getClass(obj), name, parameterTypes);
    }


    /**
     * obj 执行 Method 对象
     *
     * @param obj    操作对象
     * @param method 函数对象
     * @param args   函数参数列表
     * @return 执行结果, 操作异常, 返回 null
     */
    public static Object invoke(Object obj, Method method, Object... args) {
        try {
            Objects.requireNonNull(obj, OBJECT_ERROR);
            Objects.requireNonNull(method, METHOD_ERROR);
            method.setAccessible(true);
            return method.invoke(obj, args);
        } catch (NullPointerException | IllegalAccessException | InvocationTargetException e) {
            logger.error("执行操作异常", e);
        }
        return null;
    }

    /**
     * obj 执行 指定函数
     * <p>包括公共[public]、受保护[protected]、默认（包）[default (package)]访问和私有方法[private],继承的函数</p>
     * <p>不包括 构造函数</p>
     *
     * @param obj        操作对象
     * @param methodName 函数名
     * @param args       函数参数列表,(不支持 基本类型)
     * @return 执行结果, 操作异常, 返回 null
     */
    public static Object invoke(Object obj, String methodName, Object... args) {
        Class<?>[] parameterTypes = getClass(args);
        Method method = getMethod(obj, methodName, parameterTypes);
        return invoke(obj, method, args);
    }


    //----------------------------------- 对象 属性 反射工具

    /**
     * <p> 返回一个包含Field对象的数组，该数组反映了此Class对象表示的类或接口的所有可访问公共字段。 </p>
     * <p> 如果此Class对象表示没有可访问的公共字段的类或接口，则此方法返回长度为 0 的数组。 </p>
     * <p> 如果此Class对象表示一个类，则此方法返回该类及其所有超类的公共字段。 </p>
     * <p> 如果此Class对象表示一个接口，则此方法返回该接口及其所有超接口的字段。 </p>
     * <p> 如果此Class对象表示数组类型、原始类型或 void，则此方法返回长度为 0 的数组。 </p>
     * <p> 返回数组中的元素没有排序，也没有任何特定的顺序。 </p>
     *
     * @param clazz Class 对象
     * @return 表示公共字段的Field对象数组
     */
    public static Field[] getPublicFields(Class<?> clazz) {
        return ObjectUtils.getNext(clazz, Class::getFields, new Field[0]);
    }

    /**
     * <p> 返回一个包含Field对象的数组，该数组反映了此 操作对象的 Class对象表示的类或接口的所有可访问公共字段。 </p>
     * <p> 如果此 操作对象的 Class对象表示没有可访问的公共字段的类或接口，则此方法返回长度为 0 的数组。 </p>
     * <p> 如果此 操作对象的  Class对象表示一个类，则此方法返回该类及其所有超类的公共字段。 </p>
     * <p> 如果此 操作对象的  Class对象表示一个接口，则此方法返回该接口及其所有超接口的字段。 </p>
     * <p> 如果此 操作对象的  Class对象表示数组类型、原始类型或 void，则此方法返回长度为 0 的数组。 </p>
     * <p> 返回数组中的元素没有排序，也没有任何特定的顺序。 </p>
     *
     * @param obj 操作对象
     * @return 表示公共字段的Field对象数组
     */
    public static Field[] getPublicFields(Object obj) {
        return getPublicFields(getClass(obj));
    }

    /**
     * 返回一个反映此Class对象表示的类或接口的指定公共成员字段的Field对象。
     * name参数是一个String ，指定所需字段的简单名称
     *
     * @param clazz class 对象
     * @param name  字段名称
     * @return 由name指定的此类的Field对象
     */
    public static Field getPublicField(Class<?> clazz, String name) {
        try {
            StringUtils.requireNonEmpty(name, "字段名 不能为 空");
            return clazz == null ? null : clazz.getField(name);
        } catch (NoSuchFieldException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 返回一个反映此Class对象表示的类或接口的指定公共成员字段的Field对象。
     * name参数是一个String ，指定所需字段的简单名称
     *
     * @param obj  操作 对象
     * @param name 字段名称
     * @return 由name指定的此类的Field对象
     */
    public static Field getPublicField(Object obj, String name) {
        return getPublicField(getClass(obj), name);
    }

    /**
     * <p> 返回一个Field对象数组，反映由此Class对象表示的类或接口声明的所有字段。  </p>
     * <p> 这包括公共、受保护、默认（包）访问和私有字段，但不包括继承的字段。 </p>
     * <p> 如果此Class对象表示没有声明字段的类或接口，则此方法返回长度为 0 的数组。 </p>
     * <p> 如果此Class对象表示数组类型、原始类型或 void，则此方法返回长度为 0 的数组。 </p>
     * <p> 返回数组中的元素没有排序，也没有任何特定的顺序。 </p>
     *
     * @param clazz Class 对象
     * @return 表示公共字段的Field对象数组
     */
    public static Field[] getDeclaredFields(Class<?> clazz) {
        return ObjectUtils.getNext(clazz, Class::getDeclaredFields, new Field[0]);
    }

    /**
     * <p> 返回一个Field对象数组，反映由此Class对象表示的类或接口声明的所有字段。  </p>
     * <p> 这包括公共、受保护、默认（包）访问和私有字段，但不包括继承的字段。 </p>
     * <p> 如果此Class对象表示没有声明字段的类或接口，则此方法返回长度为 0 的数组。 </p>
     * <p> 如果此Class对象表示数组类型、原始类型或 void，则此方法返回长度为 0 的数组。 </p>
     * <p> 返回数组中的元素没有排序，也没有任何特定的顺序。 </p>
     *
     * @param obj 操作对象
     * @return 表示公共字段的Field对象数组
     */
    public static Field[] getDeclaredFields(Object obj) {
        return ObjectUtils.getNext(getClass(obj), Class::getDeclaredFields, new Field[0]);
    }

    /**
     * <p> 返回一个反映此Class对象表示的类或接口的指定声明字段的Field对象。
     * <p> 这包括公共、受保护、默认（包）访问和私有字段，但不包括继承的字段。 </p>
     * <p> name参数是一个String ，它指定所需字段的简单名称。
     * <p>  如果这个Class对象代表一个数组类型，那么这个方法没有找到数组类型的length字段。
     *
     * @param clazz Class对象
     * @param name  字段的名称
     * @return 此类中指定字段的Field对象
     */
    public static Field getDeclaredField(Class<?> clazz, String name) {
        try {
            StringUtils.requireNonEmpty(name, "字段名 不能为 空");
            return clazz == null ? null : clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * <p> 返回一个反映此Class对象表示的类或接口的指定声明字段的Field对象。
     * <p> 这包括公共、受保护、默认（包）访问和私有字段，但不包括继承的字段。 </p>
     * <p> name参数是一个String ，它指定所需字段的简单名称。
     * <p>  如果这个Class对象代表一个数组类型，那么这个方法没有找到数组类型的length字段。
     *
     * @param obj  操作对象
     * @param name 字段的名称
     * @return 此类中指定字段的Field对象
     */
    public static Field getDeclaredField(Object obj, String name) {
        return getDeclaredField(getClass(obj), name);
    }

    /**
     * <p> 返回一个反映此Class对象表示的类或接口的指定声明字段的Field对象。
     * <p> 这包括公共、受保护、默认（包）访问和私有字段，包括继承的字段。 </p>
     *
     * @param clazz Class对象
     * @param name  字段的名称
     * @return 此类中指定字段的Field对象
     */
    public static Field getField(Class<?> clazz, String name) {
        return ObjectUtils.getOrDefault(getDeclaredField(clazz, name),
                () -> getPublicField(clazz, name));
    }

    /**
     * <p> 返回一个反映此Class对象表示的类或接口的指定声明字段的Field对象。
     * <p> 这包括公共、受保护、默认（包）访问和私有字段，包括继承的字段。 </p>
     *
     * @param obj  操作对象
     * @param name 字段的名称
     * @return 此类中指定字段的Field对象
     */
    public static Field getField(Object obj, String name) {
        return getField(getClass(obj), name);
    }

    /**
     * 获取 对象内 指定字段的值
     * <p> 不经过 get() 函数 </p>
     *
     * @param obj   操作对象
     * @param field 字段对象
     * @return 字段值, 操作失败返回 null
     */
    public static Object getFieldValue(Object obj, Field field) {
        try {
            Objects.requireNonNull(obj, "操作对象不能为null");
            Objects.requireNonNull(field, "操作字段对象 不能为null");
            field.setAccessible(true);
            return field.get(obj);
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取 对象内 指定字段的值
     * <p> 不经过 get() 函数 </p>
     *
     * @param obj       操作对象
     * @param fieldName 字段名
     * @return 字段值, 操作失败返回 null
     */
    public static Object getFieldValue(Object obj, String fieldName) {
        return getFieldValue(obj, getField(obj, fieldName));
    }

    /**
     * <p>指定对象参数上的此Field对象表示的字段设置为指定的新值。
     * <p>如果基础字段具有原始类型，则新值会自动展开。
     * <p> 不经过 set() 函数 </p>
     *
     * @param obj   对象信息
     * @param field 字段信息
     * @param value 新的字段值
     * @return 操作成功 返回 true, 操作失败 返回 false
     */
    public static boolean setFieldValue(Object obj, Field field, Object value) {
        try {
            Objects.requireNonNull(obj, "操作对象不能为null");
            Objects.requireNonNull(field, "操作字段对象 不能为null");
            field.setAccessible(true);
            field.set(obj, value);
            return true;
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * <p>指定对象参数上的此Field对象表示的字段设置为指定的新值。
     * <p>如果基础字段具有原始类型，则新值会自动展开。
     * <p> 不经过 set() 函数 </p>
     *
     * @param obj       对象信息
     * @param fieldName 字段名
     * @param value     新的字段值
     * @return 操作成功 返回 true, 操作失败 返回 false
     */
    public static boolean setFieldValue(Object obj, String fieldName, Object value) {
        return setFieldValue(obj, getField(obj, fieldName), value);
    }

    /**
     * 返回 对象 指定 字段对象的 get()/is() 方法函数对象
     * <p>其他类型的字段为 get() 函数</p>
     * <p>布尔类型 {@code Boolean / boolean} 类型的字段为 is() 函数</p>
     * <p>如果操作失败 返回 null</p>
     *
     * @param obj   操作对象
     * @param field 字段对象
     * @return 指定字段的 get() 函数对象
     */
    public static Method fieldGetMethod(Object obj, Field field) {
        return ObjectUtils.getNext(obj,
                null,
                o -> {
                    String name = (field.getType().equals(Boolean.class)
                            || field.getType().equals(boolean.class) ? "is" : "get")
                            + CharSequenceUtil.convertUppercase(field.getName());
                    return getMethod(o, name);

                },
                x -> x != null && field != null);

    }

    /**
     * 返回 对象 指定 字段对象的 get()/is() 方法函数对象
     * <p>其他类型的字段为 get() 函数</p>
     * <p>布尔类型 {@code Boolean / boolean} 类型的字段为 is() 函数</p>
     * <p>如果操作失败 返回 null</p>
     *
     * @param obj       操作对象
     * @param fieldName 字段名
     * @return 指定字段的 get() 函数对象
     */
    public static Method fieldGetMethod(Object obj, String fieldName) {
        return fieldGetMethod(obj, getField(obj, fieldName));
    }


    /**
     * 返回 对象 指定 字段对象的 set() 方法函数对象
     * <p>如果操作失败 返回 null</p>
     *
     * @param obj   操作对象
     * @param field 字段对象
     * @return 指定字段的 set() 函数对象
     */
    public static Method fieldSetMethod(Object obj, Field field) {
        return ObjectUtils.getNext(obj,
                null,
                o -> getMethod(o,
                        "set" + CharSequenceUtil.convertUppercase(field.getName()),
                        field.getType()),
                x -> x != null && field != null);

    }

    /**
     * 返回 对象 指定 字段对象的  set() 方法函数对象
     * <p>如果操作失败 返回 null</p>
     *
     * @param obj       操作对象
     * @param fieldName 字段名
     * @return 指定字段的 set() 函数对象
     */
    public static Method fieldSetMethod(Object obj, String fieldName) {
        return fieldSetMethod(obj, getField(obj, fieldName));
    }


}
