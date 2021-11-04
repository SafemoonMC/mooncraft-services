package mc.mooncraft.services.minecraft.bungee.api;

import mc.mooncraft.services.minecraft.bungee.model.NetworkCounters;
import mc.mooncraft.services.minecraft.bungee.model.NetworkPlayers;
import org.jetbrains.annotations.NotNull;

public interface MSMinecraft {
    
    @NotNull NetworkPlayers getNetworkPlayers();
    @NotNull NetworkCounters getNetworkCounters();
    
}