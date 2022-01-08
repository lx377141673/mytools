package xyz.liangxin.utils.constant.text;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

/**
 * <p> 字符编码常量
 *
 * @author liangxin
 * @version V1.0
 * @since 创建时间: 2022/1/6 18:24
 */
public class CharsetConstant {
    /**
     * ISO-8859-1
     */
    public static final String ISO_8859_1 = "ISO-8859-1";
    /**
     * UTF-8
     */
    public static final String UTF_8 = "UTF-8";
    /**
     * GBK
     */
    public static final String GBK = "GBK";

    /**
     * ISO-8859-1
     */
    public static final Charset CHARSET_ISO_8859_1 = StandardCharsets.ISO_8859_1;

    /**
     * US_ASCII
     */
    public static final Charset CHARSET_US_ASCII = StandardCharsets.US_ASCII;
    /**
     * UTF-8
     */
    public static final Charset CHARSET_UTF_8 = StandardCharsets.UTF_8;

    /**
     * UTF_16
     */
    public static final Charset CHARSET_UTF_16 = StandardCharsets.UTF_16;
    /**
     * GBK
     */
    public static final Charset CHARSET_GBK;

    static {
        // 避免不支持GBK的系统中运行报错
        Charset charsetGbk = null;
        try {
            charsetGbk = Charset.forName(GBK);
        } catch (UnsupportedCharsetException e) {
            //ignore
        }
        CHARSET_GBK = charsetGbk;
    }

}
