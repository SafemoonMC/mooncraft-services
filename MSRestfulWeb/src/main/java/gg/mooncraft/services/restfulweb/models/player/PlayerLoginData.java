package gg.mooncraft.services.restfulweb.models.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public final class PlayerLoginData {
    
    /*
    Fields
     */
    private final @NotNull Timestamp firstJoin;
    private final @NotNull Timestamp lastJoin;
    private final long totalJoins;
    private final @NotNull String lastServerJoined;
    
    /*
    Override Methods
     */
    @Override
    @Contract(pure = true)
    public @NotNull String toString() {
        return "CommunityUser{" +
                "firstJoin=" + firstJoin +
                ", lastJoin=" + lastJoin +
                ", totalJoins=" + totalJoins +
                ", lastServerJoined='" + lastServerJoined + '\'' +
                '}';
    }
}