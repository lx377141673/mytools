package xyz.liangxin.utils.web.ip;


import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Continent;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Subdivision;

/**
 * IP 信息包装实体
 *
 * @author liangxin
 * @version V1.0
 * @date 2021/12/10 15:28
 */
public class IpInfo {

    private static final String LANGUAGE = "zh-CN";

    /**
     * ip 地址
     */
    private String ip;

    /**
     * 大洲 名
     */
    private String continentName;
    /**
     * 大洲 （英文名）
     */
    private String continentNameEn;

    /**
     * 大洲 代码 两个字符的大陆代码
     * 例如 ：
     * AS ( Asia/亚洲 ）；
     * NA ( North America/北美 ）；
     * OC （ Oceania/ 大洋洲）
     */
    private String continentCode;

    /**
     * 国家名
     */
    private String countryName;

    /**
     * 国家名 (英文名）
     */
    private String countryNameEn;

    /**
     * 国家代码
     */
    private String countryCode;

    /**
     * 是否是欧盟
     */
    private boolean isInEuropeanUnion;

    /**
     * 省份名
     */
    private String provinceName;
    /**
     * 省份名 (英文名）
     */
    private String provinceNameEn;

    /**
     * 省份代码
     */
    private String provinceCode;

    /**
     * 城市名
     */
    private String cityName;
    /**
     * 城市名  (英文名）
     */
    private String cityNameEn;

    /**
     * 邮政编码
     */
    private String postalCode;

    /**
     * 经度
     */
    private Double longitude;
    /**
     * 纬度
     */
    private Double latitude;

    /**
     * IP地址
     *
     * @return IP地址
     */
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getContinentName() {
        return continentName;
    }

    public void setContinentName(String continentName) {
        this.continentName = continentName;
    }

    public String getContinentNameEn() {
        return continentNameEn;
    }

    public void setContinentNameEn(String continentNameEn) {
        this.continentNameEn = continentNameEn;
    }

    public String getContinentCode() {
        return continentCode;
    }

    public void setContinentCode(String continentCode) {
        this.continentCode = continentCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryNameEn() {
        return countryNameEn;
    }

    public void setCountryNameEn(String countryNameEn) {
        this.countryNameEn = countryNameEn;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public boolean isInEuropeanUnion() {
        return isInEuropeanUnion;
    }

    public void setInEuropeanUnion(boolean inEuropeanUnion) {
        isInEuropeanUnion = inEuropeanUnion;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceNameEn() {
        return provinceNameEn;
    }

    public void setProvinceNameEn(String provinceNameEn) {
        this.provinceNameEn = provinceNameEn;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityNameEn() {
        return cityNameEn;
    }

    public void setCityNameEn(String cityNameEn) {
        this.cityNameEn = cityNameEn;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * 统一注入 大洲信息  ， 大洲名(中/英文），大洲缩写代码
     *
     * @param continent 大洲信息
     */
    public void setContinent(Continent continent) {
        this.continentName = continent.getNames().get(LANGUAGE);
        this.continentNameEn = continent.getName();
        this.continentCode = continent.getCode();
    }


    /**
     * 统一注入 国家信息
     * 国家名 （中/英） ， 国家名缩写 ， 是否所属欧盟
     *
     * @param country 国家信息
     */
    public void setCountry(Country country) {
        this.countryName = country.getNames().get(LANGUAGE);
        this.countryNameEn = country.getName();
        this.countryCode = country.getIsoCode();
        this.isInEuropeanUnion = country.isInEuropeanUnion();
    }


    /**
     * 统一注入 省份 / 洲省 信息
     * 省份名 （中/英）, 省份名缩写
     *
     * @param province 省份/洲省 信息
     */
    public void setProvince(Subdivision province) {
        this.provinceName = province.getNames().get(LANGUAGE);
        this.provinceNameEn = province.getName();
        this.provinceCode = province.getIsoCode();
    }


    /**
     * 统一注入 城市信息
     * 城市名 （中/英）, 城市名缩写
     *
     * @param city 城市信息
     */
    public void setCity(City city) {
        this.cityName = city.getNames().get(LANGUAGE);
        this.cityNameEn = city.getName();
    }


    public void setLocation(Location location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }


    @Override
    public String toString() {
        return "IpInfo{" +
                "ip='" + ip + '\'' +
                ", continentName='" + continentName + '\'' +
                ", continentNameEn='" + continentNameEn + '\'' +
                ", continentCode='" + continentCode + '\'' +
                ", countryName='" + countryName + '\'' +
                ", countryNameEn='" + countryNameEn + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", isInEuropeanUnion=" + isInEuropeanUnion +
                ", provinceName='" + provinceName + '\'' +
                ", provinceNameEn='" + provinceNameEn + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", cityName='" + cityName + '\'' +
                ", cityNameEn='" + cityNameEn + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}