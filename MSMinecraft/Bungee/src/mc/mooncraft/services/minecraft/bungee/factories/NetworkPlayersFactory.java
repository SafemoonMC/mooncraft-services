package mc.mooncraft.services.minecraft.bungee.factories;


import lombok.Getter;
import mc.mooncraft.services.datamodels.NetworkPlayers;
import mc.mooncraft.services.minecraft.bungee.MSMinecraftMain;
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
    Constants
     */
    public static final @NotNull String REDIS_KEY = "mooncraft-services-network-players";
    
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
                                    MSMinecraftMain.getInstance().getJedisManager().addKeyValue(REDIS_KEY, MSMinecraftMain.getGson().toJson(networkPlayers));
                                    return newNetworkPlayers;
                                });
    }
}