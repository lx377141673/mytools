package xyz.liangxin.utils.web.http;

import com.fasterxml.jackson.databind.JsonNode;
import xyz.liangxin.utils.json.JsonUtils;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 响应数据包装类
 *
 * @author liangxin
 * @version V1.0
 * @date 2021/4/30 14:21
 */
public class ResponseBody {
    /**
     * 响应主体
     */
    private String body;
    /**
     * 响应 header
     */
    private Map<String, List<String>> headerMap = Collections.emptyMap();
    /**
     * 响应 cookie
     */
    private Map<String, String> cookieMap = Collections.emptyMap();
    /**
     * 响应服务器响应语言
     */
    private Locale locale;
    /**
     * 响应状态码
     */
    private int statusCode;
    /**
     * 响应是否成功
     */
    private boolean success;

    public ResponseBody(String body, int statusCode) {
        this.body = body;
        this.statusCode = statusCode;
        this.success = statusCode == 200;
    }

    public ResponseBody(String body, Map<String, List<String>> headerMap, Map<String, String> cookieMap, Locale locale, int statusCode) {
        this.body = body;
        this.headerMap = headerMap;
        this.cookieMap = cookieMap;
        this.locale = locale;
        this.statusCode = statusCode;
        this.success = statusCode == 200;
    }

    public ResponseBody(String body, Map<String, List<String>> headerMap, Map<String, String> cookieMap, int statusCode) {
        this.body = body;
        this.headerMap = headerMap;
        this.cookieMap = cookieMap;
        this.statusCode = statusCode;
        this.success = statusCode == 200;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    /**
     * 获取 响应体 信息， 并转换成 {@link JsonNode}Json对象
     *
     * @return 响应信息 Json 对象
     */
    public JsonNode getBodyJson() {
        return JsonUtils.parseJson(body);
    }

    public Map<String, List<String>> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(Map<String, List<String>> headerMap) {
        this.headerMap = headerMap;
    }

    /**
     * 根据 Header 名 获取  header值
     *
     * @param name header 名
     * @return header 值
     */
    public List<String> getHeader(final String name) {
        return headerMap.getOrDefault(name, null);
    }

    public Map<String, String> getCookieMap() {
        return cookieMap;
    }

    public void setCookieMap(Map<String, String> cookieMap) {
        this.cookieMap = cookieMap;
    }

    /**
     * 根据 cookie 名 获取  header值
     *
     * @param name cookie 名
     * @return cookie 值
     */
    public String getCookie(final String name) {
        return cookieMap.getOrDefault(name, null);
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "ResponseBody{" +
                "body='" + body + '\'' +
                ", headerMap=" + headerMap +
                ", cookieMap=" + cookieMap +
                ", locale=" + locale +
                ", statusCode=" + statusCode +
                ", success=" + success +
                '}';
    }
}
