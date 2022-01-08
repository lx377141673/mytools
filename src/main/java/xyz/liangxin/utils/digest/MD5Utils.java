package xyz.liangxin.utils.digest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.liangxin.utils.core.text.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * MD5 摘要工具类
 *
 * @author liangxin
 * @version V1.0
 * @date 2021/4/7 19:58
 */
public class MD5Utils {
    private static final Logger logger = LoggerFactory.getLogger(MD5Utils.class);
    /**
     * 默认的密码字符串组合，用来将字节转换成 16 进制表示的字符,apache校验下载的文件的正确性用的就是默认的这个组合
     */
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static MessageDigest messageDigest = null;

    static {
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            logger.error("MD5FileUtil messageDigest 初始化失败", e);
        }
    }

    private MD5Utils() {
    }

    /**
     * md5 加密
     *
     * @param str 待加密明文
     * @return 密文
     */
    public static String md5(String str) {
        if (Objects.isNull(str) || str.length() <= 0) {
            return null;
        }
        return md5(str.getBytes());

    }

    public static String md5(byte[] bytes) {
        messageDigest.update(bytes);
        return bufferToHex(messageDigest.digest());
    }


    /**
     * 获取文件的MD5值
     *
     * @param file 待处理的文件
     * @return 文件 MD值
     */
    public static String md5(File file) throws IOException {
        return md5(file, 0, -1);
    }

    /**
     * 获取文件的MD5值
     *
     * @param file   待获取md5值的文件
     * @param start  读取文件起始位置
     * @param length 读取文件长度
     * @return 文件的MD5值
     * @throws IOException 文件操作异常
     */
    public static String md5(File file, long start, long length) throws IOException {
        if (!file.exists()) {
            // 文件不存在
            return null;
        }
        try (FileInputStream fis = new FileInputStream(file)) {
            FileChannel ch = fis.getChannel();

            long remain = file.length() - start;

            if (length < 0 || length > remain) {
                length = remain;
            }
            MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY,
                    start, length);
            messageDigest.update(byteBuffer);
        }
        return bufferToHex(messageDigest.digest());
    }

    /**
     * 判断, 明文密码和 md5加密的密码, 是否匹配
     *
     * @param password  明文密码
     * @param md5PwdStr md5 加密密码
     * @return 是否匹配
     */
    public static boolean checkPassword(String password, String md5PwdStr) {
        if (StringUtils.isEmpty(password)) {
            return false;
        }
        String s = md5(password);
        assert s != null;
        return s.equals(md5PwdStr);
    }


    /**
     * 计算二进制数据
     *
     * @param bytes 数据流
     * @return 返回 计算后的 摘要值
     */
    private static String bufferToHex(byte[] bytes) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte[] bytes, int m, int n) {
        StringBuilder stringBuilder = new StringBuilder(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringBuilder);
        }
        return stringBuilder.toString();
    }

    private static void appendHexPair(byte bt, StringBuilder stringBuilder) {
        // 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移
        char c0 = HEX_DIGITS[(bt & 0xf0) >> 4];
        // 取字节中低 4 位的数字转换
        char c1 = HEX_DIGITS[bt & 0xf];
        stringBuilder.append(c0);
        stringBuilder.append(c1);
    }

}
