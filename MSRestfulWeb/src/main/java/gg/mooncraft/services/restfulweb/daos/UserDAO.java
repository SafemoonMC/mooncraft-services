package gg.mooncraft.services.restfulweb.daos;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import me.eduardwayland.mooncraft.waylander.database.queries.Query;
import me.eduardwayland.mooncraft.waylander.database.resultset.ResultSetWrapper;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.services.restfulweb.ApplicationBootstrap;
import gg.mooncraft.services.restfulweb.daos.bedwars.user.BedWarsUser;

import java.math.BigInteger;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class UserDAO {

    /*
    Constants
     */
    private static final @NotNull String TABLE_NAME = "bedwars.bw_users";

    /*
    Static Methods
     */
    public static @NotNull CompletableFuture<BedWarsUser> read(@NotNull UUID uniqueId) {
        Objects.requireNonNull(ApplicationBootstrap.getApplication().getDatabase(), "The DAO hasn't been registered yet.");
        Query query = Query.single("SELECT * FROM " + TABLE_NAME + " WHERE unique_id = ?;")
                .with(uniqueId.toString())
                .build();
        return ApplicationBootstrap.getApplication().getDatabase().getDatabaseManager().executeQuery(query, resultSetIterator -> {
            if (resultSetIterator == null || !resultSetIterator.hasNext()) {
                return new BedWarsUser(uniqueId);
            }
            ResultSetWrapper resultSetWrapper = new ResultSetWrapper(resultSetIterator.next());
            BigInteger coins = resultSetWrapper.get("coins", BigInteger.class);
            BigInteger experience = resultSetWrapper.get("experience", BigInteger.class);
            return new BedWarsUser(uniqueId, coins, experience).withChildren().join();
        });
    }
}