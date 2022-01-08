package xyz.liangxin.utils.web.http;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.liangxin.utils.core.ObjectUtils;
import xyz.liangxin.utils.core.text.StringUtils;
import xyz.liangxin.utils.json.JsonUtils;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * HttpClient 工具类
 *
 * @author liangxin
 * @version V1.0
 * @date 2021/4/30 14:56
 */
public class HttpClientUtils implements HttpUtilInterface {


    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);
    /**
     * 连接池 最大连接数
     */
    private static final int MAX_TOTAL = 100;
    /**
     * 连接池 最大路由数
     */
    private static final int MAX_PER_ROUTE = 100;
    /**
     * 连接池， 连接空闲超时验证【毫秒】，
     * 超过空闲时间， 使用前需要先验证，
     * 如果为负数， 则表示永不验证
     */
    private static final int TIMEOUT_VALIDATE = 50000;
    /**
     * 连接池， 连接超时 [毫秒]
     */
    private static final int CONNECT_TIMEOUT = 50000;
    /**
     * 连接池 ， 读取超时 [毫秒]
     */
    private static final int SOCKET_TIMEOUT = 50000;
    /**
     * 连接池 ， 获取连接超时 [毫秒]
     */
    private static final int CONNECTION_REQUEST_TIMEOUT = 50000;
    /**
     * Session id
     */
    private static final String SESSION_ID = UUID.randomUUID().toString();
    /**
     * 是否自动转发
     */
    private static final boolean AUTOMATIC_REDIRECT = false;
    private static final String APPLICATION_WWW = "application/x-www-form-urlencoded";
    private static final String APPLICATION_UTF8 = "application/utf-8";
    private static final String HTTP = "http";
    private static final String HTTPS = "https";

    /**
     * http 连接池
     */
    private static PoolingHttpClientConnectionManager connMgr;
    /**
     * http 请求配置
     */
    private static RequestConfig requestConfig;

    static {
        init();
    }

    /**
     * 操作 cookie
     */
    private CookieStore cookieStore;

    /**
     * 初始化连接池
     */
    private static void init() {
        //设置http和https协议对应的处理socket链接工厂的对象
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register(HTTP, PlainConnectionSocketFactory.INSTANCE)
                .register(HTTPS, createSSLConnSocketFactory())
                .build();
        // 设置连接池
        connMgr = new PoolingHttpClientConnectionManager(registry);
        // 设置连接池大小
        connMgr.setMaxTotal(MAX_TOTAL);
        // 每条路由的最大值
        connMgr.setDefaultMaxPerRoute(MAX_PER_ROUTE);
        //  设置 连接空闲超时验证 【毫秒】，
        //  超过空闲时间， 使用前需要先验证，
        //  如果为负数， 则表示永不验证
        connMgr.setValidateAfterInactivity(TIMEOUT_VALIDATE);

        // 设置请求配置
        RequestConfig.Builder configBuilder = RequestConfig.custom();
        // 设置连接超时
        configBuilder.setConnectTimeout(CONNECT_TIMEOUT);
        // 设置读取超时
        configBuilder.setSocketTimeout(SOCKET_TIMEOUT);
        // 设置从连接池获取连接实例的超时
        configBuilder.setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT);
        requestConfig = configBuilder.build();
    }

    /**
     * 创建SSL安全连接
     *
     * @return SSL安全连接
     */
    private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
        //忽略校验HTTPS服务器证书, 将hostname校验和CA证书校验同时关闭
        SSLConnectionSocketFactory sslsf = null;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (chain, authType) -> true).build();
            sslsf = new SSLConnectionSocketFactory(sslContext, (arg0, arg1) -> Boolean.TRUE);
        } catch (GeneralSecurityException e) {
            logger.error("HttpClientUtils.createSSLConnSocketFactory ==> error : {}", e.getMessage(), e);
        }
        return sslsf;
    }

    /**
     * 创建SSL安全连接 2
     *
     * @return SSL安全连接
     */
    private static SSLConnectionSocketFactory createSSLConnSocketFactoryTwo() {
        SSLConnectionSocketFactory sslConnectionSocketFactory = null;
        try {
            SSLContext sslContext = SSLContexts.custom()
                    .loadTrustMaterial(TrustSelfSignedStrategy.INSTANCE)
                    .build();
            sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            logger.error("HttpClientUtils.createSSLConnSocketFactory2 ==> error : {}", e.getMessage(), e);
        }
        return sslConnectionSocketFactory;
    }

    /**
     * 根据 请求地址 前缀， 从 连接池中 获取 HttpClient [ http 客户端]
     *
     * @param apiUrl 请求地址
     * @return Http客户端
     */
    public CloseableHttpClient httpClientBuilder(String apiUrl) {
        cookieStore = ObjectUtils.getOrDefault(cookieStore, new BasicCookieStore());
        HttpClientBuilder httpClientBuilder = HttpClients.custom()
                .setConnectionManager(connMgr)
                .setDefaultRequestConfig(requestConfig)
                // 设置自动重定向配置
                .disableRedirectHandling()
                .setDefaultCookieStore(cookieStore);
        if (apiUrl.trim().startsWith(HTTPS)) {
            httpClientBuilder.setSSLSocketFactory(createSSLConnSocketFactory());
        }
        return httpClientBuilder.build();
    }

    /**
     * 从 连接池中 获取 HttpClient [ http 客户端]
     *
     * @return HttpClient [ http 客户端]
     */
    public CloseableHttpClient httpClientBuilder() {
        return httpClientBuilder(SESSION_ID, AUTOMATIC_REDIRECT);
    }

    /**
     * 从 连接池中 获取 HttpClient [ http 客户端]
     *
     * @param sessionId         会话ID
     * @param automaticRedirect 自动重定向
     * @return HttpClient [ http 客户端]
     */
    public CloseableHttpClient httpClientBuilder(String sessionId, boolean automaticRedirect) {
        //创建自定义httpclient对象
        HttpClientBuilder builder = HttpClients.custom();
        //设置默认请求配置
        builder.setDefaultRequestConfig(requestConfig);
        //设置连接管理器
        builder.setConnectionManager(connMgr);
        //设置自动重定向配置
        if (!automaticRedirect) {
            builder.disableRedirectHandling();
        }
        cookieStore = ObjectUtils.getOrDefault(cookieStore, new BasicCookieStore());
        builder.setDefaultCookieStore(cookieStore);
        if (StringUtils.isNotBlank(sessionId)) {
            Cookie session = new BasicClientCookie("JSESSIONID", sessionId);
            cookieStore.addCookie(session);
        }
        return builder.build();
    }

    /**
     * 注入 路径 参数 ?参数1=值1 & 参数2=值2 ...
     *
     * @param request 请求对象
     * @param params  参数字典
     */
    public void setPathParams(HttpRequestBase request, Map<String, Object> params) {
        Objects.requireNonNull(request, "请求对象不能为 null");
        if (ObjectUtils.nonEmpty(params)) {
            URI uri = request.getURI();
            StringBuilder param = new StringBuilder(uri.toString());
            int i = StringUtils.isEmpty(uri.getQuery()) ? 0 : 1;
            for (Map.Entry<String, Object> paramEntry : params.entrySet()) {
                param.append(i == 0 ? "?" : "&");
                param.append(paramEntry.getKey()).append("=").append(paramEntry.getValue());
                i++;
            }
            request.setURI(URI.create(param.toString()));
        }
    }

    /**
     * 注入 路径 参数 ?参数1=值1 & 参数2=值2 ...
     *
     * @param request 请求对象
     * @param json    参数 JSON 字符串
     */
    public void setPathParams(HttpRequestBase request, String json) {
        Map<String, Object> map = StringUtils.isEmpty(json) ? Collections.emptyMap() : JsonUtils.parseMap(json);
        setPathParams(request, map);
    }

    /**
     * 注入 请求 参数
     *
     * @param request 请求对象
     * @param entity  请求实体 , 用于包装 请求数据
     */
    public void setBody(HttpEntityEnclosingRequestBase request, HttpEntity entity) {
        Objects.requireNonNull(request, "注入参数时, 请求对象不能为 null");
        if (ObjectUtils.nonNull(entity)) {
            request.setEntity(entity);
        }
    }


    /**
     * 注入 form 表单参数
     *
     * @param request 请求对象
     * @param params  参数字典
     * @param charset 请求参数字符集 默认为 {@link  StandardCharsets#UTF_8}
     */
    public void setBodyByForm(HttpEntityEnclosingRequestBase request, Map<String, Object> params, Charset charset) {
        if (ObjectUtils.nonEmpty(params)) {
            charset = ObjectUtils.getOrDefault(charset, StandardCharsets.UTF_8);
            List<BasicNameValuePair> paramsList = params.entrySet()
                    .stream()
                    .map(entry -> new BasicNameValuePair(entry.getKey(), ObjectUtils.getOrDefault(entry.getValue(), "").toString()))
                    .collect(Collectors.toList());
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(paramsList, charset);
            setBody(request, formEntity);
        }
    }

    /**
     * 注入 form 表单参数
     * <p>
     * 默认参数字符集 {@link  StandardCharsets#UTF_8}
     *
     * @param request 请求对象
     * @param params  参数字典
     */
    public void setBodyByForm(HttpEntityEnclosingRequestBase request, Map<String, Object> params) {
        setBodyByForm(request, params, null);
    }

    /**
     * 注入 form 表单参数
     * <p>
     * 默认参数字符集 {@link  StandardCharsets#UTF_8}
     *
     * @param request 请求对象
     * @param json    参数 json 字符串
     */
    public void setBodyByForm(HttpEntityEnclosingRequestBase request, String json) {
        Map<String, Object> map = StringUtils.isEmpty(json) ? Collections.emptyMap() : JsonUtils.parseMap(json);
        setBodyByForm(request, map);
    }

    /**
     * 注入 body 参数
     *
     * @param request     请求对象
     * @param body        参数字符串 格式 根据 参数类型决定
     * @param contentType 请求参数类型 默认 {@link  ContentType#APPLICATION_JSON}
     */
    public void setBodyByContentType(HttpEntityEnclosingRequestBase request, String body, ContentType contentType) {
        contentType = ObjectUtils.getOrDefault(contentType, ContentType.APPLICATION_JSON);
        if (StringUtils.isNotEmpty(body)) {
            // 解决中文乱码问题
            StringEntity stringEntity = new StringEntity(body, contentType);
            setBody(request, stringEntity);
        }
    }


    /**
     * 注入 body 参数
     * 格式为json @link  ContentType#APPLICATION_JSON}
     *
     * @param request 请求对象
     * @param json    参数字符串
     */
    public void setBodyByJson(HttpEntityEnclosingRequestBase request, String json) {
        setBodyByContentType(request, json, ContentType.APPLICATION_JSON);
    }


    /**
     * 注入 请求 参数, 上传文件
     *
     * @param request  请求对象
     * @param params   其他参数
     * @param file     上传文件流
     * @param fileName 原文件名
     */
    public void setBodyByFile(HttpEntityEnclosingRequestBase request, Map<String, String> params, InputStream file, String fileName) {
        Objects.requireNonNull(file);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.setCharset(StandardCharsets.UTF_8).addBinaryBody(ObjectUtils.getOrDefault(fileName, "multipartFile"), file, ContentType.MULTIPART_FORM_DATA, fileName);
        // 处理其他参数
        if (ObjectUtils.isEmpty(params)) {
            params.forEach(builder::addTextBody);
        }
        setBody(request, builder.build());

    }

//    /**
//     * 注入 请求 参数, 上传文件
//     *
//     * @param request       请求对象
//     * @param params        其他参数
//     * @param multipartFile 上传文件
//     * @throws IOException 上传文件操作异常
//     */
//    public void setBodyByFile(HttpEntityEnclosingRequestBase request, Map<String, String> params, MultipartFile multipartFile) throws IOException {
//        Objects.requireNonNull(multipartFile);
//        setBodyByFile(request, params, multipartFile.getInputStream(), multipartFile.getOriginalFilename());
//    }
//
//    /**
//     * 注入 请求 上传文件
//     *
//     * @param request       请求对象
//     * @param multipartFile 上传文件
//     * @throws IOException 上传文件操作异常
//     */
//    public void setBodyByFile(HttpEntityEnclosingRequestBase request, MultipartFile multipartFile) throws IOException {
//        setBodyByFile(request, null, multipartFile);
//    }

    /**
     * 注入 请求 参数, 上传文件
     *
     * @param request 请求对象
     * @param params  其他参数
     * @param file    上传文件
     * @throws FileNotFoundException 上传文件操作异常
     */
    public void setBodyByFile(HttpEntityEnclosingRequestBase request, Map<String, String> params, File file) throws FileNotFoundException {
        Objects.requireNonNull(file);
        setBodyByFile(request, params, new FileInputStream(file), file.getName());
    }


    /**
     * 注入 请求 上传文件
     *
     * @param request 请求对象
     * @param file    上传文件
     * @throws FileNotFoundException 上传文件操作异常
     */
    public void setBodyByFile(HttpEntityEnclosingRequestBase request, File file) throws FileNotFoundException {
        setBodyByFile(request, null, file);
    }


    /**
     * 注入默认 Header 请求头
     *
     * @param request 请求对象
     */
    public void setDefaultHeader(HttpRequestBase request) {
        request.setHeader("Content-type", ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
        //  设置请求头，伪装浏览器, 防止反爬虫
        request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");
    }

    /**
     * 注入 header 字段
     *
     * @param request 请求对象
     * @param headers 请求头参数组
     */
    public void setHeader(HttpRequestBase request, Header[] headers) {
        Objects.requireNonNull(request);
        if (ObjectUtils.nonEmpty(headers)) {
            // 会覆盖具有相同名称的第一个头文件
            for (Header header : headers) {
                request.setHeader(header.getName(), header.getValue());
            }
        }
    }


    /**
     * 注入 header 字段
     *
     * @param request 请求对象
     * @param headers 请求头参数 字典
     */
    public void setHeader(HttpRequestBase request, Map<String, String> headers) {
        if (ObjectUtils.nonEmpty(headers)) {
            setHeader(request, headers.entrySet().stream().map(header -> new BasicHeader(header.getKey(), header.getValue())).toArray(Header[]::new));
        }
    }

    /**
     * 注入 header 字段
     *
     * @param request     请求对象
     * @param jsonHeaders 请求头参数 Json 字符串
     */
    public void setHeader(HttpRequestBase request, String jsonHeaders) {
        if (StringUtils.isNotEmpty(jsonHeaders)) {
            setHeader(request, JsonUtils.parseMap(jsonHeaders));
        }
    }

    /**
     * 设置 cookie 值
     *
     * @param cookies cookie 值
     */
    public void setCookies(List<Cookie> cookies) {
        if (ObjectUtils.nonEmpty(cookies)) {
            cookieStore = ObjectUtils.getOrDefault(cookieStore, new BasicCookieStore());
            cookies.forEach(cookieStore::addCookie);
        }
    }

    /**
     * 添加 Cookie
     *
     * @param name    cookie 名
     * @param value   cookie 值
     * @param endTime 过期时间
     */
    public void addCookie(String name, String value, Date endTime) {
        addCookie(name, value, "", "", 1, endTime);
    }

    /**
     * 添加 Cookie
     *
     * @param name    cookie 名
     * @param value   cookie 值
     * @param domain  domain 域
     * @param path    路径属性。
     * @param version 自定义版本号
     * @param endTime 过期时间
     */
    public void addCookie(String name, String value, String domain, String path, int version, Date endTime) {
        BasicClientCookie cookie = new BasicClientCookie(name, value);
        cookie.setExpiryDate(endTime);
        cookie.setDomain(domain);
        cookie.setPath(path);
        cookie.setVersion(version);
        cookieStore = ObjectUtils.getOrDefault(cookieStore, new BasicCookieStore());
        cookieStore.addCookie(cookie);
    }

    /**
     * 注入 HttpContext [请求上下文] 参数
     *
     * @param map 参数字典
     */
    public HttpContext setHttpContext(Map<String, String> map) {
        if (ObjectUtils.isEmpty(map)) {
            return null;
        }
        HttpContext httpContext = new BasicHttpContext();
        map.forEach(httpContext::setAttribute);
        return httpContext;
    }


    /**
     * 发送 http 请求操作
     *
     * @param request     请求对象
     * @param httpContext 请求上下文 参数
     * @param charset     请求响应字符集
     * @return 请求结果对象
     */
    private ResponseBody doSendBase(HttpRequestBase request, HttpContext httpContext, Charset charset) {
        Objects.requireNonNull(request, "request is null");
        // 如果是null 则默认值为  UTF-8
        charset = ObjectUtils.getOrDefault(charset, StandardCharsets.UTF_8);
        CloseableHttpClient httpClient = httpClientBuilder();
        ResponseBody body = null;
        CloseableHttpResponse response = null;
        try {
            request.setConfig(requestConfig);
            setDefaultHeader(request);
            response = ObjectUtils.nonNull(httpContext) ? httpClient.execute(request, httpContext) : httpClient.execute(request);
            // 请求发送成功，并得到响应
            // 请求头
            Map<String, List<String>> headerMap = loadHeaders(response.getAllHeaders());
            // 请求 cookie
            Map<String, String> cookieMap = cookieStore.getCookies().stream().collect(Collectors.toMap(Cookie::getName, Cookie::getValue));
            cookieStore.clear();
            body = new ResponseBody(EntityUtils.toString(response.getEntity(), charset), headerMap, cookieMap, response.getLocale(), response.getStatusLine().getStatusCode());
        } catch (IOException e) {
            logger.error("HttpClientUtils.doSendBase ==> : {} 请求失败 , error:{}", request.getMethod(), e.getMessage(), e);
        } finally {
            try {
                request.releaseConnection();

                if (Objects.nonNull(response)) {
                    response.close();
                }
            } catch (Exception e) {
                logger.error("HttpClientUtils.doSendBase ==> error : {}", e.getMessage(), e);
            }
        }
        return body;
    }


    /**
     * 加载封装 请求头信息
     *
     * @param headers 请求头数组信息
     * @return 封装好的 请求头信息字典
     */
    public Map<String, List<String>> loadHeaders(Header[] headers) {
        Map<String, List<String>> map = new HashMap<>(headers.length);
        for (Header header : headers) {
            map.putIfAbsent(header.getName(), new ArrayList<>());
            map.get(header.getName()).add(header.getValue());
        }
        return map;
    }

    /**
     * 发送请求
     *
     * @param request     请求对象
     * @param headers     请求头 参数
     * @param httpContext 请求上下文 参数
     * @param charset     响应结果集字符集
     * @return 响应体
     */
    public ResponseBody doSend(HttpRequestBase request, Header[] headers, HttpContext httpContext, Charset charset) {
        Objects.requireNonNull(request);
        setHeader(request, headers);
        return doSendBase(request, httpContext, charset);
    }

    /**
     * 发送请求
     *
     * @param request     请求对象
     * @param headers     请求头 参数
     * @param httpContext 请求上下文 参数
     * @param charset     响应结果集字符集
     * @return 响应体
     */
    public ResponseBody doSend(HttpRequestBase request, Map<String, String> headers, Map<String, String> httpContext, Charset charset) {
        setHeader(request, headers);
        return doSendBase(request, setHttpContext(httpContext), charset);
    }

    /**
     * 发送请求
     *
     * @param request     请求对象
     * @param httpContext 请求上下文 参数
     * @param charset     响应结果集字符集
     * @return 响应体
     */
    public ResponseBody doSend(HttpRequestBase request, Map<String, String> httpContext, Charset charset) {
        return doSend(request, null, httpContext, charset);
    }

    /**
     * 发送请求
     *
     * @param request 请求对象
     * @param charset 响应结果集字符集
     * @return 响应体
     */
    public ResponseBody doSend(HttpRequestBase request, Charset charset) {
        return doSend(request, null, charset);
    }

    /**
     * 发送请求
     *
     * @param request 请求对象
     * @return 响应体
     */
    public ResponseBody doSend(HttpRequestBase request) {
        return doSend(request, StandardCharsets.UTF_8);
    }


    /**
     * 发送请求
     *
     * @param request     请求对象
     * @param params      请求参数 , 设置 路径参数
     * @param headers     请求头 参数
     * @param httpContext 请求上下文 参数
     * @param charset     响应结果集字符集
     * @return 响应体
     */
    public ResponseBody doSend(HttpRequestBase request, Map<String, Object> params, Map<String, String> headers, Map<String, String> httpContext, Charset charset) {
        setPathParams(request, params);
        return doSend(request, headers, httpContext, charset);
    }

    /**
     * 发送请求
     *
     * @param request     请求对象
     * @param jsonParams  请求参数 , 设置 路径参数
     * @param headers     请求头 参数
     * @param httpContext 请求上下文 参数
     * @param charset     响应结果集字符集
     * @return 响应体
     */
    public ResponseBody doSend(HttpRequestBase request, String jsonParams, Map<String, String> headers, Map<String, String> httpContext, Charset charset) {
        setPathParams(request, jsonParams);
        return doSend(request, headers, httpContext, charset);
    }

    /**
     * 发送请求
     *
     * @param request     请求对象
     * @param params      请求参数 ,  设置表单 参数
     * @param headers     请求头 参数
     * @param httpContext 请求上下文 参数
     * @param charset     响应结果集字符集
     * @return 响应体
     */
    public ResponseBody doSend(HttpEntityEnclosingRequestBase request, Map<String, Object> params, Map<String, String> headers, Map<String, String> httpContext, Charset charset) {
        setBodyByForm(request, params);
        return doSend(request, headers, httpContext, charset);
    }

    /**
     * 发送请求
     *
     * @param request     请求对象
     * @param jsonParams  请求参数 ,  设置 body  json 参数
     * @param headers     请求头 参数
     * @param httpContext 请求上下文 参数
     * @param charset     响应结果集字符集
     * @return 响应体
     */
    public ResponseBody doSend(HttpEntityEnclosingRequestBase request, String jsonParams, Map<String, String> headers, Map<String, String> httpContext, Charset charset) {
        setBodyByJson(request, jsonParams);
        return doSend(request, headers, httpContext, charset);
    }


    /**
     * get 请求
     *
     * @param url         请求地址
     * @param params      请求参数 {@link Map}
     * @param headers     请求头参数
     * @param httpContext httpContxt 参数
     * @param charset     响应字符集类型
     * @return 请求响应信息
     */
    @Override
    public ResponseBody get(String url, Map<String, Object> params, Map<String, String> headers, Map<String, String> httpContext, Charset charset) {
        return doSend(requestBuilder(url, HttpGet.class), params, headers, httpContext, charset);
    }

    /**
     * get 请求
     *
     * @param url         请求地址
     * @param json        请求参数 - json 数据格式
     * @param headers     请求头参数
     * @param httpContext httpContext 参数
     * @param charset     响应字符集类型
     * @return 请求响应信息
     */
    @Override
    public ResponseBody get(String url, String json, Map<String, String> headers, Map<String, String> httpContext, Charset charset) {
        return doSend(requestBuilder(url, HttpGet.class), json, headers, httpContext, charset);
    }

    /**
     * post 请求
     *
     * @param url         请求地址
     * @param params      请求参数 - body:form-data
     * @param headers     请求头参数
     * @param httpContext httpContext 参数
     * @param charset     响应字符集类型
     * @return 请求响应信息
     */
    @Override
    public ResponseBody post(String url, Map<String, Object> params, Map<String, String> headers, Map<String, String> httpContext, Charset charset) {
        return doSend(requestBuilder(url, HttpPost.class), params, headers, httpContext, charset);
    }

    /**
     * post 请求
     *
     * @param url         请求地址
     * @param json        请求参数 - body:json
     * @param headers     请求头参数
     * @param httpContext httpContext 参数
     * @param charset     响应字符集类型
     * @return 请求响应信息
     */
    @Override
    public ResponseBody post(String url, String json, Map<String, String> headers, Map<String, String> httpContext, Charset charset) {
        return doSend(requestBuilder(url, HttpPost.class), json, headers, httpContext, charset);
    }

    /**
     * put 请求
     *
     * @param url         请求地址
     * @param params      请求参数 - body:form-data
     * @param headers     请求头参数
     * @param httpContext httpContext 参数
     * @param charset     响应字符集类型
     * @return 请求响应信息
     */
    @Override
    public ResponseBody put(String url, Map<String, Object> params, Map<String, String> headers, Map<String, String> httpContext, Charset charset) {
        return doSend(requestBuilder(url, HttpPut.class), params, headers, httpContext, charset);
    }

    /**
     * put 请求
     *
     * @param url         请求地址
     * @param json        请求参数 - body:json
     * @param headers     请求头参数
     * @param httpContext httpContext 参数
     * @param charset     响应字符集类型
     * @return 请求响应信息
     */
    @Override
    public ResponseBody put(String url, String json, Map<String, String> headers, Map<String, String> httpContext, Charset charset) {
        return doSend(requestBuilder(url, HttpPut.class), json, headers, httpContext, charset);
    }

    /**
     * delete 请求
     *
     * @param url         请求地址
     * @param params      请求参数
     * @param headers     请求头参数
     * @param httpContext httpContext 参数
     * @param charset     响应字符集类型
     * @return 请求响应信息
     */
    @Override
    public ResponseBody delete(String url, Map<String, Object> params, Map<String, String> headers, Map<String, String> httpContext, Charset charset) {
        return doSend(requestBuilder(url, HttpDelete.class), params, headers, httpContext, charset);
    }

    /**
     * delete 请求
     *
     * @param url         请求地址
     * @param json        请求参数 - body:json
     * @param headers     请求头参数
     * @param httpContext httpContext 参数
     * @param charset     响应字符集类型
     * @return 请求响应信息
     */
    @Override
    public ResponseBody delete(String url, String json, Map<String, String> headers, Map<String, String> httpContext, Charset charset) {
        return doSend(requestBuilder(url, HttpDelete.class), json, headers, httpContext, charset);
    }

    /**
     * 验证网址信息, 如果不包含 http(s):// 则拼接上
     *
     * @param url 网址信息
     * @return 处理好的网址信息
     */
    public String verifyUrl(String url) {
        Objects.requireNonNull(url);
        url = url.trim();
        final int sepIndex = url.indexOf("://");
        if (sepIndex < 0) {
            url = "http://" + url;
        }
        return url;
    }

    /**
     * 请求构建器
     *
     * @param url   请求地址
     * @param clazz 请求方式
     * @param <T>   请求方式类型
     * @return 请求对象
     */
    public <T extends HttpRequestBase> T requestBuilder(String url, Class<T> clazz) {
        try {
            T t = clazz.newInstance();
            t.setURI(new URI(verifyUrl(url)));
            return t;
        } catch (Exception e) {
            logger.error("HttpClientUtils.requestBuilder, 创建 请求对象错误,error : {}", e.getMessage(), e);
        }
        return null;
    }


}
