package gg.mooncraft.services.minecraft.bungee.factories;


import gg.mooncraft.services.datamodels.NetworkServers;
import gg.mooncraft.services.datamodels.ProtocolKeys;
import gg.mooncraft.services.minecraft.bungee.MSMinecraftMain;
import gg.mooncraft.services.minecraft.bungee.config.prefabs.ServersDisplayPrefab;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Getter
public final class NetworkServersFactory {
    
    /*
    Fields
     */
    private @NotNull NetworkServers networkServers;
    
    /*
    Constructor
     */
    public NetworkServersFactory() {
        this.networkServers = update().join();
    }
    
    /*
    Methods
     */
    public @NotNull CompletableFuture<NetworkServers> update() {
        return CompletableFuture.supplyAsync(() -> {
                    Set<String> serverList = ProxyServer.getInstance().getServers().keySet();
                    List<String> groupsList = serverList.stream().map(serverName -> serverName.contains("-") ? serverName.split("-")[0] : serverName).collect(Collectors.toList());
                    
                    Map<String, NetworkServers.ServerGroup> map = new HashMap<>();
                    for (String groupName : groupsList) {
                        String display = MSMinecraftMain.getInstance().getConfigurationPrefabs().getServersDisplayPrefab().getServerDisplay(groupName).map(ServersDisplayPrefab.ServerDisplay::display).orElse(groupName);
                        List<String> servers = serverList.stream().filter(serverName -> serverName.toLowerCase().startsWith(groupName)).collect(Collectors.toList());
                        
                        NetworkServers.ServerGroup serverGroup = new NetworkServers.ServerGroup(display, servers);
                        map.put(groupName, serverGroup);
                    }
                    
                    return new NetworkServers(map);
                })
                .thenApply(newNetworkServers -> this.networkServers = newNetworkServers)
                .thenApply(newNetworkServers -> {
//                    MSMinecraftMain.getInstance().getLogger().info("Adding NetworkServers into Redis memory...");
                    MSMinecraftMain.getInstance().getJedisManager().addKeyValue(ProtocolKeys.REDIS_NETWORK_SERVERS, MSMinecraftMain.getGson().toJson(newNetworkServers));
                    return newNetworkServers;
                });
    }
}