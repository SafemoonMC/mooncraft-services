package gg.mooncraft.services.restfulweb.daos;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import me.eduardwayland.mooncraft.waylander.database.queries.Query;
import me.eduardwayland.mooncraft.waylander.database.resultset.ResultSetWrapper;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.services.restfulweb.ApplicationBootstrap;
import gg.mooncraft.services.restfulweb.daos.bedwars.game.GameMode;
import gg.mooncraft.services.restfulweb.daos.bedwars.stats.GameStatistic;
import gg.mooncraft.services.restfulweb.daos.bedwars.stats.OverallStatistic;
import gg.mooncraft.services.restfulweb.daos.bedwars.stats.StatisticTypes;
import gg.mooncraft.services.restfulweb.daos.bedwars.user.UserStatisticContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class UserStatisticDAO {

    /*
    Constants
     */
    private static final @NotNull String GAME_TABLE_NAME = "bedwars.bw_users_stats_games";
    private static final @NotNull String OVERALL_TABLE_NAME = "bedwars.bw_users_stats_overall";

    /*
    Static Methods
     */
    public static @NotNull CompletableFuture<List<GameStatistic>> readGame(@NotNull UserStatisticContainer userStatisticContainer) {
        Objects.requireNonNull(ApplicationBootstrap.getApplication().getDatabase(), "The DAO hasn't been registered yet.");
        Query query = Query.single("SELECT * FROM " + GAME_TABLE_NAME + " WHERE unique_id = ?;")
                .with(userStatisticContainer.getParent().getUniqueId().toString())
                .build();
        return ApplicationBootstrap.getApplication().getDatabase().getDatabaseManager().executeQuery(query, resultSetIterator -> {
            if (resultSetIterator == null || !resultSetIterator.hasNext()) {
                return new ArrayList<>();
            }
            List<GameStatistic> list = new ArrayList<>();
            resultSetIterator.forEachRemaining(resultSet -> {
                ResultSetWrapper resultSetWrapper = new ResultSetWrapper(resultSet);
                GameMode mode = GameMode.valueOf(resultSetWrapper.get("mode", String.class));
                StatisticTypes.GAME type = StatisticTypes.GAME.valueOf(resultSetWrapper.get("name", String.class));
                AtomicInteger amount = new AtomicInteger(resultSetWrapper.get("amount", Integer.class));
                list.add(new GameStatistic(userStatisticContainer, mode, type, amount));
            });
            return list;
        });
    }

    public static @NotNull CompletableFuture<List<OverallStatistic>> readOverall(@NotNull UserStatisticContainer userStatisticContainer) {
        Objects.requireNonNull(ApplicationBootstrap.getApplication().getDatabase(), "The DAO hasn't been registered yet.");
        Query query = Query.single("SELECT * FROM " + OVERALL_TABLE_NAME + " WHERE unique_id = ?;")
                .with(userStatisticContainer.getParent().getUniqueId().toString())
                .build();
        return ApplicationBootstrap.getApplication().getDatabase().getDatabaseManager().executeQuery(query, resultSetIterator -> {
            if (resultSetIterator == null || !resultSetIterator.hasNext()) {
                return new ArrayList<>();
            }
            List<OverallStatistic> list = new ArrayList<>();
            resultSetIterator.forEachRemaining(resultSet -> {
                ResultSetWrapper resultSetWrapper = new ResultSetWrapper(resultSet);
                StatisticTypes.OVERALL type = StatisticTypes.OVERALL.valueOf(resultSetWrapper.get("name", String.class));
                AtomicInteger amount = new AtomicInteger(resultSetWrapper.get("amount", Integer.class));
                list.add(new OverallStatistic(userStatisticContainer, type, amount));
            });
            return list;
        });
    }
}