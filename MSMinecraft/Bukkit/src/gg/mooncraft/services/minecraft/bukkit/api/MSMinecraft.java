package gg.mooncraft.services.minecraft.bukkit.api;

import gg.mooncraft.services.datamodels.NetworkCounters;
import org.jetbrains.annotations.NotNull;

public interface MSMinecraft {
    
    @NotNull NetworkCounters getNetworkCounters();
    
}