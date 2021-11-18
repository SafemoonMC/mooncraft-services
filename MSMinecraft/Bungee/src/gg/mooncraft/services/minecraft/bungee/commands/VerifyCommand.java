package gg.mooncraft.services.minecraft.bungee.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.jetbrains.annotations.NotNull;

public class VerifyCommand extends Command {
    
    /*
    Constructor
     */
    public VerifyCommand() {
        super("verify");
    }
    
    /*
    Override Methods
     */
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof ProxiedPlayer proxiedPlayer)) {
            commandSender.sendMessage(TextComponent.fromLegacyText("You cannot execute this command by console!"));
            return;
        }
        
        if (args.length == 1) {
            String type = args[0];
            if (type.equalsIgnoreCase("discord")) {
                proxiedPlayer.sendMessage(new ComponentBuilder("+---------------------------+").color(ChatColor.DARK_GRAY).strikethrough(true).create());
                proxiedPlayer.sendMessage(new ComponentBuilder("Please join the Discord and run the").color(ChatColor.AQUA).create());
                proxiedPlayer.sendMessage(new ComponentBuilder("!verify command in the bot channel").color(ChatColor.AQUA).create());
                proxiedPlayer.sendMessage(new ComponentBuilder("https://discord.gg/mooncraftofficial/").color(ChatColor.AQUA).create());
                proxiedPlayer.sendMessage(new ComponentBuilder("+---------------------------+").color(ChatColor.DARK_GRAY).strikethrough(true).create());
                return;
            } else if (type.equalsIgnoreCase("wallet")) {
                proxiedPlayer.sendMessage(new ComponentBuilder("+---------------------------+").color(ChatColor.DARK_GRAY).strikethrough(true).create());
                proxiedPlayer.sendMessage(new ComponentBuilder("This feature is coming soon!").color(ChatColor.AQUA).create());
                proxiedPlayer.sendMessage(new ComponentBuilder("https://mc.safemoon.net/verify?uuid=" + proxiedPlayer.getUniqueId().toString()).color(ChatColor.AQUA).create());
                proxiedPlayer.sendMessage(new ComponentBuilder("+---------------------------+").color(ChatColor.DARK_GRAY).strikethrough(true).create());
                return;
            }
        }
        
        wrongUsage(proxiedPlayer);
    }
    
    /*
    Methods
     */
    private void wrongUsage(@NotNull ProxiedPlayer proxiedPlayer) {
        proxiedPlayer.sendMessage(new ComponentBuilder("Please verify usage: /verify <discord, wallet>").color(ChatColor.AQUA).create());
    }
}