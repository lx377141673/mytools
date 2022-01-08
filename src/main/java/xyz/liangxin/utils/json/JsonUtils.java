package xyz.liangxin.utils.json;


import com.fasterxml.jackson.databind.JsonNode;
import xyz.liangxin.utils.core.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Json 工具类, 此版本工具类, 使用的{@link JacksonUtils}
 *
 * @author liangxin
 * @version V1.0
 * @date 2021/4/17 23:20
 */
public class JsonUtils {

    private JsonUtils() {
    }

    /**
     * 对象转换成 JSON 字符串
     *
     * @param object 转换对象
     * @return JSON 字符串
     * <p> 如果为 json 字符串为null or "" , 则返回 null </p>
     */
    public static String toJSONString(Object object) {
        return Optional.ofNullable(object).map(JacksonUtils::toJSONString).orElse(null);
    }

    /**
     * JSON字符串转换成对象
     *
     * @param json JSON字符串
     * @param <T>  泛型
     * @return 反序列化后的对象
     * <p> 如果为 json 字符串为null or "" , 则返回 null </p>
     */
    public static <T> T parse(String json) {
        Object o = ObjectUtils.filterStringNotEmpty(json).map(JacksonUtils::parse).orElse(null);
        return ObjectUtils.checked(o);
    }

    /**
     * JSON字符串转换成对象
     *
     * @param json  JSON字符串
     * @param clazz 显示声明 需要转换的 类型
     * @param <T>   泛型
     * @return 反序列化后的对象
     * <p> 如果为 json 字符串为null or "" , 则返回 null </p>
     */
    public static <T> T parse(String json, Class<T> clazz) {
        return ObjectUtils.filterStringNotEmpty(json).map(x -> JacksonUtils.parse(x, clazz)).orElse(null);
    }

    /**
     * JSON 字符串转换成 {@link JsonNode}
     *
     * @param json json 字符串
     * @return 返回 {@link JsonNode}
     */
    public static JsonNode parseJson(String json) {
        return ObjectUtils.filterStringNotEmpty(json).map(JacksonUtils::parseJsonNode).orElse(null);
    }

    /**
     * JSON 字符串转换成 指定元素类型的 List 集合
     *
     * @param json JSON字符串
     * @param <T>  泛型
     * @return 反序列化后的 List 对象
     * <p> 如果为 json 字符串为null or "" , 则返回 {@link Collections#emptyList()} </p>
     * <p> 需要注意的是, 如果返回 {@link Collections#emptyList()} , 该  List 是一个视图对象, 不允许进行 增加, 修改等操作</p>
     */
    public static <T> List<T> parseList(String json) {
        List<Object> list = ObjectUtils.filterStringNotEmpty(json).map(JacksonUtils::parseList).orElse(Collections.emptyList());
        return ObjectUtils.checked(list);
    }

    /**
     * JSON 字符串转换成 指定元素类型的 {@code Map<String,T> }集合
     *
     * @param json JSON字符串
     * @param <T>  泛型
     * @return 反序列化后的 Map 对象
     * <p> 如果为 json 字符串为null or "" , 则返回 {@link Collections#emptyMap()} </p>
     * <p> 需要注意的是, 如果返回 {@link Collections#emptyMap()} , 该  Map 是一个视图对象, 不允许进行 增加, 修改等操作</p>
     */
    public static <T> Map<String, T> parseMap(String json) {
        Map<String, Object> map = ObjectUtils.filterStringNotEmpty(json).map(JacksonUtils::parseMap).orElse(Collections.emptyMap());
        return ObjectUtils.checked(map);
    }

    /**
     * JSON 字符串 转换成 数组 T[]
     *
     * @param json JSON字符串
     * @param <T>  泛型
     * @return 反序列化后的 数组对象
     */
    public static <T> T[] parseArray(String json) {
        Object[] objects = ObjectUtils.filterStringNotEmpty(json).map(JacksonUtils::toArray).orElse(null);
        return ObjectUtils.checked(objects);
    }

}
