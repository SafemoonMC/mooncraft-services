package mc.mooncraft.services.minecraft.bungee.api.impl;

import mc.mooncraft.services.minecraft.bungee.api.MSMinecraft;
import mc.mooncraft.services.minecraft.bungee.factories.NetworkCountersFactory;
import mc.mooncraft.services.minecraft.bungee.factories.NetworkPlayersFactory;
import mc.mooncraft.services.minecraft.bungee.model.NetworkCounters;
import mc.mooncraft.services.minecraft.bungee.model.NetworkPlayers;
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