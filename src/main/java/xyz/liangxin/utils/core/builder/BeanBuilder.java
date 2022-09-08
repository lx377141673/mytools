package xyz.liangxin.utils.core.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.liangxin.utils.core.functions.BiConsumer2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 通用的 Bean  Builder 模式构建器
 *
 * @author liangxin
 * @version V1.0
 * @date 2021/4/29 15:40
 */
public class BeanBuilder<T> {
    private static final Logger logger = LoggerFactory.getLogger(BeanBuilder.class);
    /**
     * 实例化构造器
     */
    private final Supplier<T> instantiated;

    /**
     * 属性编辑修改器
     */
    private final List<Consumer<T>> modifiers = new ArrayList<>();

    private BeanBuilder(Supplier<T> instantiated) {
        this.instantiated = instantiated;
    }

    /**
     * 构建 BeanBuilder
     *
     * @param instantiated Bean 实例化 构造器
     * @param <T>          Bean类型
     * @return 指定 Bean 生成器
     */
    public static <T> BeanBuilder<T> of(Supplier<T> instantiated) {
        return new BeanBuilder<>(instantiated);
    }


    /**
     * 实例逻辑函数执行, 无参数
     *
     * @param consumer 实例 执行的函数
     * @return 指定 Bean 生成器
     */
    public BeanBuilder<T> with(Consumer<T> consumer) {
        modifiers.add(consumer);
        return this;
    }

    /**
     * 实例参数注入
     *
     * @param consumer 实例 参数注入函数
     * @param e        注入的参数值
     * @param <E1>     参数类型
     * @return 指定 Bean 生成器
     */
    public <E1> BeanBuilder<T> with(BiConsumer<T, E1> consumer, E1 e) {
        Consumer<T> c = instance -> consumer.accept(instance, e);
        modifiers.add(c);
        return this;
    }


    /**
     * 实例参数注入
     *
     * @param consumer 实例 参数注入函数
     * @param e1       注入的参数值 1
     * @param e2       注入的参数值 2
     * @param <E1>     参数1 类型
     * @param <E2>     参数2 类型
     * @return 指定 Bean 生成器
     */
    public <E1, E2> BeanBuilder<T> with(BiConsumer2<T, E1, E2> consumer, E1 e1, E2 e2) {
        Consumer<T> c = instance -> consumer.accept(instance, e1, e2);
        modifiers.add(c);
        return this;
    }

    /**
     * 实例参数注入
     *
     * @param consumer 实例 参数注入函数
     * @param e1       注入的参数值 1
     * @param e2       注入的参数值 2
     * @param <E1>     参数1 类型
     * @param <E2>     参数2 类型
     * @return 指定 Bean 生成器
     */
    public <E1, E2, E3> BeanBuilder<T> with(BiConsumer3<T, E1, E2, E3> consumer, E1 e1, E2 e2, E3 e3) {
        Consumer<T> c = instance -> consumer.accept(instance, e1, e2, e3);
        modifiers.add(c);
        return this;
    }

    /**
     * 实例消费生成
     *
     * @return 生成的 Bean
     */
    public T builder() {
        // 获取实例
        T bean = instantiated.get();
        Objects.requireNonNull(bean, "当前实例不应该为 null");
        // 为 ben 消费 参数注入方法
        modifiers.forEach(modifier -> modifier.accept(bean));
        // 清除 保存的 参数注入消费函数
        modifiers.clear();
        // 返回 Bean
        return bean;
    }

    /**
     * 用于 三个参数值的消费 接口
     *
     * @param <T>  bean 类型
     * @param <E1> 参数1 类型
     * @param <E2> 参数2 类型
     * @param <E3> 参数3 类型
     * @author liangxin
     */
    @FunctionalInterface
    public interface BiConsumer3<T, E1, E2, E3> {
        /**
         * 三个参数的消费函数
         *
         * @param t  bean 实例
         * @param e1 参数1
         * @param e2 参数2
         * @param e3 参数3
         */
        void accept(T t, E1 e1, E2 e2, E3 e3);
    }

}

