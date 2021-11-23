package gg.mooncraft.services.restfulweb.daos;

import gg.mooncraft.services.restfulweb.ApplicationBootstrap;
import gg.mooncraft.services.restfulweb.models.PlayerData;
import gg.mooncraft.services.restfulweb.models.player.PlayerLoginData;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.eduardwayland.mooncraft.waylander.database.queries.Query;
import me.eduardwayland.mooncraft.waylander.database.resultset.ResultSetWrapper;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class PlayerDAO {
    
    /*
    Constants
     */
    private static final @NotNull List<String> RANKS = List.of("group.owner", "group.manager", "group.developer", "group.administrator", "group.srmoderator", "group.moderator", "group.helper", "group.media", "group.diamondhands", "group.trader", "group.investor", "group.holder", "group.default");
    
    /*
    Methods
     */
    public static @NotNull CompletableFuture<PlayerData> getPlayerData(@NotNull String username) {
        Query query = Query.single("SELECT * FROM misc.msm_community WHERE username = ?;")
                .with(username)
                .build();
        return ApplicationBootstrap.getApplication().getDatabase().getDatabaseManager().executeQuery(query, resultSetIterator -> {
            if (resultSetIterator == null || !resultSetIterator.hasNext()) return null;
            ResultSetWrapper resultSetWrapper = new ResultSetWrapper(resultSetIterator.next());
            
            UUID uniqueId = UUID.fromString(resultSetWrapper.get("unique_id", String.class));
            PlayerLoginData playerLoginData = getPlayerLoginData(resultSetWrapper);
            return new PlayerData(uniqueId, username, playerLoginData).withChildren().join();
        });
    }
    
    public static @NotNull CompletableFuture<PlayerData> getPlayerData(@NotNull UUID uniqueId) {
        Query query = Query.single("SELECT * FROM misc.msm_community WHERE unique_id = ?;")
                .with(uniqueId.toString())
                .build();
        return ApplicationBootstrap.getApplication().getDatabase().getDatabaseManager().executeQuery(query, resultSetIterator -> {
            if (resultSetIterator == null || !resultSetIterator.hasNext()) return null;
            ResultSetWrapper resultSetWrapper = new ResultSetWrapper(resultSetIterator.next());
            
            String username = resultSetWrapper.get("username", String.class);
            PlayerLoginData playerLoginData = getPlayerLoginData(resultSetWrapper);
            
            return new PlayerData(uniqueId, username, playerLoginData).withChildren().join();
        });
    }
    
    public static @NotNull CompletableFuture<String> getPlayerRank(@NotNull UUID uniqueId) {
        Query query = Query.single("SELECT * FROM perms.luckperms_user_permissions WHERE uuid = ? AND permission LIKE 'group.%';")
                .with(uniqueId.toString())
                .build();
        return ApplicationBootstrap.getApplication().getDatabase().getDatabaseManager().executeQuery(query, resultSetIterator -> {
            if (resultSetIterator == null || !resultSetIterator.hasNext()) {
                return RANKS.get(RANKS.size() - 1);
            }
            List<String> groupList = new ArrayList<>();
            resultSetIterator.forEachRemaining(resultSet -> {
                ResultSetWrapper resultSetWrapper = new ResultSetWrapper(resultSet);
                groupList.add(resultSetWrapper.get("permission", String.class));
            });
            groupList.removeIf(rank -> !RANKS.contains(rank));
            groupList.sort((o1, o2) -> {
                int p1Idx = RANKS.indexOf(o1);
                int p2Idx = RANKS.indexOf(o2);
                if (p1Idx == p2Idx) return 0;
                else return RANKS.indexOf(o1) > RANKS.indexOf(o2) ? 1 : -1;
            });
            String highestRank = groupList.stream().findFirst().orElse(RANKS.get(RANKS.size() - 1));
            return highestRank.replaceFirst("group.", "");
        });
    }
    
    private static @NotNull PlayerLoginData getPlayerLoginData(@NotNull ResultSetWrapper resultSetWrapper) {
        Timestamp firstJoin = resultSetWrapper.get("first_join", Timestamp.class);
        Timestamp lastJoin = resultSetWrapper.get("last_join", Timestamp.class);
        int totalJoins = resultSetWrapper.get("total_joins", Integer.class);
        String lastServer = resultSetWrapper.get("last_server", String.class);
        
        return new PlayerLoginData(firstJoin, lastJoin, totalJoins, lastServer);
    }
}