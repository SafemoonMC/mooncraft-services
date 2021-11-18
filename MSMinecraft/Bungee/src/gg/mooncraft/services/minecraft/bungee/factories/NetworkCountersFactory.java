package gg.mooncraft.services.minecraft.bungee.factories;


import gg.mooncraft.services.minecraft.bungee.MSMinecraftMain;
import lombok.Getter;
import gg.mooncraft.services.datamodels.NetworkCounters;
import gg.mooncraft.services.datamodels.ProtocolKeys;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Getter
public final class NetworkCountersFactory {
    
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
                    return new NetworkCounters(map, totalOnlinePlayers);
                })
                .thenApply(newNetworkCounters -> this.networkCounters = newNetworkCounters)
                .thenApply(newNetworkCounters -> {
                    MSMinecraftMain.getInstance().getLogger().info("Adding NetworkCounters into Redis memory...");
                    MSMinecraftMain.getInstance().getJedisManager().addKeyValue(ProtocolKeys.REDIS_NETWORK_COUNTERS, MSMinecraftMain.getGson().toJson(newNetworkCounters));
                    return newNetworkCounters;
                })
                .thenApply(newNetworkCounters -> {
                    MSMinecraftMain.getInstance().getLogger().info("Sending NetworkCounters to all servers...");
                    MSMinecraftMain.getInstance().getBungeeMessaging().sendJsonMessage(ProtocolKeys.BUNGEE_NETWORK_COUNTERS, MSMinecraftMain.getGson().toJson(newNetworkCounters));
                    return newNetworkCounters;
                });
    }
}