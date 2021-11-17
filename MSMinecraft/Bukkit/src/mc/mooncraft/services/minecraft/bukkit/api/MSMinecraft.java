package mc.mooncraft.services.minecraft.bukkit.api;

import mc.mooncraft.services.datamodels.NetworkCounters;
import org.jetbrains.annotations.NotNull;

public interface MSMinecraft {
    
    @NotNull NetworkCounters getNetworkCounters();
    
}