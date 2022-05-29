package xyz.liangxin.utils.database;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLType;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 数据库操作工具类接口
 *
 * @author liangxin
 * @version V1.0
 * @date 2021/3/27 14:40
 */
public interface DbUtils {


    /**
     * 插入操作
     *
     * @param sql    insert SQL语句
     * @param params 预编译参数
     * @return 返回 插入的数据结果行数
     * @throws SQLException 数据库操作错误
     */
    int insert(String sql, Object... params) throws SQLException;

    /**
     * 修改操作
     *
     * @param sql    update SQL语句
     * @param params 预编译参数
     * @return 返回 修改的数据结果行数
     * @throws SQLException 数据库操作错误
     */
    int update(String sql, Object... params) throws SQLException;

    /**
     * 删除操作
     *
     * @param sql    delete SQL语句
     * @param params 预编译参数
     * @return 返回 删除的数据结果行数
     * @throws SQLException 数据库操作错误
     */
    int delete(String sql, Object... params) throws SQLException;

    /**
     * 查询 操作
     *
     * @param c      返回 结果 List 元素的 类型
     * @param sql    sql语句
     * @param params 预编译操作
     * @param <T>    结果类型
     * @return 返回 List<T>
     * @throws SQLException 数据库操作错误
     */
    <T> List<T> select(Class<T> c, String sql, Object... params) throws SQLException;

    /**
     * 查询操作
     *
     * @param function 结果集 处理函数
     * @param sql      SQL语句
     * @param params   预编译参数
     * @param <R>      返回值 list 元素 类型
     * @return 返回值 list
     * @throws SQLException 数据库操作错误
     */
    <R> List<R> select(Function<ResultSet, List<R>> function, String sql, Object... params) throws SQLException;

    /**
     * 执行 查询 sql 语句
     *
     * @param sql    sql语句
     * @param params sql语句中的预编译参数 , 按 sql语句中的占位符顺序
     * @return 返回 List<Map> Map 键名为 列名, 值为对应的值
     * @throws SQLException 数据库操作错误
     */
    List<Map<String, Object>> select(String sql, Object... params) throws SQLException;

    /**
     * 传入文件执行sql语句
     *
     * @param sqlFile sql 文件
     * @return 返回 sql 文件中, 每行命令的执行结果
     * @throws SQLException 数据库操作错误
     * @throws IOException  文件操作错误
     */
    int[] executeSqlFile(File sqlFile) throws SQLException, IOException;


    /**
     *
     * 执行存储过程
     *
     * @param function 结果集处理函数
     * @param callSql  执行调用存储过程 sql
     * @param input    输出参数
     * @param output   输出参数
     * @param <R>      结果集处理类型
     * @return 返回 Map 结果
     * @throws SQLException SQL 执行异常
     */
    <R> Map<String, Object> executeProcedure(Function<ResultSet, List<R>> function, String callSql, Map<String, Object> input, Map<String, SQLType> output) throws SQLException;
}

