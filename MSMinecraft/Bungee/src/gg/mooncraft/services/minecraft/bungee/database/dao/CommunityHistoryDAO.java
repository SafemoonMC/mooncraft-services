package gg.mooncraft.services.minecraft.bungee.database.dao;

import gg.mooncraft.services.minecraft.bungee.MSMinecraftMain;
import gg.mooncraft.services.minecraft.bungee.database.objects.CommunityUserHistory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.eduardwayland.mooncraft.waylander.database.queries.Query;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class CommunityHistoryDAO {
    
    /*
    Constants
     */
    private static final @NotNull String TABLE = "msm_community";
    
    /*
    Methods
     */
    public static @NotNull CompletableFuture<CommunityUserHistory> insert(@NotNull CommunityUserHistory communityUserHistory) {
        Query query = Query.single("INSERT INTO " + TABLE + " (unique_id, username, change_time) VALUES (?, ?, ?);")
                .with(communityUserHistory.getUniqueId().toString())
                .with(communityUserHistory.getUsername())
                .with(communityUserHistory.getChangeTimestamp())
                .build();
        return MSMinecraftMain.getInstance().getDatabase().getDatabaseManager().updateQuery(query, result -> communityUserHistory);
    }
}