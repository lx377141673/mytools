package xyz.liangxin.utils.core;


/**
 * 函数信息统计
 *
 * @author liangxin
 */
public class FunctionInfoStatistics {

    /**
     * 代码函数执行时长
     *
     * @param apply 执行代码段
     * @return 执行时长 毫秒
     */
    public static long codeTime(CodeConsumer apply) {
        long begin = System.currentTimeMillis();
        apply.accept();
        return System.currentTimeMillis() - begin;
    }

    /**
     * 代码函数执行内存消耗 字节[Bytes]
     * <strong>仅支持单线程运行环境下 </strong>
     * <ul>
     *     <li> 1KB = 1024B; </li>
     *     <li> 1MB = 1024KB; </li>
     *     <li> 1GB = 1024MB; </li>
     * </ul>
     *
     * @param apply 执行代码段
     * @return 执行消耗内存 字节[Bytes] 1kb = 1024b;
     */
    @SuppressWarnings("all")
    public static long codeMemory(CodeConsumer apply) {
        Runtime runtime = Runtime.getRuntime();
        //计算内存前先垃圾回收一次
        runtime.gc();
        long startMemory = runtime.freeMemory();
        apply.accept();
        return startMemory - runtime.freeMemory();
    }

    /**
     * 无参数 消费函数
     */
    @FunctionalInterface
    public interface CodeConsumer {
        /**
         * 消费函数无参数,无返回值
         */
        void accept();
    }

}


