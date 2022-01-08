package xyz.liangxin.utils.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * <p> 描述
 *
 * @author liangxin
 * @version V1.0
 * @since 创建时间: 2022/1/6 17:28
 */
class DoubleSerializer extends JsonSerializer<Double> {
    @Override
    public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        BigDecimal d = BigDecimal.valueOf(value);
        gen.writeNumber(d.stripTrailingZeros().toPlainString());
    }

    @Override
    public Class<Double> handledType() {
        return Double.class;
    }
}
