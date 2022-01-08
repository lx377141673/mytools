package xyz.liangxin.utils.web.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * Http 工具类 ， 基于 HttpClient
 *
 * @author liangxin
 * @version V1.0
 * @date 2021/4/30 17:11
 */
public class HttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);
    private static final HttpUtilInterface HTTP_UTIL_INTERFACE = new HttpClientUtils();

    private HttpUtils() {
    }

    /**
     * get 请求
     *
     * @param url         请求地址
     * @param params      请求参数 {@link Map}
     * @param headers     请求头参数
     * @param httpContext httpContxt 参数
     * @param charset     响应字符集类型
     * @return 请求响应信息
     */
    public static ResponseBody get(String url, Map<String, Object> params, Map<String, String> headers, Map<String, String> httpContext, Charset charset) {
        return HTTP_UTIL_INTERFACE.get(url, params, headers, httpContext, charset);
    }

    /**
     * get 请求
     * 默认字符集 {@link java.nio.charset.StandardCharsets#UTF_8}
     *
     * @param url         请求地址
     * @param params      请求参数 {@link Map}
     * @param headers     请求头参数
     * @param httpContext httpContxt 参数
     * @return 请求响应信息
     */
    public static ResponseBody get(String url, Map<String, Object> params, Map<String, String> headers, Map<String, String> httpContext) {
        return get(url, params, headers, httpContext, null);
    }

    /**
     * get 请求
     *
     * @param url     请求地址
     * @param params  请求参数 {@link Map}
     * @param headers 请求头参数
     * @param charset 响应字符集类型
     * @return 请求响应信息
     */
    public static ResponseBody get(String url, Map<String, Object> params, Map<String, String> headers, Charset charset) {
        return get(url, params, headers, null, charset);
    }

    /**
     * get 请求
     * 默认字符集 {@link java.nio.charset.StandardCharsets#UTF_8}
     *
     * @param url     请求地址
     * @param params  请求参数 {@link Map}
     * @param headers 请求头参数
     * @return 请求响应信息
     */
    public static ResponseBody get(String url, Map<String, Object> params, Map<String, String> headers) {
        return get(url, params, headers, null, null);
    }

    /**
     * get 请求
     *
     * @param url     请求地址
     * @param params  请求参数 {@link Map}
     * @param charset 响应字符集类型
     * @return 请求响应信息
     */
    public static ResponseBody get(String url, Map<String, Object> params, Charset charset) {
        return get(url, params, null, charset);
    }

    /**
     * get 请求
     * 默认字符集 {@link java.nio.charset.StandardCharsets#UTF_8}
     *
     * @param url    请求地址
     * @param params 请求参数 {@link Map}
     * @return 请求响应信息
     */
    public static ResponseBody get(String url, Map<String, Object> params) {
        return get(url, params, null, null, null);
    }

    /**
     * get 请求
     *
     * @param url         请求地址
     * @param json        请求参数 - json 数据格式
     * @param headers     请求头参数
     * @param httpContext httpContext 参数
     * @param charset     响应字符集类型
     * @return 请求响应信息
     */
    public static ResponseBody get(String url, String json, Map<String, String> headers, Map<String, String> httpContext, Charset charset) {
        return HTTP_UTIL_INTERFACE.get(url, json, headers, httpContext, charset);
    }

    /**
     * get 请求
     * 默认字符集 {@link java.nio.charset.StandardCharsets#UTF_8}
     *
     * @param url         请求地址
     * @param json        请求参数 - json 数据格式
     * @param headers     请求头参数
     * @param httpContext httpContext 参数
     * @return 请求响应信息
     */
    public static ResponseBody get(String url, String json, Map<String, String> headers, Map<String, String> httpContext) {
        return get(url, json, headers, httpContext, null);
    }

    /**
     * get 请求
     *
     * @param url     请求地址
     * @param json    请求参数 - json 数据格式
     * @param headers 请求头参数
     * @param charset 响应字符集类型
     * @return 请求响应信息
     */
    public static ResponseBody get(String url, String json, Map<String, String> headers, Charset charset) {
        return get(url, json, headers, null, charset);
    }

    /**
     * get 请求
     * 默认字符集 {@link java.nio.charset.StandardCharsets#UTF_8}
     *
     * @param url     请求地址
     * @param json    请求参数 - json 数据格式
     * @param headers 请求头参数
     * @return 请求响应信息
     */
    public static ResponseBody get(String url, String json, Map<String, String> headers) {
        return get(url, json, headers, null, null);
    }

    /**
     * get 请求
     *
     * @param url     请求地址
     * @param json    请求参数 - json 数据格式
     * @param charset 响应字符集类型
     * @return 请求响应信息
     */
    public static ResponseBody get(String url, String json, Charset charset) {
        return get(url, json, null, charset);
    }

    /**
     * get 请求
     * 默认字符集 {@link java.nio.charset.StandardCharsets#UTF_8}
     *
     * @param url  请求地址
     * @param json 请求参数 - json 数据格式
     * @return 请求响应信息
     */
    public static ResponseBody get(String url, String json) {
        return get(url, json, null, null, null);
    }

    /**
     * get 请求
     *
     * @param url     请求地址
     * @param charset 响应字符集类型
     * @return 请求响应信息
     */
    public static ResponseBody get(String url, Charset charset) {
        return get(url, "", charset);
    }

    /**
     * get 请求
     * 默认字符集 {@link java.nio.charset.StandardCharsets#UTF_8}
     *
     * @param url 请求地址
     * @return 请求响应信息
     */
    public static ResponseBody get(String url) {
        return get(url, "");
    }

    /**
     * post 请求
     *
     * @param url         请求地址
     * @param params      请求参数 - body:form-data
     * @param headers     请求头参数
     * @param httpContext httpContext 参数
     * @param charset     响应字符集类型
     * @return 请求响应信息
     */
    public static ResponseBody post(String url, Map<String, Object> params, Map<String, String> headers, Map<String, String> httpContext, Charset charset) {
        return HTTP_UTIL_INTERFACE.post(url, params, headers, httpContext, charset);
    }

    /**
     * post 请求
     * 默认字符集 {@link java.nio.charset.StandardCharsets#UTF_8}
     *
     * @param url         请求地址
     * @param params      请求参数 - body:form-data
     * @param headers     请求头参数
     * @param httpContext httpContext 参数
     * @return 请求响应信息
     */
    public static ResponseBody post(String url, Map<String, Object> params, Map<String, String> headers, Map<String, String> httpContext) {
        return post(url, params, headers, httpContext, null);
    }

    /**
     * post 请求
     *
     * @param url     请求地址
     * @param params  请求参数 - body:form-data
     * @param headers 请求头参数
     * @param charset 响应字符集类型
     * @return 请求响应信息
     */
    public static ResponseBody post(String url, Map<String, Object> params, Map<String, String> headers, Charset charset) {
        return post(url, params, headers, null, charset);
    }

    /**
     * post 请求
     * 默认字符集 {@link java.nio.charset.StandardCharsets#UTF_8}
     *
     * @param url     请求地址
     * @param params  请求参数 - body:form-data
     * @param headers 请求头参数
     * @return 请求响应信息
     */
    public static ResponseBody post(String url, Map<String, Object> params, Map<String, String> headers) {
        return post(url, params, headers, null, null);
    }

    /**
     * post 请求
     *
     * @param url     请求地址
     * @param params  请求参数 - body:form-data
     * @param charset 响应字符集类型
     * @return 请求响应信息
     */
    public static ResponseBody post(String url, Map<String, Object> params, Charset charset) {
        return post(url, params, null, charset);
    }

    /**
     * post 请求
     * 默认字符集 {@link java.nio.charset.StandardCharsets#UTF_8}
     *
     * @param url    请求地址
     * @param params 请求参数 - body:form-data
     * @return 请求响应信息
     */
    public static ResponseBody post(String url, Map<String, Object> params) {
        return post(url, params, null, null, null);
    }

    /**
     * post 请求
     *
     * @param url         请求地址
     * @param json        请求参数 - body:json
     * @param headers     请求头参数
     * @param httpContext httpContext 参数
     * @param charset     响应字符集类型
     * @return 请求响应信息
     */
    public static ResponseBody post(String url, String json, Map<String, String> headers, Map<String, String> httpContext, Charset charset) {
        return HTTP_UTIL_INTERFACE.post(url, json, headers, httpContext, charset);
    }

    /**
     * post 请求
     * 默认字符集 {@link java.nio.charset.StandardCharsets#UTF_8}
     *
     * @param url         请求地址
     * @param json        请求参数 - body:json
     * @param headers     请求头参数
     * @param httpContext httpContext 参数
     * @return 请求响应信息
     */
    public static ResponseBody post(String url, String json, Map<String, String> headers, Map<String, String> httpContext) {
        return post(url, json, headers, httpContext, null);
    }

    /**
     * post 请求
     *
     * @param url     请求地址
     * @param json    请求参数 - body:json
     * @param headers 请求头参数
     * @param charset 响应字符集类型
     * @return 请求响应信息
     */
    public static ResponseBody post(String url, String json, Map<String, String> headers, Charset charset) {
        return post(url, json, headers, null, charset);
    }

    /**
     * post 请求
     * 默认字符集 {@link java.nio.charset.StandardCharsets#UTF_8}
     *
     * @param url     请求地址
     * @param json    请求参数 - body:json
     * @param headers 请求头参数
     * @return 请求响应信息
     */
    public static ResponseBody post(String url, String json, Map<String, String> headers) {
        return post(url, json, headers, null, null);
    }

    /**
     * post 请求
     *
     * @param url     请求地址
     * @param json    请求参数 - body:json
     * @param charset 响应字符集类型
     * @return 请求响应信息
     */
    public static ResponseBody post(String url, String json, Charset charset) {
        return post(url, json, null, charset);
    }

    /**
     * post 请求
     * 默认字符集 {@link java.nio.charset.StandardCharsets#UTF_8}
     *
     * @param url  请求地址
     * @param json 请求参数 - body:json
     * @return 请求响应信息
     */
    public static ResponseBody post(String url, String json) {
        return post(url, json, null, null, null);
    }

    /**
     * post 请求
     *
     * @param url     请求地址
     * @param charset 响应字符集类型
     * @return 请求响应信息
     */
    public static ResponseBody post(String url, Charset charset) {
        return post(url, "", charset);
    }

    /**
     * post 请求
     * 默认字符集 {@link java.nio.charset.StandardCharsets#UTF_8}
     *
     * @param url 请求地址
     * @return 请求响应信息
     */
    public static ResponseBody post(String url) {
        return post(url, "");
    }


    /**
     * put 请求
     *
     * @param url         请求地址
     * @param params      请求参数 - body:form-data
     * @param headers     请求头参数
     * @param httpContext httpContext 参数
     * @param charset     响应字符集类型
     * @return 请求响应信息
     */
    public static ResponseBody put(String url, Map<String, Object> params, Map<String, String> headers, Map<String, String> httpContext, Charset charset) {
        return HTTP_UTIL_INTERFACE.put(url, params, headers, httpContext, charset);
    }

    /**
     * put 请求
     * 默认字符集 {@link java.nio.charset.StandardCharsets#UTF_8}
     *
     * @param url         请求地址
     * @param params      请求参数 - body:form-data
     * @param headers     请求头参数
     * @param httpContext httpContext 参数
     * @return 请求响应信息
     */
    public static ResponseBody put(String url, Map<String, Object> params, Map<String, String> headers, Map<String, String> httpContext) {
        return put(url, params, headers, httpContext, null);
    }

    /**
     * put 请求
     *
     * @param url     请求地址
     * @param params  请求参数 - body:form-data
     * @param headers 请求头参数
     * @param charset 响应字符集类型
     * @return 请求响应信息
     */
    public static ResponseBody put(String url, Map<String, Object> params, Map<String, String> headers, Charset charset) {
        return put(url, params, headers, null, charset);
    }

    /**
     * put 请求
     * 默认字符集 {@link java.nio.charset.StandardCharsets#UTF_8}
     *
     * @param url     请求地址
     * @param params  请求参数 - body:form-data
     * @param headers 请求头参数
     * @return 请求响应信息
     */
    public static ResponseBody put(String url, Map<String, Object> params, Map<String, String> headers) {
        return put(url, params, headers, null, null);
    }

    /**
     * put 请求
     *
     * @param url     请求地址
     * @param params  请求参数 - body:form-data
     * @param charset 响应字符集类型
     * @return 请求响应信息
     */
    public static ResponseBody put(String url, Map<String, Object> params, Charset charset) {
        return put(url, params, null, charset);
    }

    /**
     * put 请求
     * 默认字符集 {@link java.nio.charset.StandardCharsets#UTF_8}
     *
     * @param url    请求地址
     * @param params 请求参数 - body:form-data
     * @return 请求响应信息
     */
    public static ResponseBody put(String url, Map<String, Object> params) {
        return put(url, params, null, null, null);
    }

    /**
     * put 请求
     *
     * @param url         请求地址
     * @param json        请求参数 - body:json
     * @param headers     请求头参数
     * @param httpContext httpContext 参数
     * @param charset     响应字符集类型
     * @return 请求响应信息
     */
    public static ResponseBody put(String url, String json, Map<String, String> headers, Map<String, String> httpContext, Charset charset) {
        return HTTP_UTIL_INTERFACE.put(url, json, headers, httpContext, charset);
    }

    /**
     * put 请求
     * 默认字符集 {@link java.nio.charset.StandardCharsets#UTF_8}
     *
     * @param url         请求地址
     * @param json        请求参数 - body:json
     * @param headers     请求头参数
     * @param httpContext httpContext 参数
     * @return 请求响应信息
     */
    public static ResponseBody put(String url, String json, Map<String, String> headers, Map<String, String> httpContext) {
        return put(url, json, headers, httpContext, null);
    }

    /**
     * put 请求
     *
     * @param url     请求地址
     * @param json    请求参数 - body:json
     * @param headers 请求头参数
     * @param charset 响应字符集类型
     * @return 请求响应信息
     */
    public static ResponseBody put(String url, String json, Map<String, String> headers, Charset charset) {
        return put(url, json, headers, null, charset);
    }

    /**
     * put 请求
     * 默认字符集 {@link java.nio.charset.StandardCharsets#UTF_8}
     *
     * @param url     请求地址
     * @param json    请求参数 - body:json
     * @param headers 请求头参数
     * @return 请求响应信息
     */
    public static ResponseBody put(String url, String json, Map<String, String> headers) {
        return put(url, json, headers, null, null);
    }

    /**
     * put 请求
     *
     * @param url     请求地址
     * @param json    请求参数 - body:json
     * @param charset 响应字符集类型
     * @return 请求响应信息
     */
    public static ResponseBody put(String url, String json, Charset charset) {
        return put(url, json, null, charset);
    }

    /**
     * put 请求
     * 默认字符集 {@link java.nio.charset.StandardCharsets#UTF_8}
     *
     * @param url  请求地址
     * @param json 请求参数 - body:json
     * @return 请求响应信息
     */
    public static ResponseBody put(String url, String json) {
        return put(url, json, null, null, null);
    }

    /**
     * put 请求
     *
     * @param url     请求地址
     * @param charset 响应字符集类型
     * @return 请求响应信息
     */
    public static ResponseBody put(String url, Charset charset) {
        return put(url, "", charset);
    }

    /**
     * put 请求
     * 默认字符集 {@link java.nio.charset.StandardCharsets#UTF_8}
     *
     * @param url 请求地址
     * @return 请求响应信息
     */
    public static ResponseBody put(String url) {
        return put(url, "");
    }

    /**
     * delete 请求
     *
     * @param url         请求地址
     * @param params      请求参数
     * @param headers     请求头参数
     * @param httpContext httpContext 参数
     * @param charset     响应字符集类型
     * @return 请求响应信息
     */
    public static ResponseBody delete(String url, Map<String, Object> params, Map<String, String> headers, Map<String, String> httpContext, Charset charset) {
        return HTTP_UTIL_INTERFACE.delete(url, params, headers, httpContext, charset);
    }

    /**
     * delete 请求
     * 默认字符集 {@link java.nio.charset.StandardCharsets#UTF_8}
     *
     * @param url         请求地址
     * @param params      请求参数 - body:form-data
     * @param headers     请求头参数
     * @param httpContext httpContext 参数
     * @return 请求响应信息
     */
    public static ResponseBody delete(String url, Map<String, Object> params, Map<String, String> headers, Map<String, String> httpContext) {
        return delete(url, params, headers, httpContext, null);
    }

    /**
     * delete 请求
     *
     * @param url     请求地址
     * @param params  请求参数 - body:form-data
     * @param headers 请求头参数
     * @param charset 响应字符集类型
     * @return 请求响应信息
     */
    public static ResponseBody delete(String url, Map<String, Object> params, Map<String, String> headers, Charset charset) {
        return delete(url, params, headers, null, charset);
    }

    /**
     * delete 请求
     * 默认字符集 {@link java.nio.charset.StandardCharsets#UTF_8}
     *
     * @param url     请求地址
     * @param params  请求参数 - body:form-data
     * @param headers 请求头参数
     * @return 请求响应信息
     */
    public static ResponseBody delete(String url, Map<String, Object> params, Map<String, String> headers) {
        return delete(url, params, headers, null, null);
    }

    /**
     * delete 请求
     *
     * @param url     请求地址
     * @param params  请求参数 - body:form-data
     * @param charset 响应字符集类型
     * @return 请求响应信息
     */
    public static ResponseBody delete(String url, Map<String, Object> params, Charset charset) {
        return delete(url, params, null, charset);
    }

    /**
     * delete 请求
     * 默认字符集 {@link java.nio.charset.StandardCharsets#UTF_8}
     *
     * @param url    请求地址
     * @param params 请求参数 - body:form-data
     * @return 请求响应信息
     */
    public static ResponseBody delete(String url, Map<String, Object> params) {
        return delete(url, params, null, null, null);
    }

    /**
     * delete 请求
     *
     * @param url         请求地址
     * @param json        请求参数 - body:json
     * @param headers     请求头参数
     * @param httpContext httpContext 参数
     * @param charset     响应字符集类型
     * @return 请求响应信息
     */
    public static ResponseBody delete(String url, String json, Map<String, String> headers, Map<String, String> httpContext, Charset charset) {
        return HTTP_UTIL_INTERFACE.delete(url, json, headers, httpContext, charset);
    }

    /**
     * delete 请求
     * 默认字符集 {@link java.nio.charset.StandardCharsets#UTF_8}
     *
     * @param url         请求地址
     * @param json        请求参数 - body:json
     * @param headers     请求头参数
     * @param httpContext httpContext 参数
     * @return 请求响应信息
     */
    public static ResponseBody delete(String url, String json, Map<String, String> headers, Map<String, String> httpContext) {
        return delete(url, json, headers, httpContext, null);
    }

    /**
     * delete 请求
     *
     * @param url     请求地址
     * @param json    请求参数 - body:json
     * @param headers 请求头参数
     * @param charset 响应字符集类型
     * @return 请求响应信息
     */
    public static ResponseBody delete(String url, String json, Map<String, String> headers, Charset charset) {
        return delete(url, json, headers, null, charset);
    }

    /**
     * delete 请求
     * 默认字符集 {@link java.nio.charset.StandardCharsets#UTF_8}
     *
     * @param url     请求地址
     * @param json    请求参数 - body:json
     * @param headers 请求头参数
     * @return 请求响应信息
     */
    public static ResponseBody delete(String url, String json, Map<String, String> headers) {
        return delete(url, json, headers, null, null);
    }

    /**
     * delete 请求
     *
     * @param url     请求地址
     * @param json    请求参数 - body:json
     * @param charset 响应字符集类型
     * @return 请求响应信息
     */
    public static ResponseBody delete(String url, String json, Charset charset) {
        return delete(url, json, null, charset);
    }

    /**
     * delete 请求
     * 默认字符集 {@link java.nio.charset.StandardCharsets#UTF_8}
     *
     * @param url  请求地址
     * @param json 请求参数 - body:json
     * @return 请求响应信息
     */
    public static ResponseBody delete(String url, String json) {
        return delete(url, json, null, null, null);
    }

    /**
     * delete 请求
     *
     * @param url     请求地址
     * @param charset 响应字符集类型
     * @return 请求响应信息
     */
    public static ResponseBody delete(String url, Charset charset) {
        return delete(url, "", charset);
    }

    /**
     * delete 请求
     * 默认字符集 {@link java.nio.charset.StandardCharsets#UTF_8}
     *
     * @param url 请求地址
     * @return 请求响应信息
     */
    public static ResponseBody delete(String url) {
        return delete(url, "");
    }

}
