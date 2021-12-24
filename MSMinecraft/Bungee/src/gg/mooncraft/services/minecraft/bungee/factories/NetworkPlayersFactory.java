package gg.mooncraft.services.minecraft.bungee.factories;


import gg.mooncraft.services.minecraft.bungee.MSMinecraftMain;
import lombok.Getter;
import gg.mooncraft.services.datamodels.NetworkPlayers;
import gg.mooncraft.services.datamodels.ProtocolKeys;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Getter
public final class NetworkPlayersFactory {
    
    /*
    Fields
     */
    private @NotNull NetworkPlayers networkPlayers;
    
    /*
    Constructor
     */
    public NetworkPlayersFactory() {
        this.networkPlayers = update().join();
    }
    
    /*
    Methods
     */
    public @NotNull CompletableFuture<NetworkPlayers> update() {
        return CompletableFuture.supplyAsync(() -> {
                    Map<String, List<UUID>> map = ProxyServer.getInstance().getServers()
                            .values()
                            .stream()
                            .collect(Collectors.toMap(ServerInfo::getName, serverInfo -> serverInfo.getPlayers()
                                    .stream()
                                    .map(ProxiedPlayer::getUniqueId)
                                    .collect(Collectors.toList())));
                    return new NetworkPlayers(map);
                })
                .thenApply(newNetworkPlayers -> this.networkPlayers = newNetworkPlayers)
                .thenApply(newNetworkPlayers -> {
//                    MSMinecraftMain.getInstance().getLogger().info("Adding NetworkPlayers into Redis memory...");
                    MSMinecraftMain.getInstance().getJedisManager().addKeyValue(ProtocolKeys.REDIS_NETWORK_PLAYERS, MSMinecraftMain.getGson().toJson(newNetworkPlayers));
                    return newNetworkPlayers;
                })
                .thenApply(newNetworkPlayers -> {
//                    MSMinecraftMain.getInstance().getLogger().info("Sending NetworkPlayers to all servers...");
                    MSMinecraftMain.getInstance().getBungeeMessaging().sendJsonMessage(ProtocolKeys.BUNGEE_NETWORK_PLAYERS, MSMinecraftMain.getGson().toJson(newNetworkPlayers));
                    return newNetworkPlayers;
                });
    }
}