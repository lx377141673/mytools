package xyz.liangxin.utils.array;

import org.junit.Test;
import xyz.liangxin.utils.core.array.ArrayUtil;

import java.util.Arrays;

/**
 * <p> 描述
 *
 * @author liangxin
 * @version V1.0
 * @since 创建时间: 2022/1/20 19:43
 */
public class ArrayUtilTest {
    @Test
    public void reverse() {
        int[] a = new int[]{1, 2, 3};
        ArrayUtil.reverse(a, true);
        System.out.println(Arrays.toString(a));
    }
}
