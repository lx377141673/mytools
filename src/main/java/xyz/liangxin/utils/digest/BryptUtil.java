package xyz.liangxin.utils.digest;

import java.security.SecureRandom;

/**
 * Brypt 加密工具类
 *
 * @author liangxin
 * @version V1.0
 * @Package xyz.liangxin.springbootdemo.common.uitls.digest
 * @date 2021/4/13 5:15
 * @Description Brypt 加密工具类
 */
public class BryptUtil {

    private BryptUtil() {
    }


    /**
     * 默认对数
     */
    private static final int LOG_ROUNDS=10;

    /**
     * 生成盐
     * @return 盐值
     */
    public static String genSalt(){
        return genSalt(LOG_ROUNDS);
    }

    /**
     * 生成盐
     * @param logRounds hash中叠加的2的对数
     * @return 盐值
     */
    public static String genSalt(int logRounds){
        return genSalt(logRounds, new SecureRandom());
    }

    /**
     * 生成盐
     * @param logRounds hash中叠加的2的对数
     * @param random 加密的强随机数生成器
     * @return 盐值
     */
    public static String genSalt(int logRounds, SecureRandom random){
        return BCrypt.gensalt(logRounds,random);
    }

    /**
     * 加密字符串
     * @param passWord 待加密密文
     * @return 密文
     */
    public static String hashPw(String passWord) {
        return hashPw(passWord,genSalt());
    }

    /**
     * 加密字符串
     * @param passWord 待加密密文
     * @param slat 盐字符串 基于 {@link #genSalt()}
     * @return 密文
     */
    public static String hashPw(String passWord, String slat) {
        return BCrypt.hashpw(passWord, slat);
    }


    /**
     * 验证密文
     * @param passWord 明文
     * @param hashed 密文
     * @return 是否验证成功
     */
    public static boolean checkPw(String passWord, String hashed) {
        return BCrypt.checkpw(passWord, hashed);
    }

}


