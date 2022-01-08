package xyz.liangxin.utils.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * FastJson 工具包
 *
 * @author liangxin
 * @version V1.0
 * @date 2021/4/12 20:51
 */
public class FastJsonUtils {
    /**
     * 设置 FastJson 序列化时的特性组
     */
    private static final SerializerFeature[] FEATURES = {
            // 输出空置字段
            SerializerFeature.WriteMapNullValue,
            // list字段如果为null，输出为[]，而不是null
            SerializerFeature.WriteNullListAsEmpty,
            // 数值字段如果为null，输出为0，而不是null
            SerializerFeature.WriteNullNumberAsZero,
            // Boolean字段如果为null，输出为false，而不是null
            SerializerFeature.WriteNullBooleanAsFalse,
            // 字符类型字段如果为null，输出为""，而不是null
            SerializerFeature.WriteNullStringAsEmpty,
            // 输出时使用 时间格式 YYYY-MM-DD HH:mm:dd
            SerializerFeature.WriteDateUseDateFormat,
            // 禁用循环参考检测
            SerializerFeature.DisableCircularReferenceDetect
    };
    private static final Logger logger = LoggerFactory.getLogger(FastJsonUtils.class);
    private static final SerializeConfig CONFIG;

    static {
        CONFIG = new SerializeConfig();
        init(CONFIG);
    }

    private FastJsonUtils() {
    }

    /**
     * 设置 FastJson 序列化时的配置
     *
     * @param config fastjson 配置包装类
     */
    public static void init(SerializeConfig config) {
/*
使用和json-lib兼容的日期输出格式
CONFIG.put(java.util.Date.class, new JSONLibDataFormatSerializer());
使用和json-lib兼容的日期输出格式
CONFIG.put(java.sql.Date.class, new JSONLibDataFormatSerializer());
 */
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormatSerializer simpleDateFormatSerializer = new SimpleDateFormatSerializer(dateFormat);
        // 配置 时间类型输出格式
        config.put(Date.class, simpleDateFormatSerializer);
        config.put(java.sql.Date.class, simpleDateFormatSerializer);
    }

    public static SerializerFeature[] getFastJsonFeatures() {
        return FEATURES;
    }

    /**
     * 对象转换成 JSON 字符串
     *
     * @param object 转换对象
     * @return JSON 字符串
     */
    public static String toJSONString(Object object) {
        return JSON.toJSONString(object, CONFIG, FEATURES);
    }

    /**
     * 对象转换成 JSON 字符串  (默认功能配置)
     *
     * @param object 转换对象
     * @return JSON 字符串
     */
    public static String toJSONNoFeatures(Object object) {
        return JSON.toJSONString(object, CONFIG);
    }

    /**
     * JSON字符串转换成对象
     *
     * @param json JSON字符串
     * @return 对象
     */
    public static Object parse(String json) {
        return JSON.parse(json);
    }

    /**
     * JSON字符串转换成指定类型对象
     *
     * @param json JSON字符串
     * @param c    类型
     * @param <T>  泛型
     * @return 对象
     */
    public static <T> T parse(String json, Class<T> c) {
        return JSON.parseObject(json, c);
    }

    /**
     * JSON 字符串转换成 {@link JSONObject}
     *
     * @param json json 字符串
     * @return 返回 {@link JSONObject}
     */
    public static JSONObject parseJsonObject(String json) {
        return JSON.parseObject(json);
    }

    /**
     * JSON 字符串转换成 指定元素类型的 List 集合
     *
     * @param json JSON 字符串
     * @param c    类型
     * @param <T>  泛型
     * @return List 集合
     */
    public static <T> List<T> parseList(String json, Class<T> c) {
        return JSON.parseArray(json, c);
    }

    /**
     * JSON 字符串转换成 List 集合
     *
     * @param json JSON 字符串
     * @return List 集合
     */
    public static List parseList(String json) {
        return JSON.parseArray(json);
    }

    /**
     * JSON 字符串转换成 Map 集合
     *
     * @param json JSON 字符串
     * @return Map 集合
     */
    public static Map parseMap(String json) {
        return JSON.parseObject(json);
    }

    /**
     * JSON 字符串 转换成 数组
     *
     * @param json json 字符串
     * @return 数组
     */
    public static Object[] toArray(String json) {
        return toArray(json, null);
    }

    /**
     * JSON 字符串 转换成 数组
     *
     * @param json  json 字符串
     * @param clazz 类型
     * @param <T>   泛型
     * @return 数组
     */
    public static <T> Object[] toArray(String json, Class<T> clazz) {
        return parseList(json, clazz).toArray();
    }

}
