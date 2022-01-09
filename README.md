# 工具包

> xyz.liangxin.utils

## 常量包

> xyz.liangxin.utils.constant

### 时间日期常量

> xyz.liangxin.utils.constant.date

`CalendarUnitEnum`: 日期各个部分的枚举

`DateConstant`:  时间工具类常量

`DateFormatEnum`: 时间格式化枚举列表

`DateUnit`: 日期时间单位枚举，每个单位都是以毫秒为基数

`MonthEnum`: 月份枚举

`QuarterEnum`: 季度枚举

`WeekEnum`:   星期枚举, 与Calendar中的星期int值对应

### 数字工具常量

> xyz.liangxin.utils.constant.number

`OperatorsEnum`: 运算符枚举

### 字符文本 常量

> xyz.liangxin.utils.constant.text

`CharConstant`:字符常量

`CharSequenceConstant`:字符串常量

`CharsetConstant`:字符集常量

## 代码包

> xyz.liangxin.utils.core

`ObjectUtils`: 对象工具类

### 数组工具包

> xyz.liangxin.utils.core.array

`ArrayUtil`: 数组对象工具类

`PrimitiveArrayUtil`: 原始类型数组工具类

### 建造者包

> xyz.liangxin.utils.core.builder

`BeanBuilder`: 通用 Bean 构造器
`ExecuteBuilder`: 通用 数据处理器责任链构造器

### 时间工具包

> xyz.liangxin.utils.core.date

`DateTime`: 时间对象封装拓展类

`DateUtils`: 时间 工具类

`WeekUtils`: 日期工具类, 周

### JDK 8 日期时间工具包

> xyz.liangxin.utils.core.date.local_date

`LocalDateTimeUtils`: 日期时间工具类

`LocalDateUtils`: 日期工具类

`LocalTimeUtils`: 时间工具类

### Lambda函数包

> xyz.liangxin.utils.core.functions

`BiConsumer2`: 用于 三个参数值的消费 接口

### 数字工具包

> xyz.liangxin.utils.core.number

`CalculateUtils`: 数字计算工具类

`NumberUtils`: 数字工具类

### 文件字符工具包

> xyz.liangxin.utils.core.text

`CharSequenceUtil`: CharSequence 相关工具类封装

`CharsetUtil`: 字符集工具类

`CharUtil`: 字符工具类

`StringUtils`: 字符串工具类

## 数据库包

> xyz.liangxin.utils.database

`DbUtils`: 数据库工具类规范接口

`JdbcUtils`: JDBC 工具类

## 加密包

> xyz.liangxin.utils.digest

`BCrypt`: BCrypt加密算法实现

`BryptUtil`: Brypt 加密工具类

`FileSafeCode`: 获取文件摘要工具类

`MD5Utils`:  MD5 摘要工具类

`PassWordEncoder`:  密码加密工具类 【 Brypt 算法】

## IO 包

> xyz.liangxin.utils.io

## JSON 包

> xyz.liangxin.utils.json

`FastJsonUtils`: FastJson 工具类

`JacksonUtils`:  Jackson 工具类

`DoubleSerializer`: Double类型 序列化器 , 用于 Jackson

`JsonUtils`:  Json 工具类, 此版本工具类, 使用的{@link JacksonUtils}

## Swing 窗口包

> xyz.liangxin.utils.swing

`ComponentUtils`: 组件工具类

`SwingUtil`: Swing 工具类

`WindowToolkit`: WindowToolkit

### 事件工具包

> xyz.liangxin.utils.swing.listener

`ListenerUtil`: 组件事件工具类

`MouseListenerUtil`: 组件 鼠标事件 工具类

## WEB 网络包

> xyz.liangxin.utils.web


`WebUtils`:  WEB 操作分析工具类

### HTTP网络连接工具包

> xyz.liangxin.utils.web.http

`HttpUtilInterface`:  Http 连接工具接口

`ResponseBody`:  Http 响应数据包装类

`HttpClientUtils`:  HttpClient 工具类

`HttpUtils`:  Http 工具类 ， 基于 HttpClient

### IP 分析工具包

> xyz.liangxin.utils.web.ip

`GeoLite2IpUtils`: IP 定位工具类

`IpInfo`: IP 信息包装实体

## 其他

> xyz.liangxin.utils.other

`Annotation`: 注释信息工具类