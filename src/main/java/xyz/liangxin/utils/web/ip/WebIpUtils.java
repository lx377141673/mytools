package xyz.liangxin.utils.web.ip;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.liangxin.utils.web.http.HttpUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * <p> 通过 第三方网站查询 ip 信息, 并爬取其内容
 *
 * @author liangxin
 * @version V1.0
 * @since 创建时间: 2022/6/26 0:34
 */
public class WebIpUtils {

    private static final Logger logger = LoggerFactory.getLogger(WebIpUtils.class);

    /**
     * 私有构造函数
     */
    private WebIpUtils() {
    }

    /**
     * 注入结果规范
     *
     * @param country  国家
     * @param province 省份
     * @param city     市
     * @param county   区县
     * @param operator 运营商
     * @return 返回 {"country":"国家","province":"省份","city":"市","county":"区县",operator:"运营商"} 格式的 Map;
     */
    private static Map<String, String> setResult(String country, String province, String city, String county, String operator) {
        Map<String, String> result = new HashMap<>(4);
        result.put("country", country);
        result.put("province", province);
        result.put("city", city);
        result.put("county", county);
        result.put("operator", operator);
        return result;
    }

    /**
     * 爬取 <a href="https://www.ipshudi.com/[ip].htm">https://www.ipshudi.com/[ip].htm</a> 的出来结果
     *
     * @param ip ip地址
     * @return ip物理地址信息
     * <ul>
     *     <li>{"country":"国家","province":"省份","city":"市","county":"区县",operator:"运营商"} 格式</li>
     *     <li>{county=, country=中国, province=北京, city=昌平, operator=电信}</li>
     *     <li>{county=竞秀区, country=中国, province=河北, city=保定, operator=电信}</li>
     *     <li>{county=, country=新加坡, province=, city=, operator=微软云}</li>
     * </ul>
     */
    public static Map<String, String> ipInfoByIpShuDi(String ip) {
        Map<String, String> result = null;
        Document document;
        try {
            document = Jsoup.parse(HttpUtils.get("https://www.ipshudi.com/" + ip + ".htm").getBody());
            Element table = document.getElementsByTag("table").get(0);
            Elements span = table.getElementsByTag("span");
            String[] s = span.get(0).text().split(" ");
            result = setResult(s[0], s.length >= 2 ? s[1] : "", s.length >= 3 ? s[2] : "", s.length >= 4 ? s[3] : "", span.get(1).text());
        } catch (Exception e) {
            logger.error("IP: " + ip, e);
        }
        return result;

    }


}
