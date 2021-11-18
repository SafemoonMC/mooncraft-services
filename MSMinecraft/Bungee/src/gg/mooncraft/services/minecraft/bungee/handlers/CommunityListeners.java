package gg.mooncraft.services.minecraft.bungee.handlers;

import gg.mooncraft.services.minecraft.bungee.MSMinecraftMain;
import gg.mooncraft.services.minecraft.bungee.database.dao.CommunityDAO;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;

public class CommunityListeners implements Listener {
    
    /*
    Constructor
     */
    public CommunityListeners() {
        ProxyServer.getInstance().getPluginManager().registerListener(MSMinecraftMain.getInstance(), this);
    }
    
    /*
    Handlers
     */
    @EventHandler
    public void on(@NotNull PostLoginEvent e) {
        ProxiedPlayer proxiedPlayer = e.getPlayer();
        CommunityDAO.handle(proxiedPlayer).thenAccept(communityUser -> MSMinecraftMain.getInstance().getLogger().info(proxiedPlayer.getName() + "'s community data: " + communityUser.toString()));
    }
    
    @EventHandler
    public void on(@NotNull ServerSwitchEvent e) {
        ProxiedPlayer proxiedPlayer = e.getPlayer();
        CommunityDAO.updateLastServer(proxiedPlayer).thenAccept(v -> MSMinecraftMain.getInstance().getLogger().info(proxiedPlayer.getName() + " switched server and its community data has been updated."));
    }
}