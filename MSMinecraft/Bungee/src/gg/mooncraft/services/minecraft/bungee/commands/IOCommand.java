package gg.mooncraft.services.minecraft.bungee.commands;

import gg.mooncraft.services.minecraft.bungee.MSMinecraftMain;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class IOCommand extends Command {
    
    /*
    Constructor
     */
    public IOCommand(@NotNull String name, @Nullable String permission) {
        super(name, permission);
        ProxyServer.getInstance().getPluginManager().registerCommand(MSMinecraftMain.getInstance(), this);
    }
    
    /*
    Override Methods
     */
    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        MSMinecraftMain.getInstance().getConfigurationPrefabs().getCommunityInputOutputPrefab().getInputOutput(getName())
                .ifPresent(inputOutput -> inputOutput.message()
                        .forEach(line -> commandSender.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', line))))
                );
    }
}