package gg.mooncraft.services.restfulweb.daos;

import gg.mooncraft.services.restfulweb.ApplicationBootstrap;
import gg.mooncraft.services.restfulweb.models.OGPrisonData;
import gg.mooncraft.services.restfulweb.models.OPPrisonData;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.eduardwayland.mooncraft.waylander.database.queries.Query;
import me.eduardwayland.mooncraft.waylander.database.resultset.ResultSetWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class PrisonDAO {
    
    /*
    Constants
     */
    private static final @NotNull String[] oppRanks = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "Guard"};
    private static final @NotNull String[] ogpRanks = {"Earth", "Mars", "Venus", "Jupiter", "Mercury", "Uranus", "Saturn", "Neptune"};
    
    /*
    Methods
     */
    public static @NotNull CompletableFuture<OGPrisonData> loadOGPrisonData(@NotNull UUID uniqueId) {
        Query query = Query.single("SELECT ranks.id_rank, ranks.id_prestige, blocks.blocks FROM ogprison.UltraPrison_Ranks ranks JOIN ogprison.UltraPrison_BlocksBroken blocks USING (UUID) WHERE UUID = ?")
                .with(uniqueId.toString())
                .build();
        return ApplicationBootstrap.getApplication().getDatabase().getDatabaseManager().executeQuery(query, resultSetIterator -> {
            if (resultSetIterator == null || !resultSetIterator.hasNext()) {
                return new OGPrisonData();
            }
            ResultSetWrapper resultSetWrapper = new ResultSetWrapper(resultSetIterator.next());
            return new OGPrisonData(resultSetWrapper.get("blocks.blocks", Integer.class), resultSetWrapper.get("ranks.id_prestige", Integer.class), ogpRanks[resultSetWrapper.get("ranks.id_rank", Integer.class) - 1]);
        });
    }
    
    public static @NotNull CompletableFuture<OPPrisonData> loadOPPrisonData(@NotNull UUID uniqueId) {
        Query query = Query.single("SELECT ranks.id_rank, ranks.id_prestige, blocks.blocks FROM opprison.UltraPrison_Ranks ranks JOIN opprison.UltraPrison_BlocksBroken blocks USING (UUID) WHERE UUID = ?")
                .with(uniqueId.toString())
                .build();
        return ApplicationBootstrap.getApplication().getDatabase().getDatabaseManager().executeQuery(query, resultSetIterator -> {
            if (resultSetIterator == null || !resultSetIterator.hasNext()) {
                return new OPPrisonData();
            }
            ResultSetWrapper resultSetWrapper = new ResultSetWrapper(resultSetIterator.next());
            return new OPPrisonData(resultSetWrapper.get("blocks.blocks", Integer.class), resultSetWrapper.get("ranks.id_prestige", Integer.class), oppRanks[resultSetWrapper.get("ranks.id_rank", Integer.class) - 1]);
        });
    }
}