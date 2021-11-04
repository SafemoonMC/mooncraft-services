package mc.mooncraft.services.minecraft.bungee.handlers;

import mc.mooncraft.services.minecraft.bungee.MSMinecraftMain;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;

public class ServerListeners implements Listener {
    
    /*
    Constructor
     */
    public ServerListeners() {
        ProxyServer.getInstance().getPluginManager().registerListener(MSMinecraftMain.getInstance(), this);
    }
    
    /*
    Handlers
     */
    @EventHandler
    public void on(@NotNull ServerConnectedEvent e) {
        MSMinecraftMain.getInstance().getNetworkCountersFactory().update();
    }
    
    @EventHandler
    public void on(@NotNull ServerDisconnectEvent e) {
        MSMinecraftMain.getInstance().getNetworkCountersFactory().update();
    }
}