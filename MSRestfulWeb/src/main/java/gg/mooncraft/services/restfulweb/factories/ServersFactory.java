package gg.mooncraft.services.restfulweb.factories;

import gg.mooncraft.services.datamodels.NetworkCounters;
import gg.mooncraft.services.datamodels.NetworkServers;
import gg.mooncraft.services.datamodels.ProtocolKeys;
import gg.mooncraft.services.restfulweb.Application;
import gg.mooncraft.services.restfulweb.ApplicationBootstrap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

public final class ServersFactory {
    
    /*
    Fields
     */
    private final int cacheEvictionTime;
    private @Nullable CompletableFuture<NetworkServers> futureNetworkServers;
    private @Nullable CompletableFuture<NetworkCounters> futureNetworkCounters;
    private long networkServersLastEviction;
    private long networkCountersLastEviction;
    
    /*
    Constructor
     */
    public ServersFactory() {
        this.cacheEvictionTime = ApplicationBootstrap.getApplication().getProperties().map(propertiesWrapper -> propertiesWrapper.getPropertyInteger("restapi.cache.eviction-time", 10)).orElse(10);
        getNetworkServers();
        getNetworkCounters();
    }
    
    /*
    Methods
     */
    public @NotNull CompletableFuture<NetworkServers> getNetworkServers() {
        if (this.futureNetworkServers == null || Duration.between(Instant.ofEpochMilli(this.networkServersLastEviction), Instant.now()).toSeconds() >= this.cacheEvictionTime) {
            this.networkServersLastEviction = System.currentTimeMillis();
            return this.futureNetworkServers = loadNetworkServers();
        }
        return this.futureNetworkServers;
    }
    
    public @NotNull CompletableFuture<NetworkCounters> getNetworkCounters() {
        if (this.futureNetworkCounters == null || Duration.between(Instant.ofEpochMilli(this.networkCountersLastEviction), Instant.now()).toSeconds() >= this.cacheEvictionTime) {
            this.networkCountersLastEviction = System.currentTimeMillis();
            return this.futureNetworkCounters = loadNetworkCounters();
        }
        return this.futureNetworkCounters;
    }
    
    private @NotNull CompletableFuture<NetworkServers> loadNetworkServers() {
        return ApplicationBootstrap.getApplication().getJedisManager().getKeyValue(ProtocolKeys.REDIS_NETWORK_SERVERS)
                .thenApply(value -> Application.GSON_REDIS.fromJson(value, NetworkServers.class));
    }
    
    private @NotNull CompletableFuture<NetworkCounters> loadNetworkCounters() {
        return ApplicationBootstrap.getApplication().getJedisManager().getKeyValue(ProtocolKeys.REDIS_NETWORK_COUNTERS)
                .thenApply(value -> Application.GSON_REDIS.fromJson(value, NetworkCounters.class));
    }
}