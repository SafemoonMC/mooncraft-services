package mc.mooncraft.services.minecraft.bungee.api;

import mc.mooncraft.services.datamodels.NetworkCounters;
import mc.mooncraft.services.datamodels.NetworkPlayers;
import org.jetbrains.annotations.NotNull;

public interface MSMinecraft {
    
    @NotNull NetworkPlayers getNetworkPlayers();
    @NotNull NetworkCounters getNetworkCounters();
    
}