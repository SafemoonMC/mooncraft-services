package gg.mooncraft.services.minecraft.bukkit.api.impl;

import gg.mooncraft.services.datamodels.NetworkCounters;
import gg.mooncraft.services.minecraft.bukkit.factories.NetworkCountersFactory;
import gg.mooncraft.services.minecraft.bukkit.api.MSMinecraft;
import org.jetbrains.annotations.NotNull;

public final class MSMinecraftImpl implements MSMinecraft {
    
    /*
    Fields
     */
    private final @NotNull NetworkCountersFactory networkCountersFactory;
    
    /*
    Constructor
     */
    public MSMinecraftImpl(@NotNull NetworkCountersFactory networkCountersFactory) {
        this.networkCountersFactory = networkCountersFactory;
    }
    
    /*
    Override Methods
     */
    @Override
    public @NotNull NetworkCounters getNetworkCounters() {
        return this.networkCountersFactory.getNetworkCounters();
    }
}