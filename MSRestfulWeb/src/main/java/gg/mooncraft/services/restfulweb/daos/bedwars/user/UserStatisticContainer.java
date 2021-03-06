package gg.mooncraft.services.restfulweb.daos.bedwars.user;

import lombok.Getter;

import me.eduardwayland.mooncraft.waylander.database.entities.EntityChild;
import me.eduardwayland.mooncraft.waylander.database.entities.EntityParent;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.services.restfulweb.daos.UserStatisticDAO;
import gg.mooncraft.services.restfulweb.daos.bedwars.game.GameMode;
import gg.mooncraft.services.restfulweb.daos.bedwars.stats.GameStatistic;
import gg.mooncraft.services.restfulweb.daos.bedwars.stats.OverallStatistic;
import gg.mooncraft.services.restfulweb.daos.bedwars.stats.StatisticTypes;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Getter
public final class UserStatisticContainer implements EntityChild<BedWarsUser>, EntityParent<UserStatisticContainer> {

    /*
    Fields
     */
    private final @NotNull BedWarsUser parent;
    private final @NotNull List<GameStatistic> gameStatisticList;
    private final @NotNull List<OverallStatistic> overallStatisticList;

    /*
    Constructor
     */
    public UserStatisticContainer(@NotNull BedWarsUser parent) {
        this.parent = parent;
        this.gameStatisticList = new ArrayList<>();
        this.overallStatisticList = new ArrayList<>();
    }

    /*
    Methods
     */
    public @NotNull BigInteger getGameStatistic(@NotNull GameMode gameMode, @NotNull StatisticTypes.GAME type) {
        return this.gameStatisticList
                .stream()
                .filter(gameStatistic -> gameStatistic.getGameMode() == gameMode && gameStatistic.getType() == type)
                .findFirst()
                .map(GameStatistic::getAmount)
                .map(atomicInteger -> BigInteger.valueOf(atomicInteger.longValue()))
                .orElse(BigInteger.ZERO);
    }

    public @NotNull BigInteger getGameStatisticTotal(@NotNull StatisticTypes.GAME type) {
        return this.gameStatisticList
                .stream()
                .filter(gameStatistic -> gameStatistic.getType() == type)
                .map(GameStatistic::getAmount)
                .map(atomicInteger -> BigInteger.valueOf(atomicInteger.longValue()))
                .reduce(BigInteger.ZERO, BigInteger::add);
    }

    public @NotNull BigInteger getOverallStatistic(@NotNull StatisticTypes.OVERALL type) {
        return this.overallStatisticList
                .stream()
                .filter(overallStatistic -> overallStatistic.getType() == type)
                .findFirst()
                .map(OverallStatistic::getAmount)
                .map(atomicInteger -> BigInteger.valueOf(atomicInteger.longValue()))
                .orElse(BigInteger.ZERO);
    }

    /*
    Override Methods
     */
    @Override
    public @NotNull CompletableFuture<UserStatisticContainer> withChildren() {
        CompletableFuture<?> futureGame = UserStatisticDAO.readGame(this).thenAccept(this.gameStatisticList::addAll);
        CompletableFuture<?> futureOverall = UserStatisticDAO.readOverall(this).thenAccept(this.overallStatisticList::addAll);
        return CompletableFuture.allOf(futureGame, futureOverall).thenApply(v -> this);
    }
}