package xyz.liangxin.utils.web;

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
     * 当前 项目 domain
     */
    static final String MY_DOMAIN = "";


    /**
     * 默认域下 设置 Cookie值
     *
     * @param response 响应对象
     * @param key      cookie 键
     * @param value    cookie 值
     * @param days     保存天数
     */
    public static void setCookie(HttpServletResponse response, String key, String value, int days) {
        setCookie(response, key, value, days, MY_DOMAIN);
    }

    /**
     * 指定域下 设置Cookie值
     *
     * @param response 响应对象
     * @param key      cookie 键
     * @param value    cookie 值
     * @param days     保存天数
     * @param domain   指定域名
     */
    public static void setCookie(HttpServletResponse response, String key, String value, int days, String domain) {
        if (StringUtils.isAllNotEmpty(key, value)) {
            Cookie cookie = new Cookie(key, value);
            cookie.setMaxAge(days * 24*60*60);
            cookie.setPath("/");
            if (StringUtils.isNotEmpty(domain)) {
                cookie.setDomain(domain);
            }
            response.addCookie(cookie);
        }
    }

    /**
     * 获取默认域下的 Cookie值
     *
     * @param request 请求对象
     * @param key     cookie 键
     * @return Cookie值
     */
    public static String getCookie(HttpServletRequest request, String key) {
        return Stream.of(request.getCookies())
                .filter(cookie -> key.equalsIgnoreCase(cookie.getName()) && StringUtils.isNotEmpty(cookie.getValue()))
                .findFirst().map(Cookie::getValue)
                .orElse("");
    }

    /**
     * 删除默认域名下的 Cookie
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param name     cookie 键名
     */
    public static void deleteCookie(HttpServletRequest request,
                                    HttpServletResponse response, String name) {
        deleteCookieDomain(request, response, name, MY_DOMAIN);
    }

    /**
     * 删除某域下的Cookie
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param name     cookie 键名
     * @param domain   指定域名
     */
    public static void deleteCookieDomain(HttpServletRequest request,
                                          HttpServletResponse response, String name, String domain) {
        Stream.of(request.getCookies())
                .filter(cookie -> !name.equalsIgnoreCase(cookie.getName()))
                .findFirst()
                .ifPresent(cookie -> {
                    Cookie ck = new Cookie(cookie.getName(), null);
                    ck.setPath("/");
                    if (StringUtils.isNotEmpty(domain)) {
                        ck.setDomain(domain);
                    }
                    ck.setMaxAge(0);
                    response.addCookie(ck);
                });

    }
}
