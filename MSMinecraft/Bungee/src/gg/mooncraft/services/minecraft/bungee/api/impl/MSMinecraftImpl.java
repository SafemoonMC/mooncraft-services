package gg.mooncraft.services.minecraft.bungee.api.impl;

import gg.mooncraft.services.datamodels.NetworkCounters;
import gg.mooncraft.services.datamodels.NetworkPlayers;
import gg.mooncraft.services.minecraft.bungee.api.MSMinecraft;
import gg.mooncraft.services.minecraft.bungee.factories.NetworkCountersFactory;
import gg.mooncraft.services.minecraft.bungee.factories.NetworkPlayersFactory;
import org.jetbrains.annotations.NotNull;

public final class MSMinecraftImpl implements MSMinecraft {
    
    /*
    Fields
     */
    private final @NotNull NetworkPlayersFactory networkPlayersFactory;
    private final @NotNull NetworkCountersFactory networkCountersFactory;
    
    /*
    Constructor
     */
    public MSMinecraftImpl(@NotNull NetworkPlayersFactory networkPlayersFactory, @NotNull NetworkCountersFactory networkCountersFactory) {
        this.networkPlayersFactory = networkPlayersFactory;
        this.networkCountersFactory = networkCountersFactory;
    }
    
    /*
    Override Methods
     */
    @Override
    public @NotNull NetworkPlayers getNetworkPlayers() {
        return this.networkPlayersFactory.getNetworkPlayers();
    }
    
    @Override
    public @NotNull NetworkCounters getNetworkCounters() {
        return this.networkCountersFactory.getNetworkCounters();
    }
}