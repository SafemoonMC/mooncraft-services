package gg.mooncraft.services.restfulweb.daos.bedwars.stats;

import lombok.AllArgsConstructor;
import lombok.Getter;

import me.eduardwayland.mooncraft.waylander.database.entities.EntityChild;

import org.jetbrains.annotations.NotNull;

import gg.mooncraft.services.restfulweb.daos.bedwars.user.UserStatisticContainer;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@AllArgsConstructor
public final class OverallStatistic implements EntityChild<UserStatisticContainer> {

    /*
    Fields
     */
    private final @NotNull UserStatisticContainer parent;
    private final @NotNull StatisticTypes.OVERALL type;
    private final @NotNull AtomicInteger amount;


    /*
    Override Methods
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OverallStatistic that = (OverallStatistic) o;
        return getType() == that.getType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType());
    }
}