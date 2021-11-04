package mc.mooncraft.services.minecraft.bungee.factories;


import mc.mooncraft.services.minecraft.bungee.MSMinecraftMain;
import lombok.Getter;
import mc.mooncraft.services.minecraft.bungee.model.NetworkCounters;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Getter
public final class NetworkCountersFactory {
    
    /*
    Constants
     */
    public static final @NotNull String REDIS_KEY = "mooncraft-services-network-counters";
    
    /*
    Fields
     */
    private @NotNull NetworkCounters networkCounters;
    
    /*
    Constructor
     */
    public NetworkCountersFactory() {
        this.networkCounters = update().join();
    }
    
    /*
    Methods
     */
    public @NotNull CompletableFuture<NetworkCounters> update() {
        return CompletableFuture.supplyAsync(() -> {
                                    Map<String, Long> map = ProxyServer.getInstance().getServers()
                                                                       .values()
                                                                       .stream()
                                                                       .collect(Collectors.toMap(ServerInfo::getName, serverInfo -> (long) serverInfo.getPlayers().size()));
                                    long totalOnlinePlayers = map.values().stream().mapToLong(Long::longValue).sum();
                                    return new NetworkCounters(totalOnlinePlayers, map);
                                })
                                .thenApply(newNetworkCounters -> this.networkCounters = newNetworkCounters)
                                .thenApply(newNetworkCounters -> {
                                    MSMinecraftMain.getInstance().getJedisManager().addKeyValue(REDIS_KEY, MSMinecraftMain.getGson().toJson(networkCounters));
                                    return newNetworkCounters;
                                });
    }
}