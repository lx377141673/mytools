package xyz.liangxin.utils.digest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;

/**
 * 获取文件摘要工具类
 *
 * @author liangxin
 * @version V1.0
 * @Package xyz.liangxin.springbootdemo.common.uitls.digest
 * @date 2021/4/12 17:05
 * @Description 获取文件摘要工具类
 */
public class FileSafeCode {

    /**
     * 计算大文件 md5获取getMD5(); SHA1获取getSha1() CRC32获取 getCRC32()
     */
    protected static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
            'f'};


    private FileSafeCode() {
    }

    /**
     * 对一个文件获取md5值
     *
     * @param file 等待获取摘要的文件
     * @return 返回 摘要码, 如果文件不存在 返回 null
     * @throws IOException              文件操作异常
     * @throws NoSuchAlgorithmException 摘要算法异常
     */
    public static String getMD5(File file) throws IOException, NoSuchAlgorithmException {
        return getAbstractCode("MD5", file);
    }


    /**
     * 计算SHA1码 适用于上G大的文件
     *
     * @param file 等待获取摘要的文件
     * @return 返回 摘要码, 如果文件不存在 返回 null
     * @throws OutOfMemoryError         内存溢出异常
     * @throws IOException              文件操作异常
     * @throws NoSuchAlgorithmException 摘要算法异常
     */
    public static String getSha1(File file) throws OutOfMemoryError, IOException, NoSuchAlgorithmException {
        return getAbstractCode("SHA-1", file);
    }

    /**
     * 获取文件SHA256码
     *
     * @param file 等待获取摘要的文件
     * @return 返回 摘要码, 如果文件不存在 返回 null
     * @throws OutOfMemoryError         内存溢出异常
     * @throws IOException              文件操作异常
     * @throws NoSuchAlgorithmException 摘要算法异常
     */
    public static String getSha256(File file) throws OutOfMemoryError, IOException, NoSuchAlgorithmException {
        return getAbstractCode("SHA-256", file);
    }


    /**
     * 根据算法类型, 生成 摘要码
     *
     * @param hashType 算法类型 : MD5 , SHA-1 , SHA-256
     * @param file     等待获取摘要的文件
     * @return 返回 摘要码, 如果文件不存在 返回 null
     * @throws IOException              文件操作异常
     * @throws NoSuchAlgorithmException 摘要算法异常
     */
    private static String getAbstractCode(String hashType, File file) throws IOException, NoSuchAlgorithmException {
        if (!file.exists()) {
            return null;
        }
        MessageDigest messagedigest = MessageDigest.getInstance(hashType);
        try (FileInputStream in = new FileInputStream(file)) {
            FileChannel ch = in.getChannel();
            MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            messagedigest.update(byteBuffer);
        }
        return bufferToHex(messagedigest.digest());
    }


    /**
     * 获取文件CRC32码
     *
     * @param file 待计算的文件
     * @return 返回文件的 CRC32码
     */
    public static String getCRC32(File file) {
        CRC32 crc32 = new CRC32();
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                crc32.update(buffer, 0, length);
            }
            return crc32.getValue() + "";
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 计算二进制数据
     *
     * @param bytes 数据流
     * @return 返回 计算后的 摘要值
     */
    private static String bufferToHex(byte[] bytes) {
        return bufferToHex(bytes, bytes.length);
    }

    private static String bufferToHex(byte[] bytes, int n) {
        StringBuilder stringBuilder = new StringBuilder(2 * n);
        for (int l = 0; l < n; l++) {
            appendHexPair(bytes[l], stringBuilder);
        }
        return stringBuilder.toString();
    }

    private static void appendHexPair(byte bt, StringBuilder stringBuilder) {
        char c0 = HEX_DIGITS[(bt & 0xf0) >> 4];
        char c1 = HEX_DIGITS[bt & 0xf];
        stringBuilder.append(c0);
        stringBuilder.append(c1);
    }


}
