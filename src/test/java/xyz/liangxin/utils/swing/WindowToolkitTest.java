package xyz.liangxin.utils.swing;

import com.formdev.flatlaf.FlatDarkLaf;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;

/**
 * <p> 描述
 *
 * @author liangxin
 * @version V1.0
 * @since 创建时间: 2022/6/28 17:44
 */
public class WindowToolkitTest {

    public static void main(String[] args) {

        FlatDarkLaf.setup();
        JFrame jFrame = new JFrame("test");
        jFrame.setPreferredSize(new Dimension(600, 400));
        jFrame.setVisible(true);
        jFrame.pack();
    }

    @Test
    public void IdeaUI() {
        JFrame jFrame = new JFrame("test");
        jFrame.setPreferredSize(new Dimension(600, 400));
        jFrame.pack();
        jFrame.setVisible(true);

    }


}