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
     * <p>单字节编码, 兼容 ASCII 范围是 0x00 - 0xFF
     * 0x00-0x7F之间完全和ASCII一致，
     * 0x80-0x9F之间是控制字符，0xA0-0xFF之间是文字符号。
     */
    public static final String ISO_8859_1 = "ISO-8859-1";


    /**
     * ASCII
     * <p>
     * 美国信息交换标准代码 , 共定义了128个字符
     * </p>
     */
    public static final String ASCII = "ASCII";
    /**
     * Unicode
     * <p>
     * 万国码
     * </p>
     */
    public static final String UNICODE = "Unicode";

    /**
     * UTF-8
     * <p>
     * 可变长度字符编码 , 使用1、2、3字节等进行编码
     * 万国码, 包含中文
     * </p>
     */
    public static final String UTF_8 = "UTF-8";
    /**
     * UTF-16
     * <p>
     * 可变长度字符编码 , 使用2,4字节等进行编码
     * 万国码, 包含中文
     * </p>
     */
    public static final String UTF_16 = "UTF-16";

    /**
     * GBK
     * <p>
     * 汉字国标码,  ISO类型编码,双字节编码
     * </p>
     */
    public static final String GBK = "GBK";


    /**
     * GB2312
     * <p>中文编码,</p>
     */
    public static final String GB_2312 = "GB2312";
    /**
     * GB18030
     * <p>中文编码</p>
     */
    public static final String GB_18030 = "GB18030";
    /**
     * java 不支持 HZ, 所以此处分配为 ASCII
     */
    public static final String HZ = "ASCII";
    /**
     * ISO2022CN_GB
     * <p>中文编码</p>
     */
    public static final String ISO2022_CN_GB = "ISO2022CN_GB";
    /**
     * ISO2022CN
     * <p>中文编码</p>
     */
    public static final String ISO2022_CN = "ISO2022CN";
    /**
     * ISO2022CN_CNS
     * <p>中文编码</p>
     */
    public static final String ISO2022_CN_CNS = "ISO2022CN_CNS";

    /**
     * BIG5
     * <p>中文编码, 繁体中文</p>
     */
    public static final String BIG5 = "BIG5";
    /**
     * EUC-TW
     * <p>中文编码, 繁体中文</p>
     */
    public static final String EUC_TW = "EUC-TW";


    /**
     * ISO2022KR
     * <p>韩文编码</p>
     */
    public static final String ISO2022_KR = "ISO2022KR";

    /**
     * EUC_KR
     * <p>韩文编码</p>
     */
    public static final String EUC_KR = "EUC_KR";
    /**
     * Johab
     * <p>韩文编码</p>
     */
    public static final String JOHAB = "Johab";

    /**
     * ISO2022JP
     * <p>日文编码</p>
     */
    public static final String ISO2022_JP = "ISO2022JP";

    /**
     * SJIS
     * <p>日文编码</p>
     */
    public static final String SHIFT_JIS = "SJIS";
    /**
     * EUC_JP
     * <p>日文编码</p>
     */
    public static final String EUC_JP = "EUC_JP";


    /**
     * MS949
     */
    public static final String CP949 = "MS949";


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
