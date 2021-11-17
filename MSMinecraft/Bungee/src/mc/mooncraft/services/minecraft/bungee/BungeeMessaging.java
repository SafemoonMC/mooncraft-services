package mc.mooncraft.services.minecraft.bungee;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import mc.mooncraft.services.datamodels.ProtocolKeys;
import net.md_5.bungee.api.ProxyServer;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class BungeeMessaging {
    
    /*
    Constructor
     */
    public BungeeMessaging() {
        ProxyServer.getInstance().registerChannel(ProtocolKeys.BUNGEE);
    }
    
    /*
    Private Methods
     */
    void unload() {
        ProxyServer.getInstance().unregisterChannel(ProtocolKeys.BUNGEE);
    }
    
    /*
    Methods
     */
    @SuppressWarnings("UnstableApiUsage")
    public CompletableFuture<Boolean> sendJsonMessage(@NotNull String channel, @NotNull String jsonMessage) {
        if (ProxyServer.getInstance().getServers().isEmpty()) return CompletableFuture.completedFuture(false);
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(channel);
        out.writeUTF(jsonMessage);
        
        // Communicate to all the servers
        return CompletableFuture.supplyAsync(() -> {
            ProxyServer.getInstance().getServers().values()
                    .forEach(serverInfo -> serverInfo.sendData(ProtocolKeys.BUNGEE, out.toByteArray()));
            return true;
        });
    }
    
}