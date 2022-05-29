package xyz.liangxin.utils.web;

import xyz.liangxin.utils.constant.date.DateConstant;
import xyz.liangxin.utils.core.ObjectUtils;
import xyz.liangxin.utils.core.array.ArrayUtil;
import xyz.liangxin.utils.core.text.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Stream;

/**
 * <p> Cookie 工具类
 *
 * @author liangxin
 * @version V1.0
 * @since 创建时间: 2022/1/10 2:04
 */
public class CookieUtils {
    /**
     * 默认 项目 domain
     */
    static final String DEFAULT_DOMAIN = "";

    /**
     * 默认 项目 path
     */
    static final String DEFAULT_PATH = "/";

    /**
     * 默认域下 设置 Cookie值
     *
     * @param response  响应对象
     * @param key       cookie 键
     * @param value     cookie 值
     * @param expirySec 有效时间 （以秒为单位）
     * @param domain    指定域名 (保存的域)
     * @param path      设置路径名(具体接口地址)
     */
    public static void setCookieBySec(HttpServletResponse response, String key, String value, int expirySec, String domain, String path) {
        if (StringUtils.isAllNotEmpty(key, value)) {
            Cookie cookie = new Cookie(key, value);
            cookie.setMaxAge(expirySec);
            cookie.setPath(StringUtils.blankToDefault(path, DEFAULT_PATH));
            cookie.setDomain(StringUtils.blankToDefault(domain, DEFAULT_DOMAIN));
            response.addCookie(cookie);
        }
    }

    /**
     * 默认域下 设置 Cookie值
     *
     * @param response  响应对象
     * @param key       cookie 键
     * @param value     cookie 值
     * @param expirySec 有效时间 （以秒为单位）
     * @param domain    指定域名 (保存的域)
     */
    public static void setCookieBySec(HttpServletResponse response, String key, String value, int expirySec, String domain) {
        setCookieBySec(response, key, value, expirySec, domain, DEFAULT_PATH);
    }

    /**
     * 默认域下 设置 Cookie值
     *
     * @param response  响应对象
     * @param key       cookie 键
     * @param value     cookie 值
     * @param expirySec 有效时间 （以秒为单位）
     */
    public static void setCookieBySec(HttpServletResponse response, String key, String value, int expirySec) {
        setCookieBySec(response, key, value, expirySec, DEFAULT_DOMAIN, DEFAULT_PATH);
    }


    /**
     * 指定域下 设置Cookie值
     *
     * @param response    响应对象
     * @param key         cookie 键
     * @param value       cookie 值
     * @param expiryHours 有效期 (小时)
     * @param domain      指定域名
     * @param path        设置路径名(具体接口地址)
     */
    public static void setCookie(HttpServletResponse response, String key, String value, int expiryHours, String domain, String path) {
        setCookieBySec(response, key, value, expiryHours * DateConstant.HOUR_TO_SECOND, domain, path);
    }

    /**
     * 默认域下 设置 Cookie值
     *
     * @param response    响应对象
     * @param key         cookie 键
     * @param value       cookie 值
     * @param expiryHours 有效期 (小时)
     */
    public static void setCookie(HttpServletResponse response, String key, String value, int expiryHours, String domain) {
        setCookie(response, key, value, expiryHours, domain, DEFAULT_PATH);
    }

    /**
     * 默认域下 设置 Cookie值
     *
     * @param response    响应对象
     * @param key         cookie 键
     * @param value       cookie 值
     * @param expiryHours 有效期 (小时)
     */
    public static void setCookie(HttpServletResponse response, String key, String value, int expiryHours) {
        setCookie(response, key, value, expiryHours, DEFAULT_DOMAIN, DEFAULT_PATH);
    }


    /**
     * 获取默认域下的 Cookie值
     *
     * @param request 请求对象
     * @param key     cookie 键
     * @return Cookie值
     */
    public static String getCookie(HttpServletRequest request, String key) {
        return ObjectUtils.getNext(request.getCookies(), "", cookies -> Stream.of(cookies)
                .filter(cookie -> key.equalsIgnoreCase(cookie.getName()) && StringUtils.isNotEmpty(cookie.getValue()))
                .findFirst().map(Cookie::getValue)
                .orElse(StringUtils.STR_EMPTY), ArrayUtil::isNotEmpty);
    }


    /**
     * 删除Cookie
     *
     * @param response 响应对象
     * @param name     cookie 键名
     * @param domain   cookie 所在域名
     * @param path     cookie 所存控制器地址
     */
    public static void deleteCookie(HttpServletResponse response, String name, String domain, String path) {
        setCookie(response, name, "delete", -1, domain, path);
    }

    /**
     * 删除Cookie
     *
     * @param response 响应对象
     * @param name     cookie 键名
     * @param path     cookie 所存控制器地址
     */
    public static void deleteCookie(HttpServletResponse response, String name, String path) {
        deleteCookie(response, name, DEFAULT_DOMAIN, path);
    }

    /**
     * 删除Cookie
     *
     * @param response 响应对象
     * @param name     cookie 键名
     */
    public static void deleteCookie(HttpServletResponse response, String name) {
        deleteCookie(response, name, DEFAULT_DOMAIN, DEFAULT_PATH);
    }
}
