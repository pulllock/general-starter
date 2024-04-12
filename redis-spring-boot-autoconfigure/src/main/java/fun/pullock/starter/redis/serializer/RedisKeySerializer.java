package fun.pullock.starter.redis.serializer;

import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.nio.charset.StandardCharsets;

public class RedisKeySerializer extends StringRedisSerializer {

    private final String namespace;

    public RedisKeySerializer(String namespace) {
        super(StandardCharsets.UTF_8);
        this.namespace = namespace;
    }

    @Override
    public String deserialize(byte[] bytes) {
        String key = super.deserialize(bytes);
        if (key == null) {
            return null;
        }

        if (namespace == null || namespace.isEmpty()) {
            return key;
        }

        String prefix = String.format("%s::", namespace);

        if (key.startsWith(prefix)) {
            return key.replaceFirst(prefix, "");
        }

        return key;
    }

    @Override
    public byte[] serialize(String value) {
        byte[] bytes = super.serialize(value);
        if (bytes == null) {
            return null;
        }

        if (namespace == null || namespace.isEmpty()) {
            return bytes;
        }

        String prefix = String.format("%s::", namespace);

        if (!value.startsWith(prefix)) {
            value = String.format("%s%s", prefix, value);
        }

        return value.getBytes(StandardCharsets.UTF_8);
    }
}
