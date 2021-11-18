package gg.mooncraft.services.minecraft.bungee.database.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Getter
@AllArgsConstructor
public final class CommunityUser {
    
    /*
    Fields
     */
    private final @NotNull UUID uniqueId;
    private final @NotNull String username;
    private final @Nullable String discordId;
    private final @Nullable String walletId;
    private final @NotNull Timestamp firstJoin;
    private final @NotNull Timestamp lastJoin;
    private final long totalJoins;
    private final @NotNull String lastServer;
    
    /*
    Override Methods
     */
    @Override
    @Contract(pure = true)
    public @NotNull String toString() {
        return "CommunityUser{" +
                "uniqueId=" + uniqueId +
                ", username='" + username + '\'' +
                ", discordId='" + discordId + '\'' +
                ", walletId='" + walletId + '\'' +
                ", firstJoin=" + firstJoin +
                ", lastJoin=" + lastJoin +
                ", totalJoins=" + totalJoins +
                ", lastServer='" + lastServer + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommunityUser that = (CommunityUser) o;
        return getUniqueId().equals(that.getUniqueId());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(getUniqueId());
    }
}