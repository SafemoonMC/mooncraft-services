package gg.mooncraft.services.minecraft.bungee.config;

import gg.mooncraft.services.minecraft.bungee.config.prefabs.CommunityInputOutputPrefab;
import gg.mooncraft.services.minecraft.bungee.config.prefabs.ServersDisplayPrefab;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public final class ConfigurationPrefabs {
    
    /*
    Fields
     */
    private final @NotNull ServersDisplayPrefab serversDisplayPrefab;
    private final @NotNull CommunityInputOutputPrefab communityInputOutputPrefab;
    
    /*
    Constructor
     */
    public ConfigurationPrefabs(@NotNull BungeeConfiguration bungeeConfiguration) {
        this.serversDisplayPrefab = new ServersDisplayPrefab(bungeeConfiguration);
        this.communityInputOutputPrefab = new CommunityInputOutputPrefab(bungeeConfiguration);
    }
}