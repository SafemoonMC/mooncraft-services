package gg.mooncraft.services.datamodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public final class NetworkServers {
    
    /*
    Fields
     */
    private final @NotNull Map<String, ServerGroup> serverGroupMap;
    
    /*
    Methods
     */
    @UnmodifiableView
    public @NotNull Map<String, ServerGroup> getServerGroupMap() {
        return Collections.unmodifiableMap(this.serverGroupMap);
    }
    
    /*
    Records
     */
    public record ServerGroup(@NotNull String display, @NotNull List<String> serverList) {
    }
}