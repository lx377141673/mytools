package xyz.liangxin.utils.core.number;

import cn.hutool.core.util.NumberUtil;
import org.junit.Test;


/**
 * <p> 描述
 *
 * @author liangxin
 * @version V1.0
 * @since 创建时间: 2022/5/29 22:21
 */
public class NumberUtilsTest {
    @Test
    public void numToString() {
        System.out.println(NumberUtil.toStr(Long.MAX_VALUE));
        System.out.println(NumberUtils.toBigDecimal(Double.MAX_VALUE));
        System.out.println(NumberUtils.toBigDecimal(999999999999999999999999999.135689).toPlainString());
        System.out.println(NumberUtils.toBigDecimal(999999999999999999999999999.135689).toEngineeringString());
    }

}