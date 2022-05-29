package xyz.liangxin.utils.core.text;

import org.junit.Test;

/**
 * <p> 描述
 *
 * @author liangxin
 * @version V1.0
 * @since 创建时间: 2022/1/20 19:52
 */
public class ChineseNumberTest {

    public static void test(int i) {
        System.out.println(i);

    }

    @Test
    public void test() {
        System.out.println(ChineseNumber.getChineseAmount(1994));
        System.out.println(ChineseNumber.getChineseAmount(1994.123456));
        System.out.println(ChineseNumber.getChineseAmount(19941115));
        System.out.println(ChineseNumber.getChineseAmount(10));
        System.out.println(ChineseNumber.getChineseNumber("1222234567003.124324213412"));
        System.out.println(ChineseNumber.getChineseNumber(10101));
        System.out.println(ChineseNumber.getChineseNumber(9999999999999.1465418894465465));

//        壹仟玖佰玖拾肆圆整
//        壹仟玖佰玖拾肆圆壹角贰分整
//        壹仟玖佰玖拾肆万壹仟壹佰壹拾伍圆整
//        壹拾圆整
//        壹万贰仟贰佰贰拾贰亿叁仟肆佰伍拾陆万柒仟零叁点壹贰肆叁贰肆贰壹叁肆壹贰
//        壹万壹仟壹佰壹拾壹
//        壹佰贰拾叁亿贰仟壹佰壹拾贰万肆仟壹佰贰拾肆
//        玖万玖仟玖佰玖拾玖亿玖仟玖佰玖拾玖万玖仟玖佰玖拾玖点壹
        int a = 10;
        test(a++);
        System.out.println(a);
    }

}
