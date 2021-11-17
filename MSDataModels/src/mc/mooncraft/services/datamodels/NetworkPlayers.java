package mc.mooncraft.services.datamodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;

@Getter
@AllArgsConstructor
public final class NetworkPlayers {
    
    /*
    Fields
     */
    private final @NotNull Map<String, List<UUID>> serversMap;
    private final long timestamp = System.currentTimeMillis();
    
    /*
    Methods
     */
    @UnmodifiableView
    public @NotNull Map<String, List<UUID>> getServersMap() {
        return Collections.unmodifiableMap(this.serversMap);
    }
    
    @UnmodifiableView
    public @NotNull List<UUID> getServerPlayersList(@NotNull String server) {
        return Collections.unmodifiableList(this.serversMap.getOrDefault(server, new ArrayList<>()));
    }
    
    public @NotNull Optional<String> getPlayerServer(@NotNull UUID playerUniqueId) {
        return serversMap.entrySet().stream().filter(entry -> entry.getValue().contains(playerUniqueId)).findFirst().map(Map.Entry::getKey);
    }
}