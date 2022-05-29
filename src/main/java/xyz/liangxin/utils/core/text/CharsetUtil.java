package xyz.liangxin.utils.core.text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.liangxin.utils.constant.text.CharsetConstant;

import java.nio.charset.Charset;

/**
 * 字符集工具类
 *
 * @author liangxin
 * @version V1.0
 * @date 2021/11/9 20:04
 */
public class CharsetUtil extends CharsetConstant {
    private static final Logger logger = LoggerFactory.getLogger(CharsetUtil.class);

    private CharsetUtil() {
    }

    /**
     * 显示当前系统支持的所有字符集信息
     */
    public static void showCharsetName() {
        Charset.availableCharsets().keySet().forEach(System.out::println);
    }

}
