package xyz.liangxin.utils.web;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import xyz.liangxin.utils.core.text.CharSequenceUtil;
import xyz.liangxin.utils.core.text.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * WebUtils
 *
 * @author liangxin
 * @version V1.0
 *
 * @date 2021/4/11 13:05

 */
public class WebUtils extends CookieUtils {


    private static final String UNKNOWN =StringUtils.STR_UNKNOWN;


    private WebUtils() {
    }



    /**
     * 获取请求IP地址
     *
     * @param request 请求对象
     * @return 请求 IP 地址
     */
    public static String getIpAddress(HttpServletRequest request) {
        Predicate<String> predicate = ipAddress -> StringUtils.isEmpty(ipAddress) || UNKNOWN.equals(ipAddress);

        String ipAddress = request.getHeader("x-forwarded-for");
        if (predicate.test(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (predicate.test(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (predicate.test(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            String locationIp = "127.0.0.1";
            try {
                ipAddress = locationIp.equals(ipAddress) ? InetAddress.getLocalHost().getHostAddress() : ipAddress;
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }

        int ipLength = 15;
        String ipAddressSplit = StringUtils.STR_COMMA;
        if (CharSequenceUtil.isNotEmpty(ipAddress) && ipAddress.length() > ipLength && ipAddress.contains(ipAddressSplit)) {
            String temStr = ipAddress.split(ipAddressSplit)[0];
            ipAddress = CharSequenceUtil.isEmpty(temStr) ? ipAddress : temStr;
        }
        return ipAddress;
    }


    /**
     * 获取请求URL
     *
     * @param request 请求对象
     * @return 请求 URL
     */
    public static String getServletRequestUrlParams(HttpServletRequest request) {
        StringBuffer sbUrlParams = request.getRequestURL();
        Enumeration<String> parNames = request.getParameterNames();
        if (parNames.hasMoreElements()) {
            sbUrlParams.append(StringUtils.STR_QUESTION);
            do {
                String parName = parNames.nextElement();
                try {
                    sbUrlParams.append(parName).append(StringUtils.STR_EQUAL)
                            .append(URLEncoder.encode(request.getParameter(parName), StandardCharsets.UTF_8.name()))
                            .append(StringUtils.STR_AMP);
                } catch (UnsupportedEncodingException e) {
                    return "";
                }
            } while (parNames.hasMoreElements());
        }

        return sbUrlParams.toString();
    }


    /**
     * 判断如果是ajax请求
     *
     * @param request 请求对象
     * @return 是 ajax 请求,返回true
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String her = request.getHeader("x-requested-with");
        return StringUtils.isNotEmpty(her);
    }

    /**
     * 判断如果不是ajax请求
     *
     * @param request 请求对象
     * @return 不是 ajax 请求,返回true
     */
    public static boolean isNotAjaxRequest(HttpServletRequest request) {
        return !isAjaxRequest(request);
    }


    /**
     * 判断referer
     * 防外链验证，
     *
     * @param referer http referer
     * @param trueUrl 正确的 地址
     * @return 是否符合判断
     */
    public static boolean compareReferer(String referer, String trueUrl) {
        String[] baseUrl = referer.split(StringUtils.STR_SLASH);
        String refererUrl = baseUrl[0] + StringUtils.STR_DOUBLE_SLASH + baseUrl[2];
        return refererUrl.equals(trueUrl);
    }

    /**
     * 各种手机的 请求标识信息
     */
    protected static final String[] MOBILE_AGENTS = {"iphone", "android", "ipad", "phone", "mobile", "wap", "netfront", "java", "opera mobi",
            "opera mini", "ucweb", "windows ce", "symbian", "series", "webos", "sony", "blackberry", "dopod",
            "nokia", "samsung", "palmsource", "xda", "pieplus", "meizu", "midp", "cldc", "motorola", "foma",
            "docomo", "up.browser", "up.link", "blazer", "helio", "hosin", "huawei", "novarra", "coolpad", "webos",
            "techfaith", "palmsource", "alcatel", "amoi", "ktouch", "nexian", "ericsson", "philips", "sagem",
            "wellcom", "bunjalloo", "maui", "smartphone", "iemobile", "spice", "bird", "zte-", "longcos",
            "pantech", "gionee", "portalmmm", "jig browser", "hiptop", "benq", "haier", "^lct", "320x320",
            "240x320", "176x220", "w3c ", "acs-", "alav", "alca", "amoi", "audi", "avan", "benq", "bird", "blac",
            "blaz", "brew", "cell", "cldc", "cmd-", "dang", "doco", "eric", "hipt", "inno", "ipaq", "java", "jigs",
            "kddi", "keji", "leno", "lg-c", "lg-d", "lg-g", "lge-", "maui", "maxo", "midp", "mits", "mmef", "mobi",
            "mot-", "moto", "mwbp", "nec-", "newt", "noki", "oper", "palm", "pana", "pant", "phil", "play", "port",
            "prox", "qwap", "sage", "sams", "sany", "sch-", "sec-", "send", "seri", "sgh-", "shar", "sie-", "siem",
            "smal", "smar", "sony", "sph-", "symb", "t-mo", "teli", "tim-", "tosh", "tsm-", "upg1", "upsi", "vk-v",
            "voda", "wap-", "wapa", "wapi", "wapp", "wapr", "webc", "winw", "winw", "xda", "xda-",
            "Googlebot-Mobile"};

    /**
     * 判断是否为手机浏览器 [ 移动端]  如果是手机端返回true
     *
     * @param request 请求对象
     * @return 是否是移动端
     */
    public static boolean isMobile(HttpServletRequest request) {
        boolean isMobile;
        // header 用户标识头 键名
        String headerName = "User-Agent";
        isMobile = Optional.ofNullable(request.getHeader(headerName))
                .map(String::toLowerCase)
                // 过滤 windows 系统,和 mac 系统
                .filter(agent -> !(agent.contains("windows nt") || agent.contains("macintosh")))
                .map(agent -> Stream.of(MOBILE_AGENTS).anyMatch(agent::contains))
                .orElse(Boolean.FALSE);
        return isMobile;
    }

    /**
     * 判断是否为微信客户端
     *
     * @param request 请求对象
     * @return 是否是微信客户端
     */
    public static boolean isWeChat(HttpServletRequest request) {
        // 微信客户端标识
        String weChatIdentify = "MicroMessenger".toLowerCase();
        //判断 是否是微信浏览器
        String userAgent = request.getHeader("user-agent").toLowerCase();
        return userAgent.contains(weChatIdentify);
    }

    /**
     * 获取请求设备信息
     *
     * @param request 请求对象
     * @return 请求设备信息
     */
    public static String getDeviceInfo(HttpServletRequest request) {
        //request为请求的request
        String userAgent = request.getHeader("User-Agent");
        //解析agent字符串
        UserAgent ua = UserAgent.parseUserAgentString(userAgent);
        //获取浏览器对象
        Browser browser = ua.getBrowser();
        //获取操作系统对象
        OperatingSystem os = ua.getOperatingSystem();
        return os.getDeviceType() + StringUtils.STR_DASHED + os.getName() + StringUtils.STR_DASHED + browser.getName();
    }


}
