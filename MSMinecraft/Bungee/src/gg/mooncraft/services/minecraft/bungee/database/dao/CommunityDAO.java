package gg.mooncraft.services.minecraft.bungee.database.dao;

import gg.mooncraft.services.minecraft.bungee.MSMinecraftMain;
import gg.mooncraft.services.minecraft.bungee.database.objects.CommunityUser;
import gg.mooncraft.services.minecraft.bungee.database.objects.CommunityUserHistory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.eduardwayland.mooncraft.waylander.database.queries.Query;
import me.eduardwayland.mooncraft.waylander.database.resultset.ResultSetWrapper;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class CommunityDAO {
    
    /*
    Constants
     */
    private static final @NotNull String TABLE = "msm_community";
    
    /*
    Methods
     */
    public static @NotNull CompletableFuture<CommunityUser> handle(@NotNull ProxiedPlayer proxiedPlayer) {
        Query query = Query.single("SELECT * FROM " + TABLE + " WHERE unique_id = ?;")
                .with(proxiedPlayer.getUniqueId().toString())
                .build();
        return MSMinecraftMain.getInstance().getDatabase().getDatabaseManager().executeQuery(query, resultSetIterator -> {
            if (!resultSetIterator.hasNext()) {
                return insert(new CommunityUser(proxiedPlayer.getUniqueId(), proxiedPlayer.getName(), null, null, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), 1, proxiedPlayer.getServer().getInfo().getName())).join();
            }
            ResultSetWrapper resultSetWrapper = new ResultSetWrapper(resultSetIterator.next());
            String username = resultSetWrapper.get("username", String.class);
            String discordId = resultSetWrapper.get("discord_id", String.class);
            String walletId = resultSetWrapper.get("wallet_id", String.class);
            Timestamp firstJoin = resultSetWrapper.get("first_join", Timestamp.class);
            long totalJoins = resultSetWrapper.get("total_joins", Long.class);
            String lastServer = resultSetWrapper.get("last_server", String.class);
            CommunityUser communityUser = new CommunityUser(proxiedPlayer.getUniqueId(), proxiedPlayer.getName(), discordId, walletId, firstJoin, Timestamp.from(Instant.now()), totalJoins + 1, lastServer);
            
            // Check if player changed his username and update the history if so
            if (!username.equals(proxiedPlayer.getName())) {
                CommunityHistoryDAO.insert(new CommunityUserHistory(proxiedPlayer.getUniqueId(), username, Timestamp.from(Instant.now()))).join();
            }
            
            return update(communityUser).join();
        });
    }
    
    public static @NotNull CompletableFuture<CommunityUser> insert(@NotNull CommunityUser communityUser) {
        Query query = Query.single("INSERT INTO " + TABLE + " (unique_id, username, first_join, last_join, last_server) VALUES (?, ?, ?, ?);")
                .with(communityUser.getUniqueId().toString())
                .with(communityUser.getUsername())
                .with(communityUser.getFirstJoin())
                .with(communityUser.getLastJoin())
                .with(communityUser.getLastServer())
                .build();
        return MSMinecraftMain.getInstance().getDatabase().getDatabaseManager().updateQuery(query, result -> communityUser);
    }
    
    public static @NotNull CompletableFuture<CommunityUser> update(@NotNull CommunityUser communityUser) {
        Query query = Query.single("UPDATE " + TABLE + " SET username = ?, last_join = ?, total_joins = ?, last_server = ? WHERE unique_id = ?;")
                .with(communityUser.getUsername())
                .with(communityUser.getLastJoin())
                .with(communityUser.getTotalJoins())
                .with(communityUser.getLastServer())
                .with(communityUser.getUniqueId().toString())
                .build();
        return MSMinecraftMain.getInstance().getDatabase().getDatabaseManager().updateQuery(query, result -> communityUser);
    }
    
    public static @NotNull CompletableFuture<Void> updateLastServer(@NotNull ProxiedPlayer proxiedPlayer) {
        Query query = Query.single("UPDATE " + TABLE + " SET last_server = ? WHERE unique_id = ?;")
                .with(proxiedPlayer.getServer().getInfo())
                .with(proxiedPlayer.getUniqueId().toString())
                .build();
        return MSMinecraftMain.getInstance().getDatabase().getDatabaseManager().updateQuery(query);
    }
}