package gg.mooncraft.services.minecraft.bukkit.factories;

import lombok.Getter;
import gg.mooncraft.services.datamodels.NetworkCounters;
import org.jetbrains.annotations.NotNull;

@Getter
public final class NetworkCountersFactory {
    
    /*
    Fields
     */
    private @NotNull NetworkCounters networkCounters;
    
    /*
    Constructor
     */
    public NetworkCountersFactory() {
        this.networkCounters = new NetworkCounters();
    }
    
    /*
    Methods
     */
    public void update(@NotNull NetworkCounters networkCounters) {
        if (this.networkCounters.getTimestamp() > networkCounters.getTimestamp()) return;
        this.networkCounters = networkCounters;
    }
    
}