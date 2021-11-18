package gg.mooncraft.services.minecraft.bungee.config;

import gg.mooncraft.services.minecraft.bungee.utilities.ResourceUtility;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@Getter
public final class BungeeConfiguration {
    
    
    /*
    Fields
     */
    private final @NotNull Plugin plugin;
    private final @NotNull Configuration configuration;
    
    /*
    Constructor
     */
    public BungeeConfiguration(@NotNull Plugin plugin) {
        this.plugin = plugin;
        File correctFile = new File(plugin.getDataFolder(), "config.yml");
        if (!correctFile.exists()) {
            File originalFile = ResourceUtility.loadResource(plugin, "bungee-config.yml");
            if (originalFile == null) throw new IllegalStateException("bungee-config.yml resource cannot be created");
            boolean renamed = originalFile.renameTo(correctFile);
            if (!renamed) throw new IllegalStateException("bungee-config.yml cannot be renamed to config.yml");
        }
        Configuration configuration = ResourceUtility.loadConfig(correctFile);
        if (configuration == null) throw new IllegalStateException("No configuration can be loaded from config.yml");
        this.configuration = configuration;
    }
}