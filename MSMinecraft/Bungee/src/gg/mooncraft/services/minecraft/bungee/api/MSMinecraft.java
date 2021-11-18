package gg.mooncraft.services.minecraft.bungee.api;

import gg.mooncraft.services.datamodels.NetworkCounters;
import gg.mooncraft.services.datamodels.NetworkPlayers;
import org.jetbrains.annotations.NotNull;

public interface MSMinecraft {
    
    @NotNull NetworkPlayers getNetworkPlayers();
    @NotNull NetworkCounters getNetworkCounters();
    
}