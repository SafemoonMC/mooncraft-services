package gg.mooncraft.services.minecraft.core;

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
    public @NotNull CompletableFuture<Boolean> addKeyValue(@NotNull String key, @NotNull String value) {
        if (jedisPool.isClosed()) return CompletableFuture.completedFuture(false);
        return CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.set(key, value);
                return true;
            } catch (Exception e) {
                System.out.println("Error addKeyValue: " + e);
                return false;
            }
        });
    }

    public @NotNull CompletableFuture<Boolean> delKeyValue(@NotNull String key) {
        if (jedisPool.isClosed()) return CompletableFuture.completedFuture(false);
        return CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.del(key);
                return true;
            } catch (Exception e) {
                System.out.println("Error delKeyValue: " + e);
                return false;
            }
        });
    }

    public @NotNull CompletableFuture<String> getKeyValue(@NotNull String key) {
        if (jedisPool.isClosed()) return CompletableFuture.completedFuture(null);
        return CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.get(key);
            } catch (Exception e) {
                System.out.println("Error getKeyValue: " + e);
                return null;
            }
        });
    }

    public void stop() {
        if (!jedisPool.isClosed()) jedisPool.close();
    }
}