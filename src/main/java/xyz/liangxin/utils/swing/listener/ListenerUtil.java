package xyz.liangxin.utils.swing.listener;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

/**
 * <p> 组件事件工具类
 *
 * @author liangxin
 * @version V1.0
 * @since 创建时间: 2021/12/28 14:16
 */
public class ListenerUtil {

    /**
     * 鼠标左键单击事件
     * @param component 组件
     * @param consumer 事件消费
     */
    public static void addClick(Component component, Consumer<MouseEvent> consumer){
        MouseListenerUtil.addLeftClick(component,consumer);
    }

    /**
     * 鼠标左键双击事件
     * @param component 组件
     * @param consumer 事件消费
     */
    public static void addDbClick(Component component, Consumer<MouseEvent> consumer){
        MouseListenerUtil.addLeftDbClick(component,consumer);

    }
}
