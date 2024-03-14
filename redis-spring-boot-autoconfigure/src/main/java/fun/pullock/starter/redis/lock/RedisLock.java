package fun.pullock.starter.redis.lock;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.time.Duration;
import java.util.Collections;

public class RedisLock {

    private static final DefaultRedisScript<Long> UNLOCK_LUA_SCRIPT = new DefaultRedisScript<>(
            "if redis.call('get', KEYS[1] == ARGV[1]) then return redis.call('del', KEYS[1])  else return 0 end",
            Long.class
    );

    private static final Long SUCCESS = 1L;

    private final RedisTemplate<String, Object> stringObjectRedisTemplate;

    private final String namespace;


    public RedisLock(RedisTemplate<String, Object> stringObjectRedisTemplate) {
        this(stringObjectRedisTemplate, null);
    }

    public RedisLock(RedisTemplate<String, Object> stringObjectRedisTemplate, String namespace) {
        this.stringObjectRedisTemplate = stringObjectRedisTemplate;
        this.namespace = namespace;
    }

    /**
     * 阻塞加锁
     * @param key 锁的key
     * @param timeout 锁的超时时间
     * @param spinTimeout 自旋超时时间
     * @return 是否加锁成功
     */
    public boolean lock(String key, long timeout, long spinTimeout) {
        long now = System.currentTimeMillis();

        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException();
        }
        key = getKey(key);

        long expire = now + spinTimeout;
        boolean acquired;
        while (!(acquired = tryLock(key, timeout)) && System.currentTimeMillis() < expire) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // ignore
            }
        }
        return acquired;
    }

    /**
     * 阻塞加锁
     * @param key 锁的key
     * @param timeout 锁的超时时间
     * @param clientId 加锁的客户端ID
     * @param spinTimeout 自旋超时时间
     * @return 是否加锁成功
     */
    public boolean lock(String key, long timeout, String clientId, long spinTimeout) {
        long now = System.currentTimeMillis();

        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException();
        }
        key = getKey(key);

        long expire = now + spinTimeout;
        boolean acquired;
        while (!(acquired = tryLock(key, timeout, clientId)) && System.currentTimeMillis() < expire) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // ignore
            }
        }
        return acquired;
    }

    /**
     * 阻塞加锁
     * @param key 锁的key
     * @param timeout 锁的超时时间
     * @return 是否加锁成功
     */
    public boolean lock(String key, long timeout) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException();
        }
        key = getKey(key);

        boolean acquired;
        while (!(acquired = tryLock(key, timeout))) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // ignore
            }
        }
        return acquired;
    }

    /**
     * 阻塞加锁
     * @param key 锁的key
     * @param timeout 锁的超时时间
     * @param clientId 加锁的客户端ID
     * @return 是否加锁成功
     */
    public boolean lock(String key, long timeout, String clientId) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException();
        }
        key = getKey(key);

        boolean acquired;
        while (!(acquired = tryLock(key, timeout, clientId))) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // ignore
            }
        }
        return acquired;
    }

    /**
     * 非阻塞加锁
     * @param key 锁的key
     * @param timeout 锁的超时时间
     * @return 是否加锁成功
     */
    public boolean tryLock(String key, long timeout) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException();
        }

        key = getKey(key);
        Boolean result = stringObjectRedisTemplate.opsForValue().setIfAbsent(key, "lock", Duration.ofMillis(timeout));
        return Boolean.TRUE.equals(result);
    }

    /**
     * 非阻塞加锁
     * @param key 锁的key
     * @param timeout 锁的超时时间
     * @param clientId 加锁的客户端ID
     * @return 是否加锁成功
     */
    public boolean tryLock(String key, long timeout, String clientId) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException();
        }

        if (clientId == null || clientId.isEmpty()) {
            throw new IllegalArgumentException();
        }

        key = getKey(key);
        Boolean result = stringObjectRedisTemplate.opsForValue().setIfAbsent(key, clientId, Duration.ofMillis(timeout));
        return Boolean.TRUE.equals(result);
    }

    /**
     * 解锁
     * @param key 锁的key
     * @return 是否解锁成功
     */
    public boolean unlock(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException();
        }

        key = getKey(key);
        Boolean result = stringObjectRedisTemplate.delete(key);
        return Boolean.TRUE.equals(result);
    }

    /**
     * 解锁
     * @param key 锁的key
     * @param clientId 解锁的客户端ID
     * @return 是否解锁成功
     */
    public boolean unlock(String key, String clientId) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException();
        }

        key = getKey(key);
        Long result = stringObjectRedisTemplate.execute(UNLOCK_LUA_SCRIPT, Collections.singletonList(key), clientId);
        return SUCCESS.equals(result);
    }

    private String getKey(String key) {
        if (namespace == null || namespace.isEmpty()) {
            return key;
        }

        String prefix = String.format("%s::", namespace);

        if (key.startsWith(prefix)) {
            return key;
        }

        return String.format("%s%s", prefix, key);
    }
}
