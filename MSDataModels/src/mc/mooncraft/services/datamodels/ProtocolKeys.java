package mc.mooncraft.services.datamodels;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class ProtocolKeys {
    
    /*
    Constants
     */
    public static final @NotNull String BUNGEE = "mooncraft-services:bungee";
    public static final @NotNull String BUNGEE_NETWORK_PLAYERS = "network-players";
    public static final @NotNull String BUNGEE_NETWORK_COUNTERS = "network-counters";
    public static final @NotNull String REDIS_NETWORK_PLAYERS = "mooncraft-services:network-players";
    public static final @NotNull String REDIS_NETWORK_COUNTERS = "mooncraft-services:network-counters";
}