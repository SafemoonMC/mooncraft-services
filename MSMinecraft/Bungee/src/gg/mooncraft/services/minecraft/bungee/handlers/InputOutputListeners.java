package gg.mooncraft.services.minecraft.bungee.handlers;

import gg.mooncraft.services.minecraft.bungee.MSMinecraftMain;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;

public class InputOutputListeners implements Listener {
    
    /*
    Constructor
     */
    public InputOutputListeners() {
        ProxyServer.getInstance().getPluginManager().registerListener(MSMinecraftMain.getInstance(), this);
    }
    
    /*
    Handlers
     */
    @EventHandler
    public void on(@NotNull ChatEvent e) {
        if (!(e.getSender() instanceof ProxiedPlayer proxiedPlayer)) return;
        if (e.isCommand()) return;
        String command = e.getMessage().replaceFirst("/", "");
        MSMinecraftMain.getInstance().getConfigurationPrefabs().getCommunityInputOutputPrefab().getInputOutput(command).ifPresent(inputOutput -> {
            if (!inputOutput.hasPermission(proxiedPlayer)) {
                proxiedPlayer.sendMessage(TextComponent.fromLegacyText("§fUnknown command. Type \"/help\" for help."));
                return;
            }
            inputOutput.message().forEach(line -> proxiedPlayer.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', line))));
        });
    }
}