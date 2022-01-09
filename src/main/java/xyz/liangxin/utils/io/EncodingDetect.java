package xyz.liangxin.utils.io;

import java.io.File;
import java.net.URL;

/**
 * 获取文件的 编码格式
 * @author liangxin
 */
public class EncodingDetect {
    private EncodingDetect(){}
    public static String detect(String path) {
        return Encoding.JAVA_NAME[BytesEncodingDetect.getInstance().detectEncoding(new File(path))];
    }

    public static String detect(File file) {
        return Encoding.JAVA_NAME[BytesEncodingDetect.getInstance().detectEncoding(file)];
    }

    public static String detect(byte[] contents) {
        return Encoding.JAVA_NAME[BytesEncodingDetect.getInstance().detectEncoding(contents)];
    }

    public static String detect(URL url) {
        return Encoding.JAVA_NAME[BytesEncodingDetect.getInstance().detectEncoding(url)];
    }

    public static void main(String[] args) {
        System.out.println(detect(new File("D:\\Desktop\\1.txt")));
    }



}