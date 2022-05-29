package xyz.liangxin.utils.core.text;

import xyz.liangxin.utils.constant.text.CharConstant;
import xyz.liangxin.utils.core.ObjectUtils;
import xyz.liangxin.utils.core.number.NumberUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;


/**
 * 阿拉伯数字转换为中文大写数字
 */
public class ChineseNumber {

    /**
     * 中文数字进制单位
     */
    private static final String[] BEFORE_SCALE = {"万", "仟", "佰", "拾", "亿", "仟", "佰", "拾", "万", "仟", "佰", "拾", ""};

    /**
     * 中文金额 单位
     */
    private static final String[] AFTER_SCALE = {"角", "分"};

    /**
     * 阿拉伯数字 对照 中文数字 字典
     */
    private static final Map<String, String> NUMBER_MAPPING = new HashMap<>();

    static {
        NUMBER_MAPPING.put("0", "零");
        NUMBER_MAPPING.put("1", "壹");
        NUMBER_MAPPING.put("2", "贰");
        NUMBER_MAPPING.put("3", "叁");
        NUMBER_MAPPING.put("4", "肆");
        NUMBER_MAPPING.put("5", "伍");
        NUMBER_MAPPING.put("6", "陆");
        NUMBER_MAPPING.put("7", "柒");
        NUMBER_MAPPING.put("8", "捌");
        NUMBER_MAPPING.put("9", "玖");
    }


    /**
     * 获取 中文大写金额
     *
     * @param number  数字字符串
     * @param unit    单位 , 默认 "圆"
     * @param postfix 后缀, 默认 "整"
     * @return 中文大写金额
     */
    public static String getChineseAmount(String number, String unit, String postfix) {
        String[] numbers;
        int doz = number.indexOf(CharConstant.CH_DOT);
        if (doz > 0) {
            numbers = number.split("\\.");
            numbers[1] = numbers[1].length() > 2 ? numbers[1].substring(0, 2) : numbers[1];
        } else {
            numbers = new String[]{number};
        }

        if ((numbers.length == 2 && numbers[1].indexOf(CharConstant.CH_DOT) != -1) || numbers[0].length() > 13) {
            throw new NumberFormatException("数字格式错误");
        }

        StringBuilder result = getIntegerChineseNumber(numbers[0]);
        result.append(unit == null ? "圆" : result.append(unit));
        if (numbers.length == 1) {
            result.append(postfix == null ? "整" : result.append(postfix));
            return result.toString();
        }

        /*
          只取前两位
         */
        int length = Math.min(numbers[1].length(), 2);
        for (int j = 0; j < length; j++) {
            if (numbers[1].charAt(j) == '0') {
                continue;
            }
            result.append(NUMBER_MAPPING.get(String.valueOf(numbers[1].charAt(j))));
            result.append(AFTER_SCALE[j]);
        }
        result.append(postfix == null ? "整" : postfix);

        return result.toString();
    }

    /**
     * 获取 中文大写金额
     *
     * @param number 数字字符串
     * @return 中文大写金额
     */
    public static String getChineseAmount(String number) {
        return getChineseAmount(number, null, null);
    }


    /**
     * 获取 中文大写金额
     *
     * @param number  数字字符串
     * @param unit    单位 , 默认 "圆"
     * @param postfix 后缀, 默认 "整"
     * @return 中文大写金额
     */
    public static String getChineseAmount(int number, String unit, String postfix) {
        return getChineseAmount(String.valueOf(number), unit, postfix);
    }

    /**
     * 获取 中文大写金额
     *
     * @param number 数字字符串
     * @return 中文大写金额
     */
    public static String getChineseAmount(int number) {
        return getChineseAmount(number, null, null);
    }


    /**
     * 获取 中文大写金额
     *
     * @param number  数字字符串
     * @param unit    单位 , 默认 "圆"
     * @param postfix 后缀, 默认 "整"
     * @return 中文大写金额
     */
    public static String getChineseAmount(long number, String unit, String postfix) {
        return getChineseAmount(String.valueOf(number), unit, postfix);
    }


    /**
     * 获取 中文大写金额
     *
     * @param number 数字字符串
     * @return 中文大写金额
     */
    public static String getChineseAmount(long number) {
        return getChineseAmount(number, null, null);
    }

    /**
     * 获取 中文大写金额
     *
     * @param number  数字字符串
     * @param unit    单位 , 默认 "圆"
     * @param postfix 后缀, 默认 "整"
     * @return 中文大写金额
     */
    public static String getChineseAmount(double number, String unit, String postfix) {
        DecimalFormat f = (DecimalFormat) NumberFormat.getInstance();
        f.applyLocalizedPattern("#.##");
        return getChineseAmount(f.format(number), unit, postfix);
    }

    /**
     * 获取 中文大写金额
     *
     * @param number 数字字符串
     * @return 中文大写金额
     */
    public static String getChineseAmount(double number) {
        return getChineseAmount(number, null, null);
    }


    /**
     * 获取整数 中文, 最长十三位
     *
     * @param number 整数字符串
     * @return 整数中文
     */
    private static StringBuilder getIntegerChineseNumber(String number) {
        int length = number.length();
        int isZero = 0;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            String digit = String.valueOf(number.charAt(i));
            int afterLength = length - i;
            if (ObjectUtils.nonNull(afterAllZero(i, number, result, length, afterLength))) {
                return result;
            }
            boolean nowZero = "0".equals(digit);
            if (nowZero) {
                if (nowZeroDispose(digit, length, result, afterLength, isZero)) continue;
                isZero++;
            } else {
                if (length < 9 && afterLength == 5 && isZero > 0) {
                    // 当前不是 0 且 之前的是 0 ; 数字范围在 1亿 ~ 一万之间 需要增加一个 零
                    result.append(NUMBER_MAPPING.get("0"));
                }
                result.append(NUMBER_MAPPING.get(digit));
                result.append(BEFORE_SCALE[BEFORE_SCALE.length - length + i]);
                isZero = 0;
            }
        }

        return result;
    }


    /**
     * 当前数字为 0 零 的处理函数
     *
     * @param digit       当前数字字符串
     * @param result      结果容器
     * @param length      总长度
     * @param afterLength 剩余长度
     * @param isZero      上一次处理的 是否为 0 标识
     * @return 处理后是否需要 跳过当前循环 (continue)
     */
    private static boolean nowZeroDispose(String digit, int length, StringBuilder result, int afterLength, int isZero) {
        if (length != 9 && (afterLength == 5 || afterLength == 9)) {
            if (afterLength == 5) {
                // 如果当前为 5位数, 增加 万 单位
                result.append(BEFORE_SCALE[8]);
                if (isZero > 0) {
                    // 如果之前为 0  则在 1万~ 1 之间 添加一个 0
                    result.append(NUMBER_MAPPING.get("0"));
                }
            } else {
                result.append(BEFORE_SCALE[4]);
            }
            return true;
        }
        if (isZero < 1 && afterLength != 6 && afterLength != 7) {
//            为不同时期的 首次出现 0 添加对应的 中文映射
            // 排除 十万为何百万位
            result.append(NUMBER_MAPPING.get(digit));
        }
        return false;
    }

    /**
     * 之后的数字都是 0 的处理函数
     *
     * @param i           当前操作的 字符索引位置
     * @param number      操作的字符串
     * @param result      结果容器
     * @param length      总长度
     * @param afterLength 剩余长度
     * @return 处理结果, 如果不为空, 则直接返回, 否则继续下一步
     */
    private static StringBuilder afterAllZero(int i, String number, StringBuilder result, int length, int afterLength) {

        String zeroStr = "00000000000000000000";
        boolean zeroAll = zeroStr.substring(0, afterLength).equals(number.substring(i, length));
        if (zeroAll) {
            // 加万单位
            if (afterLength > 4 && length <= 8) {
                result.append(BEFORE_SCALE[8]);
            } else if (afterLength >= 9) {
                // 加亿单位
                result.append(BEFORE_SCALE[4]);
            }
            return result;
        }
        return null;
    }


    /**
     * 获取小数部分 中文
     *
     * @param number 小数部分
     * @return 数字中文
     */
    private static StringBuilder getDecimalChineseNumber(String number) {
        StringBuilder result = new StringBuilder();
        for (char c : number.toCharArray()) {
            result.append(NUMBER_MAPPING.get(String.valueOf(c)));
        }
        return result;
    }


    /**
     * 获取数字中文大写, 整数部分 最长13位
     *
     * @param number 数字字符串
     * @return 数字中文大写
     */
    public static String getChineseNumber(String number) {
        String[] numbers = number.split("\\.");
        if (numbers.length > 2 || numbers[0].length() > 13) {
            throw new NumberFormatException("整数部分不能大于 13 ;  即 必须小于 十万亿");
        }
        StringBuilder result = getIntegerChineseNumber(numbers[0]);
        if (numbers.length == 1) {
            return result.toString();
        }
        int length = numbers[1].length();
        if (length > 0) {
            result.append("点");
        }
        result.append(getDecimalChineseNumber(numbers[1]));
        return result.toString();
    }


    /**
     * 获取数字中文大写, 整数部分 最长13位
     *
     * @param number 数字字符串
     * @return 数字中文大写
     */
    public static String getChineseNumber(short number) {

        return getChineseNumber(NumberUtils.toBigDecimal(number).toPlainString());
    }

    /**
     * 获取数字中文大写, 整数部分 最长13位
     *
     * @param number 数字字符串
     * @return 数字中文大写
     */
    public static String getChineseNumber(int number) {
        return getChineseNumber(NumberUtils.toBigDecimal(number).toPlainString());
    }


    /**
     * 获取数字中文大写, 整数部分 最长13位
     *
     * @param number 数字字符串
     * @return 数字中文大写
     */
    public static String getChineseNumber(long number) {
        return getChineseNumber(NumberUtils.toBigDecimal(number).toPlainString());
    }

    public static String getChineseNumber(float number) {
        return getChineseNumber(NumberUtils.toBigDecimal(number).toPlainString());
    }

    /**
     * 获取数字中文大写, 整数部分 最长13位
     *
     * @param number 数字字符串
     * @return 数字中文大写
     */
    public static String getChineseNumber(double number) {
        return getChineseNumber(NumberUtils.toBigDecimal(number).toPlainString());
    }


}
