package xyz.liangxin.utils.web.ip;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.liangxin.utils.core.ObjectUtils;
import xyz.liangxin.utils.web.http.HttpUtils;

/**
 * GeoLite2IP ip地址信息查询
 *
 * @author liangxin
 */
public class GeoLite2IpUtilsTest {
    Logger logger = LoggerFactory.getLogger(GeoLite2IpUtilsTest.class);

    @Test
    public void getIpInfo() {
        String[] ips = {"106.39.151.55", "27.187.212.57", "20.205.243.166", "59.109.123.225"};
        for (String ip : ips) {
            IpInfo ipInfo = GeoLite2IpUtils.getIpInfo(ip);
            logger.info(ipInfo.toString());
        }

    }

    @Test
    public void webIpInfo() {

        String[] ips = {"106.39.151.55", "27.187.212.57", "20.205.243.166", "59.109.123.225"};
//        for (String ip : ips) {
//            System.out.println(WebIpUtils.ipInfoByIpshudi(ip));
//        }
        int i = 0;

        for (; i < 1000; i++) {
            String ip = "27.187.212." + i;
            System.out.println(WebIpUtils.ipInfoByIpShuDi(ip));
        }
        System.out.println("结果数:" + i);

//        Stream.

    }

    @Test
    public void webIpInfo2() {
        String[] ips = {"106.39.151.55", "27.187.212.57", "20.205.243.166", "59.109.123.225"};
        int i = 0;
        Document document = null;
        try {
            for (; i < 100000; i++) {
                document = Jsoup.parse(HttpUtils.get("https://www.ipshudi.com/" + ips[i % 3] + ".htm").getBody());
                Element table = document.getElementsByTag("table").get(0);
                Elements span = table.getElementsByTag("span");
                System.out.println(span.get(0).text());
                System.out.println(span.get(1).text());
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (ObjectUtils.nonNull(document)) {

                System.out.println(document.text());
            }
        }
        System.out.println(i);

    }
}