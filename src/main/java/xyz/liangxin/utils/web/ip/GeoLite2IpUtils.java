package xyz.liangxin.utils.web.ip;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;


/**
 * IP 定位工具类
 *
 * @author liangxin
 * @version V1.0
 * @date 2021/4/27 21:27
 * @since
 * <p>
 *  IP 定位工具类 ，基于 geolite2-city 离线数据库 version-2021/12/07
 *  maven 依赖
 *  <dependency>
 *  <groupId>com.maxmind.geoip2</groupId>
 *  <artifactId>geoip2</artifactId>
 *  <version>2.16.1</version>
 *  </dependency>
 * </p>
 */
public class GeoLite2IpUtils {

    private static final Logger logger = LoggerFactory.getLogger(GeoLite2IpUtils.class);
    /**
     * geolite2-city  离线数据库文件地址,
     * 使用时需要修改为本地地址
     */
    private static final String GEOIP2_DB = "GeoLite2-City.mmdb";

    /**
     * 全局静态变量，保证类加载时加载一次，避免反复读取 GeoIP2 离线库
     */
    private static DatabaseReader reader;

    // 静态代码块, 初始化时执行一次
    static {
        InputStream database;
        try {
            // 读取 GeoIP2 离线库，jar 包中的资源文件无法以 File 方式读取，需要用 InputStream 流式读取
            database = GeoLite2IpUtils.class.getClassLoader().getResourceAsStream(GEOIP2_DB);
            setReader(database);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private GeoLite2IpUtils() {
    }

    /**
     * 用于在线更新 geoLite2 字典
     *
     * @param database 字典数据流
     */
    public static void setReader(InputStream database) throws IOException {
        reader = new DatabaseReader.Builder(database).build();
    }

    /**
     * IP 解析
     *
     * @param ip IP 地址
     * @return IpInfo
     */
    public static IpInfo getIpInfo(String ip) {
        IpInfo info = new IpInfo();
        try {
            // 根据 IP 地址查询结果
            InetAddress ipAddress = InetAddress.getByName(ip);
            CityResponse response = reader.city(ipAddress);
            info.setIp(ip);
            // 大洲
            info.setContinent(response.getContinent());
            // 国家
            info.setCountry(response.getCountry());
            // 省份
            info.setProvince(response.getMostSpecificSubdivision());
            // 市
            info.setCity(response.getCity());
            // 邮编
            info.setPostalCode(response.getPostal().getCode());
            // 经纬地址,定位
            info.setLocation(response.getLocation());

        } catch (IOException | GeoIp2Exception e) {
            e.printStackTrace();
        }

        return info;
    }


    public static void test() {
        IpInfo ipInfo = getIpInfo("219.142.145.158");
        logger.info(ipInfo.toString());
    }
}
