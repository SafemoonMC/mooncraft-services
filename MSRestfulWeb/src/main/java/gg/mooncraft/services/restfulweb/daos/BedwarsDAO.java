package gg.mooncraft.services.restfulweb.daos;

import gg.mooncraft.services.restfulweb.models.BedwarsData;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class BedwarsDAO {
    
    public static @NotNull CompletableFuture<BedwarsData> loadBedwarsData(@NotNull UUID uniqueId) {
        return CompletableFuture.completedFuture(new BedwarsData());
    }
}