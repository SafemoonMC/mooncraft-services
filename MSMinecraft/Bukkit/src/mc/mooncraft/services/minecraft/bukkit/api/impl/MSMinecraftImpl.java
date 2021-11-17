package mc.mooncraft.services.minecraft.bukkit.api.impl;

import mc.mooncraft.services.datamodels.NetworkCounters;
import mc.mooncraft.services.minecraft.bukkit.api.MSMinecraft;
import mc.mooncraft.services.minecraft.bukkit.factories.NetworkCountersFactory;
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