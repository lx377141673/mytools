package xyz.liangxin.utils.core;

import org.junit.Test;
import xyz.liangxin.utils.web.http.ResponseBody;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * <p> 描述
 *
 * @author liangxin
 * @version V1.0
 * @since 创建时间: 2022/1/20 19:31
 */
public class ReflectUtilsTest {

    @Test
    public void test() {
        ResponseBody responseBody = new ResponseBody("1111", 100);
        final Field body = ReflectUtils.getField(responseBody, "success");
        Method getMethod = ReflectUtils.fieldGetMethod(responseBody, "success");
        System.out.println(ReflectUtils.invoke(responseBody, getMethod));
        Method setMethod = ReflectUtils.fieldSetMethod(responseBody, "success");
        ReflectUtils.invoke(responseBody, setMethod, true);
        System.out.println(responseBody.isSuccess());
    }
}
