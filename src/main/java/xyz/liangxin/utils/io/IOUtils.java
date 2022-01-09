package xyz.liangxin.utils.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * <p> IO操作工具类
 *
 * @author liangxin
 * @version V1.0
 * @since 创建时间: 2022/1/9 15:11
 */
public class IOUtils {

    private static final Logger logger = LoggerFactory.getLogger(IOUtils.class);

    protected IOUtils() {
    }

    /**
     * 文件字节输入流
     * <p>通过打开与实际文件的连接来创建FileInputStream ，该文件由文件系统中的File对象file命名。
     * <p> 创建一个新的FileDescriptor对象来表示此文件连接。
     * <p> 首先，如果存在安全管理器，则调用其checkRead方法，并将file参数表示的路径作为其参数。
     * <p> 如果命名文件不存在，是目录而不是常规文件，或者由于某些其他原因无法打开读取，则抛出FileNotFoundException
     *
     * @param file 要打开以供阅读的文件。
     * @return 文件字节输入流
     * @throws FileNotFoundException 文件不存在时,是目录而不是常规文件，或者由于某些其他原因无法打开读取。
     * @throws SecurityException     如果存在安全管理器并且其checkRead方法拒绝对文件的读取访问。
     */
    public static InputStream getInputStream(File file) throws FileNotFoundException {
        return new FileInputStream(file);
    }

    /**
     * <p>创建具有指定缓冲区大小的BufferedInputStream
     * <p>创建一个长度size的内部缓冲区数组并将其存储在buf 。
     *
     * @param file 要打开以供阅读的文件。
     * @param size 缓冲区大小,字节数
     * @return 缓冲字节流
     * @throws FileNotFoundException    文件不存在时,是目录而不是常规文件，或者由于某些其他原因无法打开读取。
     * @throws SecurityException        如果存在安全管理器并且其checkRead方法拒绝对文件的读取访问。
     * @throws IllegalArgumentException 如果 size <= 0
     */
    public static BufferedInputStream getBufferedInputStream(File file, int size) throws FileNotFoundException {
        return new BufferedInputStream(getInputStream(file), size);
    }

    /**
     * 缓冲字节流
     *
     * <p>创建具有指定缓冲区大小的BufferedInputStream
     * <p>创建一个长度 8192 字节 (8KB) 的内部缓冲区数组并将其存储在buf 。
     *
     * @param file 要打开以供阅读的文件。
     * @return 缓冲字节流
     * @throws FileNotFoundException 文件不存在时,是目录而不是常规文件，或者由于某些其他原因无法打开读取。
     * @throws SecurityException     如果存在安全管理器并且其checkRead方法拒绝对文件的读取访问。
     */
    public static BufferedInputStream getBufferedInputStream(File file) throws FileNotFoundException {
        return new BufferedInputStream(getInputStream(file));
    }


    /**
     * 文件字符输入流
     * 创建一个使用给定字符集的 InputStreamReader。
     *
     * @param file    要打开以供阅读的文件。
     * @param charset 字符集编码
     * @return 文件字符输入流
     * @throws FileNotFoundException 文件不存在时,是目录而不是常规文件，或者由于某些其他原因无法打开读取。
     * @throws SecurityException     如果存在安全管理器并且其checkRead方法拒绝对文件的读取访问。
     */
    public static InputStreamReader getInputStreamReader(File file, Charset charset) throws FileNotFoundException {
        return new InputStreamReader(getInputStream(file), charset);
    }

    /**
     * 文件字符输入流
     * <p> 默认字符集编码: {@link StandardCharsets#UTF_8}</p>
     * <p>创建一个 {@link StandardCharsets#UTF_8} 字符集的InputStreamReader。</p>
     *
     * @param file 要打开以供阅读的文件。
     * @return 文件字符输入流
     * @throws FileNotFoundException 文件不存在时,是目录而不是常规文件，或者由于某些其他原因无法打开读取。
     * @throws SecurityException     如果存在安全管理器并且其checkRead方法拒绝对文件的读取访问。
     */
    public static InputStreamReader getInputStreamReader(File file) throws FileNotFoundException {
        return new InputStreamReader(getInputStream(file), StandardCharsets.UTF_8);
    }

    /**
     * 创建一个使用指定大小的输入缓冲区的缓冲字符输入流。
     * <p>
     * 从字符输入流中读取文本，缓冲字符，以便高效读取字符、数组和行。
     * 可以指定缓冲区大小，也可以使用默认大小。 对于大多数用途，默认值足够大。
     * 通常，由 Reader 发出的每个读取请求都会导致对底层字符或字节流发出相应的读取请求。 因此，建议将 BufferedReader 包装在 read() 操作可能成本高昂的任何 Reader 周围，例如 FileReaders 和 InputStreamReaders。 例如，
     * BufferedReader in = new BufferedReader(new FileReader("foo.in"));
     * <p>
     * 将缓冲来自指定文件的输入。 如果没有缓冲，每次调用 read() 或 readLine() 都可能导致从文件中读取字节，转换为字符，然后返回，这可能非常低效。
     * 使用 DataInputStreams 进行文本输入的程序可以通过将每个 DataInputStream 替换为适当的 BufferedReader 来本地化
     *
     * @param file    要打开以供阅读的文件。
     * @param charset 文件字符集
     * @param size    缓冲区大小
     * @return 缓冲字符输入流
     * @throws FileNotFoundException    文件不存在时,是目录而不是常规文件，或者由于某些其他原因无法打开读取。
     * @throws SecurityException        如果存在安全管理器并且其checkRead方法拒绝对文件的读取访问。
     * @throws IllegalArgumentException 如果 size <= 0
     */
    public static BufferedReader getBufferedReader(File file, Charset charset, int size) throws FileNotFoundException {
        return new BufferedReader(getInputStreamReader(file, charset), size);
    }

    /**
     * <p>创建一个使用 8192 字节 (8KB) 的输入缓冲区的缓冲字符输入流。
     *
     * @param file    要打开以供阅读的文件。
     * @param charset 文件字符集
     * @return 缓冲字符输入流
     * @throws FileNotFoundException 文件不存在时,是目录而不是常规文件，或者由于某些其他原因无法打开读取。
     * @throws SecurityException     如果存在安全管理器并且其checkRead方法拒绝对文件的读取访问。
     */
    public static BufferedReader getBufferedReader(File file, Charset charset) throws FileNotFoundException {
        return new BufferedReader(getInputStreamReader(file, charset));
    }

    /**
     * 创建一个使用指定大小的输入缓冲区的缓冲字符输入流。
     * 默认字符集类型为{@link StandardCharsets#UTF_8}
     * <p>
     * 从字符输入流中读取文本，缓冲字符，以便高效读取字符、数组和行。
     * 可以指定缓冲区大小，也可以使用默认大小。 对于大多数用途，默认值足够大。
     * 通常，由 Reader 发出的每个读取请求都会导致对底层字符或字节流发出相应的读取请求。 因此，建议将 BufferedReader 包装在 read() 操作可能成本高昂的任何 Reader 周围，例如 FileReaders 和 InputStreamReaders。 例如，
     * BufferedReader in = new BufferedReader(new FileReader("foo.in"));
     * <p>
     * 将缓冲来自指定文件的输入。 如果没有缓冲，每次调用 read() 或 readLine() 都可能导致从文件中读取字节，转换为字符，然后返回，这可能非常低效。
     * 使用 DataInputStreams 进行文本输入的程序可以通过将每个 DataInputStream 替换为适当的 BufferedReader 来本地化
     *
     * @param file 要打开以供阅读的文件。
     * @param size 缓冲区大小
     * @return 缓冲字符输入流
     * @throws FileNotFoundException    文件不存在时,是目录而不是常规文件，或者由于某些其他原因无法打开读取。
     * @throws SecurityException        如果存在安全管理器并且其checkRead方法拒绝对文件的读取访问。
     * @throws IllegalArgumentException 如果 size <= 0
     */
    public static BufferedReader getBufferedReader(File file, int size) throws FileNotFoundException {
        return new BufferedReader(getInputStreamReader(file), size);
    }

    /**
     * <p>创建一个使用 8192 字节 (8KB) 的输入缓冲区的缓冲字符输入流。
     * <p>读取字符编码为 {@link StandardCharsets#UTF_8}
     *
     * @param file 要打开以供阅读的文件。
     * @return 缓冲字符输入流
     * @throws FileNotFoundException 文件不存在时,是目录而不是常规文件，或者由于某些其他原因无法打开读取。
     * @throws SecurityException     如果存在安全管理器并且其checkRead方法拒绝对文件的读取访问。
     */
    public static BufferedReader getBufferedReader(File file) throws FileNotFoundException {
        return new BufferedReader(getInputStreamReader(file));
    }


    /**
     * 文件字节输出流
     * <p>创建一个文件输出流以写入由指定File对象表示的File 。
     * <p> 创建一个新的FileDescriptor对象来表示此文件连接。
     * <p>首先，如果有安全管理器，则调用它的checkWrite方法，并将file参数表示的路径作为其参数。
     * <p>如果文件存在但是是目录而不是常规文件，不存在但无法创建，或者由于任何其他原因无法打开，则抛出FileNotFoundException 。
     *
     * @param file 要打开以进行写入的文件。
     * @return 文件字节输出流
     * @throws FileNotFoundException 如果文件存在但是是目录而不是常规文件，不存在但无法创建，或者由于任何其他原因无法打开
     * @throws SecurityException     如果存在安全管理器并且其checkWrite方法拒绝对文件的写访问
     */
    public static OutputStream getOutputStream(File file) throws FileNotFoundException {
        return new FileOutputStream(file);
    }

    /**
     * 创建一个新的缓冲输出流，以将数据写入具有指定缓冲区大小的指定基础输出流。
     *
     * @param file 要打开以进行写入的文件
     * @param size 缓冲区大小, 单位: 字节
     * @return 缓冲字节输出流
     * @throws FileNotFoundException    如果文件存在但是是目录而不是常规文件，不存在但无法创建，或者由于任何其他原因无法打开
     * @throws SecurityException        如果存在安全管理器并且其checkWrite方法拒绝对文件的写访问
     * @throws IllegalArgumentException 如果 size <= 0
     */
    public static BufferedOutputStream getBufferedOutputStream(File file, int size) throws FileNotFoundException {
        return new BufferedOutputStream(getOutputStream(file), size);
    }

    /**
     * 创建一个新的缓冲输出流，以将数据写入具有 8192 字节 (8KB) 的缓冲区 的指定基础输出流。
     *
     * @param file 要打开以进行写入的文件
     * @return 缓冲字节输出流
     * @throws FileNotFoundException 如果文件存在但是是目录而不是常规文件，不存在但无法创建，或者由于任何其他原因无法打开
     * @throws SecurityException     如果存在安全管理器并且其checkWrite方法拒绝对文件的写访问
     */
    public static BufferedOutputStream getBufferedOutputStream(File file) throws FileNotFoundException {
        return new BufferedOutputStream(getOutputStream(file));
    }


    /**
     * 文件字符输出流
     * <p>创建一个使用给定字符集的OutputStreamWriter。</p>
     *
     * @param file    要打开以进行写入的文件
     * @param charset 字符集编码
     * @return 文件字符输出流
     * @throws FileNotFoundException 文件不存在时,是目录而不是常规文件，或者由于某些其他原因无法打开读取。
     * @throws SecurityException     如果存在安全管理器并且其checkRead方法拒绝对文件的读取访问。
     */
    public static OutputStreamWriter getOutputStreamWriter(File file, Charset charset) throws FileNotFoundException {
        return new OutputStreamWriter(getOutputStream(file), charset);
    }

    /**
     * 文件字符输出流
     * <p> 默认字符集编码: {@link StandardCharsets#UTF_8}</p>
     * <p>创建一个 {@link StandardCharsets#UTF_8} 字符集的OutputStreamWriter。</p>
     *
     * @param file 要打开以进行写入的文件
     * @return 文件字符输出流
     * @throws FileNotFoundException 文件不存在时,是目录而不是常规文件，或者由于某些其他原因无法打开读取。
     * @throws SecurityException     如果存在安全管理器并且其checkRead方法拒绝对文件的读取访问。
     */
    public static OutputStreamWriter getOutputStreamWriter(File file) throws FileNotFoundException {
        return getOutputStreamWriter(file, StandardCharsets.UTF_8);
    }

    /**
     * 缓冲字符输出流
     * <p>创建一个使用给定大小的输出缓冲区的新缓冲字符输出流。</p>
     * <p>需要指定 处理字符集编码类型</p>
     *
     * @param file    要打开以进行写入的文件
     * @param charset 字符集编码
     * @param size    缓冲区大小, 单位: 字节
     * @return 缓冲字符输出流
     * @throws FileNotFoundException    文件不存在时,是目录而不是常规文件，或者由于某些其他原因无法打开读取。
     * @throws SecurityException        如果存在安全管理器并且其checkRead方法拒绝对文件的读取访问。
     * @throws IllegalArgumentException 如果 size <= 0
     */
    public static BufferedWriter getBufferedWriter(File file, Charset charset, int size) throws FileNotFoundException {
        return new BufferedWriter(getOutputStreamWriter(file, charset), size);
    }

    /**
     * 缓冲字符输出流
     * <p>创建一个使用 8192 字节 (8KB) 的输出缓冲区的新缓冲字符输出流。</p>
     * <p>需要指定 处理字符集编码类型</p>
     *
     * @param file    要打开以进行写入的文件
     * @param charset 字符集编码
     * @return 缓冲字符输出流
     * @throws FileNotFoundException 文件不存在时,是目录而不是常规文件，或者由于某些其他原因无法打开读取。
     * @throws SecurityException     如果存在安全管理器并且其checkRead方法拒绝对文件的读取访问。
     */
    public static BufferedWriter getBufferedWriter(File file, Charset charset) throws FileNotFoundException {
        return new BufferedWriter(getOutputStreamWriter(file, charset));
    }

    /**
     * 缓冲字符输出流
     * <p>创建一个使用给定大小的输出缓冲区的新缓冲字符输出流。</p>
     * <p>默认字符集类型为 {@link StandardCharsets#UTF_8}</p>
     *
     * @param file 要打开以进行写入的文件
     * @param size 缓冲区大小, 单位: 字节
     * @return 缓冲字符输出流
     * @throws FileNotFoundException    文件不存在时,是目录而不是常规文件，或者由于某些其他原因无法打开读取。
     * @throws SecurityException        如果存在安全管理器并且其checkRead方法拒绝对文件的读取访问。
     * @throws IllegalArgumentException 如果 size <= 0
     */
    public static BufferedWriter getBufferedWriter(File file, int size) throws FileNotFoundException {
        return new BufferedWriter(getOutputStreamWriter(file), size);
    }

    /**
     * 缓冲字符输出流
     * <p>创建一个使用 8192 字节 (8KB) 的输出缓冲区的新缓冲字符输出流。</p>
     * <p>默认字符集类型为 {@link StandardCharsets#UTF_8}</p>
     *
     * @param file 要打开以进行写入的文件
     * @return 缓冲字符输出流
     * @throws FileNotFoundException 文件不存在时,是目录而不是常规文件，或者由于某些其他原因无法打开读取。
     * @throws SecurityException     如果存在安全管理器并且其checkRead方法拒绝对文件的读取访问。
     */
    public static BufferedWriter getBufferedWriter(File file) throws FileNotFoundException {
        return new BufferedWriter(getOutputStreamWriter(file));
    }


    /**
     * 获取文件缓冲输入流,默认缓冲大小为 8KB,
     * <p>其中会自动获取 当前文件编码
     *
     * @param file 文件
     * @return 文件输入流
     */
    public static BufferedReader getReader(File file) {
        BufferedReader read = null;
        try {
            // 获取文件编码
            String encoding = EncodingDetect.detect(file);
            // 判断是否是一个真实的文件
            if (FileUtils.isFile(file)) {
                // 考虑到编码格式
                read = getBufferedReader(file, Charset.forName(encoding));
            } else {
                logger.error("找不到指定的文件");
            }
        } catch (Exception e) {
            logger.error("读取文件内容出错", e);
        }
        return read;
    }

    /**
     * 获取文件缓冲输出流, 默认缓冲大小为 8KB,
     * <p>其中会自动获取 当前文件编码,
     *
     * @param file 文件
     * @return 文件输出流
     */
    public static BufferedWriter getWrite(File file) {
        BufferedWriter writer = null;
        try {
            // 获取文件编码
            String encoding = EncodingDetect.detect(file);
            // 判断是否是一个真实的文件
            if (FileUtils.isFile(file)) {
                writer = getBufferedWriter(file, Charset.forName(encoding));
            } else {
                logger.error("找不到指定的文件");
            }
        } catch (Exception e) {
            logger.error("error:write error", e);
        }
        return writer;
    }

    /**
     * 获取文件缓冲输入流,默认缓冲大小为 8KB,
     * <p>其中会自动获取 当前文件编码
     *
     * @param filePath 文件绝对地址
     * @return 文件输入流
     */
    public static BufferedReader getReader(String filePath) {
        return getReader(new File(filePath));
    }

    /**
     * 获取文件缓冲输入流,默认缓冲大小为 8KB,
     * <p>其中会自动获取 当前文件编码
     *
     * @param uri 资源地址
     * @return 文件输入流
     */
    public static BufferedReader getReader(URI uri) {
        return getReader(new File(uri));
    }


    /**
     * 获取文件缓冲输出流, 默认缓冲大小为 8KB,
     * <p>其中会自动获取 当前文件编码,
     *
     * @param filePath 文件绝对地址
     * @return 文件输出流
     */
    public static BufferedWriter getWrite(String filePath) {
        return getWrite(new File(filePath));
    }

    /**
     * 获取文件缓冲输出流, 默认缓冲大小为 8KB,
     * <p>其中会自动获取 当前文件编码,
     *
     * @param uri 资源地址
     * @return 文件输出流
     */
    public static BufferedWriter getWrite(URI uri) {
        return getWrite(new File(uri));
    }

}
