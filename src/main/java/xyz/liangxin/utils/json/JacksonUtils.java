package xyz.liangxin.utils.json;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.liangxin.utils.core.date.DateUtils;
import xyz.liangxin.utils.core.text.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Jackson 工具包
 *
 * @author liangxin
 * @version V1.0
 *
 * @date 2021/4/13 2:20

 */
public class JacksonUtils {
    private static final Logger logger = LoggerFactory.getLogger(JacksonUtils.class);
    /**
     * jackson 对象映射转换器
     */
    public static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        // 设置时区
        OBJECT_MAPPER.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        // 设置时间格式
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat(DateUtils.DATE_PATTERN_YEAR_TO_SECOND));

        // 解析所有类型,里的所有 从私有的到公共的属性
        OBJECT_MAPPER.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
/*
         激活默认类型 启用自动包含类型信息的方法，默认类型需要适当反序列化多态类型(除非类型已被JsonTypeInfo注释)。
        OBJECT_MAPPER.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);
 */

        // 声明自定义模块,配置double类型序列化配置
        SimpleModule module = new SimpleModule("DoubleSerializer");
        // 注意Double和double需要分配配置
        module.addSerializer(Double.class, new DoubleSerializer());
        module.addSerializer(double.class, new DoubleSerializer());

        // 注册模块
        OBJECT_MAPPER.registerModule(getJavaTimeModule())
                .registerModule(module)
                .registerModule(new Jdk8Module())
                .registerModule(new ParameterNamesModule()).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    /**
     * 初始化配置 javaTime 模块的序列化操作
     *
     * @return 转换模块配置
     */
    public static JavaTimeModule getJavaTimeModule() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        // 序列化配置,针对java8 时间
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DateUtils.DATE_PATTERN_YEAR_TO_SECOND)));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DateUtils.DATE_PATTERN_YEAR_TO_DAY)));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DateUtils.DATE_PATTERN_YEAR_TO_DAY)));

        // 反序列化配置,针对java8 时间
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DateUtils.DATE_PATTERN_YEAR_TO_SECOND)));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DateUtils.DATE_PATTERN_YEAR_TO_DAY)));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DateUtils.DATE_PATTERN_YEAR_TO_DAY)));
        return javaTimeModule;
    }

    private JacksonUtils() {

    }

    public static ObjectMapper getInstance() {
        return OBJECT_MAPPER;
    }

    /**
     * 对象转换成 JSON 字符串
     *
     * @param object 转换对象
     * @return JSON 字符串
     */
    public static String toJSONString(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * javaBean、列表数组转换为json字符串,忽略空值
     *
     * @param object 转换对象
     * @return JSON 字符串
     */
    public static String toJSONStringIgnoreNull(Object object) {


        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * JSON字符串转换成对象
     *
     * @param json JSON字符串
     * @return 对象
     */
    public static Object parse(String json) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        return parse(json, Object.class);
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
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        OBJECT_MAPPER.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        try {
            return OBJECT_MAPPER.readValue(json, c);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * JSON 字符串转换成 {@link JsonNode}
     *
     * @param json json 字符串
     * @return 返回 {@link JsonNode}
     */
    public static JsonNode parseJsonNode(String json) {
        try {
            if (StringUtils.isEmpty(json)) {
                return null;
            }
            return OBJECT_MAPPER.readTree(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * JSON 字符串转换成 指定元素类型的 List 集合
     *
     * @param json json 字符串
     * @param <T>  泛型
     * @return 返回指定类型的 List集合
     */
    public static <T> List<T> parseList(String json) {
        if (StringUtils.isEmpty(json)) {
            return new ArrayList<>();
        }
        List<T> list = new ArrayList<>();
        try {
            list = OBJECT_MAPPER.readValue(json, new TypeReference<List<T>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * json 转换成 Map
     *
     * @param json JSON 字符串
     * @return Map 集合
     */
    public static <T> Map<String, T> parseMap(String json) {
        if (StringUtils.isEmpty(json)) {
            return Collections.emptyMap();
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        Map<String, T> map = null;
        try {
            map = mapper.readValue(json, new TypeReference<Map<String, T>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 深度转换json成map
     *
     * @param json json字符串
     * @return Map 集合
     */
    public static Map<String, Object> parseMapDeeply(String json) {
        if (StringUtils.isEmpty(json)) {
            return Collections.emptyMap();
        }
        try {
            return json2MapRecursion(json, OBJECT_MAPPER);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }

    /**
     * 获取泛型的Collection Type
     *
     * @param collectionClass 泛型的Collection
     * @param elementClasses  元素类
     * @return JavaType Java类型
     * @since 1.0
     */
    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return OBJECT_MAPPER.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    /**
     * Map 转换成 JavaBean
     *
     * @param map   map集合
     * @param clazz Bean 类型
     * @param <T>   泛型
     * @return 返回 Bean 对象
     */
    public static <T> T toBean(Map<?,?> map, Class<T> clazz) {
        return OBJECT_MAPPER.convertValue(map, clazz);
    }

    /**
     * Map 转换成指定类型的 Bean
     *
     * @param obj   map 对象
     * @param clazz Bean 类型
     * @param <T>   泛型
     * @return 返回 Bean 对象
     */
    public static <T> T toBean(Object obj, Class<T> clazz) {
        return OBJECT_MAPPER.convertValue(obj, clazz);
    }


    /**
     * JSON 字符串 转换成 数组
     *
     * @param json json 字符串
     * @return 数组
     */
    public static Object[] toArray(String json) {
        return parseList(json).toArray();
    }


    /**
     * 把json解析成list，如果list内部的元素存在jsonString，继续解析
     *
     * @param json   JSON 字符串
     * @param mapper 解析工具
     * @return List 集合
     * @throws JsonProcessingException Json处理异常
     */
    private static List<?> json2ListRecursion(String json, ObjectMapper mapper) throws JsonProcessingException {
        if (json == null) {
            return new ArrayList<>();
        }

        List<Object> list = mapper.readValue(json, List.class);
        for (int i = 0; i < list.size(); i++) {
            Object o = list.get(i);
            if (o instanceof String) {
                String str = String.valueOf(o);
                if (str.startsWith("[")) {
                    list.set(i,json2ListRecursion(str, mapper));
                } else if (str.startsWith("{")) {
                    list.set(i,json2MapRecursion(str, mapper));
                }
            }
        }
        return list;
    }


    /**
     * 把json解析成map，如果map内部的value存在jsonString，继续解析
     *
     * @param json   json 字符串
     * @param mapper 解析器
     * @return Map 集合
     * @throws JsonProcessingException Json处理异常
     */
    private static Map<String, Object> json2MapRecursion(String json, ObjectMapper mapper) throws JsonProcessingException {
        if (json == null) {
            return Collections.emptyMap();
        }
        Map<String, Object> map = mapper.readValue(json, Map.class);

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object obj = entry.getValue();

            if (obj instanceof String) {
                String str = ((String) obj);
                if (str.startsWith("[")) {
                    List<?> list = json2ListRecursion(str, mapper);
                    map.put(entry.getKey(), list);
                } else if (str.startsWith("{")) {
                    Map<String, Object> mapRecursion = json2MapRecursion(str, mapper);
                    map.put(entry.getKey(), mapRecursion);
                }
            }
        }
        return map;
    }


}



