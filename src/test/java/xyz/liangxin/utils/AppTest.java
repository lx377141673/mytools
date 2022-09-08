package xyz.liangxin.utils;

import org.junit.Test;
import xyz.liangxin.utils.web.http.HttpUtils;

/**
 * Unit test for simple App.
 */
public class AppTest {

    @Test
    public void testHttpUtil() {
        System.out.println(HttpUtils.get("http://api2.jirengu.com/fm/v2/getChannels.php?callback=getChannel"));
    }

}
