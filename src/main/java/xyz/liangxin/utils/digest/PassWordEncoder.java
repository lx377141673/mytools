package xyz.liangxin.utils.digest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 密码加密工具类 【 Brypt 算法】
 *
 * @author liangxin
 * @version V1.0
 * @Package xyz.liangxin.springbootdemo.common.uitls
 * @date 2021/4/12 16:12
 * @Description 密码加密工具类
 */
public class PassWordEncoder {

    private static final Logger logger = LoggerFactory.getLogger(PassWordEncoder.class);

    /**
     * 自定义 盐， 则每次 加密的密文一致
     * $2a$10$4mpCcGPbdvCspuecwaypge
     */
    private static final String SLAT = BryptUtil.genSalt(10);

    /**
     * 验证密码加密
     * 默认加盐 {@link #SLAT}
     * @param passWord 密码
     * @return 加密后的密文
     */
    public static String encoder(String passWord) {
        return BryptUtil.hashPw(passWord, SLAT);
    }

    /**
     * 验证密码与密文是否匹配
     *
     * @param passWord 明文密码
     * @param hashed   密文
     * @return 是否匹配
     */
    public static boolean checkPw(String passWord, String hashed) {
        return BryptUtil.checkPw(passWord, hashed);
    }

    /**
     * 验证密码与密文是否 不匹配
     *
     * @param passWord 明文密码
     * @param hashed   密文
     * @return 是否匹配
     */
    public static boolean checkPwNot(String passWord, String hashed) {
        return !checkPw(passWord, hashed);
    }


    public static void main(String[] args) {
        String pwd = "11111111111111";
        String pwd1 = encoder(pwd);
        String pwd2 = encoder(pwd);
        logger.info("{}", pwd1);
        logger.info("{}", pwd2);
        logger.info("{}", checkPw(pwd, pwd1));
        logger.info("{}", checkPw(pwd, pwd2));
    }
}

