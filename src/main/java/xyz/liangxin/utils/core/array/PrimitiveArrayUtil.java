package xyz.liangxin.utils.core.array;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * 原始类型数组工具类
 *
 * @author liangxin
 * @version V1.0
 * @date 2021/11/9 20:50
 */
public class PrimitiveArrayUtil {
    protected PrimitiveArrayUtil() {
    }

    /**
     * 数组中元素未找到的下标，值为-1
     */
    public static final int INDEX_NOT_FOUND = -1;

    // ---------------------------------------------------------------------- isEmpty

    /**
     * 数组是否为空
     *
     * @param array 数组
     * @return 是否为空
     */
    public static boolean isEmpty(long[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 数组是否为空
     *
     * @param array 数组
     * @return 是否为空
     */
    public static boolean isEmpty(int[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 数组是否为空
     *
     * @param array 数组
     * @return 是否为空
     */
    public static boolean isEmpty(short[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 数组是否为空
     *
     * @param array 数组
     * @return 是否为空
     */
    public static boolean isEmpty(char[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 数组是否为空
     *
     * @param array 数组
     * @return 是否为空
     */
    public static boolean isEmpty(byte[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 数组是否为空
     *
     * @param array 数组
     * @return 是否为空
     */
    public static boolean isEmpty(double[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 数组是否为空
     *
     * @param array 数组
     * @return 是否为空
     */
    public static boolean isEmpty(float[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 数组是否为空
     *
     * @param array 数组
     * @return 是否为空
     */
    public static boolean isEmpty(boolean[] array) {
        return array == null || array.length == 0;
    }

    // ---------------------------------------------------------------------- isNotEmpty

    /**
     * 数组是否为非空
     *
     * @param array 数组
     * @return 是否为非空
     */
    public static boolean isNotEmpty(long[] array) {
        return Boolean.FALSE == isEmpty(array);
    }

    /**
     * 数组是否为非空
     *
     * @param array 数组
     * @return 是否为非空
     */
    public static boolean isNotEmpty(int[] array) {
        return Boolean.FALSE == isEmpty(array);
    }

    /**
     * 数组是否为非空
     *
     * @param array 数组
     * @return 是否为非空
     */
    public static boolean isNotEmpty(short[] array) {
        return Boolean.FALSE == isEmpty(array);
    }

    /**
     * 数组是否为非空
     *
     * @param array 数组
     * @return 是否为非空
     */
    public static boolean isNotEmpty(char[] array) {
        return Boolean.FALSE == isEmpty(array);
    }

    /**
     * 数组是否为非空
     *
     * @param array 数组
     * @return 是否为非空
     */
    public static boolean isNotEmpty(byte[] array) {
        return Boolean.FALSE == isEmpty(array);
    }

    /**
     * 数组是否为非空
     *
     * @param array 数组
     * @return 是否为非空
     */
    public static boolean isNotEmpty(double[] array) {
        return Boolean.FALSE == isEmpty(array);
    }

    /**
     * 数组是否为非空
     *
     * @param array 数组
     * @return 是否为非空
     */
    public static boolean isNotEmpty(float[] array) {
        return Boolean.FALSE == isEmpty(array);
    }

    /**
     * 数组是否为非空
     *
     * @param array 数组
     * @return 是否为非空
     */
    public static boolean isNotEmpty(boolean[] array) {
        return Boolean.FALSE == isEmpty(array);
    }


// ---------------------------------------------------------------------- 翻转数组 reverse


    /**
     * 翻转数组
     *
     * @param array 原数组
     * @param cover 是否覆盖原数组
     * @return 翻转后的数组
     */
    public static long[] reverse(long[] array, boolean cover) {
        if (isEmpty(array)) {
            return new long[0];
        }


        if (cover) {
            for (int i = 0; i < array.length / 2; i++) {
                long temp = array[i];
                array[i] = array[array.length - 1 - i];
                array[array.length - 1 - i] = temp;
            }
            return array;
        }
        return IntStream.rangeClosed(1, array.length)
                .mapToLong(i -> array[array.length - i])
                .toArray();
    }

    /**
     * 翻转数组, 不覆盖原数组
     *
     * @param array 原数组
     * @return 翻转后的数组
     */
    public static long[] reverse(long[] array) {
        return reverse(array, false);
    }

    /**
     * 翻转数组
     *
     * @param array 原数组
     * @param cover 是否覆盖原数组
     * @return 翻转后的数组
     */
    public static int[] reverse(int[] array, boolean cover) {
        if (isEmpty(array)) {
            return new int[0];
        }
        if (cover) {
            for (int i = 0; i < array.length / 2; i++) {
                int temp = array[i];
                array[i] = array[array.length - 1 - i];
                array[array.length - 1 - i] = temp;
            }
            return array;
        }
        return IntStream.rangeClosed(1, array.length)
                .map(i -> array[array.length - i])
                .toArray();
    }

    /**
     * 翻转数组, 不覆盖原数组
     *
     * @param array 原数组
     * @return 翻转后的数组
     */
    public static int[] reverse(int[] array) {
        return reverse(array, false);
    }


    /**
     * 翻转数组
     *
     * @param array 原数组
     * @param cover 是否覆盖原数组
     * @return 翻转后的数组
     */
    public static short[] reverse(short[] array, boolean cover) {
        if (isEmpty(array)) {
            return new short[0];
        }
        short[] clone = cover ? array : array.clone();
        for (int i = 0; i < clone.length / 2; i++) {
            short temp = array[i];
            clone[i] = array[array.length - 1 - i];
            clone[array.length - 1 - i] = temp;
        }
        return clone;
    }

    /**
     * 翻转数组, 不覆盖原数组
     *
     * @param array 原数组
     * @return 翻转后的数组
     */
    public static short[] reverse(short[] array) {
        return reverse(array, false);
    }

    /**
     * 翻转数组
     *
     * @param array 原数组
     * @param cover 是否覆盖原数组
     * @return 翻转后的数组
     */
    public static char[] reverse(char[] array, boolean cover) {
        if (isEmpty(array)) {
            return new char[0];
        }
        char[] clone = cover ? array : array.clone();
        for (int i = 0; i < clone.length / 2; i++) {
            char temp = array[i];
            clone[i] = array[array.length - 1 - i];
            clone[array.length - 1 - i] = temp;
        }
        return clone;
    }

    /**
     * 翻转数组, 不覆盖原数组
     *
     * @param array 原数组
     * @return 翻转后的数组
     */
    public static char[] reverse(char[] array) {
        return reverse(array, false);
    }

    /**
     * 翻转数组
     *
     * @param array 原数组
     * @param cover 是否覆盖原数组
     * @return 翻转后的数组
     */
    public static byte[] reverse(byte[] array, boolean cover) {
        if (isEmpty(array)) {
            return new byte[0];
        }
        byte[] clone = cover ? array : array.clone();
        for (int i = 0; i < clone.length / 2; i++) {
            byte temp = array[i];
            clone[i] = array[array.length - 1 - i];
            clone[array.length - 1 - i] = temp;
        }
        return clone;
    }

    /**
     * 翻转数组, 不覆盖原数组
     *
     * @param array 原数组
     * @return 翻转后的数组
     */
    public static byte[] reverse(byte[] array) {
        return reverse(array, false);
    }


    /**
     * 翻转数组
     *
     * @param array 原数组
     * @param cover 是否覆盖原数组
     * @return 翻转后的数组
     */
    public static double[] reverse(double[] array, boolean cover) {
        if (isEmpty(array)) {
            return new double[0];
        }

        if (cover) {
            for (int i = 0; i < array.length / 2; i++) {
                double temp = array[i];
                array[i] = array[array.length - 1 - i];
                array[array.length - 1 - i] = temp;
            }
            return array;
        }
        return IntStream.rangeClosed(1, array.length)
                .mapToDouble(i -> array[array.length - i])
                .toArray();
    }

    /**
     * 翻转数组, 不覆盖原数组
     *
     * @param array 原数组
     * @return 翻转后的数组
     */
    public static double[] reverse(double[] array) {
        return reverse(array, false);
    }

    /**
     * 翻转数组
     *
     * @param array 原数组
     * @param cover 是否覆盖原数组
     * @return 翻转后的数组
     */
    public static float[] reverse(float[] array, boolean cover) {
        if (isEmpty(array)) {
            return new float[0];
        }
        float[] clone = cover ? array : array.clone();
        for (int i = 0; i < clone.length / 2; i++) {
            float temp = array[i];
            clone[i] = array[array.length - 1 - i];
            clone[array.length - 1 - i] = temp;
        }
        return clone;
    }

    /**
     * 翻转数组, 不覆盖原数组
     *
     * @param array 原数组
     * @return 翻转后的数组
     */
    public static float[] reverse(float[] array) {
        return reverse(array, false);
    }

    /**
     * 翻转数组
     *
     * @param array 原数组
     * @param cover 是否覆盖原数组
     * @return 翻转后的数组
     */
    public static boolean[] reverse(boolean[] array, boolean cover) {
        if (isEmpty(array)) {
            return new boolean[0];
        }
        boolean[] clone = cover ? array : array.clone();
        for (int i = 0; i < clone.length / 2; i++) {
            boolean temp = array[i];
            clone[i] = array[array.length - 1 - i];
            clone[array.length - 1 - i] = temp;
        }
        return clone;
    }

    /**
     * 翻转数组, 不覆盖原数组
     *
     * @param array 原数组
     * @return 翻转后的数组
     */
    public static boolean[] reverse(boolean[] array) {
        return reverse(array, false);
    }


    // --------------------------------------------- 数组复制 copy()


    /**
     * <p>复制指定的数组，截断或填充空值（如有必要），使副本具有指定的长度。 </p>
     * <p>对于在原始数组和副本中都有效的所有索引，这两个数组将包含相同的值。</p>
     * <p>对于在副本中有效但在原始副本中无效的任何索引，副本将包含null 。 </p>
     * <p>当且仅当指定长度大于原始数组的长度时，此类索引才会存在。 </p>
     * <p>结果数组与原始数组的类完全相同。</p>
     *
     * @param original  要复制的数组
     * @param newLength 要返回的副本的长度
     * @return 原始数组的副本，被截断或用空值填充以获得指定的长度
     */
    public static byte[] copy(byte[] original, int newLength) {
        return Arrays.copyOf(original, newLength);
    }

    /**
     * <p>复制指定的数组，截断或填充空值（如有必要），使副本具有指定的长度。 </p>
     * <p>对于在原始数组和副本中都有效的所有索引，这两个数组将包含相同的值。</p>
     * <p>对于在副本中有效但在原始副本中无效的任何索引，副本将包含null 。 </p>
     * <p>结果数组与原始数组的类完全相同。</p>
     * <p>如果原始值是 null 或者 空数组, 则返回 new byte[0]</p>
     *
     * @param original 要复制的数组
     * @return 原始数组的副本，被截断或用空值填充以获得指定的长度
     */
    public static byte[] copy(byte[] original) {
        if (isEmpty(original)) {
            return new byte[0];
        }
        return copy(original, original.length);
    }


    /**
     * <p>复制指定的数组，截断或填充空值（如有必要），使副本具有指定的长度。 </p>
     * <p>对于在原始数组和副本中都有效的所有索引，这两个数组将包含相同的值。</p>
     * <p>对于在副本中有效但在原始副本中无效的任何索引，副本将包含null 。 </p>
     * <p>当且仅当指定长度大于原始数组的长度时，此类索引才会存在。 </p>
     * <p>结果数组与原始数组的类完全相同。</p>
     *
     * @param original  要复制的数组
     * @param newLength 要返回的副本的长度
     * @return 原始数组的副本，被截断或用空值填充以获得指定的长度
     */
    public static short[] copy(short[] original, int newLength) {
        return Arrays.copyOf(original, newLength);
    }

    /**
     * <p>复制指定的数组，截断或填充空值（如有必要），使副本具有指定的长度。 </p>
     * <p>对于在原始数组和副本中都有效的所有索引，这两个数组将包含相同的值。</p>
     * <p>对于在副本中有效但在原始副本中无效的任何索引，副本将包含null 。 </p>
     * <p>结果数组与原始数组的类完全相同。</p>
     * <p>如果原始值是 null 或者 空数组, 则返回 new short[0]</p>
     *
     * @param original 要复制的数组
     * @return 原始数组的副本，被截断或用空值填充以获得指定的长度
     */
    public static short[] copy(short[] original) {
        if (isEmpty(original)) {
            return new short[0];
        }
        return copy(original, original.length);
    }


    /**
     * <p>复制指定的数组，截断或填充空值（如有必要），使副本具有指定的长度。 </p>
     * <p>对于在原始数组和副本中都有效的所有索引，这两个数组将包含相同的值。</p>
     * <p>对于在副本中有效但在原始副本中无效的任何索引，副本将包含null 。 </p>
     * <p>当且仅当指定长度大于原始数组的长度时，此类索引才会存在。 </p>
     * <p>结果数组与原始数组的类完全相同。</p>
     *
     * @param original  要复制的数组
     * @param newLength 要返回的副本的长度
     * @return 原始数组的副本，被截断或用空值填充以获得指定的长度
     */
    public static int[] copy(int[] original, int newLength) {
        return Arrays.copyOf(original, newLength);
    }

    /**
     * <p>复制指定的数组，截断或填充空值（如有必要），使副本具有指定的长度。 </p>
     * <p>对于在原始数组和副本中都有效的所有索引，这两个数组将包含相同的值。</p>
     * <p>对于在副本中有效但在原始副本中无效的任何索引，副本将包含null 。 </p>
     * <p>结果数组与原始数组的类完全相同。</p>
     * <p>如果原始值是 null 或者 空数组, 则返回 new int[0]</p>
     *
     * @param original 要复制的数组
     * @return 原始数组的副本，被截断或用空值填充以获得指定的长度
     */
    public static int[] copy(int[] original) {
        if (isEmpty(original)) {
            return new int[0];
        }
        return copy(original, original.length);
    }


    /**
     * <p>复制指定的数组，截断或填充空值（如有必要），使副本具有指定的长度。 </p>
     * <p>对于在原始数组和副本中都有效的所有索引，这两个数组将包含相同的值。</p>
     * <p>对于在副本中有效但在原始副本中无效的任何索引，副本将包含null 。 </p>
     * <p>当且仅当指定长度大于原始数组的长度时，此类索引才会存在。 </p>
     * <p>结果数组与原始数组的类完全相同。</p>
     *
     * @param original  要复制的数组
     * @param newLength 要返回的副本的长度
     * @return 原始数组的副本，被截断或用空值填充以获得指定的长度
     */
    public static long[] copy(long[] original, int newLength) {
        return Arrays.copyOf(original, newLength);
    }

    /**
     * <p>复制指定的数组，截断或填充空值（如有必要），使副本具有指定的长度。 </p>
     * <p>对于在原始数组和副本中都有效的所有索引，这两个数组将包含相同的值。</p>
     * <p>对于在副本中有效但在原始副本中无效的任何索引，副本将包含null 。 </p>
     * <p>结果数组与原始数组的类完全相同。</p>
     * <p>如果原始值是 null 或者 空数组, 则返回 new long[0]</p>
     *
     * @param original 要复制的数组
     * @return 原始数组的副本，被截断或用空值填充以获得指定的长度
     */
    public static long[] copy(long[] original) {
        if (isEmpty(original)) {
            return new long[0];
        }
        return copy(original, original.length);
    }

    /**
     * <p>复制指定的数组，截断或填充空值（如有必要），使副本具有指定的长度。 </p>
     * <p>对于在原始数组和副本中都有效的所有索引，这两个数组将包含相同的值。</p>
     * <p>对于在副本中有效但在原始副本中无效的任何索引，副本将包含null 。 </p>
     * <p>当且仅当指定长度大于原始数组的长度时，此类索引才会存在。 </p>
     * <p>结果数组与原始数组的类完全相同。</p>
     *
     * @param original  要复制的数组
     * @param newLength 要返回的副本的长度
     * @return 原始数组的副本，被截断或用空值填充以获得指定的长度
     */
    public static float[] copy(float[] original, int newLength) {
        return Arrays.copyOf(original, newLength);
    }

    /**
     * <p>复制指定的数组，截断或填充空值（如有必要），使副本具有指定的长度。 </p>
     * <p>对于在原始数组和副本中都有效的所有索引，这两个数组将包含相同的值。</p>
     * <p>对于在副本中有效但在原始副本中无效的任何索引，副本将包含null 。 </p>
     * <p>结果数组与原始数组的类完全相同。</p>
     * <p>如果原始值是 null 或者 空数组, 则返回 new float[0]</p>
     *
     * @param original 要复制的数组
     * @return 原始数组的副本，被截断或用空值填充以获得指定的长度
     */
    public static float[] copy(float[] original) {
        if (isEmpty(original)) {
            return new float[0];
        }
        return copy(original, original.length);
    }

    /**
     * <p>复制指定的数组，截断或填充空值（如有必要），使副本具有指定的长度。 </p>
     * <p>对于在原始数组和副本中都有效的所有索引，这两个数组将包含相同的值。</p>
     * <p>对于在副本中有效但在原始副本中无效的任何索引，副本将包含null 。 </p>
     * <p>当且仅当指定长度大于原始数组的长度时，此类索引才会存在。 </p>
     * <p>结果数组与原始数组的类完全相同。</p>
     *
     * @param original  要复制的数组
     * @param newLength 要返回的副本的长度
     * @return 原始数组的副本，被截断或用空值填充以获得指定的长度
     */
    public static double[] copy(double[] original, int newLength) {
        return Arrays.copyOf(original, newLength);
    }

    /**
     * <p>复制指定的数组，截断或填充空值（如有必要），使副本具有指定的长度。 </p>
     * <p>对于在原始数组和副本中都有效的所有索引，这两个数组将包含相同的值。</p>
     * <p>对于在副本中有效但在原始副本中无效的任何索引，副本将包含null 。 </p>
     * <p>结果数组与原始数组的类完全相同。</p>
     * <p>如果原始值是 null 或者 空数组, 则返回 new double[0]</p>
     *
     * @param original 要复制的数组
     * @return 原始数组的副本，被截断或用空值填充以获得指定的长度
     */
    public static double[] copy(double[] original) {
        if (isEmpty(original)) {
            return new double[0];
        }
        return copy(original, original.length);
    }


    /**
     * <p>复制指定的数组，截断或填充空值（如有必要），使副本具有指定的长度。 </p>
     * <p>对于在原始数组和副本中都有效的所有索引，这两个数组将包含相同的值。</p>
     * <p>对于在副本中有效但在原始副本中无效的任何索引，副本将包含null 。 </p>
     * <p>当且仅当指定长度大于原始数组的长度时，此类索引才会存在。 </p>
     * <p>结果数组与原始数组的类完全相同。</p>
     *
     * @param original  要复制的数组
     * @param newLength 要返回的副本的长度
     * @return 原始数组的副本，被截断或用空值填充以获得指定的长度
     */
    public static boolean[] copy(boolean[] original, int newLength) {
        return Arrays.copyOf(original, newLength);
    }

    /**
     * <p>复制指定的数组，截断或填充空值（如有必要），使副本具有指定的长度。 </p>
     * <p>对于在原始数组和副本中都有效的所有索引，这两个数组将包含相同的值。</p>
     * <p>对于在副本中有效但在原始副本中无效的任何索引，副本将包含null 。 </p>
     * <p>结果数组与原始数组的类完全相同。</p>
     * <p>如果原始值是 null 或者 空数组, 则返回 new boolean[0]</p>
     *
     * @param original 要复制的数组
     * @return 原始数组的副本，被截断或用空值填充以获得指定的长度
     */
    public static boolean[] copy(boolean[] original) {
        if (isEmpty(original)) {
            return new boolean[0];
        }
        return copy(original, original.length);
    }


}
