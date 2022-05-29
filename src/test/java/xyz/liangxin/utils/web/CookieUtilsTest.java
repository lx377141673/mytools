package xyz.liangxin.utils.web;


import org.junit.Test;
import xyz.liangxin.utils.json.JsonUtils;

import javax.servlet.http.Cookie;

/**
 * <p> 描述
 *
 * @author liangxin
 * @version V1.0
 * @since 创建时间: 2022/3/4 16:14
 */
public class CookieUtilsTest {

    @Test
    public void CreateCookie() {
        Cookie cookie = new Cookie("key", "value");
        System.out.println(JsonUtils.toJSONString(cookie));
        System.out.println();
    }

}