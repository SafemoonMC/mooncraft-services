package mc.mooncraft.services.minecraft.bungee.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.Map;

@Getter
@AllArgsConstructor
public final class NetworkCounters {
    
    /*
    Fields
     */
    private final long totalOnlinePlayers;
    private final Map<String, Long> serverOnlinePlayers;
    private final long timestamp = System.currentTimeMillis();
    
    /*
    Methods
     */
    @UnmodifiableView
    public @NotNull Map<String, Long> getServerOnlinePlayers() {
        return Collections.unmodifiableMap(this.serverOnlinePlayers);
    }
}