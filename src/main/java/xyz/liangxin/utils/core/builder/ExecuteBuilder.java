package xyz.liangxin.utils.core.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.liangxin.utils.core.ObjectUtils;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * <p> 结果处理链, 其中一个处理结果出错, 则最后返回 null
 *
 * @author liangxin
 * @version V1.0
 * @since 创建时间: 2022/1/8 20:46
 */
public class ExecuteBuilder<T> {

    private static final Logger logger = LoggerFactory.getLogger(ExecuteBuilder.class);


    /**
     * 处理数据
     */
    private final T t;

    /**
     * 是否输出异常标识, 默认false
     */
    private boolean showError = false;

    private ExecuteBuilder(T t) {
        this.t = t;
    }

    public ExecuteBuilder(T t, boolean showError) {
        this.t = t;
        this.showError = showError;
    }


    /**
     * 静态构建 处理器, 默认不捕获输出异常
     *
     * @param t   处理数据
     * @param <K> 数据类型
     * @return 处理器
     */
    public static <K> ExecuteBuilder<K> builder(K t) {
        return new ExecuteBuilder<>(t);
    }

    /**
     * 静态构建 处理器
     * <p> 如果 showError 为 true, 则 使用日志 捕获输出</p>
     * <p> showError 默认值为 false</p>
     *
     * @param t         处理数据
     * @param showError 异常输出选项
     * @param <K>       数据类型
     * @return 处理器
     */
    public static <K> ExecuteBuilder<K> builder(K t, boolean showError) {
        return new ExecuteBuilder<>(t, showError);
    }


    /**
     * 执行下一次处理,
     * <p>根据 处理stop 约束, 判断 是否执行下一次处理</p>
     * <p>如果不执行下一次处理, 则返回 包含, 默认值函数执行结果 的 结果处理器</p>
     *
     * @param function     处理函数
     * @param defaultValue 默认值函数
     * @param stop         处理链停止条件
     * @param <R>          返回值类型
     * @return 处理器
     */
    public <R> ExecuteBuilder<R> next(Function<T, R> function, Supplier<R> defaultValue, Predicate<T> stop) {
        if (stop.test(t)) {
            return builder(defaultValue.get());
        }
        R apply = null;
        try {
            apply = function.apply(t);
        } catch (Exception e) {
            if (showError) {
                logger.error(e.getMessage(), e);
                showError = false;
            }
        }
        return builder(ObjectUtils.getOrDefault(apply, defaultValue), showError);
    }


    /**
     * 执行下一次处理,
     * <p>根据 处理stop 约束, 判断 是否执行下一次处理</p>
     * <p>如果不执行下一次处理, 则返回 包含, 默认值的处理器</p>
     *
     * @param function     处理函数
     * @param stop         处理链停止条件
     * @param defaultValue 默认值
     * @param <R>          返回值类型
     * @return 处理器
     */
    public <R> ExecuteBuilder<R> next(Function<T, R> function, Predicate<T> stop, R defaultValue) {
        return next(function, () -> defaultValue, stop);
    }


    /**
     * 执行下一次处理,
     * <p>根据 处理stop 约束, 判断 是否执行下一次处理</p>
     * <p>如果不执行下一次处理, 则返回 包含, 默认值为 null 的处理器</p>
     *
     * @param function 处理函数
     * @param stop     处理链停止条件
     * @param <R>      返回值类型
     * @return 处理器
     */
    public <R> ExecuteBuilder<R> next(Function<T, R> function, Predicate<T> stop) {
        return next(function, stop, null);
    }


    /**
     * 执行下一次处理,
     * <p>根据 结果值是否为 null , 判断 是否执行下一次处理</p>
     * <p>如果不执行下一次处理, 则返回 包含, 默认值为 null 的处理器</p>
     *
     * @param function 处理函数
     * @param <R>      返回值类型
     * @return 处理器
     */
    public <R> ExecuteBuilder<R> next(Function<T, R> function) {
        return next(function, ObjectUtils::isNull);
    }

    /**
     * 将最后的处理结果返回
     *
     * @return 处理结果
     */
    public T result() {
        return t;
    }

}
