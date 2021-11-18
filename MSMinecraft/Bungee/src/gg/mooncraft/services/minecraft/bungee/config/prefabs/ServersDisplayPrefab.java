package gg.mooncraft.services.minecraft.bungee.config.prefabs;

import gg.mooncraft.services.minecraft.bungee.MSMinecraftMain;
import gg.mooncraft.services.minecraft.bungee.config.BungeeConfiguration;
import lombok.Getter;
import net.md_5.bungee.config.Configuration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public final class ServersDisplayPrefab {
    
    /*
    Fields
     */
    private final @NotNull List<ServerDisplay> serverDisplayList;
    
    /*
    Constructor
     */
    public ServersDisplayPrefab(@NotNull BungeeConfiguration bungeeConfiguration) {
        this.serverDisplayList = new ArrayList<>();
        Configuration configuration = bungeeConfiguration.getConfiguration().getSection("servers-display");
        for (String key : configuration.getKeys()) {
            String display = configuration.getString(key).trim();
            this.serverDisplayList.add(new ServerDisplay(key.toLowerCase(), display));
        }
        MSMinecraftMain.getInstance().getLogger().info(this.serverDisplayList.size() + " elements have been loaded from servers-display.");
    }
    
    /*
    Methods
     */
    public @NotNull Optional<ServerDisplay> getServerDisplay(@NotNull String serverName) {
        return this.serverDisplayList.stream().filter(serverDisplay -> serverDisplay.matches(serverName)).findFirst();
    }
    
    /*
    Records
     */
    public record ServerDisplay(@NotNull String serverGroup, @NotNull String display) {
        public boolean matches(@NotNull String serverName) {
            return serverName.toLowerCase().startsWith(serverGroup);
        }
    }
}