package gg.mooncraft.services.restfulweb.daos.bedwars.user;

import lombok.Getter;

import me.eduardwayland.mooncraft.waylander.database.entities.EntityParent;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.services.restfulweb.daos.bedwars.prestige.ExpCalculator;
import gg.mooncraft.services.restfulweb.daos.bedwars.prestige.Prestige;

import java.math.BigInteger;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Getter
public final class BedWarsUser implements EntityParent<BedWarsUser> {

    /*
    Fields
     */
    private final @NotNull UUID uniqueId;
    private @NotNull BigInteger coins;
    private @NotNull BigInteger experience;
    private @NotNull UserStatisticContainer statisticContainer;

    /*
    Constructor
     */
    public BedWarsUser(@NotNull UUID uniqueId) {
        this(uniqueId, BigInteger.ZERO, BigInteger.ZERO);
    }

    public BedWarsUser(@NotNull UUID uniqueId, @NotNull BigInteger coins, @NotNull BigInteger experience) {
        this.uniqueId = uniqueId;
        this.coins = coins;
        this.experience = experience;
        this.statisticContainer = new UserStatisticContainer(this);
    }

    /*
    Methods
     */
    public int getLevel() {
        return ExpCalculator.getLevelForExperience(getExperience());
    }

    public int getNextLevel() {
        return getLevel() + 1;
    }

    public long getExperience() {
        return this.experience.longValue();
    }

    public @NotNull Prestige getPrestige() {
        return ExpCalculator.getPrestigeForExperience(getExperience());
    }

    public @NotNull Prestige getNextPrestige() {
        return ExpCalculator.getPrestigeForLevel(getNextLevel());
    }

    /*
    Override Methods
     */
    @Override
    public @NotNull CompletableFuture<BedWarsUser> withChildren() {
        CompletableFuture<?> statisticFuture = statisticContainer.withChildren().thenAccept(userStatisticContainer -> this.statisticContainer = userStatisticContainer);
        return CompletableFuture.allOf(statisticFuture).thenApply(v -> this);
    }
}