package xyz.liangxin.utils.database;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.liangxin.utils.core.text.CharSequenceUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author liangxin
 * @version V1.0
 * @Package xyz.liangxin.springbootdemo.common.utils.database
 * @date 2021/3/27 14:41
 * @Description JDBC 工具类
 */
public class JdbcUtils implements DbUtils, Serializable {

    private static final long serialVersionUID = 6355446233360942517L;

    /**
     * 将将每一行的结果存储为 Map , 并保存在 集合中返回
     */
    public static final Function<ResultSet, List<Map<String, Object>>> RESULTS_TO_MAP = JdbcUtils::resultSetToMap;


    /**
     * 存储过程执行后获取的 结果集键
     */
    public static final String PROCEDURE_RESULT = "resultsList";
    /**
     * 存储过程执行后 获取的 输出结果的键
     */
    public static final String PROCEDURE_OUTPUT = "output";

    /**
     * SQL 输出日志格式
     */
    private static final String JDBC_SQL_LOG = "JDBC_SQL   : {}";
    /**
     * SQL 参数输出日志
     */
    private static final String JDBC_PARAMS_LOG = "JDBC_params: {}";
    private static final Logger logger = LoggerFactory.getLogger(JdbcUtils.class);
    /**
     * 数据库驱动
     */
    private String driver;
    /**
     * 数据库主机地址
     */
    private String url;

    /**
     * 连接数据库名
     */
    private String database;
    /**
     * 连接数据库账号
     */
    private String user;
    /**
     * 连接数据库密码
     */
    private String password;


    /**
     * 私有构造函数， 用于单例模式
     */
    private JdbcUtils() {
        init();
    }

    /**
     * 获取单例对象
     *
     * @return 返回 JdbcUtils 单例对象
     */
    public static JdbcUtils getInstance() {
        return JdbcUtilsInstance.JDBC_UTILS;
    }

    /**
     * 对象转换成 JSON 字符串
     * 使用了 fastjson
     *
     * @param params 待转换的对象
     * @return json 字符串
     */
    private static String toJsonString(Object params) {
        return JSON.toJSONString(params);
    }

    /**
     * 将将每一行的结果存储为 Map , 并保存在 集合中返回
     *
     * @param rs 结果集对象
     * @return 返回所有结果(Map) 的集合
     */
    private static List<Map<String, Object>> resultSetToMap(ResultSet rs) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>(metaData.getColumnCount());
                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    map.put(metaData.getColumnName(i + 1), rs.getObject(i + 1));
                }
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 防止反序列化获取多个对象的漏洞
     *
     * @return 返回 JdbcUtils 单例对象
     * @throws ObjectStreamException 读取序列化文件流报错
     */
    private Object readResolve() throws ObjectStreamException {
        return JdbcUtilsInstance.JDBC_UTILS;
    }

    /**
     * 插入操作
     *
     * @param sql    insert SQL语句
     * @param params 预编译参数
     * @return 返回 插入的数据结果行数
     * @throws SQLException 操作数据库错误
     */
    @Override
    public int insert(String sql, Object... params) throws SQLException {
        return (int) execute(null, sql, params);
    }

    /**
     * 修改操作
     *
     * @param sql    update SQL语句
     * @param params 预编译参数
     * @return 返回 修改的数据结果行数
     * @throws SQLException 操作数据库错误
     */
    @Override
    public int update(String sql, Object... params) throws SQLException {
        return (int) execute(null, sql, params);
    }

    /**
     * 删除操作
     *
     * @param sql    delete SQL语句
     * @param params 预编译参数
     * @return 返回 删除的数据结果行数
     * @throws SQLException 操作数据库错误
     */
    @Override
    public int delete(String sql, Object... params) throws SQLException {
        return (int) execute(null, sql, params);
    }


    /**
     * 查询 操作
     *
     * @param c      返回 结果 List 元素的 类型
     * @param sql    sql语句
     * @param params 预编译操作
     * @param <T>    结果类型
     * @return 返回 List<T>
     * @throws SQLException 操作数据库错误
     */
    @Override
    public <T> List<T> select(Class<T> c, String sql, Object... params) throws SQLException {
        return select(rs -> {
            List<T> list = new ArrayList<>();
            try {
                list = resultSetToClassObject(c, rs);
            } catch (SQLException | IllegalAccessException | InstantiationException | InvocationTargetException throwables) {
                throwables.printStackTrace();
            }
            return list;
        }, sql, params);

    }

    /**
     * 执行 查询 sql 语句
     *
     * @param sql    sql语句
     * @param params sql语句中的预编译参数 , 按 sql语句中的占位符顺序
     * @return 返回 List<Map> Map 键名为 列名, 值为对应的值
     * @throws SQLException 操作数据库错误
     */
    @Override
    public List<Map<String, Object>> select(String sql, Object... params) throws SQLException {
        return select(JdbcUtils.RESULTS_TO_MAP, sql, params);
    }

    /**
     * 查询操作
     *
     * @param function 结果集 处理函数
     * @param sql      SQL语句
     * @param params   预编译参数
     * @param <R>      返回值 list 元素 类型
     * @return 查询数据结果
     * @throws SQLException 操作数据库错误
     */
    @Override
    @SuppressWarnings("unchecked")
    public <R> List<R> select(Function<ResultSet, List<R>> function, String sql, Object... params) throws SQLException {
        Object o = execute(function, sql, params);
        if (Objects.nonNull(o) && o instanceof List) {
            return (List<R>) o;
        }
        return new ArrayList<>();
    }

    /**
     * 执行sql (增删改查)
     *
     * @param function 查询时, 处理 结果集的 lambda函数
     * @param sql      sql 语句
     * @param params   预编译参数
     * @param <R>      查询时的结果 返回值
     * @return <p>增删改操作, 返回 影响行数</p>
     * <P>查询操作: 返回 function 处理的结果/P>
     * @throws SQLException 数据库操作错误
     */
    public <R> Object execute(Function<ResultSet, R> function, String sql, Object... params) throws SQLException {
        Object resource;
        Connection connection = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            pstm = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                pstm.setObject(i + 1, params[i]);
            }
            if (pstm.execute()) {
                // 执行查询
                rs = pstm.getResultSet();
                resource = function.apply(rs);
            } else {
                // 执行增删改
                resource = pstm.getUpdateCount();
                connection.commit();
            }

            logger.debug(JDBC_SQL_LOG, sql);
            logger.debug(JDBC_PARAMS_LOG, toJsonString(params));
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            close(rs, pstm, connection);
        }
        return resource;
    }

    /**
     * 传入文件执行sql语句, 文件中, 不能包含 创建存储过程等语句
     * 因为jdbc 不支持 使用其他提交符号 ;; / $$ (也许)
     *
     * @param sqlFile sql 文件
     * @return 返回 sql 文件中, 每行命令的执行结果
     * @throws SQLException 操作数据库错误
     * @throws IOException  操作文件错误
     */
    @Override
    public int[] executeSqlFile(File sqlFile) throws SQLException, IOException {
        int[] rows;
        Connection connection = getConnection();
        Statement stmt = null;
        try {
            List<String> sqlList = loadSql(sqlFile);
            stmt = connection.createStatement();
            for (String sql : sqlList) {
                logger.debug(JDBC_SQL_LOG, sql);
                // 添加批处理
                stmt.addBatch(sql);
            }
            // 执行sql 批处理
            rows = stmt.executeBatch();
            connection.commit();
        } catch (SQLException | IOException e) {
            connection.rollback();
            throw e;
        } finally {
            close(null, stmt, connection);
        }
        return rows;
    }

    /**
     * 执行存储过程
     *
     * @param function 结果集处理函数
     * @param callSql  执行调用存储过程 sql
     * @param input    输入参数 Map<{@link String},{@link Object}>
     * @param output   输出参数 Map<{@link String},{@link java.sql.JDBCType}>
     * @param <R>      结果集处理类型
     * @return 返回 Map 结果
     * <p>Map 中包含两个元素</p>
     * <li>
     * 键名为 {@link JdbcUtils#PROCEDURE_RESULT} : 查询出的结果集  List < List < {@link R} > >
     * </li>
     * <li>
     * 键名为 {@link JdbcUtils#PROCEDURE_OUTPUT} : 执行 存储过程,返回的出参结果 {@code Map<String, Object>}
     * </li>
     */
    @Override
    public <R> Map<String, Object> executeProcedure(Function<ResultSet, List<R>> function, String callSql, Map<String, Object> input, Map<String, SQLType> output) throws SQLException {

        Connection conn = null;
        ResultSet rs = null;
        CallableStatement cstm = null;
        Map<String, Object> outputResult = new HashMap<>(output.size());
        List<List<R>> resultsList = new ArrayList<>();

        Map<String, Object> result = new HashMap<>(2);
        try {
            conn = getConnection();
            cstm = conn.prepareCall(callSql);

            // 循环处理 需要的输入参数
            for (Map.Entry<String, Object> inputEntry : input.entrySet()) {
                cstm.setObject(inputEntry.getKey(), inputEntry.getValue());
            }

            // 循环处理, 需要的输出参数
            for (Map.Entry<String, SQLType> outputEntry : output.entrySet()) {

                cstm.registerOutParameter(outputEntry.getKey(), outputEntry.getValue());
            }

            logger.debug(JDBC_SQL_LOG, callSql);
            logger.debug("JDBC_params-input : {}", toJsonString(input));
            logger.debug("JDBC_params-output: {}", toJsonString(output));

            // 获取 存储过程执行中的结果集
            if (cstm.execute()) {
                rs = cstm.getResultSet();
                List<R> resultList = function.apply(rs);
                resultsList.add(resultList);
                // 如果有多个结果集,则循环处理
                while (cstm.getMoreResults()) {
                    // 不确定是否需要关闭, 稳健一点
                    rs.close();
                    rs = cstm.getResultSet();
                    resultList = function.apply(rs);
                    resultsList.add(resultList);
                }
            }

            // 获取存储过程 输出变量
            for (String outputName : output.keySet()) {
                outputResult.put(outputName, cstm.getObject(outputName));
            }
            conn.commit();

            result.put(PROCEDURE_RESULT, resultsList);
            result.put(PROCEDURE_OUTPUT, outputResult);

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            close(rs, cstm, conn);
        }
        return result;
    }


    /**
     * 读取sql文件,获取sql语句
     * 返回所有sql语句的list集合
     *
     * @param sqlFile sql 文件
     * @return sql语句 集合,集合中每一个元素 为一条sql语句
     * @throws IOException 文件操作失败
     */
    private List<String> loadSql(File sqlFile) throws IOException {
        List<String> sqlList = new ArrayList<>();
        try (
                // 读取文件的内容并写到StringBuffer中去
                InputStream sqlFileIn = new FileInputStream(sqlFile);
                InputStreamReader inputStreamReader = new InputStreamReader(sqlFileIn, StandardCharsets.UTF_8)
        ) {
            StringBuilder sqlSb = new StringBuilder();
            char[] buff = new char[12];
            int byteRead;
            while ((byteRead = inputStreamReader.read(buff)) != -1) {
                sqlSb.append(new String(buff, 0, byteRead));
            }

            /*
             * windows下换行是/r/n，Linux下是/n，
             * 此处需要根据导出的sql文件进行具体的处理，我在处理的时候
             * 也遇到了很多的问题，如果我这个不行可以在网上找找别的解析方法
             */
            String[] sqlArr = sqlSb.toString().split("(;\\s*\\rr\\n)|(;\\s*\\n)|(;)");

            for (String sql : sqlArr) {
                sql = sql.trim();
                if (!"".equals(sql)) {
                    sqlList.add(sql);
                }
            }
        }

        return sqlList;
    }


    /**
     * 加载 配置文件 classpath:jdbc.properties
     *
     * @throws IOException 文件读取错误
     */
    private void loadConfigured() throws IOException {
        //1.加载文件
//        //获取src路径下的文件--->ClassLoader类加载器
        ClassLoader classLoader = JdbcUtils.class.getClassLoader();
        URL resource = classLoader.getResource("jdbc.properties");
        String path = Objects.isNull(resource) ? "" : resource.getPath();
        try (FileReader fileReader = new FileReader(path)) {
            //2.创建Properties集合类
            Properties pro = new Properties();
            pro.load(fileReader);

            //3获取数据
            driver = pro.getProperty("driver");
            database = pro.getProperty("database");
            url = pro.getProperty("url");
            user = pro.getProperty("user");
            password = pro.getProperty("password");
        }
    }

    /**
     * 加载数据库配置
     */
    private void init() {
        try {
            loadConfigured();
            //4.注册驱动
            Class.forName(driver);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        logger.debug("\n url:{} ,\n database:{},\n user:{} ,\n password:{} ,\n driver:{}", url, database, user, password, driver);
    }


    /**
     * 获取连接 , 指定数据库
     *
     * @return 数据库连接
     */
    public Connection getConnection() {

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url + "/" + database, user, password);

            // 取消自动提交
            connection.setAutoCommit(false);
            logger.debug("JDBC: 连接数据库成功");
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 数据库连接关闭
     *
     * @param rs   结果集对象
     * @param st   sql SQL语句 声明,编译,执行 对象
     * @param conn 数据库连接对象
     */
    public void close(ResultSet rs, Statement st, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        logger.debug("JDBC: 数据库连接关闭成功");

    }


    /**
     * 将结果集中每行的数据, 映射进指定类的的对象中,并返回List 集合
     *
     * @param c   结果类型对象
     * @param rs  结果集对象
     * @param <T> 处理返回类型
     * @return 返回处理后的 List 结果
     * @throws SQLException              数据库操作错误
     * @throws IllegalAccessException    非法访问实例错误
     * @throws InstantiationException    实例化异常
     * @throws InvocationTargetException 调用目标异常
     */
    private <T> List<T> resultSetToClassObject(Class<T> c, ResultSet rs) throws SQLException, IllegalAccessException, InstantiationException, InvocationTargetException {

        List<T> list = new ArrayList<>();

        // 获取 c 类 中的 所有 set 方法
        Map<String, Method> methodMap = Arrays.stream(c.getDeclaredMethods()).filter(method -> method.getName().contains("set")).collect(Collectors.toMap(Method::getName, method -> method));

        // 用于获取当前结果集中的 列名
        ResultSetMetaData metaData = rs.getMetaData();

        while (rs.next()) {
            // 初始化 待填充的对象模型
            T t = c.newInstance();
            // 循环遍历 所有列
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                // 获取当前列的列名
                String columnName = metaData.getColumnName(i + 1);
                // 如果当前列名在 结果集中不存在则跳过 , 一般不存在这种情况
                if (Objects.isNull(rs.getObject(columnName))) {
                    continue;
                }

                // 将 数据库中的列名, 转换成 驼峰式 属性名
                String methodName = CharSequenceUtil.toCamelCase(columnName);
                // 拼接获取 对应的 set方法名
                // userName ==> setUserName
                methodName = "set" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
                // 获取 对应的方法, 如果没有则返回null
                Method method = methodMap.getOrDefault(methodName, null);
                // 如果有对应的方法
                if (Objects.nonNull(method)) {
/*
                     获取当前set方法的参数类型
                    Class<?> parameterType = method.getParameterTypes()[0];
                     通过 hutool 工具类, 将 对应的结果, 转换成 对应的类型
                    Object parameter = Convert.convert(parameterType, rs.getObject(columnName));
 */
                    Object parameter =rs.getObject(columnName);
                    // 通过方对象, 将值 反射注入到对象中 对应的值
                    method.invoke(t, parameter);
                }
            }

            list.add(t);
        }
        return list;
    }

    /**
     * 防止反射获取多个对象的漏洞
     */
    private static class JdbcUtilsInstance {
        private static final JdbcUtils JDBC_UTILS = new JdbcUtils();
    }

}
