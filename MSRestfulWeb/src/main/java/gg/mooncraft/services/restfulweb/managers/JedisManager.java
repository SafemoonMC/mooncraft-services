package gg.mooncraft.services.restfulweb.managers;

import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.CompletableFuture;

public final class JedisManager {
    
    /*
    Fields
     */
    private final @NotNull JedisPool jedisPool;
    
    /*
    Constructor
     */
    public JedisManager(@NotNull JedisPoolConfig jedisPoolConfig, @NotNull HostAndPort hostAndPort, @NotNull String username, @NotNull String password) {
        this.jedisPool = new JedisPool(jedisPoolConfig, hostAndPort.getHost(), hostAndPort.getPort(), username, password);
    }
    
    /*
    Methods
     */
    public @NotNull CompletableFuture<String> getKeyValue(@NotNull String key) {
        if (jedisPool.isClosed()) return CompletableFuture.completedFuture(null);
        return CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.get(key);
            }
        });
    }
    
    public void stop() {
        if (!jedisPool.isClosed()) jedisPool.close();
    }
}