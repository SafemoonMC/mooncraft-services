package mc.mooncraft.services.datamodels;

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
    private final Map<String, Long> serversCounterMap;
    private final long timestamp = System.currentTimeMillis();
    
    /*
    Methods
     */
    @UnmodifiableView
    public @NotNull Map<String, Long> getServersCounterMap() {
        return Collections.unmodifiableMap(this.serversCounterMap);
    }
    
    @UnmodifiableView
    public long getServerCounter(@NotNull String serverName) {
        return serversCounterMap.getOrDefault(serverName, 0L);
    }
}