package xyz.liangxin.utils.core.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.liangxin.utils.core.ObjectUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * <p> 结果处理链, 其中一个处理结果出错, 则最后返回 null
 *
 * @author liangxin
 * @version V1.0
 * @since 创建时间: 2022/1/8 20:46
 */
public class ExecuteBuilder<T> {

    private static Logger logger = LoggerFactory.getLogger(ExecuteBuilder.class);

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
     * 执行
     *
     * @param function 处理函数
     * @param <R>      处理结果类型
     * @return 下一个处理器
     */
    public <R> ExecuteBuilder<R> next(Function<T, R> function) {
        R apply = null;
        if (ObjectUtils.nonNull(t)) {
            try {
                apply = function.apply(t);
            } catch (Exception e) {
                if (showError) {
                    logger.error(e.getMessage(), e);
                    showError = false;
                }
            }
        } else {
            if (showError) {
                logger.error("The value  is Null");
                showError = false;
            }
        }
        return new ExecuteBuilder<>(apply, showError);
    }

    /**
     * 将最后的处理结果返回
     *
     * @return 处理结果
     */
    public T result() {
        return t;
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
     *
     * @param t         处理数据
     * @param showError 如果出错, 是否捕获输出
     * @param <K>       数据类型
     * @return 处理器
     */
    public static <K> ExecuteBuilder<K> builder(K t, boolean showError) {
        return new ExecuteBuilder<>(t, showError);
    }


    public static void main(String[] args) {
        Map<String, Object> map1 = new HashMap<>(10);
        Map<String, Object> map2 = new HashMap<>(10);
        Map<String, Object> map3 = new HashMap<>(10);
        map1.put("1", map2);
        map2.put("2", map3);
        map3.put("3", 123);
        Map<String, Object> result = ExecuteBuilder.builder(map1)
                .next(x -> (Map<String, Object>) x.get("1"))
                .next(x -> (Map<String, Object>) x.get("2"))
                .next(x -> (Map<String, Object>) x.get("3"))
                .next(x -> (Map<String, Object>) x.get("1"))
                .next(x -> (Map<String, Object>) x.get("1"))
                .next(x -> (Map<String, Object>) x.get("1"))
                .next(x -> (Map<String, Object>) x.get("1"))
                .result();
        System.out.println(result);
    }


}
