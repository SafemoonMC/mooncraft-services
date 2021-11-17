package mc.mooncraft.services.minecraft.bukkit;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import mc.mooncraft.services.datamodels.NetworkCounters;
import mc.mooncraft.services.datamodels.ProtocolKeys;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class BungeeMessaging implements PluginMessageListener {
    
    /*
    Constructor
     */
    public BungeeMessaging() {
        Bukkit.getMessenger().registerIncomingPluginChannel(MSMinecraftMain.getInstance(), ProtocolKeys.BUNGEE, this);
    }
    
    /*
    Private Methods
     */
    void unload() {
        Bukkit.getMessenger().unregisterIncomingPluginChannel(MSMinecraftMain.getInstance(), ProtocolKeys.BUNGEE);
    }
    
    /*
    Override Methods
     */
    @Override
    @SuppressWarnings("UnstableApiUsage")
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte[] bytes) {
        if (!channel.equalsIgnoreCase(ProtocolKeys.BUNGEE)) return;
        ByteArrayDataInput input = ByteStreams.newDataInput(bytes);
        
        String subChannel = input.readUTF();
        if (subChannel.equalsIgnoreCase(ProtocolKeys.BUNGEE_NETWORK_COUNTERS)) {
            String content = input.readUTF();
            NetworkCounters networkCounters = MSMinecraftMain.getGson().fromJson(content, NetworkCounters.class);
            MSMinecraftMain.getInstance().getNetworkCountersFactory().update(networkCounters);
            MSMinecraftMain.getInstance().getLogger().info("Received NetworkCounters from Bungee: " + networkCounters + ".");
        }
    }
}