package gg.mooncraft.services.restfulweb.factories;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import gg.mooncraft.services.restfulweb.ApplicationBootstrap;
import gg.mooncraft.services.restfulweb.daos.PlayerDAO;
import gg.mooncraft.services.restfulweb.models.PlayerData;
import gg.mooncraft.services.restfulweb.utilities.StringUtilities;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public final class PlayersFactory {
    
    /*
    Fields
     */
    private final @NotNull LoadingCache<String, CompletableFuture<PlayerData>> playerDataCache;
    
    /*
    Constructor
     */
    public PlayersFactory() {
        Caffeine<Object, Object> caffeineBuilder = Caffeine.newBuilder();
        int evictionSize = ApplicationBootstrap.getApplication().getProperties().map(propertiesWrapper -> propertiesWrapper.getPropertyInteger("restapi.cache.eviction-size", -1)).orElse(-1);
        if (evictionSize == -1) caffeineBuilder.maximumSize(evictionSize);
        int evictionTime = ApplicationBootstrap.getApplication().getProperties().map(propertiesWrapper -> propertiesWrapper.getPropertyInteger("restapi.cache.eviction-time", 10)).orElse(10);
        caffeineBuilder.expireAfterWrite(evictionTime, TimeUnit.SECONDS);
        this.playerDataCache = caffeineBuilder.build(this::loadPlayerData);
    }
    
    /*
    Methods
     */
    public @NotNull CompletableFuture<PlayerData> getPlayerData(@NotNull String parameter) {
        CompletableFuture<PlayerData> futureCache = playerDataCache.get(parameter);
        if (futureCache == null) return CompletableFuture.completedFuture(null);
        return futureCache;
    }
    
    private @NotNull CompletableFuture<PlayerData> loadPlayerData(@NotNull String playerParameter) {
        UUID uniqueId = StringUtilities.parseUniqueId(playerParameter);
        if (uniqueId != null) return PlayerDAO.getPlayerData(uniqueId);
        else return PlayerDAO.getPlayerData(playerParameter);
    }
}