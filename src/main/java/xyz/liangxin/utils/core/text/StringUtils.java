package xyz.liangxin.utils.core.text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.liangxin.utils.constant.text.CharConstant;

/**
 * 字符串工具包
 *
 * @author liangxin
 * @version V1.0
 * @Package xyz.liangxin.utils
 * @date 2021/11/7 18:40
 * @Description 操作字符串的各种工具函数
 */
public class StringUtils extends CharSequenceUtil {


    private static final Logger logger = LoggerFactory.getLogger(StringUtils.class);

    /**
     * 下划线格式字符串转换为驼峰格式字符串
     * 例:
     * user_name ==> userName
     * user_SEX ==> userSex
     *
     * @param underlineStr 待转换的 带下划线的 列名
     * @return 驼峰形式的字符串
     */
    public static String underlineToCamel(String underlineStr) {
        if (isEmpty(underlineStr)) {
            return "";
        }
        StringBuilder str = new StringBuilder(underlineStr.length());
        boolean upperCase = false;
        for (char c : underlineStr.toCharArray()) {
            if (c == CharConstant.CH_UNDERLINE) {
                upperCase = true;
                continue;
            }
            str.append(upperCase ? Character.toUpperCase(c) : Character.toLowerCase(c));
            upperCase = false;
        }
        return str.toString();
    }


    /**
     * 驼峰格式字符串转换为下划线格式字符串
     * 例:
     * <p>
     * userName ==> user_name
     * userSex ==> user_sex
     * userSEx ==> user_s_ex
     *
     * @param camelToUnderLine 待转换的 驼峰格式字符串
     * @return 出后的下划线格式字符串结果
     */
    public static String camelToUnderline(String camelToUnderLine) {
        if (isEmpty(camelToUnderLine)) {
            return "";
        }

        int len = camelToUnderLine.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = camelToUnderLine.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(CharConstant.CH_UNDERLINE);
                c = Character.toLowerCase(c);
            }

            sb.append(c);
        }
        return sb.toString();
    }

    // ------------------------------------------------------------------------ Blank

    /**
     * <p>如果对象是字符串是否为空白，空白的定义如下：</p>
     * <ol>
     *     <li>{@code null}</li>
     *     <li>空字符串：{@code ""}</li>
     *     <li>空格、全角空格、制表符、换行符，等不可见字符</li>
     * </ol>
     *
     * <p>例：</p>
     * <ul>
     *     <li>{@code StrUtil.isBlankIfStr(null)     // true}</li>
     *     <li>{@code StrUtil.isBlankIfStr("")       // true}</li>
     *     <li>{@code StrUtil.isBlankIfStr(" \t\n")  // true}</li>
     *     <li>{@code StrUtil.isBlankIfStr("abc")    // false}</li>
     * </ul>
     *
     * <p>注意：该方法与 {@link #isEmptyIfStr(Object)} 的区别是：
     * 该方法会校验空白字符，且性能相对于 {@link #isEmptyIfStr(Object)} 略慢。</p>
     *
     * @param obj 对象
     * @return 如果为字符串是否为空串
     * @see StringUtils#isBlank(CharSequence)
     * @since 3.3.0
     */
    public static boolean isBlankIfStr(Object obj) {
        if (null == obj) {
            return true;
        } else if (obj instanceof CharSequence) {
            return isBlank((CharSequence) obj);
        }
        return false;
    }
    // ------------------------------------------------------------------------ Empty

    /**
     * <p>如果对象是字符串是否为空串，空的定义如下：</p><br>
     * <ol>
     *     <li>{@code null}</li>
     *     <li>空字符串：{@code ""}</li>
     * </ol>
     *
     * <p>例：</p>
     * <ul>
     *     <li>{@code StrUtil.isEmptyIfStr(null)     // true}</li>
     *     <li>{@code StrUtil.isEmptyIfStr("")       // true}</li>
     *     <li>{@code StrUtil.isEmptyIfStr(" \t\n")  // false}</li>
     *     <li>{@code StrUtil.isEmptyIfStr("abc")    // false}</li>
     * </ul>
     *
     * <p>注意：该方法与 {@link #isBlankIfStr(Object)} 的区别是：该方法不校验空白字符。</p>
     *
     * @param obj 对象
     * @return 如果为字符串是否为空串
     * @since 3.3.0
     */
    public static boolean isEmptyIfStr(Object obj) {
        if (null == obj) {
            return true;
        } else if (obj instanceof CharSequence) {
            return 0 == ((CharSequence) obj).length();
        }
        return false;
    }


    /**
     * <p>检查指定的对象引用是否不为 Empty ( null || 空字符串 ) ，如果是则抛出自定义的 {@link NullPointerException} 。</p>
     * <p>该方法主要用于在具有多个参数的方法和构造函数中进行参数验证</p>
     *
     * @param str     检查无效性的字符串 引用
     * @param message 抛出 {@link NullPointerException} 使用的详细消息
     * @return str 如果不为 Empty
     */
    public static String requireNonEmpty(String str, String message) {
        if (isEmpty(str)) {
            throw new NullPointerException(message);
        }
        return str;
    }


}

