package fun.pullock.starter.feign.decoder;

import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import fun.pullock.general.model.Result;
import fun.pullock.starter.json.Json;

import java.io.IOException;
import java.lang.reflect.Type;

public class FeignUnwrapDecoder implements Decoder {

    @Override
    public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
        if (response == null || response.body() == null) {
            return null;
        }

        Result<?> result = Json.toObject(
                Util.toString(response.body().asReader(response.charset())),
                Result.class
        );

        // 接口返回值显式的定义成Result<T>类型的时候，直接返回
        if (response.request().requestTemplate().methodMetadata().method().getReturnType() == Result.class) {
            return result;
        }

        if (result.getCode() == 0) {
            return result.getData();
        }

        throw new DecodeException(result.getCode(), result.getMsg(), response.request());
    }
}
