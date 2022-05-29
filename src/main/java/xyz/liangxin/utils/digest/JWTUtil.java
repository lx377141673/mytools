package xyz.liangxin.utils.digest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import xyz.liangxin.utils.constant.date.CalendarUnitEnum;
import xyz.liangxin.utils.core.date.DateUtils;

import java.util.Date;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * <p> Jwt 工具类
 *
 * @author liangxin
 * @version V1.0
 * @since 创建时间: 2022/1/20 21:33
 */
public class JWTUtil {
    /**
     * 签名密钥
     */
    protected static final String SECRET = "!DAR$";


    /**
     * 生成 JWT Token
     *
     * @param payloadConsumer 装载 负载信息 消费函数
     * @param expiresAt       过期截止时间
     * @param algorithm       签名算法 (包含签名密钥, 解密时需要同步使用)
     * @return 加密 jwt 令牌 字符串
     */
    public static String getToken(Consumer<JWTCreator.Builder> payloadConsumer, Date expiresAt, Algorithm algorithm) {
        JWTCreator.Builder builder = JWT.create();
        // 构建payload
        payloadConsumer.accept(builder);
        // 指定过期时间和签名算法
        return builder.withExpiresAt(expiresAt).sign(algorithm);
    }

    /**
     * 生成 JWT Token
     *
     * @param payloadConsumer 装载 负载信息 消费函数
     * @param validDay        有效天数
     * @param algorithm       签名算法 (包含签名密钥, 解密时需要同步使用)
     * @return 加密 jwt 令牌 字符串
     */
    public static String getToken(Consumer<JWTCreator.Builder> payloadConsumer, int validDay, Algorithm algorithm) {
        return getToken(payloadConsumer,
                DateUtils.getNextTime(new Date(), validDay, CalendarUnitEnum.DAY),
                algorithm);
    }


    /**
     * <p>
     * 生成 JWT Token;<br/>
     * 指定有效天数 validDay;<br/>
     * 默认使用 {@link Algorithm#HMAC256(String)} 算法;<br/>
     * 使用默认签名 {@link JWTUtil#SECRET}<br/>
     * </p>
     *
     * @param payloadConsumer 装载 负载信息 消费函数
     * @param validDay        有效天数 [过期天数]
     * @return 加密 jwt 令牌 字符串
     */
    public static String getToken(Consumer<JWTCreator.Builder> payloadConsumer, int validDay) {
        return getToken(payloadConsumer, validDay, Algorithm.HMAC256(SECRET));
    }

    /**
     * 生成 JWT Token
     *
     * @param payload   负载信息
     * @param expiresAt 过期截止时间
     * @param algorithm 签名算法 (包含签名密钥, 解密时需要同步使用)
     * @return 加密 jwt 令牌 字符串
     */
    public static String getToken(Map<String, String> payload, Date expiresAt, Algorithm algorithm) {
        return getToken(builder -> payload.forEach(builder::withClaim), expiresAt, algorithm);
    }

    /**
     * <p>
     * 生成 JWT Token;<br/>
     * 指定有效天数 validDay;<br/>
     * 默认使用 {@link Algorithm#HMAC256(String)} 算法;<br/>
     * 使用默认签名 {@link JWTUtil#SECRET}<br/>
     * </p>
     *
     * @param payload  负载信息
     * @param validDay 有效天数 [过期天数]
     * @return 加密 jwt 令牌 字符串
     */
    public static String getToken(Map<String, String> payload, int validDay) {
        return getToken(payload,
                DateUtils.getNextTime(new Date(), validDay, CalendarUnitEnum.DAY),
                Algorithm.HMAC256(SECRET));
    }

    /**
     * 解析token
     *
     * @param token     token字符串
     * @param algorithm 加密算法令牌
     * @return jwt 解密信息包装类
     */
    public static DecodedJWT decode(String token, Algorithm algorithm) {
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        return jwtVerifier.verify(token);
    }

    /**
     * <p>
     * 解析token<br/>
     * 默认使用 {@link Algorithm#HMAC256(String)} 算法;<br/>
     * 使用默认签名 {@link JWTUtil#SECRET}<br/>
     * </p>
     *
     * @param token token字符串
     * @return jwt 解密信息包装类
     */
    public static DecodedJWT decode(String token) {
        return decode(token, Algorithm.HMAC256(SECRET));
    }


    /**
     * 解析token
     *
     * @param token       token字符串
     * @param algorithm   加密算法令牌
     * @param entityApply 结果处理包装函数
     * @param <T>         返回值类型
     * @return 处理并返回 解密结果值
     */
    public static <T> T decode(String token, Algorithm algorithm, Function<DecodedJWT, T> entityApply) {
        return entityApply.apply(decode(token, algorithm));
    }


    /**
     * <p>
     * 解析token<br/>
     * 默认使用 {@link Algorithm#HMAC256(String)} 算法;<br/>
     * 使用默认签名 {@link JWTUtil#SECRET}<br/>
     * </p>
     *
     * @param token       token字符串
     * @param entityApply 结果处理包装函数
     * @param <T>         返回值类型
     * @return 处理并返回 解密结果值
     */
    public static <T> T decode(String token, Function<DecodedJWT, T> entityApply) {
        return entityApply.apply(decode(token));
    }

}

