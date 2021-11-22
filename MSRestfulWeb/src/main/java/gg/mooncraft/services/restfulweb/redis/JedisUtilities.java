package gg.mooncraft.services.restfulweb.redis;

import gg.mooncraft.services.restfulweb.properties.PropertiesWrapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class JedisUtilities {
    
    public static @NotNull HostAndPort parseHostAndPort(@NotNull PropertiesWrapper propertiesWrapper) {
        return new HostAndPort(propertiesWrapper.getProperty("redis.hostname"), Integer.parseInt(propertiesWrapper.getProperty("redis.port")));
    }
    
    public static @NotNull JedisPoolConfig parsePoolConfig(@NotNull PropertiesWrapper propertiesWrapper) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMinIdle(propertiesWrapper.getPropertyInteger("redis.pool-settings.min-idle", 10));
        jedisPoolConfig.setMaxIdle(propertiesWrapper.getPropertyInteger("redis.pool-settings.max-idle", 20));
        jedisPoolConfig.setMaxTotal(propertiesWrapper.getPropertyInteger("redis.pool-settings.max-total", 20));
        jedisPoolConfig.setNumTestsPerEvictionRun(propertiesWrapper.getPropertyInteger("redis.pool-settings.num-tests-per-eviction-run", 3));
        jedisPoolConfig.setMinEvictableIdleTime(Duration.ofMillis(propertiesWrapper.getPropertyInteger("redis.pool-settings.min-evictable-idle-time-millis", 60000)));
        jedisPoolConfig.setTimeBetweenEvictionRuns(Duration.ofMillis(propertiesWrapper.getPropertyInteger("redis.pool-settings.time-between-eviction-runs-millis", 30000)));
        return jedisPoolConfig;
    }
}