package mc.mooncraft.services.minecraft.bukkit.papi;

import mc.mooncraft.services.minecraft.bukkit.MSMinecraftMain;
import mc.mooncraft.services.minecraft.bukkit.api.MSMinecraftProvider;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * This PlaceholderAPI expansion creates the following placeholders:
 * %msminecraft_players-[group]% -> it gets all the servers which name starts with [group] and returns the total players online
 */
public class PlayersExpansion extends PlaceholderExpansion {
    
    /*
    Constructor
     */
    public PlayersExpansion() {
        register();
    }
    
    
    /*
    Override Methods
     */
    @Override
    public @NotNull String onPlaceholderRequest(Player player, @NotNull String params) {
        if (!params.contains("-")) return "syntax error";
        String[] args = params.split("-");
        if (args.length == 2) {
            String type = args[0];
            if (type.equalsIgnoreCase("players")) {
                String content = args[1];
                long counter = MSMinecraftProvider.get().getNetworkCounters().getServersCounterMap().entrySet()
                        .stream()
                        .filter(entry -> entry.getKey().startsWith(content))
                        .mapToLong(Map.Entry::getValue)
                        .sum();
                return String.valueOf(counter);
            }
        }
        return "syntax error";
    }
    
    @Override
    public @NotNull String getIdentifier() {
        return "msminecraft";
    }
    
    @Override
    public @NotNull String getAuthor() {
        return "Eduard Wayland @ eduardwaland@gmail.com";
    }
    
    @Override
    public @NotNull String getVersion() {
        return MSMinecraftMain.getInstance().getDescription().getVersion();
    }
    
    @Override
    public boolean persist() {
        return true;
    }
    
    @Override
    public boolean canRegister() {
        return true;
    }
}