package xyz.liangxin.utils.io;

import xyz.liangxin.utils.constant.text.CharsetConstant;

/**
 * <p> 支持的编码类型
 *
 * @author liangxin
 * @version V1.0
 * @since 创建时间: 2022/1/9 16:45
 */
public class Encoding {

    private Encoding() {
    }


    /**
     * 中文编码: GB2312 , ANSI编码里的一种
     */
    public static final int GB2312 = 0;

    /**
     * 中文编码 : GBK,汉字内码扩展规范
     */
    public static final int GBK = 1;

    /**
     * 中文编码 : GB18030
     */
    public static final int GB18030 = 2;

    /**
     * 中文编码: HZ 简体中文
     */
    public static final int HZ = 3;

    /**
     * 中文编码: BIG5 , 繁体中文字符集
     */
    public static final int BIG5 = 4;

    /**
     * 中文编码: 台湾 CNS 11643 汉字标准, 繁体中文字符集
     */
    public static final int CNS11643 = 5;

    /**
     * 万国码: UTF-8, 包含中英文
     */
    public static final int UTF8 = 6;

    /**
     * 万国码: UTF-8T, 包含中英文
     */
    public static final int UTF8T = 7;
    /**
     * 万国码: UTF-8S, 包含中英文
     */
    public static final int UTF8S = 8;

    /**
     * Unicode 编码, 包含中英文
     */
    public static final int UNICODE = 9;

    /**
     * UnicodeT 编码, 包含中英文
     */
    public static final int UNICODET = 10;

    /**
     * UnicodeS 编码, 包含中英文
     */
    public static final int UNICODES = 11;

    /**
     * 中文国际标准
     */
    public static final int ISO2022CN = 12;
    /**
     * 中文国际标准
     */
    public static final int ISO2022CN_CNS = 13;
    /**
     * 中文国际标准
     */
    public static final int ISO2022CN_GB = 14;


    /**
     * 韩文编码
     */
    public static final int EUC_KR = 15;
    /**
     * 韩文编码
     */
    public static final int CP949 = 16;

    /**
     * 韩文国际标准
     */
    public static final int ISO2022KR = 17;

    /**
     * 韩文编码
     */
    public static final int JOHAB = 18;

    /**
     * 日文编码
     */
    public static final int SJIS = 19;
    /**
     * 日文编码
     */
    public static final int EUC_JP = 20;
    /**
     * 日文编码
     */
    public static final int ISO2022JP = 21;

    /**
     * ASCII 编码
     */
    public static final int ASCII = 22;

    /**
     * 其他, ISO8859-1
     */
    public static final int OTHER = 23;

    /**
     * 编码总数
     */
    public static final int TOTALTYPES = 24;


    /**
     * Java所理解的 编码名称
     */
    protected static final String[] JAVA_NAME;


    /**
     * 供人查看的 编码名称
     */
    protected static final String[] NICE_NAME;

    /**
     * 在HTML元标签的字符集参数中使用的字符集名称
     */
    protected static final String[] HTML_NAME;

    static {
        JAVA_NAME = new String[TOTALTYPES];
        NICE_NAME = new String[TOTALTYPES];
        HTML_NAME = new String[TOTALTYPES];
        init();
    }

    private static void init() {
        initEncodingJavaName();
        initEncodingHtmlName();
        initEncodingNiceName();
    }


    /**
     * 分配JAVA 编码名称
     */
    private static void initEncodingJavaName() {
        JAVA_NAME[GB2312] = CharsetConstant.GB_2312;
        JAVA_NAME[GBK] = CharsetConstant.GBK;
        JAVA_NAME[GB18030] = CharsetConstant.GB_18030;
        // jav 不支持 HZ, 所以此处分配为 ASCII
        JAVA_NAME[HZ] = CharsetConstant.HZ;
        JAVA_NAME[ISO2022CN_GB] = CharsetConstant.ISO2022_CN_GB;
        JAVA_NAME[BIG5] = CharsetConstant.BIG5;
        JAVA_NAME[CNS11643] = CharsetConstant.EUC_TW;
        JAVA_NAME[ISO2022CN_CNS] = CharsetConstant.ISO2022_CN_CNS;
        JAVA_NAME[ISO2022CN] = CharsetConstant.ISO2022_CN;
        JAVA_NAME[UTF8] = CharsetConstant.UTF_8;
        JAVA_NAME[UTF8T] = CharsetConstant.UTF_8;
        JAVA_NAME[UTF8S] = CharsetConstant.UTF_8;
        JAVA_NAME[UNICODE] = CharsetConstant.UNICODE;
        JAVA_NAME[UNICODET] = CharsetConstant.UNICODE;
        JAVA_NAME[UNICODES] = CharsetConstant.UNICODE;
        JAVA_NAME[EUC_KR] = CharsetConstant.EUC_KR;
        JAVA_NAME[CP949] = CharsetConstant.CP949;
        JAVA_NAME[ISO2022KR] = CharsetConstant.ISO2022_KR;
        JAVA_NAME[JOHAB] = CharsetConstant.JOHAB;
        JAVA_NAME[SJIS] = CharsetConstant.SHIFT_JIS;
        JAVA_NAME[EUC_JP] = CharsetConstant.EUC_JP;
        JAVA_NAME[ISO2022JP] = CharsetConstant.ISO2022_JP;
        JAVA_NAME[ASCII] = CharsetConstant.ASCII;
        JAVA_NAME[OTHER] = CharsetConstant.ISO_8859_1;
    }

    /**
     * 分配HTML编码名称
     */
    private static void initEncodingHtmlName() {
        HTML_NAME[GB2312] = "GB2312";
        HTML_NAME[GBK] = "GBK";
        HTML_NAME[GB18030] = "GB18030";
        HTML_NAME[HZ] = "HZ-GB-2312";
        HTML_NAME[ISO2022CN_GB] = "ISO-2022-CN-EXT";
        HTML_NAME[BIG5] = "BIG5";
        HTML_NAME[CNS11643] = "EUC-TW";
        HTML_NAME[ISO2022CN_CNS] = "ISO-2022-CN-EXT";
        HTML_NAME[ISO2022CN] = "ISO-2022-CN";
        HTML_NAME[UTF8] = "UTF-8";
        HTML_NAME[UTF8T] = HTML_NAME[UTF8];
        HTML_NAME[UTF8S] = HTML_NAME[UTF8];
        HTML_NAME[UNICODE] = "UTF-16";
        HTML_NAME[UNICODET] = HTML_NAME[UNICODE];
        HTML_NAME[UNICODES] = HTML_NAME[UNICODE];
        HTML_NAME[EUC_KR] = "EUC-KR";
        HTML_NAME[CP949] = "x-windows-949";
        HTML_NAME[ISO2022KR] = "ISO-2022-KR";
        HTML_NAME[JOHAB] = "x-Johab";
        HTML_NAME[SJIS] = "Shift_JIS";
        HTML_NAME[EUC_JP] = "EUC-JP";
        HTML_NAME[ISO2022JP] = "ISO-2022-JP";
        HTML_NAME[ASCII] = "ASCII";
        HTML_NAME[OTHER] = "ISO8859-1";

    }

    /**
     * 赋值人类可读名称
     */
    private static void initEncodingNiceName() {
        NICE_NAME[GB2312] = "GB-2312";
        NICE_NAME[GBK] = "GBK";
        NICE_NAME[GB18030] = "GB18030";
        NICE_NAME[HZ] = "HZ";
        NICE_NAME[ISO2022CN_GB] = "ISO2022CN-GB";
        NICE_NAME[BIG5] = "Big5";
        NICE_NAME[CNS11643] = "CNS11643";
        NICE_NAME[ISO2022CN_CNS] = "ISO2022CN-CNS";
        NICE_NAME[ISO2022CN] = "ISO2022 CN";
        NICE_NAME[UTF8] = "UTF-8";
        NICE_NAME[UTF8T] = "UTF-8 (Trad)";
        NICE_NAME[UTF8S] = "UTF-8 (Simp)";
        NICE_NAME[UNICODE] = "Unicode";
        NICE_NAME[UNICODET] = "Unicode (Trad)";
        NICE_NAME[UNICODES] = "Unicode (Simp)";
        NICE_NAME[EUC_KR] = "EUC-KR";
        NICE_NAME[CP949] = "CP949";
        NICE_NAME[ISO2022KR] = "ISO 2022 KR";
        NICE_NAME[JOHAB] = "Johab";
        NICE_NAME[SJIS] = "Shift-JIS";
        NICE_NAME[EUC_JP] = "EUC-JP";
        NICE_NAME[ISO2022JP] = "ISO 2022 JP";
        NICE_NAME[ASCII] = "ASCII";
        NICE_NAME[OTHER] = "OTHER";
    }
}

