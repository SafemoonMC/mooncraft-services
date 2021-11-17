package mc.mooncraft.services.datamodels;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
public final class NetworkCounters {
    
    /*
    Fields
     */
    private final @NotNull Map<String, Long> serversCounterMap;
    private final long totalOnlinePlayers;
    private final long timestamp = System.currentTimeMillis();
    
    /*
    Constructors
     */
    public NetworkCounters() {
        this.serversCounterMap = new HashMap<>();
        this.totalOnlinePlayers = -1;
    }
    
    public NetworkCounters(@NotNull Map<String, Long> serversCounterMap, long totalOnlinePlayers) {
        this.serversCounterMap = serversCounterMap;
        this.totalOnlinePlayers = totalOnlinePlayers;
    }
    
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
    
    /*
    Override Methods
     */
    @Override
    public String toString() {
        return "NetworkCounters{" +
                "map=" + serversCounterMap +
                ", total-online-players=" + totalOnlinePlayers +
                ", timestamp=" + timestamp +
                '}';
    }
}