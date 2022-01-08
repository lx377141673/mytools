package xyz.liangxin.utils.swing.listener;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * <p> 组件 鼠标事件 工具类
 *
 * @author liangxin
 * @version V1.0
 * @since 创建时间: 2021/12/28 14:19
 */
public class MouseListenerUtil {
    private MouseListenerUtil() {
    }

    /**
     * 为组件 注册/添加 鼠标点击事件
     *
     * @param component 组件对象
     * @param predicate 事件执行的前置条件断定
     * @param consumer  点击时,需要 消费事件
     * @param append    是否为追加事件, 如果不为追加事件, 则表示, 不执行之前注册的事件, 默认为 true
     */
    public static void addClick(Component component, Predicate<MouseEvent> predicate, Consumer<MouseEvent> consumer, boolean append) {
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 冒泡执行, 执行之前注册的事件
                if (append) {
                    super.mousePressed(e);
                }
                consumerAndPredicate(predicate, consumer).accept(e);
            }
        });
    }

    /**
     * 组装 Predicate条件断定和 Consumer 执行事件
     *
     * @param predicate 条件断定
     * @param consumer  断定成功需要执行的事件
     * @return 返回组装后的 消费事件
     */
    private static Consumer<MouseEvent> consumerAndPredicate(Predicate<MouseEvent> predicate, Consumer<MouseEvent> consumer) {
        return e -> {
            if (predicate == null || predicate.test(e)) {
                consumer.accept(e);
            }
        };
    }


    /**
     * 为组件 注册/添加 鼠标单击事件,
     * 事件注册方式为追加, 即表示会执行之前注册的事件
     *
     * @param component 组件
     * @param predicate 事件执行的前置条件断定
     * @param consumer  点击消费事件
     */
    public static void addClick(Component component, Predicate<MouseEvent> predicate, Consumer<MouseEvent> consumer) {
        addClick(component, predicate, consumer, true);
    }

    /**
     * <p>为组件 注册/添加 鼠标单击事件,
     * <p>事件注册方式为追加, 即表示会执行之前注册的事件
     *
     * @param component 组件
     * @param consumer  点击消费事件
     */
    public static void addClick(Component component, Consumer<MouseEvent> consumer) {
        addClick(component, null, consumer);
    }

    /**
     * <p>为组件 注册/添加 鼠标双击事件,
     *
     * @param component 组件
     * @param predicate 事件执行的前置条件断定
     * @param consumer  点击消费事件
     * @param append    是否为追加事件, 如果不为追加事件, 则表示, 不执行之前注册的事件, 默认为 true
     */
    public static void addDbClick(Component component, Predicate<MouseEvent> predicate, Consumer<MouseEvent> consumer, boolean append) {
        addClick(component, e -> e.getClickCount() == 2, consumerAndPredicate(predicate, consumer), append);
    }

    /**
     * <p>为组件 注册/添加 鼠标双击事件,
     * <p>事件注册方式为追加, 即表示会执行之前注册的事件
     *
     * @param component 组件
     * @param predicate 事件执行的前置条件断定
     * @param consumer  点击消费事件
     */
    public static void addDbClick(Component component, Predicate<MouseEvent> predicate, Consumer<MouseEvent> consumer) {
        addDbClick(component, predicate, consumer, true);
    }


    /**
     * <p>为组件 注册/添加 鼠标双击事件,
     * <p>事件注册方式为追加, 即表示会执行之前注册的事件
     *
     * @param component 组件
     * @param consumer  点击消费事件
     */
    public static void addDbClick(Component component, Consumer<MouseEvent> consumer) {
        addDbClick(component, null, consumer);
    }


    /**
     * <p>为组件 注册/添加 鼠标左键单击事件,
     *
     * @param component 组件对象
     * @param predicate 事件执行的前置条件断定
     * @param consumer  点击时,需要 消费事件
     * @param append    是否为追加事件, 如果不为追加事件, 则表示, 不执行之前注册的事件, 默认为 true
     */
    public static void addLeftClick(Component component, Predicate<MouseEvent> predicate, Consumer<MouseEvent> consumer, boolean append) {
        addClick(component, e -> e.getButton() == 1, consumerAndPredicate(predicate, consumer), append);
    }

    /**
     * <p>为组件 注册/添加 鼠标左键单击事件,
     * <p>事件注册方式为追加, 即表示会执行之前注册的事件
     *
     * @param component 组件对象
     * @param predicate 事件执行的前置条件断定
     * @param consumer  点击时,需要 消费事件
     */
    public static void addLeftClick(Component component, Predicate<MouseEvent> predicate, Consumer<MouseEvent> consumer) {
        addLeftClick(component, predicate, consumer, true);
    }

    /**
     * <p>为组件 注册/添加 鼠标左键单击事件,
     * <p>事件注册方式为追加, 即表示会执行之前注册的事件
     *
     * @param component 组件对象
     * @param consumer  点击时,需要 消费事件
     */
    public static void addLeftClick(Component component, Consumer<MouseEvent> consumer) {
        addLeftClick(component, null, consumer);
    }


    /**
     * <p>为组件 注册/添加 鼠标左键双击事件,
     *
     * @param component 组件对象
     * @param predicate 事件执行的前置条件断定
     * @param consumer  点击时,需要 消费事件
     * @param append    是否为追加事件, 如果不为追加事件, 则表示, 不执行之前注册的事件, 默认为 true
     */
    public static void addLeftDbClick(Component component, Predicate<MouseEvent> predicate, Consumer<MouseEvent> consumer, boolean append) {
        addDbClick(component, e -> e.getButton() == 1, consumerAndPredicate(predicate, consumer), append);
    }

    /**
     * <p>为组件 注册/添加 鼠标左键双击事件,
     * <p>事件注册方式为追加, 即表示会执行之前注册的事件
     *
     * @param component 组件对象
     * @param predicate 事件执行的前置条件断定
     * @param consumer  点击时,需要 消费事件
     */
    public static void addLeftDbClick(Component component, Predicate<MouseEvent> predicate, Consumer<MouseEvent> consumer) {
        addLeftDbClick(component, predicate, consumer, true);
    }

    /**
     * <p>为组件 注册/添加 鼠标左键双击事件,
     * <p>事件注册方式为追加, 即表示会执行之前注册的事件
     *
     * @param component 组件对象
     * @param consumer  点击时,需要 消费事件
     */
    public static void addLeftDbClick(Component component, Consumer<MouseEvent> consumer) {
        addLeftDbClick(component, null, consumer);
    }


    /**
     * <p>为组件 注册/添加 鼠标右键单击事件,
     *
     * @param component 组件对象
     * @param predicate 事件执行的前置条件断定
     * @param consumer  点击时,需要 消费事件
     * @param append    是否为追加事件, 如果不为追加事件, 则表示, 不执行之前注册的事件, 默认为 true
     */
    public static void addRightClick(Component component, Predicate<MouseEvent> predicate, Consumer<MouseEvent> consumer, boolean append) {
        addClick(component, e -> e.getButton() == 3, consumerAndPredicate(predicate, consumer), append);
    }

    /**
     * <p>为组件 注册/添加 鼠标右键单击事件,
     * <p>事件注册方式为追加, 即表示会执行之前注册的事件
     *
     * @param component 组件对象
     * @param predicate 事件执行的前置条件断定
     * @param consumer  点击时,需要 消费事件
     */
    public static void addRightClick(Component component, Predicate<MouseEvent> predicate, Consumer<MouseEvent> consumer) {
        addRightClick(component, predicate, consumer, true);
    }

    /**
     * <p>为组件 注册/添加 鼠标右键单击事件,
     * <p>事件注册方式为追加, 即表示会执行之前注册的事件
     *
     * @param component 组件对象
     * @param consumer  点击时,需要 消费事件
     */
    public static void addRightClick(Component component, Consumer<MouseEvent> consumer) {
        addRightClick(component, null, consumer);
    }


    /**
     * <p>为组件 注册/添加 鼠标右键双击事件,
     *
     * @param component 组件对象
     * @param predicate 事件执行的前置条件断定
     * @param consumer  点击时,需要 消费事件
     * @param append    是否为追加事件, 如果不为追加事件, 则表示, 不执行之前注册的事件, 默认为 true
     */
    public static void addRightDbClick(Component component, Predicate<MouseEvent> predicate, Consumer<MouseEvent> consumer, boolean append) {
        addDbClick(component, e -> e.getButton() == 3, consumerAndPredicate(predicate, consumer), append);
    }

    /**
     * <p>为组件 注册/添加 鼠标右键双击事件,
     * <p>事件注册方式为追加, 即表示会执行之前注册的事件
     *
     * @param component 组件对象
     * @param predicate 事件执行的前置条件断定
     * @param consumer  点击时,需要 消费事件
     */
    public static void addRightDbClick(Component component, Predicate<MouseEvent> predicate, Consumer<MouseEvent> consumer) {
        addRightDbClick(component, predicate, consumer, true);
    }

    /**
     * <p>为组件 注册/添加 鼠标右键双击事件,
     * <p>事件注册方式为追加, 即表示会执行之前注册的事件
     *
     * @param component 组件对象
     * @param consumer  点击时,需要 消费事件
     */
    public static void addRightDbClick(Component component, Consumer<MouseEvent> consumer) {
        addRightDbClick(component, null, consumer);
    }
}
