package xyz.liangxin.utils.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.liangxin.utils.core.ObjectUtils;

import java.io.File;

/**
 * <p> 文件工具类
 *
 * @author liangxin
 * @version V1.0
 * @since 创建时间: 2022/1/9 15:11
 */
public class FileUtils extends IOUtils{

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
    protected FileUtils() {
    }

    /**
     * 判断当前文件信息 不为 null 且 存在
     *
     * @param file 文件信息
     * @return 文件是否存在
     */
    public static boolean isExists(File file) {
        return ObjectUtils.nonNull(file) && file.exists();
    }

    /**
     * 判断是否是一个真实的文件 而不是 文件夹
     *
     * @param file 是否是文件 而非文件夹
     * @return true: 是文件 false 不是文件/ 或不存在
     */
    public static boolean isFile(File file) {
        return isExists(file) && file.isFile();
    }

    /**
     * 判断是否是一个真实存在的文件夹 而不是 文件
     *
     * @param file 是否是文件夹 而非文件
     * @return true: 是文件夹 false 不是文件夹/ 或不存在
     */
    public static boolean isDirectory(File file) {
        return isExists(file) && file.isDirectory();
    }


    /**
     * 创建文件夹, 可以多级创建
     *
     * @param folder 文件夹 文件对象
     * @return 创建是否成功
     */
    public static boolean mkdir(File folder) {
        try {
            if (!isExists(folder)) {
                return folder.mkdirs();
            }
        } catch (Exception e) {
            logger.error("新建目录操作出错", e);
        }
        return false;
    }

    /**
     * 创建文件夹,可多级创建
     *
     * @param folderPath 文件夹地址信息
     */
    public static boolean mkdir(String folderPath) {
        return mkdir(new File(folderPath));
    }


}
