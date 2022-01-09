package xyz.liangxin.utils.core.text;

import xyz.liangxin.utils.constant.text.CharConstant;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;


/**
 * 阿拉伯数字转换为中文大写数字
 */
public class ChineseNumber {

    private static final String[] BEFORE_SCALE = {"万", "仟", "佰", "拾", "亿", "仟", "佰", "拾", "万", "仟", "佰", "拾", ""};

    private static final String[] AFTER_SCALE = {"角", "分"};

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
        int doz = number.indexOf('.');
        if (doz > 0) {
            numbers = new String[2];
            numbers[0] = number.substring(0, doz);
            numbers[1] = number.substring(doz + 1);
            if (numbers[1].length() > 2) {
                numbers[1] = numbers[1].substring(0, 2);
            }
        } else {
            numbers = new String[1];
            numbers[0] = number;
        }

        if (numbers.length == 2 && numbers[1].indexOf(CharConstant.CH_DOT) != -1) {
            throw new NumberFormatException("数字格式错误!");
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
            // 如果后继的全部是零,则跳出
            boolean allZero = true;
            for (int j = i; j < length; j++) {
                if (number.charAt(j) != '0') {
                    allZero = false;
                    break;
                }
            }
            if (allZero) {
                boolean hasValue = false;
                for (int z = i; z >= 0; z--) {
                    if (number.charAt(z) != '0' && length - z <= 7 && length - z >= 5) {
                        hasValue = true;
                        break;
                    }
                }
                // 加万单位
                if ((length - i > 4 && length <= 8) ||(hasValue && length - i > 4)){
                    result.append(BEFORE_SCALE[8]);
                }

                // 加亿单位
                if (length - i >= 9) {
                    result.append(BEFORE_SCALE[4]);
                }
                break;
            }

            if (length < 9 && length - i == 5) {
                if (!"0".equals(digit) && isZero > 0) {
                    result.append(NUMBER_MAPPING.get("0"));
                }
                if ("0".equals(digit)) {
                    result.append(BEFORE_SCALE[8]);
                    if (isZero > 0) {
                        result.append(NUMBER_MAPPING.get("0"));
                    }
                    continue;
                }
            }
            if ("0".equals(digit) && length > 9 && length - i == 9) {
                result.append(BEFORE_SCALE[4]);
                continue;
            }

            if (isZero < 1 || !"0".equals(digit)) {
                if ("0".equals(digit)) {
                    if (length - i != 6 && length - i != 7) {
                        result.append(NUMBER_MAPPING.get(digit));
                    }
                } else {
                    result.append(NUMBER_MAPPING.get(digit));
                }

                if (!"0".equals(digit)) {
                    result.append(BEFORE_SCALE[BEFORE_SCALE.length - length + i]);
                }
            }
            if ("0".equals(digit)) {
                isZero++;
            } else {
                isZero = 0;
            }
        }
        return result;
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
        if (numbers.length > 2) {
            throw new NumberFormatException("数字格式错误!");
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
        return getChineseNumber(String.valueOf(number));
    }

    /**
     * 获取数字中文大写, 整数部分 最长13位
     *
     * @param number 数字字符串
     * @return 数字中文大写
     */
    public static String getChineseNumber(int number) {
        return getChineseNumber(String.valueOf(number));
    }


    /**
     * 获取数字中文大写, 整数部分 最长13位
     *
     * @param number 数字字符串
     * @return 数字中文大写
     */
    public static String getChineseNumber(long number) {
        return getChineseNumber(String.valueOf(number));
    }

    public static String getChineseNumber(float number) {
        return getChineseNumber(String.valueOf(number));
    }

    /**
     * 获取数字中文大写, 整数部分 最长13位
     *
     * @param number 数字字符串
     * @return 数字中文大写
     */
    public static String getChineseNumber(double number) {
        return getChineseNumber(String.valueOf(number));
    }

    public static void main(String[] args) {
        System.out.println(getChineseAmount(1994));
        System.out.println(getChineseAmount(1994.123456));
        System.out.println(getChineseAmount(19941115));
        System.out.println(getChineseAmount(10));
        System.out.println(getIntegerChineseNumber("10"));
        System.out.println(getDecimalChineseNumber("10"));
        System.out.println(getChineseNumber("1222234567003.124324213412"));
        System.out.println(getChineseNumber(11111));
        System.out.println(getChineseNumber(12321124124L));
    }

}
//Outputs

//壹仟玖佰玖拾肆圆整
//壹仟玖佰玖拾肆圆壹角贰分整
//壹仟玖佰玖拾肆万壹仟壹佰壹拾伍圆整
//壹拾圆整
//壹拾
//壹零
//壹万贰仟贰佰贰拾贰亿叁仟肆佰伍拾陆万柒仟零叁点壹贰肆叁贰肆贰壹叁肆壹贰
//壹万壹仟壹佰壹拾壹
//壹佰贰拾叁亿贰仟壹佰壹拾贰万肆仟壹佰贰拾肆