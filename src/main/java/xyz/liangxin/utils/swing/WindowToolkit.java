package xyz.liangxin.utils.swing;

import java.awt.*;
import java.io.File;

/**
 * <p> 窗口统一工具包
 *
 * @author liangxin
 * @version V1.0
 * @since 创建时间: 2021/12/27 17:12
 */
public class WindowToolkit {


    public static final  Toolkit toolkit = Toolkit.getDefaultToolkit();

    /**
     * 获取Window窗口 宽高
     *
     * @return Window窗口宽高
     */
    public static Dimension getWindowDimension() {
        Dimension screenSize = toolkit.getScreenSize();
        int w = screenSize.width;
        int h = screenSize.height;
        Insets screenInsets = toolkit.getScreenInsets(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());

        w = w - (screenInsets.left + screenInsets.right);
        h = h - (screenInsets.top + screenInsets.bottom);
        return new Dimension(w, h);
    }

    /**
     * 构建 Image 对象
     * @param file 文件对象
     * @return Image 对象
     */
    public static Image imageBuilder(File file) {
        return imageBuilder(file.getAbsolutePath());
    }

    /**
     * 构建 Image 对象
     * @param file 文件地址
     * @return Image 对象
     */
    public static Image imageBuilder(String file) {
        return WindowToolkit.toolkit.getImage(file);
    }

}
