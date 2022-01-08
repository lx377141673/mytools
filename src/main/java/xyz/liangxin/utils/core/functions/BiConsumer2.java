package xyz.liangxin.utils.core.functions;

/**
 * 用于 三个参数值的消费 接口
 *
 * @author liangxin
 * @param <T>  bean 类型
 * @param <E1> 参数1 类型
 * @param <E2> 参数2 类型
 */
@FunctionalInterface
public interface BiConsumer2<T, E1, E2> {
    /**
     * 三个参数的消费函数
     *
     * @param t  bean 实例
     * @param e1 参数1
     * @param e2 参数2
     */
    void accept(T t, E1 e1, E2 e2);
}
