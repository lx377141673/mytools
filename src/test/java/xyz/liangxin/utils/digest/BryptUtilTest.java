package xyz.liangxin.utils.digest;

import org.junit.Test;

/**
 * <p> Brypt 加密测试
 *
 * @author liangxin
 * @version V1.0
 * @since 创建时间: 2022/3/4 17:00
 */
public class BryptUtilTest {

    @Test
    public void bryptTest() {
        String pwd = "111222333";
        String s = BryptUtil.hashPw(pwd);
        System.out.println(s);
        System.out.println(BryptUtil.checkPw(pwd + " ", s));
    }
}