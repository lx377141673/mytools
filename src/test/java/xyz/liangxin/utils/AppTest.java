package xyz.liangxin.utils;

import org.junit.Test;
import xyz.liangxin.utils.web.http.HttpUtils;
import xyz.liangxin.utils.web.ip.GeoLite2IpUtils;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public void testGeoLite2() {
        System.out.println(GeoLite2IpUtils.getIpInfo("106.39.151.55").getCityName());
    }

    @Test
    public void testHttpUtil() {
        System.out.println(HttpUtils.get("http://api2.jirengu.com/fm/v2/getChannels.php?callback=getChannel"));
    }
}
